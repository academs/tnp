package model.communication.protocol;

import javax.xml.bind.annotation.XmlEnum;

@XmlEnum(String.class)
public enum MessageType {
    UPDATE,
    CREATE,
    START_EDIT,
    STOP_EDIT,
    DELETE,
    REPSONSE_ALLOWED,
    REPSONSE_DENIED,
    SAVE_MODEL,
    LOAD_MODEL,
    FINISH_SESSION
}
