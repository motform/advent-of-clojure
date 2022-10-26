(ns advent-of-clojure.2021.03
  (:require [clojure.string :as str]
            [clojure.data.priority-map :refer [priority-map]]))

(def input (->> "resources/2021/03.dat" slurp str/split-lines))

(defn rotate [xs]
  (apply map vector xs))

(defn ->priority-map [m]
  (apply priority-map (-> m seq flatten)))

(defn lo-hi-bits [bits]
  (->> bits
       rotate
       (map (comp keys ->priority-map frequencies))
       rotate))

(defn read-binary-num [s]
  (read-string (apply str "2r" s)))

(def part-one
  (->> input
       lo-hi-bits
       (map read-binary-num)
       (apply *)))

(defn filter-by-pos [i bit bits]
  (filter #(= bit (nth % i)) bits))

(defn nth-bit-freq [bits i]
  (->> bits
       (map #(nth % i))
       frequencies
       ->priority-map
       vec))

(defn flast [v] ((v 1) 0))

(defn find-by-bitmask [accessor bit bits]
  (reduce 
   (fn [bits i]
     (let [bit-freq (nth-bit-freq bits i)
           bit (if (apply = (map last bit-freq)) bit (accessor bit-freq))
           bits (filter-by-pos i bit bits)]
       (if (= 1 (count bits))
         (reduced (first bits))
         bits)))
   bits
   (range (count (first bits)))))

(def part-two
  (* (->> input (find-by-bitmask flast \1) read-binary-num)
     (->> input (find-by-bitmask ffirst \0) read-binary-num)))
