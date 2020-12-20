(ns advent-of-clojure.2015.01
  (:require [clojure.string :as str]))

(def floors {\) -1
             \( 1})

(def data
  (-> "resources/2015/one.dat" slurp char-array seq drop-last))

(def part-one (reduce + (map floors data)))

(count (for [x (reductions + (map floors data))
             :while (not= x -1)] [x]))

(count (take-while #(not= % -1) (reductions + (map floors data))))

(nth (reductions + (map floors data)) 1782)
