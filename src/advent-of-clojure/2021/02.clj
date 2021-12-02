(ns advent-of-clojure.2021.01
  (:require [clojure.string :as str]))

(def input (->> "resources/2021/02.dat" slurp str/split-lines))

(defn parse-commands [direction-fn input]
  (map (comp (fn [[direction n]]
               [(direction-fn direction) (read-string n)])
             #(str/split % #" "))
       input))

(def direction->f
  {"forward" (fn [state n] (update state :x + n))
   "down"    (fn [state n] (update state :y + n))
   "up"      (fn [state n] (update state :y - n))}) 

(def part-one (->> input
                   (parse-commands direction->f)
                   (reduce (fn [state [f n]]
                             (f state n))
                           {:x 0 :y 0})
                   vals
                   (apply *)))

(def direction->f-2
  {"forward" (fn [state n] (let [aim (:aim state)]
                             (-> state (update :x + n)
                                 (update :y + (* aim n)))))
   "down"    (fn [state n] (update state :aim + n))
   "up"      (fn [state n] (update state :aim - n))}) 

(def part-two (let [{:keys [x y]}
                    (->> input
                         (parse-commands direction->f-2)
                         (reduce (fn [state [f n]]
                                   (f state n))
                                 {:x 0 :y 0 :aim 0}))]
                (* x y)))
