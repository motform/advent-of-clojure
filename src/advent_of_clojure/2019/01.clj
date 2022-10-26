(ns advent-of-clojure.2019.01
  (:require [clojure.string :as s]
            [clojure.math.numeric-tower :as m]))

(defn fuel-required [module]
  (- (m/floor (/ module 3))
     2))

(def modules
  (map read-string
       (-> "resources/2019/one.dat"
           slurp
           (s/split #"\n"))))

(defn -main []
  (println
   (->> modules
        (map fuel-required)
        (reduce +))))

