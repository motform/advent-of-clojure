(ns advent-of-clojure.2015.07
  (:require [clojure.string :as str]))

(def example (->> "123 -> x\n
456 -> y\n
x AND y -> d\n
x OR y -> e\n
x LSHIFT 2 -> f\n
y RSHIFT 2 -> g\n
NOT x -> h\n
NOT y -> i\n" str/split-lines (remove str/blank?) (map #(str/split % #" "))))

(def instructions
  (->> "resources/2015/seven.dat" slurp str/split-lines (map #(str/split % #" "))))

(first instructions)

(def gate->fn
  {"RSHIFT" bit-shift-right
   "LSHIFT" bit-shift-left
   "OR"     bit-or
   "AND"    bit-and
   "NOT"    (partial bit-and-not 16rFFFF)})

(defn wire? [circuit wire]
  (let [w (read-string wire)]
    (if (number? w) w (circuit wire 0))))

(defn parse-instruction [[a b c d e :as instruction]]
  (case (count instruction)
    3 (fn [circuit] (assoc circuit c (wire? circuit a))) ; assignments
    4 (fn [circuit] (assoc circuit d ((gate->fn a) (circuit b 0)))) ; not
    5 (fn [circuit] (assoc circuit e ((gate->fn b) (circuit a 0) (wire? circuit c))))))  ; OR/AND

(defn wire-circuit [instructions]
  (reduce #((identity %2) %1) {} instructions))

(def part-one (get (->> instructions (map parse-instruction) wire-circuit) "a"))
