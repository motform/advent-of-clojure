(ns advent-of-clojure.2015.nine-
  (:require [clojure.string :as str]
            [clojure.math.combinatorics :refer [permutations]]))

(def parse-locations
  (comp (fn [[x _ y _ w]] [x y w])
        #(str/split % #" ")))

(defn swap [xs i j]
  (assoc xs i (xs j) j (xs i)))

(defn mirror-locations [locations]
  (concat locations (map #(swap % 0 1) locations)))

(defn locations->wgraph [locations]
  (reduce (fn [m [x y d]]
            (assoc m #{x y} (read-string d)))
          {} locations))

(defn distance [locations path]
  (apply + (map (fn [from to]
                  (locations #{from to}))
                path (rest path))))

(defn paths [locations]
  (permutations (distinct (apply concat (keys locations)))))

(def locations
  (->> "resources/2015/nine.dat" slurp str/split-lines (map parse-locations) mirror-locations locations->wgraph))

(def part-one
  (apply min (map #(distance locations %) (paths locations))))

(def part-two
  (apply max (map #(distance locations %) (paths locations))))
