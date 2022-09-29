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

(deftest test-weapon-api
  ;; (testing "GET all empty"
  ;;   (let [response (app (mock/request :get "/weapon"))
  ;;         body (json/parse-string (:body response) true)]
  ;;     (is (= (:status response) 200))
  ;;     (is (= (:weapons body) []))))

  (def weapon-id (atom nil))

  (testing "POST a new weapon"
    (let [response (app (-> (mock/request :post "/weapon")
                            (mock/json-body {:name "Test Sword" :damage 5 :cost 100})))
          body (json/parse-string (:body response) true)]
      (is (= (:status response) 200))
      (is (= (:name body) "Test Sword"))
      (is (:id body))
      (reset! weapon-id (:id body))))

  (def weapon-url (str "/weapon/" @weapon-id))

  (testing "GET that weapon back"
    (let [response (app (mock/request :get weapon-url))
          body (json/parse-string (:body response) true)]
      (is (= (:status response) 200))
      (is (= (:id body) @weapon-id))
      (is (= (:name body) "Test Sword"))))

  (testing "PUT a change"
    (let [response (app (-> (mock/request :put weapon-url)
                            (mock/json-body {:name "Super Test Sword" :damage 10 :cost 400})))
          body (json/parse-string (:body response) true)]
      (is (= (:status response) 200))
      (is (= (:id body) @weapon-id))
      (is (= (:name body) "Super Test Sword"))))

  (testing "GET that change back"
    (let [response (app (mock/request :get weapon-url))
          body (json/parse-string (:body response) true)]
      (is (= (:status response) 200))
      (is (= (:id body) @weapon-id))
      (is (= (:name body) "Super Test Sword"))))

  (testing "DELETE the weapon"
    (let [response (app (mock/request :delete weapon-url))]
      (is (= (:status response) 200))))

  (testing "GET the weapon and it's gone"
    (let [response (app (mock/request :get weapon-url))]
      (is (= (:status response) 404)))))
