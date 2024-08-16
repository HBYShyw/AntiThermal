package com.android.server.pm;

import android.os.Environment;
import android.os.SystemClock;
import android.util.AtomicFile;
import java.io.File;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public abstract class AbstractStatsBase<T> {
    private static final int WRITE_INTERVAL_MS;
    private final String mBackgroundThreadName;
    private final String mFileName;
    private final boolean mLock;
    private final Object mFileLock = new Object();
    private final AtomicLong mLastTimeWritten = new AtomicLong(0);
    private final AtomicBoolean mBackgroundWriteRunning = new AtomicBoolean(false);

    protected abstract void readInternal(T t);

    protected abstract void writeInternal(T t);

    static {
        WRITE_INTERVAL_MS = PackageManagerService.DEBUG_DEXOPT ? 0 : 1800000;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public AbstractStatsBase(String str, String str2, boolean z) {
        this.mFileName = str;
        this.mBackgroundThreadName = str2;
        this.mLock = z;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public AtomicFile getFile() {
        return new AtomicFile(new File(new File(Environment.getDataDirectory(), "system"), this.mFileName));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void writeNow(T t) {
        writeImpl(t);
        this.mLastTimeWritten.set(SystemClock.elapsedRealtime());
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean maybeWriteAsync(final T t) {
        if ((SystemClock.elapsedRealtime() - this.mLastTimeWritten.get() < WRITE_INTERVAL_MS && !PackageManagerService.DEBUG_DEXOPT) || !this.mBackgroundWriteRunning.compareAndSet(false, true)) {
            return false;
        }
        new Thread(this.mBackgroundThreadName) { // from class: com.android.server.pm.AbstractStatsBase.1
            @Override // java.lang.Thread, java.lang.Runnable
            public void run() {
                try {
                    AbstractStatsBase.this.writeImpl(t);
                    AbstractStatsBase.this.mLastTimeWritten.set(SystemClock.elapsedRealtime());
                } finally {
                    AbstractStatsBase.this.mBackgroundWriteRunning.set(false);
                }
            }
        }.start();
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void writeImpl(T t) {
        if (this.mLock) {
            synchronized (t) {
                synchronized (this.mFileLock) {
                    writeInternal(t);
                }
            }
            return;
        }
        synchronized (this.mFileLock) {
            writeInternal(t);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void read(T t) {
        if (this.mLock) {
            synchronized (t) {
                synchronized (this.mFileLock) {
                    readInternal(t);
                }
            }
        } else {
            synchronized (this.mFileLock) {
                readInternal(t);
            }
        }
        this.mLastTimeWritten.set(SystemClock.elapsedRealtime());
    }
}
