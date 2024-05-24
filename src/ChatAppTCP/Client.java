package ChatAppTCP;

import java.io.*;
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
        try {
            // Send the User object to the server
            out.writeObject(user);
            out.flush();

            // Listen for messages from the server
            Object received;
            while ((received = in.readObject()) != null) {
                if (received instanceof String) {
                    String message = (String) received;
                    gui.updateChatArea(message); // Update GUI with received message
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            close();
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

