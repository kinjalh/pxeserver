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
import org.kj.pxe.protocol.impl.DiscoverPeerHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Server that receives DHCP Discovers and sends offers. Runs until program
 * terminated.
 * 
 * @author Kinjal
 *
 */
class DiscoverServer implements Runnable {

	private static final int MAX__DHCP_PACKET_SIZE = 2048;
	private static Logger logger = LoggerFactory.getLogger(DiscoverServer.class);
	private DhcpSettings settings;

	/**
	 * Constructs a DiscoverServer using the given settings
	 * 
	 * @param settings
	 *            the settings to use
	 */
	DiscoverServer(DhcpSettings settings) {
		this.settings = settings;
	}

	/**
	 * Opens a socket on a port specified by the settings. If socket is
	 * successfully opened, receives discovers and sends offers.
	 */
	@Override
	public void run() {
		ExecutorService pool = Executors.newFixedThreadPool(settings.getPoolCount());

		DatagramSocket socket = null;
		try {
			logger.debug("discover port = " + settings.getDiscoverPort());
			SocketAddress address = new InetSocketAddress(InetAddress.getByName("0.0.0.0"), settings.getDiscoverPort());
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
				logger.debug("RECEIVED DISCOVER");
				pool.execute(new DiscoverPeerHandler(settings, packet.getData(), socket));
			} catch (IOException e) {
				logger.debug(e.getMessage());
			}
		}
	}

}
