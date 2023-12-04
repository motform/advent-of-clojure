(ns advent-of-clojure.2023.04
  (:require
   [clojure.math :as math]
   [clojure.set :as set]
   [clojure.string :as str]))

(def input (-> "resources/2023/04.dat" slurp str/split-lines))

(defn parse-numbers [s]
  (->> s (re-seq #"\d+") (map parse-long) set))

(defn parse-card [s]
  (let [[_ _ numbers] (re-find #"Card\s*(\d+): (.+)" s)
        [left right] (str/split numbers #"\|")]
    {:winning (parse-numbers left)
     :numbers (parse-numbers right)
     :winning-numbers nil
     :n 1}))

(defn validate-card [{:keys [winning numbers] :as card}]
  (assoc card :winning-numbers
    (set/intersection winning numbers)))

(defn calculate-score [{:keys [winning-numbers]}]
  (let [n (count winning-numbers)]
    (if (zero? n) 0
        (int (math/pow 2 (dec n))))))

(defn part-one [input]
  (let [xf (comp
             (map parse-card)
             (map validate-card)
             (map calculate-score))]
    (transduce xf + input)))

(defn inc-cards [cards i]
  (let [{:keys [n  winning-numbers]} (get cards i)
        from (inc i)
        to (+ from (count winning-numbers))]
    (reduce
      (fn [cards i]
        (update-in cards [i :n] #(+ n %)))
      cards
      (range from to))))

(defn part-two [input]
  (let [cards (mapv (comp validate-card parse-card) input)]
    (->> (range (count cards))
      (reduce inc-cards cards)
      (map :n)
      (reduce +))))

(comment
  (part-one input)
  (part-two input)
  )
