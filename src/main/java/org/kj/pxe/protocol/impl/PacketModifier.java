package org.kj.pxe.protocol.impl;

import java.util.Arrays;

import org.kj.pxe.config.DhcpSettings;
import org.kj.pxe.protocol.DhcpPacket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Modifies a packet based on certain described criteria.
 * 
 * @author Kinjal
 *
 */
class PacketModifier {

	private static Logger logger = LoggerFactory.getLogger(PacketModifier.class);
	private DhcpSettings settings;

	/**
	 * Constructs a PacketModifier that reads from the given settings
	 * 
	 * @param settings
	 *            the settings
	 */
	PacketModifier(DhcpSettings settings) {
		this.settings = settings;
	}

	/**
	 * In the request DHCP option 77 is set to "ipxe" or "gpxe", changes the
	 * response's boot file name to that specified by
	 * settings.getIpxeBootFileName() or settings.getGpxeBootFileName(). If the
	 * offer should redirect to tftp, then sets siaddr to the boot server
	 * address
	 * 
	 * @param request
	 *            the packet to respond to
	 * @param reply
	 *            the response packet
	 */
	void modifyReply(DhcpPacket request, DhcpPacket reply) {

		for (DhcpOption option : request.getOptions()) {
			if (option.getTag() == DhcpOptionTag.USER_CLASS.getValue()) {
				String str = new String(option.getData());
				if (str.toLowerCase().contains("ipxe")) {
					reply.setFile(settings.getIpxeBootFileName());
					if (settings.getIpxeOfferToTftpRedirect()) {
						reply.setSiaddr(settings.getBootServerAddress());
					}
					logger.debug("modified response packet due to request coming from ipxe client");
				} else if (str.toLowerCase().contains("gpxe")) {
					reply.setFile(settings.getGpxeBootFileName());
					if (settings.getIpxeOfferToTftpRedirect()) {
						reply.setSiaddr(settings.getBootServerAddress());
					}
					logger.debug("modified response packet due to request coming from gpxe client");
				}

			} else if (option.getTag() == DhcpOptionTag.CLIENT_SYSTEM_ARCH.getValue()) {
				byte[] data = option.getData();
				if (!Arrays.equals(data, new byte[] { 0, 0 })) {
					reply.setFile(settings.getOption93BootFileName());
					logger.debug("modified response packet due to request having non 0 system architecture");
				}
			}

		}

	}
}
