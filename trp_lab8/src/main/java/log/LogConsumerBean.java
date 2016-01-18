package log;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.MessageDriven;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@MessageDriven(mappedName = "java:app/jms/logQueue")
public class LogConsumerBean implements MessageListener {

    @PersistenceContext
    private EntityManager em;

    @Override
    public void onMessage(Message message) {
        try {
            LogRecord record = (LogRecord) ((ObjectMessage) message).getObject();
            em.persist(record);
        } catch (JMSException ex) {
            Logger.getLogger(LogConsumerBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(LogConsumerBean.class.getName()).log(Level.WARNING, null, ex);
        }
    }
}
