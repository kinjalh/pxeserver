package org.kj.pxe.protocol.impl;

/**
 * Exception when building a DHCP packet
 */
class DhcpPacketConstructionException extends PxeDhcpException {

	private static final long serialVersionUID = 1L;

	/**
	 * Constructs a DhcpPacketConstructionException with the given error message
	 * 
	 * @param e
	 *            an exception encapsulated by this exception
	 */
	DhcpPacketConstructionException(Exception e) {
		super(e);
	}

}
