(ns jina.api.segment-text
  (:require [jina.util :refer [jina-api-request]]))

(defn call
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


#_(call "This is a sample text that will be tokenized and segmented into meaningful chunks for processing.")

;; output
#_{:num_tokens 18,
   :tokens ["This", "is", "a", "sample", "text", "that", "will", "be", "tokenized", "and", "segmented", "into", "meaningful", "chunks", "for", "processing", ".", ""]}


#_(call "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed do eiusmod tempor incididunt ut labore et dolore magna aliqua."
        {:return_chunks true
         :max_chunk_length 50
         :return_tokens true})

;; output with chunks and tokens
#_{:num_tokens 23,
   :tokens ["Lorem", "ipsum", "dolor", "sit", "amet", ",", "consectetur", "adipiscing", "elit", ".", "Sed", "do", "eiusmod", "tempor", "incididunt", "ut", "labore", "et", "dolore", "magna", "aliqua", ".", ""],
   :chunks [{:text "Lorem ipsum dolor sit amet, consectetur adipiscing",
             :start_index 0,
             :end_index 49}
            {:text "elit. Sed do eiusmod tempor incididunt ut labore",
             :start_index 50,
             :end_index 96}]}
