(ns advent-of-clojure.2019.three
  (:require [clojure.string :as s]
            [clojure.set :refer [intersection]]
            [clojure.math.numeric-tower :refer [abs]]))

(def paths
  (->> "resources/three.dat" slurp s/split-lines (map #(s/split % #","))))

(def moves {\R [1 inc] \L [1 dec] \U [0 inc] \D [0 dec]})

(defn manhattan-distance [points]
  (reduce + (map abs points)))

(defn intersection-distance [points intersection]
  (inc (count (take-while (partial not= intersection) points))))

(defn path->points [path start]
  (let [[i f] (get moves (first path))
        steps (read-string (subs path 1))]
    (->> start (iterate #(update % i f)) (drop 1) (take steps))))

(defn paths->points [paths]
  (loop [paths paths points [[0 0]]]
    (if (empty? paths) points
        (recur (rest paths)
               (concat points (path->points (first paths) (last points)))))))

(defn shortest-path [paths]
  (let [points (->> paths (map paths->points) (map rest) (map #(set %)))
        manhattan-distances (map manhattan-distance (apply intersection points))]
    (apply min manhattan-distances)))

(defn lowest-delay [paths]
  (let [points (->> paths (map paths->points) (map rest))
        intersections (apply intersection (map #(set %) points))
        fst (map #(intersection-distance (first points) %) intersections)
        snd (map #(intersection-distance (second points) %) intersections)
        distances (map #(reduce + %) (partition 2 (interleave fst snd)))]
    (apply min distances)))
