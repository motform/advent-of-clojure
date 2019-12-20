(ns advent-of-clojure.2015.eight
  "tfw no raw string type"
  (:require [clojure.string :as str]))

(def strings
  (-> "resources/2015/eight.dat" slurp str/trimr str/split-lines))

(def test-data ["\"\""
                "\"abc\""
                "\"aaa\\\"aaa\""
                "\"\\x27\""])

(defn delimiting-quotes [s]
  (subs s 1 (dec (count s))))

(defn double-backslash [s]
  (str/replace s #"\\\\" "%"))

(defn double-backslash-representation [s]
  (str/replace s #"\\\\" "%%"))

(defn quotes [s]
  (str/replace s #"\\\"" "\""))

(defn quotes-representation [s]
  (str/replace s #"\\\"" "@@"))

(defn quote-encoded [s]
  (str/replace s #"\"" "@@"))

(defn quotes-encoded [s]
  (str/replace s #"\\" "@@"))

(defn hex [s]
  (str/replace s #"\\x\w\w" "&"))

(defn hex-representation [s]
  (str/replace s #"\\x\w\w" "&&&&"))

(defn hex-encoded [s]
  (str/replace s #"\\x\w\w" "&&&&&"))

(def chars-in-memory
  (partial (comp count
                 hex
                 quotes
                 double-backslash
                 delimiting-quotes)))

(def chars-in-representation
  (partial (comp #(+ 2 %)
                 count
                 char-array
                 hex-representation
                 quotes-representation
                 double-backslash-representation
                 delimiting-quotes)))

(def chars-encoded
  (partial (comp #(+ 2 %)
                 count
                 char-array
                 hex-encoded
                 quotes-encoded
                 quote-encoded)))

(def part-one
  (- (reduce + (map chars-in-representation strings))
     (reduce + (map chars-in-memory strings))))

(def part-two
  (- (reduce + (map chars-encoded strings))
     (reduce + (map chars-in-representation strings))))
