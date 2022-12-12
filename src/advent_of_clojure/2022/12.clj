(ns advent-of-clojure.2022.12
  (:require [clojure.string :as str]
            [advent-of-clojure.util.matrix :as m]
            [advent-of-clojure.util.data-structures :refer [queue]]))

(def start -14)
(def end -28)

(defn parse-line [s]
  (->> s char-array seq (mapv #(- (int %) 97))))

(def input (->> "resources/2022/12.dat" slurp str/split-lines (mapv parse-line)))

(defn possible-move? [matrix cmp elevation move]
  (let [to (get-in matrix move)
        to' (case to start 0 end 25 to)]
    (when (cmp (- to' elevation)) move)))

(defn possible-moves [matrix cmp seen pos]
  (let [elevation (max (get-in matrix pos) 0)
        elevation' (case elevation start 0 end 25 elevation)]
    (->> pos
         (m/neighbours (m/bounds matrix))
         (remove seen)
         (filterv (partial possible-move? matrix cmp elevation')))))

(defn search [matrix cmp targets start]
  (loop [q (queue start)
         seen {start 0}]
    (let [visiting (peek q)]
      (if (some #(= visiting %) targets)
        (seen visiting)
        (let [neighbours (possible-moves matrix cmp seen visiting)]
          (recur (into (pop q) neighbours)
                 (merge seen (zipmap neighbours (repeat (inc (seen visiting)))))))))))

(def part-one (search input
                      #(> 2 %)
                      [(m/index-of input end)]
                      (m/index-of input start)))

(def part-two (search input
                     #(<= -1 %)
                     (m/indexes input 0)
                     (m/index-of input end)))
