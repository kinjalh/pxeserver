package org.kj.pxe.protocol.impl;

import java.io.DataOutputStream;
import java.io.IOException;

import org.kj.pxe.protocol.VendorSuboption;

/**
 * Returns the Pxe Pad suboption in DHCP option 43. Only contains a single
 * value, which is the tag.
 * 
 * @author Kinjal
 *
 */
class PxePadSuboption implements VendorSuboption {

	/**
	 * Writes this suboption to a stream. Only a single byte, the tag, is
	 * written.
	 * 
	 * @param stream
	 *            the stream to write to
	 * @throws IOException
	 *             if there is an error writing to stream
	 */
	@Override
	public void writeToStream(DataOutputStream stream) throws IOException {
		stream.writeByte(VendorSuboptionTag.PAD.getValue());
	}

	/**
	 * Returns the length of the data for this suboption, which is always 0.
	 * 
	 * @return 0
	 */
	@Override
	public short getLen() {
		return 0;
	}

	/**
	 * Returns the tag for the suboption.
	 * 
	 * @return the tag
	 */
	@Override
	public short getTag() {
		return (short) VendorSuboptionTag.PAD.getValue();
	}
}
