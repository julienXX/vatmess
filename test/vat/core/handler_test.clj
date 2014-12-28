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
      (is (= (:body response) "{\"vat_rates\":[{\"name\":\"Belgium\",\"code\":\"BE\",\"rate\":\"21\"},{\"name\":\"Bulgaria\",\"code\":\"BG\",\"rate\":\"20\"},{\"name\":\"Czech Republic\",\"code\":\"CZ\",\"rate\":\"21\"},{\"name\":\"Denmark\",\"code\":\"DK\",\"rate\":\"25\"},{\"name\":\"Germany\",\"code\":\"DE\",\"rate\":\"19\"},{\"name\":\"Estonia\",\"code\":\"EE\",\"rate\":\"20\"},{\"name\":\"Greece\",\"code\":\"EL\",\"rate\":\"23\"},{\"name\":\"Spain\",\"code\":\"ES\",\"rate\":\"21\"},{\"name\":\"France\",\"code\":\"FR\",\"rate\":\"20\"},{\"name\":\"Croatia\",\"code\":\"HR\",\"rate\":\"25\"},{\"name\":\"Ireland\",\"code\":\"IE\",\"rate\":\"23\"},{\"name\":\"Italy\",\"code\":\"IT\",\"rate\":\"22\"},{\"name\":\"Cyprus\",\"code\":\"CY\",\"rate\":\"19\"},{\"name\":\"Latvia\",\"code\":\"LV\",\"rate\":\"21\"},{\"name\":\"Lithuania\",\"code\":\"LT\",\"rate\":\"21\"},{\"name\":\"Luxembourg\",\"code\":\"LU\",\"rate\":\"15\"},{\"name\":\"Hungary\",\"code\":\"HU\",\"rate\":\"27\"},{\"name\":\"Malta\",\"code\":\"MT\",\"rate\":\"18\"},{\"name\":\"Netherlands\",\"code\":\"NL\",\"rate\":\"21\"},{\"name\":\"Austria\",\"code\":\"AT\",\"rate\":\"20\"},{\"name\":\"Poland\",\"code\":\"PL\",\"rate\":\"23\"},{\"name\":\"Portugal\",\"code\":\"PT\",\"rate\":\"23\"},{\"name\":\"Romania\",\"code\":\"RO\",\"rate\":\"24\"},{\"name\":\"Slovenia\",\"code\":\"SI\",\"rate\":\"22\"},{\"name\":\"Slovakia\",\"code\":\"SK\",\"rate\":\"20\"},{\"name\":\"Finland\",\"code\":\"FI\",\"rate\":\"24\"},{\"name\":\"Sweden\",\"code\":\"SE\",\"rate\":\"25\"},{\"name\":\"United Kingdom\",\"code\":\"UK\",\"rate\":\"20\"}]}"))))

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
