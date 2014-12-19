(ns vat.core.handler
  (:require [compojure.core :refer :all]
            [compojure.handler :as handler]
            [ring.middleware.json :as middleware]
            [compojure.route :as route]))

(defroutes app-routes
  (GET "/" []
       {:status 200
        :body {:desc (str "This API gives you the tax_rate you should use between two countries.")
               :usage (str "Use a POST request with from and to params containing countries ISO codes.")}})

  (POST "/" request
    (let [from (or (get-in request [:params :from])
                   (get-in request [:body :from]))
          to (or (get-in request [:params :to])
                 (get-in request [:body :to]))]
      {:status 200
       :body {:from from
              :to to
              :desc (str "From: " from " To: " to)}}))

  (route/not-found "Not Found"))

(def app
  (-> (handler/site app-routes)
      (middleware/wrap-json-body {:keywords? true})
      middleware/wrap-json-response))
