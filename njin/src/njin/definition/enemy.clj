
(ns njin.definition.enemy
  (:require [njin.definitions :as definitions]))

(def enemy-definitions
  {"nightKing" {:type "nightKing"
                :bounty 7
                :health 60
                :id 1
                :position {:x 350
                           :y 640}
                :direction {:x 0
                            :y -2}}})

(definitions/add-definitions! enemy-definitions)
