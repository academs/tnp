--выполнить один раз для создании базы данных
--CREATE DATABASE films ENCODING 'UTF-8' TEMPLATE template0;

-- SQL start
-- удаляем старые таблицы
DROP TABLE IF EXISTS film;
DROP TABLE IF EXISTS director;

CREATE TABLE director (
    id     serial NOT NULL,
    name   varchar(255) NOT NULL,
    phone  bigint,
    PRIMARY KEY(id)
);
CREATE TABLE film (
    id     serial NOT NULL,
    title  varchar(255) NOT NULL,
    genre  varchar(10) NOT NULL,
    year smallint,
    duration smallint,
    director_id integer NOT NULL,
    PRIMARY KEY(id),
    FOREIGN KEY (director_id) REFERENCES director(id)
);

-- наполнение таблицы
INSERT INTO director(name) VALUES('Квентин Тарантино');
INSERT INTO director(name) VALUES('Алехандро Гонсалес Иньярриту');
INSERT INTO director(name) VALUES('Джеймс Кэмерон');
INSERT INTO director(name) VALUES('Мартин Скорсезе');
INSERT INTO director(name) VALUES('Никита Михалков');

INSERT INTO film(title, genre, duration, director_id)
SELECT 'Доказательство смерти', 'ACTION', 114, id FROM director WHERE name = 'Квентин Тарантино';
INSERT INTO film(title, genre, duration, director_id)
SELECT 'Криминальное чтиво', 'ACTION', 154, id FROM director WHERE name = 'Квентин Тарантино';
INSERT INTO film(title, genre, duration, director_id)
SELECT 'Бёрдмэн', 'DRAMA', 119, id FROM director WHERE name = 'Алехандро Гонсалес Иньярриту';
INSERT INTO film(title, genre, duration, director_id)
SELECT 'Выживший', 'DRAMA', 156, id FROM director WHERE name = 'Алехандро Гонсалес Иньярриту';
INSERT INTO film(title, genre, duration, director_id)
SELECT 'Титаник', 'DRAMA', 194, id FROM director WHERE name = 'Джеймс Кэмерон';
INSERT INTO film(title, genre, duration, year, director_id)
SELECT 'После работы', 'COMEDY', 97, 1985, id FROM director WHERE name = 'Мартин Скорсезе';