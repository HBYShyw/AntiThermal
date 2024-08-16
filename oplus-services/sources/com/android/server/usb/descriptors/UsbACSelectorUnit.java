package com.android.server.usb.descriptors;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public final class UsbACSelectorUnit extends UsbACInterface {
    private static final String TAG = "UsbACSelectorUnit";
    private byte mNameIndex;
    private byte mNumPins;
    private byte[] mSourceIDs;
    private byte mUnitID;

    public UsbACSelectorUnit(int i, byte b, byte b2, int i2) {
        super(i, b, b2, i2);
    }

    public byte getUnitID() {
        return this.mUnitID;
    }

    public byte getNumPins() {
        return this.mNumPins;
    }

    public byte[] getSourceIDs() {
        return this.mSourceIDs;
    }

    public byte getNameIndex() {
        return this.mNameIndex;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.android.server.usb.descriptors.UsbDescriptor
    public int parseRawDescriptors(ByteStream byteStream) {
        this.mUnitID = byteStream.getByte();
        int i = byteStream.getByte();
        this.mNumPins = i;
        this.mSourceIDs = new byte[i];
        for (int i2 = 0; i2 < this.mNumPins; i2++) {
            this.mSourceIDs[i2] = byteStream.getByte();
        }
        this.mNameIndex = byteStream.getByte();
        return this.mLength;
    }
}
