package com.android.server.tare;

import android.app.ActivityManager;
import android.app.IUidObserver;
import android.app.UidObserver;
import android.os.RemoteException;
import android.util.IndentingPrintWriter;
import android.util.Slog;
import android.util.SparseArrayMap;
import android.util.SparseIntArray;
import com.android.internal.annotations.GuardedBy;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class ProcessStateModifier extends Modifier {
    private static final int PROC_STATE_BUCKET_BFGS = 3;
    private static final int PROC_STATE_BUCKET_BG = 4;
    private static final int PROC_STATE_BUCKET_FGS = 2;
    private static final int PROC_STATE_BUCKET_NONE = 0;
    private static final int PROC_STATE_BUCKET_TOP = 1;
    private static final String TAG = "TARE-" + ProcessStateModifier.class.getSimpleName();
    private final InternalResourceService mIrs;
    private final Object mLock = new Object();
    private final SparseArrayMap<String, Integer> mPackageToUidCache = new SparseArrayMap<>();

    @GuardedBy({"mLock"})
    private final SparseIntArray mUidProcStateBucketCache = new SparseIntArray();
    private final IUidObserver mUidObserver = new UidObserver() { // from class: com.android.server.tare.ProcessStateModifier.1
        public void onUidStateChanged(int i, int i2, long j, int i3) {
            int procStateBucket = ProcessStateModifier.this.getProcStateBucket(i2);
            synchronized (ProcessStateModifier.this.mLock) {
                if (ProcessStateModifier.this.mUidProcStateBucketCache.get(i) != procStateBucket) {
                    ProcessStateModifier.this.mUidProcStateBucketCache.put(i, procStateBucket);
                }
                ProcessStateModifier.this.notifyStateChangedLocked(i);
            }
        }

        public void onUidGone(int i, boolean z) {
            synchronized (ProcessStateModifier.this.mLock) {
                if (ProcessStateModifier.this.mUidProcStateBucketCache.indexOfKey(i) < 0) {
                    Slog.e(ProcessStateModifier.TAG, "UID " + i + " marked gone but wasn't in cache.");
                    return;
                }
                ProcessStateModifier.this.mUidProcStateBucketCache.delete(i);
                ProcessStateModifier.this.notifyStateChangedLocked(i);
            }
        }
    };

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public @interface ProcStateBucket {
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getProcStateBucket(int i) {
        if (i <= 2) {
            return 1;
        }
        if (i <= 4) {
            return 2;
        }
        return i <= 5 ? 3 : 4;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ProcessStateModifier(InternalResourceService internalResourceService) {
        this.mIrs = internalResourceService;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.tare.Modifier
    @GuardedBy({"mLock"})
    public void setup() {
        try {
            ActivityManager.getService().registerUidObserver(this.mUidObserver, 3, -1, (String) null);
        } catch (RemoteException unused) {
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.tare.Modifier
    @GuardedBy({"mLock"})
    public void tearDown() {
        try {
            ActivityManager.getService().unregisterUidObserver(this.mUidObserver);
        } catch (RemoteException unused) {
        }
        this.mPackageToUidCache.clear();
        this.mUidProcStateBucketCache.clear();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public long getModifiedPrice(int i, String str, long j, long j2) {
        int i2;
        synchronized (this.mLock) {
            i2 = this.mUidProcStateBucketCache.get(this.mIrs.getUid(i, str), 0);
        }
        if (i2 == 1) {
            return 0L;
        }
        if (i2 != 2) {
            return (i2 == 3 && j2 > j) ? (long) (j + ((j2 - j) * 0.5d)) : j2;
        }
        return Math.min(j, j2);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.tare.Modifier
    @GuardedBy({"mLock"})
    public void dump(IndentingPrintWriter indentingPrintWriter) {
        indentingPrintWriter.print("Proc state bucket cache = ");
        indentingPrintWriter.println(this.mUidProcStateBucketCache);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$notifyStateChangedLocked$0(int i) {
        this.mIrs.onUidStateChanged(i);
    }

    /* JADX INFO: Access modifiers changed from: private */
    @GuardedBy({"mLock"})
    public void notifyStateChangedLocked(final int i) {
        TareHandlerThread.getHandler().post(new Runnable() { // from class: com.android.server.tare.ProcessStateModifier$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                ProcessStateModifier.this.lambda$notifyStateChangedLocked$0(i);
            }
        });
    }
}
