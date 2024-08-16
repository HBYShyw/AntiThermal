package com.android.server.am;

import android.os.SystemClock;
import android.util.Pair;
import android.util.Slog;
import android.util.SparseArray;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public final class PendingStartActivityUids {
    public static final long INVALID_TIME = 0;
    static final String TAG = "ActivityManager";
    private final SparseArray<Pair<Integer, Long>> mPendingUids = new SparseArray<>();

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized boolean add(int i, int i2) {
        if (this.mPendingUids.get(i) != null) {
            return false;
        }
        this.mPendingUids.put(i, new Pair<>(Integer.valueOf(i2), Long.valueOf(SystemClock.elapsedRealtime())));
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void delete(int i, long j) {
        Pair<Integer, Long> pair = this.mPendingUids.get(i);
        if (pair != null) {
            if (j < ((Long) pair.second).longValue()) {
                Slog.i("ActivityManager", "updateOomAdj start time is before than pendingPid added, don't delete it");
                return;
            }
            long elapsedRealtime = SystemClock.elapsedRealtime() - ((Long) pair.second).longValue();
            if (elapsedRealtime >= 1000) {
                Slog.i("ActivityManager", "PendingStartActivityUids startActivity to updateOomAdj delay:" + elapsedRealtime + "ms, uid:" + i);
            }
            this.mPendingUids.delete(i);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized long getPendingTopPidTime(int i, int i2) {
        Pair<Integer, Long> pair;
        pair = this.mPendingUids.get(i);
        return (pair == null || ((Integer) pair.first).intValue() != i2) ? 0L : ((Long) pair.second).longValue();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized boolean isPendingTopUid(int i) {
        return this.mPendingUids.get(i) != null;
    }
}
