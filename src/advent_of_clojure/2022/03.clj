(ns advent-of-clojure.2022.03
  (:require [clojure.set :as set]
            [clojure.string :as str]))

(def input (->> "resources/2022/03.dat" slurp str/split-lines))

(defn halve [s]
  (let [half (/ (count s) 2)]
    [(subs s 0 half) (subs s half)]))

(defn score [char]
  (- (int char)
     (if (Character/isLowerCase char) 96 38)))

(def shared-item
  (comp (partial apply set/intersection)
        (partial map set)))

(def part-one (->> input
                   (map (comp score first shared-item halve))
                   (reduce +)))

(def part-two (->> input
                   (partition 3)
                   (map (comp score first shared-item))
                   (reduce +)))
