(ns advent-of-clojure.two)

(def opcodes
  (-> "resources/two.dat" slurp (as-> xs (str "[" xs "]")) read-string))

(defn deref-param [cursor opcodes]
  (nth opcodes (nth opcodes cursor)))

(defn math-op [operator]
  (fn [cursor opcodes]
    (let [fst (deref-param (+ cursor 1) opcodes)
          snd (deref-param (+ cursor 2) opcodes)
          ret (operator fst snd)
          target (nth opcodes (+ cursor 3))]
      (assoc opcodes target ret))))

(def op-add (math-op +))
(def op-mul (math-op *))

(defn intcode [opcodes]
  (loop [cursor 0 opcodes opcodes]
    (let [op (nth opcodes cursor)]
      (case op
        1  (recur (+ cursor 4) (op-add cursor opcodes))
        2  (recur (+ cursor 4) (op-mul cursor opcodes))
        99 opcodes
        :error))))

(defn gravity-assist [noun verb opcodes]
  (let [ops (assoc opcodes 1 noun 2 verb)
        ret (intcode ops)]
    [(first ret) noun verb]))

(def part-one (gravity-assist 12 2 opcodes))

(def part-two
  (let [pairs (for [verbs (range 100) nouns (range 100)] [verbs nouns])
        target 19690720]
    (->> pairs
        (map #(gravity-assist (first %) (second %) opcodes))
        (filter #(= target (first %))))))
