(ns advent-of-clojure.2022.11
  (:require [clojure.string :as str]))

(defn first-number [s]
  (parse-long (re-find #"\d+" s)))

(defn parse [block]
  (let [[monkey items op test true? false?] (str/split-lines block)
        [_ _ _ x op y] (re-seq #"\S+" op)]
    [(first-number monkey)
     {:items     (->> items (re-seq #"\d+") (mapv parse-long))
      :operation [(parse-long x) (read-string op) (parse-long y)]
      :test   (first-number test)
      :true?  (first-number true?)
      :false? (first-number false?)
      :inspected 0}]))

(def input (into (sorted-map) (mapv parse (-> "resources/2022/11.dat" slurp (str/split #"\n\n")))))

(defn new-worry-level [item [x op y]]
  ((resolve op) (or x item) (or y item)))

(defn reduce-worry-level [item]
  (int (/ item 3)))

(defn gate-worry-level [monkeys]
  (let [div (->> monkeys vals (map :test) (reduce *))]
    (fn [item]
      (rem item div))))

(defn throwing-target [item test true? false?]
  (if (zero? (mod item test)) true? false?))

(defn round [worrying? monkeys]
  (let [worry-f (if worrying? reduce-worry-level (gate-worry-level monkeys))]
    (loop [monkeys monkeys monkey 0]
      (let [{:keys [items operation test true? false?]} (get monkeys monkey)]
        (cond
          (= monkey (count monkeys)) monkeys
          (empty? items) (recur monkeys (inc monkey))
          :else (let [item (-> items first (new-worry-level operation) worry-f)
                      target (throwing-target item test true? false?)]
                  (recur
                   (-> monkeys
                       (update-in [target :items] conj item)
                       (update-in [monkey :items] (comp vec rest))
                       (update-in [monkey :inspected] inc))
                   monkey)))))))

(def part-one
  (->> input (iterate (partial round true)) (take 21)
       last vals (map :inspected)
       (sort >) (take 2) (apply *)))

(def part-two
  (->> input (iterate (partial round false)) (take 10001)
       last vals (map :inspected)
       (sort >) (take 2) (apply *)))
