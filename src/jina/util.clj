(ns jina.util
  (:require [babashka.http-client :as http]
            [cheshire.core :as json]))

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
      (-> (http/post url
                     {:headers {"Authorization" (str "Bearer " api-key)
                                "Content-Type" "application/json"}
                      :body (json/generate-string body)})
          :body
          (json/parse-string true))
      (catch Exception e
        (println "Error making Jina API request:" (.getMessage e))
        (throw e)))))
