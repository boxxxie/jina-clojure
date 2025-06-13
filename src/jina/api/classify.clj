(ns jina.api.classify
  (:require [jina.util :refer [jina-api-request]]
            [malli.core :as m]
            [malli.error :as me]
            [malli.dev.pretty :as mp]))



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

;; Malli schemas for input validation
(def jina-embeddings-v3-input-schema
  "Schema for jina-embeddings-v3 input: vector of strings only"
  [:vector :string])

(def jina-clip-v2-input-schema
  "Schema for jina-clip-v2 input: vector of objects with either text or image keys"
  [:vector [:or
            [:map [:text :string]]
            [:map [:image :string]]]])

(defn- validate-input
  "Validates input format based on the model being used."
  [model input]
  (case model
    "jina-embeddings-v3"
    (let [schema jina-embeddings-v3-input-schema]
      (when-not (m/validate schema input)
        (let [explanation (m/explain schema input)
              errors      (me/humanize explanation)]
          (mp/explain schema explanation)
          (throw (ex-info "jina-embeddings-v3 model requires input to be an array of strings only"
                          {:input input :model model :errors errors})))))

    "jina-clip-v2"
    (let [schema jina-clip-v2-input-schema]
      (when-not (m/validate schema input)
        (let [explanation (m/explain schema input)
              errors      (me/humanize explanation)]
          (mp/explain schema explanation)
          (throw (ex-info "jina-clip-v2 model requires input to be a vector of objects with either 'text' or 'image' keys"
                          {:input input :model model :errors errors})))))

    true))

;; Valid examples
#_(validate-input (:model example-jina-embeddings-v3) (:input example-jina-embeddings-v3))
#_(validate-input (:model example-jina-clip-v2) (:input example-jina-clip-v2))

;; InValid examples
#_(validate-input (:model example-jina-embeddings-v3) (:input example-jina-clip-v2))
#_(validate-input (:model example-jina-clip-v2) (:input example-jina-embeddings-v3))

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
  [{:keys [model input labels] :as params} & opts]
  (let [opts  (first opts)
        model (or model (:model opts) "jina-embeddings-v3")
        body  (-> (merge params opts)
                  (assoc :model model))]
    (validate-input model input)
    (jina-api-request "/classify" body)))

#_(call example-jina-embeddings-v3)

{:usage             {:total_tokens 196},
 :data
 [{:object     "classification",
   :index      0,
   :prediction "Simple task",
   :score      0.35217395424842834,
   :predictions
   [{:label "Simple task", :score 0.35217395424842834}
    {:label "Complex reasoning", :score 0.34130600094795227}
    {:label "Creative writing", :score 0.30652010440826416}]}
  {:object     "classification",
   :index      1,
   :prediction "Complex reasoning",
   :score      0.3431348502635956,
   :predictions
   [{:label "Simple task", :score 0.3242877125740051}
    {:label "Complex reasoning", :score 0.3431348502635956}
    {:label "Creative writing", :score 0.3325774073600769}]}
  {:object     "classification",
   :index      2,
   :prediction "Creative writing",
   :score      0.34869447350502014,
   :predictions
   [{:label "Simple task", :score 0.3321252465248108}
    {:label "Complex reasoning", :score 0.31918027997016907}
    {:label "Creative writing", :score 0.34869447350502014}]}
  {:object     "classification",
   :index      3,
   :prediction "Complex reasoning",
   :score      0.35215625166893005,
   :predictions
   [{:label "Simple task", :score 0.34568116068840027}
    {:label "Complex reasoning", :score 0.35215625166893005}
    {:label "Creative writing", :score 0.3021625876426697}]}
  {:object     "classification",
   :index      4,
   :prediction "Creative writing",
   :score      0.3638777732849121,
   :predictions
   [{:label "Simple task", :score 0.3311176300048828}
    {:label "Complex reasoning", :score 0.30500465631484985}
    {:label "Creative writing", :score 0.3638777732849121}]}
  {:object     "classification",
   :index      5,
   :prediction "Simple task",
   :score      0.3561651110649109,
   :predictions
   [{:label "Simple task", :score 0.3561651110649109}
    {:label "Complex reasoning", :score 0.326008677482605}
    {:label "Creative writing", :score 0.31782621145248413}]}],
 :execution_time_ms 592.978605}

#_(call example-jina-clip-v2)

{:usage             {:total_tokens 12083},
 :data
 [{:object     "classification",
   :index      0,
   :prediction "Technology and Gadgets",
   :score      0.2847042977809906,
   :predictions
   [{:label "Technology and Gadgets", :score 0.2847042977809906}
    {:label "Food and Dining", :score 0.2256934940814972}
    {:label "Nature and Outdoors", :score 0.2463572919368744}
    {:label "Urban and Architecture", :score 0.2432449609041214}]}
  {:object     "classification",
   :index      1,
   :prediction "Food and Dining",
   :score      0.2799667716026306,
   :predictions
   [{:label "Technology and Gadgets", :score 0.24433106184005737}
    {:label "Food and Dining", :score 0.2799667716026306}
    {:label "Nature and Outdoors", :score 0.2470456063747406}
    {:label "Urban and Architecture", :score 0.22865663468837738}]}
  {:object     "classification",
   :index      2,
   :prediction "Nature and Outdoors",
   :score      0.2784387171268463,
   :predictions
   [{:label "Technology and Gadgets", :score 0.2343646138906479}
    {:label "Food and Dining", :score 0.23988774418830872}
    {:label "Nature and Outdoors", :score 0.2784387171268463}
    {:label "Urban and Architecture", :score 0.2473088949918747}]}
  {:object     "classification",
   :index      3,
   :prediction "Urban and Architecture",
   :score      0.2572559416294098,
   :predictions
   [{:label "Technology and Gadgets", :score 0.24453730881214142}
    {:label "Food and Dining", :score 0.24202071130275726}
    {:label "Nature and Outdoors", :score 0.25618600845336914}
    {:label "Urban and Architecture", :score 0.2572559416294098}]}
  {:object     "classification",
   :index      4,
   :prediction "Nature and Outdoors",
   :score      0.28963884711265564,
   :predictions
   [{:label "Technology and Gadgets", :score 0.2364688664674759}
    {:label "Food and Dining", :score 0.23615625500679016}
    {:label "Nature and Outdoors", :score 0.28963884711265564}
    {:label "Urban and Architecture", :score 0.2377360463142395}]}
  {:object     "classification",
   :index      5,
   :prediction "Technology and Gadgets",
   :score      0.27144649624824524,
   :predictions
   [{:label "Technology and Gadgets", :score 0.27144649624824524}
    {:label "Food and Dining", :score 0.2500467598438263}
    {:label "Nature and Outdoors", :score 0.24524091184139252}
    {:label "Urban and Architecture", :score 0.23326583206653595}]}],
 :execution_time_ms 1581.228393}
