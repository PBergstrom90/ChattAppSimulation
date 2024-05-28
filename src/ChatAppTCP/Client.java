package ChatAppTCP;

import javax.swing.*;
import java.io.*;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class Client {
    private final Socket socket;
    private final ObjectOutputStream out;
    private final ObjectInputStream in;
    private final User user;
    private final ClientGUI gui;
    private final List<String> clientUserList = new ArrayList<>();

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
                // Send a "PING" message to get the list of active users
                out.writeObject("PING");
                out.flush();
                // Listen for messages from the server
                Object received;
                while ((received = in.readObject()) != null) {
                    if (received instanceof String) {
                        processMessage((String) received);
                    }
                }
            } catch (SocketException e) {
                gui.updateChatArea("SOCKET CLOSED for user: " + user.getName());
            }
            catch (IOException | ClassNotFoundException e) {
                JOptionPane.showMessageDialog(null, "ERROR: " + e.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
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
            // If the message is a regular message, update the chatGUI.
            gui.updateChatArea(message);
        }
    }

    private void processAdminMessage(String message) {
        String[] parts = message.split("::");
        if (parts.length == 3) {
            String username = parts[2];
            String status = parts[1];
            switch (status) {
                case "CONNECTED" -> SwingUtilities.invokeLater(() -> {
                    gui.updateChatArea("CONNECTED: " + username);
                    clientUserList.add(username);
                    gui.updateMembersArea(username);
                });
                case "DISCONNECTED" -> SwingUtilities.invokeLater(() -> {
                    gui.updateChatArea("DISCONNECTED: " + username);
                    clientUserList.removeIf(user -> user.equals(username));
                    gui.resetMembersArea();
                    for (String user : clientUserList) {
                        gui.updateMembersArea(user);
                    }
                });
                case "ALIVE" -> SwingUtilities.invokeLater(() -> {
                    List<String> tempUserList = new ArrayList<>();
                    tempUserList.add(username);
                    for (String user : clientUserList) {
                        if (!username.equalsIgnoreCase(user)) {
                            tempUserList.add(user);
                        }
                    }
                    clientUserList.clear();
                    clientUserList.addAll(tempUserList);
                    gui.resetMembersArea();
                    for (String user : clientUserList) {
                        gui.updateMembersArea(user);
                    }
                });
            }
        }
    }

    public void sendMessage(String message) {
        try {
            out.writeObject(message);
            out.flush();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "ERROR sending message: " + e.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void close() {
        try {
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "ERROR closing the socket: " + e.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }
}

