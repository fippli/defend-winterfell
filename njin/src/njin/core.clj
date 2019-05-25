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

(defn generate-defender-id
  "Generates a new defender id"
  [state]
  (let [currentId (reduce (fn [acc curr]
                            (max (:id curr) acc)
                            ) 0 (:defenders state))]
    (+ currentId 1)))

(defn add-defender
  "Add a defender to the state"
  {:test (fn []
           (is (= (-> (create-empty-state)
                      (add-defender "aryaStark" {})
                      (get :defenders)
                      (first)
                      (get :type))
                  "aryaStark"))
           (is (= (-> (create-empty-state)
                      (add-defender "aryaStark" {:x 123 :y 321})
                      (get :defenders)
                      (first)
                      (get :position))
                  {:x 123 :y 321})))}
  [state type position]
  (let [id (generate-defender-id state)
        defender (->
                  (get-definition type)
                  (assoc :id id :position position))]
    (update state :defenders #(conj % defender))))

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

(defn remove-enemies
  "Remove all enemies that matches a predicate"
  [state pred]
  (update state :enemies
    (partial filter (comp not pred))))

(defn remove-enemy
  "Remove an enemy with a given id from the board"
  [state enemy-id]
  (remove-enemies state #(= (:id %) enemy-id)))

(defn enemy-has-reached-Winterfell
  "Returns whether the given enemy has reached Winterfell or not"
  [{pos :pos}]
  (<= (:x pos) 120))

(defn enemy-reached-winterfell-action
  "For each enemy that has reached Winterfell: remove it from board and lose one life."
  [state]
  (reduce
    (fn [state' enemy] (-> state'
                            (remove-enemy (:id enemy))
                            (lose-a-life)))
    state
    (filter enemy-has-reached-Winterfell (:enemies state))))

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
    (-> state
        (remove-enemy enemy-id)
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



(defn start-game
  "Returns start state of the game"
  []
  (-> (create-empty-state)
      (add-enemy "nightKing" 1)
      (add-defender "aryaStark" 1)))

(defn tick
  "Do the ticky thing"
  {}
  [state]
  (-> state
      (update-all-positions)
      (enemy-reached-winterfell-action)))

(defn main
  "Main function"
  []
  (println "Main function"))
