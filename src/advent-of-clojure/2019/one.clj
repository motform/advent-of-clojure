(ns advent-of-clojure.2019.one
  (:require [clojure.string :as s]
            [clojure.math.numeric-tower :as m]))

(defn fuel-required [module]
  (- (m/floor (/ module 3))
     2))

(defn holistic-fuel [module]
  (->> module
      (iterate fuel-required)
      (take-while pos?)
      rest
      (reduce +)))

(def modules (map read-string
                  (-> "resources/one.dat" slurp (s/split #"\n"))))

(def fuel (->> modules
              (map holistic-fuel)
              (reduce +)))
