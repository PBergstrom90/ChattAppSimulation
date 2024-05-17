package ChatApp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GUI extends JFrame implements ActionListener {

    JFrame frame;
    JPanel mainPanel;
    JPanel chatPanel;
    JPanel textPanel;
    JTextArea chatMemberArea;
    JTextArea chatArea;
    JTextField messageField;
    JScrollPane chatScroll;
    JButton connectButton = new JButton("Disconnect");
    JLabel chatMembersLabel = new JLabel("Members Online: ");

    public void createGUI(){
        frame = new JFrame("ChatApp");
        mainPanel = new JPanel();
        chatPanel = new JPanel();
        textPanel = new JPanel();
        chatMemberArea = new JTextArea();
        chatArea = new JTextArea();
        messageField = new JTextField();
        chatScroll = new JScrollPane(chatArea);
        chatArea.setEditable(false);
        chatMemberArea.setEditable(false);
        messageField.setEditable(true);

        frame.setTitle("Chat App Deluxe");
        frame.getContentPane().setBackground(Color.WHITE);
        frame.setLayout(new BorderLayout());
        frame.setSize(700,500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.add(mainPanel);

        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(connectButton, BorderLayout.NORTH);
        mainPanel.add(chatPanel, BorderLayout.CENTER);
        connectButton.addActionListener(this);

        chatPanel.setLayout(new BorderLayout());
        chatPanel.add(textPanel, BorderLayout.CENTER);
        chatPanel.add(messageField, BorderLayout.SOUTH);

        textPanel.setLayout(new GridLayout(1,2));
        textPanel.add(chatArea);
        textPanel.add(chatMemberArea);
        textPanel.add(chatScroll);
        chatMemberArea.add(chatMembersLabel);

        messageField.setFont(new Font("Roboto", Font.PLAIN, 12));
        messageField.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        pack();
    }

    public GUI() {
        createGUI();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JOptionPane.showMessageDialog(null, "User disconnected.");
        System.exit(0);
    }
}
