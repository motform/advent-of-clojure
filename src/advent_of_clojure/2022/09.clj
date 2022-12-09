(ns advent-of-clojure.2022.09
  (:require [clojure.string :as str]
            [clojure.math :as math]))

(defn parse [s]
  (let [[direction distance] (str/split s #" ")]
    [(keyword direction) (parse-long distance)]))

(def input (->> "resources/2022/09.dat" slurp str/split-lines (mapv parse)))

(def move-v {:U [1 0] :D [-1 0] :L [0 -1] :R [0 1]})
(def +v (partial mapv +))
(def -v (partial mapv -))

(defn touching? [head tail]
  (>= 1 (apply max (map abs (-v tail head)))))

(defn follow [head tail]
  (if (touching? head tail) tail
      (+v tail (mapv #(int (math/signum %)) (-v head tail)))))

(defn pull [rope direction]
  (reduce (fn [new-rope knot]
            (conj new-rope (follow (last new-rope) knot)))
          [(-> rope first (+v (move-v direction)))]
          (rest rope)))

(defn move [state [direction distance]]
  (reduce (fn [{:keys [seen rope]} _]
            (let [new-rope (pull rope direction)]
              {:seen (conj seen (last new-rope)) :rope new-rope}))
          state
          (range distance)))

(defn simulate [length instructions]
  (reduce move
          {:seen #{[0 0]}
           :rope (-> length (repeat [0 0]) vec)}
          instructions))

(def part-one (->> input (simulate 2) :seen count))
(def part-two (->> input (simulate 10) :seen count))
