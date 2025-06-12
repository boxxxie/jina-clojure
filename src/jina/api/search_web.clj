(ns jina.api.search-web
  (:require [jina.util :refer [jina-api-request]]))

(defn call
  "Search the web for information using Jina AI Search API.

  Input:
  - `q`: The search query string.
  - `opts`: An optional map of additional parameters:
    - `:gl` (string): The country to use for the search. It's a two-letter country code.
    - `:location` (string): From where you want the search query to originate. It is recommended to specify location at the city level in order to simulate a real user's search.
    - `:hl` (string): The language to use for the search. It's a two-letter language code.
    - `:num` (number): Sets maximum results returned.
    - `:page` (number): The result offset. It skips the given number of results. It's used for pagination.
    - `:X-Site` (string): Use \"X-Site: <https://specified-domain.com>\" for in-site searches limited to the given domain.
    - `:X-With-Links-Summary` (string, enum: \"all\", \"true\"): all to gather all links or true to gather unique links at the end of the response.
    - `:X-With-Images-Summary` (string, enum: \"all\", \"true\"): all to gather all images or true to gather unique images at the end of the response.
    - `:X-Retain-Images` (string, enum: \"none\"): none to remove all images from the response.
    - `:X-No-Cache` (boolean): true to bypass cache and retrieve real-time data.
    - `:X-With-Generated-Alt` (boolean): true to generate captions for images without alt tags.
    - `:X-Respond-With` (string, enum: \"no-content\"): no-content to exclude page content from the resposne.
    - `:X-With-Favicon` (boolean): true to include favicon of the website in the resposne.
    - `:X-Return-Format` (string, enum: \"markdown\", \"html\", \"text\", \"screenshot\", \"pageshot\"): markdown, html, text, screenshot, or pageshot (for URL of full-page screenshot).
    - `:X-Engine` (string, enum: \"browser\", \"direct\"): Specifies the engine to retrieve/parse content.
    - `:X-With-Favicons` (boolean): true to fetch the favicon of each URL in the SERP and include them in the response as image URI.
    - `:X-Timeout` (integer): Specifies the maximum time (in seconds) to wait for the webpage to load.
    - `:X-Set-Cookie` (string): Forwards your custom cookie settings when accessing the URL.
    - `:X-Proxy-Url` (string): Utilizes your proxy to access URLs.
    - `:X-Locale` (string): Controls the browser locale to render the page."
  [q & opts]
  (let [body (merge {:q q} (first opts))]
    (jina-api-request "/search-web" body)))
