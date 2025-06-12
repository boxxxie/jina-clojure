(ns jina.api.search-web
  (:require [jina.util :refer [jina-search-request]]))

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
  (let [headers (first opts)]
    (jina-search-request q headers)))

(defn search-page
  "Search a specific page of results.

  Input:
  - `q`: The search query string.
  - `page`: The page number (0-based).
  - `opts`: Optional map of additional parameters (same as `call` function)."
  [q page & opts]
  (let [headers (merge (first opts) {:page page})]
    (call q headers)))

(defn search-pages
  "Search multiple pages and return a lazy sequence of results.

  Input:
  - `q`: The search query string.
  - `start-page`: Starting page number (0-based, default: 0).
  - `max-pages`: Maximum number of pages to fetch (default: 5).
  - `opts`: Optional map of additional parameters (same as `call` function).

  Returns a lazy sequence where each element is a page of search results."
  [q & {:keys [start-page max-pages] :or {start-page 0 max-pages 5} :as opts}]
  (let [search-opts (dissoc opts :start-page :max-pages)]
    (map #(search-page q % search-opts)
         (range start-page (+ start-page max-pages)))))

(defn search-all-results
  "Search multiple pages and return all results flattened into a single sequence.

  Input:
  - `q`: The search query string.
  - `start-page`: Starting page number (0-based, default: 0).
  - `max-pages`: Maximum number of pages to fetch (default: 5).
  - `opts`: Optional map of additional parameters (same as `call` function).

  Returns a lazy sequence of individual search result items from all pages."
  [q & {:keys [start-page max-pages] :or {start-page 0 max-pages 5} :as opts}]
  (let [search-opts (dissoc opts :start-page :max-pages)]
    (->> (search-pages q :start-page start-page :max-pages max-pages)
         (mapcat :data))))

(defn search-until
  "Search pages until a condition is met or no more results are found.

  Input:
  - `q`: The search query string.
  - `pred`: A predicate function that takes a page response and returns true to stop.
  - `opts`: Optional map of additional parameters (same as `call` function).

  Returns a lazy sequence of page responses until the predicate returns true."
  [q pred & opts]
  (let [search-opts (first opts)]
    (take-while #(not (pred %))
                (map #(search-page q % search-opts) (range)))))


#_(call "latest AI research papers")

;; output
{:code              200,
 :status            20000,
 :data              [{:title       "Artificial Intelligence - arXiv",
                      :url         "https://arxiv.org/list/cs.AI/recent",
                      :description "Subjects: Artificial Intelligence (cs.AI); Computation and Language (cs.CL); Computer Vision and Pattern Recognition (cs.CV); Machine Learning (cs.",
                      :usage       {:tokens 1000}}
                     {:title       "Papers With Code: The latest in Machine Learning",
                      :url         "https://paperswithcode.com/",
                      :description "Stay informed on the latest trending ML papers with code, research developments, libraries, methods, and datasets. Read previous issues. Subscribe",
                      :usage       {:tokens 1000}}
                     {:title       "AI Research Papers - Arize AI",
                      :url         "https://arize.com/ai-research-papers",
                      :description "Keep up with the latest in AI research. Follow the latest in generative AI research papers and stay ahead of cutting-edge advancements.",
                      :usage       {:tokens 1000}}
                     {:title       "dair-ai/ML-Papers-of-the-Week - GitHub",
                      :url         "https://github.com/dair-ai/ML-Papers-of-the-Week",
                      :description "Validated AI discoveries – From 50 AI research papers on agents and virtual environments, CodeScientist proposed 19 findings. Of these, 6 were judged",
                      :usage       {:tokens 1000}}
                     {:title       "Noteworthy AI Research Papers of 2024 (Part One) - Ahead of AI",
                      :url         "https://magazine.sebastianraschka.com/p/ai-research-papers-2024-part-1",
                      :description "This AI Research Highlights of 2024 article. It covers a variety of topics, from mixture-of-experts models to new LLM scaling laws for precision.",
                      :date        "Dec 31, 2024",
                      :usage       {:tokens 1000}}
                     {:title       "Journal of Artificial Intelligence Research",
                      :url         "https://www.jair.org/",
                      :description "The journal's scope encompasses all areas of AI, including agents and multi-agent systems, automated reasoning, constraint processing and search",
                      :date        "Jan 6, 2025",
                      :usage       {:tokens 1000}}
                     {:title       "AI Paper with Code | Research Insights - Qualcomm",
                      :url         "https://www.qualcomm.com/research/artificial-intelligence/papers",
                      :description "Access AI paper with code and gain valuable research insights. Check out Qualcomm's cutting-edge advancements today. Explore now.",
                      :usage       {:tokens 1000}}
                     {:title       "Top AI Research Papers of 2024 - Association of Data Scientists",
                      :url         "https://adasci.org/top-ai-research-papers-of-2024",
                      :description "Top AI Research Papers of 2024 · 1. Mixtral of Experts (8th Jan 2024) · 2. Genie: Generative Interactive Environments (23rd Feb 2024) · 3. Accurate",
                      :date        "Dec 30, 2024",
                      :usage       {:tokens 1000}}
                     {:title       "[P] Noteworthy AI Research Papers of 2024 (Part One) - Reddit",
                      :url         "https://www.reddit.com/r/MachineLearning/comments/1htedss/p_noteworthy_ai_research_papers_of_2024_part_one",
                      :description "I just picked from all the papers I've read that year, then I selected one that I particularly liked or found important/useful for my work for each month.",
                      :date        "Jan 4, 2025",
                      :usage       {:tokens 1000}}
                     {:title       "Apple Machine Learning Research: Overview",
                      :url         "https://machinelearning.apple.com/",
                      :description "Apple machine learning teams are engaged in state of the art research in machine learning and artificial intelligence. Learn about the latest advancements.",
                      :usage       {:tokens 1000}}],
 :meta              {:usage {:tokens 10000}},
 :execution_time_ms 1722.374517}

;; Pagination examples:

#_(search-page "AI research" 0)  ; First page
#_(search-page "AI research" 1)  ; Second page

#_(search-pages "AI research" :start-page 0 :max-pages 3)  ; Get first 3 pages

#_(search-all-results "AI research" :max-pages 2)  ; Get all results from first 2 pages flattened

;; Search until we find a page with fewer than 10 results (indicating end of results)
#_(search-until "AI research" #(< (count (:data %)) 10))

;; Search with additional options
#_(search-pages "AI research" :start-page 1 :max-pages 2 :num 5 :hl "en")

(defn aggregate-search-results
  "Aggregates multiple search result pages into a single consolidated structure.
  
  Numerical fields (like tokens, execution_time_ms) are summed.
  List fields (like :data) are concatenated.
  Status fields (like :code, :status) use the value from the first result.
  Other fields use the value from the first non-nil result.
  
  Input:
  - `results`: A sequence of search result maps from pagination functions.
  
  Returns a single aggregated result map."
  [results]
  (when (seq results)
    (let [non-empty-results (filter some? results)
          ;; Fields that should not be summed (status codes, etc.)
          non-summable-fields #{:code :status}]
      (if (empty? non-empty-results)
        nil
        (reduce
          (fn [acc result]
            (reduce-kv
              (fn [merged-acc k v]
                (let [existing-val (get merged-acc k)]
                  (cond
                    ;; Don't sum status codes and similar fields - keep first value
                    (contains? non-summable-fields k) 
                    (assoc merged-acc k (or existing-val v))
                    
                    ;; Sum numerical values (except status codes)
                    (and (number? existing-val) (number? v)) 
                    (assoc merged-acc k (+ existing-val v))
                    
                    ;; Concatenate sequences/vectors
                    (and (sequential? existing-val) (sequential? v)) 
                    (assoc merged-acc k (concat existing-val v))
                    
                    ;; For maps, recursively merge with number summing
                    (and (map? existing-val) (map? v)) 
                    (assoc merged-acc k 
                           (merge-with
                             (fn [nested-v1 nested-v2]
                               (if (and (number? nested-v1) (number? nested-v2))
                                 (+ nested-v1 nested-v2)
                                 (or nested-v2 nested-v1)))
                             existing-val v))
                    
                    ;; Use existing value if present, otherwise use new value
                    :else (assoc merged-acc k (or existing-val v)))))
              acc
              result))
          (first non-empty-results)
          (rest non-empty-results))))))

(defn search-pages-aggregated
  "Search multiple pages and return aggregated results.
  
  Input:
  - `q`: The search query string.
  - `start-page`: Starting page number (0-based, default: 0).
  - `max-pages`: Maximum number of pages to fetch (default: 5).
  - `opts`: Optional map of additional parameters (same as `call` function).
  
  Returns a single aggregated result map with combined data from all pages."
  [q & {:keys [start-page max-pages] :or {start-page 0 max-pages 5} :as opts}]
  (let [search-opts (dissoc opts :start-page :max-pages)
        pages (search-pages q :start-page start-page :max-pages max-pages)]
    (aggregate-search-results pages)))

;; Aggregation examples:

#_(aggregate-search-results 
   [(search-page "AI research" 0)
    (search-page "AI research" 1)])

#_(search-pages-aggregated "AI research" :max-pages 3)
