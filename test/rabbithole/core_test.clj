(ns rabbithole.core-test
  (:require [clojure.test :refer :all]
            [rabbithole.core :refer :all]))

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
    (is (= () (zip-up [1] [])))))

(deftest test-to-int
  (testing "common cases"
    (is (= 41211 (to-int "+41211")))
    (is (= -55 (to-int "-55")))
    (is (= 0 (to-int "0")))
    (is (= 812 (to-int "812")))
    (is (= 7 (to-int "7"))))
  (testing "edge cases"
    (is (= nil (to-int nil)))))

(deftest test-coll-index
  (testing "common cases"
    (is (= 0 (coll-index [9 8 7 6] 9)))
    (is (= 1 (coll-index [:a :b :c]  :b)))
    (is (= 5 (coll-index ["hat" "bat" "cat" "sat" "cravat" "mat"] "mat")))
    (is (= -1 (coll-index [:a :b :c] :z)))
    (is (= 0 (coll-index '(9 8 7 6) 9)))
    (is (= 1 (coll-index '(:a :b :c)  :b)))
    (is (= 5 (coll-index '("hat" "bat" "cat" "sat" "cravat" "mat") "mat")))
    (is (= -1 (coll-index '(:a :b :c) :z)))))

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
