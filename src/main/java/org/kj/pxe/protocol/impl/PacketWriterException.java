package org.kj.pxe.protocol.impl;

import java.io.IOException;

/**
 * Exception thrown when writing a DHCP packet to a destination.
 * 
 * @author Kinjal
 *
 */
class PacketWriterException extends PxeDhcpException {

	private static final long serialVersionUID = 1L;

	/**
	 * Constructs a PacketWriterException with an encapsulated IOException
	 * 
	 * @param e
	 *            the encapsulated exception
	 */
	PacketWriterException(IOException e) {
		super(e);
	}
}
