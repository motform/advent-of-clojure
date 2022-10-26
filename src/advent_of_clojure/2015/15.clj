(ns advent-of-clojure.2015.15
  (:require [clojure.string :as str]))

(defn properties [recipie]
  (->> recipie (re-seq #"(-*[\d])") (map (comp read-string first)) flatten))

(defn transpose [matrix]
  (apply map vector matrix))

(defn meal-replacement? [cookie]
  (let [calories (last cookie)]
    (if (= 500 calories) (drop-last cookie) [0])))

(defn cookie [ingredients recipie]
  (->> recipie
      (map #(map (partial * %2) %1) ingredients)
      transpose
      (map (comp #(if (neg? %) 0 %)
                 (partial apply +)))
      meal-replacement? ; comment this get part-one
      (apply *)))

(def ingredients
  (->> "resources/2015/fifteen.dat" slurp str/split-lines (map properties)))

(def recipies
  (let [r (range 1 101)]
    (->> (for [a r b r c r d r]
          [a b c d])
        (filter #(= 100 (reduce + %))))))

(def part-one
  (->> recipies
      (pmap (partial cookie (map drop-last ingredients)))
      (apply max)))

(def part-two
  (->> recipies
      (pmap (partial cookie ingredients))
      (apply max)))
