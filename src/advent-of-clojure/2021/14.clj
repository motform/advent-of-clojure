(ns advent-of-clojure.2021.14
 (:require [clojure.string :as str]))

(def input (-> "resources/2021/14.dat" slurp))

(defn parse-input [input]
 (let [[seed template] (str/split input #"\n\n")]
  {:seed seed :template 
    (reduce (fn [m s] 
             (let [[a b c] (re-seq  #"\w" s)]
              (assoc m (str a b) [(str a c) (str c b)]))) 
    {} (str/split-lines template))}))

(defn pair-frequencies [seed] 
 (->> seed (partition 2 1) (map (partial apply str)) frequencies))

(defn grow [template freqs]
 (reduce-kv (fn [m k v]
             (reduce 
              (fn [m pair] 
               (update m pair (fnil + 0) v))
              m (template k)))
 {} freqs))

(defn polymer [n template seed]
  (->> seed 
       pair-frequencies 
       (iterate (partial grow template))
       (take (inc n)) 
       last))

(defn solution [input n]
 (let [{:keys [template seed]} (parse-input input)]
  (->> seed 
       (polymer n template) 
       (reduce (fn [m [[a _] n]] 
                (update m a (fnil + 0) n)) 
       {(last seed) 1})
       vals
       (apply (juxt max min))
       (apply -))))

(def part-one (solution input 10))

(def part-two (solution input 40))