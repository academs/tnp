package servlet.base;

import java.io.IOException;
import java.util.Collections;
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

//@WebServlet(name = "EntityEditServlet", urlPatterns = {"/entities/edit", "/entities/new"})
public abstract class EditBaseServlet<T extends Entity> extends AbstractDomainServlet<T> {

    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        boolean success = handleEditRequest(request);
        doDispatch(request, response, success);
    }

    private boolean handleEditRequest(HttpServletRequest request) {
        Number id = null;
        try {
            T entity;
            id = extractNumberParameter(request, "id");
            if (id != null) {
                entity = getDAO().find(id);
                if (entity == null) {
                    handleNotFoundError(id, request);
                    return false;
                }
                if (!LockManager.getInstance()
                        .tryLock(request.getSession(true).getId(), entity)) {
                    handleLockError(id, request);
                    return false;
                }
            } else {
                entity = createInstance();
            }
            handleSuccess(entity, request);
        } catch (Exception ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
            handleError(id, request);
            return false;
        }
        return true;
    }

    protected void handleNotFoundError(Number id, HttpServletRequest request) {
        XMLMessage msg = XMLMessage.createTextMessage(MessageType.START_EDIT,
                getTarget(), "Запись с id " + id + " не найдена");
        String xml = XMLProtocol.convertMessage(msg);
        request.setAttribute("errorMessage", xml);
    }

    private void handleLockError(Number id, HttpServletRequest request) {
        XMLMessage msg = XMLMessage.createTextMessage(MessageType.START_EDIT,
                getTarget(), "Запись с id " + id + " заблокирована");
        String xml = XMLProtocol.convertMessage(msg);
        request.setAttribute("warnMessage", xml);
    }

    private void handleSuccess(T entity, HttpServletRequest request) {
        T prev = (T) request.getSession(true)
                .getAttribute("edit" + getTarget().getName() + "Entity");
        if (prev != null && prev.getId() != null
                && !prev.getId().equals(entity.getId())) {
            LockManager.getInstance().releaseLock(request.getSession().getId(), prev);
        }
        request.getSession().setAttribute("edit" + getTarget().getName() + "Entity", entity);
        XMLMessage msg = XMLMessage.createMessage(MessageType.START_EDIT,
                getTarget(), Collections.singletonList(entity));
        String xml = XMLProtocol.convertMessage(msg);
        request.setAttribute("edit" + getTarget().getName(), xml);
    }

    private void handleError(Number id, HttpServletRequest request) {
        String text;
        if (id == null) {
            text = "Возникла ошибка при создании";
        } else {
            text = "Возникла ошибка при старте редактирования записи с id " + id;
        }
        XMLMessage msg = XMLMessage.createTextMessage(MessageType.START_EDIT,
                getTarget(), text);
        String xml = XMLProtocol.convertMessage(msg);
        request.setAttribute("errorMessage", xml);
    }

}
