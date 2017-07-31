package org.kj.pxe.protocol.impl;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.kj.pxe.protocol.DhcpDiscoverPacket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * An object that represents a DHCP Ack packet. Contains all the fields
 * specified by <a> href="RFC 2131">https://www.ietf.org/rfc/rfc2131.txt</a>.
 * 
 * @author Kinjal
 *
 */
class DhcpDiscoverPacketImpl extends AbstractDhcpPacketImpl implements DhcpDiscoverPacket {

    protected static Logger logger = LoggerFactory.getLogger(DhcpDiscoverPacketImpl.class);

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
            if (opCode != DhcpOpField.BOOTREQUEST.getValue()) {
                logger.error("Threw exception because Discover packet opCode was not 1.");
                throw new DhcpParsingException("Discover packet opCode must be 1.");
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
     * sure that it has length 1 and data field corresponding to DHCP Discover.
     * The vendor class ID is checked to make sure it contains "PXEClient:Arch:"
     * and "UNDI".
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
     * is set to the value corresponding to DHCP Discover.
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
        if (value != DhcpMessageTypeValue.DHCPDISCOVER.getValue()) {
            throw new DhcpOptionDataIncorrectException(
                    "DHCP Message type option must contain 1 as data. Got data: " + Arrays.toString(option.getData()));
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
                    "DHCP Vendor Class ID option must be in format PXEClient:Arch:xxxxx:UNDI:yyyzzz");
        }
    }
}
