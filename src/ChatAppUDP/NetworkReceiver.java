package ChatAppUDP;

import javax.swing.*;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.net.NetworkInterface;

public class NetworkReceiver extends Thread {
    private GUI gui;// Reference to the GUI
    private User user;
    private static NetworkReceiver instance;
    private final MulticastSocket socket;
    private final InetAddress iAdr;
    private final InetSocketAddress group;
    private final NetworkInterface netIf;
    private NetworkSender networkSender;
    private byte[] buffer;
    public boolean isRunning = true;

    public NetworkReceiver() throws IOException {
        String ip = "234.235.236.237";
        int port = 8000;
        socket = new MulticastSocket(port);
        iAdr = InetAddress.getByName(ip);
        group = new InetSocketAddress(iAdr, port);
        netIf = NetworkInterface.getByName("wlan2");
        socket.joinGroup(group, netIf);
        networkSender = new NetworkSender(user, gui);
        buffer = new byte[1024];
    }

    public static synchronized NetworkReceiver getInstance() throws IOException {
        if (instance == null) {
            instance = new NetworkReceiver();
        }
        return instance;
    }

    public void setGui(GUI gui) {
        this.gui = gui; // Set the reference to the GUI
    }

    public void run() {
        try {
            while (isRunning) {
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);
                // Convert the received data to a string message
                String message = new String(packet.getData(), 0, packet.getLength());

                // Process the message, check if it's an Admin-message or a User-message
                if (message.startsWith("ADMIN::")) {
                    processAdminMessage(message);
                } else {
                    // Post User-message in the GUI.
                    gui.updateChatArea(message);
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "ERROR: " + e.getMessage());
            e.printStackTrace();
        } finally {
            socket.close();
        }
    }

    private void processAdminMessage(String message) {
        String[] parts = message.split("::");
        if (parts.length < 3) {
            return; // Invalid message format
        }
        String command = parts[1];
        String userName = parts[2];
        switch (command) {
            case "CONNECTED":
                handleUserConnected(userName);
                break;
            case "DISCONNECTED":
                handleUserDisconnected(userName);
                break;
            case "RECEIVED":
                handleUserReceived(userName);
            break;
            default:
                System.err.println("Unknown admin command: " + command);
                break;
        }
    }

    private void handleUserConnected(String userName) {
        User newUser = new User(userName, true);
        User.userList.add(newUser);
        gui.updateChatArea("--- " + newUser.getName() + ": CONNECTED ---\n");
        updateMembersArea();
        try {
            // Send a confirmation to other connected users, that the newly connected user has been added.
            networkSender.sendAdminReceivedMessage(newUser);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "ERROR: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void handleUserDisconnected(String userName) {
        User userToRemove = null;
        for (User user : User.userList) {
            if (user.getName().equals(userName)) {
                userToRemove = user;
                user.setActive(false);
                break;
            }
        }
        if (userToRemove != null && !userToRemove.isActive()) {
            User.userList.remove(userToRemove);
            updateMembersArea();
            gui.updateChatArea("--- " + userToRemove.getName() + ": DISCONNECTED ---\n");
        }
    }

    private void handleUserReceived(String userName) {
        User user = new User(userName, true);
        if (!User.userList.contains(user)) {
                User.userList.add(user);
            }
        System.out.println("Sent CALLOUT");
        updateMembersArea();
    }

    private void updateMembersArea() {
        StringBuilder members = new StringBuilder();
        for (User user : User.userList) {
            members.append(user.getName()).append("\n");
        }
        gui.updateMembersArea(members.toString());
    }
}