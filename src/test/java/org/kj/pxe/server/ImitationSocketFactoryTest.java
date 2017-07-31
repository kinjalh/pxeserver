package org.kj.pxe.server;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import org.junit.Test;
import org.kj.pxe.config.DhcpSettings;
import org.kj.pxe.config.impl.DhcpSettingsImpl;

public class ImitationSocketFactoryTest {
	
	DhcpSettings settings;

	private void initSettings() {
		try {
			settings = new DhcpSettingsImpl("/dhcp_test.properties");
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(0);
		}
	}

	@Test
	public void testGetDhcpOfferSocketPassRightPort() throws IOException {
		initSettings();
		SocketFactory factory = new ImitationSocketFactory(settings);
		DatagramSocket socket = factory.getDhcpOfferSocket();
		assertTrue(socket.getPort() == 0);
	}

	@Test
	public void testGetDhcpOfferSocketRightInetAddress() throws IOException {
		initSettings();
		SocketFactory factory = new ImitationSocketFactory(settings);
		DatagramSocket socket = factory.getDhcpOfferSocket();
		assertTrue(socket.getInetAddress().equals(InetAddress.getByName("255.255.255.255")));
	}

	@Test
	public void testGetDhcpOfferSocketBroadcastOn() throws IOException {
		initSettings();
		SocketFactory factory = new ImitationSocketFactory(settings);
		DatagramSocket socket = factory.getDhcpOfferSocket();
		assertTrue(socket.getBroadcast());
	}

	@Test
	public void testGetDhcpAckSocketRightPort() throws IOException {
		initSettings();
		SocketFactory factory = new ImitationSocketFactory(settings);
		DatagramSocket socket = factory.getDhcpAckSocket(InetAddress.getByName("255.255.255.255"));
		assertTrue(socket.getPort() == 0);
	}

	@Test
	public void testGetDhcpAckSocketRightInetAddress() throws IOException {
		initSettings();
		SocketFactory factory = new ImitationSocketFactory(settings);
		DatagramSocket socket = factory.getDhcpAckSocket(InetAddress.getByName("255.255.255.255"));
		assertTrue(socket.getInetAddress().equals(InetAddress.getByName("255.255.255.255")));
	}

	@Test
	public void testGetDhcpAckSocketBroadcastOff() throws IOException {
		initSettings();
		SocketFactory factory = new ImitationSocketFactory(settings);
		DatagramSocket socket = factory.getDhcpAckSocket(InetAddress.getByName("255.255.255.255"));
		assertTrue(!socket.getBroadcast());
	}
}
