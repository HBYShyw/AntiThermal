package com.android.server.usb.descriptors;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public final class UsbInterfaceAssoc extends UsbDescriptor {
    private static final String TAG = "UsbInterfaceAssoc";
    private byte mFirstInterface;
    private byte mFunction;
    private byte mFunctionClass;
    private byte mFunctionProtocol;
    private byte mFunctionSubClass;
    private byte mInterfaceCount;

    public UsbInterfaceAssoc(int i, byte b) {
        super(i, b);
    }

    public byte getFirstInterface() {
        return this.mFirstInterface;
    }

    public byte getInterfaceCount() {
        return this.mInterfaceCount;
    }

    public byte getFunctionClass() {
        return this.mFunctionClass;
    }

    public byte getFunctionSubClass() {
        return this.mFunctionSubClass;
    }

    public byte getFunctionProtocol() {
        return this.mFunctionProtocol;
    }

    public byte getFunction() {
        return this.mFunction;
    }

    @Override // com.android.server.usb.descriptors.UsbDescriptor
    public int parseRawDescriptors(ByteStream byteStream) {
        this.mFirstInterface = byteStream.getByte();
        this.mInterfaceCount = byteStream.getByte();
        this.mFunctionClass = byteStream.getByte();
        this.mFunctionSubClass = byteStream.getByte();
        this.mFunctionProtocol = byteStream.getByte();
        this.mFunction = byteStream.getByte();
        return this.mLength;
    }
}
