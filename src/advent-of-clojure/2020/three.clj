(ns advent-of-clojure.2020.three
  (:require [clojure.string :as str]))

(def geography (->> "resources/2020/three.dat" slurp str/split-lines))

(defn traverse [geography x-step y-step]
  (let [width (count (first geography))]
    (->> (map (fn [x y]
                (nth (geography y) (mod x width)))
              (range x-step 9999 x-step)
              (range y-step (count geography) y-step))
         (filter #{\#})
         count)))

(def one (traverse geography 3 1))

(def variations [[1 1] [3 1] [5 1] [7 1] [1 2]])

(def two (apply * (map #(apply traverse geography %) variations)))
