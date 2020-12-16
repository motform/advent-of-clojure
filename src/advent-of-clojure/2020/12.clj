(ns advent-of-clojure.2020.12
  (:require [clojure.string :as str]))

(def instructions (->> "resources/2020/12.dat" slurp str/split-lines (map (comp (fn [[d x]] [(keyword d) (Integer. x)]) (partial re-seq #"\D|\d+")))))

(def test-instructions [[:F 10] [:N 3] [:F 7] [:R 90] [:F 11]])

(def turns {:R {:N :E, :E :S, :S :W, :W :N}
            :L {:N :W, :W :S, :S :E, :E :N}})

(defn navigate [ferry instructions]
  (reduce
   (fn [{:ferry/keys [facing] :as ferry} [direction distance]]
     (case direction
       (:N :S) (update ferry :ferry/Y (case direction :N + :S -) distance)
       (:E :W) (update ferry :ferry/X (case direction :E + :W -) distance)
       (:L :R) (assoc  ferry :ferry/facing (last (take (inc (/ distance 90)) (iterate (turns direction) facing))))
       :F (recur ferry [facing distance])))
   ferry instructions))

(def one (->> (select-keys (navigate #:ferry{:facing :E :X 0 :Y 0} instructions) [:ferry/X :ferry/Y])
              vals (map #(Math/abs %)) (apply +)))

(defn rotate-waypoint [direction {:waypoint/keys [X Y facing] :as ferry}]
  (let [[X' Y' D] (case facing
                    :NE (case direction :R [Y (- 0 X) :SE]      :L [(- 0 Y)      X :NW])
                    :SE (case direction :R [Y (- 0 X) :SW]      :L [(Math/abs Y) X :NE])
                    :NW (case direction :R [Y (Math/abs X) :NE] :L [(- 0 Y)      X :SW])   
                    :SW (case direction :R [Y (Math/abs X) :NW] :L [(Math/abs Y) X :SE]))] 
    (assoc ferry :waypoint/X X' :waypoint/Y Y' :waypoint/facing D)))

(defn navigate-waypoint [ferry instructions]
  (reduce
   (fn [{:waypoint/keys [X Y] :as ferry} [direction distance]]
     (case direction
       (:N :S) (update ferry :waypoint/Y (case direction :N + :S -) distance)
       (:E :W) (update ferry :waypoint/X (case direction :E + :W -) distance)
       (:L :R) (last (take (inc (/ distance 90)) (iterate (partial rotate-waypoint direction) ferry)))
       :F (-> ferry (update :ferry/X + (* distance X)) (update :ferry/Y + (* distance Y)))))
   ferry instructions))

(def two (->> (select-keys (navigate-waypoint {:ferry/X 0 :ferry/Y 0 :waypoint/facing :NE :waypoint/X 10 :waypoint/Y 1} instructions) [:ferry/X :ferry/Y])
              vals (map #(Math/abs %)) (apply +)))
