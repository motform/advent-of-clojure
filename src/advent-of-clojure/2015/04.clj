(ns advent-of-clojure.2015.04
  (:require [digest :refer [md5]]))

(def secret-key "ckczppom")

(def key-permutations
  (map (partial str secret-key) (range)))

(defn five-leading-zeroes? [hash]
  (= (subs hash 0 6) "000000"))

(def part-one
  (->> key-permutations
      (map md5)
      (take-while (complement five-leading-zeroes?))
      count))
