(ns advent-of-clojure.2021.04
  (:require [clojure.string :as str]
            [clojure.set :as set]))

(def input (->> "resources/2021/04.dat" slurp str/split-lines))

(defn transpose [matrix]
  (apply map vector matrix))

(defn ->rows-columns [board]
  (concat (map set board) (map set (transpose board))))

(defn parse-input [input]
  {:numbers (read-string (str "[" (first input) "]"))
   :boards  (->> input rest
                 (remove str/blank?)
                 (map (comp #(map read-string %)
                            #(-> % str/trim (str/split #"\s+"))))
                 (partition 5)
                 (map ->rows-columns))})

(defn check-board [numbers board]
  (when (some (partial set/superset? numbers) board)
    board))

(defn calculate-score [board numbers current-number]
  (* current-number (reduce + (set/difference (->> board (take 5) (apply set/union))
                                              numbers))))

(defn find-bingo [{:keys [numbers boards]}]
  (reduce
   (fn [numbers current-number]
     (let [numbers (conj numbers current-number)]
       (if-let [bingo? (some (partial check-board numbers) boards)]
         (reduced (calculate-score bingo? numbers current-number))
         numbers)))
   #{}
   numbers))

(def part-one (-> input parse-input find-bingo))

(defn find-last-bingo [{:keys [boards] starting-numbers :numbers}]
  (reduce
   (fn [{:keys [numbers boards]} current-number]
     (let [numbers (conj numbers current-number)
           boards (remove (partial check-board numbers) boards)]
       (if (= 1 (count boards))
         (reduced (find-bingo {:boards boards :numbers starting-numbers}))
         {:numbers numbers :boards boards})))
   {:numbers #{} :boards boards}
   starting-numbers))

(def part-two (-> input parse-input find-last-bingo))
