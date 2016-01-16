package entities;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlRootElement;
/**
 *
 * @author Agataeva, Birukova
 */

//@Entity
//@Table(name = "FILM")
@XmlRootElement
public class Film implements Serializable {
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
        this.idFilm = (Long)data[0];
        this.title = (String)data[1];
        this.genre = (Genre)data[2];
        this.year = (Short)data[3];
        this.duration = (Short)data[4];      
    }

    public Long getIdFilm() {
        return idFilm;
    }

    public void setIdFilm(Long idFilm) {
        this.idFilm = idFilm;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

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
        return "entities.Film[ idFilm=" + idFilm + " ]";
    }
    
    public Object[] getData() {
        return new Object[] {idFilm, title, genre, year, duration, idDirector.getName() + " [ID=" + idDirector.getIdDirector() + "]"};    
    }
    
}
