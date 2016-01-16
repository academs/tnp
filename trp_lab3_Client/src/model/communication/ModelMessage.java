package model.communication;

import java.io.Serializable;

/**
 *
 * @author Айна и Лена
 */
public class ModelMessage implements Serializable{
    public static enum MessageType {
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
    
    public static enum EntityTarget {
        DIRECTOR,
        FILM
    }
    
    private final MessageType type;
    private final EntityTarget target;
    private final Object data;

    public MessageType getType() {
        return type;
    }

    public EntityTarget getTarget() {
        return target;
    }

    public Object getData() {
        return data;
    }
    
    public ModelMessage(MessageType type, EntityTarget target, Object data) {
        this.type = type;
        this.target = target;
        this.data = data;
    }
}
