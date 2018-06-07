(ns rabbithole.core)

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
        cyc-s (apply str cyc-chars)]
    (= cyc-s s)))

(defn find-cycle
  "Given a string s, return the shortest substring that can be cycled to form s.
  That substring may turn out to be s."
  [s]
  {:pre [(string? s)]}
  (some #(when (is-cycle-of? s %) %) (potential-cycles s)))
(defn foo
  "I don't do a whole lot."
  [x]
  (println x "Hello, World!"))
