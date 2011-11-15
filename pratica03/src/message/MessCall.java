package message;

import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;
import java.util.logging.Logger;

import utils.Constants;

public class MessCall extends Message {

    
    private String userName;

    public MessCall(List<StringTokenizer> tokens) {
        super("CALL");
        this.parseMessage(tokens);
    }
    
    public MessCall(String userName) {
        super("CALL");
        this.userName = userName;
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

    @Override
    public void parseMessage(List<StringTokenizer> tokens) {
        //first line
        userName=tokens.get(0).nextToken();
            //log
            System.out.println("CALL");
            System.out.println("userName :"+userName);
    }

    /* (non-Javadoc)
     * @see message.Message#writeMessage()
     */
    @Override
    public String writeMessage() {
        String message="";
        
        message+="CALL "+userName+Constants.CRLF;
        
        return message;
    }
    
    

}
