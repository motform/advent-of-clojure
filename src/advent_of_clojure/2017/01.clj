(ns advent-of-clojure.2017.01
  (:require [clojure.string :as str]
            [nextjournal.clerk :as clerk]))

;; ### Description
;; The captcha requires you to review a sequence of digits (your puzzle input) and find the sum of all digits that match the next digit in the list.
;;
;; The list is circular, so the digit after the last digit is the first digit in the list.

(def input (butlast (map parse-long (-> "resources/2017/01.dat" slurp (str/split #"")))))

(defn matches-next? [xs i x]
  (if (= x (nth xs (inc i) (first xs))) x 0))

(defn captcha [xs]
  (->> xs
       (map-indexed (partial matches-next? xs))
       (reduce +)))

;; ### Examples
;; 1122 produces a sum of 3 (1 + 2) because the first digit (1) matches the second digit and the third digit (2) matches the fourth digit.
(captcha [1 1 2 2])
;; 1111 produces 4 because each digit (all 1) matches the next.
(captcha [1 1 1 1])
;; 1234 produces 0 because no digit matches the next.
(captcha [1 2 3 4])
;; 91212129 produces 9 because the only digit that matches the next one is the last digit, 9.
(captcha [9 1 2 1 2 1 2 9])

;; ##### Solution
(def part-one captcha input)

;; ### Part Two

(defn matches-halfway? [xs i x]
  (let [i (mod (+ i (/ (count xs) 2)) (count xs))]
    (if (= x (nth xs i))
      x 0)))

(defn captcha' [xs]
  (->> xs
       (map-indexed (partial matches-halfway? xs))
       (reduce +)))

;; 1212 produces 6: the list contains 4 items, and all four digits match the digit 2 items ahead.
(captcha' [1 2 1 2])
;; 1221 produces 0, because every comparison is between a 1 and a 2.
(captcha' [1 2 2 1])
;; 123425 produces 4, because both 2s match each other, but no other digit has a match.
(captcha' [1 2 3 4 2 5])
;; 123123 produces 12.
(captcha' [1 2 3 1 2 3])
;; 12131415 produces 4.
(captcha' [1 2 1 3 1 4 1 5])

;; #### Solution
(def part-two captcha' input)
