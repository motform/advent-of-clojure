(ns advent-of-clojure.2020.11
  (:require [clojure.string :as str]))

(def seats (-> "resources/2020/11.dat" slurp str/split-lines))

(def adjecent-seats
  (memoize
   (fn [i j]
     (concat
      (map vector (repeat (dec i)) (range (dec j) (+ 2 j)))
      (map vector (repeat i)       (range (dec j) (+ 2 j) 2))
      (map vector (repeat (inc i)) (range (dec j) (+ 2 j)))))))

(def occupied-seats
  (memoize
   (fn [i j seats]
     (->> (adjecent-seats i j)
          (map (fn [[i j]] (if (= \# (nth (nth seats i nil) j nil)) 1 0)))
          (apply +)))))

(defn update-seat [seats i j seat]
  (let [occupied (occupied-seats i j seats)]
    (cond (and (= seat \L) (= 0 occupied))  \#
          (and (= seat \#) (<= 4 occupied)) \L
          :else seat)))

(defn update-row [seats i row]
  (->> row (map-indexed (partial update-seat seats i)) (into [])))

(defn update-seats [seats]
  (->> seats (map-indexed (partial update-row seats)) (into [])))

(defn run-simulation [seats]
  (let [next (update-seats seats)]
    (if (= next seats)
      seats

      (recur next))))

#_(def one (get (->> (run-simulation seats) flatten frequencies) \#))

(def seat-directions
  (memoize
   (fn [i j seats row]
     (let [width (count row)
           height (count seats)]
       [(mapv vector (range (dec i) -1 -1)   (range (dec j) -1 -1))      ; NW 
        (mapv vector (range (dec i) -1 -1)   (repeat j))                 ; N
        (mapv vector (range (dec i) -1 -1)   (range (inc j) width))      ; NE
        (mapv vector (repeat i)              (range (inc j) width))      ; E
        (mapv vector (range (inc i) height)  (range (inc j) width))      ; SE 
        (mapv vector (range (inc i) height)  (repeat j))                 ; S
        (mapv vector (range (inc i) height)  (range (dec j) -1 -1))      ; SW
        (mapv vector (repeat i)              (range (dec j) -1 -1))])))) ; W

(defn first-seat [seats direction]
  (->> direction
       (map (fn [[i j]] (nth (nth seats i) j)))
       (some #{\# \L})))

(def occupied-directions
  (memoize
   (fn [i j seats row]
     (get (->> (seat-directions i j seats row)
               (map (partial first-seat seats))
               frequencies) \# 0))))

(defn update-directional-seat [seats row i j seat]
  (let [occupied (occupied-directions i j seats row)]
    (cond (and (= seat \L) (= 0 occupied))  \#
          (and (= seat \#) (<= 5 occupied)) \L
          :else seat)))

(defn update-row2 [seats i row]
  (->> row (map-indexed (partial update-directional-seat seats row i)) (into [])))

(defn update-seats2 [seats]
  (->> seats (map-indexed (partial update-row2 seats)) (into [])))

(defn run-simulation2 [seats]
  (loop [seats seats]
    (let [next (update-seats2 seats)]
      (if (= next seats)
        seats
        (recur next)))))

#_(def two (get (->> (run-simulation2 seats) flatten frequencies) \#))
