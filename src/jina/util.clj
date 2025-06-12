(ns jina.util
  (:require [cheshire.core :as json]
            [clj-http.client :as http]))

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
        url (str jina-api-base-url endpoint)
        start-time (System/nanoTime)]
    (try
      (let [response (http/post url
                               {:headers {"Authorization" (str "Bearer " api-key)
                                         "Content-Type" "application/json"}
                                :body (json/write-str body)
                                :throw-exceptions false})
            end-time (System/nanoTime)
            duration-ms (/ (- end-time start-time) 1000000.0)]
        (if (< (:status response) 400)
          (let [parsed-response (json/read-str (:body response) :key-fn keyword)]
            (assoc parsed-response :execution_time_ms duration-ms))
          (throw (ex-info (str "HTTP " (:status response) ": " (:body response))
                         {:status (:status response) :body (:body response)}))))
      (catch Exception e
        (println "Error making Jina API request:" (.getMessage e))
        (throw e)))))
