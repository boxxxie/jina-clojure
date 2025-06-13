# Classify API

Zero-shot classification for text or images using Jina AI Classifier API.

## Usage

```clojure
(require '[jina.api.classify :as classify])
```

## Function

### `call`

Performs zero-shot classification on input data.

**Parameters:**
- `params` - Map containing `:input` and `:labels`
  - `:input` - Vector of inputs for classification (format depends on model)
  - `:labels` - Vector of classification labels
  - `:model` - Optional model identifier
- `opts` - Optional map of additional parameters

**Options:**
- `:model` (string, default: "jina-embeddings-v3") - Model to use:
  - `"jina-embeddings-v3"` - For text classification only
  - `"jina-clip-v2"` - For image classification (can also handle text)
- `:classifier_id` (string) - Identifier of existing classifier

## Model-Specific Input Requirements

### jina-embeddings-v3 (Text Only)
- Input format: Vector of strings
- Example: `["I love this product!" "This is terrible"]`

### jina-clip-v2 (Images and Text)
- Input format: Vector of objects with `:image` or `:text` keys
- Image format: Base64 encoded string or URL
- Example: `[{:image "data:image/jpeg;base64,..."} {:text "A red car"}]`
- Note: Cannot mix text and image objects in same request

## Examples

### Text Classification

```clojure
(def text-example
  {:model  "jina-embeddings-v3"
   :input  ["Calculate compound interest"
            "Write a poem about nature"
            "Explain quantum physics"]
   :labels ["Simple task" "Complex reasoning" "Creative writing"]})

(classify/call text-example)
```

**Response:**
```clojure
{:usage {:total_tokens 196},
 :data
 [{:object     "classification",
   :index      0,
   :prediction "Simple task",
   :score      0.35217395424842834,
   :predictions
   [{:label "Simple task", :score 0.35217395424842834}
    {:label "Complex reasoning", :score 0.34130600094795227}
    {:label "Creative writing", :score 0.30652010440826416}]}
  ;; ... more results
  ]}
```

### Image Classification

```clojure
(def image-example
  {:model  "jina-clip-v2"
   :input  [{:text "A sleek smartphone"}
            {:image "https://picsum.photos/id/11/367/267"}
            {:text "Fresh sushi rolls"}]
   :labels ["Technology and Gadgets" "Food and Dining" "Nature and Outdoors"]})

(classify/call image-example)
```

### With Custom Classifier

```clojure
(classify/call 
  {:input ["Text to classify"]
   :labels ["Label1" "Label2"]}
  {:classifier_id "my-custom-classifier"})
```

## Input Validation

The API includes built-in validation using Malli schemas:

- **jina-embeddings-v3**: Validates input is vector of strings
- **jina-clip-v2**: Validates input is vector of objects with `:text` or `:image` keys

Invalid input will throw an exception with detailed error information.

## Response Format

The API returns a map with:
- `:usage` - Token usage information
- `:data` - Vector of classification results with:
  - `:object` - Always "classification"
  - `:index` - Position in input array
  - `:prediction` - Top predicted label
  - `:score` - Confidence score for top prediction
  - `:predictions` - All label scores sorted by confidence
- `:execution_time_ms` - Processing time

## Example Validation Errors

```clojure
;; This will throw an error - wrong input format for jina-embeddings-v3
(classify/call 
  {:model "jina-embeddings-v3"
   :input [{:text "Hello"}]  ; Should be vector of strings
   :labels ["Label1"]})

;; This will throw an error - wrong input format for jina-clip-v2  
(classify/call
  {:model "jina-clip-v2"
   :input ["Hello"]  ; Should be vector of objects
   :labels ["Label1"]})
```
