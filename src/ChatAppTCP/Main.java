package ChatAppTCP;

import javax.swing.*;
import java.io.IOException;
import java.net.Socket;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            String name = JOptionPane.showInputDialog(null, "Enter your name: ");
            User user = new User(name);
            ClientGUI clientGUI;
            try {
                clientGUI = new ClientGUI(user);
                clientGUI.setVisible(true);

                Socket socket = new Socket("localhost", 8000); // Connect to the server
                Client client = new Client(socket, clientGUI, user);
                clientGUI.setClient(client);
                client.start();

            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "ERROR: " + e.getMessage());
            }
        });
    }
}