(ns advent-of-clojure.2021.07
  (:require [clojure.string :as str]
            [clojure.java.math :as math]))

(def input (map read-string (-> "resources/2021/07.dat" slurp (str/split #","))))

(defn distance [x target]
  (math/abs (- target x)))

(defn min-fuel [distance-f xs]
  (->> (for [targets (range (apply min xs) (apply max xs))
             x xs]
         (distance-f targets x))
       (partition-all (count xs))
       (pmap (partial reduce +))
       (apply min)))

(def part-one (min-fuel distance input))

(def accumuliative-distnace
  (memoize
   (fn [x target]
     (apply + (range (inc (distance x target)))))))

(def part-two (time (min-fuel accumuliative-distnace input)))
