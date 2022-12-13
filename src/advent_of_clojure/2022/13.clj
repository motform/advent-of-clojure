(ns advent-of-clojure.2022.13
  (:require [clojure.string :as str]))

(def input (->> "resources/2022/13.dat" slurp str/split-lines (remove str/blank?) (mapv read-string)))

(defn int-int [left right]
  (cond (= left right) :continue
        (< left right) :correct
        :else :incorrect))

(defn order [[lefts rights]]
  (loop [state :continue
         [left & lefts] lefts
         [right & rights] rights]
    (if-not (= state :continue) state
            (let [pair [left right]]
              (cond
                ;; End of list
                (every? nil? pair) :continue
                (nil? left)  :correct
                (nil? right) :incorrect
                ;; Comparable input 
                (every? int? pair)    (recur (int-int left right) lefts rights)
                (every? vector? pair) (recur (order [left right]) lefts rights)
                ;; Turn into a list
                (int? left)  (recur (order [[left] right]) lefts rights)
                (int? right) (recur (order [left [right]]) lefts rights))))))

(defn ordered?
  ([packets] (case (order packets) :correct true :incorrect false))
  ([left right] (ordered? [left right])))

(def part-one
  (->> input
       (partition 2)
       (map-indexed (fn [i pair] [(inc i) (ordered? pair)]))
       (keep (fn [[i ordered?]] (when ordered? i)))
       (reduce +)))

(def part-two
  (->> (conj input [[2]] [[6]])
       (sort ordered?)
       (map-indexed (fn [i packet] [(inc i) (#{[[2]] [[6]]} packet)]))
       (keep (fn [[i ordered?]] (when ordered? i)))
       (apply *)))
