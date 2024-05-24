package ChatAppTCP;

import java.io.*;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.*;

public class Client {
    private final Socket socket;
    private final ObjectOutputStream out;
    private final ObjectInputStream in;
    private final User user;
    private final ClientGUI gui;

    public Client(Socket socket, ClientGUI gui, User user) throws IOException {
        this.socket = socket;
        this.out = new ObjectOutputStream(socket.getOutputStream());
        this.in = new ObjectInputStream(socket.getInputStream());
        this.user = user;
        this.gui = gui;
    }

    public void start() {
        Thread clientThread = new Thread(() -> {
        try {
            // Send the User object to the server
            out.writeObject(user);
            out.flush();
            // Listen for messages from the server
            Object received;
            while ((received = in.readObject()) != null) {
                if (received instanceof String) {
                    processMessage((String) received);
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            close();
        }
        });
        clientThread.start();
    }

    private void processMessage(String message) {
        if (message.startsWith("ADMIN::")) {
            processAdminMessage(message);
        } else {
            gui.updateChatArea(message); // Update GUI with received message
        }
    }

    private void processAdminMessage(String message) {
        String[] parts = message.split("::");
        if (parts.length == 3) {
            String username = parts[2];
            String status = parts[1];
            if (status.equals("CONNECTED")) {
                gui.updateChatArea("CONNECTED: " + username);
                gui.updateMembersArea(username);
            } else if (status.equals("DISCONNECTED")) {
                gui.updateChatArea("DISCONNECTED: " + username);
                gui.updateMembersArea(username);
            }
        }
    }

    public void sendMessage(String message) {
        try {
            out.writeObject(message);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void close() {
        try {
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

