(ns advent-of-clojure.2015.14
  (:require [clojure.string :as str]))

(defn raindeer-name [raindeer]
  {:name (first (str/split raindeer #" "))})

(defn raindeer-stats [raindeer]
  (->> raindeer
      (re-seq #"\d+")
      (map read-string)
      (zipmap [:kms :duration :rest])))

(defn parse-raindeer [raindeer]
  (apply merge ((juxt raindeer-name raindeer-stats) raindeer)))

(defn fly [{:keys [kms duration rest]} seconds]
  (let [cycles (quot seconds (+ duration rest))
        last-mile (- seconds (* cycles (+ duration rest)))]
    (+ (* kms duration cycles)
       (* (min last-mile duration) kms))))

(def raindeers
  (->> "resources/2015/fourteen.dat" slurp str/split-lines (map parse-raindeer)))

(def seconds 2503)

(def part-one
  (->> raindeers
      (map #(fly % seconds))
      (apply max)))

(defn leader [seconds]
  (apply max-key #(fly % seconds) raindeers))

(def part-two
  (apply max (vals (reduce (fn [score seconds]
                             (let [leader (leader seconds)]
                               (update-in score [(:name leader)] #(inc (or % 0)))))
                           {} (range 1 (inc seconds))))))
