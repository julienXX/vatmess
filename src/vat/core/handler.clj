(ns vat.core.handler
  (:require [ring.adapter.jetty :as jetty]
            [compojure.core :refer :all]
            [compojure.handler :as handler]
            [ring.middleware.json :as middleware]
            [compojure.route :as route]
            [clojure.java.io :as io])
  (:gen-class))


(defn- load-edn [name]
  (read-string (slurp (io/resource name))))

(def eu-rates (load-edn "rates.edn"))

(defn country-codes
  []
  (map :code eu-rates))

(defn extract
  [code key]
  (->> eu-rates
    (filter (fn [map]
              (= code (:code map))))
    first
    key))

(defn country-name
  [code]
  (let [name (extract code :name)]
    (if (nil? name) "Invalid EU country code." name)))

(defn get-vat-rate
  [code]
  (if (some #{(str code)} (country-codes))
    (extract code :rate)
    "No EU VAT to charge."))

;; Routes functions
(defn index
  [req]
  {:status 200
   :body {:desc (str "This API gives you the VAT rate to use for a given EU ISO country code.")
          :usage (str "curl -X GET http://vatmess.herokuapp.com/vat_rates/fr")}})

(defn vat-rates
  [req]
  {:status 200
   :body {:vat_rates eu-rates}})

(defn vat-rate
  [req]
  (let [code (clojure.string/upper-case (get-in req [:route-params :code]))]
    {:status 200
     :body {:code code
            :country (country-name code)
            :vat_rate (get-vat-rate code)}}))

;; Routes
(defroutes app-routes
  (GET "/" [] index)
  (GET "/vat_rates" [] vat-rates)
  (GET "/vat_rates/:code" [] vat-rate)
  (route/not-found "Not Found"))

(def app
  (-> (handler/site app-routes)
      (middleware/wrap-json-body {:keywords? true})
      middleware/wrap-json-response))

(defn -main
  [& [port]]
  (let [port (Integer. (or port
                           (System/getenv "PORT")
                           5000))]
    (jetty/run-jetty #'app {:port  port
                            :join? false})))
