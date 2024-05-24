package ChatAppTCP;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import static java.awt.Color.GREEN;
import static java.awt.Color.RED;

public class ServerGUI extends JFrame implements WindowListener, ActionListener {
    private final JTextArea statusArea = new JTextArea();
    private final JButton startButton = new JButton("Start Server");
    private final JButton stopButton = new JButton("Stop Server");
    private Server server;

    public ServerGUI() {
        createServerGUI();
    }

    private void createServerGUI() {
        this.setTitle("Server Control Panel");
        this.setSize(400, 300);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        startButton.addActionListener(this);
        stopButton.addActionListener(this);

        JPanel mainPanel = new JPanel(new BorderLayout());
        this.add(mainPanel);

        stopButton.setEnabled(false);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(startButton);
        buttonPanel.add(stopButton);

        mainPanel.add(buttonPanel, BorderLayout.NORTH);

        statusArea.setEditable(false);
        mainPanel.add(new JScrollPane(statusArea), BorderLayout.CENTER);
    }

    public void appendStatus(String status) {
        SwingUtilities.invokeLater(() -> statusArea.append(status + "\n"));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == startButton) {
            if (server == null || !server.isRunning) {
                server = new Server(ServerGUI.this);
                new Thread(() -> server.start()).start();
                startButton.setEnabled(false);
                startButton.setBackground(GREEN);
                stopButton.setEnabled(true);
                stopButton.setBackground(null);
            }
        }
        if (e.getSource() == stopButton) {
            if (server != null && server.isRunning) {
                server.stop();
                startButton.setEnabled(true);
                startButton.setBackground(null);
                stopButton.setEnabled(false);
                stopButton.setBackground(RED);
            }
        }
    }

    @Override
    public void windowOpened(WindowEvent e) {

    }

    @Override
    public void windowClosing(WindowEvent e) {
        JOptionPane.showMessageDialog(null, "Server closing");
        server.stop();
        System.exit(0);
    }

    @Override
    public void windowClosed(WindowEvent e) {

    }

    @Override
    public void windowIconified(WindowEvent e) {

    }

    @Override
    public void windowDeiconified(WindowEvent e) {

    }

    @Override
    public void windowActivated(WindowEvent e) {

    }

    @Override
    public void windowDeactivated(WindowEvent e) {

    }
}

