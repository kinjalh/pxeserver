package org.kj.pxe.protocol.impl;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Arrays;

/**
 * Represents the data contained in a DHCP option. An unsigned byte representing
 * the tag, an unsigned byte representing the length, and a byte array
 * representing the data.
 * 
 * @author Kinjal
 *
 */
public class DhcpOption {

	private short tag;
	private short len;
	private byte[] data;

	/**
	 * Constructs a DHCPOption with the specified tag, length, and data
	 * 
	 * @param tag
	 *            the tag
	 * @param len
	 *            the length of the data
	 * @param data
	 *            the data
	 */
	public DhcpOption(short tag, short len, byte[] data) {
		this.tag = tag;
		this.len = len;
		this.data = data;
	}

	/**
	 * Constructs a DHCP option with java's default values for tag, len, and
	 * data.
	 */
	DhcpOption() {

	}

	/**
	 * Returns the tag
	 * 
	 * @return the tag
	 */
	short getTag() {
		return tag;
	}

	/**
	 * Returns the length
	 * 
	 * @return the length
	 */
	short getLen() {
		return len;
	}

	/**
	 * Returns the data
	 * 
	 * @return the data
	 */
	public byte[] getData() {
		return data;
	}

	/**
	 * Returns a string in the format: "tag: tag, length: len, data: [data]"
	 * 
	 * @return the string specified
	 */
	@Override
	public String toString() {
		return "tag: " + tag + ", len: " + len + ", data: " + Arrays.toString(data);
	}

	/**
	 * Writes the option to the stream as bytes, in the order tag, len, data
	 * 
	 * @param stream
	 *            the stream to write to
	 * @throws IOException
	 *             if there is an error while writing to the stream
	 */
	void writeToStream(DataOutputStream stream) throws IOException {
		stream.writeByte(tag);
		if (tag == DhcpOptionTag.PAD.getValue() || tag == DhcpOptionTag.END.getValue()) {
			return;
		}
		stream.writeByte(len);
		stream.write(data, 0, len);
	}
}
