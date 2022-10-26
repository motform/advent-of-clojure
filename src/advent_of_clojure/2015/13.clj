(ns advent-of-clojure.2015.13
  (:require [clojure.string :as str]
            [clojure.math.combinatorics :refer [permutations]]))

(defn split-guest [guest]
  (re-find #"(\w+) would (\w+) (\d+) happiness units by sitting next to (\w+)." guest))

(defn format-guest [[_ from action value to]]
  [from to (({"gain" + "lose" -} action) (read-string value))])

(def parse-guest
  (comp format-guest split-guest))

(defn arrangements [guests]
  (permutations (distinct (apply concat (keys guests)))))

(defn complete-arrangenment [arrangement]
  (partition 3 1 (concat [(last arrangement)] arrangement [(first arrangement)])))

(defn happiness [guests arrangement]
  (apply + (mapcat (fn [[left middle right]]
                     [(guests [middle left] 0) (guests [middle right] 0)])
                   (complete-arrangenment arrangement))))

(def guests
  (->> "resources/2015/thirteen.dat" slurp str/split-lines (map parse-guest)
      (reduce (fn [m [from to value]] (assoc m [from to] value)) {})))

(def part-one
  (apply max (pmap #(happiness guests %) (arrangements guests))))

(def part-two
  (apply max (pmap #(happiness guests %)
                   (permutations (cons :me (first (arrangements guests)))))))
