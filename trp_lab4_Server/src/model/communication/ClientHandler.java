package model.communication;

import model.communication.protocol.ModelMessage;
import model.communication.protocol.MessageProtocol;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Айна и Лена
 */
public class ClientHandler extends Thread{
    
    private final int clientNo;
    private final MessageProtocol protocol;
    private Integer editingDirectorID;
    private Long editingFilmID;
    
    public ClientHandler(int clientNo, MessageProtocol protocol) throws IOException {                      
        this.clientNo = clientNo;
        this.protocol = protocol;
    }

    public Integer getEditingDirectorID() {
        return editingDirectorID;
    }

    public Long getEditingFilmID() {
        return editingFilmID;
    }
        
    public void setEditingDirectorID(Integer editingDirectorID) {
        this.editingDirectorID = editingDirectorID;
    }

    public void setEditingFilmID(Long editingFilmID) {
        this.editingFilmID = editingFilmID;
    }    
    
    private ModelMessage tryToDelayedRead() throws IOException {        
        ModelMessage message = null;
        try {
            message = protocol.receiveMessage();
        } catch (IOException ex) {
        } catch (Exception ex) {
            System.out.println("Клиент " + clientNo + ": Мастер-Обработчик: Получено сообщение неизвестного формата");
        }
        return message;
    }
    
    @Override
    public void run() {
        while (true) {  
            //Check new tasks
            try {
                ModelMessage message = tryToDelayedRead();
                if (message != null) {
                    if (message.getType() == ModelMessage.MessageType.FINISH_SESSION) {
                        System.out.println("Клиент " + clientNo + ": Отключён");
                        break;
                    } else {
                        ModelMessage response = ModelHandler.getInstance().handleMessage(clientNo, message);
                        protocol.sendMessage(response);
                    }
                }
            } catch (IOException ex) {
                System.out.println("Клиент " + clientNo + ": Мастер-Обработчик: Ошибка соединения. Клиент отключён");
                break;
            } catch (Exception ex) {
                Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
                break;
            }
            //Check interruption
            if (Thread.currentThread().isInterrupted()) {
                System.out.println("Клиент " + clientNo + ": Мастер-Обработчик: Принудительно отключён клиент");
                break;
            }
        }
        finilizeHandler();
    }
    
    
    private void finilizeHandler() {
        ModelHandler.getInstance().removeClient(clientNo);        
        try {
            protocol.close();
        } catch (IOException ex) {
            System.out.println("Клиент " + clientNo + ": Мастер-Обработчик: Ошибка закрытия сокета модификации");
        }
    }
}
