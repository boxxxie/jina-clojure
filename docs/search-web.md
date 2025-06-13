# Search Web API

Search the web for information using Jina AI Search API with advanced pagination and aggregation features.

## Usage

```clojure
(require '[jina.api.search-web :as search])
```

## Functions

### `call`

Basic web search function.

**Parameters:**
- `q` - The search query string
- `opts` - Optional map of search parameters

### `search-page`

Search a specific page of results.

**Parameters:**
- `q` - The search query string  
- `page` - Page number (0-based)
- `opts` - Optional map of search parameters

### `search-pages`

Search multiple pages and return lazy sequence of page results.

**Parameters:**
- `q` - The search query string
- `:start-page` - Starting page (default: 0)
- `:max-pages` - Maximum pages to fetch (default: 5)
- Additional options as keyword arguments

### `search-all-results`

Search multiple pages and return flattened sequence of all individual results.

### `search-pages-aggregated`

Search multiple pages and return single aggregated result with combined data.

### `search-until`

Search pages until a condition is met.

**Parameters:**
- `q` - The search query string
- `pred` - Predicate function that takes page response, returns true to stop
- `opts` - Optional search parameters

## Search Options

### Basic Parameters
- `:gl` (string) - Country code for search (two-letter)
- `:location` (string) - Geographic location for search origin
- `:hl` (string) - Language code (two-letter)
- `:num` (number) - Maximum results returned
- `:page` (number) - Result offset for pagination

### Advanced Parameters
- `:X-Site` (string) - Limit search to specific domain
- `:X-With-Links-Summary` ("all"/"true") - Include links summary
- `:X-With-Images-Summary` ("all"/"true") - Include images summary
- `:X-Retain-Images` ("none") - Remove images from response
- `:X-No-Cache` (boolean) - Bypass cache for real-time data
- `:X-With-Generated-Alt` (boolean) - Generate alt text for images
- `:X-Respond-With` ("no-content") - Exclude page content
- `:X-With-Favicon` (boolean) - Include website favicons
- `:X-Return-Format` ("markdown"/"html"/"text"/"screenshot"/"pageshot") - Response format
- `:X-Engine` ("browser"/"direct") - Content retrieval engine
- `:X-Timeout` (integer) - Page load timeout in seconds
- `:X-Set-Cookie` (string) - Custom cookie settings
- `:X-Proxy-Url` (string) - Proxy URL for requests
- `:X-Locale` (string) - Browser locale

## Examples

### Basic Search

```clojure
(search/call "latest AI research papers")
```

**Response:**
```clojure
{:code              200,
 :status            20000,
 :data              [{:title       "Artificial Intelligence - arXiv",
                      :url         "https://arxiv.org/list/cs.AI/recent",
                      :description "Subjects: Artificial Intelligence...",
                      :usage       {:tokens 1000}}
                     ;; ... more results
                     ],
 :meta              {:usage {:tokens 10000}},
 :execution_time_ms 1722.374517}
```

### Pagination Examples

```clojure
;; Get specific page
(search/search-page "AI research" 0)  ; First page
(search/search-page "AI research" 1)  ; Second page

;; Get multiple pages as sequence
(search/search-pages "AI research" :start-page 0 :max-pages 3)

;; Get all results flattened
(search/search-all-results "AI research" :max-pages 2)

;; Get aggregated results from multiple pages
(search/search-pages-aggregated "AI research" :max-pages 3)
```

### Conditional Search

```clojure
;; Search until page has fewer than 10 results
(search/search-until "AI research" #(< (count (:data %)) 10))
```

### Advanced Search Options

```clojure
(search/call "machine learning" 
  {:num 5
   :hl "en"
   :gl "US"
   :X-Return-Format "markdown"
   :X-With-Links-Summary "true"})
```

### Site-Specific Search

```clojure
(search/call "neural networks"
  {:X-Site "https://arxiv.org"})
```

## Aggregation Features

The `aggregate-search-results` function combines multiple search pages:

- **Numerical fields** (tokens, execution_time_ms) are summed
- **List fields** (:data) are concatenated  
- **Status fields** (:code, :status) use first result value
- **Other fields** use first non-nil value

```clojure
;; Manual aggregation
(search/aggregate-search-results 
  [(search/search-page "AI" 0)
   (search/search-page "AI" 1)])

;; Automatic aggregation
(search/search-pages-aggregated "AI research" :max-pages 3)
```

## Response Format

Search results contain:
- `:code` - HTTP status code
- `:status` - API status code  
- `:data` - Vector of search results with:
  - `:title` - Page title
  - `:url` - Page URL
  - `:description` - Page description
  - `:date` - Publication date (if available)
  - `:usage` - Token usage for this result
- `:meta` - Metadata including total usage
- `:execution_time_ms` - Processing time

## Pagination Best Practices

1. Use `search-pages-aggregated` for getting comprehensive results
2. Use `search-until` with a condition to avoid over-fetching
3. Consider rate limiting when fetching many pages
4. Use `:num` parameter to control results per page
