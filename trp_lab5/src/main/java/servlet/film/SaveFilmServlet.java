package servlet.film;

import entities.Director;
import entities.Film;
import entities.Genre;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import servlet.base.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.ModelException;
import model.communication.protocol.EntityTarget;
import model.jdbc.DomainDAOInterface;
import model.jdbc.DomainDAOManager;
import servlet.InvalidStateException;

@WebServlet(name = "SaveFilmServlet", urlPatterns = "/films/save")
public class SaveFilmServlet extends SaveBaseServlet<Film> {

    private DomainDAOInterface<Film> filmDAO = DomainDAOManager.getFilmDAO();
    private DomainDAOInterface<Director> directorDAO = DomainDAOManager.getDirectorDAO();

    @Override
    public DomainDAOInterface<Film> getDAO() {
        return filmDAO;
    }

    @Override
    public EntityTarget getTarget() {
        return EntityTarget.FILM;
    }

    @Override
    public Film createInstance() {
        return new Film();
    }

    @Override
    public Film createInstance(HttpServletRequest request) throws InvalidStateException {
        try {
            Film f = createInstance();
            String p = request.getParameter("id");
            if (p != null && !p.trim().isEmpty()) {
                Long id = extractNumberParameter(request, "id");
                f.setId(id);
            }
            String name = (String) request.getParameter("title");
            String genre = (String) request.getParameter("genre");
            p = request.getParameter("year");
            if (p != null && !p.trim().isEmpty()) {
                Long year = extractNumberParameter(request, "year");
                f.setYear(year.shortValue());
            }
            p = request.getParameter("duration");
            if (p != null && !p.trim().isEmpty()) {
                Long duration = extractNumberParameter(request, "duration");
                f.setDuration(duration.shortValue());
            }
            Long idDirector = extractNumberParameter(request, "director");
            f.setTitle(name);
            Director d = null;
            if (idDirector != null) {
                try {
                    d = directorDAO.find(idDirector.intValue());
                } catch (ModelException ex) {
                    /*ничего не делаем, исключение будет потом*/
                }
            }
            if (d == null) {
                throw new InvalidStateException("Для фильма не задан режиссёр");
            }
            f.setIdDirector(d);
            f.setGenre(Genre.valueOf(genre));
            return f;
        } catch (InvalidStateException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new InvalidStateException("Некоторые поля имеют некорректные значения", ex);
        }
    }

    @Override
    public void doDispatch(HttpServletRequest request, HttpServletResponse response, boolean success) throws ServletException, IOException {
        if (success) {
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/films/find");
            dispatcher.forward(request, response);
        } else {
			request.setAttribute("directors", directorDAO.findAll());
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/editFilm.jsp");
            dispatcher.forward(request, response);
        }
    }

}
