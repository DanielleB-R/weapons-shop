(ns weapons-shop.db)

(def fake-db (atom {}))
(def next-id (atom 0))

(defn get-all-weapons []
  (vec (vals @fake-db)))

(defn get-weapon-by-id [id]
  (@fake-db id))

(defn new-weapon [weapon]
  (let [id (swap! next-id inc)
        weapon-with-id (assoc weapon :id id)]
    (swap! fake-db assoc (str id) weapon-with-id)
    weapon-with-id))