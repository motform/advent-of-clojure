(ns advent-of-clojure.2022.04
  (:require [clojure.set :as set]
            [clojure.string :as str]))

(def parse-digits
  (comp (partial mapv parse-long)
        (partial re-seq #"\d+")))

(def input (->> "resources/2022/04.dat" slurp str/split-lines (mapv parse-digits)))

(defn in-bounds? [[x1 y1 x2 y2]]
  (or (and (<= x2 x1) (>= y2 y1))
      (and (<= x1 x2) (>= y1 y2))))

(def part-one (->> input (filter in-bounds?) count))

(defn overlaps? [[x1 y1 x2 y2]]
  (seq (set/intersection
    (set (range x1 (inc y1)))
    (set (range x2 (inc y2))))))

(def part-two (->> input (filter overlaps?) count))

