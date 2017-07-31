package org.kj.pxe.protocol.impl;

import java.io.DataOutputStream;
import java.io.IOException;

import org.kj.pxe.protocol.VendorSuboption;

/**
 * Suboption of DHCP option 43 that contains a byte representing the Discovery
 * Control.
 * 
 * @author Kinjal
 *
 */
class PxeDiscoveryControlSuboption implements VendorSuboption {

	private byte value;

	/**
	 * Constructs a PxeDiscoveryControlSuboption with the given value
	 * 
	 * @param value
	 *            the value
	 */
	PxeDiscoveryControlSuboption(byte value) {
		this.value = validateValue(value);
	}

	private static byte validateValue(byte value) throws IllegalArgumentException {
		if (value < 0 || value > 15) {
			throw new IllegalArgumentException("value must represent a bit field of size 8 with last 4 bits set to 0");
		}
		return value;
	}

	/**
	 * returns the length, which is always 1.
	 * 
	 * @return 1, the length
	 */
	@Override
	public short getLen() {
		return 1;
	}

	/**
	 * Writes the option to a stream as bytes, in the order tag, length, value
	 * 
	 * @param stream
	 *            the stream to write to
	 * @throws IOException
	 *             if there is an error writing to stream
	 */
	@Override
	public void writeToStream(DataOutputStream stream) throws IOException {
		stream.writeByte(VendorSuboptionTag.PXE_DISCOVER_CONTROL.getValue());
		stream.writeByte(getLen());
		stream.writeByte(value);
	}

	/**
	 * Returns the tag for this suboption
	 * 
	 * @return the tag
	 */
	@Override
	public short getTag() {
		return (short) VendorSuboptionTag.PXE_DISCOVER_CONTROL.getValue();
	}
}
