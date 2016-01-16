package model;

import entities.Director;
import entities.Film;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Model implements Serializable{
    
    private final List<Director> directors = new ArrayList<>();
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
