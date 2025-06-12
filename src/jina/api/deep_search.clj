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
