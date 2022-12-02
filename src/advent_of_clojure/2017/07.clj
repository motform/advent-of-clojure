(ns advent-of-clojure.2017.07
  (:require [clojure.string :as str]))

(def input (->> "resources/2017/07.dat" slurp str/split-lines))

(defn leaf? [program] (not (:children program)))

(defn parse-program [program]
  (let [[name weight & children] (re-seq #"\w+|\(\n+\)" program)]
    (cond-> {:name name :weight (parse-long weight)}
      children (assoc :children (set children)))))

(defn find-base [programs]
  (let [roots (remove leaf? programs)
        names (mapv :name roots)
        branches (->> roots (mapcat :children) (into #{}))]
    (->> names (remove branches) first)))

(def part-one (->> input (mapv parse-program) find-base))

(defn index-by [f ms]
  (reduce #(assoc %1 (f %2) %2) {} ms))

(defn child-weights [index node]
  (->> node :children (map #(:weight (get index %))) (apply +)))

(defn calculate-weights [programs]
  (let [child-weights (partial child-weights (index-by :name programs))]
    (mapv #(assoc % :tree-weight (+ (:weight %) (child-weights %))) programs)))

(defn find-inbalance [programs]
  (let [weights (calculate-weights programs)]
    (->> weights
         )))

(->> example str/split-lines (mapv parse-program) find-inbalance)
(->> input (mapv parse-program) find-inbalance)


(def part-one (->> input (mapv parse-program) find-base))

(def example
  "pbga (66)
xhth (57)
ebii (61)
havc (66)
ktlj (57)
fwft (72) -> ktlj, cntj, xhth
qoyq (66)
padx (45) -> pbga, havc, qoyq
tknk (41) -> ugml, padx, fwft
jptl (61)
ugml (68) -> gyxo, ebii, jptl
gyxo (61)
cntj (57)")
