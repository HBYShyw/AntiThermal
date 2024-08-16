package com.android.server.usb.descriptors;

import android.util.Log;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public abstract class UsbACEndpoint extends UsbDescriptor {
    public static final byte MS_GENERAL = 1;
    public static final byte MS_GENERAL_2_0 = 2;
    private static final String TAG = "UsbACEndpoint";
    protected final int mSubclass;
    protected final byte mSubtype;

    /* JADX INFO: Access modifiers changed from: package-private */
    public UsbACEndpoint(int i, byte b, int i2, byte b2) {
        super(i, b);
        this.mSubclass = i2;
        this.mSubtype = b2;
    }

    public int getSubclass() {
        return this.mSubclass;
    }

    public byte getSubtype() {
        return this.mSubtype;
    }

    @Override // com.android.server.usb.descriptors.UsbDescriptor
    public int parseRawDescriptors(ByteStream byteStream) {
        return this.mLength;
    }

    public static UsbDescriptor allocDescriptor(UsbDescriptorParser usbDescriptorParser, int i, byte b, byte b2) {
        int usbSubclass = usbDescriptorParser.getCurInterface().getUsbSubclass();
        if (usbSubclass == 1) {
            return new UsbACAudioControlEndpoint(i, b, usbSubclass, b2);
        }
        if (usbSubclass == 2) {
            return new UsbACAudioStreamEndpoint(i, b, usbSubclass, b2);
        }
        if (usbSubclass != 3) {
            Log.w(TAG, "Unknown Audio Class Endpoint id:0x" + Integer.toHexString(usbSubclass));
            return null;
        }
        if (b2 == 1) {
            return new UsbACMidi10Endpoint(i, b, usbSubclass, b2);
        }
        if (b2 == 2) {
            return new UsbACMidi20Endpoint(i, b, usbSubclass, b2);
        }
        Log.w(TAG, "Unknown Midi Endpoint id:0x" + Integer.toHexString(b2));
        return null;
    }
}
