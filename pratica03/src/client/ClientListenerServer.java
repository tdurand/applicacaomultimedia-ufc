package client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.net.InetAddress;
import java.util.Iterator;

import client.ihm.ClientList;
import client.ihm.Login;

import message.MessError;
import message.Message;
import utils.Parsing;

public class ClientListenerServer implements ActionListener {
    
    public Client client;


    public ClientListenerServer(Client client) {
    // TODO Auto-generated constructor stub
        this.client=client;
    }


    @Override
    public void actionPerformed(ActionEvent arg0) {
        // TODO Auto-generated method stub
        try {
            while(client.bufferedReader.ready()) {
                Message message= Parsing.parseMessage(client.bufferedReader); //blocking
                if(message!=null) {
                    System.out.println("Received message: "+message.writeMessage());
                    if(message.getType().equals("200")) 
                    {
                        System.out.println("OK");
                        if(message.getDestinationAddress()!=null) {
                            client.clientListWindow.label.setText(message.writeMessage());
                            client.incomeCall.setVisible(false);
                            
                            InetAddress adresseClientP2P=InetAddress.getByName(message.getDestinationAddress());
                            
                            client.clientListenerP2P.connectToOtherClient(adresseClientP2P, Integer.parseInt(message.getDestinationPort()));
                            
                        }
                    }
                    else if(message.getType().equals("CLIENTS")) {
                        
                        client.clientListWindow.clientListData.removeAllElements();
                         for (Iterator iterator = message.getClientList().iterator(); iterator.hasNext();) {
                          String aclient = (String) iterator.next();
                          client.clientListWindow.clientListData.addElement(aclient);
                         }
                    }
                    else if(message.getType().equals("401")) {
                        System.out.println(message.toString());
                    }
                    else if(message.getType().equals("100")) {
                        client.clientListWindow.label.setText(message.writeMessage());
                    }
                    else if(message.getType().equals("101")) {
                        client.clientListWindow.label.setText(message.writeMessage());
                    }
                    else if(message.getType().equals("180")) {
                        client.clientListWindow.label.setText(message.writeMessage());
                    }
                    else if(message.getType().equals("404")) {
                        client.clientListWindow.label.setText(message.writeMessage());
                        client.clientListWindow.btnCall.setEnabled(true);
                    }
                    else if(message.getType().equals("603")) {
                        client.clientListWindow.label.setText(message.writeMessage());
                        client.clientListWindow.btnCall.setEnabled(true);
                    }
                    else if(message.getType().equals("INVITE")) {
                        if(client.occuped) {
                            MessError messError=new MessError("603");
                            
                            try {
                                client.bufferedWriter.write(messError.writeMessage());
                                client.bufferedWriter.flush();
                              } catch (IOException e1) {
                                    // TODO Auto-generated catch block
                                    e1.printStackTrace();
                              }
                        }
                        else {
                        	client.p2pClient = message.getUserName();
                            client.incomeCall.label.setText(client.p2pClient);
                            client.incomeCall.setLocationRelativeTo(client.clientListWindow);
                            client.incomeCall.setVisible(true);
                        }
                    }
                    
                }

   }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
}
