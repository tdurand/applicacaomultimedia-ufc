package message;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import server.ClientConnected;
import utils.Constants;

public class MessClients extends Message {
    
    private List<String> clientsConnected=new ArrayList<String>();;
    
    public MessClients(List<ClientConnected> clientsC) {
        super("CLIENTS");
        for (Iterator iterator = clientsC.iterator(); iterator
                .hasNext();) {
            ClientConnected clientConnected = (ClientConnected) iterator.next();
            clientsConnected.add(clientConnected.getClientName());
        }
    }
    
    public MessClients(String type,List<StringTokenizer> tokens) {
        super("CLIENTS");
        parseMessage(tokens);
    }
    
    public List<String> getClientList() {
        return clientsConnected;
    }

    @Override
    public void parseMessage(List<StringTokenizer> tokens) {
        clientsConnected.clear();
        for (Iterator iterator = tokens.iterator(); iterator.hasNext();) {
            StringTokenizer stringTokenizer = (StringTokenizer) iterator.next();
            if(stringTokenizer.hasMoreTokens()) {
            String client=stringTokenizer.nextToken();
                if(!client.equals("CLIENTS")) {
                    clientsConnected.add(client);
                }
            }
        }
    }

    @Override
    public String writeMessage() {
        
        String message="";
        
        message+="CLIENTS "+Constants.CRLF;
        for (Iterator iterator = clientsConnected.iterator(); iterator.hasNext();) {
            String client = (String) iterator.next();
            message+=client+Constants.CRLF;
        }
        
        return message;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "MessClients [clientsConnected=" + clientsConnected + "]";
    }
    
    
}
