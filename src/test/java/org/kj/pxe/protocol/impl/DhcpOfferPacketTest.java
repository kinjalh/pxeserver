package org.kj.pxe.protocol.impl;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.kj.pxe.config.DhcpSettings;
import org.kj.pxe.config.impl.DhcpSettingsImpl;
import org.kj.pxe.protocol.DhcpDiscoverPacket;
import org.kj.pxe.protocol.DhcpOfferPacket;
import org.kj.pxe.protocol.DhcpPacket;
import org.kj.pxe.protocol.impl.DhcpDiscoverPacketImpl;
import org.kj.pxe.protocol.impl.DhcpMessageTypeValue;
import org.kj.pxe.protocol.impl.DhcpOfferPacketImpl;
import org.kj.pxe.protocol.impl.DhcpOption;
import org.kj.pxe.protocol.impl.DhcpOptionTag;

public class DhcpOfferPacketTest {
	
	DhcpSettings settings;
	
	private void initSettings() {
		try {
			settings = new DhcpSettingsImpl("/dhcp.properties");
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(0);
		}
	}
	
    @Test
    public void testCorrectMessageTypeCorrect() throws IOException {
    	initSettings();
        DhcpDiscoverPacket discover = new DhcpDiscoverPacketImpl();
        DhcpOfferPacket offer = new DhcpOfferPacketImpl(settings);

        offer.buildOffer(discover);

        boolean correctMessageType = false;

        for (DhcpOption option : ((DhcpPacket) offer).getOptions()) {
            if (option.getTag() == DhcpOptionTag.DHCP_MESSAGE_TYPE_TAG.getValue() && option.getData().length == 1
                    && Arrays.equals(option.getData(),
                            new byte[] { (byte) DhcpMessageTypeValue.DHCPOFFER.getValue() })) {

                correctMessageType = true;
            }
        }

        assertTrue(correctMessageType);
    }

    @Test(expected = DhcpOptionDataIncorrectException.class)
    public void testValidateMessageTypeOptionEmptyOption() {
    	initSettings();
        DhcpOption op = new DhcpOption((short) DhcpOptionTag.DHCP_MESSAGE_TYPE_TAG.getValue(), (short) 0,
                new byte[] {});
        DhcpOfferPacket packet = new DhcpOfferPacketImpl(settings);

        ((DhcpOfferPacketImpl) packet).validateDhcpMessageTypeOption(op);
    }

    @Test(expected = DhcpOptionDataIncorrectException.class)
    public void testValidateMessageTypeOptionIncorrectData() {
    	initSettings();
        DhcpOption op = new DhcpOption((short) DhcpOptionTag.DHCP_MESSAGE_TYPE_TAG.getValue(), (short) 0,
                new byte[] { (byte) DhcpMessageTypeValue.DHCPREQUEST.getValue() });
        DhcpOfferPacket packet = new DhcpOfferPacketImpl(settings);

        ((DhcpOfferPacketImpl) packet).validateDhcpMessageTypeOption(op);
    }

    @Test
    public void testValidateMessageTypeOptionCorrectData() {
        DhcpOption op = new DhcpOption((short) DhcpOptionTag.DHCP_MESSAGE_TYPE_TAG.getValue(), (short) 1,
                new byte[] { (byte) DhcpMessageTypeValue.DHCPOFFER.getValue() });
        DhcpOfferPacket packet = new DhcpOfferPacketImpl(settings);

        ((DhcpOfferPacketImpl) packet).validateDhcpMessageTypeOption(op);
    }

    @Test(expected = DhcpOptionDataIncorrectException.class)
    public void testValidateDhcpVendorClassIdOptionBadData() {
    	initSettings();
        byte[] data = "PXEClient:Arch:".getBytes();
        DhcpOption op = new DhcpOption((short) DhcpOptionTag.VENDOR_CLASS_IDENTIFIER.getValue(), (short) data.length,
                data);
        DhcpOfferPacket packet = new DhcpOfferPacketImpl(settings);

        ((DhcpOfferPacketImpl) packet).validateDhcpVendorClassIdOption(op);
    }

    @Test
    public void testValidateDhcpVendorClassIdOptionGoodData() {
    	initSettings();
        byte[] data = "PXEClient:Arch:UNDI".getBytes();
        DhcpOption op = new DhcpOption((short) DhcpOptionTag.VENDOR_CLASS_IDENTIFIER.getValue(), (short) data.length,
                data);
        DhcpOfferPacket packet = new DhcpOfferPacketImpl(settings);

        ((DhcpOfferPacketImpl) packet).validateDhcpVendorClassIdOption(op);
    }

    @Test (expected = DhcpTagNotFoundException.class)
    public void testValidateDhcpOptionsNoMessageTypeOption() {
    	initSettings();
        List<DhcpOption> options = new ArrayList<DhcpOption>();
        DhcpOption vendorClassId = new DhcpOption((short) DhcpOptionTag.VENDOR_CLASS_IDENTIFIER.getValue(),
                (short) "PXEClient:Arch:UNDI".getBytes().length, "PXEClient:Arch:UNDI".getBytes());
        options.add(vendorClassId);
        
        DhcpOfferPacket packet = new DhcpOfferPacketImpl(settings);

        ((DhcpOfferPacketImpl) packet).validateDhcpOptions(options);
    }
    
    @Test (expected = DhcpTagNotFoundException.class)
    public void testValidateDhcpOptionsNoVendorClassIdOption() {
    	initSettings();
        List<DhcpOption> options = new ArrayList<DhcpOption>();
        DhcpOption messageTypeOp = new DhcpOption((short) DhcpOptionTag.DHCP_MESSAGE_TYPE_TAG.getValue(),
                (short) 1, new byte[] { (byte)DhcpMessageTypeValue.DHCPOFFER.getValue()});
        options.add(messageTypeOp);
        
        DhcpOfferPacket packet = new DhcpOfferPacketImpl(settings);

        ((DhcpOfferPacketImpl) packet).validateDhcpOptions(options);
    }
    
    @Test 
    public void testValidateDhcpOptionsWithBothValidOptions() {
    	initSettings();
        List<DhcpOption> options = new ArrayList<DhcpOption>();
        
        DhcpOption messageTypeOp = new DhcpOption((short) DhcpOptionTag.DHCP_MESSAGE_TYPE_TAG.getValue(),
                (short) 1, new byte[] { (byte)DhcpMessageTypeValue.DHCPOFFER.getValue()});
        options.add(messageTypeOp);
        
        DhcpOption vendorClassId = new DhcpOption((short) DhcpOptionTag.VENDOR_CLASS_IDENTIFIER.getValue(),
                (short) "PXEClient:Arch:UNDI".getBytes().length, "PXEClient:Arch:UNDI".getBytes());
        options.add(vendorClassId);
        
        DhcpOfferPacket packet = new DhcpOfferPacketImpl(settings);

        ((DhcpOfferPacketImpl) packet).validateDhcpOptions(options);
    }
    
    @Test (expected = DhcpParsingException.class)
    public void testReadFromBytesWrongOpCode() throws IOException {
    	initSettings();
        byte[] buf = { 0, 0, 0 };
        DhcpDiscoverPacket packet = new DhcpDiscoverPacketImpl();
        packet.readFromBytes(buf);
    }
    
    @Test (expected = DhcpTagNotFoundException.class)
    public void testReadFromBytesCorrectOpCode() throws IOException {
    	initSettings();
        byte[] buf = new byte[2048];
        buf[0] = (byte) DhcpOpField.BOOTREPLY.getValue();
        buf[2000] = (byte) DhcpOptionTag.END.getValue();
        DhcpOfferPacket packet = new DhcpOfferPacketImpl(settings);
        packet.readFromBytes(buf);
    }
}
