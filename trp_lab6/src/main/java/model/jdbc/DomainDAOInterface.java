package model.jdbc;

import entities.Film;
import java.util.Collection;
import javax.ejb.Remote;
import model.ModelException;

/**
 * Интерфейс, содержащий все методы DAO предметной области
 *
 * @param <T> класс-сущность
 */
@Remote
public interface DomainDAOInterface<T extends Entity> {

    void create(T entity) throws ModelException;

    T find(Number id) throws ModelException;

    Collection<T> findByParam(Object... params) throws ModelException;

    Collection<T> findAll();

    T refresh(T entity) throws ModelException;

    void remove(Number id) throws ModelException;

    void removeAll() throws ModelException;

    void update(T entity) throws ModelException;

    Collection<Film> filmsForDirector(Number id);

}
