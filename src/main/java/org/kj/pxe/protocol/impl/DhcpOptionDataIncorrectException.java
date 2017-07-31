package org.kj.pxe.protocol.impl;

/**
 * Exception to be thrown when the data in a DHCP option is not the correct
 * value
 * 
 * @author Kinjal
 *
 */
public class DhcpOptionDataIncorrectException extends PxeDhcpException {
	private static final long serialVersionUID = 1L;

	/**
	 * Constructs a DhcpOptionDataIncorrectException with the given error
	 * message
	 * 
	 * @param message the error message
	 */
	DhcpOptionDataIncorrectException(String message) {
		super(message);
	}
}
