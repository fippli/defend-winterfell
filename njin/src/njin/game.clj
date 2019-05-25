(ns njin.game
  (:require [njin.core :refer [start-game tick add-defender]]))

(def game-atom (atom nil))

(defn create-game!
  []
  (reset! game-atom (start-game)))

(defn tick!
  []
  (swap! game-atom tick))

(defn add-defender!
  [type position]
  (swap! game-atom add-defender type position))
