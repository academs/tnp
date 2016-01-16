package model;

import java.util.Calendar;

/**
 *
 * @author Айна и Лена
 */
public class ClientController {
    
    private ClientController() {
    }
        
    public static void convertDirectorValue(Object[] data) throws ModelException {                
        Integer ID;
        try {
            ID = Integer.parseInt((String)data[0]);
        } catch (NumberFormatException ex) {
            throw new ModelException("ID: Диапазон значений 0-99999");
        }
        if ((ID < 0) || (ID > 99999)) 
            throw new ModelException("ID: Диапазон значений 0-99999");
        data[0] = ID;
        
        String name = (String)data[1];        
        if ((name.length() < 1) || (name.length() > 15))
            throw new ModelException("Название: Число символов 1-15");
        data[1] = name;
        
        Long phone = null;
        if (!((String)data[2]).isEmpty()) {
            try {
                phone = Long.parseLong((String)data[2]);
            } catch (NumberFormatException ex) {
                throw new ModelException("Телефон: Диапазон значений 0-9999999999");
            }
            if ((phone < 0) || (phone > 9999999999l))
                throw new ModelException("Телефон: Диапазон значений 0-9999999999");
        }   
        data[2] = phone;
    }
    
    public static void convertFilmValue(Object[] data) throws ModelException {                
        Long ID;
        try {
            ID = Long.parseLong((String)data[0]);
        } catch (NumberFormatException ex) {
            throw new ModelException("ID: Диапазон значений 0-9999999999");
        }
        if ((ID < 0) || (ID > 9999999999l)) 
            throw new ModelException("ID: Диапазон значений 0-9999999999");        
        data[0] = ID;
        
        String title = (String)data[1];
        if ((title.length() < 1) || (title.length() > 20))
            throw new ModelException("Название: Число символов 1-20");
        data[1] = title;
        
        if (data[2] == null)
            throw new ModelException("Жанр: Выберите тип");
        
        Short year = null;
        if (!((String)data[3]).isEmpty()) {
            try {
                year = Short.parseShort((String)data[3]);
            } catch (NumberFormatException ex) {
                throw new ModelException("Год выпуска: Диапазон значений 0-9999");
            }
            if ((year < 0) || (year > 9999)) {
                throw new ModelException("Год выпуска: Диапазон значений 0-9999");
            }
        }
        data[3] = year;
        
        Short duration = null;
        if (!((String)data[4]).isEmpty()) {
            try {
                duration = Short.parseShort((String)data[4]);
            } catch (NumberFormatException ex) {
                throw new ModelException("Длительность: Диапазон значений 0-999");
            }
            if ((duration < 0) || (duration > 999))
                throw new ModelException("Длительность: Диапазон значений 0-999");        
        }
        data[4] = duration;
                
        if (data[5] == null)        
            throw new ModelException("Режиссёр: задайте значение");
                    
    }
}
