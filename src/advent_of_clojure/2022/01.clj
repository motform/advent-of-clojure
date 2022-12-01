(ns advent-of-clojure.2022.01
  (:require [clojure.string :as str]))

(def input (->> (-> "resources/2022/01.dat" slurp (str/split #"\n\n"))
                (map str/split-lines)
                (map (comp (partial reduce +) (partial map parse-long)))))

(def part-one (apply max input))

(def part-two (->> input (sort >) (take 3) (reduce +)))
