package ChatApp;

import javax.swing.*;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;


public class Main {
    
    public static void main(String[] args) {
        List<User> userList = new ArrayList<>();
        Main main = new Main();
        // Ensure GUI creation and display runs on the Event Dispatch Thread (EDT)
        SwingUtilities.invokeLater(() -> {
            String name = JOptionPane.showInputDialog(null, "Enter your name: ");
            String ipAddress;
            try {ipAddress = main.getHostIPAddress();
            } catch (UnknownHostException e) {
                throw new RuntimeException(e);
            }
            boolean active = true;
            User user = new User(name, ipAddress, active);
            userList.add(user);
            GUI gui;
            try {
                gui = new GUI(user);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            try {
                NetworkSender networkSender = new NetworkSender(gui, user);
                gui.setNetworkSender(networkSender);
            }
            catch (IOException e) {
                JOptionPane.showMessageDialog(null, e);
                e.printStackTrace();
            }
            gui.setVisible(true);
            userList.stream().map(User::getName).forEach(gui::updateMembersArea);
        });
    }

    public String getHostIPAddress() throws UnknownHostException {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            throw new RuntimeException("Failed to get host IP address", e);
        }
    }
}