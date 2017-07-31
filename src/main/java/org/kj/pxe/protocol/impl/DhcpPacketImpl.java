package org.kj.pxe.protocol.impl;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.net.InetAddress;
import java.util.Collections;
import java.util.List;

import org.kj.pxe.protocol.DhcpPacket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Stores all of the data fields of a DHCP packet as specified by <a href=
 * "https://www.ietf.org/rfc/rfc2131.txt">https://www.ietf.org/rfc/rfc2131.txt</a>.
 * Provides getter and setter methods for all fields.
 * 
 * @author Kinjal
 *
 */
public class DhcpPacketImpl implements DhcpPacket {

	private static Logger logger = LoggerFactory.getLogger(DhcpPacketImpl.class);

	private DhcpOpField op;
	private byte htype;
	private byte hlen;
	private byte hops;
	private int xid;
	private short secs;
	private short flags;
	private InetAddress ciaddr;
	private InetAddress yiaddr;
	private InetAddress siaddr;
	private InetAddress giaddr;
	private byte[] chaddr;
	private String sname;
	private String file;
	private byte[] magicCookie;
	private List<DhcpOption> options = Collections.emptyList();

	@Override
	public DhcpOpField getOp() {
		return op;
	}

	@Override
	public void setOp(DhcpOpField op) {
		this.op = op;
	}

	@Override
	public byte getHtype() {
		return htype;
	}

	@Override
	public void setHtype(byte htype) {
		this.htype = htype;
	}

	@Override
	public byte getHlen() {
		return hlen;
	}

	@Override
	public void setHlen(byte hlen) {
		this.hlen = hlen;
	}

	@Override
	public byte getHops() {
		return hops;
	}

	@Override
	public void setHops(byte hops) {
		this.hops = hops;
	}

	@Override
	public int getXid() {
		return xid;
	}

	@Override
	public void setXid(int xid) {
		this.xid = xid;
	}

	@Override
	public short getSecs() {
		return secs;
	}

	@Override
	public void setSecs(short secs) {
		this.secs = secs;
	}

	@Override
	public short getFlags() {
		return flags;
	}

	@Override
	public void setFlags(short flags) {
		this.flags = flags;
	}

	@Override
	public InetAddress getCiaddr() {
		return ciaddr;
	}

	@Override
	public void setCiaddr(InetAddress ciaddr) {
		this.ciaddr = ciaddr;
	}

	@Override
	public InetAddress getYiaddr() {
		return yiaddr;
	}

	@Override
	public void setYiaddr(InetAddress yiaddr) {
		this.yiaddr = yiaddr;
	}

	@Override
	public InetAddress getSiaddr() {
		return siaddr;
	}

	@Override
	public void setSiaddr(InetAddress siaddr) {
		this.siaddr = siaddr;
	}

	@Override
	public InetAddress getGiaddr() {
		return giaddr;
	}

	@Override
	public void setGiaddr(InetAddress giaddr) {
		this.giaddr = giaddr;
	}

	@Override
	public byte[] getChaddr() {
		return chaddr;
	}

	@Override
	public void setChaddr(byte[] chaddr) {
		this.chaddr = chaddr;
	}

	@Override
	public String getSname() {
		return sname;
	}

	@Override
	public void setSname(String sname) {
		this.sname = sname;
	}

	@Override
	public String getFile() {
		return file;
	}

	@Override
	public void setFile(String file) {
		this.file = file;
	}

	@Override
	public List<DhcpOption> getOptions() {
		return options;
	}

	@Override
	public void setOptions(List<DhcpOption> options) {
		this.options = options;
	}

	@Override
	public byte[] getMagicCookie() {
		return magicCookie;
	}

	@Override
	public void setMagicCookie(byte[] magicCookie) {
		this.magicCookie = magicCookie;
	}

	/**
	 * Converts the data in this packet to an array of bytes. The fields are
	 * stored in the order specified by <a href=
	 * "https://www.ietf.org/rfc/rfc2131.txt">https://www.ietf.org/rfc/rfc2131.txt</a>
	 * and also take up the specified amount of bytes each.
	 * 
	 * @return a byte array representing all of the information contained within
	 *         this packet.
	 */
	@Override
	public byte[] convertToBytes() {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		DataOutputStream outStream = new DataOutputStream(bos);
		try {
			outStream.writeByte(getOp().getValue());
			outStream.writeByte(getHtype());
			outStream.writeByte(getHlen());
			outStream.writeByte(getHops());
			logger.debug("Wrote Hops, size: " + outStream.size());

			outStream.writeInt(getXid());
			outStream.writeShort(getSecs());
			outStream.writeShort(getFlags());
			logger.debug("Wrote Flags, size: " + outStream.size());

			outStream.write(getCiaddr().getAddress());
			outStream.write(getYiaddr().getAddress());
			outStream.write(getSiaddr().getAddress());
			outStream.write(getGiaddr().getAddress());
			logger.debug("Wrote Giaddr, size: " + outStream.size());

			outStream.write(getChaddr());
			logger.debug("Wrote Chaddr, size: " + outStream.size());

			outStream.writeBytes(getSname());
			byte[] snamePadding = new byte[64 - getSname().getBytes().length];
			outStream.write(snamePadding);
			logger.debug("Wrote Sname, size: " + outStream.size());

			outStream.writeBytes(getFile());
			byte[] filePadding = new byte[128 - getFile().getBytes().length];
			outStream.write(filePadding);
			logger.debug("Wrote File, size: " + outStream.size());

			outStream.write(getMagicCookie());

			List<DhcpOption> options = getOptions();
			for (DhcpOption option : options) {
				option.writeToStream(outStream);
			}
			logger.debug("Wrote options, size: " + outStream.size());
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new DhcpPacketToByteConversionException(e);
		}

		logger.debug("ByteArrayOutputStream size: " + outStream.size());
		return bos.toByteArray();
	}

	/**
	 * Does nothing. The readFromBytes method should only be used to build a
	 * specific type of packet. This is a generic that only stores data fields.
	 */
	public void readFromBytes(byte[] buf) {
		return;
	}
}
