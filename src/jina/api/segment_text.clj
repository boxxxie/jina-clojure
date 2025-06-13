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

;;output
{:num_tokens 18, :tokenizer "cl100k_base", :usage {:tokens 0}, :execution_time_ms 713.939601}


#_(call "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed do eiusmod tempor incididunt ut labore et dolore magna aliqua."
        {:return_chunks    true
         :max_chunk_length 50
         :return_tokens    true})

;; output with chunks and tokens
{:num_tokens        22,
 :tokenizer         "cl100k_base",
 :usage             {:tokens 0},
 :num_chunks        1,
 :chunk_positions   [[0 123]],
 :tokens            [[["Lorem" [33883]]
                      [" ipsum" [27439]]
                      [" dolor" [24578]]
                      [" sit" [2503]]
                      [" amet" [28311]]
                      ["," [11]]
                      [" consectetur" [36240]]
                      [" adipiscing" [59024]]
                      [" elit" [31160]]
                      ["." [13]]
                      [" Sed" [36378]]
                      [" do" [656]]
                      [" eiusmod" [80222]]
                      [" tempor" [19502]]
                      [" incididunt" [87504]]
                      [" ut" [8791]]
                      [" labore" [73304]]
                      [" et" [1880]]
                      [" dolore" [58396]]
                      [" magna" [60017]]
                      [" aliqua" [87027]]
                      ["." [13]]]],
 :chunks            ["Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed do eiusmod tempor incididunt ut labore et dolore magna aliqua."],
 :execution_time_ms 593.676664}
