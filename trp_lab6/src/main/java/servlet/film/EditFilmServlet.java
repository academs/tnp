package servlet.film;

import entities.Director;
import entities.Film;
import entities.Genre;
import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import servlet.base.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.communication.protocol.EntityTarget;
import model.jdbc.DomainDAOInterface;
import model.jdbc.DomainDAOManager;

@WebServlet(name = "EditFilmServlet", urlPatterns = {"/films/edit", "/films/new"})
public class EditFilmServlet extends EditBaseServlet<Film> {

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
    public void doDispatch(HttpServletRequest request, HttpServletResponse response, boolean success) throws ServletException, IOException {
        if (request.getSession().getAttribute("genres") == null) {
            request.getSession().setAttribute("genres", Genre.values());
        }
        request.setAttribute("directors", directorDAO.findAll());
        if (success) {
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/editFilm.jsp");
            dispatcher.forward(request, response);
        } else {
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/films.jsp");
            dispatcher.forward(request, response);
        }
    }
}
