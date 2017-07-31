package org.kj.pxe.protocol;

public interface DhcpRequestPacket extends DhcpPacket {

    void buildRequest(DhcpOfferPacket offer);
}
