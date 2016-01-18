package entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlRootElement
@Entity
public class Director implements Serializable, model.jdbc.Entity {

    private Integer idDirector;
    private String name;
    private Long phone;
    private Collection<Film> filmCollection = new ArrayList<Film>();

    public Director() {
    }

    public Director(Integer idDirector) {
        this.idDirector = idDirector;
    }

    public Director(Integer idDirector, String name) {
        this.idDirector = idDirector;
        this.name = name;
    }

    public Director(Object[] data) {
        this.idDirector = (Integer) data[0];
        this.name = (String) data[1];
        this.phone = (Long) data[2];
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    public Integer getIdDirector() {
        return idDirector;
    }

    public void setIdDirector(Integer idDirector) {
        this.idDirector = idDirector;
    }

    @XmlElement(required = true)
    @Column(name = "name", nullable = false)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "phone")
    public Long getPhone() {
        return phone;
    }

    public void setPhone(Long phone) {
        this.phone = phone;
    }

    @XmlTransient
    @OneToMany(mappedBy = "idDirector", cascade = CascadeType.REMOVE)
    public Collection<Film> getFilmCollection() {
        return filmCollection;
    }

    public void setFilmCollection(Collection<Film> filmCollection) {
        this.filmCollection = filmCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idDirector != null ? idDirector.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Director)) {
            return false;
        }
        Director other = (Director) object;
        if ((this.idDirector == null && other.idDirector != null) || (this.idDirector != null && !this.idDirector.equals(other.idDirector))) {
            return false;
        }
        return true;
    }

    @Transient
    public Object[] getData() {
        return new Object[]{idDirector, name, phone};
    }

    @Override
    public String toString() {
        return "Director{" + "idDirector=" + idDirector + ", name=" + name + ", phone=" + phone + '}';
    }

    @XmlTransient
    @Transient
    @Override
    public Number getId() {
        return idDirector;
    }

    @Override
    public void setId(Number n) {
        this.idDirector = (n == null ? null : n.intValue());
    }

}
