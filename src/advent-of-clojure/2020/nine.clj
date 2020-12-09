(ns advent-of-clojure.2020.nine
  (:require [clojure.string :as str]
            [clojure.math.combinatorics :as combo]))

(def XMAS (->> "resources/2020/nine.dat" slurp str/split-lines (mapv read-string)))

(defn sums [xs]
  (->> (combo/combinations xs 2) (map (partial apply +)) (into #{})))

(defn invalid-number? [x sums]
  (when-not (sums x) x))

(defn first-invalid-number [xs]
  (let [preamble (take 25 xs)]
    (if-let [invalid (->> preamble sums (invalid-number? (nth xs 25)))]
      invalid
      (recur (rest xs)))))

(def one (first-invalid-number XMAS))

(defn contigous-set? [invalid lo hi xs]
  (let [range (subvec xs lo hi)
        sum (apply + range)]
    (cond (= sum invalid) range
          (> invalid sum) (recur invalid lo (inc hi) xs))))

(defn encryption-weakness [invalid xs]
  (loop [xs xs]
    (if-let [range (contigous-set? invalid 0 2 xs)]
      (+ (apply min range) (apply max range))
      (recur (->> xs rest vec)))))

(def two (encryption-weakness (first-invalid-number XMAS) XMAS))
