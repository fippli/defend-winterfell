
(ns njin.definition.defender
  (:require [njin.definitions :as definitions]))

(def defender-definitions
  {"aryaStark" {:type "aryaStark"
                :position {:x 200
                           :y 200}
                :range 250
                :frequency 50
                :damage 5}})

(definitions/add-definitions! defender-definitions)
