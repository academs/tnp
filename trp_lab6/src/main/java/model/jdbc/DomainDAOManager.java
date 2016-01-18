package model.jdbc;

import entities.Director;
import entities.Film;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

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
