
(ns njin.definition.enemy
  (:require [njin.definitions :as definitions]))

(def enemy-definitions
  {"nightKing" {:type "nightKing"
                :bounty 7
                :health 60
                :id 1
                :position {:x 0
                           :y 0}
                :direction {:x 0
                            :y 0}}})

(definitions/add-definitions! enemy-definitions)
