package org.kj.pxe.protocol;

public interface DhcpOfferPacket extends DhcpPacket {
    
    /**
     * Sets the fields of this Offer packet to be a response to the given discover
     * packet.
     * 
     * @param packet
     *            the packet to build this as a response to
     */
    void buildOffer(DhcpDiscoverPacket packet);
}
