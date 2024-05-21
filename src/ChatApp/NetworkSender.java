package ChatApp;

import java.io.IOException;
import java.net.*;

public class NetworkSender {

    String multicastAdress = "234.235.236.237";
    int port = 8000;
    InetAddress group;
    MulticastSocket socket;
    private final GUI gui; // Reference to the GUI
    private final User user; // Reference to the user

    public NetworkSender(GUI gui, User user) throws IOException {
        this.gui = gui;
        this.user = user;
        this.group = InetAddress.getByName(multicastAdress);
        this.socket = new MulticastSocket(port);
    }

    public void sendMessage(String message) {
        try {
            String fullMessage = user.getName()  + ": " + message + "\n";
            byte[] buffer = fullMessage.getBytes();
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length, group, port);
            socket.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}