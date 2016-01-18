package model.jdbc;

import entities.Director;
import entities.Film;
import entities.Genre;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import model.ModelException;

public class FilmDAO extends AbstractDAO<Film> {

    public List<Film> filmsForDirector(Number directorId) {
        try {
            List<Film> entities = new ArrayList<>();
            try (Connection connection = DriverManager.getConnection(url, user, password)) {
                try (PreparedStatement find = connection
                        .prepareStatement("SELECT * FROM film WHERE director_id=?")) {
                    find.setInt(1, directorId.intValue());
                    try (ResultSet res = find.executeQuery()) {
                        while (res.next()) {
                            entities.add(resultSetToEntity(res));
                        }
                    }
                }
            }
            return entities;
        } catch (Exception ex) {
            ex.printStackTrace();
            //throw new RuntimeException("Не удалось получить список фильмов для режиссёра с id=" + directorId);
            return new ArrayList<>();
        }
    }

    @Override
    public Collection<Film> loadFromDatabase(Object... params) {
        try {
            List<Film> entities = new ArrayList<>();
            try (Connection connection = DriverManager.getConnection(url, user, password)) {
                try (PreparedStatement find = connection
                        .prepareStatement("SELECT * FROM film WHERE lower(title) like '%'||lower(?)||'%'")) {
                    find.setString(1, (String) params[0]);
                    try (ResultSet res = find.executeQuery()) {
                        while (res.next()) {
                            entities.add(resultSetToEntity(res));
                        }
                    }
                }
            }
            return entities;
        } catch (Exception ex) {
            ex.printStackTrace();
            //throw new RuntimeException("Не удалось получить список фильмов для режиссёра с id=" + directorId);
            return new ArrayList<>();
        }
    }

    @Override
    protected Collection<Film> loadAllFromDatabase() {
        try {
            List<Film> entities = new ArrayList<>();
            try (Connection connection = DriverManager.getConnection(url, user, password)) {
                try (PreparedStatement find = connection
                        .prepareStatement("SELECT * FROM film")) {
                    try (ResultSet res = find.executeQuery()) {
                        while (res.next()) {
                            entities.add(resultSetToEntity(res));
                        }
                    }
                }
            }
            return entities;
        } catch (Exception ex) {
            ex.printStackTrace();
            //throw new ModelException("Не удалось получить список фильмов");
            return new ArrayList<>();
        }
    }

    @Override
    protected Film loadFromDatabase(Number id) {
        try {
            Film entity = null;
            try (Connection connection = DriverManager.getConnection(url, user, password)) {
                try (PreparedStatement find = connection
                        .prepareStatement("SELECT * FROM film WHERE id=?")) {
                    find.setLong(1, id.longValue());
                    try (ResultSet res = find.executeQuery()) {
                        res.next();
                        entity = resultSetToEntity(res);
                    }
                }
            }
            return entity;
        } catch (Exception ex) {
            ex.printStackTrace();
            //throw new ModelException("Не удалось выполнить поиск записи с id=" + id);
            return null;
        }
    }

    @Override
    protected void insertIntoDatabase(Film entity) throws ModelException {
        try {
            try (Connection connection = DriverManager.getConnection(url, user, password)) {
                try (PreparedStatement insertStatement = connection
                        .prepareStatement("INSERT INTO film(title, genre, duration, year, director_id) "
                                + " VALUES(?, ?, ?, ?, ?)", new String[]{"id"})) {
                    insertStatement.setString(1, entity.getTitle());
                    if (entity.getGenre() != null) {
                        insertStatement.setString(2, entity.getGenre().name());
                    } else {
                        insertStatement.setString(2, null);
                    }
                    insertStatement.setObject(3, entity.getDuration());
                    insertStatement.setObject(4, entity.getYear());
                    insertStatement.setInt(5, entity.getIdDirector().getIdDirector());
                    insertStatement.executeUpdate();
                    ResultSet generatedKeys = insertStatement.getGeneratedKeys();
                    if (generatedKeys != null && generatedKeys.next()) {
                        Long id = generatedKeys.getLong(1);
                        entity.setIdFilm(id);
                    }
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new ModelException("Не удалось добавить запись");
        }
    }

    @Override
    protected void updateDatabase(Film entity) throws ModelException {
        try {
            try (Connection connection = DriverManager.getConnection(url, user, password)) {
                try (PreparedStatement updateStatement = connection
                        .prepareStatement("UPDATE film SET title=?, genre=?, duration=?, year=?, director_id=?"
                                + " WHERE id=?")) {
                    updateStatement.setString(1, entity.getTitle());
                    if (entity.getGenre() != null) {
                        updateStatement.setString(2, entity.getGenre().name());
                    } else {
                        updateStatement.setString(2, null);
                    }
                    updateStatement.setObject(3, entity.getDuration());
                    updateStatement.setObject(4, entity.getYear());
                    updateStatement.setInt(5, entity.getIdDirector().getIdDirector());
                    updateStatement.setLong(6, entity.getId());
                    updateStatement.executeUpdate();
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new ModelException("Не удалось выполнить обновление записи с id=" + entity.getId());
        }
    }

    @Override
    protected void removeFromDatabase(Number id) throws ModelException {
        try {
            try (Connection connection = DriverManager.getConnection(url, user, password)) {
                try (PreparedStatement deleteStatement = connection
                        .prepareStatement("DELETE FROM film WHERE id=?")) {
                    deleteStatement.setLong(1, id.longValue());
                    deleteStatement.executeUpdate();
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new ModelException("Не удалось удалить запись с id=" + id);
        }
    }

    @Override
    protected void removeAllFromDatabase() throws ModelException {
        try {
            try (Connection connection = DriverManager.getConnection(url, user, password)) {
                try (PreparedStatement deleteStatement = connection
                        .prepareStatement("DELETE FROM film")) {
                    deleteStatement.executeUpdate();
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new ModelException("Не удалось очистить таблицу");
        }
    }

    private Film resultSetToEntity(final ResultSet res) throws SQLException, ModelException {
        Film f = new Film();
        f.setIdFilm(res.getLong("id"));
        f.setTitle(res.getString("title"));
        String genre = res.getString("genre");
        if (genre != null) {
            f.setGenre(Genre.valueOf(genre));
        }
        Object duration = res.getObject("duration");
        if (duration != null && duration instanceof Number) {
            f.setDuration(((Number) duration).shortValue());
        }
        Object year = res.getObject("year");
        if (year != null && year instanceof Number) {
            f.setYear(((Number) year).shortValue());
        }
        Director d = DomainDAOManager.getDirectorDAO().find(res.getInt("director_id"));
        f.setIdDirector(d);
        return f;
    }

    @Override
    public void remove(Number id) throws ModelException {
        Film oldValue = find(id);
        super.remove(id);
        if (oldValue != null && oldValue.getIdDirector() != null) {
            DomainDAOManager.getDirectorDAO().refresh(oldValue.getIdDirector());
        }
    }

    @Override
    public void update(Film entity) throws ModelException {
        Film oldValue = find(entity.getId());
        super.update(entity);
        if (oldValue != null && oldValue.getIdDirector() != null) {
            DomainDAOManager.getDirectorDAO().refresh(oldValue.getIdDirector());
        }
    }

    @Override
    public void create(Film entity) throws ModelException {
        super.create(entity);
        DomainDAOManager.getDirectorDAO().refresh(entity.getIdDirector());
    }

}
