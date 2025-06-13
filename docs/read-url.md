# Read URL API

Retrieve and parse content from URLs using Jina AI Reader API with extensive customization options.

## Usage

```clojure
(require '[jina.api.read-url :as read-url])
```

## Function

### `call`

Retrieves and parses content from a URL.

**Parameters:**
- `url` - The URL to read
- `opts` - Optional map of parameters (correspond to X- prefixed headers in Jina API)

## Options

### Core Parameters
- `:X-Engine` ("browser"/"direct"/"cf-browser-rendering") - Content retrieval engine
- `:X-Timeout` (integer) - Maximum wait time in seconds
- `:X-Return-Format` ("markdown"/"html"/"text"/"screenshot"/"pageshot") - Response format
- `:X-No-Cache` (boolean) - Bypass cache for fresh retrieval

### Content Selection
- `:X-Target-Selector` (string) - CSS selectors for specific elements
- `:X-Wait-For-Selector` (string) - CSS selectors to wait for before returning
- `:X-Remove-Selector` (string) - CSS selectors to exclude from content
- `:X-With-Iframe` (boolean) - Include iframe content
- `:X-With-Shadow-Dom` (boolean) - Extract content from Shadow DOM roots

### Content Enhancement
- `:X-With-Links-Summary` ("all"/"true") - Include links summary
- `:X-With-Images-Summary` ("all"/"true") - Include images summary  
- `:X-With-Generated-Alt` (boolean) - Generate alt text for images
- `:X-Retain-Images` ("none") - Remove all images
- `:X-Respond-With` ("readerlm-v2") - Use specialized language model

### Browser Configuration
- `:viewport` (map) - Browser viewport dimensions
- `:injectPageScript` (string) - Preprocessing JavaScript code
- `:X-Locale` (string) - Browser locale
- `:X-Set-Cookie` (string) - Custom cookie settings
- `:X-Proxy-Url` (string) - Proxy URL for requests
- `:X-Proxy` ("auto"/"none") - Location-based proxy server

### Markdown Formatting
- `:X-No-Gfm` ("true"/"table") - GitHub Flavored Markdown features
- `:X-Md-Heading-Style` ("atx") - Markdown heading style
- `:X-Md-Hr` (string) - Horizontal rule format (default: "***")
- `:X-Md-Bullet-List-Marker` ("*"/"-"/"+") - Bullet list marker
- `:X-Md-Em-Delimiter` ("-"/"*") - Emphasis delimiter
- `:X-Md-Strong-Delimiter` ("**"/"__") - Strong emphasis delimiter
- `:X-Md-Link-Style` ("referenced"/"discarded") - Link formatting style
- `:X-Md-Link-Reference-Style` ("collapse"/"shortcut") - Reference link format

### Advanced Options
- `:X-Token-Budget` (integer) - Maximum tokens for request
- `:X-Base` ("final") - Follow full redirect chain
- `:X-Robots-Txt` (string) - Bot User-Agent for robots.txt check
- `:DNT` (integer) - Do not track/cache (set to 1)

## Examples

### Basic Usage

```clojure
(read-url/call "https://example.com/article")
```

**Response:**
```clojure
{:code              200,
 :status            20000,
 :data              {:title       "Example Domain",
                     :description "",
                     :url         "https://example.com/article",
                     :content     "This domain is for use in illustrative examples...",
                     :usage       {:tokens 42}},
 :meta              {:usage {:tokens 42}},
 :execution_time_ms 4909.543276}
```

### Custom Viewport and Format

```clojure
(read-url/call "https://responsive-site.com"
  {:viewport {:width 1920 :height 1080}
   :X-Return-Format "markdown"
   :X-Engine "browser"})
```

### Content Filtering

```clojure
(read-url/call "https://news-site.com/article"
  {:X-Target-Selector "article, .content"
   :X-Remove-Selector "header, footer, .ads"
   :X-With-Links-Summary "true"})
```

### Screenshot Capture

```clojure
(read-url/call "https://example.com"
  {:X-Return-Format "screenshot"
   :viewport {:width 1200 :height 800}})
```

### JavaScript Preprocessing

```clojure
(read-url/call "https://spa-app.com"
  {:injectPageScript "document.querySelector('.load-more').click();"
   :X-Wait-For-Selector ".content-loaded"
   :X-Timeout 30})
```

### Markdown Customization

```clojure
(read-url/call "https://blog.example.com/post"
  {:X-Return-Format "markdown"
   :X-Md-Heading-Style "atx"
   :X-Md-Link-Style "referenced"
   :X-Md-Bullet-List-Marker "*"
   :X-No-Gfm "table"})
```

### Proxy and Cookies

```clojure
(read-url/call "https://geo-restricted.com"
  {:X-Proxy "auto"
   :X-Set-Cookie "session=abc123; auth=token456"
   :X-Locale "en-US"})
```

### High-Quality Content Extraction

```clojure
(read-url/call "https://complex-site.com"
  {:X-Respond-With "readerlm-v2"
   :X-Engine "browser"
   :X-With-Generated-Alt true
   :X-With-Shadow-Dom true})
```

## Response Format

The API returns a map with:
- `:code` - HTTP status code
- `:status` - API status code
- `:data` - Content data with:
  - `:title` - Page title
  - `:description` - Page description
  - `:url` - Final URL (after redirects)
  - `:content` - Extracted content in requested format
  - `:usage` - Token usage information
  - `:warning` - Any warnings (e.g., 404 errors)
- `:meta` - Metadata including usage
- `:execution_time_ms` - Processing time

## Content Formats

### Markdown (default)
Clean, readable markdown with proper formatting

### HTML
Raw HTML content with optional filtering

### Text
Plain text extraction without markup

### Screenshot
Returns image data or URL for page screenshot

### Pageshot
Returns URL for full-page screenshot

## Best Practices

1. **Use appropriate engine**: "browser" for SPAs, "direct" for simple pages
2. **Set reasonable timeouts**: Especially for slow-loading pages
3. **Filter content**: Use selectors to focus on relevant content
4. **Consider token budget**: For large pages, set `:X-Token-Budget`
5. **Handle errors**: Check for warnings in response data
6. **Respect robots.txt**: Use `:X-Robots-Txt` for ethical scraping
