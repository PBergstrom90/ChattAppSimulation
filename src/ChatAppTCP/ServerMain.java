package ChatAppTCP;

import javax.swing.*;

public class ServerMain {

    // Start this instance first, before ClientMain
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ServerGUI serverGUI = new ServerGUI();
            serverGUI.setVisible(true);
        });
    }
}
