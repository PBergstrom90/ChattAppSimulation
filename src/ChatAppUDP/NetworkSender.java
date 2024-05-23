package ChatAppUDP;

import javax.swing.*;
import java.io.IOException;
import java.net.*;

public class NetworkSender {

    private final int port = 8000;
    private final InetAddress group;
    private final User user; // Reference to the user

    public NetworkSender(User user) throws IOException {
        this.user = user;
        String multicastAddress = "234.235.236.237";
        this.group = InetAddress.getByName(multicastAddress);
    }

    private void sendMessage(String message) {
    // Start a new thread to send the message asynchronously
        new Thread(() -> {
            // Using try-with-resources to create a new socket with every message.
            // While this approach may lead to increased resource usage, it allows for efficient socket reuse.
            try (MulticastSocket socket = new MulticastSocket(port)){
                byte[] buffer = message.getBytes();
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length, group, port);
                synchronized (this) {
                    socket.send(packet);
                }
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Error sending message: " + e.getMessage());
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
        String fullMessage = "ADMIN::RECEIVED::" + user.getName();
        sendMessage(fullMessage);
    }
}