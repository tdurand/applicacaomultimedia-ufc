package client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Iterator;
import java.util.StringTokenizer;
import javax.swing.Timer;

import message.MessError;
import message.Message;
import utils.Constants;
import utils.Parsing;

public class ClientListenerP2P implements ActionListener,Runnable {
    
    public Client client;
    
    public boolean connectionEstablished;
    
    public BufferedReader controlBufferedReader;
    public BufferedWriter controlBufferedWriter;
    public BufferedReader textBufferedReader;
    public BufferedWriter textBufferedWriter;

    public Socket controlSocket;
    public Socket messageSocket;
    public Socket textSocket;
    
    public String message;

    public static int rows;
    public int sourceTextPort;
    public int destTextPort;
    public int sourceControlPort;
    public int destControlPort;
    
    public ClientListenerP2P(Client client) {
        super();
        this.client = client;
        this.connectionEstablished=false;
        this.sourceControlPort=client.p2pClientPort;
        this.sourceTextPort=Client.findFreePort();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
                int count;
                String line;
                String str ="";
                StringTokenizer tokens;
                String token ="";
                int numberRows = 0;

                while(textBufferedReader.ready()){
                      count = 0;
                      line = "";

                      if((str = textBufferedReader.readLine()) != null){
                          tokens = new StringTokenizer(str);
                          token = tokens.nextToken();
                          numberRows = Integer.parseInt(token);
                          System.out.println("Numero de linhas: "+numberRows);
                          line = str.substring(token.length()+1, str.length());
                          System.out.println("Substrng: #" + line);
                          count++;
                      }
                      while(count < numberRows){
                          line += "\n" + textBufferedReader.readLine();
                          count++;
                      }
                      if(!line.equals("")){
                          client.windowP2P.textArea.append(line+Constants.CRLF);
                          client.windowP2P.textArea.update(client.windowP2P.textArea.getGraphics());
                          str = null;
                      }
                }
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
    }
    
    
    private void sendMessageRequest(String requestType){
        try{

            if(requestType.equals("SETUP")){
                controlBufferedWriter.write(requestType + " " + sourceControlPort + Constants.CRLF);// é a porta rtsp
                System.out.println("sourceControlPort: " + sourceControlPort);
            }
            else{
                controlBufferedWriter.write(sourceTextPort + " " + sourceControlPort + Constants.CRLF);// é a porta rtsp
                System.out.println("sourceTextPort: " + sourceTextPort);
                System.out.println("sourceControlPort: " + sourceControlPort);
            }
            
            controlBufferedWriter.flush();
        }
        catch(Exception ex){
            System.out.println("Exception caught in sendMessageRequest: "+ex);
            System.exit(0);
        }

    }
    
    public void parseMessageRequest(){
        String firstToken = "";

        try{
            String statusLine = controlBufferedReader.readLine();
            System.out.println("Client - Received from another Client:");
            System.out.println(statusLine);
            
            StringTokenizer tokens = new StringTokenizer(statusLine);
            firstToken = tokens.nextToken();
            
            //
            if(!firstToken.equals("SETUP")){
                destTextPort = Integer.parseInt(firstToken);
                System.out.println("destTextPort: " + destTextPort);
                destControlPort = Integer.parseInt(tokens.nextToken());
                System.out.println("destControlPort: " + destControlPort);
            }
            else if(firstToken.equals("SETUP"))
                destControlPort = Integer.parseInt(tokens.nextToken());
                System.out.println("destControlPort: " + destControlPort);

        }
        catch(Exception ex){
            System.out.println("Exception caught in parseMessageRequest: "+ex);
            System.exit(0);
        }
    }
    
    public void sendTextMessage() {
        try{
            String text = client.windowP2P.textField.getText();
            
            int total = 0;

            for (int i = 0; i <text.length(); i++) {
                    if (client.windowP2P.textField.getText().charAt(i) == " ".charAt(0))  {
                        total++;
                    }
            }

            StringTokenizer tokens = new StringTokenizer(text);
            rows = tokens.countTokens() - total;
            textBufferedWriter.write(rows + " " + text + Constants.CRLF);
            System.out.println("client.windowP2P.textField.getText(): " + text );
            textBufferedWriter.flush();
            client.windowP2P.textField.setText("");
        }catch(Exception ex){
            System.out.println("Exception in sendButtonListener: " + ex);
            System.exit(0);
        }
    }

    @Override
    public void run() {
        try {
            //If there is not P2P connection already established
            if(!connectionEstablished) {
                System.out.println("sourceControlPort: " + sourceControlPort);
                ServerSocket listenSocket  = new ServerSocket(sourceControlPort);

                System.out.println("Waiting connection...");
                this.controlSocket = listenSocket.accept();
                System.out.println("connection accepted!");

                //Set input and output stream filters:
                controlBufferedReader = new BufferedReader(new InputStreamReader(this.controlSocket.getInputStream()) );
                controlBufferedWriter = new BufferedWriter(new OutputStreamWriter(this.controlSocket.getOutputStream()) );

                sendMessageRequest(" ");
                System.out.println("Receiving Request!");
                parseMessageRequest();
                System.out.println("Sending Response!");
                
                //Waiting establishment of text socket
                ServerSocket listenSocketText = new ServerSocket(sourceTextPort);
                System.out.println("#####");
                System.out.println("sourceTextPort: "+ sourceTextPort);

                System.out.println("Waiting connection...");
                this.textSocket = listenSocketText.accept();
                System.out.println("connection accepted!");
                
              //Set input and output stream filters:
                textBufferedReader = new BufferedReader(new InputStreamReader(this.textSocket.getInputStream()) );
                textBufferedWriter = new BufferedWriter(new OutputStreamWriter(this.textSocket.getOutputStream()) );
                
                
                client.incomeCall.setVisible(false);
                client.windowP2P.setTitle(client.userName);
                client.windowP2P.setLocationRelativeTo(client.clientListWindow);
                client.clientListWindow.setVisible(false);
                client.windowP2P.setVisible(true);
                
                connectionEstablished=true;
                
                System.out.println("startTimer");
                Timer timerP2P=new Timer(20, this);
                timerP2P.start();
                
            }
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        
    }
    
    public void connectToOtherClient(InetAddress clientIp,int clientDestPort) {
        try {
            this.destControlPort = clientDestPort;
            this.controlSocket = new Socket(clientIp, clientDestPort);

            //Set input and output stream filters:
            controlBufferedReader = new BufferedReader(new InputStreamReader(this.controlSocket.getInputStream()) );
            controlBufferedWriter = new BufferedWriter(new OutputStreamWriter(this.controlSocket.getOutputStream()) );
            
            sendMessageRequest("SETUP");
            System.out.println("Sending Request!");
            parseMessageRequest();
            System.out.println("Response Received!");
            
            this.textSocket = new Socket(clientIp, destTextPort);
            System.out.println("#####");
            System.out.println("destTextPort: "+ destTextPort);

            //Set input and output stream filters:
            textBufferedReader = new BufferedReader(new InputStreamReader(this.textSocket.getInputStream()) );
            textBufferedWriter = new BufferedWriter(new OutputStreamWriter(this.textSocket.getOutputStream()) );
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        
        
        client.incomeCall.setVisible(false);
        client.windowP2P.setLocationRelativeTo(client.clientListWindow);
        client.clientListWindow.setVisible(false);
        client.windowP2P.setTitle(client.userName);
        
        client.windowP2P.setVisible(true);
        
        System.out.println("startTimer");
        Timer timerP2P=new Timer(20, this);
        timerP2P.start();
    }

}
