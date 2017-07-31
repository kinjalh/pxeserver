package org.kj.pxe.protocol;

/**
 * Gets the data from a Datagram packet and returns as byte array. The source of
 * the packet is implementation dependent.
 * 
 * @author Kinjal
 *
 */
public interface DatagramPacketReader {

	/**
	 * Returns packet's data as byte array
	 * 
	 * @return packet datay as byet array
	 */
	byte[] getPacketData();

}
