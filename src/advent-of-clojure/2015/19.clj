(ns advent-of-clojure.2015.19
  (:require [clojure.string :as str]))

;; (def replacements
;;   (->> "resources/2015/nineteen.dat" slurp str/split-lines (drop-last 2) (map #(str/split % #" => "))
;;       (reduce (fn [m [k v]] (update m k #(conj % v))) {})))

(def replacements
  (->> "resources/2015/nineteen.dat" slurp str/split-lines (drop-last 2) (map #(str/split % #" => "))))

(def molecules
  (re-pattern (apply str (interpose \| (into #{} (map first replacements))))))

(def medicine
  (->> "resources/2015/nineteen.dat" slurp str/split-lines last (re-seq molecules)))

(defn replace-molecule [molecue replacements])

;; (def part-one
;;   (->> medicine
;;       (map #(count (replacements %)))
;;       (reduce +)))
