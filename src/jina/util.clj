(ns jina.util
  (:require [clojure.data.json :as json]))

(def ^:private jina-api-base-url "https://api.jina.ai/v1")

(defn get-api-key []
  (if-let [api-key (System/getenv "JINA_API_KEY")]
    (do
      (println "JINA_API_KEY found.")
      api-key)
    (do
      (println "JINA_API_KEY not found. Please set the JINA_API_KEY environment variable.")
      (throw (ex-info "JINA_API_KEY environment variable not set." {:error :api-key-missing})))))

(defn jina-api-request
  [endpoint body]
  (let [api-key (get-api-key)
        url (str jina-api-base-url endpoint)]
    (try
      (let [conn (doto (.openConnection (java.net.URL. url))
                   (.setRequestMethod "POST")
                   (.setRequestProperty "Authorization" (str "Bearer " api-key))
                   (.setRequestProperty "Content-Type" "application/json")
                   (.setDoOutput true))
            json-body (json/write-str body)]
        (with-open [os (.getOutputStream conn)]
          (.write os (.getBytes json-body "UTF-8")))
        (let [response-code (.getResponseCode conn)]
          (if (< response-code 400)
            (with-open [is (.getInputStream conn)]
              (json/read-str (slurp is) :key-fn keyword))
            (with-open [es (.getErrorStream conn)]
              (let [error-msg (slurp es)]
                (throw (ex-info (str "HTTP " response-code ": " error-msg)
                               {:status response-code :body error-msg})))))))
      (catch Exception e
        (println "Error making Jina API request:" (.getMessage e))
        (throw e)))))
