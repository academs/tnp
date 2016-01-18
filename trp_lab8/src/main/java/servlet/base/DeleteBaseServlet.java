package servlet.base;

import java.io.IOException;
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

//@WebServlet(name = "DeleteEntityServlet", urlPatterns = "/entities/delete")
public abstract class DeleteBaseServlet<T extends Entity> extends AbstractDomainServlet<T> {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setStatus(405);
    }

    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        boolean success = handleDeleteRequest(request);
        doDispatch(request, response, success);
    }

    private boolean handleDeleteRequest(HttpServletRequest request) {
        Number id = null;
        try {
            id = extractNumberParameter(request, "id");
            T proxy = createInstance();
            proxy.setId(id);
            if (!LockManager.getInstance()
                    .tryLock(request.getSession(true).getId(), proxy)
                    || !checkRelationLocks(request.getSession().getId(), proxy)) {
                handleLockError(id, request);
                return false;
            }            
            getDAO().remove(proxy.getId());
            handleSuccess(proxy, request);
        } catch (NumberFormatException ex) {
            handleNoIdError(request);
            return false;
        } catch (Exception ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
            handleError(id, request);
            return false;
        }
        return true;
    }

    protected void handleNoIdError(HttpServletRequest request) {
        XMLMessage msg = XMLMessage.createTextMessage(MessageType.DELETE,
                getTarget(), "Не задан id записи");
        String xml = XMLProtocol.convertMessage(msg);
        request.setAttribute("errorMessage", xml);
    }

    protected void handleNotFoundError(Number id, HttpServletRequest request) {
        XMLMessage msg = XMLMessage.createTextMessage(MessageType.DELETE,
                getTarget(), "Запись с id " + id + " не найдена");
        String xml = XMLProtocol.convertMessage(msg);
        request.setAttribute("errorMessage", xml);
    }

    private void handleLockError(Number id, HttpServletRequest request) {
        XMLMessage msg = XMLMessage.createTextMessage(MessageType.DELETE,
                getTarget(), "Запись заблокирована");
        String xml = XMLProtocol.convertMessage(msg);
        request.setAttribute("warnMessage", xml);
    }

    private void handleSuccess(Entity entity, HttpServletRequest request) {
        LockManager.getInstance().releaseLock(request.getSession().getId(), entity);
        XMLMessage msg = XMLMessage.createTextMessage(MessageType.DELETE, getTarget(),
                "Запись успешно удалена");
        String xml = XMLProtocol.convertMessage(msg);
        request.setAttribute("infoMessage", xml);
    }

    private void handleError(Number id, HttpServletRequest request) {
        XMLMessage msg = XMLMessage.createTextMessage(MessageType.DELETE,
                getTarget(), "Возникла ошибка при удалении записи с id " + id);
        String xml = XMLProtocol.convertMessage(msg);
        request.setAttribute("errorMessage", xml);
    }

    protected abstract boolean checkRelationLocks(String id, T proxy);

}
