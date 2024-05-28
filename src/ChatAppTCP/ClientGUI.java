package ChatAppTCP;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

public class ClientGUI extends JFrame implements ActionListener, WindowListener {
    private final JTextArea chatMemberArea = new JTextArea();
    private final JTextArea chatArea = new JTextArea();
    private final JTextField messageField = new JTextField();
    private final JButton connectButton = new JButton("Disconnect");
    private final User user;
    private Client client;

    public ClientGUI(User user) throws IOException {
        this.user = user;
        createClientGUI();
    }

    public void createClientGUI() {
        chatArea.setEditable(false);
        chatMemberArea.setEditable(false);
        messageField.setEditable(true);

        setTitle("Chat TCP " + user.getName());
        setSize(700, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        addWindowListener(this);

        JPanel mainPanel = new JPanel(new BorderLayout());
        add(mainPanel);
        mainPanel.add(connectButton, BorderLayout.NORTH);
        connectButton.addActionListener(this);

        JPanel chatPanel = new JPanel(new BorderLayout());
        mainPanel.add(chatPanel, BorderLayout.CENTER);
        chatPanel.add(messageField, BorderLayout.SOUTH);
        chatPanel.add(new JScrollPane(chatArea), BorderLayout.CENTER);

        JPanel textPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;

        // Set the chat area
        gbc.weightx = 0.75; // 75% of horizontal space
        gbc.weighty = 1.0;
        gbc.gridx = 0;
        gbc.gridy = 0;
        textPanel.add(new JScrollPane(chatArea), gbc);

        // Set the member area
        gbc.weightx = 0.25; // 25% of horizontal space
        gbc.weighty = 1.0;
        gbc.gridx = 1;
        gbc.gridy = 0;
        JPanel memberAreaPanel = new JPanel(new BorderLayout());
        JLabel membersLabel = new JLabel("MEMBERS ONLINE: ");
        memberAreaPanel.add(membersLabel, BorderLayout.NORTH);
        memberAreaPanel.add(new JScrollPane(chatMemberArea), BorderLayout.CENTER);
        textPanel.add(memberAreaPanel, gbc);
        chatPanel.add(textPanel, BorderLayout.CENTER);

        chatArea.setFont(new Font("Dialog", Font.PLAIN, 16));
        chatMemberArea.setFont(new Font("Dialog", Font.PLAIN, 16));
        messageField.setFont(new Font("Dialog", Font.PLAIN, 16));
        messageField.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        messageField.addActionListener(e -> {
            String message = messageField.getText();
            if (!message.isEmpty()) {
                client.sendMessage(message);
                messageField.setText("");
            }
        });
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        disconnectAndExit();
    }

    private void disconnectAndExit() {
        client.close();
        JOptionPane.showMessageDialog(null, user.getName() + " disconnected.");
        System.exit(0);
    }

    public void updateChatArea(String message) {
        SwingUtilities.invokeLater(() -> chatArea.append(message));
    }

    public void resetMembersArea() {
        SwingUtilities.invokeLater(() -> chatMemberArea.setText(""));
    }

    public void updateMembersArea(String userName) {
        SwingUtilities.invokeLater(() -> chatMemberArea.append(userName));
    }

    public void setClient(Client client) {
        this.client = client;
    }

    @Override
    public void windowOpened(WindowEvent e) {
    }

    @Override
    public void windowClosing(WindowEvent e) {
        disconnectAndExit();
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


