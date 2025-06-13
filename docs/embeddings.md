# Embeddings API

Converts text/images to fixed-length vectors using Jina AI Embeddings API.

## Usage

```clojure
(require '[jina.api.embeddings :as embeddings])
```

## Function

### `call`

Converts input to embeddings.

**Parameters:**
- `input` - A vector of strings or objects to be embedded
- `opts` - Optional map of parameters

**Options:**
- `:model` (string, default: "jina-embeddings-v3") - Model identifier
- `:embedding_type` (string, default: "float") - Format of returned embeddings (float, base64, binary, ubinary)
- `:task` (string) - Intended downstream application (retrieval.query, retrieval.passage, text-matching, classification, separation)
- `:dimensions` (integer) - Truncate output embeddings to specified size
- `:normalized` (boolean, default: false) - Normalize embeddings to unit L2 norm
- `:late_chunking` (boolean, default: false) - Concatenate all sentences and treat as single input
- `:truncate` (boolean, default: false) - Auto-drop tail extending beyond max context length

## Examples

### Basic Usage

```clojure
(embeddings/call ["Hello, world!"])
```

**Response:**
```clojure
{:model  "jina-embeddings-v3",
 :object "list",
 :usage  {:total_tokens 6, :prompt_tokens 6},
 :data   [{:object    "embedding",
           :index     0,
           :embedding [0.11442348 -0.11122073 0.13124809 ...]}]}
```

### With Dimensions

```clojure
(embeddings/call ["Hello, world!" "there is no spoon"] {:dimensions 10})
```

**Response:**
```clojure
{:model             "jina-embeddings-v3",
 :object            "list",
 :usage             {:total_tokens 13, :prompt_tokens 13},
 :data              [{:object    "embedding",
                      :index     0,
                      :embedding [0.39906722 -0.38789722 0.45774525 ...]}
                     {:object    "embedding",
                      :index     1,
                      :embedding [0.22698139 -0.17781383 0.41603658 ...]}],
 :execution_time_ms 1382.003666}
```

### Advanced Options

```clojure
(embeddings/call 
  ["Document for retrieval" "Query text"]
  {:task "retrieval.passage"
   :normalized true
   :dimensions 512})
```

## Response Format

The API returns a map with:
- `:model` - Model used for embeddings
- `:object` - Always "list"
- `:usage` - Token usage information
- `:data` - Vector of embedding objects with `:index` and `:embedding`
- `:execution_time_ms` - Processing time
