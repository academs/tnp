package model.jdbc;

import entities.Director;
import entities.Film;
import java.util.ArrayList;
import java.util.Collection;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import model.ModelException;

@Stateless(name = "directorDAO")
public class DirectorDAO extends AbstractDAO<Director> {

    @PersistenceContext
    private EntityManager em;

    @EJB
    private FilmDAO filmDAO;

    @Override
    public void remove(Number id) throws ModelException {
        em.remove(find(id));
    }

    @Override
    public Collection<Film> filmsForDirector(Number id) {
        return filmDAO.filmsForDirector(id);
    }

    @Override
    public void create(Director entity) throws ModelException {
        em.persist(entity);
    }

    @Override
    public Director find(Number id) throws ModelException {
        if (id == null) {
            return null;
        }
        return em.find(Director.class, id.intValue());
    }

    @Override
    public Collection<Director> findByParam(Object... params) throws ModelException {
        if (params[0] != null) {
            String s = (String) params[0];
            return em.createQuery("select d from Director d where lower(d.name) like :param")
                    .setParameter("param", "%" + s.toLowerCase() + "%")
                    .getResultList();
        }
        return new ArrayList<>();
    }

    @Override
    public Collection<Director> findAll() {
        return em.createQuery("select d from Director d").getResultList();
    }

    @Override
    public Director refresh(Director entity) throws ModelException {
        em.refresh(entity);
        return entity;
    }

    @Override
    public void removeAll() throws ModelException {
        em.createQuery("delete from Director d").executeUpdate();
    }

    @Override
    public void update(Director entity) throws ModelException {
        em.merge(entity);
    }

}
