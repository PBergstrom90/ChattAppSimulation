package ChatAppUDP;

import javax.swing.*;
import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        // Ensure GUI creation and display runs on the Event Dispatch Thread (EDT)
        SwingUtilities.invokeLater(() -> {
            String name = JOptionPane.showInputDialog(null, "Enter your name: ");
            boolean active = true;
            User user = new User(name, active);
            GUI gui;
            try {
                gui = new GUI(user);
                gui.setVisible(true);
                NetworkSender networkSender = new NetworkSender(user, gui);
                networkSender.sendAdminConnectedMessage(user);
                gui.setNetworkSender(networkSender);
            }
            catch (IOException e) {
                JOptionPane.showMessageDialog(null, "ERROR: " + e.getMessage());
                e.printStackTrace();
            }
        });
    }
}