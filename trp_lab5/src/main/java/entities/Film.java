package entities;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import model.communication.protocol.XMLProtocol.DirectorReferenceAdapter;
import model.jdbc.Entity;

/**
 *
 * @author Agataeva, Birukova
 */
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlRootElement
public class Film implements Serializable, Entity {

    private static final long serialVersionUID = 1L;

    private Long idFilm;
    private String title;
    private Genre genre;
    private Short year;
    private Short duration;
    private Director idDirector;

    public Film() {
    }

    public Film(Long idFilm) {
        this.idFilm = idFilm;
    }

    public Film(Long idFilm, String title, Genre genre) {
        this.idFilm = idFilm;
        this.title = title;
        this.genre = genre;
    }

    public Film(Object[] data) {
        this.idFilm = (Long) data[0];
        this.title = (String) data[1];
        this.genre = (Genre) data[2];
        this.year = (Short) data[3];
        this.duration = (Short) data[4];
        this.idDirector = new Director((Integer) data[5]);
    }

    public Long getIdFilm() {
        return idFilm;
    }

    public void setIdFilm(Long idFilm) {
        this.idFilm = idFilm;
    }

    @XmlElement(required = true)
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @XmlElement(required = true)
    public Genre getGenre() {
        return genre;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }

    public Short getYear() {
        return year;
    }

    public void setYear(Short year) {
        this.year = year;
    }

    public Short getDuration() {
        return duration;
    }

    public void setDuration(Short duration) {
        this.duration = duration;
    }

    @XmlElement(required = true)
    @XmlJavaTypeAdapter(DirectorReferenceAdapter.class)
    public Director getIdDirector() {
        return idDirector;
    }

    public void setIdDirector(Director idDirector) {
        this.idDirector = idDirector;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idFilm != null ? idFilm.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Film)) {
            return false;
        }
        Film other = (Film) object;
        if ((this.idFilm == null && other.idFilm != null) || (this.idFilm != null && !this.idFilm.equals(other.idFilm))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Film{" + "idFilm=" + idFilm + ", title=" + title + ", genre=" + genre + ", year=" + year + ", duration=" + duration + ", idDirector=" + idDirector + '}';
    }

    public Object[] getData() {
        return new Object[]{idFilm, title, genre, year, duration, idDirector.getIdDirector()};
    }

    @XmlTransient
    @Override
    public Long getId() {
        return idFilm;
    }

    @Override
    public void setId(Number n) {
        this.idFilm = n.longValue();
    }

    public String getGenreName() {
        return getGenre() != null ? getGenre().getName() : null;
    }

    public void setGenreName(String genre) {
    }

    public String getDirectorName() {
        return getIdDirector() != null ? getIdDirector().getName() : null;
    }

    public void setDirectorName(String name) {
    }
}
