package com.android.server.integrity.parser;

import java.io.IOException;
import java.io.InputStream;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class RandomAccessInputStream extends InputStream {
    private int mPosition = 0;
    private final RandomAccessObject mRandomAccessObject;

    public RandomAccessInputStream(RandomAccessObject randomAccessObject) throws IOException {
        this.mRandomAccessObject = randomAccessObject;
    }

    public int getPosition() {
        return this.mPosition;
    }

    public void seek(int i) throws IOException {
        this.mRandomAccessObject.seek(i);
        this.mPosition = i;
    }

    @Override // java.io.InputStream
    public int available() throws IOException {
        return this.mRandomAccessObject.length() - this.mPosition;
    }

    @Override // java.io.InputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        this.mRandomAccessObject.close();
    }

    @Override // java.io.InputStream
    public int read() throws IOException {
        if (available() <= 0) {
            return -1;
        }
        this.mPosition++;
        return this.mRandomAccessObject.read();
    }

    @Override // java.io.InputStream
    public int read(byte[] bArr) throws IOException {
        return read(bArr, 0, bArr.length);
    }

    @Override // java.io.InputStream
    public int read(byte[] bArr, int i, int i2) throws IOException {
        if (i2 <= 0) {
            return 0;
        }
        int available = available();
        if (available <= 0) {
            return -1;
        }
        int read = this.mRandomAccessObject.read(bArr, i, Math.min(i2, available));
        this.mPosition += read;
        return read;
    }

    @Override // java.io.InputStream
    public long skip(long j) throws IOException {
        int available;
        if (j <= 0 || (available = available()) <= 0) {
            return 0L;
        }
        int min = (int) Math.min(available, j);
        int i = this.mPosition + min;
        this.mPosition = i;
        this.mRandomAccessObject.seek(i);
        return min;
    }
}
