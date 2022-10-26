(ns advent-of-clojure.2020.04
  (:require [clojure.string :as str]
            [clojure.set :as set]))

(defn parse-passport [passport]
  (->> (str/split passport #"\n|\ ")
       (map (comp (fn [[k v]] [(keyword k) v])
                  #(str/split % #":")))
       (into {})))

(def passports (-> "resources/2020/four.dat" slurp (str/split #"\n\n")))

(defn all-fields? [passport]
  (set/superset? passport #{:byr :iyr :eyr :hgt :hcl :ecl :pid}))

(defn valid-hgt? [hgt]
  (let [unit (second (str/split hgt #"\d+"))
        value (Integer. (first (str/split hgt #"\D")))]
    (case unit
      "cm" (<= 150 value 193)
      "in" (<= 59 value 76)
      false)))

;; should do this with clojure spec!
(defn valid-fields? [{:keys [byr iyr hgt eyr ecl pid hcl]}]
  (and (<= 1920 (Integer. byr) 2002)
       (<= 2010 (Integer. iyr) 2020)
       (<= 2020 (Integer. eyr) 2030)
       (valid-hgt? hgt)
       (re-matches #"#[\d|a-f]{6}+" hcl)
       (#{"amb" "blu" "brn" "gry" "grn" "hzl" "oth"} ecl)
       (= 9 (count pid))))

(defn valid-passport? [passport]
  (when (all-fields? passport) (valid-fields? passport)))

(def one (->> passports (map parse-passport) (filter all-fields?) count))

(def two (->> passports (map parse-passport) (filter valid-passport?) count))
