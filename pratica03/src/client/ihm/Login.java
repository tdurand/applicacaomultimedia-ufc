package client.ihm;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;
import javax.swing.JButton;

import client.Client;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Login extends JFrame {

    private JPanel contentPane;
    private JTextField txtName;
    private Client client;
    /**
     * Create the frame.
     */
    public Login(Client theClient) {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 450, 300);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);
        
        txtName = new JTextField();
        txtName.setText("Name");
        txtName.setBounds(97, 120, 134, 28);
        contentPane.add(txtName);
        txtName.setColumns(10);
        
        this.client=theClient;
        
        JButton btnRegister = new JButton("Register");
        btnRegister.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent arg0) {
                client.userName=txtName.getText();
                client.register();
            }
        });
        btnRegister.setBounds(92, 188, 117, 29);
        contentPane.add(btnRegister);
    }
}
