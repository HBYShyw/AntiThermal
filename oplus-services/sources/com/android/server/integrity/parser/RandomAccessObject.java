package com.android.server.integrity.parser;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public abstract class RandomAccessObject {
    public abstract void close() throws IOException;

    public abstract int length();

    public abstract int read() throws IOException;

    public abstract int read(byte[] bArr, int i, int i2) throws IOException;

    public abstract void seek(int i) throws IOException;

    public static RandomAccessObject ofFile(File file) throws IOException {
        return new RandomAccessFileObject(file);
    }

    public static RandomAccessObject ofBytes(byte[] bArr) {
        return new RandomAccessByteArrayObject(bArr);
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    private static class RandomAccessFileObject extends RandomAccessObject {
        private final int mLength;
        private final RandomAccessFile mRandomAccessFile;

        RandomAccessFileObject(File file) throws IOException {
            long length = file.length();
            if (length > 2147483647L) {
                throw new IOException("Unsupported file size (too big) " + length);
            }
            this.mRandomAccessFile = new RandomAccessFile(file, "r");
            this.mLength = (int) length;
        }

        @Override // com.android.server.integrity.parser.RandomAccessObject
        public void seek(int i) throws IOException {
            this.mRandomAccessFile.seek(i);
        }

        @Override // com.android.server.integrity.parser.RandomAccessObject
        public int read() throws IOException {
            return this.mRandomAccessFile.read();
        }

        @Override // com.android.server.integrity.parser.RandomAccessObject
        public int read(byte[] bArr, int i, int i2) throws IOException {
            return this.mRandomAccessFile.read(bArr, i, i2);
        }

        @Override // com.android.server.integrity.parser.RandomAccessObject
        public void close() throws IOException {
            this.mRandomAccessFile.close();
        }

        @Override // com.android.server.integrity.parser.RandomAccessObject
        public int length() {
            return this.mLength;
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    private static class RandomAccessByteArrayObject extends RandomAccessObject {
        private final ByteBuffer mBytes;

        @Override // com.android.server.integrity.parser.RandomAccessObject
        public void close() throws IOException {
        }

        RandomAccessByteArrayObject(byte[] bArr) {
            this.mBytes = ByteBuffer.wrap(bArr);
        }

        @Override // com.android.server.integrity.parser.RandomAccessObject
        public void seek(int i) throws IOException {
            this.mBytes.position(i);
        }

        @Override // com.android.server.integrity.parser.RandomAccessObject
        public int read() throws IOException {
            if (this.mBytes.hasRemaining()) {
                return this.mBytes.get() & 255;
            }
            return -1;
        }

        @Override // com.android.server.integrity.parser.RandomAccessObject
        public int read(byte[] bArr, int i, int i2) throws IOException {
            int min = Math.min(i2, this.mBytes.remaining());
            if (min <= 0) {
                return 0;
            }
            this.mBytes.get(bArr, i, i2);
            return min;
        }

        @Override // com.android.server.integrity.parser.RandomAccessObject
        public int length() {
            return this.mBytes.capacity();
        }
    }
}
