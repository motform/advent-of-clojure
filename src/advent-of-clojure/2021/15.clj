(ns advent-of-clojure.2021.15
 (:require [clojure.string :as str]
  [clojure.data.priority-map :refer [priority-map]]))

(def input (-> "resources/2021/15.dat" slurp))

(defn parse-input [input] (->> input str/split-lines (mapv (partial mapv #(Character/getNumericValue %)))))

(defn bounds [matrix] 
 [(count (first matrix)) (count matrix)])

(defn points [[max-x max-y]]
 (for [x (range max-x) y (range max-y)] [x y]))

(defn adjecent [[max-x max-y] point]
 (let [in-bounds? (fn [[x y]] (and (<= 0 x (dec max-x)) (<= 0 y (dec max-y))))]
  (filter in-bounds? (mapv (partial mapv +) (repeat point) [[0 -1] [-1 0] [1 0] [0 1]]))))

(defn matrix->map [matrix]
 (let [points (points (bounds matrix))]
  (apply hash-map (interleave points (flatten matrix)))))

(defn tentative-distances [distances weights current nodes]
 (let [current (distances current)]
  (reduce (fn [distances node]
           (assoc distances node (min (distances node ##Inf) (+ current (weights node)))))
  distances nodes)))

(defn smallest-tentative-distance [seen? distances]
 (some (fn [[node _]] (when-not (seen? node) node)) distances))

(defn dijkstra [start matrix] ; wait, it just hit me that this is just manhattan distances? oh well
 (let [weights (matrix->map matrix)
       distances (priority-map start 0)
       adjecent (partial adjecent (bounds matrix))
       end (mapv dec (bounds matrix))]
  (loop [seen? #{start}
         distances distances
         current start]
   (let [adjecent (adjecent current)
         distances (->> adjecent (remove seen?) (tentative-distances distances weights current))
         seen? (conj seen? current)]
    (if (seen? end) (distances end)
     (recur seen? distances (smallest-tentative-distance seen? distances)))))))

(def part-one (->> input parse-input (dijkstra [0 0])))

(defn next-matrix [matrix] 
 (mapv (partial mapv #(inc (mod % 9))) matrix))

(defn ->row [xs]
 (let [cols (count xs)
       rows (count (first xs))]
  (map #(apply concat (map (partial nth (apply concat xs)) (range % (* cols rows) rows))) (range rows))))

(defn expand-matrix [n matrix]
 (let [row (iterate next-matrix matrix)]
  (map #(drop % (take (+ n %) row)) (range n))))

(def part-two (->> input parse-input (expand-matrix 5) (map ->row) (apply concat) (dijkstra [0 0])))