package org.kj.pxe.protocol.impl;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

import org.kj.pxe.protocol.DatagramPacketReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Receives a Datagram Packet from a given socket and returns the data for that
 * Datagram.
 * 
 * @author Kinjal
 *
 */
class DatagramPacketReaderImpl implements DatagramPacketReader {

	private static Logger logger = LoggerFactory.getLogger(DatagramPacketReaderImpl.class);
	private static final int MAX_PACKET_LENGTH = 2048;

	private DatagramSocket socket;

	/**
	 * Constructs a DatagramPacketReaderImpl that receives from the given socket
	 * 
	 * @param socket
	 *            the socket to receive from
	 */
	DatagramPacketReaderImpl(DatagramSocket socket) {
		this.socket = socket;
	}

	/**
	 * Receives a packet and returns its data. The packet is received from the
	 * socket in the constructor.
	 * 
	 * @throws PacketReaderException
	 *             if there is an IO Exception while receiving the packet.
	 */
	@Override
	public byte[] getPacketData() {
		byte[] buffer = new byte[MAX_PACKET_LENGTH];
		DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

		try {
			socket.receive(packet);
			logger.debug("Read " + packet.getLength() + " from port " + socket.getPort() + " address "
					+ socket.getInetAddress());
		} catch (IOException e) {
			logger.error("Error receiving data from socket to packet. Socket: port = " + socket.getPort() + " adress = "
					+ socket.getLocalAddress(), e);
			throw new PacketReaderException(e);
		}

		return packet.getData();
	}

}
