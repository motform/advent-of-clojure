(ns advent-of-clojure.2016.01
  (:require [clojure.string :as str]))

(def instructions
  (as-> "resources/2016/one.dat" <> (slurp <>) (str/split <> #", ") (map #(str/split % #"\w") <>)))

(def right-turn
  {"N" "E"
   "E" "S"
   "S" "W"
   "W" "N"})

(def left-turn
  {"N" "W"
   "W" "S"
   "S" "E"
   "E" "N"})
