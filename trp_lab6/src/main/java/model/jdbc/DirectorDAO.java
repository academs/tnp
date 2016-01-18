package model.jdbc;

import entities.Director;
import entities.Film;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import model.ModelException;

public class DirectorDAO extends AbstractDAO<Director> {

    @Override
    protected Collection<Director> loadAllFromDatabase() {
        try {
            List<Director> entities = new ArrayList<>();
            try (Connection connection = DriverManager.getConnection(url, user, password)) {
                try (PreparedStatement find = connection
                        .prepareStatement("SELECT * FROM director")) {
                    try (ResultSet res = find.executeQuery()) {
                        while (res.next()) {
                            entities.add(resultSetToEntity(res));
                        }
                    }
                }
            }
            return entities;
        } catch (SQLException ex) {
            ex.printStackTrace();
            // throw new ModelException("Не удалось получить список режиссёров");
            return new ArrayList<>();
        }
    }

    @Override
    protected Collection<Director> loadFromDatabase(Object... params) {
        try {
            List<Director> entities = new ArrayList<>();
            try (Connection connection = DriverManager.getConnection(url, user, password)) {
                try (PreparedStatement findStatement = connection
                        .prepareStatement("SELECT * FROM director WHERE lower(name) "
                                + " like '%' || lower(?) || '%'")) {
                    findStatement.setString(1, (String) params[0]);
                    try (ResultSet res = findStatement.executeQuery()) {
                        while (res.next()) {
                            entities.add(resultSetToEntity(res));
                        }
                    }
                }
            }
            return entities;
        } catch (SQLException ex) {
            ex.printStackTrace();
            //throw new ModelException("Не удалось выполнить поиск записи с id=" + id);
            return null;
        }
    }

    @Override
    protected Director loadFromDatabase(Number id) {
        try {
            Director entity = null;
            try (Connection connection = DriverManager.getConnection(url, user, password)) {
                try (PreparedStatement findStatement = connection
                        .prepareStatement("SELECT * FROM director WHERE id=?")) {
                    findStatement.setInt(1, id.intValue());
                    try (ResultSet res = findStatement.executeQuery()) {
                        if (res.next()) {
                            entity = resultSetToEntity(res);
                        } else {
                            return null;
                        }
                    }
                }
            }
            return entity;
        } catch (SQLException ex) {
            ex.printStackTrace();
            //throw new ModelException("Не удалось выполнить поиск записи с id=" + id);
            return null;
        }
    }

    @Override
    protected void insertIntoDatabase(Director entity) throws ModelException {
        try {
            try (Connection connection = DriverManager.getConnection(url, user, password)) {
                try (PreparedStatement insertStatement = connection
                        .prepareStatement("INSERT INTO director(name, phone) "
                                + " VALUES(?, ?)", new String[]{"id"})) {
                    insertStatement.setString(1, entity.getName());
                    insertStatement.setObject(2, entity.getPhone());
                    insertStatement.executeUpdate();
                    ResultSet generatedKeys = insertStatement.getGeneratedKeys();
                    if (generatedKeys != null && generatedKeys.next()) {
                        Integer id = generatedKeys.getInt(1);
                        entity.setIdDirector(id);
                    }
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new ModelException("Не удалось добавить запись");
        }
    }

    @Override
    protected void updateDatabase(Director entity) throws ModelException {
        try {
            try (Connection connection = DriverManager.getConnection(url, user, password)) {
                try (PreparedStatement updateStatement = connection
                        .prepareStatement("UPDATE director SET name=?, phone=? WHERE id = ?")) {
                    updateStatement.setString(1, entity.getName());
                    updateStatement.setObject(2, entity.getPhone());
                    updateStatement.setInt(3, entity.getId().intValue());
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
                        .prepareStatement("DELETE FROM director WHERE id=?")) {
                    deleteStatement.setInt(1, id.intValue());
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
                        .prepareStatement("DELETE FROM director")) {
                    deleteStatement.executeUpdate();
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new ModelException("Не удалось очистить таблицу");
        }
    }

    private Director resultSetToEntity(ResultSet res) throws SQLException {
        Director d = new Director() {
            private boolean loaded = false;

            @Override
            public Collection<Film> getFilmCollection() {
                if (!loaded) {
                    DomainDAOInterface films = DomainDAOManager.getFilmDAO();
                    super.setFilmCollection(films.filmsForDirector(getId()));
                    loaded = true;
                }
                return super.getFilmCollection();
            }

        };
        d.setIdDirector(res.getInt("id"));
        d.setName(res.getString("name"));
        d.setPhone((Long) res.getObject("phone"));
        return d;
    }

    @Override
    public void remove(Number id) throws ModelException {
        DomainDAOInterface films = DomainDAOManager.getFilmDAO();
        for (Film film : filmsForDirector(id)) {
            film.setIdDirector(null);
            films.remove(film.getId());
        }
        super.remove(id);
    }

    @Override
    public Collection<Film> filmsForDirector(Number id) {
        return DomainDAOManager.getFilmDAO().filmsForDirector(id);
    }

}
