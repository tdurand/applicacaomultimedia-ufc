package persisted;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import message.MessClients;
import message.MessError;
import message.MessOk;
import message.Message;

import server.ClientConnected;

public class UserList {

    private List<ClientConnected> clientConnectedList=new ArrayList<ClientConnected>();
    
    private static UserList ul;
    
    private UserList() {}
    
    public static synchronized UserList getInstance(){
        if(ul == null){
            ul = new UserList();
        }
        
        return ul;
    }
    
     public synchronized boolean register(ClientConnected newClient){
         if(searchClient(newClient.getClientName())==null) {
            clientConnectedList.add(newClient);
            return true;
         }
         else {
             return false;
         }
    }
    
    public synchronized boolean unregister(String userName){
        ClientConnected clientConnected=searchClient(userName);
        if(clientConnected!=null) {
            clientConnectedList.remove(clientConnected);
            return true;
        }
        else {
            return false;
        }
    }
    
    public synchronized void sendMessage(String clientName,Message message) {
        ClientConnected clientConnected=searchClient(clientName);
        if(clientConnected!=null) {
            try {
                clientConnected.getBuffWriter().write(message.writeMessage());
                clientConnected.getBuffWriter().flush();
            } catch (IOException e) {
                //TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        else {
            //TODO client doesn't exit
            System.out.println("doesn't exit");
        }
    }
    
    public synchronized void sendToAll(Message message) {
        for (Iterator iterator = clientConnectedList.iterator(); iterator.hasNext();) {
            ClientConnected clientConnected = (ClientConnected) iterator.next();
            try {
                clientConnected.getBuffWriter().write(message.writeMessage());
                clientConnected.getBuffWriter().flush();
            } catch (IOException e) {
                
                e.printStackTrace();
            }
        }
    }
    
    public ClientConnected searchClient(String userName) {
        ClientConnected client=null;
        for (Iterator iterator = clientConnectedList.iterator(); iterator.hasNext();) {
            ClientConnected clientConnected = (ClientConnected) iterator.next();
            if(clientConnected.getClientName().equals(userName)) {
                return clientConnected;
            }
        }
        return client;
    }
    
    public void sendClientsList() {
        Message message=new MessClients(clientConnectedList);
        sendToAll(message);
    }
    
    public synchronized String setCaller(String caller,String clientName) {
        ClientConnected clientConnected=searchClient(clientName);
        String message="";
        if(clientConnected!=null) {
             clientConnected.caller=caller;
        }
        return message;
    }
    
    
}
