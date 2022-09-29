(ns weapons-shop.handler
  (:require [compojure.core :as w]
            [compojure.route :as route]
            [ring.middleware.defaults :refer [wrap-defaults api-defaults]]
            [ring.middleware.json :refer [wrap-json-response wrap-json-body]]
            [ring.util.response :as response]
            [schema.utils :as s-utils]

            [weapons-shop.schema :refer [coerce-weapon-input]]
            [weapons-shop.db :as db]))

(defmacro when-let-or-404 [binding-form & body]
  `(if-let ~binding-form
     (do ~@body)
     (response/not-found "")))

(defn- bad-request [body]
  (-> (response/response body)
      (response/status 400)))

(w/defroutes app-routes
  (w/GET "/" [] (response/response {:hello "World"}))

  (w/GET "/health" []
    (db/check-database)
    (response/response {:ok true}))

  (w/GET "/weapon" [] (response/response {:weapons (db/get-all-weapons)}))

  (w/GET "/weapon/:id" [id]
    (when-let-or-404 [weapon (db/get-weapon-by-id id)]
                     (response/response weapon)))

  (w/POST "/weapon" {:keys [:body]}
    (let [weapon (coerce-weapon-input body)]
      (if-let [error (s-utils/error-val weapon)]
        (bad-request {:message "Invalid input JSON" :error error})
        (response/response (db/new-weapon weapon)))))

  (w/PUT "/weapon/:id" {:keys [:body :params]}
    (let [id (:id params)
          weapon (coerce-weapon-input body)]
      (if (s-utils/error-val weapon)
        (bad-request {:message "Invalid input JSON"})
        (when-let-or-404 [new-weapon (db/update-weapon id (assoc weapon :id (Integer/parseInt id)))]
                         (response/response new-weapon)))))

  (w/DELETE "/weapon/:id" [id]
    (if (db/delete-weapon id)
      (response/response "")
      (response/not-found "")))

  (route/not-found "Not Found"))

(def app
  (-> app-routes
      wrap-json-response
      (wrap-json-body {:keywords? true})
      (wrap-defaults api-defaults)))
