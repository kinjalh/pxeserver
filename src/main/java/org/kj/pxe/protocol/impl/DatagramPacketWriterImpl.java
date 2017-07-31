package org.kj.pxe.protocol.impl;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import org.kj.pxe.protocol.DatagramPacketWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Writes a datagrampacket to the network.
 * 
 * @author Kinjal
 *
 */
class DatagramPacketWriterImpl implements DatagramPacketWriter {
	private static Logger logger = LoggerFactory.getLogger(DatagramPacketWriterImpl.class);

	/**
	 * Creates a datagram packet with the specified data and length and sends it
	 * to the port and IP address that the socket is connected to.
	 * 
	 * @param buffer
	 *            the data to send
	 * @param length
	 *            the length of the data to send
	 * @param socket
	 *            the socket to send the packet through. Must be connected to a
	 *            port and IP address
	 * @throws PacketWriterException
	 */
	@Override
	public void writePacket(byte[] buffer, int length, DatagramSocket socket) {
		logger.debug("socket connected to address: " + socket.getInetAddress() + " port: " + socket.getPort());
		DatagramPacket packet = new DatagramPacket(buffer, length);

		try {
			socket.send(packet);
		} catch (IOException e) {
			logger.error("error sending packet with data: " + buffer + " to port " + socket.getPort() + " address "
					+ socket.getInetAddress(), e);
			throw new PacketWriterException(e);
		}
	}

	/**
	 * Sends the given buffer of the given length from the given socket to the
	 * given address and port.
	 * 
	 * @param buffer
	 *            the byte array of data to send
	 * @param length
	 *            the length of the data to be sent in the packet
	 * @param socket
	 *            the socket to send from
	 * @param address
	 *            the remote address to send to
	 * @param port
	 *            the remote port to send to
	 */
	@Override
	public void writePacket(byte[] buffer, int length, DatagramSocket socket, InetAddress address, int port) {
		logger.debug("socket connected to address: " + socket.getInetAddress() + " port: " + socket.getPort());
		DatagramPacket packet = new DatagramPacket(buffer, length, address, port);
		logger.debug("sending packet to address: " + address + " port: " + port);
		packet.setPort(port);
		packet.setAddress(address);

		try {
			socket.send(packet);
		} catch (IOException e) {
			logger.error("error sending packet with data: " + buffer + " to port " + socket.getPort() + " address "
					+ socket.getInetAddress(), e);
			throw new PacketWriterException(e);
		}
	}

}
