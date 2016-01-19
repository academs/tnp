package model.jdbc;

import entities.Director;
import entities.Film;
import java.util.ArrayList;
import java.util.Collection;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import log.LogProducer;
import model.ModelException;

@Stateless(name = "directorDAO")
public class DirectorDAO extends AbstractDAO<Director> {

    @PersistenceContext
    private EntityManager em;

    @EJB
    private FilmDAO filmDAO;
    
    @EJB
    private LogProducer log;

    @Override
    public void remove(Number id) throws ModelException {
        Director d = find(id);
        if(d != null) {
            for(Film f : filmsForDirector(id)) {
                filmDAO.remove(f.getId());
            }
            em.remove(d);
            log.send(d);
        }
    }

    @Override
    public Collection<Film> filmsForDirector(Number id) {
        return filmDAO.filmsForDirector(id);
    }

    @Override
    public void create(Director entity) throws ModelException {
        em.persist(entity);
        em.flush(); // force set id
        log.send(entity);
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
        for(Director d : findAll()) {
            em.remove(d);
            log.send(d);
        }
    }

    @Override
    public void update(Director entity) throws ModelException {
        em.merge(entity);
        log.send(entity);
    }

}
