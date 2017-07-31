package org.kj.pxe.server;

/**
 * Exception when reading properties file.
 * 
 * @author Kinjal
 *
 */
public class DhcpPropertiesReadingException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	/**
	 * Constructs a DhcpPropertiesReadingException with the encapsulated
	 * Exception
	 * 
	 * @param e
	 *            the encapsulated Exception
	 */
	public DhcpPropertiesReadingException(Exception e) {
		super(e);
	}

	/**
	 * Constructs a DhcpPropertiesReadingException with the given error message.
	 * 
	 * @param string
	 *            the error message
	 */
	public DhcpPropertiesReadingException(String string) {
		super(string);
	}

}
