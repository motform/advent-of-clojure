(ns advent-of-clojure.util.data-structures
  (:import [clojure.lang PersistentQueue]))

(defn queue [& args]
  (into PersistentQueue/EMPTY args))
