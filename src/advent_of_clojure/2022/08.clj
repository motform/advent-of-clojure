(ns advent-of-clojure.2022.08
  (:require [clojure.string :as str]))

(defn explode-numbers [str]
  (->> str (re-seq #"\d") (mapv parse-long)))

(def input (->> "resources/2022/08.dat" slurp str/split-lines (mapv explode-numbers)))

(defn points [grid] ; sans edge
  (for [x (range 1 (-> grid first count dec))
        y (range 1 (-> grid count dec))]
    [x y]))

(defn transpose [matrix]
  (apply mapv vector matrix))

(defn directions [rows columns [y x]]
  [#_left   (reverse (subvec (rows y)   0        x))
   #_right           (subvec (rows y)   (inc x)  (count (first rows)))
   #_top    (reverse (subvec (columns x) 0       y))
   #_bottom          (subvec (columns x) (inc y) (count (first columns)))])

(defn visible? [height direction]
  (> height (apply max direction)))

(defn visibility [rows columns points]
  (let [height (get-in rows points)]
    (->> points
         (directions rows columns)
         (filter (partial visible? height)))))

(defn visible-trees [rows]
  (->> (points rows)
       (map (partial visibility rows (transpose rows)))
       (remove empty?)
       count))

(defn edge [grid]
  (- (* 2 (+ (count grid) (count (first grid)))) 4))

(def part-one (+ (edge input) (visible-trees input)))

(defn visible-trees' [height trees]
  (let [sight (take-while #(> height %) trees)]
    (if (= sight trees) (count sight) (inc (count sight)))))

(defn senic-score [rows columns points]
  (let [height (get-in rows points)]
    (->> points
         (directions rows columns)
         (map (partial visible-trees' height)))))

(defn senic-scores [rows]
  (map (partial senic-score rows (transpose rows)) (points rows)))

(def part-two
  (->> input
       senic-scores
       (map (partial apply *))
       (apply max)))
