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

    private ModelMessage.MessageType type;
    private ModelMessage.EntityTarget target;
    @XmlElementWrapper(name = "directors")
    @XmlElement(name = "director")
    private Collection<Director> directories;
    @XmlElementWrapper(name = "films")
    @XmlElement(name = "film")
    private Collection<Film> films;
    private String errorMessage;
    private Long id;

    public XMLMessage() {
    }

    public XMLMessage(ModelMessage message) {
        this.target = message.getTarget();
        this.type = message.getType();
        if (message.getTarget() != null) {
            switch (message.getTarget()) {
                case DIRECTOR:
                    processDirectorEvent(message);
                    break;
                case FILM:
                    processFilmEvent(message);
                    break;
                default:
                    throw new AssertionError();
            }
        }
        if (message.getData() instanceof String) {
            errorMessage = (String) message.getData();
        }
    }

    private void processDirectorEvent(ModelMessage message) {
        Object data = message.getData();
        if (data == null) {
            return;
        }
        if (data instanceof Number) {
            Number n = (Number) data;
            this.id = n.longValue();
        }

        if (data instanceof Object[][]) {
            directories = new ArrayList<Director>();
            Object[][] tuple = (Object[][]) data;
            for (Object[] t : tuple) {
                directories.add(new Director(t));
            }
        } else if (data instanceof Object[]) {
            directories = new ArrayList<Director>();
            Object[] tuple = (Object[]) data;
            directories.add(new Director(tuple));
        }
    }

    private void processFilmEvent(ModelMessage message) {
        Object data = message.getData();
        if (data == null) {
            return;
        }
        if (data instanceof Number) {
            Number n = (Number) data;
            this.id = n.longValue();
        }
        if (data instanceof Object[][]) {
            films = new ArrayList<Film>();
            Object[][] tuple = (Object[][]) data;
            for (Object[] t : tuple) {
                films.add(createFilm(t));
            }
        } else if (data instanceof Object[]) {
            films = new ArrayList<Film>();
            Object[] tuple = (Object[]) data;
            films.add(createFilm(tuple));
        }
    }

    private static Film createFilm(Object[] data) {
        Film f = new Film(data);
        Director d = new Director();
        d.setIdDirector((Integer) data[5]);
        f.setIdDirector(d);
        return f;
    }

    public Object getData() {
        if (id != null) {
            if (target == ModelMessage.EntityTarget.DIRECTOR) {
                return id.intValue();
            } else {
                return id;
            }
        }
        if (errorMessage != null) {
            return errorMessage;
        }
        if (directories != null) {
            if (directories.size() > 1
                    || type == ModelMessage.MessageType.UPDATE) {
                Object[][] data = new Object[directories.size()][];
                int i = 0;
                for (Director d : directories) {
                    data[i] = d.getData();
                    i++;
                }
                return data;
            } else if (directories.size() == 1) {
                return directories.iterator().next().getData();
            }
        }
        if (films != null) {
            if (films.size() > 1
                    || type == ModelMessage.MessageType.UPDATE) {
                Object[][] data = new Object[films.size()][];
                int i = 0;
                for (Film d : films) {
                    data[i] = d.getData();
                    i++;
                }
                return data;
            } else if (films.size() == 1) {
                return films.iterator().next().getData();
            }
        }
        return null;
    }

    public static ModelMessage createModelMessage(XMLMessage xml) {
        if (xml == null) {
            return null;
        }
        return new ModelMessage(xml.type, xml.target, xml.getData());
    }

}
