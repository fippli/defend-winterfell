(ns njin.core
  (:require [clojure.test :refer [function? is]]
            [njin.definitions :refer [get-definition
                                      get-definitions]]
            [njin.definitions-loader]
            [clojure.math.numeric-tower :refer [expt sqrt]]))

(defn create-empty-state
  "Creates an empty state"
  {:test (fn []
           (is (= (keys (create-empty-state))
                  [:enemies :defenders :lives :wave :gold :tick])))}
  []
  {:enemies []
   :defenders []
   :lives 3
   :wave 1
   :gold 100
   :tick 1})

(defn lose-a-life
  "removes a life"
  [state]
  (update state :lives (fn [lives] (- lives 1))))

(defn increase-tick
  "Increases tick by one"
  [state]
  (update state :tick (fn [tick] (+ tick 1))))

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

(defn get-defender
  "Gets the defender with provided id"
  {:test (fn []
           (is (= (-> {:defenders [{:type "aryaStark"
                                    :position {:x 200
                                               :y 200}
                                    :range 1000
                                    :frequency 1
                                    :id 1}]}
                      (get-defender 1)
                      (:id))
                  1)))}
  [state defender-id]
  (-> (filter (fn [defender]
                (= (:id defender) defender-id))
              (:defenders state))
      (first)))

(defn get-enemy
  "Gets the enemy with provided id"
  {:test (fn []
           (is (= (-> {:enemies [{:type "nightKing"
                                  :id 1}]}
                      (get-enemy 1)
                      (:id))
                  1)))}
  [state enemy-id]
  (-> (filter (fn [enemy]
                (= (:id enemy) enemy-id))
              (:enemies state))
      (first)))

(defn get-closest-enemy-in-range
  "Returns the closest enemy in range"
  {:test (fn []
           (is (= (-> {:enemies [{:id 1
                                  :position {:x 50
                                             :y 50}}
                                 {:id 2
                                  :position {:x 200
                                             :y 200}}]
                       :defenders [{:type "aryaStark"
                                    :position {:x 200
                                               :y 200}
                                    :range 1000
                                    :frequency 1
                                    :id 1}]}
                      (get-closest-enemy-in-range 1)
                      (:id))
                  2)))}
  [state defender-id]
  (let [defender (get-defender state defender-id)
        defenderPosition (:position defender)]

    (->> (map (fn [enemy]
                (do (println enemy)
                (println defender)
                (let [enemyPosition (:position enemy)
                      distance (sqrt (+ (expt (- (:x enemyPosition) (:x defenderPosition)) 2)
                                        (expt (- (:y enemyPosition) (:y defenderPosition)) 2)))]
                  (assoc enemy :distance distance)))) (:enemies state))
         (sort (fn [e1 e2]
                 (< (:distance e1) (:distance e2))))
         (first))))

(defn generate-object-id
 "Generates a new object id"
 [state object]
 (let [currentId (reduce (fn [acc curr]
                           (max (:id curr) acc)) 0 (object state))]
   (+ currentId 1)))

(defn add-enemy
  "Add an enemy to the state"
  {:test (fn []
           (is (= (-> (create-empty-state)
                      (add-enemy "nightKing" {:x 440
                                 :y 640})
                      (get :enemies)
                      (first)
                      (get :type))
                  "nightKing")))}
  [state type position]
  (let [id (generate-object-id state :enemies)
          enemy (->
               (get-definition type)
               (assoc :position position :id id))]
    (update state :enemies #(conj % enemy))))


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
  (let [id (generate-object-id state :defenders)
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
  {:test (fn []
           (is (= (-> (create-empty-state)
                      (update-all-positions)))))}
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
  [{pos :position}]
  (<= (:x pos) 120))

(defn enemy-reached-winterfell-action
  "For each enemy that has reached Winterfell: remove it from board and lose one life."
  {:test (fn []
           (is (= (-> (create-empty-state)
                      (enemy-reached-winterfell-action)
                      (:enemies)
                      (count))
                  0)))}
  [state]
  (reduce
   (fn [state enemy] (-> state
                         (remove-enemy (:id enemy))
                         (lose-a-life)))
   state
   (filter enemy-has-reached-Winterfell (:enemies state))))

(defn enemy-die
  "Actions when an enemy dies"
  {:test (fn []
           ; Check that enemy is removed
           (is (= (-> (create-empty-state)
                      (add-enemy "nightKing" {:x 440
                                 :y 640})
                      (add-enemy "nightKing" {:x 440
                                 :y 640})
                      (enemy-die 1)
                      (get :enemies)
                      (count))
                  1))
           ; Check that gold is increased
           (is (= (as-> (create-empty-state) $
                    (add-enemy $ "nightKing" {:x 440
                               :y 640})
                    (enemy-die $ 1)
                    (get $ :gold))
                  107)))}
  [state enemy-id]
  (let [killed-enemy (->> (get state :enemies)
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


(defn hurt-enemy
  "Deals damage to the enemy and kills it if too damaged"
  {:test (fn []
           (is (= (hurt-enemy {:enemies [{:health 10
                                          :id 1
                                          :bounty 1337}]
                               :gold 0} 1 10)
                  {:gold 1337
                   :enemies []}))
           (is (= (hurt-enemy {:enemies [{:health 60
                                          :id 1
                                          :bounty 1337}]
                               :gold 0} 1 10)
                  {:gold 0
                   :enemies [{:health 50
                              :id 1
                              :bounty 1337}]})))}
  [state enemy-id damage]
  (let [enemy (get-enemy state enemy-id)]
    (if (<= (:health enemy) damage)
      (enemy-die state enemy-id)
      (modify-enemy state (fn [enemy]
                            (update enemy :health (fn [health]
                                                    (- health damage)))) enemy-id))))

(defn shoot
  "Shoot nearest enemy"
  {:test (fn []
           (is (= (as-> (create-empty-state) $
                    (add-enemy $ "nightKing" {:x 440
                               :y 640})
                    (add-defender $ "aryaStark" {:x 123 :y 321})
                    (shoot $ (get-defender $ 1))
                    (get-enemy $ 1)
                    (:health $))
                  50)))}
  [state defender]
  (let [enemy (get-closest-enemy-in-range state (:id defender))]
  (if (nil? enemy)
  state
    (hurt-enemy state (:id enemy) (:damage defender)))))

(defn defend
  "All defenders do their actions if they should"
  {:test (fn []
           (is (= (-> (create-empty-state)
                      (add-enemy "nightKing" {:x 440
                                 :y 640})
                      (add-defender "aryaStark" {:x 123 :y 321})
                      (defend)
                      (:enemies)
                      (first)
                      (:health))
                  50))
           (is (= (-> (create-empty-state)
                      (defend)))))}
  [state]
  (reduce (fn [internalState defender]
            (if (= (mod (:tick internalState) (:frequency defender)) 0)
              (shoot internalState defender)
              internalState))
          state (:defenders state)))

(defn spawn-enemies
  "Spawns enemies"
  {:test (fn []
    (is (= (-> (create-empty-state)
    (spawn-enemies)
    (:enemies)
    (count))
    10)))}
  [state]
  (reduce
    (fn [state iteration]
      (let [yPosition (+ 640 (* iteration 10))]
      (add-enemy state "nightKing" {:x 440 :y yPosition})))
    state
    (range (:wave state))))

(defn maybe-spawn-enemies
  "Spawns enemies if applicable"
  [state]
  (if (= (mod (:tick state) 1000) 0)
  (-> (spawn-enemies state)
  (next-wave))
  state))

(defn start-game
  "Returns start state of the game"
  []
  (-> (create-empty-state)
      (add-enemy "nightKing" {:x 440
                 :y 640})
      (add-defender "aryaStark" {:x 123 :y 321})))

(defn tick
  "Do the ticky thing"
  {}
  [state]
  (-> state
      (update-all-positions)
      (increase-tick)
      (defend)
      (enemy-reached-winterfell-action)
      (maybe-spawn-enemies)))

(defn main
  "Main function"
  []
  (println "Main function"))
