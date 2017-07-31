package org.kj.pxe.protocol.impl;

import org.junit.Test;
import org.kj.pxe.protocol.DhcpPacket;
import org.kj.pxe.protocol.impl.DhcpPacketImpl;
import org.kj.pxe.protocol.impl.DhcpPacketToByteConversionException;

public class DhcpPacketTest {
    
    @Test (expected = DhcpPacketToByteConversionException.class)
    public void testSerializeEmptyPacket() {
        DhcpPacket packet = new DhcpPacketImpl();
        packet.convertToBytes();
    }
}
