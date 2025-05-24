(ns jina-clj-api.core
  (:require [jina-clj-api.api.embeddings :as embeddings]
            [jina-clj-api.api.rerank :as rerank]
            [jina-clj-api.api.read-url :as read-url]
            [jina-clj-api.api.search-web :as search-web]
            [jina-clj-api.api.deep-search :as deep-search]
            [jina-clj-api.api.segment-text :as segment-text]
            [jina-clj-api.api.classify :as classify]))

(def jina-embeddings embeddings/jina-embeddings)
(def jina-rerank rerank/jina-rerank)
(def jina-read-url read-url/jina-read-url)
(def jina-search-web search-web/jina-search-web)
(def jina-deep-search deep-search/jina-deep-search)
(def jina-segment-text segment-text/jina-segment-text)
(def jina-classify classify/jina-classify)
