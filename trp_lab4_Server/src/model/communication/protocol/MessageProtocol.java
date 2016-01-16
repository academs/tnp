package model.communication.protocol;

import java.io.IOException;

public interface MessageProtocol {
    
    void sendMessage(ModelMessage message) throws IOException;
    
    ModelMessage receiveMessage() throws Exception;
    
    void close() throws IOException;
    
}
