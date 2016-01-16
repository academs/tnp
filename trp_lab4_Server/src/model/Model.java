package model;

import entities.Director;
import entities.Film;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
public class Model implements Serializable {

    @XmlElementWrapper(name = "directors")
    @XmlElement(name = "director")
    private final List<Director> directors = new ArrayList<>();
    @XmlElementWrapper(name = "films")
    @XmlElement(name = "film")
    private final List<Film> films = new ArrayList<>();

    public Model() {
    }

    public List<Director> getDirectors() {
        return directors;
    }

    public List<Film> getFilms() {
        return films;
    }
}
