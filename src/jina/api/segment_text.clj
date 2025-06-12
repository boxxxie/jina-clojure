(ns jina.api.segment-text
  (:require [jina.util :refer [jina-api-request]]))

(defn jina-segment-text
  "Tokenizes text and divides text into chunks using Jina AI Segmenter API.

  Input:
  - `content`: The text content to segment.
  - `opts`: An optional map of additional parameters:
    - `:tokenizer` (string, default: \"cl100k_base\"): Specifies the tokenizer to use (cl100k_base, o200k_base, p50k_base, r50k_base, p50k_edit, gpt2).
    - `:return_tokens` (boolean, default: false): If true, includes tokens and their IDs in the response.
    - `:return_chunks` (boolean, default: false): If true, segments the text into semantic chunks.
    - `:max_chunk_length` (integer, default: 1000): Maximum characters per chunk (only effective if return_chunks is true).
    - `:head` (integer): Returns the first N tokens (exclusive with tail).
    - `:tail` (integer): Returns the last N tokens (exclusive with head)."
  [content & opts]
  (let [body (merge {:content content} (first opts))]
    (jina-api-request "/segment-text" body)))
