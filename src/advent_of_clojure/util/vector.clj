(ns advent-of-clojure.util.vector
  (:refer-clojure :exclude [- +]))

(def + (partial mapv clojure.core/+))

(def - (partial mapv clojure.core/-))
