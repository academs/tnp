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

}
