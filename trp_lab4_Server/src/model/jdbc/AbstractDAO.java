package model.jdbc;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import model.ModelException;

/**
 * Data Access Object класс, реализующий простую логику кеширования данных, 
 * реализация запросов и object relational mapping предоставляется наследникам
 * 
 * @param <T> класс-сущность
 */
public abstract class AbstractDAO<T extends Entity> implements DomainDAOInterface<T> {

    protected static final String url = "jdbc:derby://localhost:1527/films";
    protected static final String user = "postgres";
    protected static final String password = "postgres";

    private final Map<Number, T> loaded = new HashMap<>();

    @Override
    public T find(Number id) throws ModelException {
        if (loaded.containsKey(id)) {
            return loaded.get(id);
        } else {
            T res = loadFromDatabase(id);
            if (res != null) {
                loaded.put(id, res);
            }
            return res;
        }
    }

    @Override
    public Collection<T> findAll() {
        Collection<T> all = loadAllFromDatabase();
        for (T t : all) {
            if (!loaded.containsKey(t.getId())) {
                loaded.put(t.getId(), t);
            }
        }
        return Collections.unmodifiableCollection(loaded.values());
    }

    @Override
    public void create(T entity) throws ModelException {
        if (entity.getId() != null) {
            throw new IllegalArgumentException("У сущности уже есть первичный ключ!");
        }
        insertIntoDatabase(entity);
        loaded.put(entity.getId(), entity);
    }

    @Override
    public void update(T entity) throws ModelException {
        updateDatabase(entity);
        loaded.put(entity.getId(), entity);
    }

    @Override
    public void remove(Number id) throws ModelException {
        T oldValue = find(id);
        if (oldValue != null) {
            loaded.remove(id);
            removeFromDatabase(id);
        }
    }

    @Override
    public void removeAll() throws ModelException {
        removeAllFromDatabase();
        loaded.clear();
    }

    @Override
    public T refresh(T entity) throws ModelException {
        T oldValue = loaded.remove(entity.getId());
        return find(entity.getId());
    }

    protected abstract Collection<T> loadAllFromDatabase();

    protected abstract T loadFromDatabase(Number id);

    protected abstract void insertIntoDatabase(T entity) throws ModelException;

    protected abstract void updateDatabase(T entity) throws ModelException;

    protected abstract void removeFromDatabase(Number id) throws ModelException;

    protected abstract void removeAllFromDatabase() throws ModelException;
}
