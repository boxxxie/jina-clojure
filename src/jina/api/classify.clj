(ns jina.api.classify
  (:require [jina.util :refer [jina-api-request]]))



;; jina-embeddings-v3 input example JSON
{
 "model"  : "jina-embeddings-v3",
 "input"  : [
             "Calculate the compound interest on a principal of $10,000 invested for 5 years at an annual rate of 5%, compounded quarterly.",
             "分析使用CRISPR基因编辑技术在人类胚胎中的伦理影响。考虑潜在的医疗益处和长期社会后果。",
             "AIが自意識を持つディストピアの未来を舞台にした短編小説を書いてください。人間とAIの関係や意識の本質をテーマに探求してください。",
             "Erklären Sie die Unterschiede zwischen Merge-Sort und Quicksort-Algorithmen in Bezug auf Zeitkomplexität, Platzkomplexität und Leistung in der Praxis.",
             "Write a poem about the beauty of nature and its healing power on the human soul.",
             "Translate the following sentence into French: The quick brown fox jumps over the lazy dog."
             ],
 "labels" : [
             "Simple task",
             "Complex reasoning",
             "Creative writing"
             ]
 }

;; jina-clip-v2 input example JSON
{
 "model"  : "jina-clip-v2",
 "input"  : [
             {
              "text" : "A sleek smartphone with a high-resolution display and multiple camera lenses"
              },
             {
              "text" : "Fresh sushi rolls served on a wooden board with wasabi and ginger"
              },
             {
              "image" : "https://picsum.photos/id/11/367/267"
              },
             {
              "image" : "https://picsum.photos/id/22/367/267"
              },
             {
              "text" : "Vibrant autumn leaves in a dense forest with sunlight filtering through"
              },
             {
              "image" : "https://picsum.photos/id/8/367/267"
              }
             ],
 "labels" : [
             "Technology and Gadgets",
             "Food and Dining",
             "Nature and Outdoors",
             "Urban and Architecture"
             ]
 }

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
#_{:data [{:index 0,
           :input {"image" "data:image/jpeg;base64,/9j/4AAQSkZJRgABAQAAAQ..."},
           :prediction [{:label "cat", :score 0.87}
                        {:label "dog", :score 0.09}
                        {:label "bird", :score 0.03}
                        {:label "car", :score 0.01}]}]}
