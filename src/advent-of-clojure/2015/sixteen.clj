(ns advent-of-clojure.2015.sixteen
  (:require [clojure.string :as str]
            [clojure.data :refer [diff]]))

(defn parse-sue [sue]
  (->> sue (re-seq #"\d+|\w+") rest (map #(%1 %2) (cycle [read-string identity]))))

(defn parse-odd [xs]
  (map #(%1 %2) (cycle [identity read-string]) xs))

(def sues
  (->> "resources/2015/sixteen.dat" slurp str/split-lines (map (comp (partial apply hash-map) #(conj % "nth") parse-sue))))

(def the-one-true-sue
  (-> "resources/2015/sixteen-list.dat" slurp (str/split #": |\n") parse-odd (as-> sue (apply hash-map sue))))

(def part-one
  (->> sues
      (map #(diff the-one-true-sue %))
      (sort-by #(count (nth % 2)))
      last
      (#(get (second %) "nth"))))
