(ns advent-of-clojure.2020.13
  (:require [clojure.string :as str]))

(def note (-> "resources/2020/13.dat" slurp str/split-lines))

(defn parse-note [note]
  [(-> note first read-string)
   (map read-string (-> note second (str/split #"[,x,]+|,")))])

(defn first-departure [timestamp line]
  (first (drop-while #(> timestamp %) (iterate #(+ line %) line))))

(defn earliest-depratures [timestamp lines]
  (map (fn [line] #:bus{:id line :departure (first-departure timestamp line)}) lines))

(def one
  (let [[timestamp lines] (parse-note note)
        {:bus/keys [id departure]} (apply min-key :bus/departure (earliest-depratures timestamp lines))]
    (* id (- departure timestamp))))

;;; There is obviously a nicer mathematical solution, but alas, I'm but a civilized brute.
(defn bus? [line] (not= "x" line))

(defn departure-distances [departures]
  (let [lines (str/split departures #",")]
    (apply array-map ; array-map maintains insertion order
           (interleave 
            (->> lines (filter (partial bus?)) (map read-string))
            (->> lines (map-indexed (fn [i x] (when (bus? x) i))) (remove nil?))))))

(defn timetable [timestamp distances]
  (map #(+ timestamp %) distances))

(defn valid-deprature? [line departure]
  (when (zero? (rem departure line)) departure))

(defn valid-timestamp? [timestamp distances]
  (->> (timetable timestamp (vals distances))
       (map (fn [line departure] (valid-deprature? line departure)) (keys distances))
       (every? number?)))

(defn earliest-timestamp [departures from]
  (let [distances (departure-distances departures)]
    (loop [timestamp from]
      (if (valid-timestamp? timestamp distances)
        timestamp
        (recur (+ 1 #_step timestamp))))))

#_(def two (earliest-timestamp (second note) 100000000000000))
