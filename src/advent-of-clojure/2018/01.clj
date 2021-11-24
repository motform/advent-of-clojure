(ns advent-of-clojure.2018.01
  (:require [clojure.string :as str]))

(def input (->> "resources/2018/01.dat" slurp str/split-lines (map read-string)))

(def one (reduce + input))

(defn repeating-freq [input]
  (let [freqs (reductions + (cycle input))]
    (reduce (fn [seen x]
              (if (seen x)
                (reduced x)
                (conj seen x)))
            #{} freqs)))

(def two (repeating-freq input))
