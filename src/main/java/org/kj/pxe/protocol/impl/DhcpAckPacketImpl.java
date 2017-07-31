package org.kj.pxe.protocol.impl;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.kj.pxe.config.DhcpSettings;
import org.kj.pxe.protocol.DhcpAckPacket;
import org.kj.pxe.protocol.DhcpRequestPacket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * An object that represents a DHCP Ack packet.
 * 
 * @author Kinjal
 *
 */
class DhcpAckPacketImpl extends AbstractDhcpPacketImpl implements DhcpAckPacket {

	private static Logger logger = LoggerFactory.getLogger(DhcpAckPacketImpl.class);
	private DhcpSettings settings;

	DhcpAckPacketImpl(DhcpSettings settings) {
		this.settings = settings;
	}

	/**
	 * Sets the data fields of this DHCP Ack packet so that it responds to the
	 * given request packet. The next server IP address, siaddr, is set to the
	 * server from which the boot file will be downloaded with TFTP. If the
	 * client has DHCP option 77 set to "ipxe" or "gpxe" (case insensitive),
	 * then vendor options are added.
	 * 
	 * @param request
	 *            the request packet using which this ack packet is built
	 */
	@Override
	public void buildAck(DhcpRequestPacket request) {
		try {
			setOp(DhcpOpField.BOOTREPLY);
			setHtype(request.getHtype());
			setHlen(request.getHlen());
			setHops((byte) 0);

			setXid(request.getXid());

			setSecs(request.getSecs());
			setFlags(request.getFlags());

			setCiaddr(InetAddress.getByName("0.0.0.0"));
			setYiaddr(InetAddress.getByName("0.0.0.0"));
			setSiaddr(settings.getBootServerAddress());
			setGiaddr(request.getGiaddr());

			setChaddr(request.getChaddr());

			setSname(request.getSname());

			setFile(settings.getBootFileName());

			setMagicCookie(request.getMagicCookie());

			List<DhcpOption> options = new ArrayList<DhcpOption>();

			DhcpOption messageTypeOption = new DhcpOption((short) DhcpOptionTag.DHCP_MESSAGE_TYPE_TAG.getValue(),
					(short) 1, new byte[] { (byte) DhcpMessageTypeValue.DHCPACK.getValue() });
			options.add(messageTypeOption);

			DhcpOption serverIdOption = new DhcpOption((short) DhcpOptionTag.SERVER_ID.getValue(), (short) 4,
					InetAddress.getLocalHost().getAddress());
			options.add(serverIdOption);

			for (int i = 0; i < request.getOptions().size(); ++i) {
				if (request.getOptions().get(i).getTag() == DhcpOptionTag.CLIENT_IDENTIFIER.getValue()) {
					options.add(request.getOptions().get(i));
				}
			}

			for (int i = 0; i < request.getOptions().size(); ++i) {
				if (request.getOptions().get(i).getTag() == DhcpOptionTag.VENDOR_CLASS_IDENTIFIER.getValue()) {
					options.add(request.getOptions().get(i));
				}
			}

			for (DhcpOption option : request.getOptions()) {
				if (option.getTag() == DhcpOptionTag.USER_CLASS.getValue()) {
					String str = new String(option.getData());
					if (str.toLowerCase().contains("ipxe") || str.toLowerCase().contains("gpxe")) {

						VendorOptions vendorOption = new VendorOptions();

						PxeDiscoveryControlSuboption dis = new PxeDiscoveryControlSuboption(
								settings.getVendorOptionDiscoveryControl());
						vendorOption.getSuboptions().add(dis);

						List<BootServerInformation> serverList = new ArrayList<BootServerInformation>();
						List<InetAddress> serverAddrs = new ArrayList<InetAddress>();
						serverAddrs.add(settings.getVendorOptionBootServer());
						BootServerInformation sInfo = new BootServerInformation((short) 0, serverAddrs); // ipxe
																											// hack
						serverList.add(sInfo);
						PxeBootServersSuboption servers = new PxeBootServersSuboption(serverList);
						vendorOption.getSuboptions().add(servers);

						String str1 = settings.getVendorOptionBootMenu();
						List<BootMenuInformation> menuInfo = new ArrayList<BootMenuInformation>();
						BootMenuInformation info = new BootMenuInformation(
								Short.parseShort(str1.substring(0, str1.indexOf(',')).trim()),
								str1.substring(str1.indexOf(',') + 1).trim());
						menuInfo.add(info);
						PxeBootMenuSuboption menu = new PxeBootMenuSuboption(menuInfo);
						vendorOption.getSuboptions().add(menu);

						PxeMenuPromptSuboption prompt = new PxeMenuPromptSuboption((byte) 10,
								settings.getVendorOptionMenuPrompt());
						vendorOption.getSuboptions().add(prompt);

						short[] arr = settings.getVendorOptionBootItem();
						PxeBootItemSuboption item = new PxeBootItemSuboption(arr[0], arr[1]);
						vendorOption.getSuboptions().add(item);

						PxeEndSuboption end = new PxeEndSuboption();
						vendorOption.getSuboptions().add(end);

						options.add(vendorOption);
					}
				}
			}

			DhcpOption endOption = new DhcpOption((short) DhcpOptionTag.END.getValue(), (short) 0, new byte[] {});
			options.add(endOption);

			setOptions(options);

		} catch (IOException e) {
			throw new DhcpPacketConstructionException(e);
		}

	}

	/**
	 * Reads the packet data from a byte array. This assumes that the bytes
	 * represent the data fields as specified by <a> href="RFC
	 * 2131">https://www.ietf.org/rfc/rfc2131.txt</a>.
	 * 
	 * @param buf
	 *            the byte array to read from
	 * @throws DhcpParsingException
	 *             if the opCode is the wrong value.
	 */
	@Override
	public void readFromBytes(byte[] buf) {
		ByteArrayInputStream bis = new ByteArrayInputStream(buf);
		DataInputStream dataStream = new DataInputStream(bis);

		// Check to make sure that opCode is 1
		try {
			byte opCode = dataStream.readByte();
			if (opCode != DhcpOpField.BOOTREPLY.getValue()) {
				logger.error("Threw exception because Ack packet opCode was not 2.");
				throw new DhcpParsingException("Ack packet opCode must be 2.");
			}
			logger.debug("op: " + DhcpOpField.BOOTREPLY.getValue());
			setOp(DhcpOpField.BOOTREPLY);

			// read hardware type (1 byte)
			byte htype = dataStream.readByte();
			setHtype(htype);
			logger.debug("hardware type: " + htype);

			// read hardware address length (1 byte)
			byte hlen = dataStream.readByte();
			setHlen(hlen);
			logger.debug("hardware address length: " + hlen);

			// read hop count (1 byte)
			byte hops = dataStream.readByte();
			setHops(hops);
			logger.debug("hop count: " + hops);

			// read transaction id (4 bytes) and save as string
			int xid = dataStream.readInt();
			setXid(xid);
			logger.debug("transaction id: " + String.format("%08x", xid));

			// read number of seconds (2 bytes)
			short secs = dataStream.readShort();
			setSecs(secs);
			logger.debug("number of seconds: " + Short.toUnsignedInt(secs));

			// read flags (2 bytes)
			short flags = dataStream.readShort();
			setFlags(flags);
			logger.debug("flags: " + Short.toUnsignedInt(flags));

			// read client ip address (4 bytes)
			byte[] ciaddrNums = new byte[4];
			dataStream.read(ciaddrNums);
			InetAddress ciaddr = InetAddress.getByAddress(ciaddrNums);
			setCiaddr(ciaddr);
			logger.debug("client ip address: " + ciaddr.toString().substring(1));

			// read your ip address (4 bytes)
			byte[] yiaddrNums = new byte[4];
			dataStream.read(yiaddrNums);
			InetAddress yiaddr = InetAddress.getByAddress(yiaddrNums);
			setYiaddr(yiaddr);
			logger.debug("your ip address: " + yiaddr.toString().substring(1));

			// read server ip address (4 bytes)
			byte[] siaddrNums = new byte[4];
			dataStream.read(siaddrNums);
			InetAddress siaddr = InetAddress.getByAddress(siaddrNums);
			setSiaddr(siaddr);
			logger.debug("server ip address: " + siaddr.toString().substring(1));

			// read gateway ip address (4 bytes)
			byte[] giaddrNums = new byte[4];
			dataStream.read(giaddrNums);
			InetAddress giaddr = InetAddress.getByAddress(giaddrNums);
			setGiaddr(giaddr);
			logger.debug("gateway ip address: " + giaddr.toString().substring(1));

			// read client hardware address (16 bytes)
			byte[] chaddr = new byte[16];
			dataStream.read(chaddr);
			setChaddr(chaddr);
			String hexString = "";
			for (int i = 0; i < chaddr.length; ++i) {
				hexString += String.format("%02x", chaddr[i]) + " ";
			}
			logger.debug("client hardware address: " + hexString);

            // read server host name (64 bytes)
            // Consider padding, String should only use values up to first null character, which is 0
            byte[] shn = new byte[64];
            dataStream.read(shn);
            String serverHostName = new String(shn, 0, ParsingUtils.findByteIndex(shn, (byte)0));
            setSname(serverHostName);
            logger.debug("server host name: " + serverHostName);

            // read boot file name (128 bytes)
            // Consider padding, String should only use values up to first null character, which is 0
            byte[] bfn = new byte[128];
            dataStream.read(bfn);
            String bootFileName = new String(bfn, 0, ParsingUtils.findByteIndex(bfn, (byte) 0));
            setFile(bootFileName);
            logger.debug("boot file name: " + bootFileName);

			// read magic cookie
			byte[] magicCookie = new byte[4];
			dataStream.read(magicCookie);
			setMagicCookie(magicCookie);
			logger.debug("magic cookie: " + Arrays.toString(magicCookie));

			// Check DHCP option 60 Class ID is set to pxe client tag
			short tag;
			List<DhcpOption> options = new ArrayList<DhcpOption>();
			do {
				tag = (short) dataStream.readUnsignedByte();
				short len = 0;
				byte[] data = null;
				if (tag != DhcpOptionTag.PAD.getValue() && tag != DhcpOptionTag.END.getValue()) {
					len = (short) dataStream.readUnsignedByte();
					data = new byte[len];
					dataStream.read(data);
				}
				DhcpOption option = new DhcpOption(tag, len, data);
				options.add(option);
				logger.debug(option.toString());
			} while (tag != DhcpOptionTag.END.getValue());

			validateDhcpOptions(options);

			setOptions(options);

		} catch (IOException e) {
			throw new DhcpParsingException(e);
		}
	}

	/**
	 * Checks the DHCP options to check that the message type option (53) and
	 * vendor class ID (60) exist. The message type option is checked to make
	 * sure that it has length 1 and data field corresponding to DHCP Ack. The
	 * vendor class ID is checked to make sure it contains "PXEClient:Arch:" and
	 * "UNDI".
	 *
	 * @param options
	 *            the List of options to validate
	 * @throws DhcpTagNotFoundException
	 *             if either the message type or the vendor class ID option are
	 *             not found
	 */
	void validateDhcpOptions(List<DhcpOption> options) throws DhcpTagNotFoundException {
		boolean foundDhcpMessageTypeTag = false;
		boolean foundVendorClassIdTag = false;
		for (DhcpOption option : options) {
			if (option.getTag() == DhcpOptionTag.DHCP_MESSAGE_TYPE_TAG.getValue()) {
				foundDhcpMessageTypeTag = true;
				validateDhcpMessageTypeOption(option);
			} else if (option.getTag() == DhcpOptionTag.VENDOR_CLASS_IDENTIFIER.getValue()) {
				foundVendorClassIdTag = true;
				validateDhcpVendorClassIdOption(option);
			}
		}

		if (!foundDhcpMessageTypeTag) {
			throw new DhcpTagNotFoundException("Missing DHCP message type tag (53)");
		}
		if (!foundVendorClassIdTag) {
			throw new DhcpTagNotFoundException("Missing DHCP vendor class identification tag (60)");
		}
	}

	/**
	 * Checks the given option to make sure it has length 1 and its data field
	 * is set to the value corresponding to DHCP Ack.
	 * 
	 * @param option
	 *            the option to check
	 * @throws DhcpOptionDataIncorrectException
	 *             if either the length or the data field is not the correct
	 *             value
	 */
	void validateDhcpMessageTypeOption(DhcpOption option) throws DhcpOptionDataIncorrectException {
		if (option.getLen() != 1) {
			throw new DhcpOptionDataIncorrectException("DHCP Message type option must have length 1");
		}
		byte value = option.getData()[0];
		if (value != DhcpMessageTypeValue.DHCPACK.getValue()) {
			throw new DhcpOptionDataIncorrectException(
					"DHCP Message type option must contain 5 as data. Got data: " + Arrays.toString(option.getData()));
		}
	}

	/**
	 * Checks the given option to make sure it contains "PXEClient:Arch:" and
	 * "UNDI".
	 * 
	 * @param option
	 *            the option to check
	 * @throws DhcpOptionDataIncorrectException
	 *             if either the length or the data field is not the correct
	 *             value
	 */
	void validateDhcpVendorClassIdOption(DhcpOption option) throws DhcpOptionDataIncorrectException {
		String id = new String(option.getData());
		if (!id.contains("PXEClient:Arch:") || !id.contains("UNDI")) {
			throw new DhcpOptionDataIncorrectException(
					"DHCP Vendor Class ID option must be in format PXEClient:Arch:xxxxx:UNDI:yyyzzz" + " Got data: "
							+ id);
		}
	}
}
