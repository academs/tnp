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
import model.jdbc.LockManager;

@WebServlet(name = "DeleteDirectorServlet", urlPatterns = "/directors/delete")
public class DeleteDirectorServlet extends DeleteBaseServlet<Director> {

    @Override
    protected boolean checkRelationLocks(String id, Director proxy) {
        return LockManager.getInstance().noFilmsLockedForDirectorId(id, proxy.getId());
    }

    @EJB
    private DirectorDAO directorDAO;

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
    public void doDispatch(HttpServletRequest request, HttpServletResponse response, boolean success) throws ServletException, IOException {
        if (success) {
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/directors/find");
            dispatcher.forward(request, response);
        } else {
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/directors.jsp");
            dispatcher.forward(request, response);
        }
    }

}
