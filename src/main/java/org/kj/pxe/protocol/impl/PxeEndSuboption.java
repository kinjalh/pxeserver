package org.kj.pxe.protocol.impl;

import java.io.DataOutputStream;
import java.io.IOException;

import org.kj.pxe.protocol.VendorSuboption;

/**
 * Represents the End suboption in Dhcp option 43. Contains only a single
 * constant value.
 * 
 * @author Kinjal
 *
 */
class PxeEndSuboption implements VendorSuboption {

	/**
	 * Writes this suboption as bytes to a stream. The only value that is
	 * written is the tag, which is a single byte.
	 * 
	 * @param stream
	 *            the stream to write to
	 * @throws IOException
	 *             if there is an error writing to stream
	 */
	@Override
	public void writeToStream(DataOutputStream stream) throws IOException {
		stream.writeByte(VendorSuboptionTag.END.getValue());
	}

	/**
	 * Returns the length, which is always 0 since this suboption has no data.
	 * 
	 * @return the length, which is 0
	 */
	@Override
	public short getLen() {
		return 0;
	}

	/**
	 * Returns the tag for this suboption.
	 * 
	 * @return the tag
	 */
	@Override
	public short getTag() {
		return (short) VendorSuboptionTag.END.getValue();
	}
}
