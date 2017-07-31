package org.kj.pxe.protocol.impl;

/**
 * Exception thrown thrown when a DHCP option with a certain tag is not found
 * 
 * @author Kinjal
 *
 */
public class DhcpTagNotFoundException extends PxeDhcpException {

	private static final long serialVersionUID = 1L;

	/**
	 * Constructs a DhcpTagNotFoundException with the given error message
	 * 
	 * @param message
	 */
	DhcpTagNotFoundException(String message) {
		super(message);
	}
}
