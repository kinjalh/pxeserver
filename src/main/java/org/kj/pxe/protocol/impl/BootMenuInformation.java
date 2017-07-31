package org.kj.pxe.protocol.impl;

/**
 * Contains the information for one Boot Menu suboption subset - a server type,
 * and a String value description.
 * 
 * @author Kinjal
 *
 */
class BootMenuInformation {

	private short type;
	private String description;

	/**
	 * Constructs a BootMenuInformation with the given type and description
	 * @param type the boot server type
	 * @param descriptions the description
	 */
	BootMenuInformation(short type, String descriptions) {
		this.type = type;
		this.description = descriptions;
	}

	/**
	 * Returns the server type value
	 * @return the type
	 */
	short getType() {
		return type;
	}

	/**
	 * Returns the description
	 * @return the description
	 */
	String getDescription() {
		return description;
	}

}
