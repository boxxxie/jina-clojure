# Jina AI API Reference

Complete reference for all Jina AI APIs available in this Clojure client library.

## Quick Start

```clojure
;; Add to your deps.edn
{:deps {jina/api {:local/root "path/to/jina"}}}

;; Require the APIs you need
(require '[jina.api.embeddings :as embeddings]
         '[jina.api.search-web :as search]
         '[jina.api.classify :as classify])

;; Basic usage
(embeddings/call ["Hello, world!"])
(search/call "latest AI research")
(classify/call {:input ["Great product!"] :labels ["positive" "negative"]})
```

## API Overview

| API | Purpose | Input | Output |
|-----|---------|-------|--------|
| [Embeddings](embeddings.md) | Convert text/images to vectors | Text/images | Numerical vectors |
| [Rerank](rerank.md) | Re-order search results | Query + documents | Ranked results |
| [Classify](classify.md) | Zero-shot classification | Text/images + labels | Classifications |
| [Search Web](search-web.md) | Web search | Search query | Web results |
| [Read URL](read-url.md) | Extract content from URLs | URL | Parsed content |
| [Segment Text](segment-text.md) | Tokenize and chunk text | Text | Tokens/chunks |
| [Deep Search](deep-search.md) | Research with reasoning | Conversation | Researched response |

## Common Patterns

### Error Handling

```clojure
(try
  (embeddings/call ["text"])
  (catch Exception e
    (println "Error:" (.getMessage e))))
```

### Async Processing

```clojure
(future
  (let [results (search/call "query")]
    (process-results results)))
```

### Batch Processing

```clojure
(defn process-batch [texts]
  (->> texts
       (partition 100)  ; Process in batches
       (map #(embeddings/call %))
       (mapcat :data)))
```

## Authentication

Set your Jina AI API key in your environment:

```bash
export JINA_API_KEY="your-api-key-here"
```

Or configure it in your application settings.

## Rate Limiting

Be mindful of API rate limits:
- Use batch processing where possible
- Implement exponential backoff for retries
- Monitor token usage in responses

## Best Practices

1. **Batch requests** when possible to reduce API calls
2. **Cache results** for repeated queries
3. **Handle errors gracefully** with try-catch blocks
4. **Monitor token usage** to control costs
5. **Use appropriate models** for your use case
6. **Validate inputs** before sending to APIs

## Examples by Use Case

### Semantic Search Pipeline

```clojure
(defn semantic-search [query documents]
  (let [;; Get embeddings for query and documents
        query-emb (embeddings/call [query])
        doc-embs (embeddings/call documents)
        
        ;; Rerank documents by relevance
        ranked (rerank/call query documents {:top_n 5})]
    ranked))
```

### Content Analysis Workflow

```clojure
(defn analyze-url [url]
  (let [;; Extract content
        content (read-url/call url {:X-Return-Format "text"})
        
        ;; Segment into chunks
        segments (segment-text/call (:content (:data content)) 
                   {:return_chunks true :max_chunk_length 500})
        
        ;; Classify each chunk
        classifications (classify/call 
                         {:input (:chunks segments)
                          :labels ["technical" "business" "general"]})]
    {:content content
     :segments segments  
     :classifications classifications}))
```

### Research Assistant

```clojure
(defn research-question [question]
  (let [;; First, do a web search
        search-results (search/call question {:num 10})
        
        ;; Then use deep search for comprehensive analysis
        analysis (deep-search/call 
                   [{:role "user" :content question}]
                   {:reasoning_effort "high"
                    :max_returned_urls 5})]
    {:search-results search-results
     :analysis analysis}))
```

## API Comparison

### When to Use Each API

- **Embeddings**: Similarity search, clustering, recommendation systems
- **Rerank**: Improving search result relevance, document ranking
- **Classify**: Content categorization, sentiment analysis, intent detection
- **Search Web**: Information gathering, research, real-time data
- **Read URL**: Content extraction, web scraping, document processing
- **Segment Text**: Text preprocessing, chunk management, token counting
- **Deep Search**: Complex research, multi-step reasoning, comprehensive analysis

### Performance Characteristics

| API | Speed | Token Usage | Use Case |
|-----|-------|-------------|----------|
| Embeddings | Fast | Low | Real-time similarity |
| Rerank | Fast | Low | Search optimization |
| Classify | Fast | Low | Content categorization |
| Search Web | Medium | Medium | Information retrieval |
| Read URL | Medium | Medium | Content extraction |
| Segment Text | Fast | Very Low | Text preprocessing |
| Deep Search | Slow | High | Complex research |

## Troubleshooting

### Common Issues

1. **Authentication errors**: Check API key configuration
2. **Rate limiting**: Implement backoff and retry logic
3. **Large inputs**: Use chunking for oversized content
4. **Network timeouts**: Increase timeout values for slow operations
5. **Invalid formats**: Validate input data before API calls

### Debug Mode

Enable debug logging to troubleshoot issues:

```clojure
(require '[taoensso.timbre :as log])
(log/set-level! :debug)
```

## Support

- Check individual API documentation for detailed parameters
- Review example code in each API guide
- Monitor API response messages for warnings and errors
- Use token usage information to optimize costs
