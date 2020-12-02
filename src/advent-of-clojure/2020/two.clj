(ns advent-of-clojure.2020.two
  (:require [clojure.string :as str]))

(defn parse-password [[x y c _ password]]
  [(Integer. x) (Integer. y) (.charAt c 0) password])

(def passwords
  (->> "resources/2020/two.dat" slurp str/split-lines (map (comp parse-password #(str/split % #"\W")))))

(defn in-range? [x min max]
  (when x (<= min x max)))

(defn valid-password? [[min max c password]]
  (-> password frequencies (get c) (in-range? min max)))

(def one (->> passwords (filter valid-password?) count))

(defn valid-password?-2 [[x y c password]]
  (not= (= c (nth password (dec x)))
        (= c (nth password (dec y)))))

(def two (->> passwords (filter valid-password?-2) count))
