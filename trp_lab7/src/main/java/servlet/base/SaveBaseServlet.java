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
import servlet.InvalidStateException;

//@WebServlet(name = "EntitySaveServlet", urlPatterns = "/entities/save")
public abstract class SaveBaseServlet<T extends Entity> extends AbstractDomainServlet<T> {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setStatus(405);
    }

    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        boolean success = handleSaveRequest(request, response);
        doDispatch(request, response, success);
    }

    private boolean handleSaveRequest(HttpServletRequest request, HttpServletResponse response) {
        try {
            T entity = createInstance(request);
            if (entity.getId() != null) {
                if (!LockManager.getInstance()
                        .tryLock(request.getSession(true).getId(), entity)) {
                    handleLockError(entity.getId(), request);
                    return false;
                }
                getDAO().update(entity);
                handleSuccessUpdate(entity, request);
            } else {
                getDAO().create(entity);
                handleSuccessCreate(entity, request);
            }
        } catch (InvalidStateException ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
            handleError(request, ex.getMessage());
            return false;
        } catch (Exception ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
            handleError(request, "Возникла ошибка при сохранении записи");
            return false;
        }
        return true;
    }

    protected void handleError(HttpServletRequest request, String message) {
        XMLMessage msg = XMLMessage.createTextMessage(MessageType.UPDATE,
                getTarget(), message);
        String xml = XMLProtocol.convertMessage(msg);
        request.setAttribute("errorMessage", xml);
    }

    protected void handleNoIdError(HttpServletRequest request) {
        XMLMessage msg = XMLMessage.createMessage(MessageType.UPDATE,
                getTarget(), Collections.singletonList(createInstance()));
        String xml = XMLProtocol.convertMessage(msg);
        request.setAttribute("edit" + getTarget().getName(), xml);
    }

    protected void handleNotFoundError(Number id, HttpServletRequest request) {
        XMLMessage msg = XMLMessage.createTextMessage(MessageType.UPDATE,
                getTarget(), "Запись с id " + id + " не найдена");
        String xml = XMLProtocol.convertMessage(msg);
        request.setAttribute("errorMessage", xml);
    }

    private void handleLockError(Number id, HttpServletRequest request) {
        XMLMessage msg = XMLMessage.createTextMessage(MessageType.UPDATE,
                getTarget(), "Запись с id " + id + " заблокирована");
        String xml = XMLProtocol.convertMessage(msg);
        request.setAttribute("warnMessage", xml);
    }

    private void handleSuccessCreate(Entity entity, HttpServletRequest request) {
        request.getSession().removeAttribute("edit" + getTarget().getName() + "Entity");
        XMLMessage msg = XMLMessage.createTextMessage(MessageType.UPDATE,
                getTarget(), "Запись успешно добавлена");
        String xml = XMLProtocol.convertMessage(msg);
        request.setAttribute("infoMessage", xml);
    }

    private void handleSuccessUpdate(Entity entity, HttpServletRequest request) {
        LockManager.getInstance().releaseLock(request.getSession().getId(), entity);
//        request.getSession().removeAttribute("instance" + getTarget().getName());
        request.getSession().removeAttribute("edit" + getTarget().getName() + "Entity");
        XMLMessage msg = XMLMessage.createTextMessage(MessageType.UPDATE,
                getTarget(), "Запись успешно изменена");
        String xml = XMLProtocol.convertMessage(msg);
        request.setAttribute("infoMessage", xml);
    }

    protected abstract T createInstance(HttpServletRequest request) throws InvalidStateException;

}
