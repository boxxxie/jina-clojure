(ns jina-clj-api.util
  (:require [clj-http.client :as client]
            [cheshire.core :as json]))

(def ^:private jina-api-base-url "https://api.jina.ai/v1")

(defn get-api-key []
  (or (System/getenv "JINA_API_KEY")
      (throw (ex-info "JINA_API_KEY environment variable not set." {:error :api-key-missing}))))

(defn jina-api-request
  [endpoint body]
  (let [api-key (get-api-key)
        url (str jina-api-base-url endpoint)]
    (try
      (-> (client/post url
                       {:headers {"Authorization" (str "Bearer " api-key)
                                  "Content-Type" "application/json"}
                        :body (json/generate-string body)
                        :as :json})
          :body)
      (catch Exception e
        (println "Error making Jina API request:" (.getMessage e))
        (throw e)))))
