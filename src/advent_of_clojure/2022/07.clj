(ns advent-of-clojure.2022.07
  (:require [clojure.string :as str]
            [clojure.walk :as walk]))

(defmulti parse first)

(defmethod parse \$ [line]
  (let [[command target] (re-seq #"\w+|\/|\.+" line)]
    {:type :command :command (keyword command) :target target}))

(defmethod parse \d [line]
  (let [[_ name] (str/split line #" ")]
    {:type :dir :name name}))

(defmethod parse :default [line]
  (let [[size name] (str/split line #" ")]
    {:type :file :size (parse-long size) :name name}))

(def input (->> "resources/2022/07.dat" slurp str/split-lines (mapv parse)))

(defmulti build-tree #(-> % :commands first :type))

(defn change-dir [state target]
  (case target
    "/" (assoc state :path ["/"])
    ".." (update state :path pop)
    (update state :path conj target)))

(defmethod build-tree :command [{:keys [commands] :as state}]
  (let [{:keys [command target]} (first commands)]
    (cond-> state
      (= command :cd) (change-dir target)
      :always (update :commands rest))))

(defmethod build-tree :default [{:keys [path commands] :as state}]
  (let [{:keys [type size name]} (first commands)]
    (-> state
        (update :tree assoc-in (conj path name) {:file/type type :file/size size})
        (update :commands rest))))

(defn dir-size [tree]
  (for [[_ v] tree]
    (when (map? v)
      (if-let [size (:file/size v)]
        size
        (dir-size v)))))

(defn dir? [node]
  (= :dir (get node :file/type)))

(defn calculate-dir-sizes [node]
  (if (dir? node)
    (assoc node :file/size (->> node dir-size flatten (remove nil?) (apply +)))
    node))

(defn dir-sizes [tree]
  (let [sizes (atom [])]
    (walk/prewalk
     (fn [node]
       (when (dir? node) (swap! sizes conj (:file/size node)))
       node)
     tree)
    @sizes))

(defn filesystem-tree [commands]
  (->> {:tree {"/" {:file/type :dir}} :path [] :commands commands}
       (iterate build-tree)
       (take-while #(-> % :commands seq))
       last build-tree))

(def max-size 100000)

(def part-one
  (->> input
      filesystem-tree
      (walk/prewalk calculate-dir-sizes)
      dir-sizes
      (filter #(> max-size %))
      (reduce +)))

(defn find-dir-to-delete [input]
  (let [filesystem-tree (walk/prewalk calculate-dir-sizes (filesystem-tree input))
        sizes (dir-sizes filesystem-tree)
        free (- 70000000 (apply max sizes))]
    (->> sizes
        (map (fn [size] [(+ free size) size]))
        (filter #(< 30000000 (first %)))
        (map second)
        (apply min))))

(def part-two (find-dir-to-delete input))

(comment
  ;; We want a tree to look like this:
  (def example-tree
    {"/" {:file/type :dir,
          "a" {:file/type :dir,
               "e" {:file/type :dir,
                    "i" #:file{:type :file, :size 584}},
               "f" #:file{:type :file, :size 29116},
               "g" #:file{:type :file, :size 2557},
               "h.lst" #:file{:type :file, :size 62596}},
          "b.txt" #:file{:type :file, :size 14848514},
          "c.dat" #:file{:type :file, :size 8504156},
          "d" {:file/type :dir,
               "j" #:file{:type :file, :size 4060174},
               "d.log" #:file{:type :file, :size 8033020},
               "d.ext" #:file{:type :file, :size 5626152},
               "k" #:file{:type :file, :size 7214296}}}})
  ;; So that we can (get-in tree path) as path is a vector of keys.
  )


