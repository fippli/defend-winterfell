(ns njin.game
  (:require [njin.core :refer [start-game tick]]))

(def game-atom (atom nil))

(defn create-game!
  []
  (reset! game-atom (start-game)))

(defn tick!
  []
  (swap! game-atom tick))
