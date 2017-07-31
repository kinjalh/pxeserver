package org.kj.pxe.protocol.impl;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.Arrays;

import org.junit.Test;
import org.kj.pxe.config.DhcpSettings;
import org.kj.pxe.config.impl.DhcpSettingsImpl;
import org.kj.pxe.protocol.DhcpDiscoverPacket;
import org.kj.pxe.protocol.DhcpOfferPacket;

public class DiscoverPeerHandlerTest {

	DhcpSettings settings;

	private void initSettings() {
		try {
			settings = new DhcpSettingsImpl("/dhcp.properties");
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(0);
		}
	}

	@Test(expected = DhcpPacketToByteConversionException.class)
	public void testReceiveBytesWithInvalidInput() throws SocketException {
		initSettings();
		DhcpDiscoverPacket discover = new DhcpDiscoverPacketImpl();
		(new DiscoverPeerHandler(settings, discover.convertToBytes(), new DatagramSocket())).receiveBytes(discover.convertToBytes());
	}

	@Test
	public void testReceiveBytesWithValidInput() throws IOException {
		initSettings();
		FakeClient client = new FakeClient();
		byte[] bytes = client.makeDiscover();
		DiscoverPeerHandler handler = new DiscoverPeerHandler(settings, bytes, new DatagramSocket());
		DhcpOfferPacket offer = handler.receiveBytes(bytes);

		assertTrue(offer.getOp().getValue() == DhcpOpField.BOOTREPLY.getValue());
		assertTrue(Arrays.equals(offer.getCiaddr().getAddress(), new byte[] { 0, 0, 0, 0 }));
		assertTrue(Arrays.equals(offer.getMagicCookie(), "DHCP".getBytes()));
		((DhcpOfferPacketImpl) offer).validateDhcpOptions(offer.getOptions());
	}

	@Test(expected = DhcpPacketToByteConversionException.class)
	public void testReceiveBytesWithNoOptions() throws IOException {
		initSettings();
		FakeClient client = new FakeClient();
		byte[] bytes = client.makeDiscover();

		DhcpDiscoverPacket discover = new DhcpDiscoverPacketImpl();
		discover.readFromBytes(bytes);
		discover.setOptions(null);

		DiscoverPeerHandler handler = new DiscoverPeerHandler(settings, discover.convertToBytes(), new DatagramSocket());
		handler.receiveBytes(discover.convertToBytes());
	}

	@Test(expected = DhcpParsingException.class)
	public void testReceiveBytesWithWrongOpCode() throws IOException {
		initSettings();
		FakeClient client = new FakeClient();
		byte[] bytes = client.makeDiscover();

		DhcpDiscoverPacket discover = new DhcpDiscoverPacketImpl();
		discover.readFromBytes(bytes);
		discover.setOp(DhcpOpField.BOOTREPLY);

		DiscoverPeerHandler handler = new DiscoverPeerHandler(settings, discover.convertToBytes(), new DatagramSocket());
		handler.receiveBytes(discover.convertToBytes());
	}
}
