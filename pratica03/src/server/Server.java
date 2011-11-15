package server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import javax.swing.Timer;

import persisted.UserList;
import utils.Parsing;

import message.Mess1xx;
import message.MessCall;
import message.MessError;
import message.MessInvite;
import message.MessOKCall;
import message.MessUnregister;
import message.Message;
import message.MessOk;
import message.MessRegister;

public class Server implements Runnable {
    
    public UserList userList;
    
    public Socket clientServerControlSocket;
    public BufferedWriter clientServerControlBufferedWriter;
    public BufferedReader clientServerControlBufferedReader;
    public InetAddress clientIPAddr;
    public String userName;
    
    public Server(Socket socket) {
        super();
        System.out.println("Accepted resquest from :"+ socket.getInetAddress());
        this.clientServerControlSocket = socket;
        this.userList=UserList.getInstance();
    }
    
    @Override
    public void run() {
        //*Init buffers
        try {
            clientServerControlBufferedReader= new BufferedReader(new InputStreamReader(clientServerControlSocket.getInputStream()) );
            clientServerControlBufferedWriter= new BufferedWriter(new OutputStreamWriter(clientServerControlSocket.getOutputStream()) );
        } catch (IOException e) { e.printStackTrace();}
        
        //*Init clientIpAdress
        clientIPAddr=clientServerControlSocket.getInetAddress();
        
        //###Wait for messages from the client
        while(true) {
            waitMessage();
        }
    }
    
    //Wait Message
    //------------------------------------
    public void waitMessage() {
       Message message= Parsing.parseMessage(clientServerControlBufferedReader); //blocking
       if(message!=null) {
           if(message.getType().equals("REGISTER")) 
           {
               ClientConnected newClient=new ClientConnected(message.getClientCommunicationControlPort(), clientIPAddr, clientServerControlBufferedWriter,clientServerControlBufferedReader, message.getUserName());
               if(userList.register(newClient)) {
                   userList.sendMessage(newClient.getClientName(), new MessOk());
                   userName=newClient.getClientName();
                   userList.sendClientsList();
               }
               else {
                   try {
                       clientServerControlBufferedWriter.write(new MessError("401").writeMessage());
                       clientServerControlBufferedWriter.flush();
                   } catch (IOException e) {
                       // TODO Auto-generated catch block
                       e.printStackTrace();
                   }
               }
           }
           else if(message.getType().equals("UNREGISTER")) {
               userList.unregister(message.getUserName());
               userList.sendClientsList();
           }
           else if(message.getType().equals("CALL")) {
               userList.sendMessage(userName, new Mess1xx("100"));
               ClientConnected client=userList.searchClient(message.getUserName());
               if(client!=null) {
                   userList.sendMessage(userName, new Mess1xx("101"));
                   userList.sendMessage(client.getClientName(), new MessInvite(userName));
                   userList.setCaller(userName, client.getClientName());
                   userList.sendMessage(userName, new Mess1xx("180"));
               }
               else {
                   userList.sendMessage(userName, new MessError("404"));
               }
           }
           else if(message.getType().equals("200")) {
               ClientConnected clientConnected=userList.searchClient(userName);
               if(clientConnected.caller!="") {
                   MessOKCall okCall=new MessOKCall(clientConnected.getClientAddress().toString(), String.valueOf(clientConnected.getClientclientCommunicationPort()));
                   userList.sendMessage(clientConnected.caller, okCall);
                   clientConnected.caller="";
               }
           }
           else if(message.getType().equals("603")) {
               ClientConnected clientConnected=userList.searchClient(userName);
               if(clientConnected.caller!="") {
                   MessError declineCall=new MessError("603");
                   userList.sendMessage(clientConnected.caller, declineCall);
                   clientConnected.caller="";
               }
           }
       }
    }
}

