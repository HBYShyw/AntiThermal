package com.android.server.usb.descriptors;

import com.android.server.usb.descriptors.report.ReportCanvas;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public final class Usb10ACMixerUnit extends UsbACMixerUnit implements UsbAudioChannelCluster {
    private static final String TAG = "Usb10ACMixerUnit";
    private byte mChanNameID;
    private int mChannelConfig;
    private byte[] mControls;
    private byte mNameID;

    public Usb10ACMixerUnit(int i, byte b, byte b2, int i2) {
        super(i, b, b2, i2);
    }

    @Override // com.android.server.usb.descriptors.UsbAudioChannelCluster
    public byte getChannelCount() {
        return this.mNumOutputs;
    }

    @Override // com.android.server.usb.descriptors.UsbAudioChannelCluster
    public int getChannelConfig() {
        return this.mChannelConfig;
    }

    @Override // com.android.server.usb.descriptors.UsbAudioChannelCluster
    public byte getChannelNames() {
        return this.mChanNameID;
    }

    public byte[] getControls() {
        return this.mControls;
    }

    public byte getNameID() {
        return this.mNameID;
    }

    @Override // com.android.server.usb.descriptors.UsbACMixerUnit, com.android.server.usb.descriptors.UsbDescriptor
    public int parseRawDescriptors(ByteStream byteStream) {
        super.parseRawDescriptors(byteStream);
        this.mChannelConfig = byteStream.unpackUsbShort();
        this.mChanNameID = byteStream.getByte();
        int calcControlArraySize = UsbACMixerUnit.calcControlArraySize(this.mNumInputs, this.mNumOutputs);
        this.mControls = new byte[calcControlArraySize];
        for (int i = 0; i < calcControlArraySize; i++) {
            this.mControls[i] = byteStream.getByte();
        }
        this.mNameID = byteStream.getByte();
        return this.mLength;
    }

    @Override // com.android.server.usb.descriptors.UsbACInterface, com.android.server.usb.descriptors.UsbDescriptor, com.android.server.usb.descriptors.report.Reporting
    public void report(ReportCanvas reportCanvas) {
        super.report(reportCanvas);
        reportCanvas.writeParagraph("Mixer Unit", false);
        reportCanvas.openList();
        reportCanvas.writeListItem("Unit ID: " + ReportCanvas.getHexString(getUnitID()));
        byte numInputs = getNumInputs();
        byte[] inputIDs = getInputIDs();
        reportCanvas.openListItem();
        reportCanvas.write("Num Inputs: " + ((int) numInputs) + " [");
        for (int i = 0; i < numInputs; i++) {
            reportCanvas.write("" + ReportCanvas.getHexString(inputIDs[i]));
            if (i < numInputs - 1) {
                reportCanvas.write(" ");
            }
        }
        reportCanvas.write("]");
        reportCanvas.closeListItem();
        reportCanvas.writeListItem("Num Outputs: " + ((int) getNumOutputs()));
        reportCanvas.writeListItem("Channel Config: " + ReportCanvas.getHexString(getChannelConfig()));
        byte[] controls = getControls();
        reportCanvas.openListItem();
        reportCanvas.write("Controls: " + controls.length + " [");
        for (int i2 = 0; i2 < controls.length; i2++) {
            reportCanvas.write("" + ((int) controls[i2]));
            if (i2 < controls.length - 1) {
                reportCanvas.write(" ");
            }
        }
        reportCanvas.write("]");
        reportCanvas.closeListItem();
        reportCanvas.closeList();
    }
}
