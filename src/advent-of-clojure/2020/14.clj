(ns advent-of-clojure.2020.14
  (:require [clojure.string :as str]))

(def init (-> "resources/2020/14.dat" slurp str/split-lines))

(def t (str/split-lines "mask = XXXXXXXXXXXXXXXXXXXXXXXXXXXXX1XXXX0X
mem[8] = 11
mem[7] = 101
mem[8] = 0"))

(defn parse-op [op]
  (if (str/includes? op "mem")
    (re-seq #"\d+" op)
    (re-seq #"[01X]+" op)))

(defn pad-binary-str [b]
  (str (apply str (repeat (- 36 (count b)) "0")) b))

(defn long->binary-str [x]
  (Long/toBinaryString (Long. x)))

(defn mask [m x]
  (case m (\X x) x m))

(defn apply-mask [xs ms]
  (apply str (map mask ms xs)))

(defn binary-string->long [b]
  (read-string (str "2r" b)))

(defn mask-val [m x]
  (-> x long->binary-str pad-binary-str (apply-mask m) binary-string->long))

(defn update-mem [mem addr val]
  (assoc mem addr val))

(defn boot [init]
  (reduce
   (fn [{:keys [mask] :as cpu} [x y]] ; x = addr||mask, y = val
     (if-not y
       (assoc cpu :mask x)
       (update cpu :mem update-mem x (mask-val mask y))))
   {:mask nil :mem {}}
   init))

(def one (->> init (map parse-op) boot :mem vals (apply +)))
