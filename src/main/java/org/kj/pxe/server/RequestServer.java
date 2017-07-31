package org.kj.pxe.server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.kj.pxe.config.DhcpSettings;
import org.kj.pxe.protocol.impl.RequestPeerHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Server that receives DHCP Requests and sends acks. Runs until program
 * terminated.
 * 
 * @author Kinjal
 *
 */
class RequestServer implements Runnable {

	private static final int MAX__DHCP_PACKET_SIZE = 2048;
	private static Logger logger = LoggerFactory.getLogger(RequestServer.class);
	private DhcpSettings settings;

	/**
	 * Constructs a RequestServer using the given settings
	 * 
	 * @param settings
	 *            the settings to use
	 */
	RequestServer(DhcpSettings settings) {
		this.settings = settings;
	}

	/**
	 * Opens a socket on a port specified by the settings. If socket is
	 * successfully opened, receives requests and sends acks.
	 */
	@Override
	public void run() {
		ExecutorService pool = Executors.newFixedThreadPool(settings.getPoolCount());

		DatagramSocket socket = null;
		try {
			logger.debug("request port = " + settings.getRequestPort());
			SocketAddress address = new InetSocketAddress(InetAddress.getByName("0.0.0.0"), settings.getRequestPort());
			socket = new DatagramSocket(address);
			socket.setBroadcast(true);
		} catch (IOException e) {
			logger.error(e.getMessage());
			throw new RuntimeException(e);
		}

		DatagramPacket packet = new DatagramPacket(new byte[MAX__DHCP_PACKET_SIZE], MAX__DHCP_PACKET_SIZE);

		while (true) {
			try {
				socket.receive(packet);
				logger.debug("RECEIVED REQUEST. packet address: " + packet.getAddress());
				pool.execute(new RequestPeerHandler(settings, packet.getData(), packet.getAddress(), socket));
			} catch (IOException e) {
				logger.error(e.getMessage());
			}
		}
	}

}
