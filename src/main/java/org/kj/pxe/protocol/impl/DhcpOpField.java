package org.kj.pxe.protocol.impl;

/**
 * Contains the Op Code field for the 2 different types of Dhcp Messages: Boot
 * request (1) and Boot reply (2).
 * 
 * @author Kinjal
 *
 */
public enum DhcpOpField {
    BOOTREQUEST(1), BOOTREPLY(2);

    private int value;

    private DhcpOpField(int value) {
        this.value = value;
    }

    int getValue() {
        return value;
    }
}
