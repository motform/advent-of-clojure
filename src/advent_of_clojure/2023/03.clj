(ns advent-of-clojure.2023.03
  (:refer-clojure :exclude [symbol? test])
  (:require
    [clojure.string :as str]
    [clojure.set :as set]
    [advent-of-clojure.util.matrix :as grid]))

(def input (->> (-> "resources/2023/03.dat" slurp (str/split-lines))))

(def not-symbol (re-pattern #"[^\d\.]+"))

(defn symbol? [s]
  (re-matches not-symbol (str s)))

(defn digit-x [row y index number]
  (let [from (str/index-of row number index)
        to (+ from (count number))]
    (vec (for [x (range from to)] [y x]))))

(defn digits [grid y]
  (let [row (get grid y)]
    (->> (re-seq #"\d+" row)
         (reduce
          (fn [{:keys [index] :as state} number]
            (let [digit (digit-x row y index number)]
              (-> state
                  (assoc :index (-> digit last last inc))
                  (update :digits conj digit))))
          {:digits [] :index 0})
         :digits)))

(defn adjecent-to-digits [bounds digits]
  (->> digits
    (mapcat (partial grid/diagonal-neighbours bounds))
    (remove (set digits))))

(defn adjencent-symbol? [grid bounds points]
  (->> points
       (adjecent-to-digits bounds)
       (some #(symbol? (get-in grid %)))))

(defn get-number [grid points]
  (->> points
       (map #(get-in grid %))
       (apply str)
       parse-long))

(defn part-one [grid]
  (let [bounds (grid/bounds grid)
        digits (mapcat #(digits grid %) (range (count grid)))
        xf (comp
            (filter #(adjencent-symbol? grid bounds %))
            (map #(get-number grid %)))]
    (transduce xf + digits)))

(defn asterisks [grid y]
  (->> (get grid y)
    (map-indexed (fn [x c] (when (= c \*) [y x])))
    (remove nil?)))

(defn gear? [grid bounds digits asterisk]
  (let [neighbours (set (grid/diagonal-neighbours bounds asterisk))
        adjecent-numbers (->> digits
                           (filter #(seq (set/intersection neighbours (set %))))
                           (map #(get-number grid %)))]
    (when (= 2 (count adjecent-numbers))
      adjecent-numbers)))

(defn part-two [grid]
  (let [bounds (grid/bounds grid)
        rows (range (count grid))
        asterisks (mapcat #(asterisks grid %) rows)
        digits (mapcat #(digits grid %) rows)
        xf (comp
             (keep #(gear? grid bounds digits %))
             (map #(apply * %)))]
    (transduce xf + asterisks)))

(comment
  (part-one input)
  (part-two input)
  )
