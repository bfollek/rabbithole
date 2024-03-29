(ns rabbithole.core
  "Utility functions"
  (:require [clojure.java.io :as io]
            [clojure.string :as str]))

(defn assoc-if-missing
  "If k is not in m, add it with the value v. If k is already in m, do nothing.
  In either case, return m."
  [m k v]
  (if (contains? m k)
    m
    (assoc m k v)))

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
  ;; Catch the no-args-passed case.
  (if seqs
    ;; `seqs` is a collection, so `(map vector seqs)` won't work. We need `apply`.
    (apply (partial map vector) seqs)
    ()))

(defn index-of
  "index-of returns the index of `item` in `coll`, or -1 if `item` is not found.
  It wraps the java .indexOf method."
  [^java.util.List coll item]
  (.indexOf coll item))

(defn read-lines
  "Read a text file and return all lines as a seq. Ensures the file is closed.
   Not lazy. That may matter on huge files."
  [file-name]
  (with-open [rdr (io/reader file-name)]
    (doall (line-seq rdr))))

(def flat-set
  "flatten, then convert to set"
  (comp set flatten))

(defn char-range
  "Char-range returns a lazy sequence of chars from `start` to `end`, inclusive.
  From mikera on stackoverflow:
  https://stackoverflow.com/questions/11670941/generate-character-sequence-from-a-to-z-in-clojure"
  [start end]
  (map char (range (int start) (inc (int end)))))

(defmulti to-int
  "Convert string to int. Return nil for nil values."
  class)
(defmethod to-int String [s] (Integer/parseInt s))
(defmethod to-int nil [_] nil)

(defmulti to-lower
  "Convert string or char to lower-case. Return nil for nil values."
  class)
(defmethod to-lower Character [^java.lang.Character c] (Character/toLowerCase c))
(defmethod to-lower String [s] (str/lower-case s))
(defmethod to-lower nil [_] nil)

(defmulti to-upper
  "Convert string or char to upper-case. Return nil for nil values."
  class)
(defmethod to-upper Character [^java.lang.Character c] (Character/toUpperCase c))
(defmethod to-upper String [s] (str/upper-case s))
(defmethod to-upper nil [_] nil)

(defn transpose
  "Given a collection whose elements are collections, i.e. a matrix, return a vector of vectors where each row is now a column. From https://twitter.com/kelvinmai/status/1466914942318043139

  Caveat: If `matrix` is jagged, the number of rows in the transposed matrix is the size of the shortest row in the original matrix. `transpose` ignores the extra elements in longer rows. For example:
   
  rabbithole.core=> (def matrix [[1 2 3] [4 5]])
  rabbithole.core=> (transpose matrix)
  [[1 4] [2 5]]
   
  `transpose` ignored the 3 element at ((matrix 0) 2).
  "
  [matrix]
  (apply mapv vector matrix))

(defn update-multi
  "Update multiple values in a map `m`. `ks` is a vector of the keys
  whose corresponding values will be updated. `f` is a function that takes
  the old value and returns the new one. `update-multi` returns the updated map."
  [m ks f]
  (reduce #(update %1 %2 f) m ks))
