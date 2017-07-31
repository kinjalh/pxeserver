package org.kj.pxe.protocol.impl;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import org.junit.Test;
import org.kj.pxe.config.DhcpSettings;
import org.kj.pxe.config.impl.DhcpSettingsImpl;
import org.kj.pxe.protocol.DhcpAckPacket;
import org.kj.pxe.protocol.DhcpRequestPacket;

public class RequestPeerHandlerTest {

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
	public void testReceiveBytesWithInvalidInput() throws IOException {
		initSettings();
		DhcpRequestPacket request = new DhcpRequestPacketImpl();
		(new RequestPeerHandler(settings, new byte[] {}, InetAddress.getLocalHost(), new DatagramSocket()))
				.receiveBytes(request.convertToBytes());
	}

	@Test
	public void testReceiveBytesWithValidInput() throws IOException {
		initSettings();
		FakeClient client = new FakeClient();
		byte[] bytes = client.makeRequest();
		RequestPeerHandler handler = new RequestPeerHandler(settings, new byte[] {}, InetAddress.getLocalHost(),
				new DatagramSocket());
		DhcpAckPacket ack = handler.receiveBytes(bytes);

		assertTrue(ack.getOp().getValue() == DhcpOpField.BOOTREPLY.getValue());
		// assertTrue(Arrays.equals(ack.getCiaddr().getAddress(), new byte[] {
		// 0, 0, 0 , 0 } ));
		// assertTrue(Arrays.equals(ack.getMagicCookie(), "DHCP".getBytes()));
		// ((DhcpAckPacketImpl) ack).validateDhcpOptions(ack.getOptions());
	}

	@Test(expected = DhcpPacketToByteConversionException.class)
	public void testReceiveBytesWithNoOptions() throws IOException {
		initSettings();
		FakeClient client = new FakeClient();
		byte[] bytes = client.makeRequest();

		DhcpRequestPacket request = new DhcpRequestPacketImpl();
		request.readFromBytes(bytes);
		request.setOptions(null);

		RequestPeerHandler handler = new RequestPeerHandler(settings, new byte[] {}, InetAddress.getLocalHost(),
				new DatagramSocket());
		handler.receiveBytes(request.convertToBytes());
	}

	@Test(expected = DhcpParsingException.class)
	public void testReceiveBytesWithWrongOpCode() throws IOException {
		initSettings();
		FakeClient client = new FakeClient();
		byte[] bytes = client.makeRequest();

		DhcpRequestPacket request = new DhcpRequestPacketImpl();
		request.readFromBytes(bytes);
		request.setOp(DhcpOpField.BOOTREPLY);

		RequestPeerHandler handler = new RequestPeerHandler(settings, new byte[] {}, InetAddress.getLocalHost(),
				new DatagramSocket());
		handler.receiveBytes(request.convertToBytes());
	}
}
