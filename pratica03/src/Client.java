
  //How to use the client
  //----------------
  //1.    Compile with javac 
  //2.    Run : *java Client [Server hostname] [Server RTSP listening port]*
  //
  //      example: `java Client 127.0.0.1 3000`
import java.io.*;
import java.net.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.Timer;

import message.Register;

public class Client{
  //Variables
  //---------
  //###GUI variables
  JFrame f = new JFrame("Client");
  JButton registerButton = new JButton("Register");
  JButton tearButton = new JButton("Teardown");
  JPanel mainPanel = new JPanel();
  JPanel buttonPanel = new JPanel();
  JLabel iconLabel = new JLabel();
  ImageIcon icon;


  //###RTP variables:
  DatagramPacket rcvdp; //UDP packet received from the server
  DatagramSocket RTPsocket; //socket to be used to send and receive UDP packets
  public int RTP_RCV_PORT; //port where the client will receive the RTP packets

  Timer timer; //timer used to receive data from the UDP socket
  byte[] buf; //buffer used to store data received from the server

  //###RTSP variables
  //* rtsp states
  final static int INIT = 0;
  final static int READY = 1;
  final static int PLAYING = 2;
  static int state; //RTSP state == INIT or READY or PLAYING
  Socket RTSPsocket; //socket used to send/receive RTSP messages
  //* input and output stream filters
  public BufferedReader RTSPBufferedReader;
  public BufferedWriter RTSPBufferedWriter;
  public String VideoFileName; //video file to request to the server
  public int RTSPSeqNb = 0; //Sequence number of RTSP messages within the session
  public int RTSPid = 0; //ID of the RTSP session (given by the RTSP Server)

  final static String CRLF = "\r\n";

  //* video constants
  static int MJPEG_TYPE = 26; //RTP payload type for MJPEG video

  //Constructor
  //----------
  public Client() {

    //###Build GUI

    //* frame
    f.addWindowListener(new WindowAdapter() {
       public void windowClosing(WindowEvent e) {
	 System.exit(0);
       }
    });

    //* init buttons linked to handlers
    buttonPanel.setLayout(new GridLayout(1,0));
    buttonPanel.add(registerButton);
    buttonPanel.add(tearButton);
    registerButton.addActionListener(new registerButtonListener());

    //* image display label
    iconLabel.setIcon(null);

    //* frame layout
    mainPanel.setLayout(null);
    mainPanel.add(iconLabel);
    mainPanel.add(buttonPanel);
    iconLabel.setBounds(0,0,380,280);
    buttonPanel.setBounds(0,280,380,50);

    f.getContentPane().add(mainPanel, BorderLayout.CENTER);
    f.setSize(new Dimension(390,370));
    f.setVisible(true);


    //###Init buffer
    //* allocate enough memory for the buffer used to receive data from the server
    buf = new byte[15000];
  }

  //Main
  //-----
  public static void main(String argv[]) throws Exception
  {
    //* create a Client object
    Client theClient = new Client();

    //* get server RTSP port and IP address from the command line
    int RTSP_server_port = Integer.parseInt(argv[1]);
    String ServerHost = argv[0];
    InetAddress ServerIPAddr = InetAddress.getByName(ServerHost);
    
    //* find free port
    theClient.RTP_RCV_PORT=findFreePort();

    //* establish a TCP connection with the server to exchange RTSP messages
    theClient.RTSPsocket = new Socket(ServerIPAddr, RTSP_server_port);

    //* set input and output stream filters:
    theClient.RTSPBufferedReader = new BufferedReader(new InputStreamReader(theClient.RTSPsocket.getInputStream()) );
    theClient.RTSPBufferedWriter = new BufferedWriter(new OutputStreamWriter(theClient.RTSPsocket.getOutputStream()) );
    
    System.out.println("Init Success");
  }
  
  //Handlers for buttons Setup,Play,Pause,Teardown
  //------------------------------------

  //### Setup button handler
  class registerButtonListener implements ActionListener{
    public void actionPerformed(ActionEvent e){

      System.out.println("Register Button pressed !");
      
      Register messRegister=new Register("tib", "test", 2993);
      
      try {
        RTSPBufferedWriter.write(messRegister.writeMessage());
        RTSPBufferedWriter.flush();
    } catch (IOException e1) {
        // TODO Auto-generated catch block
        e1.printStackTrace();
    }
      
      
	  

	  //* wait for the response
	  
    }
  }

  
  
  

 

  //Send RTSP Request
  //------------------------------------
  //*Send message to the server on the RTSP socket*
  //
  //*Example of message:*
  //
  //        PLAY movie.Mjpeg RTSP/1.0  
  //        CSeq: 2  
  //        Session: 123456  
  private void send_RTSP_request(String request_type)
  {
    try{
      //use the RTSPBufferedWriter to write to the RTSP socket

      //write the request line
      RTSPBufferedWriter.write(request_type+" "+VideoFileName+" "+"RTSP/1.0"+CRLF);

      //write the CSeq line
      RTSPBufferedWriter.write("CSeq: "+RTSPSeqNb+CRLF);

      //check if request type is equal to "SETUP" and in this case write the Transport: line advertising to the server the port used to receive the RTP packets RTP_RCV_PORT
      if(request_type.equals("SETUP")) {
          RTSPBufferedWriter.write("Transport: RTP/UDP; client_port= "+RTP_RCV_PORT+CRLF);
      }
      //otherwise, write the Session line from the RTSPid field
      else {
          RTSPBufferedWriter.write("Session: "+RTSPid+CRLF);
      }

      RTSPBufferedWriter.flush();
    }
    catch(Exception ex)
      {
	System.out.println("Exception caught: "+ex);
      }
  }
  
  public static int findFreePort()
  throws IOException {
      ServerSocket server =
          new ServerSocket(0);
      int port = server.getLocalPort();
      server.close();
      return port;
  }

}//end of Class Client

