package client.ihm;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.HeadlessException;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextArea;
import javax.swing.JButton;
import javax.swing.JTextField;

import client.Client;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Color;

public class P2P extends JFrame {

    public JPanel contentPane;
    public JTextField textField;
    public JTextArea textArea;
    public Client client;
    
    /**
     * Create the frame.
     */
    public P2P(Client theClient) {
        this.client = theClient;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 450, 300);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);
        
        JButton btnSend = new JButton("Send");
        btnSend.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent arg0) {
                client.clientListenerP2P.sendTextMessage();
            }
        });
        btnSend.setBounds(315, 224, 117, 29);
        contentPane.add(btnSend);
        
        textField = new JTextField();
        textField.setBounds(69, 223, 222, 28);
        contentPane.add(textField);
        textField.setColumns(10);
        
        textArea = new JTextArea();
        textArea.setBounds(69, 20, 245, 191);
        contentPane.add(textArea);
    }
}
