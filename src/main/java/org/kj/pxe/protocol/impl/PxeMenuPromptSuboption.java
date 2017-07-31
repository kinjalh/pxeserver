package org.kj.pxe.protocol.impl;

import java.io.DataOutputStream;
import java.io.IOException;

import org.kj.pxe.protocol.VendorSuboption;

/**
 * Represents the Pxe menu prompt suboption in DHCP option 43. Contains a
 * timeout and a prompt (message displayed).
 * 
 * @author Kinjal
 *
 */
public class PxeMenuPromptSuboption implements VendorSuboption {

	private byte timeout;
	private String prompt;

	/**
	 * Constructs a PxeMenuPromptSuboption with the given timeout and prompt.
	 * 
	 * @param timeout
	 *            the given timeout
	 * @param prompt
	 *            the given prompt
	 */
	public PxeMenuPromptSuboption(byte timeout, String prompt) {
		this.timeout = timeout;
		this.prompt = prompt;
	}

	/**
	 * Returns the length of the data, which is the prompt length (as bytes) + 1
	 * (for the timeout value).
	 * 
	 * @return the length of the data
	 */
	@Override
	public short getLen() {
		return (short) (prompt.getBytes().length + 1);
	}

	/**
	 * Writes this to a stream as bytes. The sequence of writing is tag, length,
	 * timeout, prompt
	 * 
	 * @param stream
	 *            the stream to write to
	 * @throws IOException
	 *             if there is an error writing to stream
	 */
	@Override
	public void writeToStream(DataOutputStream stream) throws IOException {
		stream.writeByte(VendorSuboptionTag.PXE_MENU_PROMPT.getValue());
		stream.writeByte(getLen());
		stream.writeByte(timeout);
		stream.write(prompt.getBytes());
	}

	/**
	 * Returns the tag for this suboption.
	 * 
	 * @return the tag
	 */
	@Override
	public short getTag() {
		return (short) VendorSuboptionTag.PXE_MENU_PROMPT.getValue();
	}
}
