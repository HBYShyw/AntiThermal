package com.android.server.apphibernation;

import android.util.AtomicFile;
import android.util.Slog;
import android.util.proto.ProtoInputStream;
import android.util.proto.ProtoOutputStream;
import com.android.internal.annotations.VisibleForTesting;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class HibernationStateDiskStore<T> {
    private static final long DISK_WRITE_DELAY = 60000;
    private static final String STATES_FILE_NAME = "states";
    private static final String TAG = "HibernationStateDiskStore";
    private final ScheduledExecutorService mExecutorService;
    private ScheduledFuture<?> mFuture;
    private final File mHibernationFile;
    private final ProtoReadWriter<List<T>> mProtoReadWriter;
    private List<T> mScheduledStatesToWrite;

    /* JADX INFO: Access modifiers changed from: package-private */
    public HibernationStateDiskStore(File file, ProtoReadWriter<List<T>> protoReadWriter, ScheduledExecutorService scheduledExecutorService) {
        this(file, protoReadWriter, scheduledExecutorService, STATES_FILE_NAME);
    }

    @VisibleForTesting
    HibernationStateDiskStore(File file, ProtoReadWriter<List<T>> protoReadWriter, ScheduledExecutorService scheduledExecutorService, String str) {
        this.mScheduledStatesToWrite = new ArrayList();
        this.mHibernationFile = new File(file, str);
        this.mExecutorService = scheduledExecutorService;
        this.mProtoReadWriter = protoReadWriter;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void scheduleWriteHibernationStates(List<T> list) {
        synchronized (this) {
            this.mScheduledStatesToWrite = list;
            if (this.mExecutorService.isShutdown()) {
                Slog.e(TAG, "Scheduled executor service is shut down.");
            } else if (this.mFuture != null) {
                Slog.i(TAG, "Write already scheduled. Skipping schedule.");
            } else {
                this.mFuture = this.mExecutorService.schedule(new Runnable() { // from class: com.android.server.apphibernation.HibernationStateDiskStore$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        HibernationStateDiskStore.this.writeHibernationStates();
                    }
                }, 60000L, TimeUnit.MILLISECONDS);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public List<T> readHibernationStates() {
        synchronized (this) {
            if (!this.mHibernationFile.exists()) {
                Slog.i(TAG, "No hibernation file on disk for file " + this.mHibernationFile.getPath());
                return null;
            }
            try {
                return this.mProtoReadWriter.readFromProto(new ProtoInputStream(new AtomicFile(this.mHibernationFile).openRead()));
            } catch (IOException e) {
                Slog.e(TAG, "Failed to read states protobuf.", e);
                return null;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void writeHibernationStates() {
        synchronized (this) {
            writeStateProto(this.mScheduledStatesToWrite);
            this.mScheduledStatesToWrite.clear();
            this.mFuture = null;
        }
    }

    private void writeStateProto(List<T> list) {
        AtomicFile atomicFile = new AtomicFile(this.mHibernationFile);
        try {
            FileOutputStream startWrite = atomicFile.startWrite();
            try {
                ProtoOutputStream protoOutputStream = new ProtoOutputStream(startWrite);
                this.mProtoReadWriter.writeToProto(protoOutputStream, list);
                protoOutputStream.flush();
                atomicFile.finishWrite(startWrite);
            } catch (Exception e) {
                Slog.e(TAG, "Failed to finish write to states protobuf.", e);
                atomicFile.failWrite(startWrite);
            }
        } catch (IOException e2) {
            Slog.e(TAG, "Failed to start write to states protobuf.", e2);
        }
    }
}
