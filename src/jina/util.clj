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
  ([endpoint body]
   (jina-api-request endpoint body {}))
  ([endpoint body extra-headers]
   (let [api-key    (get-api-key)
         url        (str jina-api-base-url endpoint)
         start-time (System/nanoTime)
         headers    (merge {"Authorization" (str "Bearer " api-key)
                            "Content-Type"  "application/json"}
                           extra-headers)]
     (try
       (let [response    (http/post url
                                    {:headers          headers
                                     :body             (json/encode body)
                                     :throw-exceptions false})
             end-time    (System/nanoTime)
             duration-ms (/ (- end-time start-time) 1000000.0)]
         (if (< (:status response) 400)
           (let [parsed-response (json/decode (:body response) keyword)]
             (assoc parsed-response :execution_time_ms duration-ms))
           (throw (ex-info (str "HTTP " (:status response) ": " (:body response))
                           {:status (:status response) :body (:body response)}))))
       (catch Exception e
         (println "Error making Jina API request:" (.getMessage e))
         (throw e))))))

(defn jina-reader-request
  "Request function for Jina Reader API which uses GET with URL pattern https://r.jina.ai/"
  [url opts]
  (let [api-key    (get-api-key)
        reader-url (str "https://r.jina.ai/" url)
        start-time (System/nanoTime)
        headers    (merge {"Authorization" (str "Bearer " api-key)
                           "Accept" "application/json"}
                          opts)]
    (try
      (let [response    (http/get reader-url
                                  {:headers          headers
                                   :throw-exceptions false})
            end-time    (System/nanoTime)
            duration-ms (/ (- end-time start-time) 1000000.0)]
        (if (< (:status response) 400)
          (let [parsed-response (json/decode (:body response) keyword)]
            (assoc parsed-response :execution_time_ms duration-ms))
          (throw (ex-info (str "HTTP " (:status response) ": " (:body response))
                          {:status (:status response) :body (:body response)}))))
      (catch Exception e
        (println "Error making Jina Reader API request:" (.getMessage e))
        (throw e)))))

(defn jina-search-request
  "Request function for Jina Search API which uses GET with URL pattern https://s.jina.ai/"
  [query opts]
  (let [api-key    (get-api-key)
        search-url (str "https://s.jina.ai/" query)
        start-time (System/nanoTime)
        headers    (merge {"Authorization" (str "Bearer " api-key)
                           "Accept" "application/json"
                           "X-Respond-With" "no-content"}
                          opts)]
    (try
      (let [response    (http/get search-url
                                  {:headers          headers
                                   :throw-exceptions false})
            end-time    (System/nanoTime)
            duration-ms (/ (- end-time start-time) 1000000.0)]
        (if (< (:status response) 400)
          (let [parsed-response (json/decode (:body response) keyword)]
            (assoc parsed-response :execution_time_ms duration-ms))
          (throw (ex-info (str "HTTP " (:status response) ": " (:body response))
                          {:status (:status response) :body (:body response)}))))
      (catch Exception e
        (println "Error making Jina Search API request:" (.getMessage e))
        (throw e)))))
