package pratica01;

//Description
//----------
// A ServerThread is instantiated in a Thread for each Client connected to MultiServer.
//
// It basicaly set up a RTSP connection with the Client, and instantiate a Server.java to handle the RTP connection. 
//
// After it loops indefinitely to handle the Client RTSP messages : SETUP-PLAY-PAUSE-TEARDOWN.

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
    private Server server;

    public ServerThread(Socket socket) {
        this.socket=socket;
        this.server=new Server();
    }
    //Run: (actions performed when the Thread is launched)
    //-------
    @Override
    public void run() {
        
        //* show GUI:
        server.pack();
        server.setVisible(true);

        //* Initiate TCP connection with the client for the RTSP session
        server.RTSPsocket = socket;

        //* Get Client IP address
        server.ClientIPAddr = server.RTSPsocket.getInetAddress();

        //* Initiate RTSPstate
        server.state = server.INIT;

        //* Set input and output stream filters:
        try {
            server.RTSPBufferedReader = new BufferedReader(new InputStreamReader(server.RTSPsocket.getInputStream()) );
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        try {
            server.RTSPBufferedWriter = new BufferedWriter(new OutputStreamWriter(server.RTSPsocket.getOutputStream()) );
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        //###Wait for the SETUP message
        int request_type;
        boolean done = false;
        while(!done)
          {
        request_type = server.parse_RTSP_request(); //blocking

        if (request_type == server.SETUP)
          {
            done = true;

            //* update RTSP state
            server.state = server.READY;
            System.out.println("New RTSP state: READY");

            //* send response
            server.send_RTSP_response();

            //* init the VideoStream object:
            try {
                server.video = new VideoStream(server.VideoFileName);
            } catch (Exception e) {
                e.printStackTrace();
            }

            //* init RTP socket
            try {
                server.RTPsocket = new DatagramSocket();
            } catch (SocketException e) {
                e.printStackTrace();
            }
          }
          }

        boolean threadON=true;
        
         //###Loop indefinitely to handle RTSP requests
        while(threadON)
          {
           //* Parse the request
        request_type = server.parse_RTSP_request(); //blocking
           //* if PLAY
        if ((request_type == server.PLAY) && (server.state == server.READY))
          {
            //>*send back response*
            server.send_RTSP_response();
            //>*start timer*
            server.timer.start();
            //>*update state*
            server.state = server.PLAYING;
            System.out.println("New RTSP state: PLAYING");
          }
            //* if PAUSE
        else if ((request_type == server.PAUSE) && (server.state == server.PLAYING))
          {
            //>*send back response*
            server.send_RTSP_response();
            //>*stop timer*
            server.timer.stop();
            //>*update state*
            server.state = server.READY;
            System.out.println("New RTSP state: READY");
          }
            //* if TEARDOWN
        else if (request_type == server.TEARDOWN)
          {
            //>*send back response*
            server.send_RTSP_response();
            //>*stop timer*
            server.timer.stop();
            
            server.setVisible(false);
            threadON=false;
            //>*close sockets, end thread*
            
            try {
                server.RTSPsocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            server.RTPsocket.close();
            
          }
          }
    }
    
    

}
