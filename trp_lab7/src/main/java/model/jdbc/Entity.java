package model.jdbc;

/**
 * Интерфейс простейшей сущности
 */
public interface Entity {

    /**
     * @return значение первичного ключа
     */
    Number getId();
    
    void setId(Number n);
}
