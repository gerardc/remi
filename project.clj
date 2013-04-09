(defproject remi "0.1.0-SNAPSHOT"
  :description "Simple data persistence web service implementing the epochal time model"
  :url ""
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [
                 [org.clojure/clojure "1.4.0"]
                 [zookeeper-clj "0.9.1"]
                 [clj-http "0.7.0"]
                 [cheshire "5.0.2"]
                 [compojure "1.1.1"]
                 [ring/ring-json "0.2.0"]
                 [ring-basic-authentication "1.0.1"]
                 ]
  :plugins [[lein-ring "0.7.1"]]
  :ring {:handler remi.handler/app}
  :min-lein-version "2.0.0"
  :profiles
  {:dev {:dependencies [[ring-mock "0.1.2"]]}})
