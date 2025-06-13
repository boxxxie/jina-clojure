# Rerank API

Re-rank search results using Jina AI Reranker API to improve relevance ordering.

## Usage

```clojure
(require '[jina.api.rerank :as rerank])
```

## Function

### `call`

Re-ranks documents based on relevance to a query.

**Parameters:**
- `query` - The search query string
- `documents` - Vector of strings, TextDocs, and/or images to rerank
- `opts` - Optional map of parameters

**Options:**
- `:model` (string, default: "jina-reranker-v2-base-multilingual") - Model identifier
- `:top_n` (integer) - Number of most relevant documents to return (defaults to length of documents)
- `:return_documents` (boolean, default: true) - Include document text in response

## Examples

### Basic Usage

```clojure
(rerank/call 
  "machine learning algorithms"
  ["Deep learning is a subset of machine learning"
   "Natural language processing uses statistical methods"
   "Computer vision applies machine learning to images"
   "Reinforcement learning trains agents through rewards"])
```

**Response:**
```clojure
{:model             "jina-reranker-v2-base-multilingual",
 :usage             {:total_tokens 50},
 :results
 [{:index           0,
   :document        {:text "Deep learning is a subset of machine learning"},
   :relevance_score 0.23231014609336853}
  {:index           2,
   :document        {:text "Computer vision applies machine learning to images"},
   :relevance_score 0.16026602685451508}
  {:index           1,
   :document        {:text "Natural language processing uses statistical methods"},
   :relevance_score 0.09401018172502518}
  {:index           3,
   :document        {:text "Reinforcement learning trains agents through rewards"},
   :relevance_score 0.08509904146194458}],
 :execution_time_ms 1446.232782}
```

### Top N Results Only

```clojure
(rerank/call 
  "machine learning"
  ["Document 1" "Document 2" "Document 3" "Document 4"]
  {:top_n 2})
```

### Without Document Text

```clojure
(rerank/call 
  "search query"
  ["Doc 1" "Doc 2" "Doc 3"]
  {:return_documents false})
```

## Response Format

The API returns a map with:
- `:model` - Model used for reranking
- `:usage` - Token usage information
- `:results` - Vector of ranked results with:
  - `:index` - Original position in input documents
  - `:document` - Document content (if `:return_documents` is true)
  - `:relevance_score` - Relevance score (higher = more relevant)
- `:execution_time_ms` - Processing time

Results are ordered by relevance score in descending order.
