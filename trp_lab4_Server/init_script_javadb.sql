-- SQL start
-- удаляем старые таблицы
DROP TABLE film;
DROP TABLE director;

CREATE TABLE director (
    id INTEGER NOT NULL 
        PRIMARY KEY GENERATED ALWAYS AS IDENTITY 
        (START WITH 1, INCREMENT BY 1),
    name VARCHAR(255) NOT NULL,
    phone BIGINT
)
;
CREATE TABLE film (
    id BIGINT NOT NULL 
        PRIMARY KEY GENERATED ALWAYS AS IDENTITY 
        (START WITH 1, INCREMENT BY 1),
    title VARCHAR(255) NOT NULL,
    genre VARCHAR(15) NOT NULL,
    "year" SMALLINT,
    duration SMALLINT,
    director_id INTEGER NOT NULL
);

ALTER TABLE film ADD FOREIGN KEY (director_id) REFERENCES director(id);

-- наполнение таблиц
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
INSERT INTO film(title, genre, duration, "year", director_id)
SELECT 'После работы', 'COMEDY', 97, 1985, id FROM director WHERE name = 'Мартин Скорсезе';