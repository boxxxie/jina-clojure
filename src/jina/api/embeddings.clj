(ns jina.api.embeddings
  (:require [jina.util :refer [jina-api-request]]))

(defn call
  "Converts text/images to fixed-length vectors using Jina AI Embeddings API.

  Input:
  - `input`: A vector of strings or objects to be embedded.
  - `opts`: An optional map of additional parameters:
    - `:model` (string, default: \"jina-embeddings-v3\"): Identifier of the model to use (e.g., jina-embeddings-v3). **Required by API - defaults to jina-embeddings-v3 if not specified.**
    - `:embedding_type` (string, default: \"float\"): The format of the returned embeddings (float, base64, binary, ubinary).
    - `:task` (string): Specifies the intended downstream application to optimize embedding output (retrieval.query, retrieval.passage, text-matching, classification, separation).
    - `:dimensions` (integer): Truncates output embeddings to the specified size if set.
    - `:normalized` (boolean, default: false): If true, embeddings are normalized to unit L2 norm.
    - `:late_chunking` (boolean, default: false): If true, concatenates all sentences in input and treats as a single input for late chunking.
    - `:truncate` (boolean, default: false): If true, the model will automatically drop the tail that extends beyond the maximum context length allowed by the model instead of throwing an error."
  [input & opts]
  (let [default-opts {:model "jina-embeddings-v3"}
        body         (merge {:input input} default-opts (first opts))]
    (jina-api-request "/embeddings" body)))


#_(call ["Hello, world!"])

;; output
#_{
   :model  "jina-embeddings-v3",
   :object "list",
   :usage  {:total_tokens 6, :prompt_tokens 6},
   :data   [
            {:object    "embedding",
             :index     0,
             :embedding [0.11442348 -0.11122073 0.13124809 ...]}
            ]
   }


#_(call ["Hello, world!" "there is no spoon"] {:dimensions 10})

;; output
#_{:model             "jina-embeddings-v3",
   :object            "list",
   :usage             {:total_tokens 13, :prompt_tokens 13},
   :data              [{:object    "embedding",
                        :index     0,
                        :embedding [0.39906722
                                    -0.38789722
                                    0.45774525
                                    0.06643874
                                    0.27025834
                                    -0.39918035
                                    -0.1370097
                                    0.41541225
                                    -0.062082075
                                    -0.22081324]}
                       {:object    "embedding",
                        :index     1,
                        :embedding [0.22698139
                                    -0.17781383
                                    0.41603658
                                    -0.08164376
                                    0.3316518
                                    -0.08299294
                                    -0.22000621
                                    0.70784235
                                    0.13058735
                                    0.23180217]}],
   :execution_time_ms 1382.003666}
