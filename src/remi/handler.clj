(ns remi.handler
  (:use compojure.core)
  (:use [ring.middleware.basic-authentication
         :only [wrap-basic-authentication]])
  (:use ring.middleware.json
        [remi.zookeeper :only [zk-read zk-write]]
        [remi.riak :only [riak-read riak-write]])
  (:require [compojure.handler :as handler]
            [compojure.route :as route]
            [cheshire.core :as json]))

(defn uuid []
  (str (java.util.UUID/randomUUID)))

(defn create [bucket-name id data]
  (let [payload (conj {:id id} data)
        key (riak-write bucket-name payload)]
    (zk-write id key)
    payload))

(defn show [bucket-name id]
  (riak-read bucket-name (zk-read id)))

(defn- emit-json
  [type & [status]]
  {:headers {"Content-Type"  "application/json" 
             "Vary"          "Accept-Encoding"}
   :status (or status 200)
   :body (json/generate-string type)})

(defroutes app-routes
  (GET "/" [& params]
       (emit-json {}))
  (GET "/bucket/:bucket-name/:id" [bucket-name id :as r]
       (emit-json (show bucket-name id)))
  (POST "/bucket/:bucket-name" [bucket-name :as r]
        (emit-json (create bucket-name (uuid) (:body r))))
  (PUT "/bucket/:bucket-name/:id" [bucket-name id :as r]
       (emit-json (create bucket-name id (:body r))))
  
  (route/not-found "Not found"))

(def app
  (->
   (handler/site app-routes)
   wrap-json-body))
