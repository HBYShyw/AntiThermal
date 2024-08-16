package com.android.server.am;

import android.content.ContentResolver;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Trace;
import android.util.Slog;
import android.util.proto.ProtoOutputStream;
import com.android.internal.annotations.GuardedBy;
import com.android.server.DropBoxManagerInternal;
import com.android.server.LocalServices;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Objects;
import java.util.Set;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public abstract class BroadcastQueue {
    public static final String TAG = "BroadcastQueue";
    public static final String TAG_DUMP = "broadcast_queue_dump";
    final Handler mHandler;
    final BroadcastHistory mHistory;
    final String mQueueName;
    final ActivityManagerService mService;
    final BroadcastSkipPolicy mSkipPolicy;

    @GuardedBy({"mService"})
    public abstract void backgroundServicesFinishedLocked(int i);

    @GuardedBy({"mService"})
    public abstract boolean cleanupDisabledPackageReceiversLocked(String str, Set<String> set, int i);

    @GuardedBy({"mService"})
    public abstract String describeStateLocked();

    @GuardedBy({"mService"})
    public abstract void dumpDebug(ProtoOutputStream protoOutputStream, long j);

    @GuardedBy({"mService"})
    public abstract boolean dumpLocked(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr, int i, boolean z, boolean z2, boolean z3, String str, boolean z4);

    @GuardedBy({"mService"})
    public abstract void enqueueBroadcastLocked(BroadcastRecord broadcastRecord);

    @GuardedBy({"mService"})
    public abstract boolean finishReceiverLocked(ProcessRecord processRecord, int i, String str, Bundle bundle, boolean z, boolean z2);

    public void forceDelayBroadcastDelivery(String str, long j) {
    }

    @GuardedBy({"mService"})
    public abstract int getPreferredSchedulingGroupLocked(ProcessRecord processRecord);

    @GuardedBy({"mService"})
    public abstract boolean isBeyondBarrierLocked(long j);

    public abstract boolean isDelayBehindServices();

    @GuardedBy({"mService"})
    public abstract boolean isDispatchedLocked(Intent intent);

    @GuardedBy({"mService"})
    public abstract boolean isIdleLocked();

    @GuardedBy({"mService"})
    public abstract boolean onApplicationAttachedLocked(ProcessRecord processRecord) throws BroadcastDeliveryFailedException;

    @GuardedBy({"mService"})
    public abstract void onApplicationCleanupLocked(ProcessRecord processRecord);

    @GuardedBy({"mService"})
    public abstract void onApplicationProblemLocked(ProcessRecord processRecord);

    @GuardedBy({"mService"})
    public abstract void onApplicationTimeoutLocked(ProcessRecord processRecord);

    @GuardedBy({"mService"})
    public abstract void onProcessFreezableChangedLocked(ProcessRecord processRecord);

    public abstract void start(ContentResolver contentResolver);

    public abstract void waitForBarrier(PrintWriter printWriter);

    public abstract void waitForDispatched(Intent intent, PrintWriter printWriter);

    public abstract void waitForIdle(PrintWriter printWriter);

    /* JADX INFO: Access modifiers changed from: package-private */
    public BroadcastQueue(ActivityManagerService activityManagerService, Handler handler, String str, BroadcastSkipPolicy broadcastSkipPolicy, BroadcastHistory broadcastHistory) {
        Objects.requireNonNull(activityManagerService);
        this.mService = activityManagerService;
        Objects.requireNonNull(handler);
        this.mHandler = handler;
        Objects.requireNonNull(str);
        this.mQueueName = str;
        Objects.requireNonNull(broadcastSkipPolicy);
        this.mSkipPolicy = broadcastSkipPolicy;
        Objects.requireNonNull(broadcastHistory);
        this.mHistory = broadcastHistory;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void logw(String str) {
        Slog.w(TAG, str);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void logv(String str) {
        Slog.v(TAG, str);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void checkState(boolean z, String str) {
        if (!z) {
            throw new IllegalStateException(str);
        }
    }

    static void checkStateWtf(boolean z, String str) {
        if (z) {
            return;
        }
        Slog.wtf(TAG, new IllegalStateException(str));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int traceBegin(String str) {
        int hashCode = str.hashCode();
        Trace.asyncTraceForTrackBegin(64L, TAG, str, hashCode);
        return hashCode;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void traceEnd(int i) {
        Trace.asyncTraceForTrackEnd(64L, TAG, i);
    }

    public String toString() {
        return this.mQueueName;
    }

    public void dumpToDropBoxLocked(final String str) {
        ((DropBoxManagerInternal) LocalServices.getService(DropBoxManagerInternal.class)).addEntry(TAG_DUMP, new DropBoxManagerInternal.EntrySource() { // from class: com.android.server.am.BroadcastQueue$$ExternalSyntheticLambda0
            @Override // com.android.server.DropBoxManagerInternal.EntrySource
            public final void writeTo(FileDescriptor fileDescriptor) {
                BroadcastQueue.this.lambda$dumpToDropBoxLocked$0(str, fileDescriptor);
            }
        }, 2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$dumpToDropBoxLocked$0(String str, FileDescriptor fileDescriptor) throws IOException {
        FileOutputStream fileOutputStream = new FileOutputStream(fileDescriptor);
        try {
            PrintWriter printWriter = new PrintWriter(fileOutputStream);
            try {
                printWriter.print("Message: ");
                printWriter.println(str);
                dumpLocked(fileDescriptor, printWriter, null, 0, false, false, false, null, false);
                printWriter.flush();
                printWriter.close();
                fileOutputStream.close();
            } finally {
            }
        } catch (Throwable th) {
            try {
                fileOutputStream.close();
            } catch (Throwable th2) {
                th.addSuppressed(th2);
            }
            throw th;
        }
    }
}
