(ns advent-of-clojure.2020.16
  (:require [clojure.string :as str]
            [clojure.set :as set]
            [clojure.math.combinatorics :as combo])) ; never a good sign

(def input (-> "resources/2020/16.dat" slurp (str/split #"\n\n")))

(defn parse-rule [rule]
  (let [[tag lo1 hi1 lo2 hi2] (str/split rule #": | or |-")]
    {tag (concat (range (Integer. lo1) (inc (Integer. hi1))) (range (Integer. lo2) (inc (Integer. hi2))))}))

(defn parse-rules [rules]
  (apply merge (mapv parse-rule (str/split-lines rules))))

(defn parse-ticket [ticket]
  (mapv read-string (str/split ticket #",")))

(defn parse-tickets [tickets]
  (map parse-ticket (rest (str/split-lines tickets))))

(defn valid-values [rules]
  (->> rules vals flatten (into #{})))

(defn invalid-value? [values value]
  (when-not (values value) value))

(defn error-rate [[rules _ tickets]]
  (let [values (->> rules parse-rules valid-values)]
    (->> tickets
         parse-tickets
         flatten
         (filter (partial invalid-value? values))
         (apply +))))

(def one (error-rate input))

(defn invalid-ticket? [values ticket]
  (some (partial invalid-value? values) ticket))

(defn parse-rule2 [rule]
  (let [[tag lo1 hi1 lo2 hi2] (str/split rule #": | or |-")]
    {tag (into #{} (concat (range (Integer. lo1) (inc (Integer. hi1))) (range (Integer. lo2) (inc (Integer. hi2)))))}))

(defn parse-rules2 [rules]
  (apply merge (mapv parse-rule2 (str/split-lines rules))))

(defn transpose [matrix]
  (apply map vector matrix))

(defn collate-fields [values tickets]
  (->> tickets parse-tickets (remove (partial invalid-ticket? values)) transpose (map set)))

(def valid-ruleset?
  (memoize ; "optimization"
   (fn [fields rules]
     (map (fn [field [tag values]] (when (set/subset? field values) tag))
          fields rules))))

(defn valid-ruleset [fields rules]
  (pmap (partial valid-ruleset? fields) rules))

(defn translate-ticket [[rules ticket tickets]]
  (let [values (->> rules parse-rules valid-values)
        rule-permutations (->> rules parse-rules2 combo/permutations) ; BRUTE FORCE ALERTA TRIGGER TRIGGER
        fields (collate-fields values tickets)]
    (zipmap (->> rule-permutations (valid-ruleset fields) (filter #(not-any? nil? %)) first)
            (->> ticket parse-tickets first)))) ; don't run this, it runs forever

#_(def two (->> input translate-ticket (filter #(when (str/includes? % "departure") %))              
                vals (apply *))) ; no but seriously, don't even try to eval this, you will run until the end of days








;; told you
