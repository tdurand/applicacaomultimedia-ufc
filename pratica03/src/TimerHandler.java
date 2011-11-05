

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.Iterator;

public class TimerHandler implements ActionListener {
    
    public VideoStream videoStream;
    public int currentImageNb;
    public byte[] buf; //buffer used to store the images to send to the client
    
    //###Video constants:
    public static int MJPEG_TYPE = 26; //RTP payload type for MJPEG video
    public static int FRAME_PERIOD = 100; //Frame period of the video to stream, in ms
    public static int VIDEO_LENGTH = 500; //length of the video in framess
    
    public TimerHandler(String videoFileName) throws Exception {
        try {
            this.videoStream=new VideoStream(videoFileName);
        } catch (Exception e) {
            System.out.println("Wrong video path");
        }
        buf = new byte[15000];
        currentImageNb=0;
    }

    //Handler for timer
    //------------------------
    @Override
    public void actionPerformed(ActionEvent e) {
      System.out.println("Timer tick");
      //*if still have client connected
      if(Server.clientList.size() > 0) {
          //* if the current image nb is less than the length of the video
          if (currentImageNb < VIDEO_LENGTH)
            {
          //* update current imagenb
              currentImageNb++;
    
          try {
            //* get next frame to send from the video, as well as its size
            int image_length = videoStream.getnextframe(buf);
    
            //* builds an RTPpacket object containing the frame
            RTPpacket rtp_packet = new RTPpacket(MJPEG_TYPE, currentImageNb, currentImageNb*FRAME_PERIOD, buf, image_length);
    
            //* get to total length of the full rtp packet to send
            int packet_length = rtp_packet.getlength();
    
            //* retrieve the packet bitstream and store it in an array of bytes
            byte[] packet_bits = new byte[packet_length];
            rtp_packet.getpacket(packet_bits);
    
            //* For each client connected, send the packets
            for (Iterator<Listener> iterator = Server.clientList.iterator(); iterator.hasNext();) {
                Listener aClient = (Listener) iterator.next();
                System.out.println("Sending datagramPacket");
                aClient.getDatagramSocket().send(new DatagramPacket(packet_bits, packet_length,aClient.getClientAddress(),aClient.getRTPClientPort()));
            }
            //* print the header bitstream (just one time for all)
            rtp_packet.printheader();
          }
          catch(Exception ex)
            {
              System.out.println("Exception caught: "+ex);
              System.exit(0);
            }
            }
          else {
                //*if we have reached the end of the video file, reinit at the begining
                System.out.println("End of video, reinit");
                currentImageNb=0;
                videoStream.reset();
            }
        }
        else {
            System.out.println("There is no more clients connected, stopping timer");
            //*there is no more client connected
            //*reinit framenb
            currentImageNb=0;
            videoStream.reset();
            //*stop timer
            Server.timer.stop();
        }
    }
}
