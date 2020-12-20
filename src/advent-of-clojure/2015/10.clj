(ns advent-of-clojure.2015.10)

(def seed 3113322113)

(defn digits [x]
  (map #(Character/getNumericValue %) (str x)))

(defn chain [digits]
  (->> digits (partition-by identity) (mapcat (juxt count first))))

(defn look-and-say [seed]
  (iterate chain seed))

(def part-one
  (->> seed digits look-and-say (drop 1) (take 40) last count))

(def part-two
  (->> seed digits look-and-say (drop 1) (take 50) last count))
