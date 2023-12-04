(ns advent-of-clojure.util.matrix
  "All of these operations expect the point to be in `[y x]`,
  since this enables `(get-in matrix [y x])` in a col-row matrix."
  (:refer-clojure :exclude [- +])
  (:require [advent-of-clojure.util.vector :as v]))

(def bounds
  (memoize
   (fn [matrix]
     [(dec (count matrix))
      (dec (count (first matrix)))])))

(def in-bounds?
  (memoize
   (fn [[max-y max-x] [y x]]
     (and (<= 0 y max-y)
          (<= 0 x max-x)))))


(defn- neighbours' [deltas]
  (memoize
   (fn [bounds point]
     (->> (repeat point)
          (mapv v/+ deltas)
          (filter (partial in-bounds? bounds))))))

(def deltas [[1 0] [-1 0] [0 -1] [0 1]])
(def neighbours (neighbours' deltas))

(def diagonal-deltas
  [[-1 -1] [-1 0] [-1 1]
   [0  -1]        [0  1]
   [1  -1] [1  1] [1  0]])

(def diagonal-neighbours (neighbours' diagonal-deltas))

(defn map-indexed-matrix [f matrix]
  (map-indexed
   (fn [x row]
     (map-indexed
      (fn [y value]
        (f x y value))
      row))
   matrix))

(defn index-of [matrix value]
  (->> matrix
       (map-indexed-matrix (fn [x y item] [[x y] (= item value)]))
       (apply concat)
       (some (fn [[pos hit?]] (when hit? pos)))))

(defn indexes [matrix value]
  (->> matrix
       (map-indexed-matrix (fn [x y item] [[x y] (= item value)]))
       (apply concat)
       (keep (fn [[pos hit?]] (when hit? pos)))
       vec))
