(ns jina-clj-api.api.read-url
  (:require [jina-clj-api.util :refer [jina-api-request]]))

(defn jina-read-url
  "Retrieve/parse content from a URL using Jina AI Reader API.

  Input:
  - `url`: The URL to read.
  - `opts`: An optional map of additional parameters. These correspond to the X- prefixed headers in the Jina API documentation, e.g., `:X-Engine`, `:X-Return-Format`, etc.
    - `:viewport` (map): Sets browser viewport dimensions for responsive rendering.
    - `:injectPageScript` (string): Executes preprocessing JS code (inline string or remote URL), for instance manipulating DOMs.
    - `:X-Engine` (string, enum: \"browser\", \"direct\", \"cf-browser-rendering\"): Specifies the engine to retrieve/parse content.
    - `:X-Timeout` (integer): Specifies the maximum time (in seconds) to wait for the webpage to load.
    - `:X-Target-Selector` (string): CSS selectors to focus on specific elements within the page.
    - `:X-Wait-For-Selector` (string): CSS selectors to wait for specific elements before returning.
    - `:X-Remove-Selector` (string): CSS selectors to exclude certain parts of the page (e.g., headers, footers).
    - `:X-With-Links-Summary` (string, enum: \"all\", \"true\"): all to gather all links or true to gather unique links at the end of the response.
    - `:X-With-Images-Summary` (string, enum: \"all\", \"true\"): all to gather all images or true to gather unique images at the end of the response.
    - `:X-With-Generated-Alt` (boolean): true to add alt text to images lacking captions.
    - `:X-No-Cache` (boolean): true to bypass cache for fresh retrieval.
    - `:X-With-Iframe` (boolean): true to include iframe content in the response.
    - `:X-Return-Format` (string, enum: \"markdown\", \"html\", \"text\", \"screenshot\", \"pageshot\"): markdown, html, text, screenshot, or pageshot (for URL of full-page screenshot).
    - `:X-Token-Budget` (integer): Specifies maximum number of tokens to use for the request.
    - `:X-Retain-Images` (string, enum: \"none\"): none to remove all images from the response.
    - `:X-Respond-With` (string, enum: \"readerlm-v2\"): readerlm-v2, the language model specialized in HTML-to-Markdown, to deliver high-quality results for websites with complex structures and contents.
    - `:X-Set-Cookie` (string): Forwards your custom cookie settings when accessing the URL.
    - `:X-Proxy-Url` (string): Utilizes your proxy to access URLs.
    - `:X-Proxy` (string, enum: \"auto\", \"none\"): Sets country code for location-based proxy server. Use auto for optimal selection or none to disable.
    - `:DNT` (integer): 1 to not cache and track the requested URL on our server.
    - `:X-No-Gfm` (string, enum: \"true\", \"table\"): Opt in/out features from GFM (Github Flavored Markdown).
    - `:X-Locale` (string): Controls the browser locale to render the page.
    - `:X-Robots-Txt` (string): Defines bot User-Agent to check against robots.txt before fetching content.
    - `:X-With-Shadow-Dom` (boolean): true to extract content from all Shadow DOM roots in the document.
    - `:X-Base` (string, enum: \"final\"): final to follow the full redirect chain.
    - `:X-Md-Heading-Style` (string, enum: \"atx\"): When to use # or === to create Markdown headings. Set atx to use any number of \"==\" or \"--\" characters on the line below the text to create headings.
    - `:X-Md-Hr` (string): Defines Markdown horizontal rule format (passed to Turndown). Default is \"***\".
    - `:X-Md-Bullet-List-Marker` (string, enum: \"*\", \"-\", \"+\"): Sets Markdown bullet list marker character (passed to Turndown). Options: *, -, +.
    - `:X-Md-Em-Delimiter` (string, enum: \"-\", \"*\"): Defines Markdown emphasis delimiter (passed to Turndown). Options: -, *.
    - `:X-Md-Strong-Delimiter` (string, enum: \"**\", \"__\"): Sets Markdown strong emphasis delimiter (passed to Turndown). Options: **, __.
    - `:X-Md-Link-Style` (string, enum: \"referenced\", \"discarded\"): When not set, links are embedded directly within the text. Sets referenced to list links at the end, referenced by numbers in the text. Sets discarded to replace links with their anchor text.
    - `:X-Md-Link-Reference-Style` (string, enum: \"collapse\", \"shortcut\"): Sets Markdown reference link format (passed to Turndown). Set to collapse, shortcut or do not set this header."
  [url & opts]
  (let [body (merge {:url url} (first opts))]
    (jina-api-request "/read-url" body)))
