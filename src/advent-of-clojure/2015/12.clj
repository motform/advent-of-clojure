(ns advent-of-clojure.2015.12
  (:require [clojure.data.json :as json]))

(def books
  (-> "resources/2015/twelve.json" slurp json/read-str))

(defn red? [node]
  (or (vector? node)
      (and (map? node)
           (not (.contains (vals node) "red")))))

(def part-one
  (->> books (tree-seq coll? identity) (filter number?) (reduce +)))

(def part-two
  (->> books (tree-seq red? identity) (filter number?) (reduce +)))
