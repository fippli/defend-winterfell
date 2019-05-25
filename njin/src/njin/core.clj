(ns njin.core
  (:require [clojure.test :refer [function? is]]))

(defn create-empty-state
  "Creates an empty state"
  []
  {:test (fn []
           (is (= (keys (create-empty-state))
                  [:enemies :defenders :lives :wave :gold])))}
  {:enemies [{:bounty 7
              :health 60
              :id 1
              :position {:x 0
                         :y 0}
              :direction {:x 10
                          :y 0}}]
   :defenders [:position {:x 500
                          :y 100
                          :range 50}]
   :lives 3
   :wave 0
   :gold 100})

(defn lose-a-life
  "removes a life"
  [state]
  (update state :lives (fn [lives] (- lives 1))))

(defn next-wave
  "adds 1 to wave"
  [state]
  (update state :wave (fn [wave] (+ wave 1))))

(defn main
  "Main function"
  []
  (println "Main function"))
