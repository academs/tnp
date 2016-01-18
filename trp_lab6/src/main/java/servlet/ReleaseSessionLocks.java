package servlet;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import model.jdbc.LockManager;

@WebListener
public class ReleaseSessionLocks implements HttpSessionListener {

    @Override
    public void sessionCreated(HttpSessionEvent hse) {
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent hse) {
        LockManager.getInstance().releaseAllLocks(hse.getSession().getId());
    }
    
}
