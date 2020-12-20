(ns advent-of-clojure.2015.07
  (:require [clojure.string :as str]))

(def instructions
  (->> "resources/2015/seven.dat" slurp str/split-lines (map #(str/split % #" "))))

(def gate->fn
  {"RSHIFT" bit-shift-right
   "LSHIFT" bit-shift-left
   "OR"  bit-or
   "AND" bit-and
   "NOT" bit-not})

(defn wire? [m wire]
  (let [w (read-string wire)]
    (if (number? w) w (m wire))))

(defn parse-instruction [[a b c d e :as i]]
  (case (count i)
    3 (fn [m] (assoc m c a)) ; assignments
    4 (fn [m] (assoc m d ((gate->fn a) (m b)))) ; not
    5 (fn [m] (assoc m e ((gate->fn b) ; else
                          (get m a) (get m (wire? m (i 2))))))))

(defn wire-circuit [instructions]
  (reduce #((identity %2) %1) {} instructions))

(def part-one
  (->> instructions
      (map parse-instruction)
      wire-circuit
      (get "e")))
