(ns advent-of-clojure.2015.02
  "Someone just learned about juxt!"
  (:require [clojure.string :as str]))

(def data
  (->> "resources/2015/two.dat"
      slurp
      str/trimr
      str/split-lines
      (map (comp #(zipmap [:l :w :h] %)
                 #(map read-string %)
                 #(str/split % #"x")))))

(def sides
  (juxt #(* (:l %) (:w %))
        #(* (:w %) (:h %))
        #(* (:h %) (:l %))))

(defn slack [box]
  (quot (apply min box) 2))

(defn wrapping-paper [box]
  (reduce + (slack box) box))

(defn ribbon [{:keys [l w h]}]
  (* l w h))

(defn wrapping [{:keys [l w h]}]
  (->> [l w h] sort drop-last (reduce +) (* 2)))

(defn feet-of-ribbon [box]
  (apply + ((juxt ribbon wrapping) box)))

(def part-one
  (->> data
      (map (comp (partial wrapping-paper) (partial map #(* 2 %)) sides))
      (reduce +)))

(def part-two
  (->> data
      (map (partial feet-of-ribbon))
      (reduce +)))
