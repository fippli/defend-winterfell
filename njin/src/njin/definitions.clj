(ns njin.definitions (:require [clojure.test :refer [is]]))
(defonce definitions-atom (atom {}))
(defn add-definitions! [definitions]
  (swap! definitions-atom merge definitions))

(defn get-definitions []
  (vals @definitions-atom))

(defn get-definition
  "Gets the definition identified by the type."
  {:test (fn []
           (is (= (get-definition "nightKing")
                {:type "nightKing"
                 :bounty 7
                 :health 60
                 :id 1
                 :position {:x 0
                            :y 0}
                 :direction {:x 0
                             :y 0}}))
           (is (= (get-definition {:type "nightKing"})
                (get-definition "nightKing"))))}
  [type-or-entity]
  {:pre [(or (string? type-or-entity)
             (and (map? type-or-entity)
                  (contains? type-or-entity :type)))]}
  (get @definitions-atom
                        (if (string? type-or-entity)
                          type-or-entity
                          (:type type-or-entity))))
