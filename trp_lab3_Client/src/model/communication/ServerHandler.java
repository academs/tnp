package model.communication;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Айна и Лена
 */
public class ServerHandler{
    
    private static ServerHandler instance;
        
    private static List<ActionListener> closeConnectionListeners = Collections.synchronizedList(new ArrayList<ActionListener>());
    private static List<ActionListener> updateListeners = Collections.synchronizedList(new ArrayList<ActionListener>());
        
    private final UpdateListener updateListener;
    
    private final Socket updateSocket;
    private final Socket modifySocket;
    private final ObjectOutputStream outStream;
    private final ObjectInputStream inStream;
    
    private ServerHandler(String serverHost) throws IOException {
        this.updateSocket = new Socket();
        this.modifySocket = new Socket();
        updateSocket.connect(new InetSocketAddress(serverHost, 6666), 10000);
        modifySocket.connect(new InetSocketAddress(serverHost, 7777), 10000);        
        
        this.modifySocket.setSoTimeout(10*1000);
        this.outStream = new ObjectOutputStream(modifySocket.getOutputStream());
        this.outStream.flush();
        this.inStream = new ObjectInputStream(modifySocket.getInputStream());
        
        updateListener = new UpdateListener(updateSocket);
    }
    
    public static ServerHandler createInstance(String serverHost) throws IOException {
        if (instance != null) {            
            instance.stopHandler();
        }
        instance = new ServerHandler(serverHost);
        instance.updateListener.start();
        return instance;
    }
    
    public static ServerHandler getInstance() {
        return instance;
    }
    
    public static void addCloseConnectionListener(ActionListener listener) {
        closeConnectionListeners.add(listener);
    }

    public static void addUpdateListener(ActionListener listener) {
        updateListeners.add(listener);
    }
    
    private void invokeCloseConnection(String str) {
        for (int i = 0; i < closeConnectionListeners.size(); i++) {
            closeConnectionListeners.get(i).actionPerformed(new ActionEvent(str,0,null));
        }
    }
    
    private void invokeUpdate(ModelMessage message) {
        for (int i = 0; i < updateListeners.size(); i++) {
            updateListeners.get(i).actionPerformed(new ActionEvent(message, 0, null));
        }
    }
    
    public void stopHandler() {
        updateListener.interrupt();
        try {
            updateListener.join();
        } catch (InterruptedException ex) {
            System.out.println("Мастер-Обработчик: Ошибка завершения обработчика");
        }
    }
    
    public ModelMessage sendRespMessage(ModelMessage message) {
        ModelMessage response = null;
        try {
            outStream.writeObject(message);
            response = (ModelMessage)inStream.readObject();
        } catch (IOException ex) {
            System.out.println("Мастер-Обработчик: Ошибка соединения с сервером");
        } catch (ClassNotFoundException ex) {
            System.out.println("Мастер-Обработчик: Получено сообщение неизвестного формата");
        }
        return response;
    }
    
    private void finalizeConnection() { 
        try {
            outStream.writeObject(new ModelMessage(ModelMessage.MessageType.FINISH_SESSION, null, null));
        } catch (IOException ex) {
            System.out.println("Мастер-Обработчик: Ошибка отправки завершающего сообщения");
        }
        try {
            updateSocket.close();
        } catch (IOException ex) {
            System.out.println("Мастер-Обработчик: Ошибка закрытия сокета обновления");
        }
        try {
            modifySocket.close();
        } catch (IOException ex) {
            System.out.println("Мастер-Обработчик: Ошибка закрытия сокета модификации");
        }        
        
        invokeCloseConnection("Мастер-Обработчик: Соединение закрыто");
    }
    
    private class UpdateListener extends Thread {

        private final Socket updateSocket;
        private final ObjectInputStream inStream;

        public UpdateListener(Socket updateSocket) throws IOException {
            this.updateSocket = updateSocket;
            this.updateSocket.setSoTimeout(1000);

            inStream = new ObjectInputStream(updateSocket.getInputStream());
        }

        private void tryToDelayedRead() throws IOException {
            try {
                ModelMessage message = (ModelMessage) inStream.readObject();
                invokeUpdate(message);
            } catch (SocketTimeoutException ex) {
            } catch (ClassNotFoundException ex) {
                System.out.println("Слушатель обновлений: Получено сообщение неизвестного формата");
            }
        }

        @Override
        public void run() {
            while (true) {
                try {
                    tryToDelayedRead();
                } catch (IOException ex) {
                    System.out.println("Слушатель обновлений: Ошибка соединения с сервером");
                    break;
                }
                if (Thread.currentThread().isInterrupted()) {
                    break;
                }
            }
            finalizeConnection();
        }
    }
}
