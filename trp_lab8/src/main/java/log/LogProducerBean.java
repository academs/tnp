package log;

import entities.Director;
import entities.Film;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import model.communication.protocol.EntityTarget;

@Stateless
public class LogProducerBean implements LogProducer {

    @Resource(mappedName = "java:app/jms/logConnectionFactory")
    private ConnectionFactory connectionFactory;
    @Resource(mappedName = "java:app/jms/logQueue")
    private Queue queue;

    public LogProducerBean() {
    }

    @Override
    public void send(Director d) {
        LogRecord record = new LogRecord();
        record.setTime(new Date());
        record.setTargetEntity(EntityTarget.DIRECTOR);
        record.setTargetId(d.getIdDirector().longValue());
        this.send(record);
    }

    @Override
    public void send(Film m) {
        LogRecord record = new LogRecord();
        record.setTime(new Date());
        record.setTargetEntity(EntityTarget.FILM);
        record.setTargetId(m.getId());
        this.send(record);
    }

    private void send(LogRecord record) {
        try {
            try (Connection connection = connectionFactory.createConnection()) {
                try (Session session = connection.createSession()) {
                    try (MessageProducer producer = session.createProducer(queue)) {
                        Message msg = session.createObjectMessage(record);
                        producer.send(msg);
                    }
                }
            }

        } catch (JMSException ex) {
            Logger.getLogger(LogProducerBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
