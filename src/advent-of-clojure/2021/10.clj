(ns advent-of-clojure.2021.10
  (:require [clojure.string :as str]
            [clojure.set :as set]))

(def input (-> "resources/2021/10.dat" slurp str/split-lines))

(def closing {\( \) \[ \] \{ \} \< \>})
(def closing? (set/map-invert closing))
(def corrupt-score {\) 3 \] 57 \} 1197 \> 25137})

(defn correct [line]
  (let [ret (reduce (fn [stack char]
                      (if (closing? char)
                        (if (= char (closing (peek stack)))
                          (pop stack)
                          (reduced char))
                        (conj stack char)))
                    '() line)]
    (if (list? ret)
      (->> ret (map closing) (apply str))
      ret)))

(def part-one (->> input (map correct) (filter char?) (map corrupt-score) (reduce +)))

(defn autocomplete-score [line]
  (let [chart {\) 1 \] 2 \} 3 \> 4}]
    (reduce (fn [score char] (+ (chart char) (* 5 score))) 0 line)))

(defn middle [xs] (first (drop (dec (/ (count xs) 2)) xs)))

(def part-two (->> input (map correct) (filter string?) (map autocomplete-score) sort middle))
