package com.android.server.powerstats;

import android.content.Context;
import android.util.Slog;
import com.android.internal.util.FileRotator;
import com.android.server.job.controllers.JobStatus;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.concurrent.locks.ReentrantLock;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class PowerStatsDataStorage {
    private static final long DELETE_AGE_MILLIS = 172800000;
    private static final long MILLISECONDS_PER_HOUR = 3600000;
    private static final long ROTATE_AGE_MILLIS = 14400000;
    private static final String TAG = "PowerStatsDataStorage";
    private final File mDataStorageDir;
    private final String mDataStorageFilename;
    private final FileRotator mFileRotator;
    private final ReentrantLock mLock = new ReentrantLock();

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public interface DataElementReadCallback {
        void onReadDataElement(byte[] bArr);
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    private static class DataElement {
        private static final int LENGTH_FIELD_WIDTH = 4;
        private static final int MAX_DATA_ELEMENT_SIZE = 32768;
        private byte[] mData;

        /* JADX INFO: Access modifiers changed from: private */
        public byte[] toByteArray() throws IOException {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            byteArrayOutputStream.write(ByteBuffer.allocate(4).putInt(this.mData.length).array());
            byteArrayOutputStream.write(this.mData);
            return byteArrayOutputStream.toByteArray();
        }

        protected byte[] getData() {
            return this.mData;
        }

        private DataElement(byte[] bArr) {
            this.mData = bArr;
        }

        private DataElement(InputStream inputStream) throws IOException {
            byte[] bArr = new byte[4];
            int read = inputStream.read(bArr);
            this.mData = new byte[0];
            if (read == 4) {
                int i = ByteBuffer.wrap(bArr).getInt();
                if (i > 0 && i < 32768) {
                    byte[] bArr2 = new byte[i];
                    this.mData = bArr2;
                    int read2 = inputStream.read(bArr2);
                    if (read2 == i) {
                        return;
                    }
                    throw new IOException("Invalid bytes read, expected: " + i + ", actual: " + read2);
                }
                throw new IOException("DataElement size is invalid: " + i);
            }
            throw new IOException("Did not read 4 bytes (" + read + ")");
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    private static class DataReader implements FileRotator.Reader {
        private DataElementReadCallback mCallback;

        DataReader(DataElementReadCallback dataElementReadCallback) {
            this.mCallback = dataElementReadCallback;
        }

        public void read(InputStream inputStream) throws IOException {
            while (inputStream.available() > 0) {
                this.mCallback.onReadDataElement(new DataElement(inputStream).getData());
            }
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    private static class DataRewriter implements FileRotator.Rewriter {
        byte[] mActiveFileData = new byte[0];
        byte[] mNewData;

        public void reset() {
        }

        public boolean shouldWrite() {
            return true;
        }

        DataRewriter(byte[] bArr) {
            this.mNewData = bArr;
        }

        public void read(InputStream inputStream) throws IOException {
            byte[] bArr = new byte[inputStream.available()];
            this.mActiveFileData = bArr;
            inputStream.read(bArr);
        }

        public void write(OutputStream outputStream) throws IOException {
            outputStream.write(this.mActiveFileData);
            outputStream.write(this.mNewData);
        }
    }

    public PowerStatsDataStorage(Context context, File file, String str) {
        this.mDataStorageDir = file;
        this.mDataStorageFilename = str;
        if (!file.exists() && !file.mkdirs()) {
            Slog.wtf(TAG, "mDataStorageDir does not exist: " + file.getPath());
            this.mFileRotator = null;
            return;
        }
        File[] listFiles = file.listFiles();
        for (int i = 0; i < listFiles.length; i++) {
            if (listFiles[i].getName().startsWith(this.mDataStorageFilename.substring(0, this.mDataStorageFilename.lastIndexOf(46))) && !listFiles[i].getName().startsWith(this.mDataStorageFilename)) {
                listFiles[i].delete();
            }
        }
        this.mFileRotator = new FileRotator(this.mDataStorageDir, this.mDataStorageFilename, 14400000L, DELETE_AGE_MILLIS);
    }

    public void write(byte[] bArr) {
        if (bArr == null || bArr.length <= 0) {
            return;
        }
        this.mLock.lock();
        try {
            try {
                long currentTimeMillis = System.currentTimeMillis();
                this.mFileRotator.rewriteActive(new DataRewriter(new DataElement(bArr).toByteArray()), currentTimeMillis);
                this.mFileRotator.maybeRotate(currentTimeMillis);
            } catch (IOException e) {
                Slog.e(TAG, "Failed to write to on-device storage: " + e);
            }
        } finally {
            this.mLock.unlock();
        }
    }

    public void read(DataElementReadCallback dataElementReadCallback) throws IOException {
        this.mLock.lock();
        try {
            this.mFileRotator.readMatching(new DataReader(dataElementReadCallback), Long.MIN_VALUE, JobStatus.NO_LATEST_RUNTIME);
        } finally {
            this.mLock.unlock();
        }
    }

    public void deleteLogs() {
        this.mLock.lock();
        try {
            File[] listFiles = this.mDataStorageDir.listFiles();
            for (int i = 0; i < listFiles.length; i++) {
                if (listFiles[i].getName().startsWith(this.mDataStorageFilename.substring(0, this.mDataStorageFilename.lastIndexOf(46)))) {
                    listFiles[i].delete();
                }
            }
        } finally {
            this.mLock.unlock();
        }
    }
}
