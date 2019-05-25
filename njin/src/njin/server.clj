(ns njin.server
  (:require [org.httpkit.server]
            [njin.game :refer [create-game! tick! add-defender!]]
            [clojure.data.json]))

(def headers {"Access-Control-Allow-Origin"  "*"
              "Access-Control-Allow-Methods" "GET,POST"
              "Access-Control-Allow-Headers" "X-Requested-With,Content-Type,Cache-Control,Origin,Accept"
              "Content-Type"                 "application/json; charset=utf-8"})
              
(defn game-response
  [client-state]
  {:status  200
   :headers headers
   :body    (clojure.data.json/write-str client-state)})

(defn handle-request
  [request]
  (println "Incoming request: " (:uri request))
  (condp = (:uri request)

    "/createGame"
    (time (game-response (create-game!)))

    "/tick"
    (time
     (-> (tick!)
         (game-response)))

    "/action"
    (time (let [body (clojure.data.json/read-str (slurp (:body request)))
                action-id (get body "actionId")
                type (get body "type")
                position (get body "position")]
            (case action-id
              "defender" (-> (add-defender! type position)
                             (game-response))
              (-> (tick!)
                  (game-response)))))

    {:status  404
     :headers {"Access-Control-Allow-Origin" "*"}}))

(defonce server-instance-atom (atom nil))

(defn start!
  []
  (do
    (println "Server started")
    (org.httpkit.server/run-server (fn [request]
                                     (handle-request request))
                                   {:port 8001})))

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
