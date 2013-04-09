(ns remi.riak
  (require [clj-http.client :as http]
           [clojure.string :as s]
           [cheshire.core :as c]))

(def settings
  ((comp :riak read-string slurp) "settings.clj"))

(defn url [bucket]
  (str (:http_endpoint settings) "/" bucket))

(defn riak-read [bucket key]
  (let [resp (http/get (str (url bucket) "/" key)
                       {:basic-auth (:http_basic settings)})]
    (c/parse-string (:body resp))))

(defn riak-write [bucket data]
  (let [resp (http/post (url bucket)
                        {:basic-auth (:http_basic settings)
                         :body (c/generate-string data)
                         :content-type :json
                         :socket-timeout 1000 ;; in milliseconds
                         :conn-timeout 1000   ;; in milliseconds
                         :accept :json
                         :decompress-body false})]
    (println resp)
    ((comp last
           #(s/split % #"/")
           #(% "location")
           :headers) resp)))
