(ns rabbithole.core
  (:require [clojure.string :as str]))

(defn potential-cycles
  "Return a collection of all substrings of s that are also potential cycles of s, including s."
  [s]
  {:pre [(string? s)]}
  ;; start-from: Any cycle will be at least as long as the number of distinct
  ;; chars in s. We can skip anything shorter.
  ;;
  ;; end-at: (subs) stops short of the end value, so we feed it the length of s.
  ;; (range) also stops short of the end value, so we feed it (length of s) + 1.
  (let [start-from (count (distinct s))
        end-at (inc (count s))]
    (map #(subs s 0 %) (range start-from end-at))))

(defn is-cycle-of?
  "Can cyc be cycled to form s? cyc and s must both be strings.
  cyc may be longer than s, since it will be cycled to (count s)."
  [s cyc]
  {:pre [(and (string? s) (string? cyc))]}
  (let [cyc-chars (take (count s) (cycle cyc))
        cyc-s (str/join cyc-chars)]
    (= cyc-s s)))

(defn find-cycle
  "Given a string s, return the shortest substring that can be cycled to form s.
  That substring may turn out to be s."
  [s]
  {:pre [(string? s)]}
  (some #(when (is-cycle-of? s %) %) (potential-cycles s)))

(defn zip-up
  "Given two or more seqs, return a seq of vectors. The nth vector comprises the nth element from each of the input seqs. Examples:

    (zip-up [1 2] [3 4])
    => ([1 3] [2 4])

    (zip-up [1 2 3] [4 5 6] [7 8 9])
    => ([1 4 7] [2 5 8] [3 6 9])

  "
  [& seqs]
  (if seqs
    (apply (partial map vector) seqs)
    ()))
