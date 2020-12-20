(ns advent-of-clojure.2015.05
  (:require [clojure.string :as str]))

(def strings
  (-> "resources/2015/five.dat" slurp str/trimr str/split-lines))

(defn three-vowels? [s]
  (->> s
      (map (partial #{\a \e \i \o \u}))
      (remove nil?)
      count
      (<= 3)))

(defn repetition? [s]
  (->> s
      (partition-by identity)
      (map count)
      (some (partial <= 2))))

(defn no-forbidden-pairs? [s]
  (->> [#"ab" #"cd" #"pq" #"xy"]
      (map #(re-find % s))
      (remove nil?)
      empty?))

(def nice?
  (every-pred three-vowels? repetition? no-forbidden-pairs?))

(defn pair-twice? [s]
  (->> (re-seq #"(\w)\1+" s)
      (map first)
      frequencies
      vals
      (not-every? (partial not= 2)))) ; every? returns t on nil…

(defn pairs [xs]
  (interleave (partition 2 xs) (partition-all 2 (rest xs))))

(defn pair-twice? [s]
  (->> s
      pairs
      frequencies
      vals
      (not-every? (partial not= 2)))) ; every? returns t on nil…

(defn sandwiched-char? [s]
  )

(def actually-nice?
  (every-pred pair-twice sandwiched-char))

(def part-one
  (->> strings
      (filter nice?)
      count))

(def part-two
  (->> strings
      (filter actually-nice?)
      count))
