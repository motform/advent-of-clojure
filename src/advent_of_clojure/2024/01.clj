(ns advent-of-clojure.2024.01)

(defn parse [str]
  (->> str
       (re-seq #"\d+")
       (partition 2)
       (reduce (fn [[l r] [ll rr]]
                 [(conj l (parse-long ll)) (conj r (parse-long rr))])
               [[] []])))

(defn part-1 [list]
  (->> list (map sort) (apply map #(- %1 %2)) (map abs) (reduce +)))

(defn part-2 [[l r]]
  (let [similarities (frequencies r)]
    (reduce + (map #(* % (or (similarities %) 0)) l))))

(comment
  (def input (parse (slurp "resources/2024/01.dat")))
  (part-1 input)
  (part-2 input)
 )
