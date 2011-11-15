package message;

import java.util.List;
import java.util.StringTokenizer;

import utils.Constants;

public class Mess1xx extends Message {
    
    public String description;

    public Mess1xx(String number) {
        super(number);
        if(number.equals("100")) {
            description="trying";;
        }
        else if(number.equals("101")) {
            description="found";
        }
        else if(number.equals("180")) {
            description="ringing";
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
