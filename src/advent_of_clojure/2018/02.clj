(ns advent-of-clojure.2018.02
  (:require [clojure.string :as str]))

(def boxes (->> "resources/2018/02.dat" slurp str/split-lines))

;; Hugely inefficient stream of consciousness
(def one
  (->> boxes
       (map (comp set vals frequencies))
       (mapcat #(filter (fn [x] (< 1 x 4)) %))
       frequencies
       vals
       (reduce *)))

(def two
  )

(def test-data ["abcde" "fghij" "klmno" "pqrst" "fguij" "axcye" "wvxyz"])
