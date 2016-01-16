package model.communication;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Collections;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import model.ServerController;

/**
 *
 * @author Айна и Лена
 */
public class UpdateInvoker{
    
    private static UpdateInvoker instance;
    
    private final SortedMap<Integer, Socket> sockets = Collections.synchronizedSortedMap(new TreeMap<Integer, Socket>());
    private final SortedMap<Integer, ObjectOutputStream> streams = Collections.synchronizedSortedMap(new TreeMap<Integer, ObjectOutputStream>());
    
    private UpdateInvoker() {
    }     
    
    public static UpdateInvoker getInstance() {
        if (instance == null) {
            instance = new UpdateInvoker();
        }
        return instance;
    }
    
    public void addClient(int clientNo, Socket updateSocket) throws IOException  {
        sockets.put(clientNo, updateSocket);
        ObjectOutputStream outStream = new ObjectOutputStream(updateSocket.getOutputStream());
        outStream.flush();
        initClientTables(outStream);
        streams.put(clientNo, outStream);
    }
    
    public void removeClient(int clientNo) {
        Socket client = sockets.get(clientNo);
        if (client != null) {
            sockets.remove(clientNo);
            streams.remove(clientNo);
            try {
                client.close();
            } catch (IOException ex) {
                System.out.println("Клиент " + clientNo + ": Генератор обновлений: Ошибка закрытия сокета обновления");
            }
        }
    }
    
    public synchronized void invokeUpdate(ModelMessage message) {
        for (Map.Entry<Integer, ObjectOutputStream> entryStream : streams.entrySet()) {
            try {
                entryStream.getValue().writeObject(message);
            } catch (IOException ex) {
                System.out.println("Клиент " + entryStream.getKey() + ": Генератор обновлений: Ошибка соединения");
                removeClient(entryStream.getKey());
                ModelHandler.getInstance().removeClient(entryStream.getKey());
            }
        }
    }
    
    private void initClientTables(ObjectOutputStream outStream) throws IOException {
        ModelMessage updateAllDirectors = new ModelMessage(
                ModelMessage.MessageType.UPDATE, 
                ModelMessage.EntityTarget.DIRECTOR,
                ServerController.getInstance().getDirectorsData());
        outStream.writeObject(updateAllDirectors);
        
        ModelMessage updateAllFilms = new ModelMessage(
                ModelMessage.MessageType.UPDATE, 
                ModelMessage.EntityTarget.FILM,
                ServerController.getInstance().getFilmsData());
        outStream.writeObject(updateAllFilms);
    }    
}
