package message;

import java.util.List;
import java.util.StringTokenizer;

import utils.Constants;

public class MessOk extends Message {

    public MessOk() {
        super("200");
        // TODO Auto-generated constructor stub
    }

    @Override
    public void parseMessage(List<StringTokenizer> tokens) {
    }

    @Override
    public String writeMessage() {
        return "200 OK"+Constants.CRLF;
    }

}
