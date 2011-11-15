package message;

import java.util.List;
import java.util.StringTokenizer;

import utils.Constants;

public class MessError extends Message {
    
    public String description;

    public MessError(String error) {
        super(error);
        if(error.equals("401")) {
            description="";
        }
        else if(error.equals("404")) {
            description="not found";
        }
        else if(error.equals("603")) {
            description="decline";
        }
    }

    @Override
    public void parseMessage(List<StringTokenizer> tokens) {
        description=tokens.get(0).nextToken();
            //log
            System.out.println(type+" "+description);
    }

    @Override
    public String writeMessage() {
        return type+" "+description+Constants.CRLF;
    }

}
