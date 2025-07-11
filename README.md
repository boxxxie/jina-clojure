# Jina AI API Documentation

This documentation covers the Clojure client library for Jina AI APIs.

## Available APIs

- [Embeddings](docs/embeddings.md) - Convert text/images to fixed-length vectors
- [Rerank](docs/rerank.md) - Re-rank search results for relevance
- [Classify](docs/classify.md) - Zero-shot classification for text or images
- [Search Web](docs/search-web.md) - Search the web for information
- [Read URL](docs/read-url.md) - Retrieve and parse content from URLs
- [Segment Text](docs/segment-text.md) - Tokenize and segment text into chunks
- [Deep Search](docs/deep-search.md) - Comprehensive web searching with reasoning

## Getting Started

All API functions follow a similar pattern:

```clojure
(require '[jina.core :as jina])

;; Basic usage
(jina/embeddings ["Hello, world!"])

;; With options
(jina/embeddings ["Hello, world!"] {:dimensions 512})


;; main API functions
jina/embeddings  
jina/rerank      
jina/classify    
jina/deep-search  
jina/read-url     
jina/search-web   
jina/segment-text 
```

## Common Parameters

Most APIs accept an optional map of parameters as the last argument. Refer to individual API documentation for specific parameters.

## Authentication

Make sure you have your Jina AI API key configured in your environment or application settings.
