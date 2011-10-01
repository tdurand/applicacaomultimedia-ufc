package pratica02;

import java.net.DatagramSocket;
import java.net.InetAddress;

public class Listener {
    
    private DatagramSocket datagramSocket;
    private int RTPclientPort;
    private InetAddress clientAddress;
    
    public Listener(DatagramSocket datagramSocket, int clientPort,
            InetAddress clientAddress) {
        super();
        this.datagramSocket = datagramSocket;
        this.RTPclientPort = clientPort;
        this.clientAddress = clientAddress;
    }
    public DatagramSocket getDatagramSocket() {
        return datagramSocket;
    }
    public void setDatagramSocket(DatagramSocket datagramSocket) {
        this.datagramSocket = datagramSocket;
    }
    public int getRTPClientPort() {
        return RTPclientPort;
    }
    public void setRTPClientPort(int RTPclientPort) {
        this.RTPclientPort = RTPclientPort;
    }
    public InetAddress getClientAddress() {
        return clientAddress;
    }
    public void setClientAddress(InetAddress clientAddress) {
        this.clientAddress = clientAddress;
    }

    
}
