package ChatAppTCP;

import java.io.*;
import java.net.*;
import java.util.*;

public class Server {
    private final int port = 8000;
    private final Set<ServerListener> clientHandlers = new HashSet<>();
    private final ServerGUI gui;
    private ServerSocket serverSocket;
    protected boolean isRunning = false;

    public Server(ServerGUI gui) {
        this.gui = gui;
    }

    public void start() {
        try {
            serverSocket = new ServerSocket(port);
            isRunning = true;
            gui.appendStatus("SERVER STARTED ON PORT: " + serverSocket.getLocalPort());

            while (isRunning) {
                try {
                    Socket clientSocket = serverSocket.accept();
                    ServerListener clientHandler = new ServerListener(clientSocket, this, gui);
                    clientHandlers.add(clientHandler);
                    new Thread(clientHandler).start();
                    gui.appendStatus("Client Handler started");
                } catch (IOException e) {
                    if (isRunning) {
                        gui.appendStatus("ERROR accepting connection: " + e.getMessage());
                    }
                }
            }
        } catch (IOException e) {
            gui.appendStatus("SERVER ERROR: " + e.getMessage());
        }
    }

    public synchronized void broadcastMessage(Object message) {
        for (ServerListener clientHandler : clientHandlers) {
            clientHandler.sendMessage(message);
        }
        gui.appendStatus("BROADCAST: " + message);
    }

    public synchronized void removeClient(ServerListener clientHandler) {
        clientHandlers.remove(clientHandler);
        gui.appendStatus("Client disconnected: " + clientHandler.getUserName());
    }

    public void stop() {
        isRunning = false;
        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
            for (ServerListener clientHandler : clientHandlers) {
                clientHandler.close();
            }
            clientHandlers.clear();
        } catch (IOException e) {
            gui.appendStatus("ERROR stopping server: " + e.getMessage());
        }
        gui.appendStatus("SERVER STOPPED.");
    }
}

