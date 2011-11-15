package client.ihm;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;

import client.Client;
import javax.swing.JLabel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class IncomeCall extends JFrame {

    private JPanel contentPane;
    private Client client;
    public JLabel label;

    /**
     * Create the frame.
     */
    public IncomeCall(Client theClient) {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 207, 106);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);
        
        JButton btnAccept = new JButton("Accept");
        btnAccept.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent arg0) {
                client.accept();
            }
        });
        btnAccept.setBounds(9, 49, 87, 29);
        contentPane.add(btnAccept);
        
        JButton btnReject = new JButton("Reject");
        btnReject.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent arg0) {
                client.reject();
            }
        });
        btnReject.setBounds(108, 49, 93, 29);
        contentPane.add(btnReject);
        
        JLabel lblIncomeCall = new JLabel("Income call:");
        lblIncomeCall.setBounds(9, 17, 87, 16);
        contentPane.add(lblIncomeCall);
        
        label = new JLabel("");
        label.setBounds(96, 17, 61, 16);
        contentPane.add(label);
        
        this.client=theClient;
    }

}
