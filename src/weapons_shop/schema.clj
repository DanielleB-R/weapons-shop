(ns weapons-shop.schema
  (:require [schema.core :as s]
            [schema.coerce :refer [coercer json-coercion-matcher]]))

(s/defschema WeaponInput
  {:name s/Str
   :damage s/Num
   :cost s/Num
   (s/optional-key :durability) s/Num})

(def coerce-weapon-input
  (coercer WeaponInput json-coercion-matcher))
