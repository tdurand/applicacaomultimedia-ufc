package utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import message.Mess1xx;
import message.MessCall;
import message.MessClients;
import message.MessError;
import message.MessInvite;
import message.MessOKCall;
import message.MessOk;
import message.MessRegister;
import message.MessUnregister;
import message.Message;

public class Parsing {
    
  //Parse Message
    //------------------------------------
    public static Message parseMessage(BufferedReader clientServerControlBufferedReader)
    {
      Message message=null;
      
      List<StringTokenizer> lines=new ArrayList<StringTokenizer>();
      
      try{
          synchronized (clientServerControlBufferedReader) {
              StringTokenizer firstLineTokenized = readLine(clientServerControlBufferedReader);
              String requestType = firstLineTokenized.nextToken();
              lines.add(firstLineTokenized);

              //* Identify the messageType and use the correct parser
              if ((new String(requestType)).compareTo("REGISTER") == 0) {
                  //read two lines more
                  lines.add(readLine(clientServerControlBufferedReader));
                  lines.add(readLine(clientServerControlBufferedReader));
                  message=new MessRegister(lines);
              }
              else if ((new String(requestType)).compareTo("UNREGISTER") == 0) {
                  message=new MessUnregister(lines);
              }
              else if ((new String(requestType)).compareTo("CALL") == 0) {
                  message=new MessCall(lines);
              }
              else if ((new String(requestType)).compareTo("Destination-address:") == 0) {
                  lines.add(readLine(clientServerControlBufferedReader));
                  message=new MessOKCall(lines);
                  
              }
              else if ((new String(requestType)).compareTo("200") == 0) {
                  message=new MessOk();
              }
              else if(new String(requestType).compareTo("401") == 0) {
                  message= new MessError("401");
              }
              else if(new String(requestType).compareTo("404") == 0) {
                  message= new MessError("401");
              }
              else if(new String(requestType).compareTo("603") == 0) {
                  message= new MessError("603");
              }
              else if(new String(requestType).compareTo("100") == 0) {
                  message= new Mess1xx("100");
              }
              else if(new String(requestType).compareTo("101") == 0) {
                  message= new Mess1xx("101");
              }
              else if(new String(requestType).compareTo("180") == 0) {
                  message= new Mess1xx("180");
              }
              else if(new String(requestType).compareTo("INVITE") == 0) {
                  message= new MessInvite(lines);
              }
              else if((new String(requestType)).compareTo("CLIENTS") == 0) {
                  while(clientServerControlBufferedReader.ready()) {
                      lines.add(readLine(clientServerControlBufferedReader));
                  }
                  message=new MessClients(null,lines);
              }
        }
      }
      catch(Exception ex)
        {
          System.out.println("Exception caught: "+ex);
          System.exit(0);
        }
      
      return(message);
    }
    
    public static StringTokenizer readLine(BufferedReader buffReader) {
        StringTokenizer lineTokenized=null;
        try {
            String line= buffReader.readLine();
            //System.out.println(line);
            lineTokenized = new StringTokenizer(line);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
       return lineTokenized;
    }
}
