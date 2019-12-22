(ns advent-of-clojure.2015.eleven
  (:require [clojure.string :as str]))

(def password "hxbxwxba")

(defn straight? [[x y z]]
  (and (= (inc x) y) (= (inc y) z)))

(defn one-straight? [s]
  (->> s (map int) (partition 3 1) (some straight?)))

(defn no-iol? [s]
  (not (re-find #"i|o|l" s)))

(defn two-pairs? [s]
  (= 2 (count (re-seq #"(.)\1" s))))

(def valid-password?
  (every-pred one-straight? no-iol? two-pairs?))

(valid-password? "hxbxwxya")

(defn inc-char [c]
  (if (= c \z) \a
      (-> c int inc char)))

(defn next-password' [password]
  "assumes you enter a reveresd password"
  (cond
    (= password "z") "aa"
    (= \z (first password)) (str \a (next-password' (subs password 1)))
    :else (str (inc-char (first password)) (subs password 1))))

(defn next-password [password]
  (str/reverse (next-password' (str/reverse password))))

(def part-one
  (->> password
      (iterate next-password)
      (drop-while (complement valid-password?))
      first))

(def part-two
  (->> password
      (iterate next-password)
      (filter valid-password?)
      second))
