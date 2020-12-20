(ns advent-of-clojure.2020.10
  (:require [clojure.string :as str]))

(def joltages (->> "resources/2020/10.dat" slurp str/split-lines (map read-string)))

(defn add-extremes [joltages]
  (conj joltages 0 (+ 3 (apply max joltages))))

(defn joltage-differences [joltages]
  (frequencies (map #(- %2 %1) joltages (rest joltages))))

(def one (->> joltages add-extremes sort joltage-differences vals (apply *)))

(defn find-next [adapter adapters]
  (seq (filter adapters (map + [1 2 3] (repeat 3 adapter)))))

(def arrangements
  (memoize
   (fn [start]
     (if-let [found (find-next start joltages)]
       (apply + (dec (count found)) (map arrangements found))
       0))))

(def count-arranges
  (memoize
   (fn [start]
     (if-let [found (find-next start adapters)]
       (apply + (dec (count found)) (map count-arranges found))
       0))))
