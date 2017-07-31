package org.kj.pxe.server;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;

import org.kj.pxe.config.DhcpSettings;

/**
 * Provides sockets used to send offer or ack messages.
 * 
 * @author Kinjal
 *
 */
class SocketFactoryImpl implements SocketFactory {

	private DhcpSettings settings;

	/**
	 * Constructs a {@link SocketFactoryImpl} with the given settings.
	 * 
	 * @param settings
	 *            the settings
	 */
	SocketFactoryImpl(DhcpSettings settings) {
		this.settings = settings;
	}

	/**
	 * Returns a socket connected to address 255.255.255.255, with setBroadcast
	 * set to true, and connected to the port specified in settings.
	 * 
	 * @throws IOException
	 *             if there is an error creating the socket
	 */
	@Override
	public DatagramSocket getDhcpOfferSocket() throws IOException {
		DatagramSocket socket = new DatagramSocket();
		socket.setBroadcast(true);
		socket.connect(InetAddress.getByName("255.255.255.255"), settings.getOfferPort());
		return socket;
	}

	/**
	 * Returns a socket connected to the specified address and connected to the
	 * port specified in settings.
	 * 
	 * @param address
	 *            the address to connect to
	 * @throws IOException
	 *             if there is an error creating the socket
	 */
	@Override
	public DatagramSocket getDhcpAckSocket(InetAddress address) throws IOException {
		DatagramSocket socket = new DatagramSocket();
		socket.connect(address, settings.getAckPort());
		return socket;
	}

}
