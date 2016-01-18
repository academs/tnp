package servlet.base;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.communication.protocol.EntityTarget;
import model.jdbc.DomainDAOInterface;
import model.jdbc.Entity;

public interface DomainBaseServlet<T extends Entity> {

    DomainDAOInterface<T> getDAO();

    EntityTarget getTarget();

    T createInstance();

    void doDispatch(HttpServletRequest request, HttpServletResponse response,
            boolean success) throws ServletException, IOException;

}
