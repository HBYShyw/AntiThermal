package com.android.server.am;

import android.util.SparseArray;
import com.android.server.am.ActivityManagerService;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public final class PendingTempAllowlists {
    private final SparseArray<ActivityManagerService.PendingTempAllowlist> mPendingTempAllowlist = new SparseArray<>();
    private ActivityManagerService mService;

    /* JADX INFO: Access modifiers changed from: package-private */
    public PendingTempAllowlists(ActivityManagerService activityManagerService) {
        this.mService = activityManagerService;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void put(int i, ActivityManagerService.PendingTempAllowlist pendingTempAllowlist) {
        synchronized (this.mPendingTempAllowlist) {
            this.mPendingTempAllowlist.put(i, pendingTempAllowlist);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void removeAt(int i) {
        synchronized (this.mPendingTempAllowlist) {
            this.mPendingTempAllowlist.removeAt(i);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ActivityManagerService.PendingTempAllowlist get(int i) {
        ActivityManagerService.PendingTempAllowlist pendingTempAllowlist;
        synchronized (this.mPendingTempAllowlist) {
            pendingTempAllowlist = this.mPendingTempAllowlist.get(i);
        }
        return pendingTempAllowlist;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int size() {
        int size;
        synchronized (this.mPendingTempAllowlist) {
            size = this.mPendingTempAllowlist.size();
        }
        return size;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ActivityManagerService.PendingTempAllowlist valueAt(int i) {
        ActivityManagerService.PendingTempAllowlist valueAt;
        synchronized (this.mPendingTempAllowlist) {
            valueAt = this.mPendingTempAllowlist.valueAt(i);
        }
        return valueAt;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int indexOfKey(int i) {
        int indexOfKey;
        synchronized (this.mPendingTempAllowlist) {
            indexOfKey = this.mPendingTempAllowlist.indexOfKey(i);
        }
        return indexOfKey;
    }
}
