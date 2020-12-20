(ns advent-of-clojure.2020.14
  (:require [clojure.string :as str]))

(def init (-> "resources/2020/14.dat" slurp str/split-lines))

(defn parse-op [op]
  (if (str/includes? op "mem")
    (re-seq #"\d+" op)
    (re-seq #"[01X]+" op)))

(defn pad-binary-str [b]
  (str (apply str (repeat (- 36 (count b)) "0")) b))

(defn long->binary-str [x]
  (Long/toBinaryString (Long. x)))

(defn binary-string->long [b]
  (read-string (str "2r" b)))

(defn apply-mask [xs ms]
  (apply str (map (fn [m x] (case m (\X x) x m)) ms xs)))

(defn mask-val [m x]
  (-> x long->binary-str pad-binary-str (apply-mask m) binary-string->long))

(defn boot1 [init]
  (reduce
   (fn [{:keys [mask] :as cpu} [x y]] ; x = addr||mask, y = val
     (if-not y
       (assoc cpu :mask x)
       (assoc-in cpu [:mem x] (mask-val mask y))))
   {:mask nil :mem {}}
   init))

(def one (->> init (map parse-op) boot1 :mem vals (apply +)))

(defn indexes-of [s val]
  (remove nil? (map-indexed (fn [i c] (when (= val c) i)) s)))

(defn permute-mask
  ([mask]
   (let [indxs (indexes-of mask \X)
         mask  (apply vector mask)]
     (flatten (permute-mask mask indxs))))
  ([mask [i & is]]
   (if-not i
     (apply str mask)
     [(permute-mask (assoc mask i \a) is)
      (permute-mask (assoc mask i \b) is)])))

(defn apply-mask2 [xs ms]
  (apply str (map (fn [m x] (case m \0 x (\1 \a) \1 \b \0)) ms xs)))

(defn mask-addr [masks addr]
  (map (comp binary-string->long #(apply-mask2 addr %)) masks))

(defn masked-addrs [masks addr val]
  (zipmap (mask-addr masks (pad-binary-str (long->binary-str addr))) (repeat val)))

(defn boot2 [init]
  (reduce
   (fn [{:keys [masks] :as cpu} [x y]] ; x = addr||mask, y = val
     (if y
       (update cpu :mem #(merge % (masked-addrs masks x (Integer. y))))
       (assoc cpu :masks (permute-mask x))))
   {:masks [] :mem {}}
   init))

(def two (->> init (map parse-op) boot2 :mem vals (apply +)))
