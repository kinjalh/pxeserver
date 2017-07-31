package org.kj.pxe.protocol.impl;

/**
 * Contains the values of the different DHCP message types. In a packet, this
 * data is found within the DHCP message type option (DHCP option 53)
 * 
 * @author Kinjal
 *
 */
enum DhcpMessageTypeValue {
    DHCPDISCOVER(1),
    DHCPOFFER(2),
    DHCPREQUEST(3),
    DHCPDECLINE(4),
    DHCPACK(5),
    DHCPNAK(6),
    DHCPRELEASE(7),
    DHCPINFORM(8);
    
    private int value;
    
    private DhcpMessageTypeValue(int value) {
        this.value = value;
    }
    
    /**
     * Returns the numerical value of a message type
     * @return
     */
    int getValue() {
        return value;
    }
}
