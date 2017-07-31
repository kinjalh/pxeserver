package org.kj.pxe.protocol.impl;

/**
 * Contains tag values for vendor suboptions. Not all tags are included.
 * 
 * @author Kinjal
 *
 */
enum VendorSuboptionTag {
	PAD(0), PXE_DISCOVER_CONTROL(6), PXE_BOOT_SERVERS(8), PXE_BOOT_MENU(9), PXE_BOOT_ITEM(71), END(
			255), PXE_MENU_PROMPT(10);

	private int value;

	private VendorSuboptionTag(int value) {
		this.value = value;
	}

	/**
	 * Returns the value of a tag
	 * 
	 * @return the value
	 */
	int getValue() {
		return value;
	}
}
