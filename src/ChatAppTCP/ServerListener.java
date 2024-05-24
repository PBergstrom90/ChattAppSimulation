package ChatAppTCP;

import java.io.*;
import java.net.*;

public class ServerListener implements Runnable {
    private final Socket socket;
    private final Server server;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private User user;

    public ServerListener(Socket socket, Server server) {
        this.socket = socket;
        this.server = server;
    }

    @Override
    public void run() {
        try {
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());

            // Read the User object sent by the client
            user = (User) in.readObject();
            User.userList.add(user);
            server.broadcastMessage("ADMIN::CONNECTED::" + user.getName());

            Object received;
            while ((received = in.readObject()) != null) {
                if (received instanceof String) {
                    String message = (String) received;
                    server.broadcastMessage(user.getName() + ": " + message + "\n");
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            close();
            server.removeClient(this);
        }
    }

    public void sendMessage(Object message) {
        try {
            out.writeObject(message);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getUserName() {
        return user.getName();
    }

    public void close() {
        try {
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
            if (user != null) {
                user.setActive(false);
                User.userList.remove(user);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

