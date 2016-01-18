package model.jdbc;

import entities.Director;
import entities.Film;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Простейший менеджер блокировок
 */
public class LockManager {

    /**
     * Класс, представляющий блокировку
     */
    private static class Lock {

        private final Entity entity;
        private final Object client;

        public Lock(Entity entity, Object client) {
            this.entity = entity;
            this.client = client;
        }

        public Entity getEntity() {
            return entity;
        }

        public Object getClient() {
            return client;
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 17 * hash + Objects.hashCode(this.entity);
            hash = 17 * hash + Objects.hashCode(this.client);
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final Lock other = (Lock) obj;
            if (!Objects.equals(this.entity, other.entity)) {
                return false;
            }
            if (!Objects.equals(this.client, other.client)) {
                return false;
            }
            return true;
        }

    }

    private static final LockManager instance = new LockManager();

    public static LockManager getInstance() {
        return instance;
    }

    private LockManager() {
    }

    private final Map<Number, Lock> lockedDirectors = new HashMap();
    private final Map<Number, Lock> lockedFilms = new HashMap<>();

    /**
     * Попытка захвата блокировки
     *
     * @param client id клиента, который запрашивает блокировку
     * @param entity сущность, на которую налагается блокировка
     * @return true, если захват блокировки был произведён успешно
     */
    public synchronized boolean tryLock(Object client, Entity entity) {
        Lock lock = new Lock(entity, client);
        if (entity instanceof Film && !isLocked(lock, lockedFilms)) {
            lockedFilms.put(entity.getId(), lock);
            return true;
        }
        if (entity instanceof Director && !isLocked(lock, lockedDirectors)) {
            lockedDirectors.put(entity.getId(), lock);
            return true;
        }
        return false;
    }

    /**
     * * Снимает блокировку клиента с сущности, если таковая была
     *
     * @param client id клиента
     * @param entity сущность, с которой нужно снять блокировку
     */
    public synchronized void releaseLock(Object client, Entity entity) {
        if (entity instanceof Film) {
            releaseLock(client, entity, lockedFilms);
        }
        if (entity instanceof Director) {
            releaseLock(client, entity, lockedDirectors);
        }
    }

    private void releaseLock(Object client, Entity entity, Map<Number, Lock> locks) {
        Lock lock = locks.get(entity.getId());
        if (lock != null && client.equals(lock.getClient())) {
            locks.remove(entity.getId());
        }
    }

    public synchronized boolean noFilmsLockedForDirectorId(Object cient, Number id) {
        Lock directorLock = lockedDirectors.get(id);
        if (directorLock != null && !directorLock.getClient().equals(cient)) {
            return false;
        }
        for (Map.Entry<Number, Lock> entry : lockedFilms.entrySet()) {
            Film f = (Film) entry.getValue().getEntity();
            if (id.equals(f.getIdDirector().getId())
                    && !entry.getValue().getClient().equals(cient)) {
                // какой-то фильм заблокирован другим клиентом, удалять режиссёра нельзя
                return false;
            }
        }
        return true;
    }

    /**
     * @return true, если нет блокировок
     */
    public synchronized boolean empty() {
        return lockedFilms.isEmpty() && lockedDirectors.isEmpty();
    }
    
    public synchronized void releaseAllLocks(Object client) {
        releaseLock(client, lockedFilms);
        releaseLock(client, lockedDirectors);
    }

    private void releaseLock(Object client, Map<Number, Lock> locks) {
        List<Number> keys = new ArrayList<>();
        for (Map.Entry<Number, Lock> entry : locks.entrySet()) {
            Number key = entry.getKey();
            Lock value = entry.getValue();
            if (client.equals(value.getClient())) {
                keys.add(key);
            }
        }
        for (Number key : keys) {
            locks.remove(key);
        }
    }

    private boolean isLocked(Lock lock, Map<Number, Lock> locks) {
        Number id = lock.getEntity().getId();
        Lock old = locks.get(id);
        return old != null && !old.getClient().equals(lock.getClient());
    }

}
