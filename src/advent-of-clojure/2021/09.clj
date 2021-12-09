(ns advent-of-clojure.2021.09
  (:require [clojure.string :as str]
            [clojure.set :as set]))

(def input (->> "resources/2021/09.dat" slurp str/split-lines (mapv (comp (partial mapv parse-long) #(str/split % #" *")))))

(defn nnth? [xs x y]
  (get (get xs y nil) x nil))

(defn adjecent [matrix]
  (let [nnth? (partial nnth? matrix)]
    (map-indexed
     (fn [y row]
       (map-indexed
        (fn [x n]
          [n (remove nil? [(nnth? (dec x) (dec y))  (nnth? x (dec y))  (nnth? (inc x) (dec y))
                           (nnth? (dec x) y)        #_ij               (nnth? (inc x) y)
                           (nnth? (dec x) (inc y))  (nnth? x (inc y))  (nnth? (inc x) (inc y))])])
        row)) matrix)))

(def part-one (->> input
                   adjecent
                   (apply concat)
                   (filter (fn [[x adjecent]] (every? #(< x %) adjecent)))
                   (map (comp inc first))
                   (reduce +)))

(defn in-basin? [matrix x y]
  (when-let [point (nnth? matrix x y)]
    (when (< point 9) point)))

(defn flood-fill' [matrix seen x y]
  (when-let [point (and (not (seen [x y]))
                        (in-basin? matrix x y))]
    (conj! seen [x y])
    (concat [point]
            (flood-fill' matrix seen x (dec y))     ; down
            (flood-fill' matrix seen x (inc y))     ; up
            (flood-fill' matrix seen (inc x) y)     ; right
            (flood-fill' matrix seen (dec x) y))))  ; left

(defn flood-fill [matrix x y]
  (let [seen (transient #{})]
    (flood-fill' matrix seen x y)
    (when-not (= 0 (count seen))
      (persistent! seen))))

(defn points [matrix]
  (->> matrix (map-indexed (fn [y row] (map-indexed (fn [x _] [x y]) row))) (apply concat)))

(def part-two (->> input
                   points
                   (keep (partial apply flood-fill input))
                   set
                   (map count)
                   sort
                   reverse
                   (take 3)
                   (reduce *)))
