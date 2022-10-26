(ns advent-of-clojure.2020.03
  (:require [clojure.string :as str]))

(def geography (->> "resources/2020/three.dat" slurp str/split-lines))

(defn traverse [geography x-step y-step]
  (->> (map #(nth (geography %2) %1)
            (iterate #(mod (+ x-step %) (count (first geography))) x-step)
            (range y-step (count geography) y-step))
       (filter #{\#})
       count))

(def one (traverse geography 3 1))

(def two (apply * (map #(apply traverse geography %) [[1 1] [3 1] [5 1] [7 1] [1 2]])))
