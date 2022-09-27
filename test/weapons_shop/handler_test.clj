(ns weapons-shop.handler-test
  (:require [clojure.test :refer :all]
            [ring.mock.request :as mock]
            [cheshire.core :as json]
            [weapons-shop.handler :refer [app]]))

(deftest test-app
  (testing "main route"
    (let [response (app (mock/request :get "/"))
          body (json/parse-string (:body response) true)]
      (is (= (:status response) 200))
      (is (= (get-in response [:headers "Content-Type"]) "application/json; charset=utf-8"))
      (is (= (:hello body) "World"))))

  (testing "not-found route"
    (let [response (app (mock/request :get "/invalid"))]
      (is (= (:status response) 404)))))
