package org.kj.pxe.protocol.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.kj.pxe.protocol.DhcpRequestPacket;
import org.kj.pxe.protocol.impl.DhcpMessageTypeValue;
import org.kj.pxe.protocol.impl.DhcpOption;
import org.kj.pxe.protocol.impl.DhcpOptionDataIncorrectException;
import org.kj.pxe.protocol.impl.DhcpOptionTag;
import org.kj.pxe.protocol.impl.DhcpRequestPacketImpl;
import org.kj.pxe.protocol.impl.DhcpTagNotFoundException;

public class DhcpRequestPacketTest {
    
    @Test(expected = DhcpOptionDataIncorrectException.class)
    public void testValidateMessageTypeOptionEmptyOption() {
        DhcpOption op = new DhcpOption((short) DhcpOptionTag.DHCP_MESSAGE_TYPE_TAG.getValue(), (short) 0,
                new byte[] {});
        DhcpRequestPacket packet = new DhcpRequestPacketImpl();

        ((DhcpRequestPacketImpl) packet).validateDhcpMessageTypeOption(op);
    }

    @Test(expected = DhcpOptionDataIncorrectException.class)
    public void testValidateMessageTypeOptionIncorrectData() {
        DhcpOption op = new DhcpOption((short) DhcpOptionTag.DHCP_MESSAGE_TYPE_TAG.getValue(), (short) 0,
                new byte[] { (byte) DhcpMessageTypeValue.DHCPDISCOVER.getValue() });
        DhcpRequestPacket packet = new DhcpRequestPacketImpl();

        ((DhcpRequestPacketImpl) packet).validateDhcpMessageTypeOption(op);
    }

    @Test
    public void testValidateMessageTypeOptionCorrectData() {
        DhcpOption op = new DhcpOption((short) DhcpOptionTag.DHCP_MESSAGE_TYPE_TAG.getValue(), (short) 1,
                new byte[] { (byte) DhcpMessageTypeValue.DHCPREQUEST.getValue() });
        DhcpRequestPacket packet = new DhcpRequestPacketImpl();

        ((DhcpRequestPacketImpl) packet).validateDhcpMessageTypeOption(op);
    }

    @Test(expected = DhcpOptionDataIncorrectException.class)
    public void testValidateDhcpVendorClassIdOptionBadData() {
        byte[] data = "PXEClient:Arch:".getBytes();
        DhcpOption op = new DhcpOption((short) DhcpOptionTag.VENDOR_CLASS_IDENTIFIER.getValue(), (short) data.length,
                data);
        DhcpRequestPacket packet = new DhcpRequestPacketImpl();

        ((DhcpRequestPacketImpl) packet).validateDhcpVendorClassIdOption(op);
    }

    @Test
    public void testValidateDhcpVendorClassIdOptionGoodData() {
        byte[] data = "PXEClient:Arch:UNDI".getBytes();
        DhcpOption op = new DhcpOption((short) DhcpOptionTag.VENDOR_CLASS_IDENTIFIER.getValue(), (short) data.length,
                data);
        DhcpRequestPacket packet = new DhcpRequestPacketImpl();

        ((DhcpRequestPacketImpl) packet).validateDhcpVendorClassIdOption(op);
    }

    @Test (expected = DhcpTagNotFoundException.class)
    public void testValidateDhcpOptionsNoMessageTypeOption() {
        List<DhcpOption> options = new ArrayList<DhcpOption>();
        DhcpOption vendorClassId = new DhcpOption((short) DhcpOptionTag.VENDOR_CLASS_IDENTIFIER.getValue(),
                (short) "PXEClient:Arch:UNDI".getBytes().length, "PXEClient:Arch:UNDI".getBytes());
        options.add(vendorClassId);
        
        DhcpRequestPacket packet = new DhcpRequestPacketImpl();

        ((DhcpRequestPacketImpl) packet).validateDhcpOptions(options);;
    }
    
    @Test (expected = DhcpTagNotFoundException.class)
    public void testValidateDhcpOptionsNoVendorClassIdOption() {
        List<DhcpOption> options = new ArrayList<DhcpOption>();
        DhcpOption messageTypeOp = new DhcpOption((short) DhcpOptionTag.DHCP_MESSAGE_TYPE_TAG.getValue(),
                (short) 1, new byte[] { (byte)DhcpMessageTypeValue.DHCPREQUEST.getValue()});
        options.add(messageTypeOp);
        
        DhcpRequestPacket packet = new DhcpRequestPacketImpl();

        ((DhcpRequestPacketImpl) packet).validateDhcpOptions(options);
    }
    
    @Test 
    public void testValidateDhcpOptionsWithBothValidOptions() {
        List<DhcpOption> options = new ArrayList<DhcpOption>();
        
        DhcpOption messageTypeOp = new DhcpOption((short) DhcpOptionTag.DHCP_MESSAGE_TYPE_TAG.getValue(),
                (short) 1, new byte[] { (byte)DhcpMessageTypeValue.DHCPREQUEST.getValue()});
        options.add(messageTypeOp);
        
        DhcpOption vendorClassId = new DhcpOption((short) DhcpOptionTag.VENDOR_CLASS_IDENTIFIER.getValue(),
                (short) "PXEClient:Arch:UNDI".getBytes().length, "PXEClient:Arch:UNDI".getBytes());
        options.add(vendorClassId);
        
        DhcpRequestPacket packet = new DhcpRequestPacketImpl();

        ((DhcpRequestPacketImpl) packet).validateDhcpOptions(options);
    }
    
    @Test (expected = DhcpParsingException.class)
    public void testReadFromBytesWrongOpCode() throws IOException {
        byte[] buf = { 0, 0, 0 };
        DhcpRequestPacket packet = new DhcpRequestPacketImpl();
        packet.readFromBytes(buf);
    }
    
    @Test (expected = DhcpTagNotFoundException.class)
    public void testReadFromBytesCorrectOpCode() throws IOException {
        byte[] buf = new byte[2048];
        buf[0] = 1;
        buf[2000] = (byte) DhcpOptionTag.END.getValue();
        DhcpRequestPacket packet = new DhcpRequestPacketImpl();
        packet.readFromBytes(buf);
    }
}
