package org.kj.pxe.protocol.impl;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.kj.pxe.config.DhcpSettings;
import org.kj.pxe.config.impl.DhcpSettingsImpl;
import org.kj.pxe.protocol.DhcpAckPacket;
import org.kj.pxe.protocol.DhcpPacket;
import org.kj.pxe.protocol.DhcpRequestPacket;

public class DhcpAckPacketTest {

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
		DhcpRequestPacket request = new DhcpRequestPacketImpl();
		DhcpAckPacket ack = new DhcpAckPacketImpl(settings);

		ack.buildAck(request);

		boolean correctMessageType = false;

		for (DhcpOption option : ((DhcpPacket) ack).getOptions()) {
			if (option.getTag() == DhcpOptionTag.DHCP_MESSAGE_TYPE_TAG.getValue() && option.getData().length == 1
					&& Arrays.equals(option.getData(), new byte[] { (byte) DhcpMessageTypeValue.DHCPACK.getValue() })) {

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
		DhcpAckPacket packet = new DhcpAckPacketImpl(settings);

		((DhcpAckPacketImpl) packet).validateDhcpMessageTypeOption(op);
	}

	@Test(expected = DhcpOptionDataIncorrectException.class)
	public void testValidateMessageTypeOptionIncorrectData() {
		initSettings();
		DhcpOption op = new DhcpOption((short) DhcpOptionTag.DHCP_MESSAGE_TYPE_TAG.getValue(), (short) 0,
				new byte[] { (byte) DhcpMessageTypeValue.DHCPREQUEST.getValue() });
		DhcpAckPacket packet = new DhcpAckPacketImpl(settings);

		((DhcpAckPacketImpl) packet).validateDhcpMessageTypeOption(op);
	}

	@Test
	public void testValidateMessageTypeOptionCorrectData() {
		initSettings();
		DhcpOption op = new DhcpOption((short) DhcpOptionTag.DHCP_MESSAGE_TYPE_TAG.getValue(), (short) 1,
				new byte[] { (byte) DhcpMessageTypeValue.DHCPACK.getValue() });
		DhcpAckPacket packet = new DhcpAckPacketImpl(settings);

		((DhcpAckPacketImpl) packet).validateDhcpMessageTypeOption(op);
	}

	@Test(expected = DhcpOptionDataIncorrectException.class)
	public void testValidateDhcpVendorClassIdOptionBadData() {
		initSettings();
		byte[] data = "PXEClient:Arch:".getBytes();
		DhcpOption op = new DhcpOption((short) DhcpOptionTag.VENDOR_CLASS_IDENTIFIER.getValue(), (short) data.length,
				data);
		DhcpAckPacket packet = new DhcpAckPacketImpl(settings);

		((DhcpAckPacketImpl) packet).validateDhcpVendorClassIdOption(op);
	}

	@Test
	public void testValidateDhcpVendorClassIdOptionGoodData() {
		initSettings();
		byte[] data = "PXEClient:Arch:UNDI".getBytes();
		DhcpOption op = new DhcpOption((short) DhcpOptionTag.VENDOR_CLASS_IDENTIFIER.getValue(), (short) data.length,
				data);
		DhcpAckPacket packet = new DhcpAckPacketImpl(settings);

		((DhcpAckPacketImpl) packet).validateDhcpVendorClassIdOption(op);
	}

	@Test(expected = DhcpTagNotFoundException.class)
	public void testValidateDhcpOptionsNoMessageTypeOption() {
		initSettings();
		List<DhcpOption> options = new ArrayList<DhcpOption>();
		DhcpOption vendorClassId = new DhcpOption((short) DhcpOptionTag.VENDOR_CLASS_IDENTIFIER.getValue(),
				(short) "PXEClient:Arch:UNDI".getBytes().length, "PXEClient:Arch:UNDI".getBytes());
		options.add(vendorClassId);

		DhcpAckPacket packet = new DhcpAckPacketImpl(settings);

		((DhcpAckPacketImpl) packet).validateDhcpOptions(options);
	}

	@Test(expected = DhcpTagNotFoundException.class)
	public void testValidateDhcpOptionsNoVendorClassIdOption() {
		initSettings();
		List<DhcpOption> options = new ArrayList<DhcpOption>();
		DhcpOption messageTypeOp = new DhcpOption((short) DhcpOptionTag.DHCP_MESSAGE_TYPE_TAG.getValue(), (short) 1,
				new byte[] { (byte) DhcpMessageTypeValue.DHCPACK.getValue() });
		options.add(messageTypeOp);

		DhcpAckPacket packet = new DhcpAckPacketImpl(settings);

		((DhcpAckPacketImpl) packet).validateDhcpOptions(options);
	}

	@Test
	public void testValidateDhcpOptionsWithBothValidOptions() {
		initSettings();
		List<DhcpOption> options = new ArrayList<DhcpOption>();

		DhcpOption messageTypeOp = new DhcpOption((short) DhcpOptionTag.DHCP_MESSAGE_TYPE_TAG.getValue(), (short) 1,
				new byte[] { (byte) DhcpMessageTypeValue.DHCPACK.getValue() });
		options.add(messageTypeOp);

		DhcpOption vendorClassId = new DhcpOption((short) DhcpOptionTag.VENDOR_CLASS_IDENTIFIER.getValue(),
				(short) "PXEClient:Arch:UNDI".getBytes().length, "PXEClient:Arch:UNDI".getBytes());
		options.add(vendorClassId);

		DhcpAckPacket packet = new DhcpAckPacketImpl(settings);

		((DhcpAckPacketImpl) packet).validateDhcpOptions(options);
	}

	@Test(expected = DhcpParsingException.class)
	public void testReadFromBytesWrongOpCode() throws IOException {
		initSettings();
		byte[] buf = { 0, 0, 0 };
		DhcpRequestPacket packet = new DhcpRequestPacketImpl();
		packet.readFromBytes(buf);
	}

	@Test(expected = DhcpTagNotFoundException.class)
	public void testReadFromBytesCorrectOpCode() throws IOException {
		initSettings();
		byte[] buf = new byte[2048];
		buf[0] = (byte) DhcpOpField.BOOTREPLY.getValue();
		buf[2000] = (byte) DhcpOptionTag.END.getValue();
		DhcpAckPacket packet = new DhcpAckPacketImpl(settings);
		packet.readFromBytes(buf);
	}
}
