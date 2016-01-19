package servlet.base;

import java.io.IOException;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.communication.protocol.MessageType;
import model.communication.protocol.XMLMessage;
import model.communication.protocol.XMLProtocol;
import model.jdbc.Entity;
import model.jdbc.LockManager;

//@WebServlet(name = "EntityFindServlet", urlPatterns = "/entities/find")
public abstract class FindBaseServlet<T extends Entity> extends AbstractDomainServlet<T> {

    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        boolean success = handleSearchRequest(request);
        LockManager.getInstance().releaseAllLocks(request.getSession().getId());
        doDispatch(request, response, success);
    }

    protected boolean handleSearchRequest(HttpServletRequest request) {
        Object[] searchParam = extractParams(request);
        try {
            Collection<T> found;
            if (searchParam == null || searchParam[0] == null
                    || String.valueOf(searchParam[0]).trim().isEmpty()) {
                found = getDAO().findAll();
            } else {
                found = getDAO().findByParam(searchParam);
            }
            handleSuccess(found, request);
        } catch (Exception ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
            handleError(request);
            return false;
        }
        return true;
    }

    protected void handleError(HttpServletRequest request) {
        XMLMessage msg = XMLMessage.createTextMessage(MessageType.LOAD_MODEL,
                getTarget(), "Не удалось выполнить поиск");
        String xml = XMLProtocol.convertMessage(msg);
        request.setAttribute("errorMessage", xml);
    }

    protected void handleSuccess(Collection<T> found, HttpServletRequest request) {
        XMLMessage msg = XMLMessage.createMessage(MessageType.LOAD_MODEL, getTarget(), found);
        String xml = XMLProtocol.convertMessage(msg);
        request.getSession(true).setAttribute("found" + getTarget().getName(), xml);
    }

    protected abstract Object[] extractParams(HttpServletRequest request);

}
