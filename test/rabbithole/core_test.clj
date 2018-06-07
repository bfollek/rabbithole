(ns rabbithole.core-test
  (:require [clojure.test :refer :all]
            [rabbithole.core :refer :all]))

(deftest test-potential-cycles
  (testing "potential-cycles common cases"
    (is (= '("ab" "aba" "abab")
           (potential-cycles "abab")))
    (is (= '("abab" "abab5" "abab56")
           (potential-cycles "abab56"))))                   ; 4 unique chars, so shortest potential cycle is 4 chars
  (testing "potential-cycles edge cases"
    (is (= '("")
           (potential-cycles "")))))

(deftest test-is-cycle-of?
  (testing "is-cycle-of? common cases"
    (is (not (is-cycle-of? "abab" "a")))
    (is (not (is-cycle-of? "abab" "abc")))
    (is (is-cycle-of? "abab7qw34" "abab7qw34"))
    (is (is-cycle-of? "abab" "abab"))
    (is (is-cycle-of? "abab" "ab")))
  (testing "is-cycle-of? edge cases"
    (is (is-cycle-of? "abab" "ababz"))                      ; cyc longer than s is okay
    (is (is-cycle-of? "" ""))))

(deftest test-find-cycle
  (testing "find-cycle common cases"
    (is (= "123456" (find-cycle "123456")))
    (is (= "ab" (find-cycle "abababab"))))
  (testing "find-cycle edge cases"
    (is (= "" (find-cycle "")))))
