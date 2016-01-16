package model.communication;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketTimeoutException;

/**
 *
 * @author Айна и Лена
 */
public class ClientHandler extends Thread{
    
    private final int clientNo;
            
    private final Socket modifySocket;
    private final ObjectOutputStream outStream;
    private final ObjectInputStream inStream;
    
    private Integer editingDirectorID;
    private Long editingFilmID;
    
    public ClientHandler(int clientNo, Socket modifySocket) throws IOException {              
        this.modifySocket = modifySocket;
        this.modifySocket.setSoTimeout(100);
        
        this.clientNo = clientNo;
        
        this.outStream = new ObjectOutputStream(modifySocket.getOutputStream());
        this.outStream.flush();
        this.inStream = new ObjectInputStream(modifySocket.getInputStream());
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
            message = (ModelMessage) inStream.readObject();
        } catch (SocketTimeoutException ex) {
        } catch (ClassNotFoundException ex) {
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
                        outStream.writeObject(response);
                    }
                }
            } catch (IOException ex) {
                System.out.println("Клиент " + clientNo + ": Мастер-Обработчик: Ошибка соединения. Клиент отключён");
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
            modifySocket.close();
        } catch (IOException ex) {
            System.out.println("Клиент " + clientNo + ": Мастер-Обработчик: Ошибка закрытия сокета модификации");
        }
    }
}
