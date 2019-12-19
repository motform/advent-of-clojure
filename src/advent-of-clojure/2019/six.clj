(ns advent-of-clojure.2019.six
  (:require [clojure.string :as str]))

(def orbits
  (->> "resources/six.dat"
      slurp
      s/split-lines
      (map #(str/split % #"\)"))
      (map (comp vec reverse))
      (into {})))

(defn traverse [g k]
  (take-while identity (rest (iterate g k))))

(def part-one
  (->> orbits
      keys
      (map (comp count (partial #(traverse orbits %))))
      (reduce +)))

(def part-two
  (let [start (traverse orbits "YOU")
        target (traverse orbits "SAN")
        paths (clojure.set/intersection (set start) (set target))]
    (->> paths
        (map #(+ (.indexOf start %) (.indexOf target %)))
        (apply min))))
