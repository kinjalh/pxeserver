package org.kj.pxe.protocol.impl;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;

import org.kj.pxe.config.DhcpSettings;
import org.kj.pxe.protocol.DatagramPacketWriter;
import org.kj.pxe.protocol.DhcpDiscoverPacket;
import org.kj.pxe.protocol.DhcpOfferPacket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Receives a DHCP discover message and sends and offer as reply
 * 
 * @author Kinjal
 *
 */
public class DiscoverPeerHandler implements Runnable {

	private byte[] bytes;
	private DhcpSettings settings;
	private DatagramSocket socket;
	private static Logger logger = LoggerFactory.getLogger(DiscoverPeerHandler.class);

	/**
	 * Constructs a DiscoverPeerHandler with the given values.
	 * 
	 * @param settings
	 *            the settings to use to set the packet values
	 * @param bytes
	 *            the bytes that should be used to build the discover packet
	 * @param socket
	 *            the socket to send the offer packet with
	 */
	public DiscoverPeerHandler(DhcpSettings settings, byte[] bytes, DatagramSocket socket) {
		super();
		this.bytes = bytes;
		this.settings = settings;
		this.socket = socket;
	}

	/**
	 * Reads a DHCP discover packet from the bytes, builds a discover as a
	 * reply, and sends the discover
	 * 
	 * @param bytes
	 *            the bytes to build the discover packet with
	 * @throws IOException
	 *             if there is an error parsing or sending the packet
	 */
	void dealWithPacket(byte[] bytes) throws IOException {
		DhcpDiscoverPacket discover = new DhcpDiscoverPacketImpl();
		discover.readFromBytes(bytes);

		DhcpOfferPacket offer = new DhcpOfferPacketImpl(settings);
		offer.buildOffer(discover);

		PacketModifier mod = new PacketModifier(settings);
		mod.modifyReply(discover, offer);

		DatagramPacketWriter writer = new DatagramPacketWriterImpl();
		writer.writePacket(offer.convertToBytes(), offer.convertToBytes().length, socket,
				InetAddress.getByName("255.255.255.255"), settings.getOfferPort());
	}

	/**
	 * Builds a discover packet from the bytes and returns the DHCP Offer built
	 * as a reply. Does not send anything over network. Used for testing.
	 * 
	 * @param bytes
	 *            the bytes comprising the discover packet
	 * @return the offer packet
	 */
	DhcpOfferPacket receiveBytes(byte[] bytes) {
		DhcpDiscoverPacket discover = new DhcpDiscoverPacketImpl();
		discover.readFromBytes(bytes);

		DhcpOfferPacket offer = new DhcpOfferPacketImpl(settings);
		try {
			offer.buildOffer(discover);
		} catch (PxeDhcpException e) {
			logger.error(e.getMessage());
		}
		return offer;
	}

	/**
	 * Calls dealwithPacket with the parameter set to the bytes field. If any
	 * exception is thrown, logs the exception and does NOT exit the program.
	 */
	@Override
	public void run() {
		try {
			dealWithPacket(bytes);
		} catch (Exception e) {
			logger.error("error: ", e);
		}
	}

}
