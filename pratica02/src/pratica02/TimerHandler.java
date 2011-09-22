package pratica02;

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
        this.videoStream=new VideoStream(videoFileName);
    }

    //Handler for timer
    //------------------------
    @Override
    public void actionPerformed(ActionEvent e) {
      //*if still have client connected
      if(Server.datagramSocketList.size() > 0) {
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
            for (Iterator<DatagramSocket> iterator = Server.datagramSocketList.iterator(); iterator.hasNext();) {
                DatagramSocket aDatagramSocket = (DatagramSocket) iterator.next();
                aDatagramSocket.send(new DatagramPacket(packet_bits, packet_length));
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
                currentImageNb=0;
            }
        }
        else {
            //*there is no more client connected
            //*reinit framenb
            currentImageNb=0;
            //*stop timer
            Server.timer.stop();
        }
    }
}
