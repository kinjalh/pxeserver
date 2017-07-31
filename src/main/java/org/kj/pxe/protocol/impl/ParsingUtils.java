package org.kj.pxe.protocol.impl;

/**
 * Utilities class for parsing.
 * @author Kinjal
 *
 */
class ParsingUtils {

	/**
	 * Returns the index of the first occurrence of the target byte value within
	 * the byte array. Returns -1 if the target value is not found.
	 * 
	 * @param bytes
	 *            the byte array to search in
	 * @param target
	 *            the value to search for
	 * @return the index of the value, -1 if not found
	 */
	static int findByteIndex(byte[] bytes, byte target) {
		int i = 0;
		for (i = 0; i < bytes.length; i++) {
			if (bytes[i] == target) {
				return i;
			}
		}
		return -1;
	}
}
