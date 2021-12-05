(ns advent-of-clojure.2021.05
  (:require [clojure.string :as str]))

(def input (->> "resources/2021/05.dat" slurp str/split-lines (mapv (comp #(mapv read-string %)
                                                                          #(re-seq #"\d+" %)))))

(defn ortho? [[x1 y1 x2 y2]]
  (or (= x1 x2) (= y1 y2)))

(defmulti path ; You can obviously make due with one of these, but it's Sunday.
  (fn [points]
    (ortho? points)))

(defmethod path true [[x1 y1 x2 y2]]
  (for [x (range (min x1 x2) (inc (max x1 x2)))
        y (range (min y1 y2) (inc (max y1 y2)))]
    [x y]))

(defn points [p1 p2]
  (let [points (range (min p1 p2) (inc (max p1 p2)))]
    (if (> p1 p2)
      (reverse points)
      points)))

(defmethod path false [[x1 y1 x2 y2]]
  (partition 2 (interleave (points x1 x2) (points y1 y2))))

(def part-one (->> input
                   (filter ortho?)
                   (mapcat path)
                   frequencies
                   (filter (fn [[_ v]] (<= 2 v)))
                   count))

(def part-two (->> input
                   (mapcat path)
                   frequencies
                   (filter (fn [[_ v]] (<= 2 v)))
                   count))
