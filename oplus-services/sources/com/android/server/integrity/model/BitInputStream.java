package com.android.server.integrity.model;

import java.io.IOException;
import java.io.InputStream;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class BitInputStream {
    private long mBitsRead;
    private byte mCurrentByte;
    private InputStream mInputStream;

    public BitInputStream(InputStream inputStream) {
        this.mInputStream = inputStream;
    }

    public int getNext(int i) throws IOException {
        int i2 = 0;
        int i3 = 0;
        while (true) {
            int i4 = i2 + 1;
            if (i2 >= i) {
                return i3;
            }
            if (this.mBitsRead % 8 == 0) {
                this.mCurrentByte = getNextByte();
            }
            long j = this.mBitsRead;
            i3 = (i3 << 1) | ((this.mCurrentByte >>> (7 - ((int) (j % 8)))) & 1);
            this.mBitsRead = j + 1;
            i2 = i4;
        }
    }

    public boolean hasNext() throws IOException {
        return this.mInputStream.available() > 0;
    }

    private byte getNextByte() throws IOException {
        return (byte) this.mInputStream.read();
    }
}
