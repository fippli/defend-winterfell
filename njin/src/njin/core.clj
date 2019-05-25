(ns njin.core
  (:require [clojure.test :refer [function? is]]
            [njin.definitions :refer [get-definition
                                      get-definitions]]
            [njin.definitions-loader]))

(defn create-empty-state
  "Creates an empty state"
  {:test (fn []
           (is (= (keys (create-empty-state))
                  [:enemies :defenders :lives :wave :gold])))}
  []
  {:enemies []
   :defenders []
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

(defn add-enemy
  "Add an enemy to the state"
  {:test (fn []
           (is (= (-> (create-empty-state)
                      (add-enemy "nightKing" 1)
                      (get :enemies)
                      (first)
                      (get :type))
                  "nightKing")))}
  [state type id]
  (let [enemy (->
               (get-definition type)
               (assoc :id id))]
    (update state :enemies #(conj % enemy))))

(defn update-pos
  "Update the position object according to a vector"
  [{x :x y :y} {dx :x dy :y}]
  {:x (+ x dx) :y (+ y dy)})

(defn modify-enemy
  "Modify an enemy with the given id using the provided function"
  [state f enemy-id]
  (update state :enemies
          (partial map (fn [enemy]
                         (if (= enemy-id (:id enemy))
                           (f enemy)
                           enemy)))))

(defn update-object-position
"Update the position of a movable object"
[object]
  (let [dir (:direction object)]
    (update object :position #(update-pos % dir))))

(defn update-enemy-position
  "Update the position of an enemy"
  [state enemy-id]
  (modify-enemy state update-object-position enemy-id))

(defn update-all-positions
  "Update the position of all movable object"
  [state]
  (-> state
      (update :enemies (partial map update-object-position))))

(defn enemy-die
  "Actions when an enemy dies"
  {:test (fn []
           ; Check that enemy is removed
           (is (= (-> (create-empty-state)
                      (add-enemy "nightKing" 1)
                      (add-enemy "nightKing" 2)
                      (enemy-die 1)
                      (get :enemies)
                      (count))
                  1))
           ; Check that gold is increased
           (is (= (as-> (create-empty-state) $
                    (add-enemy $ "nightKing" 1)
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
                           (add-defender (create-defender {:x 100 :y 100 :range 50}))
                           (get :defenders)
                           (count))
                       1)))}
  [state defender]
  (update state :defenders (fn [defenders]
                            (conj defenders defender)))
)

(defn start-game
  "Returns start state of the game"
  []
  (-> (create-empty-state)
      (add-enemy "nightKing" 1)))

(defn tick
  "Do the ticky thing"
  {}
  [state]
  (-> state
      (update-all-positions)))

(defn main
  "Main function"
  []
  (println "Main function"))
