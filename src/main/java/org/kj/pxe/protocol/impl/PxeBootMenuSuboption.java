package org.kj.pxe.protocol.impl;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.List;

import org.kj.pxe.protocol.VendorSuboption;

/**
 * Represents the data in one Boot Menu suboption
 * @author Kinjal
 *
 */
class PxeBootMenuSuboption implements VendorSuboption {

    private List<BootMenuInformation> descriptions;

    /**
     * Constructs a PxeBootMenuSuboption with the specifed list of {@link BootMenuInformation}
     * @param descriptions the list of {@link BootMenuInformation}
     */
    PxeBootMenuSuboption(List<BootMenuInformation> descriptions) {
        this.descriptions = descriptions;
    }

    /**
     * Returns the length of the data in this suboption
     */
    @Override
    public short getLen() {
        short len = 0;
        for (BootMenuInformation info : descriptions) {
            len += 3 + info.getDescription().getBytes().length;
        }
        return len;
    }

    /**
     * 
     */
    @Override
    public void writeToStream(DataOutputStream stream) throws IOException {
        stream.writeByte(VendorSuboptionTag.PXE_BOOT_MENU.getValue());

        stream.writeByte(getLen());
        for (BootMenuInformation info : descriptions) {
            stream.writeShort(info.getType());
            stream.writeByte(info.getDescription().getBytes().length);
            stream.write(info.getDescription().getBytes());
        }
    }

    @Override
    public short getTag() {
        return (short) VendorSuboptionTag.PXE_BOOT_MENU.getValue();
    }

}
