package servlet.base;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.jdbc.Entity;

public abstract class AbstractDomainServlet<T extends Entity> extends HttpServlet
        implements DomainBaseServlet<T> {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    protected abstract void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException;

    protected Long extractNumberParameter(HttpServletRequest request, String name) {
        Long result = null;
        String raw = request.getParameter(name);
        if (raw != null) {
            result = Long.valueOf(raw);
        }
        return result;
    }

}
