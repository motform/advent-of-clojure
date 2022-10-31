(ns advent-of-clojure.2017.06
  (:require [clojure.string :as str]))

(def input (mapv parse-long (-> "resources/2017/06.dat" slurp (str/split #"\s"))))

(defn first-occurance [pred xs]
  (first (keep-indexed #(when (pred %2) %1) xs)))

(defn next-index [i n]
  (if (not= (inc i) n) (inc i) 0))

(defn redistribute
  ([banks]
   (let [highest-value (apply max banks)
         i (first-occurance #{highest-value} banks)]
     (redistribute (assoc banks i 0) highest-value (next-index i (count banks)))))
  ([banks n i]
   (if (= 0 n) banks
       (recur (update banks i inc) (dec n) (next-index i (count banks))))))

(defn defrag
  ([banks] (defrag banks {}))
  ([banks seen]
   (let [banks (redistribute banks)]
     (if-let [difference (seen banks)]
       [(-> seen count inc) (- (count seen) difference)]
       (recur banks (assoc seen banks (count seen)))))))

(def part-one (-> input defrag first))

(def part-two (-> input defrag second))
