package com.android.server.usb.descriptors;

import com.android.server.usb.descriptors.report.ReportCanvas;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public final class UsbVCOutputTerminal extends UsbVCInterface {
    private static final String TAG = "UsbVCOutputTerminal";

    public UsbVCOutputTerminal(int i, byte b, byte b2) {
        super(i, b, b2);
    }

    @Override // com.android.server.usb.descriptors.UsbDescriptor
    public int parseRawDescriptors(ByteStream byteStream) {
        return super.parseRawDescriptors(byteStream);
    }

    @Override // com.android.server.usb.descriptors.UsbDescriptor, com.android.server.usb.descriptors.report.Reporting
    public void report(ReportCanvas reportCanvas) {
        super.report(reportCanvas);
    }
}
