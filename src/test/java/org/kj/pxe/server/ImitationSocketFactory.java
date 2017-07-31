package org.kj.pxe.server;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import org.kj.pxe.config.DhcpSettings;

class ImitationSocketFactory implements SocketFactory {

    private DhcpSettings settings;
    
    public ImitationSocketFactory(DhcpSettings settings) {
		this.settings = settings;
	}

	@Override
    public DatagramSocket getDhcpOfferSocket() throws SocketException {
        DatagramSocket socket = null;
        try {
            socket = new ImitationSocket();
            socket.setBroadcast(true);
            socket.connect(InetAddress.getByName("255.255.255.255"), settings.getOfferPort());
        } catch (SocketException | UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return socket;
    }

    @Override
    public DatagramSocket getDhcpAckSocket(InetAddress address) {
        DatagramSocket socket = null;
        try {
            socket = new ImitationSocket();
            socket.setBroadcast(false);
            socket.connect(address, settings.getAckPort());
        } catch (SocketException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return socket;
    }

}
