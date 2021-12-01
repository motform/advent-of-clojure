(ns advent-of-clojure.2021.01
  (:require [clojure.string :as str]))

(def input (->> "resources/2021/01.dat" slurp str/split-lines (map read-string)))

(defn pairs [n step xs]
  (interleave (partition-all n step xs)
              (partition-all n step (rest xs))))

(defn compare-pairs [xs]
  (get (->> xs
            (pairs 2 2)
            (map (fn [[x y]] (compare x y)))
            frequencies) -1))

(def part-one (compare-pairs input))

(def part-two (->> input (pairs 3 1) (map #(apply + %)) compare-pairs))
