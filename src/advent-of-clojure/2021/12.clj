(ns advent-of-clojure.2021.12
  (:require [clojure.string :as str]))

(def input (->> "resources/2021/12.dat" slurp str/split-lines (map #(str/split % #"-"))))

(defn parse-input [input]
  (reduce (fn [m [to from]]
            (if (m to) (update m to conj from)
                (assoc m to #{from})))
          {} (concat input (map reverse input))))

(defn find-path [pred graph]
  (loop [paths #{}
         queue [["start"]]]
    (let [path (peek queue)
          cave (peek path)
          edges (mapv #(conj path %) (graph cave))]
      (cond (empty? queue)   paths
            (= cave "end")   (recur (conj paths path) (pop queue))
            (or (pred cave path) (= ["start"] path)) (recur paths (-> queue pop (concat edges) vec))
            :else            (recur paths (pop queue))))))

(defn small-cave? [[c]]
  (Character/isLowerCase c))

(defn valid-path? [cave path]
  (not (when (small-cave? cave)
         (not= 1 (get (frequencies path) cave 1)))))

(def part-one (->> input parse-input (find-path valid-path?) count))

(defn real-path? [_ path]
  (let [seen (frequencies path)
        occurances (->> seen (filter (fn [[cave _]] (small-cave? cave))) vals frequencies)]
    (and (>= 1 (seen "start" 0))
         (>= 1 (seen "end" 0))
         (>= 1 (get occurances 2 0))
         (nil? (get occurances 3)))))

(def part-two (->> input parse-input (find-path real-path?) count))
