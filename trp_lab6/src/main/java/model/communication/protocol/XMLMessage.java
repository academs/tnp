package model.communication.protocol;

import entities.Director;
import entities.Film;
import java.util.ArrayList;
import java.util.Collection;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
public class XMLMessage {

    private MessageType type;
    private EntityTarget target;
    @XmlElementWrapper(name = "directors")
    @XmlElement(name = "director")
    private Collection<Director> directories;
    @XmlElementWrapper(name = "films")
    @XmlElement(name = "film")
    private Collection<Film> films;
    private String message;
    private Long id;

    public XMLMessage() {
    }

    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

    public EntityTarget getTarget() {
        return target;
    }

    public void setTarget(EntityTarget target) {
        this.target = target;
    }

    public Collection<Director> getDirectories() {
        return directories;
    }

    public void setDirectories(Collection<Director> directories) {
        this.directories = directories;
    }

    public Collection<Film> getFilms() {
        return films;
    }

    public void setFilms(Collection<Film> films) {
        this.films = films;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public static XMLMessage createDirectorMessage(MessageType type, Collection<Director> directors) {
        XMLMessage message = new XMLMessage();
        message.setTarget(EntityTarget.DIRECTOR);
        message.setType(type);
        message.setDirectories(directors);
        return message;
    }

    public static XMLMessage createFilmMessage(MessageType type, Collection<Film> films) {
        XMLMessage message = new XMLMessage();
        message.setTarget(EntityTarget.FILM);
        message.setType(type);
        message.setFilms(films);
        return message;
    }

    public static XMLMessage createTextMessage(MessageType type, EntityTarget target, String text) {
        XMLMessage message = new XMLMessage();
        message.setTarget(target);
        message.setType(type);
        message.setMessage(text);
        return message;
    }

    public static XMLMessage createMessage(MessageType type, EntityTarget target, Collection<?> entities) {
        switch (target) {
            case DIRECTOR:
                return createDirectorMessage(type, (Collection<Director>) entities);
            case FILM:
                return createFilmMessage(type, (Collection<Film>) entities);
            default:
                throw new AssertionError(target);
        }
    }
}
