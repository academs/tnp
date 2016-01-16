package model.jdbc;

import entities.Director;
import entities.Film;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import model.Model;
import model.ModelException;
import model.ModelLoader;

/**
 * Класс для управления созданием DAO предметной области
 */
public class DomainDAOManager {

    /**
     * Общий объект для организации последовательного доступа к ресурсу
     */
    private static final Lock domainLock = new ReentrantLock();

    // динамические прокси-объекты, при каждом вызове будет захватываться блокировка
    // предметной области, что приведёт к последовательному доступу к БД
    private static final DomainDAOInterface<Director> directorDAO = (DomainDAOInterface) Proxy
            .newProxyInstance(DirectorDAO.class.getClassLoader(),
                    new Class<?>[]{DomainDAOInterface.class},
                    new SynchronizationHandler(new DirectorDAO()));
    private static final DomainDAOInterface<Film> filmDAO = (DomainDAOInterface) Proxy
            .newProxyInstance(FilmDAO.class.getClassLoader(),
                    new Class<?>[]{DomainDAOInterface.class},
                    new SynchronizationHandler(new FilmDAO()));

    public static DomainDAOInterface<Director> getDirectorDAO() {
        return directorDAO;
    }

    public static DomainDAOInterface<Film> getFilmDAO() {
        return filmDAO;
    }

    public static void acquireLock() {
        domainLock.lock();
    }

    public static void releaseLock() {
        domainLock.unlock();
    }

    public static void loadFromXML() throws ModelException {
        acquireLock();
        // записываем текущее состояние, чтобы в случае ошибки можно было 
        // восстановить данные
        saveToXML("backup.xml");
        try {
            loadFromXML("model.xml");
        } catch (ModelException ex) {
            // типа rollback
            loadFromXML("backup.xml");
            throw ex;
        } finally {
            releaseLock();
        }
    }

    protected static void loadFromXML(String filename) throws ModelException {
        Model model = ModelLoader.loadFromXMLFile("model.xml");
        acquireLock();
        try {
            filmDAO.removeAll();
            directorDAO.removeAll();
            for (Director d : model.getDirectors()) {
                // сбрасывем первичный ключ, чтобы в СУБД сгенерился новый
                d.setIdDirector(null);
                directorDAO.create(d);
            }
            for (Film f : model.getFilms()) {
                // сбрасываем первичный ключ, чтобы в СУБД сгенерился новый
                f.setIdFilm(null);
                filmDAO.create(f);
            }
        } finally {
            releaseLock();
        }
    }

    public static void saveToXML() throws ModelException {
        saveToXML("model.xml");
    }

    private static void saveToXML(String filename) throws ModelException {
        Model model = new Model();
        acquireLock();
        try {
            model.getDirectors().addAll(directorDAO.findAll());
            model.getFilms().addAll(filmDAO.findAll());
            ModelLoader.saveToXMLFile(model, filename);
        } finally {
            releaseLock();
        }
    }

    /**
     * Обработчик вызовов к заданному DAO предметной области. Перед вызовом 
     * каждого метода будет проводится блокировка предметной области. После 
     * завершения работы метода блокировка снимается
     *
     * @param <T> класс-сущность
     */
    private static class SynchronizationHandler<T extends Entity> implements InvocationHandler {

        private final DomainDAOInterface<T> dao;

        public SynchronizationHandler(DomainDAOInterface<T> dao) {
            this.dao = dao;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            acquireLock();
            try {
                return method.invoke(dao, args);
            } finally {
                releaseLock();
            }
        }
    }

}
