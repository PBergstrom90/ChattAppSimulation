package ChatAppUDP;

import java.io.IOException;
import java.net.*;

public class NetworkSender extends Thread {

    private final String multicastAddress = "234.235.236.237";
    private final int port = 8000;
    private final InetAddress group;
    private final MulticastSocket socket;
    private User user; // Reference to the user
    private GUI gui;

    public NetworkSender(User user, GUI gui) throws IOException {
        this.user = user;
        this.gui = gui;
        this.group = InetAddress.getByName(multicastAddress);
        this.socket = new MulticastSocket(port);
    }

    private void sendMessage(String message) {
        new Thread(() -> {
            try {
                byte[] buffer = message.getBytes();
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length, group, port);
                synchronized (this) {
                    socket.send(packet);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void sendUserMessage(String message) {
        String fullMessage = user.getName() + ": " + message + "\n";
        sendMessage(fullMessage);
    }

    public void sendAdminConnectedMessage(User user) throws IOException {
        String fullMessage = "ADMIN::CONNECTED::" + user.getName();
        sendMessage(fullMessage);
    }

    public void sendAdminDisconnectedMessage(User user) throws IOException {
        String fullMessage = "ADMIN::DISCONNECTED::" + user.getName();
        sendMessage(fullMessage);
    }

    public void sendAdminReceivedMessage(User user) throws IOException {
        StringBuilder members = new StringBuilder();
        for (User u : User.userList) {
            members.append(u.getName()).append(",");
        }
        if (!members.isEmpty()) {
            members.setLength(members.length() - 1); // Remove last comma
        }
        String fullMessage = "ADMIN::RECEIVED::" + user.getName() + "::" + members + "\n";
        sendMessage(fullMessage);
    }
}