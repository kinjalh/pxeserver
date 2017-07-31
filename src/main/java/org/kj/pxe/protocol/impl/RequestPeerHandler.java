package org.kj.pxe.protocol.impl;

import java.net.DatagramSocket;
import java.net.InetAddress;
import org.kj.pxe.config.DhcpSettings;
import org.kj.pxe.protocol.DatagramPacketWriter;
import org.kj.pxe.protocol.DhcpAckPacket;
import org.kj.pxe.protocol.DhcpRequestPacket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestPeerHandler implements Runnable {

	private byte[] bytes;
	private InetAddress packetSourceAddress;
	private DatagramSocket localSocket;
	private DhcpSettings settings;
	private static Logger logger = LoggerFactory.getLogger(RequestPeerHandler.class);

	public RequestPeerHandler(DhcpSettings settings, byte[] bytes, InetAddress address, DatagramSocket localSocket) {
		super();
		this.settings = settings;
		this.bytes = bytes;
		this.packetSourceAddress = address;
		this.localSocket = localSocket;
	}

	/**
	 * Builds a DHCP Request packet from the given bytes. Then builds a DHCP Ack
	 * packet in response to the request packet. Then sends the DHCP Ack to the
	 * IP Address the DHCP Request came from, on the port specified by the
	 * configuration file.
	 * 
	 * @param bytes
	 *            the bytes used to build the DHCP Request
	 * @param address
	 *            the address that the request came from. The ack is sent to
	 *            this address
	 */
	void dealWithPacket(byte[] bytes, InetAddress address) {
		DhcpRequestPacket request = new DhcpRequestPacketImpl();
		request.readFromBytes(bytes);

		DhcpAckPacket ack = new DhcpAckPacketImpl(settings);
		ack.buildAck(request);

		PacketModifier mod = new PacketModifier(settings);
		mod.modifyReply(request, ack);

		logger.debug("SENDING ACK ON ADDRESS " + address + "	PORT: " + settings.getAckPort());

		DatagramPacketWriter writer = new DatagramPacketWriterImpl();
		writer.writePacket(ack.convertToBytes(), ack.convertToBytes().length, localSocket, address,
				settings.getAckPort());

	}

	/**
	 * Builds a DHCP request using the bytes given. Then builds an ack and
	 * returns it. Nothing sent over network. Used for testing.
	 * 
	 * @param bytes
	 *            the input bytes for the DHCP request
	 * @return the ack packet response
	 */
	DhcpAckPacket receiveBytes(byte[] bytes) {
		DhcpRequestPacket request = new DhcpRequestPacketImpl();
		request.readFromBytes(bytes);

		DhcpAckPacket ack = new DhcpAckPacketImpl(settings);
		try {
			ack.buildAck(request);
		} catch (PxeDhcpException e) {
			throw new DhcpPacketConstructionException(e);
		}
		return ack;
	}

	/**
	 * Calls dealWithPacket using the bytes and packetSourceAddress fields as
	 * parameters. Any exception is caught and recorded. The program does not
	 * terminate.
	 */
	@Override
	public void run() {
		try {
			dealWithPacket(bytes, packetSourceAddress);
		} catch (Exception e) {
			logger.error("error dealing with request packet", e);
		}
	}
}
