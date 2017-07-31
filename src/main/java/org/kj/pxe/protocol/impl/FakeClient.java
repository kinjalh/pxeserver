package org.kj.pxe.protocol.impl;

import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

import org.kj.pxe.protocol.DhcpDiscoverPacket;
import org.kj.pxe.protocol.DhcpRequestPacket;

/**
 * Creates discover and offer packets using hypothetical values captures. Used
 * only for testing.
 * 
 * @author Kinjal
 *
 */
class FakeClient {

	/**
	 * Constructs a DHCP discover with hypothetical values. Testing only/
	 * 
	 * @return the discover packet in byte array form.
	 * @throws IOException
	 *             if there is an IO error while building the packet or
	 *             converting it to bytes
	 */
	byte[] makeDiscover() throws IOException {
		DhcpDiscoverPacket discover = new DhcpDiscoverPacketImpl();

		discover.setOp(DhcpOpField.BOOTREQUEST);
		discover.setHtype((byte) 0);
		discover.setHlen((byte) 6);
		discover.setHops((byte) 0);
		discover.setXid(0xf399bf50);
		discover.setSecs((short) 4);
		discover.setFlags((short) 0x8000);

		discover.setCiaddr(InetAddress.getByName("0.0.0.0"));
		discover.setYiaddr(InetAddress.getByName("0.0.0.0"));
		discover.setSiaddr(InetAddress.getByName("0.0.0.0"));
		discover.setGiaddr(InetAddress.getByName("0.0.0.0"));

		discover.setChaddr(new byte[] { 0, 15, (byte) 0xf2, 99, (byte) 0xbf, 50, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 });
		discover.setSname(new String(new byte[64]));
		discover.setFile(new String(new byte[128]));

		discover.setMagicCookie("DHCP".getBytes());

		List<DhcpOption> options = new ArrayList<DhcpOption>();

		DhcpOption messageType = new DhcpOption((short) DhcpOptionTag.DHCP_MESSAGE_TYPE_TAG.getValue(), (short) 1,
				new byte[] { (byte) DhcpMessageTypeValue.DHCPDISCOVER.getValue() });
		options.add(messageType);

		DhcpOption classId = new DhcpOption((short) DhcpOptionTag.VENDOR_CLASS_IDENTIFIER.getValue(),
				(short) "PXEClient:Arch:xxxxx:UNDI:yyyzzz".getBytes().length,
				"PXEClient:Arch:xxxxx:UNDI:yyyzzz".getBytes());
		options.add(classId);

		DhcpOption end = new DhcpOption((short) DhcpOptionTag.END.getValue(), (short) 0, new byte[] {});
		options.add(end);

		discover.setOptions(options);
		return discover.convertToBytes();
	}

	/**
	 * Constructs a DHCP request with hypothetical values. Testing only.
	 * 
	 * @return the request packet in byte array form.
	 * @throws IOException
	 *             if there is an IO error while building the packet or
	 *             converting it to bytes
	 */
	byte[] makeRequest() throws IOException {
		DhcpRequestPacket request = new DhcpRequestPacketImpl();

		request.setOp(DhcpOpField.BOOTREQUEST);
		request.setHtype((byte) 0);
		request.setHlen((byte) 6);
		request.setHops((byte) 0);
		request.setXid(0xf399bf50);
		request.setSecs((short) 4);
		request.setFlags((short) 0x8000);

		request.setCiaddr(InetAddress.getByName("0.0.0.0"));
		request.setYiaddr(InetAddress.getByName("0.0.0.0"));
		request.setSiaddr(InetAddress.getByName("0.0.0.0"));
		request.setGiaddr(InetAddress.getByName("0.0.0.0"));

		request.setChaddr(new byte[] { 0, 15, (byte) 0xf2, 99, (byte) 0xbf, 50, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 });
		request.setSname(new String(new byte[64]));
		request.setFile(new String(new byte[128]));

		request.setMagicCookie("DHCP".getBytes());

		List<DhcpOption> options = new ArrayList<DhcpOption>();

		DhcpOption messageType = new DhcpOption((short) DhcpOptionTag.DHCP_MESSAGE_TYPE_TAG.getValue(), (short) 1,
				new byte[] { (byte) DhcpMessageTypeValue.DHCPREQUEST.getValue() });
		options.add(messageType);

		DhcpOption classId = new DhcpOption((short) DhcpOptionTag.VENDOR_CLASS_IDENTIFIER.getValue(),
				(short) "PXEClient:Arch:xxxxx:UNDI:yyyzzz".getBytes().length,
				"PXEClient:Arch:xxxxx:UNDI:yyyzzz".getBytes());
		options.add(classId);

		DhcpOption end = new DhcpOption((short) DhcpOptionTag.END.getValue(), (short) 0, new byte[] {});
		options.add(end);

		request.setOptions(options);
		return request.convertToBytes();
	}
}
