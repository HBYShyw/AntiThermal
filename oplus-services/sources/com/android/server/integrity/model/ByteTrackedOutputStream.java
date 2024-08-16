package com.android.server.integrity.model;

import java.io.IOException;
import java.io.OutputStream;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class ByteTrackedOutputStream extends OutputStream {
    private final OutputStream mOutputStream;
    private int mWrittenBytesCount = 0;

    public ByteTrackedOutputStream(OutputStream outputStream) {
        this.mOutputStream = outputStream;
    }

    @Override // java.io.OutputStream
    public void write(int i) throws IOException {
        this.mWrittenBytesCount++;
        this.mOutputStream.write(i);
    }

    @Override // java.io.OutputStream
    public void write(byte[] bArr) throws IOException {
        write(bArr, 0, bArr.length);
    }

    @Override // java.io.OutputStream
    public void write(byte[] bArr, int i, int i2) throws IOException {
        this.mWrittenBytesCount += i2;
        this.mOutputStream.write(bArr, i, i2);
    }

    public int getWrittenBytesCount() {
        return this.mWrittenBytesCount;
    }
}
