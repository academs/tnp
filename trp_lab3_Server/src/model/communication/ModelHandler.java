package model.communication;

import entities.Director;
import entities.Film;
import java.io.IOException;
import java.net.Socket;
import java.util.Collections;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import model.ModelException;
import model.ServerController;

/**
 *
 * @author Айна и Лена
 */
public class ModelHandler {
    
    private static ModelHandler instance;
    
    private final SortedMap<Integer, ClientHandler> clients = Collections.synchronizedSortedMap(new TreeMap<Integer, ClientHandler>());
    
    private ModelHandler() {        
    }
    
    public static ModelHandler getInstance() {
        if (instance == null) {
            instance = new ModelHandler();
        }
        return instance;
    }
    
    public void addClient(int clientNo, Socket modifySocket) throws IOException {
        ClientHandler client = new ClientHandler(clientNo, modifySocket);
        clients.put(clientNo, client);
        client.start();
    }
    
    public void removeClient(int clientNo) {        
        UpdateInvoker.getInstance().removeClient(clientNo);
        ClientHandler client = clients.get(clientNo);        
        if (client != null) {
            clients.remove(clientNo);
            client.interrupt();
        }
    }   
      
    public synchronized ModelMessage handleMessage(int clientNo, ModelMessage message) {
        ModelMessage response = null;
        switch (message.getType()) {
            case CREATE:
                response = createEntity(clientNo, message);
                break;
            case START_EDIT:
                response = startEditEntity(clientNo, message);
                break;
            case STOP_EDIT:
                response = stopEditEntity(clientNo, message);
                break;
            case DELETE:
                response = deleteEntity(clientNo, message);
                break;
            case SAVE_MODEL:
                response = saveModel(clientNo);
                break;
            case LOAD_MODEL:
                response = loadModel(clientNo);
                break;
        }
        return response;
    }

    private ModelMessage createEntity(int clientNo, ModelMessage message) {
        ModelMessage response;
        try {
            ServerController.getInstance().addEntity(message);
            response = new ModelMessage(ModelMessage.MessageType.REPSONSE_ALLOWED, message.getTarget(), null);
            UpdateInvoker.getInstance().invokeUpdate(message);
            System.out.println("Клиент " + clientNo + ": Запись добавлена");
        } catch (ModelException ex) {
            response = new ModelMessage(ModelMessage.MessageType.REPSONSE_DENIED, message.getTarget(), ex.getMessage());
            System.out.println("Клиент " + clientNo + ": Добавление записи отклонено: " + ex.getMessage());
        }
        return response;
    }

    private ModelMessage startEditEntity(int clientNo, ModelMessage message) {
        ModelMessage response = null;
        try {
            if (message.getTarget() == ModelMessage.EntityTarget.DIRECTOR) {
                Director director = ServerController.getInstance().getDirector((Integer) message.getData());
                //Try get lock on editing Director
                if (tryLockDirector(director, clientNo))      
                    response = new ModelMessage(ModelMessage.MessageType.REPSONSE_ALLOWED, message.getTarget(), director.getData());
            } else {
                Film film = ServerController.getInstance().getFilm((Long) message.getData());
                //Try get lock on editing Film
                if (tryLockFilm(film, clientNo))
                    response = new ModelMessage(ModelMessage.MessageType.REPSONSE_ALLOWED, message.getTarget(), film.getData());
            }
            
            if (response != null) {                
                System.out.println("Клиент " + clientNo + ": Разрешено редактирование записи");
            } 
            else throw new ModelException();
            
        } catch (ModelException ex) {
            response = new ModelMessage(ModelMessage.MessageType.REPSONSE_DENIED, message.getTarget(), "Редактирование запрещено");
            System.out.println("Клиент " + clientNo + ": Запрещено редактирование записи");
        }        
        return response;
    }
    
    private ModelMessage stopEditEntity(int clientNo, ModelMessage message) {
        ModelMessage response;
        if (message.getData() != null) {
            try {
                ServerController.getInstance().editEntity(message);
                UpdateInvoker.getInstance().invokeUpdate(message);
                if (message.getTarget() == ModelMessage.EntityTarget.DIRECTOR) {
                    //Update all Director's Films - field "Director"="Title [ID=DirectorID]"
                    for (Film film : ((Director)ServerController.getInstance().getDirector((Integer)((Object[])message.getData())[0])).getFilmCollection()) {
                        UpdateInvoker.getInstance().invokeUpdate(new ModelMessage(ModelMessage.MessageType.STOP_EDIT, ModelMessage.EntityTarget.FILM, film.getData()));
                    }
                }
                response = new ModelMessage(ModelMessage.MessageType.REPSONSE_ALLOWED, message.getTarget(), null);
                System.out.println("Клиент " + clientNo + ": Редактирование записи завершено");
            } catch (ModelException ex) {
                response = new ModelMessage(ModelMessage.MessageType.REPSONSE_DENIED, message.getTarget(), "Редактирование не удалось");
                System.out.println("Клиент " + clientNo + ": Редактирование записи не удалось");
            }
        } else {
            response = new ModelMessage(ModelMessage.MessageType.REPSONSE_ALLOWED, message.getTarget(), null);
            System.out.println("Клиент " + clientNo + ": Редактирование записи отменено клиентом");
        }
        //Pull off lock
        ClientHandler client = clients.get(clientNo);
        if (message.getTarget() == ModelMessage.EntityTarget.DIRECTOR) {            
            client.setEditingDirectorID(null);
        } else {
            client.setEditingFilmID(null);
        }
        
        return response;
    }
    
    private ModelMessage deleteEntity(int clientNo, ModelMessage message) {
        ModelMessage response;
        ClientHandler client = clients.get(clientNo);
        try {
            if (message.getTarget() == ModelMessage.EntityTarget.DIRECTOR) {
                Director director = ServerController.getInstance().getDirector((Integer) message.getData());
                //Try get lock on deleting Director
                if (isDirectorNotHandled(director)) {
                    ServerController.getInstance().removeEntity(message);
                    response = new ModelMessage(ModelMessage.MessageType.REPSONSE_ALLOWED, message.getTarget(), null);
                    UpdateInvoker.getInstance().invokeUpdate(message);
                    //Invoke deleting all Director's Films
                    for (Film film : director.getFilmCollection()) {
                        UpdateInvoker.getInstance().invokeUpdate(new ModelMessage(ModelMessage.MessageType.DELETE, ModelMessage.EntityTarget.FILM, film.getIdFilm()));
                    }
                    System.out.println("Клиент " + clientNo + ": Запись удалена");
                } else {
                    throw new ModelException();
                }
            } else {
                Film film = ServerController.getInstance().getFilm((Long) message.getData());
                //Try get lock on deleting Film
                if (tryLockFilm(film, clientNo)) {
                    ServerController.getInstance().removeEntity(message);
                    client.setEditingFilmID(null);
                    response = new ModelMessage(ModelMessage.MessageType.REPSONSE_ALLOWED, message.getTarget(), null);
                    UpdateInvoker.getInstance().invokeUpdate(message);
                    System.out.println("Клиент " + clientNo + ": Запись удалена");
                } else {
                    throw new ModelException();
                }
            }
        } catch (ModelException ex) {
            response = new ModelMessage(ModelMessage.MessageType.REPSONSE_DENIED, message.getTarget(), "Удаление запрещено");
            System.out.println("Клиент " + clientNo + ": Запрещено удаление компании");
        }
        return response;
    }
    
    private ModelMessage saveModel(int clientNo) {
        ModelMessage response;
        try {
            ServerController.getInstance().saveToFile();
            response = new ModelMessage(ModelMessage.MessageType.REPSONSE_ALLOWED, null, null);
            System.out.println("Клиент " + clientNo + ": Модель сохранена");
        } catch (ModelException ex) {
            response = new ModelMessage(ModelMessage.MessageType.REPSONSE_DENIED, null, "Сохранение модели отклонено");
            System.out.println("Клиент " + clientNo + ": Сохранение модели отклонено: " + ex.getMessage());
        }
        return response;
    }
    
    private ModelMessage loadModel(int clientNo) {
        ModelMessage response;
        try {
            //Check model using
            if (isModelNotHandled()) {
                ServerController.getInstance().loadFromFile();
                response = new ModelMessage(ModelMessage.MessageType.REPSONSE_ALLOWED, null, null);
                UpdateInvoker.getInstance().invokeUpdate(new ModelMessage(ModelMessage.MessageType.UPDATE, 
                        ModelMessage.EntityTarget.DIRECTOR, ServerController.getInstance().getDirectorsData()));
                UpdateInvoker.getInstance().invokeUpdate(new ModelMessage(ModelMessage.MessageType.UPDATE, 
                        ModelMessage.EntityTarget.FILM, ServerController.getInstance().getFilmsData()));
                System.out.println("Клиент " + clientNo + ": Модель загружена");
            }
            else throw new ModelException();
        } catch (ModelException ex) {
            response = new ModelMessage(ModelMessage.MessageType.REPSONSE_DENIED, null, "Загрузка модели отклонена");
            System.out.println("Клиент " + clientNo + ": Загрузка модели отклонена: " + ex.getMessage());
        }
        return response;
    }
    
    private boolean isModelNotHandled() {
        for (Map.Entry<Integer, ClientHandler> entry : clients.entrySet()) {
            if ((entry.getValue().getEditingDirectorID() != null) ||
                    (entry.getValue().getEditingFilmID() != null))
                return false;
        }
        return true;
    }
    
    private boolean isDirectorNotHandled(Director director) {
        for (Map.Entry<Integer, ClientHandler> entry : clients.entrySet()) {
            if (director.getIdDirector().equals(entry.getValue().getEditingDirectorID())) {                
                return false;
            }
            for (Film film : director.getFilmCollection()) {
                if (film.getIdFilm().equals(entry.getValue().getEditingFilmID()))
                    return false;
            }
        }
        return true;
    }

    private boolean tryLockDirector(Director director, int clientNo) {
        ClientHandler client = clients.get(clientNo);
        for (Map.Entry<Integer, ClientHandler> entry : clients.entrySet()) {
            if (director.getIdDirector().equals(entry.getValue().getEditingDirectorID())) {                 
                return false;
            }
        }
        client.setEditingDirectorID(director.getIdDirector());
        return true;
    }

    private boolean tryLockFilm(Film film, int clientNo) {
        ClientHandler client = clients.get(clientNo);
        for (Map.Entry<Integer, ClientHandler> entry : clients.entrySet()) {
            if (film.getIdFilm().equals(entry.getValue().getEditingFilmID())) {                    
                return false;
            }
        }
        client.setEditingFilmID(film.getIdFilm());
        return true;
    }
}
