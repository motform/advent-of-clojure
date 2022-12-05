(ns advent-of-clojure.2022.05
  (:require [clojure.string :as str]))

(defn transpose [matrix]
  (apply map vector matrix))

(defn ->stack [start]
  {(-> start last str parse-long) (drop-last start)})

(defn parse [input]
  (let [[start moves] (str/split input #"\n\n")]
    {:stacks (->> start str/split-lines
                  transpose
                  (map (partial remove #(#{\space \[ \]} %)))
                  (remove empty?)
                  (map ->stack)
                  (apply merge))
     :moves (->> moves str/split-lines
                 (map (comp (partial map (comp parse-long str))
                            #(re-seq #"\d+" %))))}))

(def input (-> "resources/2022/05.dat" slurp parse))

(defn move [crane_mover_9001?]
  (fn [{stacks :stacks [[n from to]] :moves :as state}]
    (let [boxes (->> (stacks from) (take n))
         boxes (if crane_mover_9001? boxes (reverse boxes))]
     (-> state
         (update-in [:stacks from] #(drop n %))
         (update-in [:stacks to]   #(concat boxes %))
         (update :moves rest)))))

(defn top-crates [stacks]
  (->> stacks (into (sorted-map)) vals (map first) (apply str)))

(defn sort-crates [input crane_mover_9001?]
  (->> input
       (iterate (move crane_mover_9001?))
       (take-while #(seq (% :moves)))
       last ((move crane_mover_9001?))
       :stacks top-crates))

(def part-one (sort-crates input false))
(def part-two (sort-crates input true))
