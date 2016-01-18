package model.communication.protocol;

import javax.xml.bind.annotation.XmlEnum;

@XmlEnum(value = String.class)
public enum EntityTarget {
    DIRECTOR("Director"), FILM("Film");

    private final String name;

    private EntityTarget(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
