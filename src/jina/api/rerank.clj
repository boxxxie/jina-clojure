(ns jina.api.rerank
  (:require [jina.util :refer [jina-api-request]]))

(defn call
  "Re-rank search results using Jina AI Reranker API.

  Input:
  - `query`: The search query string.
  - `documents`: A vector of strings, TextDocs, and/or images to rerank.
  - `opts`: An optional map of additional parameters:
    - `:model` (string, default: \"jina-reranker-v2-base-multilingual\"): Identifier of the model to use (e.g., jina-reranker-v2-base-multilingual).
    - `:top_n` (integer): The number of most relevant documents or indices to return, defaults to the length of documents.
    - `:return_documents` (boolean, default: true): If false, returns only the index and relevance score without the document text. If true, returns the index, text, and relevance score."
  [query documents & opts]
  (let [default-opts {:model "jina-reranker-v2-base-multilingual"}
        body         (merge {:query query :documents documents} default-opts (first opts))]
    (jina-api-request "/rerank" body)))


#_(call "machine learning algorithms"
        ["Deep learning is a subset of machine learning"
         "Natural language processing uses statistical methods"
         "Computer vision applies machine learning to images"
         "Reinforcement learning trains agents through rewards"])

;; output
{:model             "jina-reranker-v2-base-multilingual",
 :usage             {:total_tokens 50},
 :results
 [{:index           0,
   :document        {:text "Deep learning is a subset of machine learning"},
   :relevance_score 0.23231014609336853}
  {:index           2,
   :document        {:text "Computer vision applies machine learning to images"},
   :relevance_score 0.16026602685451508}
  {:index           1,
   :document        {:text "Natural language processing uses statistical methods"},
   :relevance_score 0.09401018172502518}
  {:index           3,
   :document        {:text "Reinforcement learning trains agents through rewards"},
   :relevance_score 0.08509904146194458}],
 :execution_time_ms 1446.232782}
