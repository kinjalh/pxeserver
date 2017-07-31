package org.kj.pxe.protocol.impl;

import java.io.DataOutputStream;
import java.io.IOException;

import org.kj.pxe.protocol.VendorSuboption;

/**
 * Represents the data within one PXE Boot item suboption - a type and a layer.
 * 
 * @author Kinjal
 *
 */
class PxeBootItemSuboption implements VendorSuboption {

	private short type;
	private short layer;

	/**
	 * Constructs a PxeBootItemSuboption with the specified type and layer.
	 * 
	 * @param type
	 *            the type
	 * @param layer
	 *            the layer
	 */
	PxeBootItemSuboption(short type, short layer) {
		this.type = type;
		this.layer = layer;
	}

	/**
	 * Returns the length of the data, which is 4.
	 * 
	 * @return 4, which is the length of the data
	 */
	@Override
	public short getLen() {
		return 4;
	}

	/**
	 * Writes this option to a stream in the order tag, len, type, layer.
	 * 
	 * @param stream
	 *            the stream to write to
	 * @throws IOException
	 *             if there is an error writing to stream
	 */
	@Override
	public void writeToStream(DataOutputStream stream) throws IOException {
		stream.writeByte(VendorSuboptionTag.PXE_BOOT_ITEM.getValue());
		stream.writeByte(getLen());
		stream.writeShort(type);
		stream.writeShort(layer);
	}

	/**
	 * Returns the tag value
	 * 
	 * @return the tag
	 */
	@Override
	public short getTag() {
		return (short) VendorSuboptionTag.PXE_BOOT_ITEM.getValue();
	}
}
