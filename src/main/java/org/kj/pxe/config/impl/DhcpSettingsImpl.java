package org.kj.pxe.config.impl;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Properties;

import org.kj.pxe.config.DhcpSettings;
import org.kj.pxe.server.DhcpPropertiesReadingException;

/**
 * Gets the configurations from the dhcp.properties file.
 * 
 * @author Kinjal
 *
 */
public class DhcpSettingsImpl implements DhcpSettings {

	private Properties props;

	private int DEFAULT_DISCOVER_PORT;
	private int DEFAULT_OFFER_PORT;
	private int DEFAULT_REQUEST_PORT;
	private int DEFAULT_ACK_PORT;
	private String DEFAULT_BOOT_FILE_NAME;
	private InetAddress DEFAULT_BOOT_SERVER_ADDRESS;
	private byte DEFAULT_DISCOVERY_CONTROL;
	private InetAddress DEFAULT_SERVER_OPTION;
	private String DEFAULT_BOOT_MENU;
	private short[] DEFAULT_BOOT_ITEM;
	private int DEFAULT_POOL_COUNT;
	private String DEFAULT_IPXE_BOOT_FILE_NAME;
	private String DEFAULT_GPXE_BOOT_FILE_NAME;
	private String DEFAULT_OPTION_93_BOOT_FILE_NAME;
	private boolean DEFAULT_IPXE_OFFER_TO_TFTP_REDIRECT;

	/**
	 * Constructs a DhcpSettingsImpl that loads the properties from
	 * dhcp.properties
	 * 
	 * @param fileName
	 *            the name of the file to read the properties from
	 * 
	 * @throws IOException
	 *             if there is an error initializing a default InetAddress field
	 *             - the default boot server address and the default server
	 *             option address
	 * 
	 * @throws DhcpPropertiesReadingException
	 *             if the file is not found
	 */
	public DhcpSettingsImpl(String fileName) throws IOException {
		props = new Properties();
		readProperties(fileName);

		DEFAULT_DISCOVER_PORT = 67;
		DEFAULT_OFFER_PORT = 68;
		DEFAULT_REQUEST_PORT = 4011;
		DEFAULT_ACK_PORT = 4011;
		DEFAULT_BOOT_FILE_NAME = "pxelinux.0";
		DEFAULT_BOOT_SERVER_ADDRESS = InetAddress.getLocalHost();
		DEFAULT_DISCOVERY_CONTROL = 0;
		DEFAULT_SERVER_OPTION = InetAddress.getLocalHost();
		DEFAULT_BOOT_MENU = "0, boot_menu";
		DEFAULT_BOOT_ITEM = new short[] { 0, 0 };
		DEFAULT_POOL_COUNT = 10;
		DEFAULT_IPXE_BOOT_FILE_NAME = "default_ipxe_file_name";
		DEFAULT_GPXE_BOOT_FILE_NAME = "default_gpxe_file_name";
		DEFAULT_OPTION_93_BOOT_FILE_NAME = "default_option_93_file_names";
		DEFAULT_IPXE_OFFER_TO_TFTP_REDIRECT = true;
	}

	private void readProperties(String fileName) {
		try {
			InputStream ins = this.getClass().getResourceAsStream(fileName);
			if (ins == null) {
				throw new DhcpPropertiesReadingException("could not find resource " + fileName + " in classpath");
			}
			props.load(ins);
		} catch (IOException e) {
			throw new DhcpPropertiesReadingException(e);
		}
	}

	/**
	 * Returns the port to which the DHCP Discover packet is sent by the client.
	 * 
	 * @return the port number
	 */
	@Override
	public int getDiscoverPort() {
		String str = props.getProperty(PropertyFields.DISCOVER_PORT.getName());
		if (str == null) {
			return DEFAULT_DISCOVER_PORT;
		}
		return Integer.parseInt(str.trim());
	}

	/**
	 * Returns the port to which the DHCP Offer packet is sent by the client.
	 * 
	 * @return the port number
	 */
	@Override
	public int getOfferPort() {
		String str = props.getProperty(PropertyFields.OFFER_PORT.getName());
		if (str == null) {
			return DEFAULT_OFFER_PORT;
		}
		return Integer.parseInt(str.trim());
	}

	/**
	 * Returns the port to which the DHCP Request packet is sent by the client.
	 * 
	 * @return the port number
	 */
	@Override
	public int getRequestPort() {
		String str = props.getProperty(PropertyFields.REQUEST_PORT.getName());
		if (str == null) {
			return DEFAULT_REQUEST_PORT;
		}
		return Integer.parseInt(str.trim());
	}

	/**
	 * Returns the port to which the DHCP Ack packet is sent by the client.
	 * 
	 * @return the port number
	 */
	@Override
	public int getAckPort() {
		String str = props.getProperty(PropertyFields.ACK_PORT.getName());
		if (str == null) {
			return DEFAULT_ACK_PORT;
		}
		return Integer.parseInt(str.trim());
	}

	/**
	 * Returns the Boot File Name.
	 * 
	 * @return the file name
	 */
	@Override
	public String getBootFileName() {
		String str = props.getProperty(PropertyFields.BOOT_FILE_NAME.getName());
		if (str == null) {
			return DEFAULT_BOOT_FILE_NAME;
		}
		return str;
	}

	/**
	 * Returns the BootStrap server address.
	 * 
	 * @return the server InetAddress
	 */
	@Override
	public InetAddress getBootServerAddress() {
		String str = props.getProperty(PropertyFields.BOOT_SERVER_ADDRESS.getName());
		if (str == null) {
			return DEFAULT_BOOT_SERVER_ADDRESS;
		}
		try {
			return InetAddress.getByName(str.trim());
		} catch (UnknownHostException e) {
			throw new DhcpPropertiesReadingException(e);
		}
	}

	/**
	 * Returns the data field for the discovery control vendor option. The
	 * different values are specified in the dhcp.properties file.
	 * 
	 * @return the value of the discovery control
	 */
	@Override
	public byte getVendorOptionDiscoveryControl() {
		String str = props.getProperty(PropertyFields.VENDOR_OPTION_DISCOVERY_CONTROL.getName());
		if (str == null) {
			return DEFAULT_DISCOVERY_CONTROL;
		}
		return Byte.parseByte(str.trim());
	}

	/**
	 * Returns the InetAddress for the boot server vendor option. Only supports
	 * a single server of type 0 (PXE Bootstrap server).
	 * 
	 * @return the InetAddress of the server
	 */
	@Override
	public InetAddress getVendorOptionBootServer() {
		String str = props.getProperty(PropertyFields.VENDOR_OPTION_PXE_BOOT_SERVER.getName());
		if (str == null) {
			return DEFAULT_SERVER_OPTION;
		}
		try {
			return InetAddress.getByName(str.trim());
		} catch (UnknownHostException e) {
			throw new DhcpPropertiesReadingException(e);
		}
	}

	/**
	 * Returns the boot menu vendor option as a String with they type and
	 * description separated by a comma.
	 * 
	 * @return a string containing the type and description, separated by a
	 *         comma
	 */
	@Override
	public String getVendorOptionBootMenu() {
		String str = props.getProperty(PropertyFields.VENDOR_OPTION_PXE_BOOT_MENU.getName());
		if (str == null) {
			return DEFAULT_BOOT_MENU;
		}
		return str;
	}

	/**
	 * Returns the boot item vendor option as an array of 2 shorts. the first is
	 * the boot server type, the second is the layer.
	 * 
	 * @return a byte array of 2 bytes, the first is the boot server type, the
	 *         second is the layer.
	 */
	@Override
	public short[] getVendorOptionBootItem() {
		String str = props.getProperty(PropertyFields.VENDOR_OPTION_PXE_BOOT_ITEM.getName());
		if (str == null) {
			return DEFAULT_BOOT_ITEM;
		}
		String first = str.substring(0, str.indexOf(','));
		String second = str.substring(str.indexOf(' '), str.length());
		return new short[] { Short.parseShort(first.trim()), Short.parseShort(second.trim()) };
	}

	/**
	 * Returns the threadpool count per server. Default value is 10.
	 */
	@Override
	public int getPoolCount() {
		String str = props.getProperty(PropertyFields.POOL_COUNT.getName());
		if (str == null) {
			return DEFAULT_POOL_COUNT;
		}
		return Integer.parseInt(str.trim());
	}

	/**
	 * Returns the boot file name for ipxe clients.
	 */
	@Override
	public String getIpxeBootFileName() {
		String str = props.getProperty(PropertyFields.IPXE_BOOT_FILE_NAME.getName());
		if (str == null) {
			return DEFAULT_IPXE_BOOT_FILE_NAME;
		}
		return str;
	}

	/**
	 * Returns the boot file name for gpxe clients
	 */
	@Override
	public String getGpxeBootFileName() {
		String str = props.getProperty(PropertyFields.GPXE_BOOT_FILE_NAME.getName());
		if (str == null) {
			return DEFAULT_GPXE_BOOT_FILE_NAME;
		}
		return str;
	}

	/**
	 * Returns the menu prompt for the menu prompt suboption in DHCP option 43
	 */
	@Override
	public String getVendorOptionMenuPrompt() {
		String str = props.getProperty(PropertyFields.VENDOR_OPTION_MENU_PROMPT.getName());
		if (str == null) {
			return DEFAULT_GPXE_BOOT_FILE_NAME;
		}
		return str;
	}

	/**
	 * Returns the boot file name if it is supposed to be altered for clients
	 * based on DHCP option 93 (client system architecture)
	 */
	@Override
	public String getOption93BootFileName() {
		String str = props.getProperty(PropertyFields.OPTION_93_BOOT_FILE_NAME.getName());
		if (str == null) {
			return DEFAULT_OPTION_93_BOOT_FILE_NAME;
		}
		return str;
	}

	/**
	 * Should be turned on in case the ipxe client gets its boot information
	 * from a DHCP offer instead of carrying out the full protocol and wating
	 * for the ack.
	 */
	@Override
	public boolean getIpxeOfferToTftpRedirect() {
		String str = props.getProperty(PropertyFields.IPXE_OFFER_TO_TFTP_REDIRECT.getName());
		if (str == null) {
			return DEFAULT_IPXE_OFFER_TO_TFTP_REDIRECT;
		}

		if (str.equalsIgnoreCase("on")) {
			return true;
		}
		return false;

	}
}
