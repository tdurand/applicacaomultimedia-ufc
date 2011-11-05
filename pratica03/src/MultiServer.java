

//How to use the MultiServer
//----------------
//1.    Compile with javac 
//2.    Run : *java MultiServer [Server RTSP listening port]*
//
//      example: `java MultiServer 3000`
//
// You can connect clients !

import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.util.ArrayList;

import javax.swing.Timer;

public class MultiServer {
    
    public static final int DEFAULT_PORT=3000;
    public static final String DEFAULT_VIDEO="src/pratica02/movie.Mjpeg";

    public static void main(String argv[]) throws Exception
    {
        int port;
        //*get the port from command line or setup on the default port
        if(argv.length==0) port=DEFAULT_PORT; else port=Integer.parseInt(argv[0]);
        //*create the socket
        ServerSocket listenSocket=new ServerSocket(port);
        //*init the timer
        TimerHandler timerHandler=new TimerHandler(DEFAULT_VIDEO);
        Server.timer = new Timer(TimerHandler.FRAME_PERIOD,timerHandler);
        Server.timer.setInitialDelay(0);
        Server.timer.setCoalesce(true);
        
        //* listen infinitely on port "RTSP" port, and instantiates a Server in a new Thread if a client wants to be connected 
        while(true) {
            Server newServer=new Server(listenSocket.accept());
            new Thread(newServer).start();
        }
        
    }

}
