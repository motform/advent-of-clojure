(ns advent-of-clojure.2022.06
  (:require [clojure.string :as str]))

(def input (-> "resources/2022/06.dat" slurp))

(defn first-marker [n buffer]
  (->> buffer
       (partition n 1)
       (map (partial apply str))
       (filter #(= n (-> % distinct count)))
       first))

(defn preamble-lenght [n buffer]
  (->> buffer (first-marker n) (str/index-of buffer) (+ n)))

(def part-one (preamble-lenght 4 input))

(def part-two (preamble-lenght 14 input))
