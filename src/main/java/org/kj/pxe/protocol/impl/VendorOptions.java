package org.kj.pxe.protocol.impl;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.kj.pxe.protocol.VendorSuboption;

/**
 * Represents DHCP option 43, a.k.a. vendor suboptions. Contains a list of the
 * suboptions.
 * 
 * @author Kinjal
 *
 */
public class VendorOptions extends DhcpOption {

	List<VendorSuboption> suboptions = new ArrayList<VendorSuboption>();

	/**
	 * Returns the list of suboptions. Initially an empty list.
	 * 
	 * @return the list of suboptions
	 */
	List<VendorSuboption> getSuboptions() {
		return suboptions;
	}

	/**
	 * Sets the suboptions to the specified value
	 * 
	 * @param suboptions
	 *            the list to set to
	 */
	void setSuboptions(List<VendorSuboption> suboptions) {
		this.suboptions = suboptions;
	}

	/**
	 * Returns the length of the option. This is the sum of all the bytes of the
	 * suboptions contained.
	 * 
	 * @return the length
	 */
	@Override
	short getLen() {
		short actualLen = 0;
		for (VendorSuboption suboption : suboptions) {
			if (suboption.getTag() == VendorSuboptionTag.PAD.getValue()
					|| suboption.getTag() == VendorSuboptionTag.END.getValue()) {
				actualLen++;
			} else {
				actualLen += 2; // for tag and len fields
				actualLen += suboption.getLen();
			}
		}
		return actualLen;
	}

	/**
	 * Writes itself as a series of bytes into a DataOutPutStream. Order is tag,
	 * length, then each suboption from the list, sequentially.
	 * 
	 * @param outStream
	 *            the stream to write to
	 */
	@Override
	void writeToStream(DataOutputStream outStream) {
		try {
			outStream.writeByte(43);

			outStream.writeByte(getLen());

			for (VendorSuboption suboption : suboptions) {
				suboption.writeToStream(outStream);
			}
		} catch (IOException e) {
			throw new DhcpPacketConstructionException(e);
		}
	}
}
