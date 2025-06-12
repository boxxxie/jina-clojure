(ns jina.api.rerank
  (:require [jina.util :refer [jina-api-request]]))

(defn jina-rerank
  "Re-rank search results using Jina AI Reranker API.

  Input:
  - `query`: The search query string.
  - `documents`: A vector of strings, TextDocs, and/or images to rerank.
  - `opts`: An optional map of additional parameters:
    - `:model` (string, default: \"jina-reranker-v2-base-multilingual\"): Identifier of the model to use (e.g., jina-reranker-v2-base-multilingual).
    - `:top_n` (integer): The number of most relevant documents or indices to return, defaults to the length of documents.
    - `:return_documents` (boolean, default: true): If false, returns only the index and relevance score without the document text. If true, returns the index, text, and relevance score."
  [query documents & opts]
  (let [body (merge {:query query :documents documents} (first opts))]
    (jina-api-request "/rerank" body)))
