(ns advent-of-clojure.2022.02
  (:require [clojure.string :as str]))

(def input (partition 2 (-> "resources/2022/02.dat" slurp (str/split #"\s+"))))

;; You can probably do some smart shifting here
(def draw     {"A" "X", "B" "Y", "C" "Z"})
(def beats    {"A" "Y", "B" "Z", "C" "X"})
(def loses-to {"A" "Z", "B" "X", "C" "Y"})

(defn outcome [opponent you]
  (cond (= (draw opponent) you) 3
        (= (beats opponent) you) 6
        :else 0))

(def value {"X" 1, "Y" 2, "Z" 3})

(defn score [[opponent you]]
  (+ (value you) (outcome opponent you)))

(def part-one (->> input (map score) (reduce +)))

(defn move [opponent outcome]
  (case outcome
    "X" (loses-to opponent)
    "Y" (draw opponent)
    "Z" (beats opponent)))

(def points {"X" 0 "Y" 3 "Z" 6})

(defn final-score [[opponent outcome]]
  (+ (value (move opponent outcome))
     (points outcome)))

(def part-two (->> input (map final-score) (reduce +)))
