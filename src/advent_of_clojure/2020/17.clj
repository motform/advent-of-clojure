(ns advent-of-clojure.2020.17
  (:require [clojure.string :as str]))

(def initial-state (-> "resources/2020/17.dat" slurp str/split-lines))

(def t (str/split-lines ".#.
..#
###"))

(defn parse-map [map]
  (reduce
   (fn [m [z x cube]]
     (assoc-in m [0 z x] cube))
   {}
   (->> map (map-indexed (fn [i xs] (map-indexed (partial vector i) xs))) (apply concat))))

(defn parse-state [map]
  {:w [0 (dec (-> map first count))]
   :h [0 (dec (-> map count))]
   :d [0 0]
   :space (parse-map map)})

(defn- neighbours' [x y z]
  (for [x' (range (dec x) (+ 2 x))
        y' (range (dec y) (+ 2 y))
        z' (range (dec z) (+ 2 z))
        :when (not= [x' y' z'] [x y z])]
    [z' y' x']))

(defn neighbours [space cube]
  (->> (apply neighbours' cube)
       (map (fn [[z y x]] (get-in space [z y x] \.)))
       frequencies))

(defn cells [[w-min w-max] [h-min h-max] [d-min d-max]]
  (for [z (range d-min (inc d-max))
        y (range w-min (inc w-max))
        x (range h-min (inc h-max))]
    [z y x]))

(defn grow [[min max]]
  [(dec min) (inc max)])

(defn update-cubes [cells space]
  (reduce
   (fn [m [z y x]]
     (let [cube? (= \# (get-in space [z y x] \.))
           nbs (get (neighbours space [z y x]) \# 0)]
       (assoc-in m [z y x] (cond (and cube? (<= 2 nbs 3))    \#
                                 (and (not cube?) (= 2 nbs)) \#
                                 :else \.))))
   {} cells))

#(defn print-state [{:keys [w h d space] :as state}]
   (->> (cells w h d)
        (map (fn [[y z x]] (get-in space [y z x] \.)))
        (partition ((fn [[x y]] (inc (+ y (- x)))) w))
        (partition ((fn [[x y]] (inc (+ y (- x)))) d))
        (run! (fn [row] (println) (run! println row))))
   state)

(defn run-simulation [{:keys [w h d space] :as state}]
  ;; there might be a bug here where we are not using the growed vals
  (-> state
      (update :w grow)
      (update :h grow)
      (update :d grow)
      (assoc :space (update-cubes (cells w h d) space))
      print-state))

(defn simulate [cycles inital-state]
  (last (take cycles (iterate run-simulation (print-state inital-state)))))

;; (def one (count (simulate initial-state 6)))

