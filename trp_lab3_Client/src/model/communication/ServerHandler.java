package model.communication;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import model.communication.protocol.BinaryProtocol;
import model.communication.protocol.MessageProtocol;
import model.communication.protocol.ModelMessage;
import model.communication.protocol.XMLProtocol;

/**
 *
 * @author Айна и Лена
 */
public abstract class ServerHandler{
    
    private static ServerHandler instance;
        
    private static List<ActionListener> closeConnectionListeners = Collections.synchronizedList(new ArrayList<ActionListener>());
    private static List<ActionListener> updateListeners = Collections.synchronizedList(new ArrayList<ActionListener>());
        
    private final UpdateListener updateListener;
    private final MessageProtocol modifyProtocol;
    
    private ServerHandler(String serverHost, int updatePort, int modifyPort) throws IOException {
        Socket updateSocket = new Socket();
        Socket modifySocket = new Socket();
        updateSocket.connect(new InetSocketAddress(serverHost, updatePort), 10000);
        modifySocket.connect(new InetSocketAddress(serverHost, modifyPort), 10000);
        
        modifySocket.setSoTimeout(10*1000);
        this.modifyProtocol = createProtocol(modifySocket);
        updateSocket.setSoTimeout(100);
        updateListener = new UpdateListener(createProtocol(updateSocket));
    }
    
    public static ServerHandler createInstance(String serverHost) throws IOException {
        if (instance != null) {            
            instance.stopHandler();
        }
        instance = new ServerHandler(serverHost, 6666, 7777) {
            @Override
            public MessageProtocol createProtocol(InputStream in, OutputStream out) throws IOException {
                return new BinaryProtocol(in, out);
            }            
        };
        instance.updateListener.start();
        return instance;
    }
    
        public static ServerHandler createXMLInstance(String serverHost) throws IOException {
        if (instance != null) {            
            instance.stopHandler();
        }
        instance = new ServerHandler(serverHost, 6667, 7778) {
            @Override
            public MessageProtocol createProtocol(InputStream in, OutputStream out) throws IOException {
                return new XMLProtocol(in, out);
            }            
        };
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
            this.modifyProtocol.sendMessage(message);
            response = (ModelMessage) modifyProtocol.receiveMessage();
        } catch (IOException ex) {
            System.out.println("Мастер-Обработчик: Ошибка соединения с сервером");
        } catch (ClassNotFoundException ex) {
            System.out.println("Мастер-Обработчик: Получено сообщение неизвестного формата");
        } catch (Exception ex) { 
            ex.printStackTrace();
        }
        return response;
    }
    
    private void finalizeConnection() { 
        try {
            modifyProtocol.sendMessage(new ModelMessage(ModelMessage.MessageType.FINISH_SESSION, null, null));
        } catch (IOException ex) {
            System.out.println("Мастер-Обработчик: Ошибка отправки завершающего сообщения");
        }
        try {
            updateListener.dispose();
        } catch (IOException ex) {
            System.out.println("Мастер-Обработчик: Ошибка закрытия сокета обновления");
        }
        try {
            modifyProtocol.close();
        } catch (IOException ex) {
            System.out.println("Мастер-Обработчик: Ошибка закрытия сокета модификации");
        }        
        
        invokeCloseConnection("Мастер-Обработчик: Соединение закрыто");
    }
    
    private class UpdateListener extends Thread {

        private final MessageProtocol updateProtocol;

        public UpdateListener(MessageProtocol updateProtocol) {
            this.updateProtocol = updateProtocol;
        }

        private void tryToDelayedRead() throws IOException {
            try {
                ModelMessage message = updateProtocol.receiveMessage();
                invokeUpdate(message);            
            } catch (ClassNotFoundException ex) {
                System.out.println("Слушатель обновлений: Получено сообщение неизвестного формата");
            } catch (Exception ex) {                
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

        private void dispose() throws IOException {
            updateProtocol.close();
        }
    }
    
    private MessageProtocol createProtocol(Socket s) throws IOException {
        return createProtocol(s.getInputStream(), s.getOutputStream());
    }
    
    public abstract MessageProtocol createProtocol(InputStream in, OutputStream out) throws IOException;
}
