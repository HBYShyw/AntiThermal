package com.android.server.people.data;

import android.util.ArrayMap;
import android.util.AtomicFile;
import android.util.Slog;
import android.util.proto.ProtoInputStream;
import android.util.proto.ProtoOutputStream;
import com.android.internal.annotations.GuardedBy;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public abstract class AbstractProtoDiskReadWriter<T> {
    private static final long DEFAULT_DISK_WRITE_DELAY = 120000;
    private static final long SHUTDOWN_DISK_WRITE_TIMEOUT = 5000;
    private static final String TAG = "AbstractProtoDiskReadWriter";
    private final File mRootDir;
    private final ScheduledExecutorService mScheduledExecutorService;

    @GuardedBy({"this"})
    private Map<String, T> mScheduledFileDataMap = new ArrayMap();

    @GuardedBy({"this"})
    private ScheduledFuture<?> mScheduledFuture;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public interface ProtoStreamReader<T> {
        T read(ProtoInputStream protoInputStream);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public interface ProtoStreamWriter<T> {
        void write(ProtoOutputStream protoOutputStream, T t);
    }

    abstract ProtoStreamReader<T> protoStreamReader();

    abstract ProtoStreamWriter<T> protoStreamWriter();

    /* JADX INFO: Access modifiers changed from: package-private */
    public AbstractProtoDiskReadWriter(File file, ScheduledExecutorService scheduledExecutorService) {
        this.mRootDir = file;
        this.mScheduledExecutorService = scheduledExecutorService;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void delete(String str) {
        synchronized (this) {
            this.mScheduledFileDataMap.remove(str);
        }
        File file = getFile(str);
        if (file.exists() && !file.delete()) {
            Slog.e(TAG, "Failed to delete file: " + file.getPath());
        }
    }

    void writeTo(String str, T t) {
        AtomicFile atomicFile = new AtomicFile(getFile(str));
        try {
            FileOutputStream startWrite = atomicFile.startWrite();
            try {
                ProtoOutputStream protoOutputStream = new ProtoOutputStream(startWrite);
                protoStreamWriter().write(protoOutputStream, t);
                protoOutputStream.flush();
                atomicFile.finishWrite(startWrite);
                atomicFile.failWrite(null);
            } catch (Throwable th) {
                atomicFile.failWrite(startWrite);
                throw th;
            }
        } catch (IOException e) {
            Slog.e(TAG, "Failed to write to protobuf file.", e);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public T read(final String str) {
        File[] listFiles = this.mRootDir.listFiles(new FileFilter() { // from class: com.android.server.people.data.AbstractProtoDiskReadWriter$$ExternalSyntheticLambda0
            @Override // java.io.FileFilter
            public final boolean accept(File file) {
                boolean lambda$read$0;
                lambda$read$0 = AbstractProtoDiskReadWriter.lambda$read$0(str, file);
                return lambda$read$0;
            }
        });
        if (listFiles == null || listFiles.length == 0) {
            return null;
        }
        if (listFiles.length > 1) {
            Slog.w(TAG, "Found multiple files with the same name: " + Arrays.toString(listFiles));
        }
        return parseFile(listFiles[0]);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$read$0(String str, File file) {
        return file.isFile() && file.getName().equals(str);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void scheduleSave(String str, T t) {
        this.mScheduledFileDataMap.put(str, t);
        if (this.mScheduledExecutorService.isShutdown()) {
            Slog.e(TAG, "Worker is shutdown, failed to schedule data saving.");
        } else {
            if (this.mScheduledFuture != null) {
                return;
            }
            this.mScheduledFuture = this.mScheduledExecutorService.schedule(new AbstractProtoDiskReadWriter$$ExternalSyntheticLambda1(this), 120000L, TimeUnit.MILLISECONDS);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void saveImmediately(String str, T t) {
        synchronized (this) {
            this.mScheduledFileDataMap.put(str, t);
        }
        triggerScheduledFlushEarly();
    }

    private void triggerScheduledFlushEarly() {
        synchronized (this) {
            if (!this.mScheduledFileDataMap.isEmpty() && !this.mScheduledExecutorService.isShutdown()) {
                ScheduledFuture<?> scheduledFuture = this.mScheduledFuture;
                if (scheduledFuture != null) {
                    scheduledFuture.cancel(true);
                }
                try {
                    this.mScheduledExecutorService.submit(new AbstractProtoDiskReadWriter$$ExternalSyntheticLambda1(this)).get(SHUTDOWN_DISK_WRITE_TIMEOUT, TimeUnit.MILLISECONDS);
                } catch (InterruptedException | ExecutionException | TimeoutException e) {
                    Slog.e(TAG, "Failed to save data immediately.", e);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void flushScheduledData() {
        if (this.mScheduledFileDataMap.isEmpty()) {
            this.mScheduledFuture = null;
            return;
        }
        for (String str : this.mScheduledFileDataMap.keySet()) {
            writeTo(str, this.mScheduledFileDataMap.get(str));
        }
        this.mScheduledFileDataMap.clear();
        this.mScheduledFuture = null;
    }

    private T parseFile(File file) {
        try {
            FileInputStream openRead = new AtomicFile(file).openRead();
            try {
                T read = protoStreamReader().read(new ProtoInputStream(openRead));
                if (openRead != null) {
                    openRead.close();
                }
                return read;
            } finally {
            }
        } catch (IOException e) {
            Slog.e(TAG, "Failed to parse protobuf file.", e);
            return null;
        }
    }

    private File getFile(String str) {
        return new File(this.mRootDir, str);
    }
}
