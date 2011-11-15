package message;

import java.util.List;
import java.util.StringTokenizer;

public abstract class Message {
    
    protected String type;

    public Message(String type) {
        super();
        this.type = type;
    }
    
    public abstract void parseMessage(List<StringTokenizer> tokens);
    
    public abstract String writeMessage();
    
    public String getUserName() {return null;}
    public int getClientCommunicationControlPort() {return 0;}
    public List<String> getClientList() {return null;}
    public String getDestinationAddress() { return null;}
    /**
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
    }
    
    
}
