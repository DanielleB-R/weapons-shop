-- :name- insert-weapon-query
-- :command :returning-execute
-- :result :one
INSERT INTO weapon (name, damage, price, durability)
    VALUES (:name, :damage, :price, :durability)
RETURNING
    *;

-- :name- get-weapon-query :? :1
SELECT
    *
FROM
    weapon
WHERE
    id = :id;

-- :name- get-all-weapons-query :? :*
SELECT
    *
FROM
    weapon;

-- :name- update-weapon-query
-- :command :returning-execute
-- :result :one
UPDATE
    weapon
SET
    name = :name,
    damage = :damage,
    price = :price,
    durability = :durability
WHERE
    id = :id
RETURNING
    *;

-- :name- delete-weapon-query :! :n
DELETE FROM weapon
WHERE id = :id;
