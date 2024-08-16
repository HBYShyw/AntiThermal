package com.android.server.usb.descriptors;

import android.util.Log;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public abstract class UsbVCInterface extends UsbDescriptor {
    private static final String TAG = "UsbVCInterface";
    public static final byte VCI_EXTENSION_UNIT = 6;
    public static final byte VCI_INPUT_TERMINAL = 2;
    public static final byte VCI_OUTPUT_TERMINAL = 3;
    public static final byte VCI_PROCESSING_UNIT = 5;
    public static final byte VCI_SELECTOR_UNIT = 4;
    public static final byte VCI_UNDEFINED = 0;
    public static final byte VCI_VEADER = 1;
    protected final byte mSubtype;

    public UsbVCInterface(int i, byte b, byte b2) {
        super(i, b);
        this.mSubtype = b2;
    }

    public static UsbDescriptor allocDescriptor(UsbDescriptorParser usbDescriptorParser, ByteStream byteStream, int i, byte b) {
        byte b2 = byteStream.getByte();
        usbDescriptorParser.getCurInterface();
        switch (b2) {
            case 0:
            case 6:
                return null;
            case 1:
                int unpackUsbShort = byteStream.unpackUsbShort();
                usbDescriptorParser.setVCInterfaceSpec(unpackUsbShort);
                return new UsbVCHeader(i, b, b2, unpackUsbShort);
            case 2:
                return new UsbVCInputTerminal(i, b, b2);
            case 3:
                return new UsbVCOutputTerminal(i, b, b2);
            case 4:
                return new UsbVCSelectorUnit(i, b, b2);
            case 5:
                return new UsbVCProcessingUnit(i, b, b2);
            default:
                Log.w(TAG, "Unknown Video Class Interface subtype: 0x" + Integer.toHexString(b2));
                return null;
        }
    }
}
