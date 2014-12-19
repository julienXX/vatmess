(ns vat.core.handler-test
  (:require [clojure.test :refer :all]
            [ring.mock.request :as mock]
            [vat.core.handler :refer :all]))

(deftest test-app
  (testing "index"
    (let [response (app (mock/request :get "/"))]
      (is (= (:status response) 200))
      (is (= (:body response) "{\"desc\":\"This API gives you the VAT rate to use for a given EU ISO country code.\",\"usage\":\"curl -X GET http://vatmess.herokuapp.com/vat_rates/fr\"}"))))

  (testing "vat_rates"
    (let [response (app (mock/request :get "/vat_rates"))]
      (is (= (:status response) 200))
      (is (= (:body response) "{\"vat_rates\":[{\"name\":\"Belgium\",\"rate\":\"21\",\"code\":\"BE\"},{\"name\":\"Bulgaria\",\"rate\":\"20\",\"code\":\"BG\"},{\"name\":\"Czech Republic\",\"rate\":\"21\",\"code\":\"CZ\"},{\"name\":\"Denmark\",\"rate\":\"25\",\"code\":\"DK\"},{\"name\":\"Germany\",\"rate\":\"19\",\"code\":\"DE\"},{\"name\":\"Estonia\",\"rate\":\"20\",\"code\":\"EE\"},{\"name\":\"Greece\",\"rate\":\"23\",\"code\":\"EL\"},{\"name\":\"Spain\",\"rate\":\"21\",\"code\":\"ES\"},{\"name\":\"France\",\"rate\":\"20\",\"code\":\"FR\"},{\"name\":\"Croatia\",\"rate\":\"25\",\"code\":\"HR\"},{\"name\":\"Ireland\",\"rate\":\"23\",\"code\":\"IE\"},{\"name\":\"Italy\",\"rate\":\"22\",\"code\":\"IT\"},{\"name\":\"Cyprus\",\"rate\":\"19\",\"code\":\"CY\"},{\"name\":\"Latvia\",\"rate\":\"21\",\"code\":\"LV\"},{\"name\":\"Lithuania\",\"rate\":\"21\",\"code\":\"LT\"},{\"name\":\"Luxembourg\",\"rate\":\"15\",\"code\":\"LU\"},{\"name\":\"Hungary\",\"rate\":\"27\",\"code\":\"HU\"},{\"name\":\"Malta\",\"rate\":\"18\",\"code\":\"MT\"},{\"name\":\"Netherlands\",\"rate\":\"21\",\"code\":\"NL\"},{\"name\":\"Austria\",\"rate\":\"20\",\"code\":\"AT\"},{\"name\":\"Poland\",\"rate\":\"23\",\"code\":\"PL\"},{\"name\":\"Portugal\",\"rate\":\"23\",\"code\":\"PT\"},{\"name\":\"Romania\",\"rate\":\"24\",\"code\":\"RO\"},{\"name\":\"Slovenia\",\"rate\":\"22\",\"code\":\"SI\"},{\"name\":\"Slovakia\",\"rate\":\"20\",\"code\":\"SK\"},{\"name\":\"Finland\",\"rate\":\"24\",\"code\":\"FI\"},{\"name\":\"Sweden\",\"rate\":\"25\",\"code\":\"SE\"},{\"name\":\"United Kingdom\",\"rate\":\"20\",\"code\":\"UK\"}]}"))))

  (testing "EU vat_rate"
    (let [response (app (mock/request :get "/vat_rates/fr"))]
      (is (= (:status response) 200))
      (is (= (:body response) "{\"code\":\"FR\",\"country\":\"France\",\"vat_rate\":\"20\"}"))))

  (testing "non EU vat_rate"
    (let [response (app (mock/request :get "/vat_rates/us"))]
      (is (= (:status response) 200))
      (is (= (:body response) "{\"code\":\"US\",\"country\":\"Invalid EU country code.\",\"vat_rate\":\"No EU VAT to charge.\"}"))))

  (testing "not-found route"
    (let [response (app (mock/request :get "/invalid"))]
      (is (= (:status response) 404)))))
