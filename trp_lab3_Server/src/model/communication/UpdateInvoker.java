package model.communication;

import model.communication.protocol.ModelMessage;
import model.communication.protocol.MessageProtocol;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.ServerController;

/**
 *
 * @author Айна и Лена
 */
public class UpdateInvoker{
    
    private static UpdateInvoker instance;
    
    private final SortedMap<Integer, MessageProtocol> clients = Collections
            .synchronizedSortedMap(new TreeMap<Integer, MessageProtocol>());
    
    private UpdateInvoker() {
    }     
    
    public static UpdateInvoker getInstance() {
        if (instance == null) {
            instance = new UpdateInvoker();
        }
        return instance;
    }
    
    public void addClient(int clientNo, MessageProtocol protocol) throws IOException  {
        clients.put(clientNo, protocol);
        initClientTables(protocol);;
    }
    
    public void removeClient(int clientNo) {
        MessageProtocol client = clients.remove(clientNo);
        if (client != null) {
            try {
                client.close();
            } catch (IOException ex) {
                System.out.println("Клиент " + clientNo + ": Генератор обновлений: Ошибка закрытия сокета обновления");
            }
        }
    }
    
    public synchronized void invokeUpdate(ModelMessage message) {
        for (Map.Entry<Integer, MessageProtocol> client : clients.entrySet()) {
            try {
                client.getValue().sendMessage(message);
            } catch (IOException ex) {
                System.out.println("Клиент " + client.getKey() + ": Генератор обновлений: Ошибка соединения");
                removeClient(client.getKey());
                ModelHandler.getInstance().removeClient(client.getKey());
            } catch (Exception ex) {
                Logger.getLogger(UpdateInvoker.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    private void initClientTables(MessageProtocol protocol) throws IOException {
        ModelMessage updateAllDirectors = new ModelMessage(
                ModelMessage.MessageType.UPDATE, 
                ModelMessage.EntityTarget.DIRECTOR,
                ServerController.getInstance().getDirectorsData());
        ModelMessage updateAllFilms = new ModelMessage(
                ModelMessage.MessageType.UPDATE, 
                ModelMessage.EntityTarget.FILM,
                ServerController.getInstance().getFilmsData());
        try {
            protocol.sendMessage(updateAllDirectors);
            protocol.sendMessage(updateAllFilms);
        } catch(IOException e) {
            throw e;
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }    
}
