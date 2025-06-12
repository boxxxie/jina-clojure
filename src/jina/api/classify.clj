(ns jina.api.classify
  (:require [jina.util :refer [jina-api-request]]))

(defn call
  "Zero-shot classification for text or images using Jina AI Classifier API.

  Input:
  - `input`: Array of inputs for classification. Each entry can either be a text object `{\"text\": \"your_text_here\"}` or an image object `{\"image\": \"base64_image_string\"}`. You cannot mix text and image objects in the same request.
  - `labels`: List of labels used for classification.
  - `opts`: An optional map of additional parameters:
    - `:model` (string, enum: \"jina-clip-v2\", \"jina-embeddings-v3\"): Identifier of the model to use (jina-clip-v2 for images, jina-embeddings-v3 for text).
    - `:classifier_id` (string): The identifier of the classifier. If not provided, a new classifier will be created."
  [input labels & opts]
  (let [body (merge {:input input :labels labels} (first opts))]
    (jina-api-request "/classify" body)))


#_(call [{"text" "I love this new smartphone! The camera quality is amazing."}
         {"text" "The delivery was delayed and the package was damaged."}]
        ["positive" "negative" "neutral"]
        {:model "jina-embeddings-v3"})

;; output
#_{:data [{:index 0,
           :input {"text" "I love this new smartphone! The camera quality is amazing."},
           :prediction [{:label "positive", :score 0.92}
                        {:label "neutral", :score 0.06}
                        {:label "negative", :score 0.02}]}
          {:index 1,
           :input {"text" "The delivery was delayed and the package was damaged."},
           :prediction [{:label "negative", :score 0.89}
                        {:label "neutral", :score 0.08}
                        {:label "positive", :score 0.03}]}]}


#_(call [{"image" "data:image/jpeg;base64,/9j/4AAQSkZJRgABAQAAAQ..."}]
        ["cat" "dog" "bird" "car"]
        {:model "jina-clip-v2"})

;; output for image classification
#_{:data [{:index 0,
           :input {"image" "data:image/jpeg;base64,/9j/4AAQSkZJRgABAQAAAQ..."},
           :prediction [{:label "cat", :score 0.87}
                        {:label "dog", :score 0.09}
                        {:label "bird", :score 0.03}
                        {:label "car", :score 0.01}]}]}
