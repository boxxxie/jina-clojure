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

;; API functions
(def jina-embeddings embeddings/jina-embeddings)
(def jina-rerank rerank/jina-rerank)
(def jina-classify classify/jina-classify)
(def jina-deep-search deep-search/jina-deep-search)
(def jina-read-url read-url/jina-read-url)
(def jina-search-web search-web/jina-search-web)
(def jina-segment-text segment-text/jina-segment-text)
