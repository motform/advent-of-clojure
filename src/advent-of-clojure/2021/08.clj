(ns advent-of-clojure.2021.08
  (:require [clojure.string :as str]
            [clojure.set :as set]))

(def input (-> "resources/2021/08.dat" slurp))

(defn parse-input [input]
  (mapv (comp (partial mapv #(str/split % #" "))
              #(str/split % #"\| "))
        (str/split-lines input)))

(defn counts [input]
  (->> input (mapcat (comp (partial map count) second)) frequencies))

(def part-one (apply + (-> input parse-input counts (select-keys [2 3 4 7]) vals)))

(defn map-by-string-lenght [strs]
  (reduce (fn [m s]
            (let [len (count s)]
              (assoc m len (conj (m len []) (into #{} s)))))
          {} strs))

(defn str-diff [s1 s2] (first (set/difference s1 s2)))

(defn nth-m [m k] (first (m k)))

(defn letter-frequencies [m]
  (assoc m :lfreqs
         (reduce-kv
          (fn [m k v]
            (if-let [k' (m v)] 
              (assoc m v #{k k'})
              (assoc m v k)))
          {} (-> (apply str m) frequencies))))

(defn unique-numbers [{:keys [lfreqs] :as m}]
  (merge m {\b (lfreqs 6)
            \e (lfreqs 4)
            \f (lfreqs 9)}))

(defn a [m]
  (assoc m \a (str-diff (nth-m m 3) (nth-m m 2))))

(defn c [{:keys [lfreqs] :as m}]
  (assoc m \c (str-diff (lfreqs 8) #{(m \a)})))

(defn d [{:keys [lfreqs] :as m}]
  (assoc m \d (str-diff (nth-m m 4) (-> m (select-keys [\c \f \b]) vals set))))

(defn g [{:keys [lfreqs] :as m}]
  (assoc m \g (str-diff (lfreqs 7) #{(m \d)})))

(defn decode [m]
  (-> m unique-numbers a c d g (select-keys [\a \b \c \d \e \f \g])))

(def seven-digit-display-mappings
  (set/map-invert
   {0  #{\a \b \c \e \f \g}
    1  #{\c \f}
    2  #{\a \c \d \e \g}
    3  #{\a \c \d \f \g}
    4  #{\b \c \d \f}
    5  #{\a \b \d \f \g}
    6  #{\a \b \d \e \f \g}
    7  #{\a \c \f}
    8  #{\a \b \c \d \e \f \g}
    9  #{\a \b \c \d \f \g}}))

(defn decode-output [key output]
  (seven-digit-display-mappings (set (map #(key %) output))))

(defn decode-row [[pattern output]]
  (map (partial decode-output (-> pattern map-by-string-lenght letter-frequencies decode set/map-invert)) output))

(defn decode-table [table]
  (map (comp parse-long (partial apply str) decode-row) table))

(def part-two (->> input parse-input decode-table (apply +)))
