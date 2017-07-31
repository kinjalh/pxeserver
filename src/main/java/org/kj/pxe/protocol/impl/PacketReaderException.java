package org.kj.pxe.protocol.impl;

import java.io.IOException;

/**
 * Exception thrown when reading a DHCP packet from a source.
 * 
 * @author Kinjal
 *
 */
class PacketReaderException extends PxeDhcpException {

	private static final long serialVersionUID = 1L;

	/**
	 * Constructs a PacketReaderException with an encapsulate IOexception
	 * 
	 * @param e
	 *            the encapsulated exception
	 */
	PacketReaderException(IOException e) {
		super(e);
	}

}
