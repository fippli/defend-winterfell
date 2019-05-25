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
              :type "nightKing"
              :position {:x 0
                         :y 0}
              :direction {:x 10
                          :y 0}}
             {:bounty 7
              :health 60
              :id 2
              :type "nightKing"
              :position {:x 0
                         :y 0}
              :direction {:x 10
                          :y 0}}]
   :defenders [{:position {:x 500
                          :y 100
                          :range 50}}]
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

(defn increase-gold
  "Increases the amount of gold"
  {:test (fn []
           (is (= (->
                   (create-empty-state)
                   (increase-gold 50)
                   (get :gold))
                  150)))}
  [state amount]
  (update state :gold
          (fn [gold]
            (+ gold amount))))

(defn get-bounty
  "Returns the bounty of the provided enemy"
  {:test (fn []
           (is (= (get-bounty {:bounty 56})
                  56)))}
  [enemy]
  (get enemy :bounty))

(defn enemy-die
  "Actions when an enemy dies"
  {:test (fn []
           ; Check that enemy is removed
           (is (= (-> (create-empty-state)
                      (enemy-die 1)
                      (get :enemies)
                      (count))
                  1))
           ; Check that gold is increased
           (is (= (as-> (create-empty-state) $
                    (enemy-die $ 1)
                    (get $ :gold))
                  107)))}
  [state enemy-id]
  (let [killed-enemy (->>(get state :enemies)
                       (filter (fn [enemy] (= (:id enemy) enemy-id)))
                       (first))]
    (->
     (update state :enemies
             (fn [enemies]
               (filter (fn [enemy] (not= (:id enemy) enemy-id)) enemies)))
     (increase-gold (get-bounty killed-enemy)))))

(defn create-defender
  "creates a defender given a position and range"
  {:test (fn [] (is (= (create-defender {:x 100 :y 200 :range 25})
                        {:position {:x 100
                                    :y 200
                                    :range 25}})))}
  [{x :x y :y range :range}]
    {:position {:x x
                :y y
                :range range}})

(defn add-defender
  "Adds a defender"
  {:test (fn [] (is (= (-> (create-empty-state)
                           (add-defender {:position {:x 500
                                                  :y 100
                                                  :range 50}})
                           (get :defenders)
                           (count))
                       2)))}
  [state defender]
  (update state :defenders (fn [defenders]
                            (conj defenders defender)))
)

(defn start-game
  "Returns start state of the game"
  []
  (create-empty-state))

(defn main
  "Main function"
  []
  (println "Main function"))

(defn add-enemy
  "Add an enemy to the state"
  [state]
  (let [enemy {:bounty 7
               :health 60
               :id 2
               :type "nightKing"
               :position {:x 10 :y 0}
               :direction {:x 10 :y 0}}]
    (update state :enemies #(conj % enemy))))
