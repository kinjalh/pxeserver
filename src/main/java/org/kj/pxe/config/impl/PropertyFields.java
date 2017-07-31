package org.kj.pxe.config.impl;

/**
 * Contains the string values corresponding to property fields.
 * @author Kinjal
 *
 */
public enum PropertyFields {

    DISCOVER_PORT("discover_port"),
    OFFER_PORT("offer_port"),
    REQUEST_PORT("request_port"),
    ACK_PORT("ack_port"),
    BOOT_FILE_NAME("boot_file_name"),
    BOOT_SERVER_ADDRESS("boot_server_address"),
    VENDOR_OPTION_DISCOVERY_CONTROL("vendor_option.discovery_control"),
    VENDOR_OPTION_PXE_BOOT_SERVER("vendor_option.pxe_boot_server"),
    VENDOR_OPTION_PXE_BOOT_MENU("vendor_option.pxe_boot_menu"),
    VENDOR_OPTION_PXE_BOOT_ITEM("vendor_option.pxe_boot_item"), 
    POOL_COUNT("pool_count"), 
    IPXE_BOOT_FILE_NAME("ipxe_boot_file_name"), 
    GPXE_BOOT_FILE_NAME("gpxe_boot_file_name"), 
    VENDOR_OPTION_MENU_PROMPT("vendor_option.menu_prompt"), 
    OPTION_93_BOOT_FILE_NAME("option_93_boot_file_name"), 
    IPXE_OFFER_TO_TFTP_REDIRECT("ipxe_offer_to_tftp_redirect");
    
    private String name;
    
    PropertyFields(String name) {
        this.name = name;
    }
    
    public String getName() {
        return name;
    }
}
