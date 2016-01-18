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
import servlet.InvalidStateException;

@WebServlet(name = "SaveDirectorServlet", urlPatterns = "/directors/save")
public class SaveDirectorServlet extends SaveBaseServlet<Director> {

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
    public Director createInstance(HttpServletRequest request) throws InvalidStateException {
        try {
            Director d = createInstance();
            String p = request.getParameter("id");
            if (p != null && !p.trim().isEmpty()) {
                Long id = extractNumberParameter(request, "id");
                d.setId(id);
            }
            String name = (String) request.getParameter("name");
            p = request.getParameter("phone");
            if (p != null && !p.trim().isEmpty()) {
                Long phone = extractNumberParameter(request, "phone");
                d.setPhone(phone);
            }
            d.setName(name);
            return d;
        } catch (Exception ex) {
            throw new InvalidStateException("Некоторые поля имеют некорректные значения", ex);
        }
    }

    @Override
    public void doDispatch(HttpServletRequest request, HttpServletResponse response, boolean success) throws ServletException, IOException {
        if (success) {
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/directors/find");
            dispatcher.forward(request, response);
        } else {
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/editDirector.jsp");
            dispatcher.forward(request, response);
        }
    }

}
