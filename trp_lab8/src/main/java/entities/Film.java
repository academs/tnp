package entities;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import model.communication.protocol.XMLProtocol.DirectorReferenceAdapter;

/**
 *
 * @author Agataeva, Birukova
 */
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlRootElement
@Entity
public class Film implements Serializable, model.jdbc.Entity {

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

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    public Long getIdFilm() {
        return idFilm;
    }

    public void setIdFilm(Long idFilm) {
        this.idFilm = idFilm;
    }

    @XmlElement(required = true)
    @Column(name = "title", nullable = false)
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @XmlElement(required = true)
    @Enumerated(EnumType.STRING)
    @Column(name = "genre", nullable = false)
    public Genre getGenre() {
        return genre;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }

    @Column(name = "\"year\"")
    public Short getYear() {
        return year;
    }

    public void setYear(Short year) {
        this.year = year;
    }

    @Column(name = "duration")
    public Short getDuration() {
        return duration;
    }

    public void setDuration(Short duration) {
        this.duration = duration;
    }

    @XmlElement(required = true)
    @XmlJavaTypeAdapter(DirectorReferenceAdapter.class)
    @ManyToOne(optional = false)
    @JoinColumn(name = "director_id", referencedColumnName = "id")
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

    @Transient
    public Object[] getData() {
        return new Object[]{idFilm, title, genre, year, duration, idDirector.getIdDirector()};
    }

    @XmlTransient
    @Transient
    @Override
    public Long getId() {
        return idFilm;
    }

    @Override
    public void setId(Number n) {
        this.idFilm = n.longValue();
    }

    @Transient
    public String getGenreName() {
        return getGenre() != null ? getGenre().getName() : null;
    }

    public void setGenreName(String genre) {
    }

    @Transient
    public String getDirectorName() {
        return getIdDirector() != null ? getIdDirector().getName() : null;
    }

    public void setDirectorName(String name) {
    }
}
