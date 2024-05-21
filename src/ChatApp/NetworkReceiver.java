package ChatApp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.net.NetworkInterface;

public class NetworkReceiver implements Runnable {
    private GUI gui; // Reference to the GUI
    private static NetworkReceiver instance;
    MulticastSocket socket;
    InetAddress iAdr;
    InetSocketAddress group;
    NetworkInterface netIf;
    byte[] buffer;

    public NetworkReceiver() throws IOException {
        String ip = "234.235.236.237";
        int port = 8000;
        socket = new MulticastSocket(port);
        iAdr = InetAddress.getByName(ip);
        group = new InetSocketAddress(iAdr, port);
        netIf = NetworkInterface.getByName("wlan2");
        socket.joinGroup(group, netIf);
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

    @Override
    public void run() {
        boolean isRunning = true;
        try {
            while (isRunning) {
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);
                // Convert the received data to a string message
                String message = new String(packet.getData(), 0, packet.getLength());
                // Process the message (e.g., add user to active users)
                gui.updateChatArea(message);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        socket.close();
    }
}