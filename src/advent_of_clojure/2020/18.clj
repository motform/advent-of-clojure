(ns advent-of-clojure.2020.18
  (:require [clojure.string :as str]
            [clojure.edn :as edn]))

(def input (-> "resources/2020/18.dat" slurp str/split-lines))

(defn wrap [x]
  (str \( x \)))

(defn oper? [token]
  (#{'+ '*} token))

(defn parse-expr [[t & ts]]
  (:acc
   (reduce 
    (fn [{:keys [op] :as state} token]
      (cond (oper? token) (assoc state :op token)
            (list? token) (update state :acc (resolve op) (parse-expr token))
            :else (update state :acc (resolve op) token)))
    {:acc (if (list? t) (parse-expr t) t) :op nil}
    ts)))

(defn parse-line [line]
  (->> line wrap edn/read-string parse-expr))

(def one (->> input (map parse-line) (reduce +)))

(defn parse-expr2 [[t & ts]]
  (reduce 
   (fn [{:keys [op] :as state} token]
     (cond (= '* token)  (assoc state :op token)
           (list? token) (update state :exps conj (resolve op) (parse-expr token))
           :else (update state :exps conj (resolve op) token)))
   {:acc (if (list? t) (parse-expr t) t) :exps []}
   ts))

(defn parse-line2 [line]
  (->> line wrap edn/read-string parse-expr))

(def two (->> input (map parse-line2) (reduce +)))

(comment
  (-> "5 * 9 * (7 * 3 * 3 + 9 * 3 + (8 + 6 * 4))" wrap edn/read-string parse-expr2)

  (-> "((2 + 4 * 9) * (6 + 9 * 8 + 6) + 6) + 2 + 4 * 2" wrap edn/read-string parse-expr2)
  )
