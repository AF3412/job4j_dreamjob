CREATE TABLE IF NOT EXISTS post (
   id SERIAL PRIMARY KEY,
   name TEXT,
   description TEXT,
   created TEXT
);

CREATE TABLE IF NOT EXISTS candidate (
   id SERIAL PRIMARY KEY,
   name TEXT
);

CREATE TABLE IF NOT EXISTS photo (
    id serial PRIMARY KEY
);

ALTER TABLE candidate ADD COLUMN photo_id INT REFERENCES photo(id);

CREATE TABLE IF NOT EXISTS users (
    id SERIAL PRIMARY KEY,
    name TEXT,
    email TEXT,
    password TEXT
);