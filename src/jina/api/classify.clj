(ns jina.api.classify
  (:require [jina.util :refer [jina-api-request]]))

(defn jina-classify
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
