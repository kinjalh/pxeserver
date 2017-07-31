package org.kj.pxe.protocol.impl;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.util.List;

import org.kj.pxe.protocol.VendorSuboption;

/**
 * Represents the Boot Servers suboption in DHCP option 43. Stores a list of
 * {@link BootServerInformation} for each server type.
 * 
 * @author Kinjal
 *
 */
class PxeBootServersSuboption implements VendorSuboption {

	private List<BootServerInformation> serverInfo;

	/**
	 * Constructs a PxeBootServersSuboption with the given list of
	 * {@link BootServerInformation}
	 * 
	 * @param serverInfo
	 *            the list of {@link BootServerInformation}
	 */
	PxeBootServersSuboption(List<BootServerInformation> serverInfo) {
		this.serverInfo = serverInfo;
	}

	/**
	 * Returns the length of the data. Each bootServerInfo has 2 bytes for
	 * server type, 1 byte for IP count, and 4 * ipCount for the IP addresses of
	 * the servers.
	 * 
	 * @return the data length
	 */
	@Override
	public short getLen() {
		short len = 0;
		for (BootServerInformation info : serverInfo) {
			len += 3 + info.getServerList().size() * 4;
		}
		return len;
	}

	/**
	 * Writes the option as bytes into a stream in order tag, len, then
	 * sequentially goes down the list of {@link BootServerInformation} and
	 * sequentially writes the type, IPcount, and IP addresses in each.
	 * 
	 * @param stream
	 *            the stream to write to
	 * @throws IOException
	 *             if there is an error writing to stream
	 */
	@Override
	public void writeToStream(DataOutputStream stream) throws IOException {
		stream.writeByte(VendorSuboptionTag.PXE_BOOT_SERVERS.getValue());

		stream.writeByte(getLen());

		for (BootServerInformation serverList : serverInfo) {
			stream.writeShort(serverList.getType());
			stream.writeByte(serverList.getServerList().size());
			for (InetAddress addr : serverList.getServerList()) {
				stream.write(addr.getAddress());
			}
		}
	}

	/**
	 * returns the tag for this option.
	 * 
	 * @return the tag
	 */
	@Override
	public short getTag() {
		return (short) VendorSuboptionTag.PXE_BOOT_SERVERS.getValue();
	}
}
