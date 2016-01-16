package model;

import entities.Director;
import entities.Film;
import java.util.Collection;
import model.communication.protocol.ModelMessage;
import model.jdbc.DomainDAOManager;
import model.jdbc.DomainDAOInterface;
import model.jdbc.LockManager;

/**
 *
 * @author Айна и Лена
 */
public class ServerController {

    private static ServerController instance;
    private final DomainDAOInterface<Director> directorDAO = DomainDAOManager.getDirectorDAO();
    private final DomainDAOInterface<Film> filmDAO = DomainDAOManager.getFilmDAO();

    private ServerController() {
    }

    public static ServerController getInstance() {
        if (instance == null) {
            instance = new ServerController();
        }
        return instance;
    }

    public Object[][] getDirectorsData() {
        Collection<Director> directors = directorDAO.findAll();
        Object[][] result = new Object[directors.size()][5];
        int i = 0;
        for (Director director : directors) {
            result[i] = director.getData();
            i++;
        }
        return result;
    }

    public Object[][] getFilmsData() {
        Collection<Film> films = filmDAO.findAll();
        Object[][] result = new Object[films.size()][6];
        int i = 0;
        for (Film film : films) {
            result[i] = film.getData();
            i++;
        }
        return result;
    }

    public Director getDirector(int ID) throws ModelException {
        Director res = directorDAO.find(ID);
        if (res != null) {
            return res;
        }
        throw new ModelException("Неверный ID");
    }

    public Film getFilm(Long ID) throws ModelException {
        Film res = filmDAO.find(ID);
        if (res != null) {
            return res;
        }
        throw new ModelException("Неверный ID");
    }

    public Object getEntity(ModelMessage message) throws ModelException {
        if (message.getTarget() == ModelMessage.EntityTarget.DIRECTOR) {
            return getDirector((Integer) message.getData());
        } else {
            return getFilm((Long) message.getData());
        }
    }

    public void addEntity(ModelMessage message) throws ModelException {
        Object[] data = (Object[]) message.getData();
        if (message.getTarget() == ModelMessage.EntityTarget.DIRECTOR) {
            Director director = new Director(data);
            director.setIdDirector(null); // TODO убрать из API
            directorDAO.create(director);
        } else {
            Film film = new Film(data);
            film.setIdFilm(null); // TODO убрать из API
            filmDAO.create(film);
        }
    }

    public void editEntity(ModelMessage message) throws ModelException {
        Object[] data = (Object[]) message.getData();
        if (message.getTarget() == ModelMessage.EntityTarget.DIRECTOR) {
            Director director = new Director(data);
            directorDAO.update(director);
        } else {
            Film film = new Film(data);
            filmDAO.update(film);
        }
    }

    public void removeEntity(ModelMessage message) throws ModelException {
        if (message.getTarget() == ModelMessage.EntityTarget.DIRECTOR) {
            directorDAO.remove((Integer) message.getData());
        } else {
            filmDAO.remove((Long) message.getData());
        }
    }

    public void loadFromFile() throws ModelException {
        //Model model = ModelLoader.loadFromXMLFile();
        //throw new UnsupportedOperationException("Операция пока ещё не реализована");
        DomainDAOManager.loadFromXML();
    }

    public void saveToFile() throws ModelException {
        //ModelLoader.saveToXMLFile(model);
        //throw new UnsupportedOperationException("Операция пока ещё не реализована");
        DomainDAOManager.saveToXML();
    }
}
