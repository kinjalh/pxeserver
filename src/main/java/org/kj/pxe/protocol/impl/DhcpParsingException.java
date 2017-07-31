package org.kj.pxe.protocol.impl;

/**
 * Exception thrown when there is an error parsing a DHCP packet from bytes
 */
class DhcpParsingException extends PxeDhcpException {

	/**
	 * Constructs a DhcpParsingException with the given error message
	 * 
	 * @param string
	 *            the message
	 */
	DhcpParsingException(String string) {
		super(string);
	}

	/**
	 * Constructs a DhcpParsingException with the given encapsulated exception
	 * 
	 * @param e
	 *            the encapsulated exception
	 */
	DhcpParsingException(Exception e) {
		super(e);
	}

	private static final long serialVersionUID = 1L;
}
