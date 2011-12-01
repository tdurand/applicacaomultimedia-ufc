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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Color;

public class P2P extends JFrame {

    public JPanel contentPane;
    public JTextArea textInput;
    public JTextArea chat;
    public Client client;
    
    /**
     * Create the frame.
     */
    public P2P(Client theClient) {
        this.client = theClient;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 450, 281);
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
        btnSend.setBounds(317, 221, 117, 25);
        contentPane.add(btnSend);
        
        JButton btnClose = new JButton("Close");
		btnClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//TODO BYE
			}
		});
		btnClose.setBounds(0, 221, 117, 25);
		contentPane.add(btnClose);
        
        textInput = new JTextArea();
        textInput.setBounds(0, 164, 434, 51);
        contentPane.add(textInput);
        textInput.setColumns(10);
        
        chat = new JTextArea();
        chat.setBounds(0, 0, 434, 161);
        contentPane.add(chat);
    }
}
