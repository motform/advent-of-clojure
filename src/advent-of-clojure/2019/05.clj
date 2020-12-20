(ns advent-of-clojure.2019.05)

(def state
  (-> "resources/five.DAT"
      slurp
      (clojure.string/split #",")
      (as-> ops (mapv read-string ops))))

(def test-run [3,0,4,0,99])

(def test-opcodes [1 99 1002])

(def modes {0 (fn [state cursor] (state (state cursor))) ; position mode
            1 (fn [state cursor] (state cursor))})       ; immediate mode

(def ops {1 (fn [state [fst snd trg]] ; addition
              (assoc state trg (+ fst snd)))
          2 (fn [state [fst snd trg]] ; multiplication
              (assoc state trg (* fst snd)))
          3 (fn [state [trg]] ; input
              (assoc state trg (read-line)))
          4 (fn [state [trg]] ; output
              (do (prn trg) state))
          99 (fn [state _] ; halt
               state)})

(def steps {1 4, 2 4
            3 2, 4 2
            99 0})

(defn digits [x]
  (->> x str (map (comp read-string str))))

(defn get-op [params]
  (let [d (digits params)]
    (case (count d)
      (1 2) params
      (last d))))

(defn get-params [step op params]
  (let []))

(defn parse-params [cursor state params]
  (let [op (get-op params)
        step (steps op)
        fn (ops op)
        mem (state cursor (+ cursor step))
        params (get-params step op params)]
    [step fn params]))

(defn intcode [cursor state]
  (let [op (digits (state cursor))
        [step f & params] (parse-params state op)]
    (recur (+ step cursor) (apply f state params))))

(map #(parse-params [] %) test-opcodes)

(map get-op test-opcodes)
