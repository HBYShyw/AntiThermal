package com.android.server.usb.descriptors;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class UsbACAudioControlEndpoint extends UsbACEndpoint {
    static final byte ADDRESSMASK_DIRECTION = Byte.MIN_VALUE;
    static final byte ADDRESSMASK_ENDPOINT = 15;
    static final byte ATTRIBMASK_TRANS = 3;
    static final byte ATTRIBSMASK_SYNC = 12;
    private static final String TAG = "UsbACAudioControlEndpoint";
    private byte mAddress;
    private byte mAttribs;
    private byte mInterval;
    private int mMaxPacketSize;

    @Override // com.android.server.usb.descriptors.UsbACEndpoint
    public /* bridge */ /* synthetic */ int getSubclass() {
        return super.getSubclass();
    }

    @Override // com.android.server.usb.descriptors.UsbACEndpoint
    public /* bridge */ /* synthetic */ byte getSubtype() {
        return super.getSubtype();
    }

    public UsbACAudioControlEndpoint(int i, byte b, int i2, byte b2) {
        super(i, b, i2, b2);
    }

    public byte getAddress() {
        return this.mAddress;
    }

    public byte getAttribs() {
        return this.mAttribs;
    }

    public int getMaxPacketSize() {
        return this.mMaxPacketSize;
    }

    public byte getInterval() {
        return this.mInterval;
    }

    @Override // com.android.server.usb.descriptors.UsbACEndpoint, com.android.server.usb.descriptors.UsbDescriptor
    public int parseRawDescriptors(ByteStream byteStream) {
        super.parseRawDescriptors(byteStream);
        this.mAddress = byteStream.getByte();
        this.mAttribs = byteStream.getByte();
        this.mMaxPacketSize = byteStream.unpackUsbShort();
        this.mInterval = byteStream.getByte();
        return this.mLength;
    }
}
