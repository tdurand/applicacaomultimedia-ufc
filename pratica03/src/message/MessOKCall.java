package message;

import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;
import java.util.logging.Logger;

import utils.Constants;

public class MessOKCall extends Message {

    private String destinationAddress;
    private String destinationPort;

    public MessOKCall(List<StringTokenizer> tokens) {
        super("200");
        this.parseMessage(tokens);
    }
    
    public MessOKCall(String destinationAddress,String destinationPort) {
        super("200");
        this.destinationAddress=destinationAddress;
        this.destinationPort=destinationPort;
    }

    @Override
    public void parseMessage(List<StringTokenizer> tokens) {
         //log
         System.out.println("200 OK");
        destinationAddress=tokens.get(0).nextToken();
            System.out.println("Destination-address: "+destinationAddress);
        tokens.get(1).nextToken();
        destinationPort=tokens.get(1).nextToken();
            System.out.println("Destination-port: "+destinationPort);
    }

    /* (non-Javadoc)
     * @see message.Message#writeMessage()
     */
    @Override
    public String writeMessage() {
        String message="";
        
        message+="200 OK"+Constants.CRLF;
        message+="Destination-address: "+destinationAddress+Constants.CRLF;
        message+="Destination-port: "+destinationPort+Constants.CRLF;
        
        return message;
    }

    /**
     * @return the destinationAddress
     */
    public String getDestinationAddress() {
        return destinationAddress;
    }

    /**
     * @param destinationAddress the destinationAddress to set
     */
    public void setDestinationAddress(String destinationAddress) {
        this.destinationAddress = destinationAddress;
    }

    /**
     * @return the destinationPort
     */
    public String getDestinationPort() {
        return destinationPort;
    }

    /**
     * @param destinationPort the destinationPort to set
     */
    public void setDestinationPort(String destinationPort) {
        this.destinationPort = destinationPort;
    }
    
    
    
    

}
