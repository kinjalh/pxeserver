package org.kj.pxe.config;

import java.net.InetAddress;

/**
 * Provides access to the DHCP packet settings:
 * 
 * @author Kinjal
 *
 */
public interface DhcpSettings {

	int getDiscoverPort();

	int getOfferPort();

	int getRequestPort();

	int getAckPort();

	String getBootFileName();

	InetAddress getBootServerAddress();

	byte getVendorOptionDiscoveryControl();

	InetAddress getVendorOptionBootServer();

	String getVendorOptionBootMenu();

	short[] getVendorOptionBootItem();

	int getPoolCount();

	String getIpxeBootFileName();

	String getGpxeBootFileName();

	String getVendorOptionMenuPrompt();

	String getOption93BootFileName();

	boolean getIpxeOfferToTftpRedirect();
}