package servlet.film;

import entities.Film;
import java.io.IOException;
import javax.ejb.EJB;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import servlet.base.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.communication.protocol.EntityTarget;
import model.jdbc.DomainDAOInterface;
import model.jdbc.FilmDAO;

@WebServlet(name = "DeleteFilmServlet", urlPatterns = "/films/delete")
public class DeleteFilmServlet extends DeleteBaseServlet<Film> {

    @EJB
    private FilmDAO filmDAO;

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
        if (success) {
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/films/find");
            dispatcher.forward(request, response);
        } else {
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/films.jsp");
            dispatcher.forward(request, response);
        }
    }

    @Override
    protected boolean checkRelationLocks(String id, Film proxy) {
        return true;
    }

}
