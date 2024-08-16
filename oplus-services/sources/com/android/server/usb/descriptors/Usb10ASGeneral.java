package com.android.server.usb.descriptors;

import com.android.server.usb.descriptors.report.ReportCanvas;
import com.android.server.usb.descriptors.report.UsbStrings;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public final class Usb10ASGeneral extends UsbACInterface {
    private static final String TAG = "Usb10ASGeneral";
    private byte mDelay;
    private int mFormatTag;
    private byte mTerminalLink;

    public Usb10ASGeneral(int i, byte b, byte b2, int i2) {
        super(i, b, b2, i2);
    }

    public byte getTerminalLink() {
        return this.mTerminalLink;
    }

    public byte getDelay() {
        return this.mDelay;
    }

    public int getFormatTag() {
        return this.mFormatTag;
    }

    @Override // com.android.server.usb.descriptors.UsbDescriptor
    public int parseRawDescriptors(ByteStream byteStream) {
        this.mTerminalLink = byteStream.getByte();
        this.mDelay = byteStream.getByte();
        this.mFormatTag = byteStream.unpackUsbShort();
        return this.mLength;
    }

    @Override // com.android.server.usb.descriptors.UsbACInterface, com.android.server.usb.descriptors.UsbDescriptor, com.android.server.usb.descriptors.report.Reporting
    public void report(ReportCanvas reportCanvas) {
        super.report(reportCanvas);
        reportCanvas.openList();
        reportCanvas.writeListItem("Delay: " + ((int) this.mDelay));
        reportCanvas.writeListItem("Terminal Link: " + ((int) this.mTerminalLink));
        reportCanvas.writeListItem("Format: " + UsbStrings.getAudioFormatName(this.mFormatTag) + " - " + ReportCanvas.getHexString(this.mFormatTag));
        reportCanvas.closeList();
    }
}
