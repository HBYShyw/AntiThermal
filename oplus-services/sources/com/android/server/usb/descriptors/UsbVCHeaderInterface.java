package com.android.server.usb.descriptors;

import com.android.server.usb.descriptors.report.ReportCanvas;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public abstract class UsbVCHeaderInterface extends UsbVCInterface {
    private static final String TAG = "UsbVCHeaderInterface";
    protected int mTotalLength;
    protected int mVDCRelease;

    public UsbVCHeaderInterface(int i, byte b, byte b2, int i2) {
        super(i, b, b2);
        this.mVDCRelease = i2;
    }

    public int getVDCRelease() {
        return this.mVDCRelease;
    }

    public int getTotalLength() {
        return this.mTotalLength;
    }

    @Override // com.android.server.usb.descriptors.UsbDescriptor, com.android.server.usb.descriptors.report.Reporting
    public void report(ReportCanvas reportCanvas) {
        super.report(reportCanvas);
        reportCanvas.openList();
        reportCanvas.writeListItem("Release: " + ReportCanvas.getBCDString(getVDCRelease()));
        reportCanvas.writeListItem("Total Length: " + getTotalLength());
        reportCanvas.closeList();
    }
}
