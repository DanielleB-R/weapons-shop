(ns weapons-shop.db
  (:require [clojure.java.jdbc :as j]
            [weapons-shop.config :refer [db-config-dev]]
            [hugsql.core :refer [def-db-fns]]))

(def-db-fns "weapons_shop/sql/initialize.sql")
(def-db-fns "weapons_shop/sql/weapon.sql")

;; HACK
(create-weapon-table db-config-dev)

(def fake-db (atom {}))
(def next-id (atom 0))

(defn get-all-weapons []
  (get-all-weapons-query db-config-dev))

(defn get-weapon-by-id [id]
  (get-weapon-query db-config-dev {:id (Integer/parseInt id)}))

(defn new-weapon [weapon]
  (let [id (swap! next-id inc)
        weapon-with-id (assoc weapon :id id)
        db-weapon (insert-weapon-query db-config-dev (assoc weapon :price (:cost weapon) :durability (:durability weapon)))]
    (swap! fake-db assoc (str id) weapon-with-id)
    db-weapon))

(defn update-weapon [id weapon]
  (update-weapon-query db-config-dev (assoc weapon :price (:cost weapon) :durability (:durability weapon) :id (Integer/parseInt id))))

(defn delete-weapon [id]
  (let [count (delete-weapon-query db-config-dev {:id (Integer/parseInt id)})]
    (if (> count 0) true nil)))

(defn check-database []
  (j/query db-config-dev ["select 1;"]))
