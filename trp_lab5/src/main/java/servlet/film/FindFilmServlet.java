package servlet.film;

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

@WebServlet(name = "FindFilmServlet", urlPatterns = "/films/find")
public class FindFilmServlet extends FindBaseServlet<Film> {

    private DomainDAOInterface<Film> filmDAO = DomainDAOManager.getFilmDAO();

    @Override
    protected Object[] extractParams(HttpServletRequest request) {
        Object[] res = new Object[]{request.getParameter("filmTitle")};
        if (res[0] != null && !"".equals(res[0])) {
            request.getSession().setAttribute("filmTitle", res[0]);
        } else {
            res[0] = request.getSession().getAttribute("filmTitle");
        }
        return res;
    }

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
    public void doDispatch(HttpServletRequest request, HttpServletResponse response, boolean success)
            throws ServletException, IOException {
        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/films.jsp");
        dispatcher.forward(request, response);
    }
}
