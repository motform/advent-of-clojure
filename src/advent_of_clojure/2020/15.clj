(ns advent-of-clojure.2020.15)

(def starting-numbers [13 16 0 12 15 1])

(def t [0 3 6])

(defn prepare-numbers [numbers]
  (->> numbers (map-indexed (fn [i n] [n (list (inc i))])) (into {})))

(defn next-number [numbers prev]
  (let [[x y] (numbers prev)]
    (if y (- x y)
        0)))

(defn nth-number-spoken [n numbers]
  (loop [numbers (prepare-numbers numbers) prev (last numbers) i (inc (count numbers))]
    (let [next (next-number numbers prev)]
      (if (= n i) next
          (recur (update numbers next conj i) next (inc i))))))

(def one (nth-number-spoken 2020 starting-numbers))

(def two (nth-number-spoken 30000000 starting-numbers))
