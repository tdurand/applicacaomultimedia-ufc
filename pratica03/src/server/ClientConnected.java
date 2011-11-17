package server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class ClientConnected {
    
    private int clientclientCommunicationPort;
    private String clientAddress;
    private BufferedWriter buffWriter;
    private BufferedReader buffReader;
    private String clientName;
    public String caller;
    
    public ClientConnected(int clientclientCommunicationPort,
            InetAddress clientAddress, BufferedWriter buffWriter,
            BufferedReader buffReader, String clientName) {
        super();
        this.clientclientCommunicationPort = clientclientCommunicationPort;
        this.clientAddress = clientAddress.getCanonicalHostName();
        this.buffWriter = buffWriter;
        this.buffReader = buffReader;
        this.clientName = clientName;
    }


    /**
     * @return the clientclientCommunicationPort
     */
    public int getClientclientCommunicationPort() {
        return clientclientCommunicationPort;
    }


    /**
     * @param clientclientCommunicationPort the clientclientCommunicationPort to set
     */
    public void setClientclientCommunicationPort(int clientclientCommunicationPort) {
        this.clientclientCommunicationPort = clientclientCommunicationPort;
    }


    /**
     * @return the clientAddress
     */
    public String getClientAddress() {
        return clientAddress;
    }


    /**
     * @param clientAddress the clientAddress to set
     */
    public void setClientAddress(String clientAddress) {
        this.clientAddress = clientAddress;
    }


    /**
     * @return the buffWriter
     */
    public BufferedWriter getBuffWriter() {
        return buffWriter;
    }


    /**
     * @param buffWriter the buffWriter to set
     */
    public void setBuffWriter(BufferedWriter buffWriter) {
        this.buffWriter = buffWriter;
    }


    /**
     * @return the clientName
     */
    public String getClientName() {
        return clientName;
    }


    /**
     * @param clientName the clientName to set
     */
    public void setClientName(String clientName) {
        this.clientName = clientName;
    }


    /**
     * @return the buffReader
     */
    public BufferedReader getBuffReader() {
        return buffReader;
    }


    /**
     * @param buffReader the buffReader to set
     */
    public void setBuffReader(BufferedReader buffReader) {
        this.buffReader = buffReader;
    }
    
    
    
    
    

    
}
