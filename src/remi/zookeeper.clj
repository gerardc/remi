(ns remi.zookeeper
  (require [zookeeper :as z]
           [zookeeper.data :as d]
           [zookeeper.util :as u]))

(def settings
  ((comp :zookeeper read-string slurp) "settings.clj"))

(defn with-client [f & args]
  (let [client (z/connect (:endpoint settings))
        value (apply f client args)]
    (z/close client)
    value))

(defn node-read-data [client path]
  (String. (:data (z/data client path)) "UTF-8"))

(defn node-write [client id value]
  (let [node (z/create-all client
                            (str "/" (:namespace settings) "/" id "/value-")
                            :sequential? true
                            :persistent? true
                            :data (d/to-bytes value))]
    {:value value :id id :node node}))

(defn node-latest-value [client id]
  (let [path (str "/" (:namespace settings) "/" id)
        value-key ((comp last
                         u/sort-sequential-nodes
                         #(z/children client %)) path)]
    (node-read-data client (str path "/" value-key))))

(defn zk-write [id data]
  (with-client node-write id data))

(defn zk-read [id]
  (with-client node-latest-value id))


