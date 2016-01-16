package forms;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import model.communication.protocol.MessageProtocol;
import model.communication.ModelHandler;
import model.communication.UpdateInvoker;

public abstract class Server {
    
    private final int updatePort;
    private final int modifyPort;

    public Server(int updatePort, int modifyPort) {
        this.updatePort = updatePort;
        this.modifyPort = modifyPort;
    }

    public void start() {
        try {
            int clientNo = 0;
            ServerSocket updateServerSocket = new ServerSocket(updatePort);
            ServerSocket modifyServerSocket = new ServerSocket(modifyPort);
            System.out.println("Сервер запущен");
            updateServerSocket.setSoTimeout(10000);
            modifyServerSocket.setSoTimeout(10000);
            ModelHandler clientsMap = ModelHandler.getInstance();
            UpdateInvoker updateInvoker = UpdateInvoker.getInstance();
            while (true) {
                try {
                    Socket updateSocket = updateServerSocket.accept();
                    Socket modifySocket = modifyServerSocket.accept();
                    if (updateSocket != null && modifySocket != null) {
                        if (!updateSocket.getInetAddress().equals(
                                modifySocket.getInetAddress())) {
                            System.out.println("Клиент " + clientNo + ": IP-адреса сокетов не совпадают");
                            throw new IOException();
                        }
                        MessageProtocol updateProtocol = createProtocol(updateSocket);
                        updateInvoker.addClient(clientNo, updateProtocol);
                        modifySocket.setSoTimeout(100);
                        MessageProtocol modifyProtocol = createProtocol(modifySocket);
                        clientsMap.addClient(clientNo, modifyProtocol);
                        System.out.println("Клиент " + clientNo + ": Присоединён");
                    }
                } catch (SocketTimeoutException ex) {
                } catch (IOException ex) {
                    ex.printStackTrace();
                    System.out.println("Клиент " + clientNo + ": Не удалось установить соединение");
                }
                clientNo++;
            }
        } catch (IOException ex) {
            System.out.println("Не удалось создать сокет");
        }
    }

    public MessageProtocol createProtocol(Socket socket) throws IOException {
        return createProtocol(socket.getInputStream(), socket.getOutputStream());
    }
    
    public abstract MessageProtocol createProtocol(InputStream in, OutputStream out) throws IOException;
}
