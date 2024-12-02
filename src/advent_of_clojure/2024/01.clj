(ns advent-of-clojure.2024.01
  (:require
    [clojure.string :as str]))

(defn parse [str]
  (->> str str/split-lines
       (reduce (fn [[left right] line]
                 (let [[l r] (->> line (re-seq #"\d+") (mapv parse-long))]
                   [(conj left l) (conj right r)]))
               [[] []])))

(defn part-1 [list]
  (->> list
       (map sort)
       (apply map #(abs (- %1 %2)))
       (apply +)))

(defn part-2 [[left right]]
  (let [similarities (frequencies right)]
    (->> left
         (map #(* % (similarities % 0)))
         (apply +))))

(comment
  (def input (parse (slurp "resources/2024/01.dat")))
  (part-1 input)
  (part-2 input)
 )
