(ns advent-of-clojure.2017.02
  (:require [clojure.string :as str]))

;; ### Description
;; The spreadsheet consists of rows of apparently-random numbers.
;; To make sure the recovery process is on the right track, they need you to calculate the spreadsheet's checksum.
;; For each row, determine the difference between the largest value and the smallest value; the checksum is the sum of all of these differences.

(def input (->> (-> "resources/2017/02.dat" slurp str/split-lines)
                (mapv (comp #(mapv parse-long %)
                            #(str/split % #"\s")))))

;; ## Part one
(defn max-min [row]
  [(apply max row) (apply min row)])

(defn row-checksum [row]
  (->> row max-min (apply -)))

;;The first row's largest and smallest values are 9 and 1, and their difference is 8.
;;The second row's largest and smallest values are 7 and 3, and their difference is 4.
;;The third row's difference is 6.

(->> [[5 1 9 5] [7 5 3] [2 4 6 8]]
     (mapv row-checksum))

;; #### Solution
(def part-one (->> input (mapv row-checksum) (reduce +)))

;; ## Part two

(defn evenly-divisiable [row]
  (->> row
       (mapcat (fn [x] (mapv #(when-not (= % x) (/ x %)) row)))
       (some #(when (int? %) %))))

;; In the first row, the only two numbers that evenly divide are 8 and 2; the result of this division is 4.
;; In the second row, the two numbers are 9 and 3; the result is 3.
;; In the third row, the result is 2.
(map evenly-divisiable
     [[5 9 2 8]
      [9 4 7 3]
      [3 8 6 5]])

(def part-two (->> input (map evenly-divisiable) (reduce +)))
