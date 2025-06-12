(ns jina.core
  (:require [jina.api.embeddings :as embeddings]
            [jina.api.rerank :as rerank]
            [jina.api.read-url :as read-url]
            [jina.api.search-web :as search-web]
            [jina.api.deep-search :as deep-search]
            [jina.api.segment-text :as segment-text]
            [jina.api.classify :as classify]))

(def jina-embeddings embeddings/jina-embeddings)
(def jina-rerank rerank/jina-rerank)
(def jina-read-url read-url/jina-read-url)
(def jina-search-web search-web/jina-search-web)
(def jina-deep-search deep-search/jina-deep-search)
(def jina-segment-text segment-text/jina-segment-text)
(def jina-classify classify/jina-classify)
