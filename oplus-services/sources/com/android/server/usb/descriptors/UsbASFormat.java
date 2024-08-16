package com.android.server.usb.descriptors;

import com.android.server.usb.descriptors.report.ReportCanvas;
import com.android.server.usb.descriptors.report.UsbStrings;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class UsbASFormat extends UsbACInterface {
    public static final byte EXT_FORMAT_TYPE_I = -127;
    public static final byte EXT_FORMAT_TYPE_II = -126;
    public static final byte EXT_FORMAT_TYPE_III = -125;
    public static final byte FORMAT_TYPE_I = 1;
    public static final byte FORMAT_TYPE_II = 2;
    public static final byte FORMAT_TYPE_III = 3;
    public static final byte FORMAT_TYPE_IV = 4;
    private static final String TAG = "UsbASFormat";
    private final byte mFormatType;

    public int[] getBitDepths() {
        return null;
    }

    public int[] getChannelCounts() {
        return null;
    }

    public int[] getSampleRates() {
        return null;
    }

    public UsbASFormat(int i, byte b, byte b2, byte b3, int i2) {
        super(i, b, b2, i2);
        this.mFormatType = b3;
    }

    public byte getFormatType() {
        return this.mFormatType;
    }

    public static UsbDescriptor allocDescriptor(UsbDescriptorParser usbDescriptorParser, ByteStream byteStream, int i, byte b, byte b2, int i2) {
        byte b3 = byteStream.getByte();
        int aCInterfaceSpec = usbDescriptorParser.getACInterfaceSpec();
        if (b3 == 1) {
            if (aCInterfaceSpec == 512) {
                return new Usb20ASFormatI(i, b, b2, b3, i2);
            }
            return new Usb10ASFormatI(i, b, b2, b3, i2);
        }
        if (b3 != 2) {
            if (b3 == 3) {
                return new Usb20ASFormatIII(i, b, b2, b3, i2);
            }
            return new UsbASFormat(i, b, b2, b3, i2);
        }
        if (aCInterfaceSpec == 512) {
            return new Usb20ASFormatII(i, b, b2, b3, i2);
        }
        return new Usb10ASFormatII(i, b, b2, b3, i2);
    }

    @Override // com.android.server.usb.descriptors.UsbACInterface, com.android.server.usb.descriptors.UsbDescriptor, com.android.server.usb.descriptors.report.Reporting
    public void report(ReportCanvas reportCanvas) {
        super.report(reportCanvas);
        reportCanvas.writeParagraph(UsbStrings.getFormatName(getFormatType()), false);
    }
}
