(ns vat.core.handler
  (:require [compojure.core :refer :all]
            [compojure.handler :as handler]
            [ring.middleware.json :as middleware]
            [compojure.route :as route]))

(def eu-rates [{:name "Belgium" :code "BE" :rate 21}
               {:name "Bulgaria" :code "BG" :rate 20}
               {:name "Czech Republic" :code "CZ" :rate 21}
               {:name "Denmark" :code "DK" :rate 25}
               {:name "Germany" :code "DE" :rate 19}
               {:name "Estonia" :code "EE" :rate 20}
               {:name "Greece" :code "EL" :rate 23}
               {:name "Spain" :code "ES" :rate 21}
               {:name "France" :code "FR" :rate 20}
               {:name "Croatia" :code "HR" :rate 25}
               {:name "Ireland" :code "IE" :rate 23}
               {:name "Italy" :code "IT" :rate 22}
               {:name "Cyprus" :code "CY" :rate 19}
               {:name "Latvia" :code "LV" :rate 21}
               {:name "Lithuania" :code "LT" :rate 21}
               {:name "Luxembourg" :code "LU" :rate 15}
               {:name "Hungary" :code "HU" :rate 27}
               {:name "Malta" :code "MT" :rate 18}
               {:name "Netherlands" :code "NL" :rate 21}
               {:name "Austria" :code "AT" :rate 20}
               {:name "Poland" :code "PL" :rate 23}
               {:name "Portugal" :code "PT" :rate 23}
               {:name "Romania" :code "RO" :rate 24}
               {:name "Slovenia" :code "SI" :rate 22}
               {:name "Slovakia" :code "SK" :rate 20}
               {:name "Finland" :code "FI" :rate 24}
               {:name "Sweden" :code "SE" :rate 25}
               {:name "United Kingdom" :code "UK" :rate 20}])

(defroutes app-routes
  (GET "/" []
       {:status 200
        :body {:desc (str "This API gives you the tax_rate you should use between two countries.")
               :usage (str "Use a POST request with from and to params containing countries ISO codes.")}})

  (POST "/" request
        (let [me (or (get-in request [:params :me])
                     (get-in request [:body :me]))
              client (or (get-in request [:params :client])
                         (get-in request [:body :client]))]
          {:status 200
           :body {:me me
                  :client client
                  :desc (str "me: " me " client: " client)}}))

  (route/not-found "Not Found"))

(def app
  (-> (handler/site app-routes)
      (middleware/wrap-json-body {:keywords? true})
      middleware/wrap-json-response))
