package org.kj.pxe.protocol.impl;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.kj.pxe.protocol.impl.BootMenuInformation;
import org.kj.pxe.protocol.impl.BootServerInformation;
import org.kj.pxe.protocol.impl.PxeBootItemSuboption;
import org.kj.pxe.protocol.impl.PxeBootMenuSuboption;
import org.kj.pxe.protocol.impl.PxeBootServersSuboption;
import org.kj.pxe.protocol.impl.PxeDiscoveryControlSuboption;

public class VendorSuboptionsTest {

    @Test(expected = IllegalArgumentException.class)
    public void testPxeDiscoveryControlSuboptionWithInvalidInput() throws Exception {
        PxeDiscoveryControlSuboption op = new PxeDiscoveryControlSuboption((byte) 16);

        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(byteStream);

        op.writeToStream(dos);
    }

    @Test
    public void testPxeDiscoveryControlSuboptionWithValidInput() throws Exception {
        PxeDiscoveryControlSuboption op = new PxeDiscoveryControlSuboption((byte) 3);

        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(byteStream);

        op.writeToStream(dos);

        byte[] expected = { 6, 1, 3 };
        byte[] actual = byteStream.toByteArray();

        assertTrue(Arrays.equals(actual, expected));
    }

    @Test
    public void testPxeBootServersSuboptionWithEmptyInput() throws Exception {
        List<BootServerInformation> info = new ArrayList<BootServerInformation>();
        PxeBootServersSuboption op = new PxeBootServersSuboption(info);

        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(byteStream);

        op.writeToStream(dos);
        byte[] expected = { 8, 0 };
        byte[] actual = byteStream.toByteArray();

        assertTrue(Arrays.equals(actual, expected));
    }

    @Test
    public void testPxeBootServersSuboptionWithValidInput() throws Exception {
        List<InetAddress> addresses = new ArrayList<InetAddress>();
        addresses.add(InetAddress.getByName("0.0.0.0"));
        addresses.add(InetAddress.getByName("1.1.1.1"));
        addresses.add(InetAddress.getByName("2.2.2.2"));

        List<BootServerInformation> info = new ArrayList<BootServerInformation>();
        info.add(new BootServerInformation((short) 0, addresses));
        PxeBootServersSuboption op = new PxeBootServersSuboption(info);

        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(byteStream);

        op.writeToStream(dos);
        byte[] expected = { 8, 15, 0, 0, 3, 0, 0, 0, 0, 1, 1, 1, 1, 2, 2, 2, 2 };
        byte[] actual = byteStream.toByteArray();

        assertTrue(Arrays.equals(actual, expected));
    }

    @Test
    public void testPxeBootItemSuboptionithInvalidInput() throws IOException {
        PxeBootItemSuboption op = new PxeBootItemSuboption((short) 0, (short) 1);

        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(byteStream);

        op.writeToStream(dos);
        byte[] expected = { 71, 4, 0, 0, 0, 1 };
        byte[] actual = byteStream.toByteArray();

        for (byte b : actual) {
            System.out.print(b + " ");
        }
        assertTrue(Arrays.equals(actual, expected));
    }

    @Test
    public void testPxeBootItemSuboptionWithValidInput() throws Exception {
        PxeBootItemSuboption op = new PxeBootItemSuboption((short) 0, (short) 1);

        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(byteStream);

        op.writeToStream(dos);
        byte[] expected = { 71, 4, 0, 0, 0, 1 };
        byte[] actual = byteStream.toByteArray();

        for (byte b : actual) {
            System.out.print(b + " ");
        }
        assertTrue(Arrays.equals(actual, expected));
    }

    @Test
    public void testPxeBootMenuSuboptionWithValidInput() throws Exception {
        List<BootMenuInformation> info = new ArrayList<BootMenuInformation>();
        info.add(new BootMenuInformation((short) 0, "abcd"));

        PxeBootMenuSuboption op = new PxeBootMenuSuboption(info);

        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(byteStream);

        op.writeToStream(dos);
        byte[] expected = { 9, 7, 0, 0, 4, 97, 98, 99, 100 };
        byte[] actual = byteStream.toByteArray();

        for (byte b : actual) {
            System.out.print(b + " ");
        }

        assertTrue(Arrays.equals(actual, expected));
    }
}
