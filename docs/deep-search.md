# Deep Search API

Combines web searching, reading, and reasoning for comprehensive investigation using Jina AI DeepSearch API.

## Usage

```clojure
(require '[jina.api.deep-search :as deep-search])
```

## Function

### `call`

Performs comprehensive search with reasoning capabilities.

**Parameters:**
- `messages` - List of messages between user and assistant (conversation format)
- `opts` - Optional map of parameters

## Options

### Core Parameters
- `:model` (string, default: "jina-deepsearch-v1") - Model identifier
- `:stream` (boolean, default: true) - Server-sent events delivery
- `:reasoning_effort` ("low"/"medium"/"high") - Reasoning effort level

### Search Control
- `:budget_tokens` (number) - Maximum tokens for DeepSearch process
- `:max_attempts` (number) - Maximum retries for problem solving
- `:no_direct_answer` (boolean) - Force additional thinking/search steps
- `:max_returned_urls` (number) - Maximum URLs in final answer

### Domain Control
- `:boost_hostnames` (vector of strings) - Prioritized domains
- `:bad_hostnames` (vector of strings) - Excluded domains  
- `:only_hostnames` (vector of strings) - Exclusively included domains

### Response Format
- `:response_format` (map) - JSON schema for structured responses

## Examples

### Basic Deep Search

```clojure
(deep-search/call 
  [{:role "user" 
    :content "What are the latest developments in quantum computing?"}])
```

**Response:**
```clojure
{:id "deepsearch-123",
 :object "deepsearch.completion",
 :created 1234567890,
 :model "jina-deepsearch-v1",
 :choices [{:index 0,
            :message {:role "assistant",
                      :content "Based on my research, here are the latest developments in quantum computing..."},
            :finish_reason "stop"}],
 :usage {:prompt_tokens 15, :completion_tokens 250, :total_tokens 265}}
```

### Enhanced Reasoning with Domain Preferences

```clojure
(deep-search/call 
  [{:role "user" 
    :content "Compare the performance of different machine learning frameworks"}]
  {:reasoning_effort "high"
   :max_returned_urls 5
   :boost_hostnames ["arxiv.org" "github.com"]})
```

**Response:**
```clojure
{:id "deepsearch-456",
 :object "deepsearch.completion", 
 :created 1234567891,
 :model "jina-deepsearch-v1",
 :choices [{:index 0,
            :message {:role "assistant",
                      :content "After extensive research and analysis, here's a comprehensive comparison..."},
            :finish_reason "stop"}],
 :usage {:prompt_tokens 25, :completion_tokens 400, :total_tokens 425}}
```

### Conversation Context

```clojure
(deep-search/call
  [{:role "user" 
    :content "What is machine learning?"}
   {:role "assistant"
    :content "Machine learning is a subset of AI that enables computers to learn from data..."}
   {:role "user"
    :content "How does it compare to deep learning specifically?"}])
```

### Structured Response Format

```clojure
(deep-search/call
  [{:role "user"
    :content "Analyze the current state of renewable energy adoption"}]
  {:response_format 
   {:type "json_schema"
    :json_schema 
    {:name "energy_analysis"
     :schema 
     {:type "object"
      :properties 
      {:summary {:type "string"}
       :key_trends {:type "array" :items {:type "string"}}
       :statistics {:type "object"}
       :sources {:type "array" :items {:type "string"}}}}}}})
```

### Domain-Specific Research

```clojure
;; Academic research focus
(deep-search/call
  [{:role "user"
    :content "Latest breakthroughs in CRISPR gene editing"}]
  {:boost_hostnames ["pubmed.ncbi.nlm.nih.gov" "nature.com" "science.org"]
   :bad_hostnames ["wikipedia.org"]
   :reasoning_effort "high"})

;; Technical documentation focus  
(deep-search/call
  [{:role "user"
    :content "Best practices for Kubernetes deployment"}]
  {:only_hostnames ["kubernetes.io" "github.com" "stackoverflow.com"]
   :max_returned_urls 10})
```

### Forced Deep Analysis

```clojure
(deep-search/call
  [{:role "user"
    :content "What is 2+2?"}]
  {:no_direct_answer true
   :reasoning_effort "medium"})
```

### Budget-Controlled Search

```clojure
(deep-search/call
  [{:role "user"
    :content "Comprehensive analysis of climate change impacts"}]
  {:budget_tokens 5000
   :max_attempts 3
   :reasoning_effort "high"})
```

## Message Format

Messages follow the standard chat completion format:

```clojure
[{:role "system" :content "You are a helpful research assistant."}
 {:role "user" :content "Research question here"}
 {:role "assistant" :content "Previous response"}
 {:role "user" :content "Follow-up question"}]
```

**Roles:**
- `"system"` - System instructions/context
- `"user"` - User messages/questions  
- `"assistant"` - Assistant responses

## Response Format

The API returns a map with:
- `:id` - Unique request identifier
- `:object` - Always "deepsearch.completion"
- `:created` - Unix timestamp
- `:model` - Model used
- `:choices` - Vector of completion choices with:
  - `:index` - Choice index
  - `:message` - Response message with `:role` and `:content`
  - `:finish_reason` - Completion reason ("stop", "length", etc.)
- `:usage` - Token usage with `:prompt_tokens`, `:completion_tokens`, `:total_tokens`

## Reasoning Effort Levels

### Low
- Quick searches with basic reasoning
- Faster responses, lower token usage
- Good for simple questions

### Medium (Recommended)
- Balanced search depth and reasoning
- Moderate response time and token usage
- Good for most research questions

### High  
- Extensive search and deep reasoning
- Longer response time, higher token usage
- Best for complex analysis and research

## Best Practices

1. **Use conversation context**: Include relevant previous messages for better continuity
2. **Choose appropriate reasoning effort**: Match effort level to question complexity
3. **Leverage domain control**: Use hostname filters for focused research
4. **Set reasonable budgets**: Control costs with token limits
5. **Structure responses**: Use response_format for consistent output
6. **Handle streaming**: Process server-sent events if using `:stream true`

## Use Cases

### Research and Analysis
```clojure
(deep-search/call
  [{:role "user"
    :content "Analyze the economic impact of remote work on urban centers"}]
  {:reasoning_effort "high"
   :boost_hostnames ["brookings.edu" "mckinsey.com"]})
```

### Technical Investigation
```clojure
(deep-search/call
  [{:role "user" 
    :content "Compare serverless vs containerized deployment strategies"}]
  {:only_hostnames ["aws.amazon.com" "cloud.google.com" "azure.microsoft.com"]
   :max_returned_urls 8})
```

### Academic Research
```clojure
(deep-search/call
  [{:role "user"
    :content "Recent advances in quantum error correction"}]
  {:boost_hostnames ["arxiv.org" "nature.com"]
   :reasoning_effort "high"
   :budget_tokens 8000})
```
