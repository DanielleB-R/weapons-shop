(ns weapons-shop.handler
  (:require [compojure.core :as w]
            [compojure.route :as route]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            [ring.middleware.json :refer [wrap-json-response wrap-json-body]]
            [ring.util.response :as response]))

(w/defroutes app-routes
  (w/GET "/" [] (response/response {:hello "World"}))
  (route/not-found "Not Found"))

(def app
  (-> app-routes
      wrap-json-response
      wrap-json-body
      (wrap-defaults site-defaults)))
