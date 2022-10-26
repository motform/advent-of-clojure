(ns advent-of-clojure.2015.17
  (:require [clojure.string :as str]
            [clojure.math.combinatorics :as combo]))

(def containers
  (->> "resources/2015/seventeen.dat" slurp str/split-lines (map read-string)))

(defn filled? [limit subset]
  (= limit (reduce + (map second subset)))) 

(defn duplicate-subsets [xs]
  (combo/subsets (map-indexed #(vector %1 %2) xs)))

(def part-one
  (->> containers
      duplicate-subsets
      (filter (partial filled? 150))
      count))

(def part-two
  (->> containers
      duplicate-subsets
      (filter (partial filled? 150))
      (map count)
      frequencies
      (into (sorted-map))
      ffirst))

