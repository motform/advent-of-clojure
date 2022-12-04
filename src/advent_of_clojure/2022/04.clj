(ns advent-of-clojure.2022.04
  (:require [clojure.string :as str]))

(def parse-digits
  (comp (partial mapv parse-long)
        (partial re-seq #"\d+")))

(def input (->> "resources/2022/04.dat" slurp str/split-lines (mapv parse-digits)))

(defn in-bounds? [[x1 y1 x2 y2]]
  (or (<= x1 x2 y2 y1)
      (<= x2 x1 y1 y2)))

(def part-one (->> input (filter in-bounds?) count))

(defn overlaps? [[x1 y1 x2 y2]]
  (or (<= x1 y2 y1)
      (<= x2 y1 y2)))

(def part-two (->> input (filter overlaps?) count))

