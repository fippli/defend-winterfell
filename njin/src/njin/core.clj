(ns njin.core
  (:require [clojure.test :refer [function? is]]))

(defn create-empty-state
  "Creates an empty state"
  []
  {:test (fn []
           (is (= (keys (create-empty-state))
                  [:enemies])))}
  {:enemies [{:name "Filip"}]})

(defn main
  "Main function"
  []
  (println "Main function"))
