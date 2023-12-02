(ns advent-of-clojure.2023.02
  (:require [clojure.string :as str]))

(def input (->> (-> "resources/2023/02.dat" slurp (str/split-lines))))

(defn parse-color [s]
  (let [[_ number color] (re-find #"(\d+) (\w+)" s)]
    [(keyword color) (parse-long number)]))

(defn parse-round [round]
  (->> (str/split round #",")
    (map parse-color)
    (into {})))

(defn parse-game [line]
  (let [[game & rounds] (str/split line #":|;")]
    {:game (parse-long (re-find #"\d+" game))
     :rounds (mapv parse-round rounds)}))

(defn resolve-rounds [game]
  (update game :rounds (partial apply merge-with max)))

(defn possible-game? [max-cubes]
  (fn [game]
    (->> (merge-with - (:rounds game) max-cubes)
      vals
      (every? #(<= % 0)))))

(defn part-one [input max-cubes]
  (let [xf (comp
             (map parse-game)
             (map resolve-rounds)
             (filter (possible-game? max-cubes))
             (map :game))]
    (transduce xf + input)))

(defn power [{:keys [rounds]}]
  (apply * (vals rounds)))

(defn part-two [input]
  (let [xf (comp
             (map parse-game)
             (map resolve-rounds)
             (map power))]
    (transduce xf + input)))

(comment
  (part-one input {:red 12 :green 13 :blue 14})
  (part-two input)
  )

