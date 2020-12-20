(ns advent-of-clojure.2020.06
  (:require [clojure.string :as str]
            [clojure.set :as set]))

(def groups (-> "resources/2020/six.dat" slurp (str/split #"\n\n") ))

(def one (->> groups (map (comp count distinct #(str/replace % #"\n" ""))) (apply +)))

(defn all-yes [group]
  (->> group str/split-lines (map (partial into #{})) (apply set/intersection) count))

(def two (->> groups (map all-yes) (apply +)))
