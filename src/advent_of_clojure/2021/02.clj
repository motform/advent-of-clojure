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

(defn execute-commands [commands]
  (reduce (fn [state [f n]]
            (f state n))
          {:x 0 :y 0 :aim 0}
          commands))

(defn mul-pos [{:keys [x y]}] (* x y))

(def part-one (->> input (parse-commands direction->f) execute-commands mul-pos))

(def direction->f-2
  {"forward" (fn [state n] (let [aim (:aim state)]
                             (-> state (update :x + n)
                                 (update :y + (* aim n)))))
   "down"    (fn [state n] (update state :aim + n))
   "up"      (fn [state n] (update state :aim - n))}) 

(def part-two (->> input (parse-commands direction->f-2) execute-commands mul-pos))
