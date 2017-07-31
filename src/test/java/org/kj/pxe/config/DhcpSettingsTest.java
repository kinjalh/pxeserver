package org.kj.pxe.config;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.kj.pxe.config.DhcpSettings;
import org.kj.pxe.config.impl.DhcpSettingsImpl;

public class DhcpSettingsTest {

	private String testPropsName;
	private String emptyPropsName;
	
	@Before
	public void setemptyPropsNames() {
		testPropsName = "/dhcp_test.properties";
		emptyPropsName = "/empty.properties";
	}
	
    @Test
    public void testGetDiscoveryPort() throws IOException {
        DhcpSettings settings = new DhcpSettingsImpl(testPropsName);
        int port = settings.getDiscoverPort();

        assertTrue(port == 0);
    }

    @Test
    public void testGetDiscoveryPortEmpty() throws IOException {
        DhcpSettings settings = new DhcpSettingsImpl(emptyPropsName);
        int port = settings.getDiscoverPort();

        assertTrue(port == 67);
    }

    @Test
    public void testGetOfferPort() throws IOException {
        DhcpSettings settings = new DhcpSettingsImpl(testPropsName);
        int port = settings.getOfferPort();

        assertTrue(port == 0);
    }

    @Test
    public void testGetOfferPortEmpty() throws IOException {
        DhcpSettings settings = new DhcpSettingsImpl(emptyPropsName);
        int port = settings.getOfferPort();

        assertTrue(port == 68);
    }

    @Test
    public void testGetRequestPort() throws IOException {
        DhcpSettings settings = new DhcpSettingsImpl(testPropsName);
        int port = settings.getRequestPort();

        assertTrue(port == 0);
    }

    @Test
    public void testGetRequestPortEmpty() throws IOException {
        DhcpSettings settings = new DhcpSettingsImpl(emptyPropsName);
        int port = settings.getRequestPort();

        assertTrue(port == 4011);
    }

    @Test
    public void testGetAckPort() throws IOException {
        DhcpSettings settings = new DhcpSettingsImpl(testPropsName);
        int port = settings.getAckPort();

        assertTrue(port == 0);
    }

    @Test
    public void testGetAckPortEmpty() throws IOException {
        DhcpSettings settings = new DhcpSettingsImpl(emptyPropsName);
        int port = settings.getAckPort();

        assertTrue(port == 4011);
    }

    @Test
    public void testGetBootFileName() throws IOException {
        DhcpSettings settings = new DhcpSettingsImpl(testPropsName);
        String str = settings.getBootFileName();

        assertTrue(str.equals("filename"));
    }

    @Test
    public void testGetBootFileNameEmpty() throws IOException {
        DhcpSettings settings = new DhcpSettingsImpl(emptyPropsName);
        String str = settings.getBootFileName();

        assertTrue(str.equals("pxelinux.0"));
    }

    @Test
    public void testGetBootServerAddress() throws IOException {
        DhcpSettings settings = new DhcpSettingsImpl(testPropsName);
        InetAddress addr = settings.getBootServerAddress();

        assertTrue(addr.equals(InetAddress.getByName("192.168.1.7")));
    }

    @Test
    public void testGetBootServerAddressEmpty() throws IOException {
        DhcpSettings settings = new DhcpSettingsImpl(emptyPropsName);
        InetAddress addr = settings.getBootServerAddress();

        assertTrue(addr.equals(InetAddress.getLocalHost()));
    }

    @Test
    public void testGetDiscoveryControl() throws IOException {
        DhcpSettings settings = new DhcpSettingsImpl(testPropsName);
        int disc = settings.getVendorOptionDiscoveryControl();

        assertTrue(disc == 1);
    }

    @Test
    public void testGetDiscoveryControlEmpty() throws IOException {
        DhcpSettings settings = new DhcpSettingsImpl(emptyPropsName);
        int disc = settings.getVendorOptionDiscoveryControl();

        assertTrue(disc == 0);
    }

    @Test
    public void testGetBootServerOption() throws IOException {
        DhcpSettings settings = new DhcpSettingsImpl(testPropsName);
        InetAddress addr = settings.getVendorOptionBootServer();

        assertTrue(addr.equals(InetAddress.getByName("192.168.1.7")));
    }

    @Test
    public void testGetBootServerOptionEmpty() throws IOException {
        DhcpSettings settings = new DhcpSettingsImpl(emptyPropsName);
        InetAddress addr = settings.getVendorOptionBootServer();

        assertTrue(addr.equals(InetAddress.getLocalHost()));
    }

    @Test
    public void testGetBootMenuOption() throws IOException {
        DhcpSettings settings = new DhcpSettingsImpl(testPropsName);
        String str = settings.getVendorOptionBootMenu();

        assertTrue(str.equals("string"));
    }

    @Test
    public void testGetBootMenuOptionEmpty() throws IOException {
        DhcpSettings settings = new DhcpSettingsImpl(emptyPropsName);
        String str = settings.getVendorOptionBootMenu();

        assertTrue(str.equals("0, boot_menu"));
    }

    @Test
    public void testGetBootItemOption() throws IOException {
        DhcpSettings settings = new DhcpSettingsImpl(testPropsName);
        short[] arr = settings.getVendorOptionBootItem();

        assertTrue(Arrays.equals(arr, new short[] { 1, 1 }));
    }

    @Test
    public void testGetBootItemOptionEmpty() throws IOException {
        DhcpSettings settings = new DhcpSettingsImpl(emptyPropsName);
        short[] arr = settings.getVendorOptionBootItem();

        assertTrue(Arrays.equals(arr, new short[] { 0, 0 }));
    }
}
