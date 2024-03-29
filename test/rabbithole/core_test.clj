(ns rabbithole.core-test
  (:require [clojure.test :refer [deftest is testing]]
            [clojure.string :as str]
            [rabbithole.core :refer [assoc-if-missing char-range find-cycle flat-set index-of is-cycle-of? potential-cycles to-int to-lower to-upper transpose update-multi zip-up]]))

(deftest test-assoc-if-missing
  (testing "assoc-if-missing common cases"
    (is (= {:a 1}
           (assoc-if-missing {} :a 1)))
    (is (= {:a 1}
           (assoc-if-missing {:a 1} :a 1))))
  (testing "assoc-if-missing edge cases"
    (is (= {:a nil}
           (assoc-if-missing {} :a nil)))
    (is (= {:a nil}
           (assoc-if-missing {:a nil} :a 99)))))

(deftest test-potential-cycles
  (testing "potential-cycles common cases"
    (is (= '("ab" "aba" "abab")
           (potential-cycles "abab")))
    (is (= '("abab" "abab5" "abab56")
           ;; 4 unique chars, so shortest potential cycle is 4 chars
           (potential-cycles "abab56"))))
  (testing "potential-cycles edge cases"
    (is (= '("")
           (potential-cycles "")))))

(deftest test-is-cycle-of?
  (testing "is-cycle-of? common cases"
    (is (false? (is-cycle-of? "abab" "a")))
    (is (false? (is-cycle-of? "abab" "abc")))
    (is (true? (is-cycle-of? "abab7qw34" "abab7qw34")))
    (is (true? (is-cycle-of? "abab" "abab")))
    (is (true? (is-cycle-of? "abab" "ab"))))
  (testing "is-cycle-of? edge cases"
    ;; cyc longer than s is okay
    (is (true? (is-cycle-of? "abab" "ababz")))
    (is (true? (is-cycle-of? "" "")))))

(deftest test-find-cycle
  (testing "find-cycle common cases"
    (is (= "123456" (find-cycle "123456")))
    (is (= "ab" (find-cycle "abababab"))))
  (testing "find-cycle edge cases"
    (is (= "" (find-cycle "")))))

(deftest test-zip-up
  (testing "common cases"
    (is (= '([1 3] [2 4]) (zip-up '(1 2) '(3 4))))
    (is (= '([1 3] [2 4]) (zip-up [1 2] [3 4])))
    (is (= '([1 4 7] [2 5 8]) (zip-up [1 2 3] [4 5 6] [7 8])))
    (is (= '([1 4 7] [2 5 8] [3 6 9]) (zip-up [1 2 3] [4 5 6] [7 8 9]))))
  (testing "zip-up edge cases"
    (is (= () (zip-up)))
    (is (= () (zip-up [])))
    (is (= () (zip-up [] [])))
    (is (= () (zip-up [1] [])))
    (is (= () (zip-up [] [1])))))

(deftest test-to-int
  (testing "common cases - string"
    (is (= 41211 (to-int "+41211")))
    (is (= -55 (to-int "-55")))
    (is (= 0 (to-int "0")))
    (is (= 812 (to-int "812")))
    (is (= 7 (to-int "7"))))
  (testing "edge cases"
    (is (= nil (to-int nil)))))

(deftest test-index-of
  (testing "common cases"
    (is (= 0 (index-of [9 8 7 6] 9)))
    (is (= 1 (index-of [:a :b :c]  :b)))
    (is (= 5 (index-of ["hat" "bat" "cat" "sat" "cravat" "mat"] "mat")))
    (is (= -1 (index-of [:a :b :c] :z)))
    (is (= 0 (index-of '(9 8 7 6) 9)))
    (is (= 1 (index-of '(:a :b :c)  :b)))
    (is (= 5 (index-of '("hat" "bat" "cat" "sat" "cravat" "mat") "mat")))
    (is (= -1 (index-of '(:a :b :c) :z)))))

(deftest test-flat-set
  (testing "common cases"
    (is (= #{1 2 3 4 5} (flat-set [1 2 3 4 5])))
    (is (= #{1 2 3 4 5} (flat-set [1 [2 3 4] 5])))
    (is (= #{1 2 3 4 5} (flat-set [1 2 3 4 5 3 4 5])))
    (is (= #{1 2 3 4 5} (flat-set [1 [2 3 4 5] 3 4 5])))))

(deftest test-char-range
  (testing "common cases"
    (is (= '(\a \b \c \d \e) (char-range \a \e)))))

(deftest test-to-lower
  (testing "common cases - char"
    (is (= \a (to-lower \A)))
    (is (= \a (to-lower \a))))
  (testing "common cases - string"
    (is (= "abc" (to-lower "ABC")))
    (is (= "abc" (to-lower "aBc"))))
  (testing "edge cases"
    (is (= nil (to-lower nil)))))

(deftest test-to-upper
  (testing "common cases - char"
    (is (= \A (to-upper \a)))
    (is (= \A (to-upper \A))))
  (testing "common cases - string"
    (is (= "ABC" (to-upper "abc")))
    (is (= "ABC" (to-upper "aBc"))))
  (testing "edge cases"
    (is (= nil (to-upper nil)))))

(deftest test-transpose
  (testing "common cases"
    (is (= [[1 4 7] [2 5 8] [3 6 9]] (transpose [[1 2 3] [4 5 6] [7 8 9]])))
    (is (= [[1 4 7 10] [2 5 8 11] [3 6 9 12]] (transpose [[1 2 3] [4 5 6] [7 8 9] [10 11 12]])))
    (is (= [[1 4] [2 5] [3 6]] (transpose [[1 2 3] [4 5 6]])))
    ;; For jagged vectors, drop elements past the shortest row size.
    (is (= [[1 4] [2 5]] (transpose [[1 2 3] [4 5]])))
    (is (= [[1 3] [2 4]] (transpose [[1 2] [3 4 5]])))))

(deftest test-update-multi
  (testing "common cases"
    (is (= {"a" "foo", "b" "bar", "c" "bang"} (update-multi  {"a" "  foo ", "b" "   bar", "c" "bang "} ["a" "b" "c"] str/trim)))
    (is (= {:a 2 :b 3 :c 4} (update-multi  {:a 1 :b 2 :c 3} [:a :b :c] inc)))))
