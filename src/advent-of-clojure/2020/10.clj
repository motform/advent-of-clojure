(ns advent-of-clojure.2020.10
  (:require [clojure.string :as str]))

(def joltages (->> "resources/2020/10.dat" slurp str/split-lines (map read-string)))

(defn add-extremes [joltages]
  (conj joltages 0 (+ 3 (apply max joltages))))

(defn joltage-differences [joltages]
  (frequencies (map #(- %2 %1) joltages (rest joltages))))

(def one (->> joltages add-extremes sort joltage-differences vals (apply *)))
