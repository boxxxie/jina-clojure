(ns jina.api.embeddings
  (:require [jina.util :refer [jina-api-request]]))

(defn jina-embeddings
  "Converts text/images to fixed-length vectors using Jina AI Embeddings API.

  Input:
  - `input`: A vector of strings or objects to be embedded.
  - `opts`: An optional map of additional parameters:
    - `:model` (string, default: \"jina-embeddings-v3\"): Identifier of the model to use (e.g., jina-embeddings-v3).
    - `:embedding_type` (string, default: \"float\"): The format of the returned embeddings (float, base64, binary, ubinary).
    - `:task` (string): Specifies the intended downstream application to optimize embedding output (retrieval.query, retrieval.passage, text-matching, classification, separation).
    - `:dimensions` (integer): Truncates output embeddings to the specified size if set.
    - `:normalized` (boolean, default: false): If true, embeddings are normalized to unit L2 norm.
    - `:late_chunking` (boolean, default: false): If true, concatenates all sentences in input and treats as a single input for late chunking.
    - `:truncate` (boolean, default: false): If true, the model will automatically drop the tail that extends beyond the maximum context length allowed by the model instead of throwing an error."
  [input & opts]
  (let [body (merge {:input input} (first opts))]
    (jina-api-request "/embeddings" body)))
