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

INSERT INTO users (name, email, password) VALUES ('root', 'root@local', 'root');

CREATE TABLE IF NOT EXISTS city
(
    id SERIAL PRIMARY KEY,
    name TEXT
);

INSERT INTO city (name) VALUES ('Москва'), ('Санкт-Петербург'), ('Екатеринбург');

ALTER TABLE candidate ADD COLUMN city_id INT REFERENCES city(id);