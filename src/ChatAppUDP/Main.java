package ChatAppUDP;

import javax.swing.*;
import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        // Ensure GUI creation and display runs on the Event Dispatch Thread (EDT)
        SwingUtilities.invokeLater(() -> {
            String name = JOptionPane.showInputDialog(null, "Enter your name: ");
            User user = new User(name);
            GUI gui;
            try {
                gui = new GUI(user);
                gui.setVisible(true);

                NetworkSender networkSender = new NetworkSender(user);
                gui.setNetworkSender(networkSender);

                NetworkReceiver networkReceiver = NetworkReceiver.getInstance(user);
                networkReceiver.setGui(gui);
                // Start the receiver thread to listen for incoming messages.
                new Thread(networkReceiver).start();

                // Send a multicast message, to notify other members.
                networkSender.sendAdminConnectedMessage(user);
            }
            catch (IOException e) {
                JOptionPane.showMessageDialog(null, "ERROR: " + e.getMessage());
            }
        });
    }
}