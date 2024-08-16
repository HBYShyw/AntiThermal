package com.android.server.backup;

import android.util.Slog;
import com.android.internal.annotations.GuardedBy;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.HashSet;
import java.util.Set;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
final class ProcessedPackagesJournal {
    private static final boolean DEBUG = true;
    private static final String JOURNAL_FILE_NAME = "processed";
    private static final String TAG = "ProcessedPackagesJournal";

    @GuardedBy({"mProcessedPackages"})
    private final Set<String> mProcessedPackages = new HashSet();
    private final File mStateDirectory;

    /* JADX INFO: Access modifiers changed from: package-private */
    public ProcessedPackagesJournal(File file) {
        this.mStateDirectory = file;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void init() {
        synchronized (this.mProcessedPackages) {
            loadFromDisk();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean hasBeenProcessed(String str) {
        boolean contains;
        synchronized (this.mProcessedPackages) {
            contains = this.mProcessedPackages.contains(str);
        }
        return contains;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void addPackage(String str) {
        synchronized (this.mProcessedPackages) {
            if (this.mProcessedPackages.add(str)) {
                File file = new File(this.mStateDirectory, JOURNAL_FILE_NAME);
                try {
                    RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rws");
                    try {
                        randomAccessFile.seek(randomAccessFile.length());
                        randomAccessFile.writeUTF(str);
                        randomAccessFile.close();
                    } catch (Throwable th) {
                        try {
                            randomAccessFile.close();
                        } catch (Throwable th2) {
                            th.addSuppressed(th2);
                        }
                        throw th;
                    }
                } catch (IOException unused) {
                    Slog.e(TAG, "Can't log backup of " + str + " to " + file);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Set<String> getPackagesCopy() {
        HashSet hashSet;
        synchronized (this.mProcessedPackages) {
            hashSet = new HashSet(this.mProcessedPackages);
        }
        return hashSet;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void reset() {
        synchronized (this.mProcessedPackages) {
            this.mProcessedPackages.clear();
            new File(this.mStateDirectory, JOURNAL_FILE_NAME).delete();
        }
    }

    private void loadFromDisk() {
        File file = new File(this.mStateDirectory, JOURNAL_FILE_NAME);
        if (!file.exists()) {
            return;
        }
        try {
            DataInputStream dataInputStream = new DataInputStream(new BufferedInputStream(new FileInputStream(file)));
            while (true) {
                try {
                    String readUTF = dataInputStream.readUTF();
                    Slog.v(TAG, "   + " + readUTF);
                    this.mProcessedPackages.add(readUTF);
                } catch (Throwable th) {
                    try {
                        dataInputStream.close();
                    } catch (Throwable th2) {
                        th.addSuppressed(th2);
                    }
                    throw th;
                }
            }
        } catch (EOFException unused) {
        } catch (IOException e) {
            Slog.e(TAG, "Error reading processed packages journal", e);
        }
    }
}
