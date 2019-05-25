(ns njin.game
  (:require [njin.core :refer [start-game]]))

(def game-atom (atom nil))

(defn create-game!
  []
  (reset! game-atom (start-game)))
