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
  "Special request function for Jina Reader and Search APIs which use GET and different URL patterns"
  [url-or-query opts]
  (let [api-key    (get-api-key)
        ;; Determine if this is a search query or URL based on content
        is-search? (not (re-matches #"^https?://.*" url-or-query))
        base-url   (if is-search? "https://s.jina.ai/" "https://r.jina.ai/")
        full-url   (str base-url url-or-query)
        start-time (System/nanoTime)
        headers    (merge {"Authorization" (str "Bearer " api-key)
                           "Accept" "application/json"}
                          opts)]
    (try
      (let [response    (http/get full-url
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
        (println "Error making Jina Reader/Search API request:" (.getMessage e))
        (throw e)))))
