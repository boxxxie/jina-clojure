(ns jina.core
  (:require [jina.api.classify :as classify]
            [jina.api.deep-search :as deep-search]
            [jina.api.embeddings :as embeddings]
            [jina.api.read-url :as read-url]
            [jina.api.rerank :as rerank]
            [jina.api.search-web :as search-web]
            [jina.api.segment-text :as segment-text]
            [jina.util :as util]))

;; Utility functions
(def get-api-key util/get-api-key)

(def api-key (get-api-key))

;; API functions
(def embeddings   embeddings/call)
(def rerank       rerank/call)
(def classify     classify/call)
(def deep-search  deep-search/call)
(def read-url     read-url/call)
(def search-web   search-web/call)
(def segment-text segment-text/call)
