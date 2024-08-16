package com.android.server.wm;

import android.util.SparseIntArray;
import java.io.PrintWriter;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
class MirrorActiveUids {
    private final SparseIntArray mUidStates = new SparseIntArray();
    private final SparseIntArray mNumNonAppVisibleWindowMap = new SparseIntArray();

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void onUidActive(int i, int i2) {
        this.mUidStates.put(i, i2);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void onUidInactive(int i) {
        this.mUidStates.delete(i);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void onUidProcStateChanged(int i, int i2) {
        int indexOfKey = this.mUidStates.indexOfKey(i);
        if (indexOfKey >= 0) {
            this.mUidStates.setValueAt(indexOfKey, i2);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized int getUidState(int i) {
        return this.mUidStates.get(i, 20);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void onNonAppSurfaceVisibilityChanged(int i, boolean z) {
        int indexOfKey = this.mNumNonAppVisibleWindowMap.indexOfKey(i);
        int i2 = 1;
        if (indexOfKey >= 0) {
            int valueAt = this.mNumNonAppVisibleWindowMap.valueAt(indexOfKey);
            if (!z) {
                i2 = -1;
            }
            int i3 = valueAt + i2;
            if (i3 > 0) {
                this.mNumNonAppVisibleWindowMap.setValueAt(indexOfKey, i3);
            } else {
                this.mNumNonAppVisibleWindowMap.removeAt(indexOfKey);
            }
        } else if (z) {
            this.mNumNonAppVisibleWindowMap.append(i, 1);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized boolean hasNonAppVisibleWindow(int i) {
        return this.mNumNonAppVisibleWindowMap.get(i) > 0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void dump(PrintWriter printWriter, String str) {
        printWriter.print(str + "NumNonAppVisibleWindowUidMap:[");
        for (int size = this.mNumNonAppVisibleWindowMap.size() + (-1); size >= 0; size += -1) {
            printWriter.print(" " + this.mNumNonAppVisibleWindowMap.keyAt(size) + ":" + this.mNumNonAppVisibleWindowMap.valueAt(size));
        }
        printWriter.println("]");
    }
}
