
(ns njin.definition.defender
  (:require [njin.definitions :as definitions]))

(def defender-definitions
  {"aryaStark" {:type "aryaStark"
                :position {:x 200
                           :y 200}
                :range 1000
                :frequency 1
                :damage 10}})

(definitions/add-definitions! defender-definitions)
