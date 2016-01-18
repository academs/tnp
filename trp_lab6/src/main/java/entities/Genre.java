/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import javax.xml.bind.annotation.XmlEnum;

/**
 *
 * @author Айна и Лена
 */
@XmlEnum(String.class)
public enum Genre {
    ACTION("Экшн"),
    COMEDY("Комедия"),
    DRAMA("Драма"),
    FANTASY("Фэнтэзи"),
    HORROR("Ужасы");

    private final String name;

    private Genre(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
