(ns jina.api.deep-search
  (:require [jina.util :refer [jina-api-request]]))

(defn call
  "Combines web searching, reading, and reasoning for comprehensive investigation using Jina AI DeepSearch API.

  Input:
  - `messages`: A list of messages between the user and the assistant comprising the conversation so far.
  - `opts`: An optional map of additional parameters:
    - `:model` (string, default: \"jina-deepsearch-v1\"): ID of the model to use.
    - `:stream` (boolean, default: true): Delivers events as they occur through server-sent events.
    - `:reasoning_effort` (string, enum: \"low\", \"medium\", \"high\"): Constrains effort on reasoning for reasoning models.
    - `:budget_tokens` (number): Maximum number of tokens allowed for DeepSearch process.
    - `:max_attempts` (number): The maximum number of retries for solving a problem in DeepSearch process.
    - `:no_direct_answer` (boolean): Forces the model to take further thinking/search steps even when the query seems trivial.
    - `:max_returned_urls` (number): The maximum number of URLs to include in the final answer/chunk.
    - `:response_format` (map): Ensures the final answer from the model will match your supplied JSON schema.
    - `:boost_hostnames` (vector of strings): A list of domains that are given a higher priority for content retrieval.
    - `:bad_hostnames` (vector of strings): A list of domains to be strictly excluded from content retrieval.
    - `:only_hostnames` (vector of strings): A list of domains to be exclusively included in content retrieval."
  [messages & opts]
  (let [body (merge {:messages messages} (first opts))]
    (jina-api-request "/deep-search" body)))


#_(call [{:role "user" :content "What are the latest developments in quantum computing?"}])

;; output
#_{:id "deepsearch-123",
   :object "deepsearch.completion",
   :created 1234567890,
   :model "jina-deepsearch-v1",
   :choices [{:index 0,
              :message {:role "assistant",
                        :content "Based on my research, here are the latest developments in quantum computing..."},
              :finish_reason "stop"}],
   :usage {:prompt_tokens 15, :completion_tokens 250, :total_tokens 265}}


#_(call [{:role "user" :content "Compare the performance of different machine learning frameworks"}]
        {:reasoning_effort "high"
         :max_returned_urls 5
         :boost_hostnames ["arxiv.org" "github.com"]})

;; output with enhanced reasoning and specific sources
#_{:id "deepsearch-456",
   :object "deepsearch.completion", 
   :created 1234567891,
   :model "jina-deepsearch-v1",
   :choices [{:index 0,
              :message {:role "assistant",
                        :content "After extensive research and analysis, here's a comprehensive comparison..."},
              :finish_reason "stop"}],
   :usage {:prompt_tokens 25, :completion_tokens 400, :total_tokens 425}}
