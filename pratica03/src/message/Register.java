package message;

import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;
import java.util.logging.Logger;

import utils.Constants;

public class Register extends Message {

    
    private String userName;
    private String password;
    private int    clientCommunicationControlPort;

    public Register(List<StringTokenizer> tokens) {
        super("REGISTER");
        this.parseMessage(tokens);
    }
    
    public Register(String userName, String password,
            int clientCommunicationControlPort) {
        super("REGISTER");
        this.userName = userName;
        this.password = password;
        this.clientCommunicationControlPort = clientCommunicationControlPort;
    }
    /**
     * @return the userName
     */
    @Override
    public String getUserName() {
        return userName;
    }

    /**
     * @param userName the userName to set
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return the clientCommunicationControlPort
     */
    @Override
    public int getClientCommunicationControlPort() {
        return clientCommunicationControlPort;
    }

    /**
     * @param clientCommunicationControlPort the clientCommunicationControlPort to set
     */
    public void setClientCommunicationControlPort(int clientCommunicationControlPort) {
        this.clientCommunicationControlPort = clientCommunicationControlPort;
    }

    @Override
    public void parseMessage(List<StringTokenizer> tokens) {
        //first line
        userName=tokens.get(0).nextToken();
            //log
            System.out.println("REGISTER");
            System.out.println("user:Name :"+userName);
        tokens.get(1).nextToken();
        tokens.get(1).nextToken();
        password=tokens.get(1).nextToken();
            System.out.println("password = "+password);
        tokens.get(2).nextToken();
        tokens.get(2).nextToken();
        clientCommunicationControlPort=Integer.parseInt(tokens.get(2).nextToken());
            System.out.println("port = "+clientCommunicationControlPort);
    }



    /* (non-Javadoc)
     * @see message.Message#writeMessage()
     */
    @Override
    public String writeMessage() {
        String message="";
        
        message+="REGISTER "+userName+Constants.CRLF;
        message+="password = "+password+Constants.CRLF;
        message+="port = "+clientCommunicationControlPort+Constants.CRLF;
        
        return message;
    }
    
    

}
