(ns advent-of-clojure.2020.one
  (:require [clojure.string :as str]
            [clojure.math.combinatorics :refer [combinations]]
            [clojure.set :as set]))

(def expenses
  (->> "resources/2020/one.dat" slurp str/split-lines (map read-string) (into #{})))

(def one
  (->> expenses
       (map (comp #(Math/abs %) #(- % 2020)))
       (into #{})
       (set/intersection expenses)
       (apply *)))

(def two
  (->> (combinations expenses 3)
       (filter #(= 2020 (apply + %)))
       flatten
       (apply *)))
