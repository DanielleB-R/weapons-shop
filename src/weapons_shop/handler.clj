(ns weapons-shop.handler
  (:require [compojure.core :as w]
            [compojure.route :as route]
            [ring.middleware.defaults :refer [wrap-defaults api-defaults]]
            [ring.middleware.json :refer [wrap-json-response wrap-json-body]]
            [ring.util.response :as response]
            [schema.utils :as s-utils]

            [weapons-shop.schema :refer [coerce-weapon-input coerce-weapon]]
            [weapons-shop.db :as db]))

(def not-found-response (response/not-found "Not Found"))

(defmacro when-let-or-404
  "Mirrors the `when-let` form but returns a 404 response when the binding fails"
  [binding-form & body]
  `(if-let ~binding-form
     (do ~@body)
     not-found-response))

(defn- bad-request
  "Returns a Ring request with a 400 status"
  [body]
  (-> (response/response body)
      (response/status 400)))


(defn- parse-integer
  "Returns the integer value of input if valid or nil if invalid"
  [input]
  (try
    (Integer/parseInt input)
    (catch NumberFormatException _ nil)))

(w/defroutes app-routes
  (w/GET "/" [] (response/response {:hello "World"}))

  (w/GET "/health" []
    (db/check-database)
    (response/response {:ok true}))

  (w/GET "/weapon" [] (response/response {:weapons (db/get-all-weapons)}))

  (w/GET "/weapon/:id" [id]
    (when-let-or-404 [numeric-id (parse-integer id)]
                     (when-let-or-404 [weapon (db/get-weapon-by-id numeric-id)]
                                      (response/response weapon))))

  (w/POST "/weapon" {:keys [:body]}
    (let [weapon (coerce-weapon-input body)]
      (if-let [error (s-utils/error-val weapon)]
        (bad-request {:message "Invalid input JSON" :error error})
        (response/response (db/new-weapon weapon)))))

  (w/PUT "/weapon/:id" {:keys [:body :params]}
    (let [id (:id params)
          weapon (coerce-weapon body)]
      (when-let-or-404 [numeric-id (parse-integer id)]
                       (if (s-utils/error-val weapon)
                         (bad-request {:message "Invalid input JSON"})
                         (when-let-or-404 [new-weapon (db/update-weapon numeric-id weapon)]
                                          (response/response new-weapon))))))

  (w/DELETE "/weapon/:id" [id]
    (when-let-or-404 [numeric-id (parse-integer id)]
                     (if (db/delete-weapon numeric-id)
                       (response/response "")
                       not-found-response)))

  (route/not-found "Not Found"))

(def app
  (-> app-routes
      wrap-json-response
      (wrap-json-body {:keywords? true})
      (wrap-defaults api-defaults)))
