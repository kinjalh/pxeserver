package org.kj.pxe.protocol.impl;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.kj.pxe.protocol.VendorSuboption;
import org.kj.pxe.protocol.impl.PxeDiscoveryControlSuboption;
import org.kj.pxe.protocol.impl.VendorOptions;

public class VendorOptionsTest {
    
    @Test
    public void testEmptyVendorOption() {
        VendorOptions ops = new VendorOptions();
        
        assertTrue(ops.getLen() == 0);
    }
    
    @Test
    public void testVendorLengthSerializingInvalid() throws IOException {
        VendorOptions ops = new VendorOptions();
        PxeDiscoveryControlSuboption discoveryControl = new PxeDiscoveryControlSuboption((byte) 2);
        List<VendorSuboption> suboptions = new ArrayList<VendorSuboption>();
        suboptions.add(discoveryControl);
        ops.setSuboptions(suboptions);

        assertTrue(ops.getLen() != 1);
    }
    
    @Test
    public void testVendorLengthSerializingValid() throws IOException {
        VendorOptions ops = new VendorOptions();
        PxeDiscoveryControlSuboption discoveryControl = new PxeDiscoveryControlSuboption((byte) 2);
        List<VendorSuboption> suboptions = new ArrayList<VendorSuboption>();
        suboptions.add(discoveryControl);
        ops.setSuboptions(suboptions);

        assertTrue(ops.getLen() == 3);
    }
    
    @Test
    public void testWriteToStreamFail() throws Exception {
        VendorOptions ops = new VendorOptions();
        PxeDiscoveryControlSuboption discoveryControl = new PxeDiscoveryControlSuboption((byte) 2);
        List<VendorSuboption> suboptions = new ArrayList<VendorSuboption>();
        suboptions.add(discoveryControl);
        ops.setSuboptions(suboptions);
        
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(byteStream);
        
        ops.writeToStream(dos);
        byte[] expected = {43, 2, 6, 1, 2};      
        byte[] actual = byteStream.toByteArray();

        assertTrue(!Arrays.equals(actual, expected));
    }
    
    @Test
    public void testWriteToStreamWorks() throws Exception {
        VendorOptions ops = new VendorOptions();
        
        PxeDiscoveryControlSuboption discoveryControl = new PxeDiscoveryControlSuboption((byte) 2);
        List<VendorSuboption> suboptions = new ArrayList<VendorSuboption>();
        suboptions.add(discoveryControl);
        
        PxeEndSuboption end = new PxeEndSuboption();
        suboptions.add(end);
        
        ops.setSuboptions(suboptions);
        
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(byteStream);
        
        ops.writeToStream(dos);
        byte[] expected = {43, 4, 6, 1, 2, -1};      
        byte[] actual = byteStream.toByteArray();
        System.out.println(Arrays.toString(actual));
        assertTrue(Arrays.equals(actual, expected));
    }
}
