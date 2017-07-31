package org.kj.pxe.protocol;

import java.net.InetAddress;
import java.util.List;

import org.kj.pxe.protocol.impl.DhcpOpField;
import org.kj.pxe.protocol.impl.DhcpOption;

/**
 * Contains getters and setters for all the data fields in a DHCP Packet.
 * 
 * @author Kinjal
 *
 */
public interface DhcpPacket {

    DhcpOpField getOp();

    void setOp(DhcpOpField op);

    byte getHtype();

    void setHtype(byte htype);

    byte getHlen();

    void setHlen(byte hlen);

    byte getHops();

    void setHops(byte hops);

    int getXid();

    void setXid(int xid);

    short getSecs();

    void setSecs(short secs);

    short getFlags();

    void setFlags(short flags);

    InetAddress getCiaddr();

    void setCiaddr(InetAddress ciaddr);

    InetAddress getYiaddr();

    void setYiaddr(InetAddress yiaddr);

    InetAddress getSiaddr();

    void setSiaddr(InetAddress siaddr);

    InetAddress getGiaddr();

    void setGiaddr(InetAddress giaddr);

    byte[] getChaddr();

    void setChaddr(byte[] chaddr);

    String getSname();

    void setSname(String sname);

    String getFile();

    void setFile(String file);

    List<DhcpOption> getOptions();

    void setOptions(List<DhcpOption> options);

    byte[] getMagicCookie();

    void setMagicCookie(byte[] magicCookie);

    byte[] convertToBytes();

    void readFromBytes(byte[] buf);

}