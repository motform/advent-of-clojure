(ns advent-of-clojure.2020.08
  (:require [clojure.string :as str]))

(defn parse-op [[op arg]]
  [(keyword "op" op) (Integer. arg)])

(def boot-code (->> "resources/2020/eight.dat" slurp str/split-lines (mapv (comp parse-op #(str/split % #" ")))))

(def vm #:vm{:ptr 0 :acc 0 :hist #{}})

(def ops #:op{:acc (fn [vm arg]
                     (-> vm
                         (update :vm/acc + arg)
                         (update :vm/ptr inc)))
              :jmp (fn [vm arg]
                     (update vm :vm/ptr + arg))
              :nop (fn [vm _]
                     (update vm :vm/ptr inc))})

(defn run [vm code]
  (loop [{:vm/keys [hist ptr] :as vm} vm]
    (let [[op arg] (code ptr)]
      ;; (tap> vm)
      (cond
        (= (count code) (inc ptr))  (-> vm ((ops op) arg) (assoc :vm/exit :exit/success))
        (contains? hist ptr)        (assoc vm :vm/exit :exit/fail)
        :else (recur (-> vm (update :vm/hist conj ptr) ((ops op) arg)))))))

(def one (:vm/acc (run vm boot-code)))

(defn flip-op [[op arg]]
  (let [ops {:op/jmp :op/nop :op/nop :op/jmp}]
    [(ops op) arg]))

(defn code-permutations [code]
  (reduce (fn [cs i]
            (if-not (= :op/acc (first (code i)))
              (conj cs (update code i flip-op))
              cs))
          [] (range (count code))))

(def two (:vm/acc
          (->> boot-code
               code-permutations
               (map (partial run vm))
               (filter #(= :exit/success (:vm/exit %)))
               first)))
