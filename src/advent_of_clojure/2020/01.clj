(ns advent-of-clojure.2020.01
  (:require [clojure.string :as str]))

(def expenses
  (->> "resources/2020/one.dat" slurp str/split-lines (map read-string)))

(def one
  (first
   (for [x expenses y expenses
         :when (= 2020 (+ x y))]
     (* x y))))

(def two
  (first
   (for [x expenses, y expenses, z expenses
         :when (= 2020 (+ x y z))]
     (* x y z))))

(reduce
 (fn [seen expense]
   (let [diff (- 2020 expense)]
     (if-let [s (seen diff)]
       (reduced (* expense s))
       (conj seen expense))))
 #{}
 expenses)

;; ...

(reduce
 (fn [seen expense]
   (if-let [s (seen (- 2020 expense))]
     (reduced (* expense s))
     (conj seen expense)))
 #{}
 expenses)
