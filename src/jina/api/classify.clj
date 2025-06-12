(ns jina.api.classify
  (:require [jina.util :refer [jina-api-request]]))



;; jina-embeddings-v3 input example EDN
(def example-jina-embeddings-v3
  {:model  "jina-embeddings-v3"
   :input  ["Calculate the compound interest on a principal of $10,000 invested for 5 years at an annual rate of 5%, compounded quarterly."
            "分析使用CRISPR基因编辑技术在人类胚胎中的伦理影响。考虑潜在的医疗益处和长期社会后果。"
            "AIが自意識を持つディストピアの未来を舞台にした短編小説を書いてください。人間とAIの関係や意識の本質をテーマに探求してください。"
            "Erklären Sie die Unterschiede zwischen Merge-Sort und Quicksort-Algorithmen in Bezug auf Zeitkomplexität, Platzkomplexität und Leistung in der Praxis."
            "Write a poem about the beauty of nature and its healing power on the human soul."
            "Translate the following sentence into French: The quick brown fox jumps over the lazy dog."]
   :labels ["Simple task"
            "Complex reasoning"
            "Creative writing"]})

;; jina-clip-v2 input example EDN
(def example-jina-clip-v2
  {:model  "jina-clip-v2"
   :input  [{:text "A sleek smartphone with a high-resolution display and multiple camera lenses"}
            {:text "Fresh sushi rolls served on a wooden board with wasabi and ginger"}
            {:image "https://picsum.photos/id/11/367/267"}
            {:image "https://picsum.photos/id/22/367/267"}
            {:text "Vibrant autumn leaves in a dense forest with sunlight filtering through"}
            {:image "https://picsum.photos/id/8/367/267"}]
   :labels ["Technology and Gadgets"
            "Food and Dining"
            "Nature and Outdoors"
            "Urban and Architecture"]})

(defn- validate-input
  "Validates input format based on the model being used."
  [model input]
  (let [has-text?  (some #(contains? % :text) input)
        has-image? (some #(contains? % :image) input)]
    (cond
      (and has-text? has-image?)
      (throw (ex-info "Cannot mix text and image objects in the same request"
                      {:input input :model model}))

      (and (= model "jina-clip-v2") (not has-image?))
      (throw (ex-info "jina-clip-v2 model requires image inputs"
                      {:input input :model model}))

      (and (= model "jina-embeddings-v3") has-image?)
      (throw (ex-info "jina-embeddings-v3 model does not support image inputs"
                      {:input input :model model}))

      :else true)))

(validate-input "jina-embeddings-v3" example-jina-embeddings-v3)
(validate-input "jina-clip-v2" example-jina-clip-v2)

(defn call
  "Zero-shot classification for text or images using Jina AI Classifier API.

  Model-specific input requirements:
  - `jina-embeddings-v3` (default): For text classification only
    - Input format: Vector of strings or text objects with \"text\" key
    - Example: [\"I love this product!\"] or [{\"text\" \"I love this product!\"}]

  - `jina-clip-v2`: For image classification (can also handle text)
    - Input format: Vector of objects with \"image\" or \"text\" keys
    - Image format: Base64 encoded string or URL
    - Example: [{\"image\" \"data:image/jpeg;base64,...\"}] or [{\"text\" \"A red car\"}]
    - Note: Cannot mix text and image objects in the same request

  Input:
  - `input`: Array of inputs for classification. Format depends on model:
    - For jina-embeddings-v3: Vector of strings or text objects
    - For jina-clip-v2: Vector of image/text objects (cannot mix types)
  - `labels`: List of labels used for classification.
  - `opts`: An optional map of additional parameters:
    - `:model` (string, default: \"jina-embeddings-v3\", enum: \"jina-clip-v2\", \"jina-embeddings-v3\"):
      Identifier of the model to use. jina-embeddings-v3 for text, jina-clip-v2 for images.
    - `:classifier_id` (string): The identifier of the classifier. If not provided, a new classifier will be created."
  [input labels & opts]
  (let [default-opts {:model "jina-embeddings-v3"}
        merged-opts  (merge default-opts (first opts))
        model        (:model merged-opts)]
    (validate-input input model)
    (let [body (merge {:input input :labels labels} merged-opts)]
      (jina-api-request "/classify") body)))


#_(call
    ["I love this new smartphone! The camera quality is amazing."
     "The delivery was delayed and the package was damaged."]
    ["positive" "negative" "neutral"])

;; output
#_{:usage {:total_tokens 38}, :data [{:object "classification", :index 0, :prediction "positive", :score 0.3556700050830841, :predictions [{:label "positive", :score 0.3556700050830841} {:label "negative", :score 0.3169832229614258} {:label "neutral", :score 0.3273467719554901}]} {:object "classification", :index 1, :prediction "negative", :score 0.3376658260822296, :predictions [{:label "positive", :score 0.32943376898765564} {:label "negative", :score 0.3376658260822296} {:label "neutral", :score 0.33290040493011475}]}]}

;; output
#_{:data [{:index      0,
           :input      {"text" "I love this new smartphone! The camera quality is amazing."},
           :prediction [{:label "positive", :score 0.92}
                        {:label "neutral", :score 0.06}
                        {:label "negative", :score 0.02}]}
          {:index      1,
           :input      {"text" "The delivery was delayed and the package was damaged."},
           :prediction [{:label "negative", :score 0.89}
                        {:label "neutral", :score 0.08}
                        {:label "positive", :score 0.03}]}]}


#_(call [{"image" "data:image/jpeg;base64,/9j/4AAQSkZJRgABAQAAAQ..."}]
        ["cat" "dog" "bird" "car"]
        {:model "jina-clip-v2"})

;; output for image classification
#_{:data [{:index      0,
           :input      {"image" "data:image/jpeg;base64,/9j/4AAQSkZJRgABAQAAAQ..."},
           :prediction [{:label "cat", :score 0.87}
                        {:label "dog", :score 0.09}
                        {:label "bird", :score 0.03}
                        {:label "car", :score 0.01}]}]}
