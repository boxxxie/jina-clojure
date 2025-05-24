(ns jina-clj-api.core-test
  (:require [clojure.test :refer :all]
            [jina-clj-api.core :as jina]))

;; IMPORTANT: For actual integration tests, set the JINA_API_KEY environment variable.
;; For example, in your shell: export JINA_API_KEY="YOUR_JINA_API_KEY_HERE"
;; Or, if running with `bb`: JINA_API_KEY="YOUR_JINA_API_KEY_HERE" bb test

(use-fixtures :once
  (fn [f]
    ;; This fixture sets a dummy API key for tests that don't make actual API calls.
    ;; For integration tests, ensure JINA_API_KEY is set in your environment.
    (System/setProperty "JINA_API_KEY" (or (System/getenv "JINA_API_KEY") "dummy-api-key"))
    (f)
    (System/clearProperty "JINA_API_KEY")))

(deftest test-get-api-key
  (testing "get-api-key retrieves from environment variable"
    (with-redefs [System/getenv (fn [k] (if (= k "JINA_API_KEY") "test-key" nil))]
      (is (= "test-key" (jina/get-api-key)))))

  (testing "get-api-key throws exception if not set"
    (with-redefs [System/getenv (fn [k] nil)]
      (is (thrown-with-msg? clojure.lang.ExceptionInfo #"JINA_API_KEY environment variable not set."
                            (jina/get-api-key))))))

;; Example of an integration test (requires a valid JINA_API_KEY and network access)
;; Uncomment and provide a real API key to run this.
;; (deftest integration-test-jina-embeddings
;;   (when (System/getenv "JINA_API_KEY")
;;     (testing "jina-embeddings returns a valid response"
;;       (let [input ["Hello, world!"]
;;             response (jina/jina-embeddings input)]
;;         (is (map? response))
;;         (is (contains? response :data))
;;         (is (sequential? (:data response)))
;;         (is (pos? (count (:data response))))
;;         (is (contains? (first (:data response)) :embedding))))))

;; Example of an integration test for jina-rerank
;; (deftest integration-test-jina-rerank
;;   (when (System/getenv "JINA_API_KEY")
;;     (testing "jina-rerank returns a valid response"
;;       (let [query "What is the capital of France?"
;;             documents ["Paris is the capital of France." "Berlin is the capital of Germany."]
;;             response (jina/jina-rerank query documents)]
;;         (is (map? response))
;;         (is (contains? response :results))
;;         (is (sequential? (:results response)))
;;         (is (pos? (count (:results response))))
;;         (is (contains? (first (:results response)) :relevance_score))))))
