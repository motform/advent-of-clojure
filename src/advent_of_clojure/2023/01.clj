(ns advent-of-clojure.2023.01
  (:refer-clojure :exclude [number?])
  (:require [clojure.string :as str]))

(def input (-> "resources/2023/01.dat" slurp (str/split-lines)))

(def number?
  (re-pattern #"\d"))

(def number-or-word?
  (re-pattern #"(?=(\d|one|two|three|four|five|six|seven|eight|nine))"))

(defn digits-from [pattern]
  (fn [row]
    (let [xs (re-seq pattern row)]
      (map last ; last is the capture group
        [(first xs) (if-not (zero? (count xs))
                      (last xs) (first xs))]))))


(defn ->number [digits]
  (parse-long (apply str digits)))

(def word->number
  {"one" "1" "two" "2" "three" "3" "four" "4" "five" "5" "six" "6" "seven" "7" "eight" "8" "nine" "9"})

(defn decode-number-string [s]
  (if (re-matches number? s) s
    (word->number s)))

(defn part-one [input]
  (let [xf (comp (map (digits-from number?)) (map ->number))]
    (transduce xf + input)))

(defn part-two [input]
  (let [xf (comp
             (map (digits-from number-or-word?))
             (map (partial map decode-number-string))
             (map ->number))]
    (transduce xf + input)))

(comment
  (time (part-one input))
  (time (part-two input))
  )


"Elapsed time: 2.334783 msecs"
"Elapsed time: 5.455636 msecs"
