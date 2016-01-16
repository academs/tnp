package forms;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.Locale;
import model.communication.ModelHandler;
import model.communication.UpdateInvoker;

public class Main {

    public static void main(String args[]) {
        //Initialize parameters
        Locale.setDefault(Locale.ENGLISH);
        
        try {
            int clientNo = 0;
            ServerSocket updateServerSocket = new ServerSocket(6666);
            ServerSocket modifyServerSocket = new ServerSocket(7777);
            System.out.println("Сервер запущен");
            updateServerSocket.setSoTimeout(10000);
            modifyServerSocket.setSoTimeout(10000);
            ModelHandler clientsMap = ModelHandler.getInstance();
            UpdateInvoker updateInvoker = UpdateInvoker.getInstance();
            while(true) {
                try {
                    Socket updateSocket = updateServerSocket.accept();
                    Socket modifySocket = modifyServerSocket.accept();
                    if (updateSocket != null && modifySocket != null) {
                        if (!updateSocket.getInetAddress().equals(
                                modifySocket.getInetAddress())) {
                            System.out.println("Клиент " + clientNo + ": IP-адреса сокетов не совпадают");
                            throw new IOException();
                        }
                        updateInvoker.addClient(clientNo, updateSocket);
                        clientsMap.addClient(clientNo, modifySocket);
                        System.out.println("Клиент " + clientNo + ": Присоединён");
                    }
                } catch (SocketTimeoutException ex) {
                } catch (IOException ex) {
                    System.out.println("Клиент " + clientNo + ": Не удалось установить соединение");
                }
                clientNo++;
            }
        } catch (IOException ex) {
            System.out.println("Не удалось создать сокет");
        }
    }
}
