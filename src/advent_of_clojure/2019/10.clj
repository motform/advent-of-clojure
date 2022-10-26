(ns advent-of-clojure.10
  (:require [clojure.string :as str]))

(def sector
  (->> "resources/ten.dat" slurp str/split-lines (mapv #(comp first (str/split % #"")))))

(def test-input
  (->> ".#..#\n.....\n#####\n....#\n...##" str/split-lines (mapv #(str/split % #""))))

(defn map->asteroids [sector]
  (for [x (range (count (first sector))) ; W
        y (range (count sector)) ; H
        :when (= \# ((sector x) y))]
    [x y]))

(defn at-point [[x y] sector]
  ((sector x) y))

(defn line-of-sight [point asteroid sector]
  )

(defn part-one [sector]
  (->> sector
      asteroids
      (map (line-of-sight))

      ))


