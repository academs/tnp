package log;

import entities.Director;
import entities.Film;
import javax.ejb.Remote;

@Remote
public interface LogProducer {

    void send(Director m);

    void send(Film m);
    
}
