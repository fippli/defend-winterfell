
(ns njin.definition.defender
  (:require [njin.definitions :as definitions]))

(def defender-definitions
  {"tower" {:position {:x 500
                       :y 100
                       :range 50}}})

(definitions/add-definitions! defender-definitions)
(println "Definitions loaded")
