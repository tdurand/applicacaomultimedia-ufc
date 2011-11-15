package message;

import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;
import java.util.logging.Logger;

import utils.Constants;

public class MessInvite extends Message {

    
    private String userName;

    public MessInvite(List<StringTokenizer> tokens) {
        super("INVITE");
        this.parseMessage(tokens);
    }
    
    public MessInvite(String userName) {
        super("INVITE");
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
            System.out.println("INVITE");
            System.out.println("userName :"+userName);
    }

    /* (non-Javadoc)
     * @see message.Message#writeMessage()
     */
    @Override
    public String writeMessage() {
        String message="";
        
        message+="INVITE "+userName+Constants.CRLF;
        
        return message;
    }
    
    

}
