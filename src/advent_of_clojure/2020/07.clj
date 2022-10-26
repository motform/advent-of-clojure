(ns advent-of-clojure.2020.07
  (:require [clojure.string :as str]))

(declare contains-bag)

(def input (->> "resources/2020/seven.dat" slurp str/split-lines (map #(str/split % #" contain no other bags.| contain |, "))))

(defn parse-rule [[container & contained]]
  (conj (map (partial re-seq #"\d|\w+\s\w+") contained)
        (re-find #"\w+\s\w+" container)))

(def rules
  (reduce (fn [m [c & cs]]
            (assoc m c (reduce (fn [m [n c'd]] (assoc m c'd (Integer. n))) {} cs)))
          {} (map parse-rule input)))

(defn contains-bag? [bag container]
  (or (get-in rules [container bag])
      (pos? (contains-bag bag (keys (get rules container))))))

(defn contains-bag [bag containers]
  (reduce
   (fn [n container]
     (if (contains-bag? bag container) (inc n) n))
   0 containers))

(def one (contains-bag "shiny gold" (keys rules)))

(defn count-bags [container]
  (reduce-kv
   (fn [acc bag n]
     (+ acc n (* n (count-bags (get rules bag)))))
   0
   container))

(def two (count-bags (get rules "shiny gold")))
