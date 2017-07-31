package org.kj.pxe.protocol.impl;

import java.net.InetAddress;
import java.util.List;

/**
 * Contains the information for one subset of the BootServers suboption - the
 * list of servers of a particular type.
 * 
 * @author Kinjal
 *
 */
public class BootServerInformation {
	short type;
	List<InetAddress> serverList;

	/**
	 * Constructs a BootServerInformation for the given type and list of servers
	 * (by IP address)
	 * 
	 * @param type
	 *            the server type
	 * @param serverList
	 *            the list of servers of the given type
	 */
	public BootServerInformation(short type, List<InetAddress> serverList) {
		this.type = type;
		this.serverList = serverList;
	}

	/**
	 * Returns the type
	 * 
	 * @return the type
	 */
	short getType() {
		return type;
	}

	/**
	 * Returns the list of Boot servers by IP address.
	 * 
	 * @return the list of boot servers
	 */
	List<InetAddress> getServerList() {
		return serverList;
	}
}
