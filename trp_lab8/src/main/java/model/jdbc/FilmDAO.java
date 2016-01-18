package model.jdbc;

import entities.Film;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import model.ModelException;

@Stateless(name = "filmDAO")
public class FilmDAO extends AbstractDAO<Film> {

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<Film> filmsForDirector(Number directorId) {
        return em.createQuery("select f from Film where f.idDirector = :id")
                .setParameter("id", directorId.intValue())
                .getResultList();
    }

    @Override
    public void remove(Number id) throws ModelException {
        Film f = find(id);
        if (f != null) {
            em.remove(f);
        }
    }

    @Override
    public void update(Film entity) throws ModelException {
        em.merge(entity);
    }

    @Override
    public void create(Film entity) throws ModelException {
        em.persist(entity);
    }

    @Override
    public Film find(Number id) throws ModelException {
        if (id == null) {
            return null;
        }
        return em.find(Film.class, id.longValue());
    }

    @Override
    public Collection<Film> findByParam(Object... params) throws ModelException {
        if (params[0] != null) {
            String s = (String) params[0];
            return em.createQuery("select f from Film f where lower(f.title) like :param")
                    .setParameter("param", "%" + s.toLowerCase() + "%")
                    .getResultList();
        }
        return new ArrayList<Film>();
    }

    @Override
    public Collection<Film> findAll() {
        return em.createQuery("select f from Film f").getResultList();
    }

    @Override
    public Film refresh(Film entity) throws ModelException {
        em.refresh(entity);
        return entity;
    }

    @Override
    public void removeAll() throws ModelException {
        em.createQuery("delete from Film f").executeUpdate();
    }

}
