package org.kj.pxe.protocol;

public interface DhcpAckPacket extends DhcpPacket {

    /**
     * Sets the fields of this Ack packet to be a response to the given request
     * packet.
     * 
     * @param request
     *            the packet to build this as a response to
     */
    void buildAck(DhcpRequestPacket request);
}
