package org.kj.pxe.protocol.impl;

/**
 * Represents different possible values of DHCP option tage. Does not cover all possible values.
 * @author Kinjal
 *
 */
public enum DhcpOptionTag {
    PAD(0),
    DHCP_MESSAGE_TYPE_TAG(53),
    SERVER_ID(54),
    PARAMETER_REQUEST_LIST(55),
    MAX_MESSAGE_SIZE(57),
    VENDOR_CLASS_IDENTIFIER(60),
    USER_CLASS(77),
    CLIENT_IDENTIFIER(97),
    CLIENT_SYSTEM_ARCH(93),
    CLIENT_NET_DEVICE_INTERFACE(94),
    END(255);
    
    private int value;
    
    private DhcpOptionTag(int value) {
        this.value = value;
    }
    
    /**
     * Returns the integer value of a specific tag.
     * 
     * @return the value
     */
    public int getValue() {
        return value;
    }
}
