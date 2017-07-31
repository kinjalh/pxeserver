package org.kj.pxe.protocol;

import java.net.DatagramSocket;
import java.net.InetAddress;

public interface DatagramPacketWriter {
    
    void writePacket(byte[] buffer, int length, DatagramSocket socket);

	void writePacket(byte[] buffer, int length, DatagramSocket socket, InetAddress address, int port);
}
