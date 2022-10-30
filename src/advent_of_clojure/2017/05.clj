(ns advent-of-clojure.2017.05
  (:require [clojure.string :as str]))

(def input (->> "resources/2017/05.dat" slurp str/split-lines (mapv parse-long)))

(defn run [instructions update-fn]
  (let [exit (count instructions)]
    (loop [instructions instructions i 0 steps 0]
      (if (= i exit) steps
          (recur (update instructions i update-fn)
                 (+ i (nth instructions i))
                 (inc steps))))))

(def part-one (run input inc))

(defn update-instruction [instruction]
  ((if (= 3 instruction) dec inc) instruction))

(def part-two (run input update-instruction))



