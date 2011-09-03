package pratica01;

import java.net.ServerSocket;

public class MultiServer {
    
  //------------------------------------
    //main
    //------------------------------------
    public static void main(String argv[]) throws Exception
    {
        boolean listening=true;
        
        //get RTSP socket port from the command line
        int RTSPport = Integer.parseInt(argv[0]);

        //create the socket to listen to 
        ServerSocket listenSocket = new ServerSocket(RTSPport);
        
        while(listening) {
            new ServerThread(listenSocket.accept()).start();
        }
        
    }

}
