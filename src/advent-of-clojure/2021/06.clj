(ns advent-of-clojure.2021.06
  (:require [clojure.string :as str]))

(def input (map read-string (-> "resources/2021/06.dat" slurp (str/split #","))))

(def +? (fnil + 0))

(defn next-generation [generation]
  (reduce-kv
   (fn [next-generation fish fishes]
     (if (zero? fish)
       (-> next-generation (update 6 +? fishes) (assoc 8 fishes))
       (update next-generation (dec fish) +? fishes)))
   {}
   generation))

(defn simulate [input generations]
  (->> input
       frequencies
       (iterate next-generation)
       (take (inc generations))
       last
       vals
       (apply +)))

(def part-one (simulate input 80))
(def part-two (simulate input 256))
