package com.android.server.usb.descriptors;

import android.util.Log;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public abstract class UsbVCEndpoint extends UsbDescriptor {
    private static final String TAG = "UsbVCEndpoint";
    public static final byte VCEP_ENDPOINT = 2;
    public static final byte VCEP_GENERAL = 1;
    public static final byte VCEP_INTERRUPT = 3;
    public static final byte VCEP_UNDEFINED = 0;

    UsbVCEndpoint(int i, byte b) {
        super(i, b);
    }

    public static UsbDescriptor allocDescriptor(UsbDescriptorParser usbDescriptorParser, int i, byte b, byte b2) {
        usbDescriptorParser.getCurInterface();
        if (b2 != 0 && b2 != 1 && b2 != 2 && b2 != 3) {
            Log.w(TAG, "Unknown Video Class Endpoint id:0x" + Integer.toHexString(b2));
        }
        return null;
    }
}
