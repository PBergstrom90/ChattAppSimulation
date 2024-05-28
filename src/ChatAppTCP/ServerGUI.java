package ChatAppTCP;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import static java.awt.Color.GREEN;
import static java.awt.Color.RED;

public class ServerGUI extends JFrame implements WindowListener, ActionListener {
    private final JTextArea statusArea = new JTextArea();
    private final JButton startButton = new JButton("Start Server");
    private final JButton stopButton = new JButton("Stop Server");
    private final JButton printButton = new JButton("Print to File");
    private Server server;

    public ServerGUI() {
        createServerGUI();
    }

    private void createServerGUI() {
        this.setTitle("Server Control Panel");
        this.setSize(400, 500);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        startButton.addActionListener(this);
        stopButton.addActionListener(this);
        printButton.addActionListener(this);

        JPanel mainPanel = new JPanel(new BorderLayout());
        this.add(mainPanel);

        stopButton.setEnabled(false);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(startButton);
        buttonPanel.add(stopButton);
        buttonPanel.add(printButton);

        mainPanel.add(buttonPanel, BorderLayout.NORTH);

        statusArea.setEditable(false);
        mainPanel.add(new JScrollPane(statusArea), BorderLayout.CENTER);
    }

    public void appendStatus(String status) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        String timestamp = LocalTime.now().format(formatter);
        String formattedStatus = "[" + timestamp + "] - " + status;
        SwingUtilities.invokeLater(() -> statusArea.append(formattedStatus));
    }

    // Method to handle file writing
    private void writeStatusToFile() {
        LocalDate currentDate = LocalDate.now();
        String formattedDate = currentDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String filename = "server_status_" + formattedDate + ".txt";

        try (PrintWriter writer = new PrintWriter(new FileWriter(filename, true))) {
            writer.println("Date: " + currentDate); // Add the date to the file
            writer.println(statusArea.getText());
            writer.println("---------------------");
            writer.println();
        } catch (IOException e) {
            appendStatus("ERROR: writing status to file");
        }
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
        if (e.getSource() == printButton) { // Handle the print button action
            try {
                writeStatusToFile();
                JOptionPane.showMessageDialog(null, "File saved successfully");
            } catch (Exception err) {
                JOptionPane.showMessageDialog(null, "File Error " + err.getMessage());
            }
        }
    }

    @Override
    public void windowOpened(WindowEvent e) {

    }

    @Override
    public void windowClosing(WindowEvent e) {
        JOptionPane.showMessageDialog(null, "SERVER CLOSING");
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

