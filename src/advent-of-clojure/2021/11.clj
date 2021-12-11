(ns advent-of-clojure.2021.11
  (:require [clojure.string :as str]))

(def input (->> "resources/2021/11.dat" slurp str/split-lines (mapv (partial mapv (comp parse-long str)))))

(defn mmapv [f xs]
  (mapv (partial mapv f) xs))

(defn pairs [xs ys]
  (partition 2 (interleave xs ys)))

(defn flashing? [octopus]
  (> octopus 9))

(defn bounds [matrix]
  [(count (first matrix)) (count matrix)])

(defn points [[max-x max-y]]
  (for [x (range max-x)
        y (range max-y)]
    [x y]))

(defn neighbours [[max-x max-y] point]
  (let [in-bounds? (fn [[x y]] (and (<= 0 x (dec max-x)) (<= 0 y (dec max-y))))]
    (for [x (range -1 2)
          y (range -1 2)
          :let [point' (mapv + point [x y])]
          :when (and (not= point point') (in-bounds? point'))]
      point')))

(defn inc-area [population points]
  (reduce (fn [population x] (update-in population x inc)) population points))

(defn pass [population points flashed]
  (reduce (fn [[population flashed :as state] [point neighbours]]
            (let [octopus (get-in population point)]
              (if (and (flashing? octopus) (not (flashed point)))
                [(inc-area population neighbours) (conj flashed point)]
                state)))
          [population flashed] points))

(defn reset-flashed [population]
  (mmapv #(if (flashing? %) 0 %) population))

(defn generation [population points]
  (loop [population (mmapv inc population)
         flash #{}
         last-flash nil]
    (if (= flash last-flash)
      [(reset-flashed population) flash]
      (let [[population new-flash] (pass population points flash)]
        (recur population new-flash flash)))))

(defn simulate [population generations]
  (let [bounds (bounds population)
        points (points bounds)
        neighbours (pairs points (mapv (partial neighbours bounds) points))]
    (loop [population population
           flashes    []]
      (if (= (count flashes) generations)
        [population flashes]
        (let [[population flash] (generation population neighbours)]
          (recur population (conj flashes flash)))))))

(def part-one (->> (simulate input 100) second (apply concat) count))

(def part-two (let [octopi (count (apply concat input))]
                (->> (simulate input 400) second
                     (map-indexed (fn [i flashes]
                                    (when (= octopi (count flashes))
                                      i)))
                     (some identity)
                     inc)))
