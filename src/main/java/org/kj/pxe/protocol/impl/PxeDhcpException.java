package org.kj.pxe.protocol.impl;

/**
 * Exception thrown when there is an error PXE booting using the DHCP protocol
 * 
 * @author Kinjal
 *
 */
public class PxeDhcpException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	/**
	 * Constructs a PxeDhcpException encapsulating the given exception.
	 * 
	 * @param e
	 *            the given exception
	 */
	PxeDhcpException(Exception e) {
		super(e);
	}

	/**
	 * Constructs a PxeDhcpException with the given error message
	 * 
	 * @param string
	 *            the error message
	 */
	PxeDhcpException(String string) {
		super(string);
	}
}
