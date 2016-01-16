package model;

import entities.Director;
import entities.Film;
import entities.Genre;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Collection;
import java.util.List;
import model.communication.ModelMessage;

/**
 *
 * @author Айна и Лена
 */
public class ServerController {
    
    private Model model;
    private static ServerController instance;
    
    private ServerController() {
        model = new Model();
    }
    
    public static ServerController getInstance() {
        if (instance == null) {
            instance = new ServerController();
        }
        return instance;
    }
    
    public Object[][] getDirectorsData() {
        List<Director> directors = model.getDirectors();
        Object[][] result = new Object[directors.size()][5];
        for (int i = 0; i < directors.size(); i++) {
            result[i] = directors.get(i).getData();
        }
        return result;
    }
    
    public Object[][] getFilmsData() {
        List<Film> films = model.getFilms();
        Object[][] result = new Object[films.size()][6];
        for (int i = 0; i < films.size(); i++) {
            result[i] = films.get(i).getData();            
        }
        return result;
    }
    
    public Director getDirector(int ID) throws ModelException {
        for(Director director: model.getDirectors()) {
            if (director.getIdDirector().equals(ID))
                return director;
        }
        throw new ModelException("Неверный ID");
    }

    public Film getFilm(Long ID) throws ModelException {
        for(Film film: model.getFilms()) {
            if (film.getIdFilm().equals(ID))
                return film;
        }
        throw new ModelException("Неверный ID");
    }
    
    public Object getEntity(ModelMessage message) throws ModelException {
        if (message.getTarget() == ModelMessage.EntityTarget.DIRECTOR)
            return getDirector((Integer)message.getData());
        else
            return getFilm((Long)message.getData());
    }
    
    public void addEntity(ModelMessage message) throws ModelException {
        Object[] data = (Object[])message.getData();
        if (message.getTarget() == ModelMessage.EntityTarget.DIRECTOR) {
            testDirectorData(data);
            Director director = new Director(data);
            model.getDirectors().add(director);
        }
        else {
            Director parent = testFilmData(data);
            Film film = new Film(data);
            film.setIdDirector(parent);
            parent.getFilmCollection().add(film);
            model.getFilms().add(film);
        }
    }    
    
    private void testDirectorData(Object[] data) throws ModelException {
        Integer ID = (Integer) data[0];
        for (Director director : model.getDirectors()) {
            if (director.getIdDirector().equals(ID)) {
                throw new ModelException("ID: Дублирование");
            }
        }
    }
    
    private Director testFilmData(Object[] data) throws ModelException {
        Long ID = (Long) data[0];
        for (Film film : model.getFilms()) {
            if (film.getIdFilm().equals(ID)) {
                throw new ModelException("ID: Дублирование");
            }
        }
        String scID = (String)data[5];        
        Integer icID = Integer.parseInt(scID.substring(
                scID.lastIndexOf("[ID=") + 4, scID.lastIndexOf("]")));
        Director parent = null;
        for (Director director : model.getDirectors()) {
            if (director.getIdDirector().equals(icID)) {
                parent = director;
                break;
            }
        }
        if (parent == null)
            throw new ModelException("DirectorID: Такой компании не существует");
        return parent;
    }
    
    public void editEntity(ModelMessage message) throws ModelException {
        Object[] data = (Object[])message.getData();
        if (message.getTarget() == ModelMessage.EntityTarget.DIRECTOR) {
            Director director = getDirector((Integer)data[0]);
            director.setName((String)data[1]);
            director.setPhone((Long)data[2]);
        }
        else {
            Film film = getFilm((Long)data[0]);            
            film.setTitle((String)data[1]);
            film.setGenre((Genre)data[2]);
            film.setYear((Short)data[3]);
            film.setDuration((Short)data[4]);
            //Removing link to the Director
            film.getIdDirector().getFilmCollection().remove(film);
            //Creating link to the Director
            String scID = (String)data[5];
            Integer icID = Integer.parseInt(scID.substring(
                    scID.lastIndexOf("[ID=") + 4, scID.lastIndexOf("]")));
            film.setIdDirector(getDirector(icID));            
            film.getIdDirector().getFilmCollection().add(film);
        }
    } 
    
    public void removeEntity(ModelMessage message) throws ModelException {
       if (message.getTarget() == ModelMessage.EntityTarget.DIRECTOR) {
            Director director = getDirector((Integer)message.getData());
            Collection<Film> linkedFilms = director.getFilmCollection();
            //Removing linked Films
            for(Film film: linkedFilms)
                model.getFilms().remove(film);
            model.getDirectors().remove(director);
        }
        else {
            Film film = getFilm((Long)message.getData());
            //Removing linked Director
            film.getIdDirector().getFilmCollection().remove(film);
            model.getFilms().remove(film);
        }
    }
    
    public void loadFromFile() throws ModelException{
        try {
            ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream("model.dat"));
            try {
                model = (Model)ois.readObject();    
            } finally {
                ois.close();
            }            
        } catch (ClassNotFoundException | IOException ex) {
            throw new ModelException("Загрузка из файла: " + ex.getMessage());
        }
    }

    public void saveToFile() throws ModelException{
        try {
            ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream("model.dat"));            
            try {
                oos.writeObject(model);
            } finally {
                oos.close();
            }            
        } catch (FileNotFoundException ex) {
            throw new ModelException("Сохранение в файл: " + ex.getMessage());
        } catch (IOException ex) {
            throw new ModelException("Сохранение в файл: " + ex.getMessage());
        }
    }
}
