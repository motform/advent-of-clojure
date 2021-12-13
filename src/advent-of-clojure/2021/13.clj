(ns advent-of-clojure.2021.13
  (:require [clojure.string :as str]))

(def input (-> "resources/2021/13.dat" slurp))

(defn parse-input [input]
  (let [[points folds] (map str/split-lines (str/split input #"\n\n"))]
    {:points (into #{} (map (comp (partial mapv parse-long)
                                  #(str/split % #",")) points))
     :folds  (map (comp (fn [[dir x]] [(keyword dir) (parse-long x)])
                        #(re-seq #"[xy]|\d+" %)) folds)}))

(defn fold-point [[axis fold] [x y :as point]]
  (case axis
    :x (if (< fold x) [(- x (* 2 (- x fold))) y] point)
    :y (if (< fold y) [x (- y (* 2 (- y fold)))] point)))

(defn print-map [points]
  (map (comp println (partial apply str))
       (partition (inc (apply max (map first points)))
                  (for [y (range (inc (apply max (map second points))))
                        x (range (inc (apply max (map first points))))]
                    (if (points [x y]) " # " " . ")))))

(defn fold [{:keys [points folds]}]
  (reductions
   (fn [points fold]
     (set (map (partial fold-point fold) points)))
   points folds))

(def part-one (-> input parse-input fold second count))

(def part-two (-> input parse-input fold last print-map))
