package org.kj.pxe.server;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class ImitationSocket extends DatagramSocket {

    private InetAddress address;
    private int port;
    boolean broadcast;
    
    // Broadcast is set to true by default
    public ImitationSocket() throws SocketException {
        super();
        this.broadcast = true;
    }
    
    @Override
    public void connect(InetAddress address, int port) {
        this.address = address;
        this.port = port;
    }
    
    @Override
    public int getPort() {
        return this.port;
    }
    
    @Override
    public InetAddress getInetAddress() {
        return this.address;
    }
    
    @Override
    public void setBroadcast(boolean broadcast) {
        this.broadcast = broadcast;
    }
    
    @Override
    public boolean getBroadcast() {
        return this.broadcast;
    }
}
