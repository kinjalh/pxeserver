package org.kj.pxe.protocol.impl;

/**
 * Exception when converting a DHCP packet to bytes.
 * 
 */
public class DhcpPacketToByteConversionException extends PxeDhcpException {

	private static final long serialVersionUID = 1L;

	/**
	 * Constructs a DhcpPacketToByteConversionException with the encapsulated
	 * exception.
	 * 
	 * @param e
	 *            the encapsulated exception
	 */
	DhcpPacketToByteConversionException(Exception e) {
		super(e);
	}

}
