(ns advent-of-clojure.2015.03
  (:require [clojure.string :as str]))

(def data
  (-> "resources/2015/three.dat" slurp str/trimr (str/split #"")))

(def moves
  {"^" [1  0] ">" [0  1]
   "<" [0 -1] "v" [-1 0]})

(def part-one
  (->> data
      (map moves)
      (reductions #(mapv + %1 %2) [0 0])
      set
      count))

(def part-two
  (->> data
      (map moves)
      (partition 2)
      (reductions #(map (partial map +) %1 %2)
                  '([0 0] [0 0]))
      (apply concat)
      set
      count))
