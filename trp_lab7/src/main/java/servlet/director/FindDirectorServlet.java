package servlet.director;

import entities.Director;
import java.io.IOException;
import javax.ejb.EJB;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import servlet.base.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.communication.protocol.EntityTarget;
import model.jdbc.DirectorDAO;
import model.jdbc.DomainDAOInterface;

@WebServlet(name = "FindDirectorServlet", urlPatterns = "/directors/find")
public class FindDirectorServlet extends FindBaseServlet<Director> {

    @EJB
    private DirectorDAO directorDAO;

    @Override
    protected Object[] extractParams(HttpServletRequest request) {
        Object[] res = new Object[]{request.getParameter("directorName")};
        if (res[0] != null && !"".equals(res[0])) {
            request.getSession().setAttribute("directorName", res[0]);
        } else {
            res[0] = request.getSession().getAttribute("directorName");
        }
        return res;
    }

    @Override
    public DomainDAOInterface<Director> getDAO() {
        return directorDAO;
    }

    @Override
    public EntityTarget getTarget() {
        return EntityTarget.DIRECTOR;
    }

    @Override
    public Director createInstance() {
        return new Director();
    }

    @Override
    public void doDispatch(HttpServletRequest request, HttpServletResponse response, boolean success)
            throws ServletException, IOException {
        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/directors.jsp");
        dispatcher.forward(request, response);
    }
}
