package org.kj.pxe.protocol;

import java.io.DataOutputStream;
import java.io.IOException;

public interface VendorSuboption {
    
    short getTag();
    
    short getLen();
    
    void writeToStream(DataOutputStream outStream) throws IOException;
}
