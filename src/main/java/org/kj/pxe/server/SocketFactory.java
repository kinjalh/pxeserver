package org.kj.pxe.server;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * Provides sockets for DHCP offers and DHCP acks.
 * 
 * @author Kinjal
 *
 */
public interface SocketFactory {

	/**
	 * Should be used to create a socket used to either send or receive an
	 * offer.
	 * 
	 * @return the socket
	 * @throws IOException
	 *             if there was an error creating the socket
	 */
	DatagramSocket getDhcpOfferSocket() throws IOException;

	/**
	 * Should be used to create a socket used to either send or receive an ack.
	 * 
	 * @param address
	 *            the address to connect the socket to
	 * @return the socket
	 * @throws IOException
	 *             if there was an error creating the socket
	 */
	DatagramSocket getDhcpAckSocket(InetAddress address) throws IOException;
}
