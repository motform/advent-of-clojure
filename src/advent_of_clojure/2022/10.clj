(ns advent-of-clojure.2022.10
  (:require [clojure.string :as str]))

(defn parse [[op v]]
  (cond-> [(keyword op)] v (conj (parse-long v))))

(def input (->> "resources/2022/10.dat" slurp str/split-lines
                (mapv (comp parse #(str/split % #"\s")))))

(defn run-cycle [{:keys [clock register] :as state}]
  (-> state
      (update :samples conj [clock register])
      (update :clock inc)))

(defn final-run [{:keys [clock register] :as state}]
  (update state :samples conj [clock register]))

(defn run [instructions]
  (final-run
   (reduce (fn [state [op value]]
             (case op
               :noop (run-cycle state)
               :addx (update
                      (run-cycle (run-cycle state))
                      :register + value)))
           {:register 1 :clock 1 :samples [[0 1]]}
           instructions)))

(defn samples [start sample-rate samples]
  (vals (select-keys samples (range start (count samples) sample-rate))))

(def part-one
  (->> input run :samples
       (samples 20 40)
       (map (partial apply *))
       (reduce +)))

(def empty-CRT (vec (repeat 240 \.)))

(defn lit-pixel? [middle target]
  (contains? (set (range (dec middle) (inc (inc middle)))) target))

(defn light-pixels [CRT samples]
  (reduce
   (fn [CRT [clock register]]
     (let [pixel (dec clock)
           sprite (+ register (* 40 (int (/ pixel 40))))]
       (if (lit-pixel? sprite pixel)
         (assoc CRT pixel \#)
         CRT)))
   CRT samples))

(defn draw [CRT]
  (doseq [line (partition 40 CRT)]
    (apply println line)))

(def part-two
  (->> input run :samples rest
       (light-pixels empty-CRT)
       draw))
