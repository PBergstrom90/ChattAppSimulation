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
    private final User user;
    private final MulticastSocket socket;
    private static NetworkReceiver instance;
    private final NetworkSender networkSender;
    private final byte[] buffer;
    public static boolean isRunning = true;

    public NetworkReceiver(User user) throws IOException {
        this.user = user;
        String ip = "234.235.236.237";
        int port = 8000;
        this.socket = new MulticastSocket(port);
        InetAddress iAdr = InetAddress.getByName(ip);
        InetSocketAddress group = new InetSocketAddress(iAdr, port);
        NetworkInterface netIf = NetworkInterface.getByName("wlan2");
        socket.joinGroup(group, netIf);
        networkSender = new NetworkSender(user);
        buffer = new byte[1024];
    }

    public static synchronized NetworkReceiver getInstance(User user) throws IOException {
        // Singleton pattern, to ensure that only one instance of NetworkReceiver is created. As multiple receivers will cause issues.
        if (instance == null) {
            instance = new NetworkReceiver(user);
        }
        return instance;
    }

    public void setGui(GUI gui) {
        this.gui = gui; // Set the reference to the GUI
    }

    public static void setAppRunning(boolean running) {
        isRunning = running;
    }

    public void run() {
        try {
            // Network receiver keeps running during the entire runtime, in order to process incoming messages.
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
        } finally {
            // When the network receiver is done, close the socket.
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        }
    }

    private void processAdminMessage(String message) {
        String[] parts = message.split("::");
        if (parts.length < 3) {
            return; // Invalid message format. Return.
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
        User newUser = new User(userName);
        if(!User.userList.contains(newUser)) {
            User.userList.add(newUser);
            updateMembersArea();
        }
        gui.updateChatArea("--- " + newUser.getName() + ": CONNECTED ---\n");
        try {
            // Send a confirmation to other connected users, that the newly connected user has been added.
            networkSender.sendAdminReceivedMessage(user);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "ERROR: " + e.getMessage());
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
        boolean userExists = false;
        for (User user : User.userList) {
            if (user.getName().equals(userName)) {
                userExists = true;
                break;
            }
        }
        if (!userExists) {
            User newUser = new User(userName);
            User.userList.add(newUser);
            updateMembersArea();
        }
    }

    private void updateMembersArea() {
        StringBuilder members = new StringBuilder();
        for (User user : User.userList) {
            members.append(user.getName()).append("\n");
            gui.updateMembersArea(members.toString());
        }
    }
}