package com.android.server.usb.descriptors;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public final class Usb20ACMixerUnit extends UsbACMixerUnit implements UsbAudioChannelCluster {
    private static final String TAG = "Usb20ACMixerUnit";
    private int mChanConfig;
    private byte mChanNames;
    private byte[] mControls;
    private byte mControlsMask;
    private byte mNameID;

    public Usb20ACMixerUnit(int i, byte b, byte b2, int i2) {
        super(i, b, b2, i2);
    }

    @Override // com.android.server.usb.descriptors.UsbAudioChannelCluster
    public byte getChannelCount() {
        return this.mNumOutputs;
    }

    @Override // com.android.server.usb.descriptors.UsbAudioChannelCluster
    public int getChannelConfig() {
        return this.mChanConfig;
    }

    @Override // com.android.server.usb.descriptors.UsbAudioChannelCluster
    public byte getChannelNames() {
        return this.mChanNames;
    }

    @Override // com.android.server.usb.descriptors.UsbACMixerUnit, com.android.server.usb.descriptors.UsbDescriptor
    public int parseRawDescriptors(ByteStream byteStream) {
        super.parseRawDescriptors(byteStream);
        this.mChanConfig = byteStream.unpackUsbInt();
        this.mChanNames = byteStream.getByte();
        int calcControlArraySize = UsbACMixerUnit.calcControlArraySize(this.mNumInputs, this.mNumOutputs);
        this.mControls = new byte[calcControlArraySize];
        for (int i = 0; i < calcControlArraySize; i++) {
            this.mControls[i] = byteStream.getByte();
        }
        this.mControlsMask = byteStream.getByte();
        this.mNameID = byteStream.getByte();
        return this.mLength;
    }
}
