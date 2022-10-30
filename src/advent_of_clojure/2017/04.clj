(ns advent-of-clojure.2017.04
  (:require [clojure.string :as str]))

(def input (-> "resources/2017/04.dat" slurp str/split-lines))

(defn valid-passphrase? [passphrase]
  (let [words (str/split passphrase #" ")]
    (= (count words)
       (count (distinct words)))))

(def part-one (->> input (filter valid-passphrase?) count))

(defn sort-chars [s]
  (apply str (sort s)))

(defn contains-anagram? [passphrase]
  (let [words (str/split passphrase #" ")]
    (not= (count words)
          (->> words (map sort-chars) distinct count))))

(def part-two (->> input (remove contains-anagram?) count))
