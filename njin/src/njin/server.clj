(ns njin.server
  (:require [org.httpkit.server]
            [njin.game :refer [create-game!]]
            [clojure.data.json]))

(defn game-response
  [client-state]
  {:status  200
   :headers {"Access-Control-Allow-Origin" "*"
             "Content-Type"                "application/json; charset=utf-8"}
   :body    (clojure.data.json/write-str client-state)})

(defn handle-request
  [request]
  (println "Incoming request: " (:uri request))
  (condp = (:uri request)

    "/createGame"
    (time (game-response (create-game!)))

    {:status  404
     :headers {"Access-Control-Allow-Origin" "*"}}))

(defonce server-instance-atom (atom nil))

(defn start!
  []
  (org.httpkit.server/run-server (fn [request]
                                   (handle-request request))
                                 {:port 8001}))

(defn stop!
  []
  (when-not (nil? @server-instance-atom)
    ;; graceful shutdown: wait 100ms for existing requests to be finished
    ;; :timeout is optional, when no timeout, stop immediately
    (@server-instance-atom :timeout 100)
    (reset! server-instance-atom nil)))

(comment
  (start!)
  (stop!))
