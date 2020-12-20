(ns advent-of-clojure.2019.04)

(def input (range 356261 846304))

(defn explode-int [x]
  (map read-string (map #(str %) (vec (str x)))))

(defn includes-pair? [xs]
  (->> xs frequencies vals (some #(= 2 %))))

(defn pairs [xs]
  (drop-last (interleave (partition 2 xs) (partition-all 2 (rest xs)))))

(defn increased? [[x y]] (<= x y))

(defn increases? [xs] (every? true? (map increased? xs)))

(defn checks? [xs]
  (when (and (includes-pair? xs)
             (increases? (pairs xs))) xs))

(defn valid-passwords [input]
  (->> input (map explode-int) (filter checks?) count))
