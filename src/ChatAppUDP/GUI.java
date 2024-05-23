package ChatAppUDP;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class GUI extends JFrame implements ActionListener {

    private final JTextArea chatMemberArea = new JTextArea();
    private final JTextArea chatArea = new JTextArea();
    private final JTextField messageField = new JTextField();
    private final JButton connectButton = new JButton("Disconnect");
    private final User user;
    private NetworkSender networkSender;

    public GUI(User user) throws IOException {
        this.user = user;
        createGUI();
        NetworkReceiver.getInstance().setGui(this); // Set reference to GUI
        new Thread(NetworkReceiver.getInstance()).start(); // Start the network receiver in a new thread
    }

    public void createGUI() {
        chatArea.setEditable(false);
        chatMemberArea.setEditable(false);
        messageField.setEditable(true);

        this.setTitle("Chat " + user.getName());
        this.setSize(700, 550);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());
        this.add(mainPanel);
        mainPanel.add(connectButton, BorderLayout.NORTH);
        connectButton.addActionListener(this);

        JPanel chatPanel = new JPanel(new BorderLayout());
        mainPanel.add(chatPanel, BorderLayout.CENTER);
        chatPanel.add(messageField, BorderLayout.SOUTH);

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
        JPanel memberAreaContainer = new JPanel(new BorderLayout());
        JLabel membersLabel = new JLabel("MEMBERS ONLINE: ");
        memberAreaContainer.add(membersLabel, BorderLayout.NORTH);
        memberAreaContainer.add(new JScrollPane(chatMemberArea), BorderLayout.CENTER);
        textPanel.add(memberAreaContainer, gbc);
        chatPanel.add(textPanel, BorderLayout.CENTER);

        chatArea.setFont(new Font("Comic Sans", Font.PLAIN, 16));
        chatMemberArea.setFont(new Font("Comic Sans", Font.PLAIN, 16));
        messageField.setFont(new Font("Comic Sans", Font.PLAIN, 16));
        messageField.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        messageField.addActionListener(e -> {
            String message = messageField.getText();
            if(!message.isEmpty()) {
               networkSender.sendUserMessage(message);
               messageField.setText("");
           }
        });
    }

    // Action for when the disconnect button is pressed.
    @Override
    public void actionPerformed(ActionEvent e) {
        user.setActive(false);
        try {
            networkSender.sendAdminDisconnectedMessage(user);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

        User.userList.remove(user);
        JOptionPane.showMessageDialog(null, user.getName() + " disconnected.");
        System.exit(0);
    }

    // Method to update chat area from NetworkReceiver
    public void updateChatArea(String message) {
        SwingUtilities.invokeLater(() -> chatArea.append(message));
    }

    // Method to update members area
    public void updateMembersArea(String members) {
        SwingUtilities.invokeLater(() -> {
            chatMemberArea.setText("");
            chatMemberArea.append(members);
        });
    }

    public void setNetworkSender(NetworkSender networkSender) {
        this.networkSender = networkSender;
    }
}
