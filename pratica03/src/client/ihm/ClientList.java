package client.ihm;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JList;
import javax.swing.JButton;

import client.Client;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JLabel;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;

public class ClientList extends JFrame {

    private JPanel contentPane;
    
    public JList clientList;
    public JLabel label;
    public JButton btnCall = new JButton("Call");
    private Client client;
    
    public DefaultListModel clientListData=new DefaultListModel();
    /**
     * Create the frame.
     */
    public ClientList(Client theClient) {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 450, 300);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);
        
        clientList = new JList(clientListData);
        clientList.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent arg0) {
                if(clientList.getSelectedValue()!=null) {
                    if(clientList.getSelectedValue().equals(client.userName)) {
                        btnCall.setEnabled(false);
                    }
                    else {
                        btnCall.setEnabled(true);
                    }
                }
            }
        });
        clientList.setBounds(23, 16, 247, 175);
        contentPane.add(clientList);
        btnCall.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent arg0) {
                if(clientList.getSelectedValue()!=null) {
                    if(btnCall.isEnabled()) {
                        client.call((String) clientList.getSelectedValue());
                    }
                }
                
            }
        });
        
        btnCall.setBounds(19, 220, 117, 29);
        contentPane.add(btnCall);
        
        JButton btnUnregister = new JButton("Unregister");
        btnUnregister.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent arg0) {
                client.unregister();
            }
        });
        btnUnregister.setBounds(148, 220, 117, 29);
        contentPane.add(btnUnregister);
        
        label = new JLabel("");
        label.setBounds(293, 44, 137, 50);
        contentPane.add(label);
        
        this.client=theClient;
    }
}
