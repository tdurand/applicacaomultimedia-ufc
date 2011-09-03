package pratica01;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class ServerThread extends Thread {
    
    private Socket socket;
    private ServerBean server;

    public ServerThread(Socket socket) {
        this.socket=socket;
        this.server=new ServerBean();
    }

    @Override
    public void run() {

        //show GUI:
        server.pack();
        server.setVisible(true);

        //Initiate TCP connection with the client for the RTSP session
        server.RTSPsocket = socket;

        //Get Client IP address
        server.ClientIPAddr = server.RTSPsocket.getInetAddress();

        //Initiate RTSPstate
        server.state = server.INIT;

        //Set input and output stream filters:
        try {
            server.RTSPBufferedReader = new BufferedReader(new InputStreamReader(server.RTSPsocket.getInputStream()) );
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        try {
            server.RTSPBufferedWriter = new BufferedWriter(new OutputStreamWriter(server.RTSPsocket.getOutputStream()) );
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        //Wait for the SETUP message from the client
        int request_type;
        boolean done = false;
        while(!done)
          {
        request_type = server.parse_RTSP_request(); //blocking

        if (request_type == server.SETUP)
          {
            done = true;

            //update RTSP state
            server.state = server.READY;
            System.out.println("New RTSP state: READY");

            //Send response
            server.send_RTSP_response();

            //init the VideoStream object:
            try {
                server.video = new VideoStream(server.VideoFileName);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            //init RTP socket
            try {
                server.RTPsocket = new DatagramSocket();
            } catch (SocketException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
          }
          }

        boolean threadON=true;
        
         //loop to handle RTSP requests
        while(threadON)
          {
        //parse the request
        request_type = server.parse_RTSP_request(); //blocking

        if ((request_type == server.PLAY) && (server.state == server.READY))
          {
            //send back response
            server.send_RTSP_response();
            //start timer
            server.timer.start();
            //update state
            server.state = server.PLAYING;
            System.out.println("New RTSP state: PLAYING");
          }
        else if ((request_type == server.PAUSE) && (server.state == server.PLAYING))
          {
            //send back response
            server.send_RTSP_response();
            //stop timer
            server.timer.stop();
            //update state
            server.state = server.READY;
            System.out.println("New RTSP state: READY");
          }
        else if (request_type == server.TEARDOWN)
          {
            //send back response
            server.send_RTSP_response();
            //stop timer
            server.timer.stop();
            
            server.setVisible(false);
            threadON=false;
            //close sockets
            
            try {
                server.RTSPsocket.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            server.RTPsocket.close();
            
          }
          }
    }
    
    

}
