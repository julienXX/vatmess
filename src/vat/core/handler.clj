(ns vat.core.handler
  (:require [ring.adapter.jetty :as jetty]
            [compojure.core :refer :all]
            [compojure.handler :as handler]
            [ring.middleware.json :as middleware]
            [compojure.route :as route])
  (:gen-class))

(def eu-rates [{:name "Belgium" :code "BE" :rate "21"}
               {:name "Bulgaria" :code "BG" :rate "20"}
               {:name "Czech Republic" :code "CZ" :rate "21"}
               {:name "Denmark" :code "DK" :rate "25"}
               {:name "Germany" :code "DE" :rate "19"}
               {:name "Estonia" :code "EE" :rate "20"}
               {:name "Greece" :code "EL" :rate "23"}
               {:name "Spain" :code "ES" :rate "21"}
               {:name "France" :code "FR" :rate "20"}
               {:name "Croatia" :code "HR" :rate "25"}
               {:name "Ireland" :code "IE" :rate "23"}
               {:name "Italy" :code "IT" :rate "22"}
               {:name "Cyprus" :code "CY" :rate "19"}
               {:name "Latvia" :code "LV" :rate "21"}
               {:name "Lithuania" :code "LT" :rate "21"}
               {:name "Luxembourg" :code "LU" :rate "15"}
               {:name "Hungary" :code "HU" :rate "27"}
               {:name "Malta" :code "MT" :rate "18"}
               {:name "Netherlands" :code "NL" :rate "21"}
               {:name "Austria" :code "AT" :rate "20"}
               {:name "Poland" :code "PL" :rate "23"}
               {:name "Portugal" :code "PT" :rate "23"}
               {:name "Romania" :code "RO" :rate "24"}
               {:name "Slovenia" :code "SI" :rate "22"}
               {:name "Slovakia" :code "SK" :rate "20"}
               {:name "Finland" :code "FI" :rate "24"}
               {:name "Sweden" :code "SE" :rate "25"}
               {:name "United Kingdom" :code "UK" :rate "20"}])

(defn country-codes
  []
  (map :code eu-rates))

(defn country-name
  [code]
  (let [name (:name (first (filter (fn [map]
                                     (= code (:code map))) eu-rates)))]
    (if (nil? name) "Invalid EU country code." name)))

(defn extract-rate
  [code]
  (:rate (first (filter (fn [map]
                          (= code (:code map))) eu-rates))))

(defn vat-rate
  [code]
  (if (some #{(str code)} (country-codes))
    (extract-rate code)
    "No EU VAT to charge."))

(defroutes app-routes
  (GET "/" []
       {:status 200
        :body {:desc (str "This API gives you the VAT rate to use for a given EU ISO country code.")
               :usage (str "curl -X GET -d code=fr http://vatmess.herokuapp.com/vat_rate")}})

  (GET "/vat_rate" request
       (let [code (clojure.string/upper-case (or (get-in request [:params :code])
                                                 (get-in request [:body :code])))]

         {:status 200
          :body {:code code
                 :country (country-name code)
                 :vat_rate (vat-rate code)}}))

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
