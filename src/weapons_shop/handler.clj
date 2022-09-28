(ns weapons-shop.handler
  (:require [compojure.core :as w]
            [compojure.route :as route]
            [ring.middleware.defaults :refer [wrap-defaults api-defaults]]
            [ring.middleware.json :refer [wrap-json-response wrap-json-body]]
            [ring.util.response :as response]
            [schema.utils :as s-utils]

            [weapons-shop.schema :refer [coerce-weapon-input]]))

;; (defmacro with-coerced [[name coerce-result] & body]
;;   (let [error (gensym)]
;;     `(let [~name ~coerce-result]
;;        (if-let [~error (s-utils/error-val ~name)]
;;          (response/bad-request {:message "Invalid input JSON" :error ~error})
;;          (do ~@body)))))

(defn- bad-request [body]
  (-> (response/response body)
      (response/status 400)))

(w/defroutes app-routes
  (w/GET "/" [] (response/response {:hello "World"}))

  (w/POST "/weapon" {:keys [:body]}
    (let [weapon (coerce-weapon-input body)]
      (if-let [error (s-utils/error-val weapon)]
        (bad-request {:message "Invalid input JSON" :error error})
        (response/response weapon))))

  (route/not-found "Not Found"))

(def app
  (-> app-routes
      wrap-json-response
      (wrap-json-body {:keywords? true})
      (wrap-defaults api-defaults)))
