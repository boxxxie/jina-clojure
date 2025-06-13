# Segment Text API

Tokenizes text and divides it into chunks using Jina AI Segmenter API.

## Usage

```clojure
(require '[jina.api.segment-text :as segment])
```

## Function

### `call`

Tokenizes and segments text into meaningful chunks.

**Parameters:**
- `content` - The text content to segment
- `opts` - Optional map of parameters

## Options

- `:tokenizer` (string, default: "cl100k_base") - Tokenizer to use:
  - `"cl100k_base"` - GPT-3.5/GPT-4 tokenizer
  - `"o200k_base"` - GPT-4o tokenizer  
  - `"p50k_base"` - GPT-3 tokenizer
  - `"r50k_base"` - GPT-3 tokenizer variant
  - `"p50k_edit"` - GPT-3 edit tokenizer
  - `"gpt2"` - GPT-2 tokenizer
- `:return_tokens` (boolean, default: false) - Include tokens and IDs in response
- `:return_chunks` (boolean, default: false) - Segment text into semantic chunks
- `:max_chunk_length` (integer, default: 1000) - Maximum characters per chunk
- `:head` (integer) - Return first N tokens (exclusive with `:tail`)
- `:tail` (integer) - Return last N tokens (exclusive with `:head`)

## Examples

### Basic Tokenization

```clojure
(segment/call "This is a sample text that will be tokenized and segmented into meaningful chunks for processing.")
```

**Response:**
```clojure
{:num_tokens 18, 
 :tokenizer "cl100k_base", 
 :usage {:tokens 0}, 
 :execution_time_ms 713.939601}
```

### With Chunks and Tokens

```clojure
(segment/call 
  "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed do eiusmod tempor incididunt ut labore et dolore magna aliqua."
  {:return_chunks    true
   :max_chunk_length 50
   :return_tokens    true})
```

**Response:**
```clojure
{:num_tokens        22,
 :tokenizer         "cl100k_base",
 :usage             {:tokens 0},
 :num_chunks        1,
 :chunk_positions   [[0 123]],
 :tokens            [[["Lorem" [33883]]
                      [" ipsum" [27439]]
                      [" dolor" [24578]]
                      ;; ... more tokens
                      ]],
 :chunks            ["Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed do eiusmod tempor incididunt ut labore et dolore magna aliqua."],
 :execution_time_ms 593.676664}
```

### Different Tokenizers

```clojure
;; GPT-4o tokenizer
(segment/call "Hello, world!" {:tokenizer "o200k_base"})

;; GPT-2 tokenizer  
(segment/call "Hello, world!" {:tokenizer "gpt2"})
```

### Head and Tail Tokens

```clojure
;; Get first 10 tokens
(segment/call "Long text content here..." 
  {:head 10 :return_tokens true})

;; Get last 5 tokens
(segment/call "Long text content here..."
  {:tail 5 :return_tokens true})
```

### Custom Chunk Size

```clojure
(segment/call "Very long document content that needs to be split into smaller, manageable chunks for processing..."
  {:return_chunks true
   :max_chunk_length 100
   :return_tokens true})
```

### Multiple Tokenizers Comparison

```clojure
(def text "The quick brown fox jumps over the lazy dog.")

;; Compare different tokenizers
(doseq [tokenizer ["cl100k_base" "o200k_base" "gpt2"]]
  (let [result (segment/call text {:tokenizer tokenizer})]
    (println (str tokenizer ": " (:num_tokens result) " tokens"))))
```

## Response Format

The API returns a map with:

### Basic Response
- `:num_tokens` - Total number of tokens
- `:tokenizer` - Tokenizer used
- `:usage` - Token usage information
- `:execution_time_ms` - Processing time

### With Tokens (`:return_tokens` true)
- `:tokens` - Vector of token arrays, each containing [text, [token_id]]

### With Chunks (`:return_chunks` true)  
- `:num_chunks` - Number of chunks created
- `:chunk_positions` - Start/end positions of each chunk
- `:chunks` - Vector of chunk text content

### With Head/Tail
When using `:head` or `:tail`, only the specified number of tokens are returned in the `:tokens` field.

## Use Cases

### Document Processing
```clojure
;; Split large document into processable chunks
(segment/call large-document
  {:return_chunks true
   :max_chunk_length 500})
```

### Token Counting
```clojure
;; Count tokens for API cost estimation
(defn count-tokens [text model]
  (let [tokenizer (case model
                    "gpt-4" "cl100k_base"
                    "gpt-3.5-turbo" "cl100k_base"
                    "gpt-4o" "o200k_base"
                    "cl100k_base")]
    (:num_tokens (segment/call text {:tokenizer tokenizer}))))
```

### Content Preview
```clojure
;; Get first few tokens as preview
(segment/call article-text 
  {:head 50 
   :return_tokens true})
```

### Text Analysis
```clojure
;; Analyze token distribution
(segment/call text
  {:return_tokens true
   :return_chunks true
   :max_chunk_length 200})
```

## Tokenizer Selection Guide

- **cl100k_base**: GPT-3.5-turbo, GPT-4 (most common)
- **o200k_base**: GPT-4o models
- **p50k_base**: GPT-3 davinci, curie, babbage, ada
- **r50k_base**: GPT-3 models (alternative)
- **p50k_edit**: GPT-3 text editing models
- **gpt2**: GPT-2 models, some older applications

Choose the tokenizer that matches your target model for accurate token counting.
