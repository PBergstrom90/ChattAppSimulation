package ChatAppTCP;

import javax.swing.*;
import java.io.*;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.*;

public class ServerListener implements Runnable {
    private final Socket socket;
    private final Server server;
    private final ServerGUI gui;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private User user;

    public ServerListener(Socket socket, Server server, ServerGUI gui) {
        this.socket = socket;
        this.server = server;
        this.gui = gui;
    }

    @Override
    public void run() {
        try {
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
            gui.appendStatus("SERVER LISTENING ADDRESS: " + socket.getInetAddress() + "\n");

            // Read the User object sent by the client
            user = (User) in.readObject();
            synchronized (User.userList) {
                User.userList.add(user);
            }
            server.broadcastMessage("ADMIN::CONNECTED::" + user.getName() + "\n");

            Object received;
            while ((received = in.readObject()) != null) {
                if (received instanceof String message) {
                    // Process the "Ping" message sent by newly connected users, to update the user list.
                    if (message.equals("PING")) {
                        synchronized (User.userList) {
                            for (User u : User.userList) {
                                // Connected users send a reply, to confirm that they are online.
                                sendMessage("ADMIN::ALIVE::" + u.getName() + "\n");
                            }
                        }
                    } else {
                        server.broadcastMessage(user.getName() + ": " + message + "\n");
                    }
                }
            }
        } catch (EOFException e) {
            gui.appendStatus("Client has disconnected: " + user.getName() + "\n");
        }
        catch (SocketException e) {
            gui.appendStatus("Socket closed for Client: " + user.getName() + "\n");
        }
        catch (IOException | ClassNotFoundException e) {
            gui.appendStatus("IO Error for user: " + user.getName() + "\n");
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
            gui.appendStatus("Error sending message: " + message + "\n");
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
            if(!(user == null)) {
                gui.appendStatus("Client Handler closed for user: " + user.getName() + "\n");
            } else {
                gui.appendStatus("Client Handler closed\n");
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}

