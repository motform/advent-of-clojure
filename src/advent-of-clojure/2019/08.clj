(ns advent-of-clojure.2019.08
  (:require [clojure.string :as s]))

(def image
  (-> "resources/eight.dat" slurp s/trim (s/split #"") (as-> x (map read-string x))))

(defn read-image [x y image]
  (let [dim (* x y)]
    (partition dim image)))

(defn fewest-zeroes [x y image]
  (->> image (read-image 25 6) (map frequencies) (apply min-key #(get % 0 0))))

(defn transpose [matrix]
  (apply map vector matrix))

(def part-one
  (apply * (vals (select-keys (fewest-zeroes 25 6 image) [1 2]))))

(def part-two
  (->> image
      (read-image 25 6)
      transpose
      (map #(remove (partial = 2) %))
      (map first)
      (partition 25)
      (map (partial apply str))
      (map #(s/replace % #"1" "□"))
      (map #(s/replace % #"0" "■")))))

