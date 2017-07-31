package org.kj.pxe.protocol.impl;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.kj.pxe.protocol.DhcpOfferPacket;
import org.kj.pxe.protocol.DhcpRequestPacket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * An object that represents a DHCP Ack packet.
 * 
 * @author Kinjal
 *
 */
public class DhcpRequestPacketImpl extends AbstractDhcpPacketImpl implements DhcpRequestPacket {

	private static Logger logger = LoggerFactory.getLogger(DhcpRequestPacketImpl.class);
	private static String BOOT_FILE_NAME = "pxelinux.0";

	/**
	 * Sets the data fields of this DHCP Offer packet so that it responds to the
	 * given offer packet. The next server IP address, siaddr, is set to the IP
	 * address of the server.
	 * 
	 * @param offer
	 *            the offer packet using which this request packet is built
	 */
	@Override
	public void buildRequest(DhcpOfferPacket offer) {

		try {
			setOp(DhcpOpField.BOOTREPLY);
			setHtype(offer.getHtype());
			setHlen(offer.getHlen());
			setHops((byte) 0);

			setXid(offer.getXid());

			setSecs(offer.getSecs());
			setFlags(offer.getFlags());

			setCiaddr(InetAddress.getByName("0.0.0.0"));
			setYiaddr(InetAddress.getByName("0.0.0.0"));
			// NEXT SERVER ADDRESS MUST BE OF THIS MACHINE
			setSiaddr(offer.getSiaddr());
			setGiaddr(offer.getGiaddr());

			setChaddr(offer.getChaddr());

			setSname(offer.getSname());

			setFile(BOOT_FILE_NAME);

			setMagicCookie(offer.getMagicCookie());
			logger.debug("set magic cookie to: " + Arrays.toString(getMagicCookie()));

			List<DhcpOption> options = new ArrayList<DhcpOption>();
			DhcpOption messageTypeOption = new DhcpOption((short) DhcpOptionTag.DHCP_MESSAGE_TYPE_TAG.getValue(),
					(short) 1, new byte[] { (byte) DhcpMessageTypeValue.DHCPREQUEST.getValue() });
			options.add(messageTypeOption);

			DhcpOption serverIdOption = new DhcpOption((short) 54, (short) 4, getSiaddr().getAddress());
			options.add(serverIdOption);

			DhcpOption classIdOption = new DhcpOption((short) 60, (short) "PXEClient".getBytes().length,
					"PXEClient".getBytes());
			options.add(classIdOption);

			for (int i = 0; i < offer.getOptions().size(); ++i) {
				if (offer.getOptions().get(i).getTag() == DhcpOptionTag.CLIENT_IDENTIFIER.getValue()) {
					options.add(offer.getOptions().get(i));
				}
			}

			VendorOptions vendorOption = new VendorOptions();
			PxeDiscoveryControlSuboption dis = new PxeDiscoveryControlSuboption((byte) 0);
			vendorOption.getSuboptions().add(dis);

			DhcpOption endOption = new DhcpOption((short) DhcpOptionTag.END.getValue(), (short) 0, new byte[] {});
			options.add(endOption);

			setOptions(options);

		} catch (IOException e) {
			throw new DhcpPacketConstructionException(e);
		}
	}

	/**
	 * Reads the packet data from a byte array. This assumes that the bytes
	 * represent the data fields as specified by <a href=
	 * "https://www.ietf.org/rfc/rfc2131.txt">https://www.ietf.org/rfc/rfc2131.txt</a>.
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
			if (opCode != DhcpOpField.BOOTREQUEST.getValue()) {
				logger.error("Threw exception because Request packet opCode was not 1.");
				throw new DhcpParsingException("Request packet opCode must be 1.");
			}
			logger.debug("op: " + DhcpOpField.BOOTREQUEST.getValue());
			setOp(DhcpOpField.BOOTREQUEST);

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

			// TODO: validate magic cookie values before saving, throw exception
			// if
			// values are not as they should be
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
	 * sure that it has length 1 and data field corresponding to DHCP Request.
	 * The vendor class ID is checked to make sure it contains "PXEClient:Arch:"
	 * and "UNDI".
	 *
	 * @param options
	 *            the List of options to validate
	 * @throws DhcpTagNotFoundException
	 *             if either the message type or the vendor class ID option are
	 *             not found
	 */
	public void validateDhcpOptions(List<DhcpOption> options) throws DhcpTagNotFoundException {
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
	 * is set to the value corresponding to DHCP Request.
	 * 
	 * @param option
	 *            the option to check
	 * @throws DhcpOptionDataIncorrectException
	 *             if either the length or the data field is not the correct
	 *             value
	 */
	public void validateDhcpMessageTypeOption(DhcpOption option) throws DhcpOptionDataIncorrectException {
		if (option.getLen() != 1) {
			throw new DhcpOptionDataIncorrectException("DHCP Message type option must have length 1");
		}
		byte value = option.getData()[0];
		if (value != DhcpMessageTypeValue.DHCPREQUEST.getValue()) {
			throw new DhcpOptionDataIncorrectException(
					"DHCP Message type option must contain 3 as data. Got data: " + Arrays.toString(option.getData()));
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
	public void validateDhcpVendorClassIdOption(DhcpOption option) throws DhcpOptionDataIncorrectException {
		String id = new String(option.getData());
		if (!id.contains("PXEClient:Arch:") || !id.contains("UNDI")) {
			throw new DhcpOptionDataIncorrectException(
					"DHCP Vendor Class ID option must be in format PXEClient:Arch:xxxxx:UNDI:yyyzzz");
		}
	}
}
