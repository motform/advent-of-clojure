(ns advent-of-clojure.2020.five
  (:require [clojure.string :as str]
            [clojure.set :as set]))

(def seats (->> "resources/2020/five.dat" slurp str/split-lines (map #(re-seq #"[F|B]+|[L|R]+" %))))

(defn bsp [hi xs]
  (loop [lo 0 hi hi [x & xs] xs]
    (case x
      (\F \L) (recur lo (+ lo (quot (- hi lo) 2)) xs)
      (\B \R) (recur (+ lo (inc (quot (- hi lo) 2))) hi xs)
      lo)))

(def IDs (->> seats (map (fn [[row col]] (+ (bsp 7 col) (* 8 (bsp 127 row)))))))

(def one (apply max IDs))

(def two (set/difference (into #{} (range (apply min IDs) (inc (apply max IDs))))
                         (into #{} IDs)))
