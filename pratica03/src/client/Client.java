package client;

  //How to use the client
  //----------------
  //1.    Compile with javac 
  //2.    Run : *java Client [Server hostname] [Server RTSP listening port]*
  //
  //      example: `java Client 127.0.0.1 3000`
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.List;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.Timer;

import client.ihm.ClientList;
import client.ihm.IncomeCall;
import client.ihm.Login;
import client.ihm.P2P;

import server.ClientConnected;
import utils.Parsing;

import message.MessCall;
import message.MessClients;
import message.MessError;
import message.MessOKCall;
import message.MessUnregister;
import message.Message;
import message.MessOk;
import message.MessRegister;

public class Client{

  public Socket clientServerSocket; //socket used to send/receive RTSP messages
  //* input and output stream filters
  public BufferedReader bufferedReader;
  public BufferedWriter bufferedWriter;
  
  public int p2pClientPort;
  public Login loginWindow;
  public ClientList clientListWindow;
  public IncomeCall incomeCall;
  public P2P windowP2P;
  
  public ClientListenerP2P clientListenerP2P;
  
  public boolean occuped;
  

  final static String CRLF = "\r\n";
  
  public String userName;

  //Constructor
  //----------
  public Client() {
  }

  //Main
  //-----
  public static void main(String argv[]) throws Exception
  {
    //Login
    //* create a Client object
    Client theClient = new Client();
    theClient.loginWindow= new Login(theClient);
    theClient.clientListWindow=new ClientList(theClient);
    theClient.incomeCall=new IncomeCall(theClient);
    theClient.windowP2P=new P2P(theClient);
    theClient.occuped=false;
    
    theClient.loginWindow.setVisible(true);

    //* get server RTSP port and IP address from the command line
    int RTSP_server_port = Integer.parseInt(argv[1]);
    String ServerHost = argv[0];
    InetAddress ServerIPAddr = InetAddress.getByName(ServerHost);
    
    theClient.userName=argv[2];
    
    theClient.p2pClientPort=findFreePort();

    //* establish a TCP connection with the server to exchange RTSP messages
    theClient.clientServerSocket = new Socket(ServerIPAddr, RTSP_server_port);

    //* set input and output stream filters:
    theClient.bufferedReader = new BufferedReader(new InputStreamReader(theClient.clientServerSocket.getInputStream()) );
    theClient.bufferedWriter = new BufferedWriter(new OutputStreamWriter(theClient.clientServerSocket.getOutputStream()) );
    
    System.out.println("Init Success");
    
    
  }
  
//Wait Message
  //------------------------------------
  public void waitMessage() {
      
      ClientListenerServer clientListener=new ClientListenerServer(this);
      Timer timer=new Timer(20, clientListener);
      timer.start();
      
      clientListenerP2P=new ClientListenerP2P(this);
      new Thread(clientListenerP2P).start();
  }
  
  
  public void register() {
      System.out.println("Register Button!");
      MessRegister messRegister=new MessRegister(userName, "...", p2pClientPort);
      System.out.println("Register message: "+messRegister.writeMessage());
      
      
      try {
        //clean bufferedReader
          while(bufferedReader.ready()) {
              bufferedReader.readLine();
          }
          
          
        bufferedWriter.write(messRegister.writeMessage());
        bufferedWriter.flush();
      } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
      }
      Message message=Parsing.parseMessage(bufferedReader);
      
      System.out.println("Response: "+message.writeMessage());
      
      if(message.getType().equals("200")) {
          clientListWindow.setTitle(userName);
          clientListWindow.setLocationRelativeTo(loginWindow);
          loginWindow.setVisible(false);
          clientListWindow.setVisible(true);
          //wait for message
          waitMessage();
      }
          
  }
  
  public void unregister() {
      System.out.println("Unregister Button pressed !");
      
      MessUnregister messRegister=new MessUnregister(userName);
      
    try {
      bufferedWriter.write(messRegister.writeMessage());
      bufferedWriter.flush();
    } catch (IOException e1) {
          // TODO Auto-generated catch block
          e1.printStackTrace();
    }
    
      loginWindow.setLocationRelativeTo(clientListWindow);
      clientListWindow.setVisible(false);
      loginWindow.setVisible(true);
  }
  
  public void call(String name) {
      // TODO Auto-generated method stub
      System.out.println("Call Button pressed !");
      
      MessCall messCall=new MessCall(name);
      
      try {
          bufferedWriter.write(messCall.writeMessage());
          bufferedWriter.flush();
        } catch (IOException e1) {
              // TODO Auto-generated catch block
              e1.printStackTrace();
        }
        
      clientListWindow.btnCall.setEnabled(false);
  }
  
  public void accept() {
      // TODO Auto-generated method stub
        System.out.println("Accept Call Button pressed !");
      
      MessOk messOK=new MessOk();
      
      try {
          bufferedWriter.write(messOK.writeMessage());
          bufferedWriter.flush();
        } catch (IOException e1) {
              // TODO Auto-generated catch block
              e1.printStackTrace();
        }
      
        occuped=true;
        
  }
  
  public void reject() {
        System.out.println("Reject");
        
        MessError messError=new MessError("603");
        
        try {
            bufferedWriter.write(messError.writeMessage());
            bufferedWriter.flush();
          } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
          }
          
        incomeCall.setVisible(false);
        clientListWindow.btnCall.setEnabled(true);
  }
  
  

  
  public static int findFreePort(){
      
    try {
        ServerSocket server;
        server = new ServerSocket(0);
        int port = server.getLocalPort();
        server.close();
        return port;
    } catch (IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }
      return (int)(Math.random()*40000);
  }

}//end of Class Client

