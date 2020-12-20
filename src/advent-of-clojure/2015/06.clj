(ns advent-of-clojure.2015.06
  (:require [clojure.string :as str]
            [clojure.set :as set]))

(def instructions
  (-> "resources/2015/six.dat" slurp str/trimr str/split-lines))

(def instructions-one
  {"turn on" turn-on
   "turn off" turn-off
   "toggle" toggle})

(defn digits [xs]
  (map read-string xs))

(defn lights [[x1 y1 x2 y2]]
  (for [x (range x1 (inc x2))
        y (range y1 (inc y2))]
    [x y]))

(defn turn-on [lights]
  (fn [house]
    (set/union house lights)))

(defn turn-off [lights]
  (fn [house]
    (set/difference house lights)))

(defn toggle' [house light]
  (if (house light) (disj house light) (conj house light)))

(defn toggle [lights]
  (fn [house]
    (reduce toggle' house lights)))

(defn parse-fn [instruction instructions]
  (-> instruction
      (str/split #"\d")
      first
      str/trim
      instructions))

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
  ((parse-fn instruction instructions-one) (into #{} (parse-range instruction))))

#_(def part-one
   (->> instructions
       (map parse-instruction)
       (reduce #((identity %2) %1) #{})
       count))

(def lights-matrix
  (into [] (repeat 1000 (into [] (repeat 1000 0)))))

(def instructions-two
  {"turn on" inc
   "turn off" #(-> % dec (max 0))
   "toggle" #(+ 2 %)})

(defn parse-revised-instruction [instruction]
  [(parse-fn instruction instructions-two)
   (parse-range instruction)])

(defn update-point [f]
  (fn [mat [x y]]
    (update-in mat [y x] f)))

(defn update-points [mat [f points]]
  (let [f (update-point f)]
    (reduce #(f %1 %2) mat points)))

(def part-two
  (->> instructions
      (map parse-revised-instruction)
      (reduce #(update-points %1 %2) lights-matrix)
      flatten
      (reduce +)))
