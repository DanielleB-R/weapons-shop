-- :name create-weapon-table
-- :command :execute
CREATE TABLE IF NOT EXISTS weapon (
    id serial PRIMARY KEY,
    name text NOT NULL,
    damage integer NOT NULL,
    price integer NOT NULL,
    durability integer
);
