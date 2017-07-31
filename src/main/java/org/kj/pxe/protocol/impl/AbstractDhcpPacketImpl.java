package org.kj.pxe.protocol.impl;

import java.net.InetAddress;
import java.util.List;

import org.kj.pxe.protocol.DhcpPacket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Abstract implementation of a DHCP Packet. Implements getter and setter
 * methods from interface.
 * 
 * @author Kinjal
 *
 */
abstract class AbstractDhcpPacketImpl implements DhcpPacket {

	protected static Logger logger = LoggerFactory.getLogger(AbstractDhcpPacketImpl.class);
	private DhcpPacket delegate = new DhcpPacketImpl();

	/**
	 * Returns the DHCP op field of this packet
	 * 
	 * @return the op field
	 */
	@Override
	public DhcpOpField getOp() {
		return delegate.getOp();
	}

	/**
	 * Sets the op field to the specified list of values.
	 * 
	 * @param op
	 *            the op field
	 */
	@Override
	public void setOp(DhcpOpField op) {
		delegate.setOp(op);
	}

	/**
	 * Returns the hardware type field of this packet
	 * 
	 * @return the hardware type field
	 */
	@Override
	public byte getHtype() {
		return delegate.getHtype();
	}

	/**
	 * Sets the hardware type field
	 * 
	 * @param htype
	 *            the hardware type
	 */
	@Override
	public void setHtype(byte htype) {
		delegate.setHtype(htype);
	}

	/**
	 * Returns the hardware address length field of this packet
	 * 
	 * @return the hardware address length field
	 */
	@Override
	public byte getHlen() {
		return delegate.getHlen();
	}

	/**
	 * Sets the hardware address length field
	 * 
	 * @param hlen
	 *            the hardware address length
	 */
	@Override
	public void setHlen(byte hlen) {
		delegate.setHlen(hlen);
	}

	/**
	 * Returns the number of hops of this packet
	 * 
	 * @return the number of hops
	 */
	@Override
	public byte getHops() {
		return delegate.getHops();
	}

	/**
	 * Sets the number of hops to the specified list of values.
	 * 
	 * @param hops
	 *            the number of hops
	 */
	@Override
	public void setHops(byte hops) {
		delegate.setHops(hops);
	}

	/**
	 * Returns the transaction ID field of this packet
	 * 
	 * @return the transaction ID field
	 */
	@Override
	public int getXid() {
		return delegate.getXid();
	}

	/**
	 * Sets the transaction ID.
	 * 
	 * @param xid
	 *            the transaction ID
	 */
	@Override
	public void setXid(int xid) {
		delegate.setXid(xid);
	}

	/**
	 * Returns the seconds field of this packet
	 * 
	 * @return the number of seconds
	 */
	@Override
	public short getSecs() {
		return delegate.getSecs();
	}

	/**
	 * Sets the seconds field.
	 * 
	 * @param secs
	 *            the number of seconds
	 */
	@Override
	public void setSecs(short secs) {
		delegate.setSecs(secs);
	}

	/**
	 * Returns the flags field of this packet
	 * 
	 * @return the flags field
	 */
	@Override
	public short getFlags() {
		return delegate.getFlags();
	}

	/**
	 * Sets the flags.
	 * 
	 * @param flags
	 *            the flags
	 */
	@Override
	public void setFlags(short flags) {
		delegate.setFlags(flags);
	}

	/**
	 * Returns the client IP address field of this packet
	 * 
	 * @return the client IP address
	 */
	@Override
	public InetAddress getCiaddr() {
		return delegate.getCiaddr();
	}

	/**
	 * Sets the client IP address.
	 * 
	 * @param ciaddr
	 *            the client IP address
	 */
	@Override
	public void setCiaddr(InetAddress ciaddr) {
		delegate.setCiaddr(ciaddr);
	}

	/**
	 * Returns your (the client's) IP Address field of this packet
	 * 
	 * @return the receiver's IP address
	 */
	@Override
	public InetAddress getYiaddr() {
		return delegate.getYiaddr();
	}

	/**
	 * Sets your (client's) IP address.
	 * 
	 * @param yiaddr
	 *            your IP address
	 */
	@Override
	public void setYiaddr(InetAddress yiaddr) {
		delegate.setYiaddr(yiaddr);
	}

	/**
	 * Returns the IP address of the next server for the client to contact
	 * 
	 * @return the next server's IP address
	 */
	@Override
	public InetAddress getSiaddr() {
		return delegate.getSiaddr();
	}

	/**
	 * Sets the server IP address.
	 * 
	 * @param siaddr
	 *            the server IP address
	 */
	@Override
	public void setSiaddr(InetAddress siaddr) {
		delegate.setSiaddr(siaddr);
	}

	/**
	 * Returns the gateway IP address field of this packet
	 * 
	 * @return the gateway IP address
	 */
	@Override
	public InetAddress getGiaddr() {
		return delegate.getGiaddr();
	}

	/**
	 * Sets the gateway IP address.
	 * 
	 * @param giaddr
	 *            the gateway IP address
	 */
	@Override
	public void setGiaddr(InetAddress giaddr) {
		delegate.setGiaddr(giaddr);
	}

	/**
	 * Returns the client hardware address field of this packet
	 * 
	 * @return the client hardware address
	 */
	@Override
	public byte[] getChaddr() {
		return delegate.getChaddr();
	}

	/**
	 * Sets the client hardware address.
	 * 
	 * @param chaddr
	 *            the client hardware address
	 */
	@Override
	public void setChaddr(byte[] chaddr) {
		delegate.setChaddr(chaddr);
	}

	/**
	 * Returns the server name field of this packet. Often left blank
	 * 
	 * @return the server name
	 */
	@Override
	public String getSname() {
		return delegate.getSname();
	}

	/**
	 * Sets the server name to the specified list of values.
	 * 
	 * @param sname
	 *            server name
	 */
	@Override
	public void setSname(String sname) {
		delegate.setSname(sname);
	}

	/**
	 * Returns the boot file name
	 * 
	 * @return the boot file name
	 */
	@Override
	public String getFile() {
		return delegate.getFile();
	}

	/**
	 * Sets the boot file name to the specified list of values.
	 * 
	 * @param file
	 *            the boot file name
	 */
	@Override
	public void setFile(String file) {
		delegate.setFile(file);
	}

	/**
	 * Returns the list of DHCP options contained in the packet.
	 * 
	 * @return list of DHCP options
	 */
	@Override
	public List<DhcpOption> getOptions() {
		return delegate.getOptions();
	}

	/**
	 * Sets the DHCP options to the specified list of values.
	 * 
	 * @param options
	 *            the value to set options as
	 */
	@Override
	public void setOptions(List<DhcpOption> options) {
		delegate.setOptions(options);
	}

	/**
	 * Returns the magic cookie of this packet. Should be equal to "DHCP" when
	 * interpreted as a string.
	 * 
	 * @return the magic cookie
	 */
	@Override
	public byte[] getMagicCookie() {
		return delegate.getMagicCookie();
	}

	/**
	 * Sets the magic cookie value. Should be set to the byte representation of
	 * "DHCP".
	 * 
	 * @param magicCookie
	 *            the value to set the magic cookie as
	 */
	@Override
	public void setMagicCookie(byte[] magicCookie) {
		delegate.setMagicCookie(magicCookie);
	}

	/**
	 * Converts packet to byte array. Padding is added if necessary in order to
	 * ensure that each field occupies the correct number of bytes.
	 * 
	 * @return a byte array representing all the data in this DHCP packet
	 */
	@Override
	public byte[] convertToBytes() {
		return delegate.convertToBytes();
	}
}