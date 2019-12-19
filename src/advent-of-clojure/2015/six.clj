(ns advent-of-clojure.2015.six
  (:require [clojure.string :as str]
            [clojure.set :as set]))

(def instructions
  (-> "resources/2015/six.dat" slurp str/trimr str/split-lines))

(defn digits [xs]
  (map read-string xs))

(defn lights [[x1 y1 x2 y2]]
  (into #{} (for [x (range x1 (inc x2))
                  y (range y1 (inc y2))]
              [x y])))

(defn turn-on [lights]
  (fn [house]
    (set/union house lights)))

(defn turn-off [lights]
  (fn [house]
    (set/difference house lights)))

(defn toggle [lights]
  (fn [house]
    (reduce toggle' house lights)))

(defn toggle' [house light]
  (if (house light) (disj house light) (conj house light)))

(defn parse-fn [instruction]
  (-> instruction
      (str/split #"\d")
      first
      str/trim
      ({"turn on" turn-on
        "turn off" turn-off
        "toggle" toggle})))

(defn parse-range [instruction]
  (let [chars #{\0 \1 \2 \3 \4 \5 \6 \7 \8 \9 \, \-}]
    (-> instruction
        (str/replace #"through" "-")
        (str/replace #"," "-")
        (as-> s (apply str (filter #(chars %) s)))
        (str/split #"-")
        digits
        lights)))

(defn parse-instruction [instruction]
  ((parse-fn instruction) (parse-range instruction)))

(def part-one
  (->> instructions
      (map parse-instruction)
      (reduce #((identity %2) %1) #{})
      count))
