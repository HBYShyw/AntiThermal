package com.android.server.am;

import android.util.ArraySet;
import com.android.internal.annotations.GuardedBy;
import java.io.PrintWriter;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public final class ProcessReceiverRecord {
    final ProcessRecord mApp;
    private int mCurReceiversSize;
    private final ActivityManagerService mService;
    private final ArraySet<BroadcastRecord> mCurReceivers = new ArraySet<>();
    private final ArraySet<ReceiverList> mReceivers = new ArraySet<>();

    /* JADX INFO: Access modifiers changed from: package-private */
    public int numberOfCurReceivers() {
        return this.mCurReceiversSize;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void incrementCurReceivers() {
        this.mCurReceiversSize++;
        this.mApp.getWrapper().getExtImpl().updateExecutingComponent(this.mApp, "broadcast", 1);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void decrementCurReceivers() {
        this.mCurReceiversSize--;
        this.mApp.getWrapper().getExtImpl().updateExecutingComponent(this.mApp, "broadcast", 2);
    }

    @Deprecated
    BroadcastRecord getCurReceiverAt(int i) {
        return this.mCurReceivers.valueAt(i);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Deprecated
    public boolean hasCurReceiver(BroadcastRecord broadcastRecord) {
        return this.mCurReceivers.contains(broadcastRecord);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Deprecated
    public void addCurReceiver(BroadcastRecord broadcastRecord) {
        this.mCurReceivers.add(broadcastRecord);
        this.mCurReceiversSize = this.mCurReceivers.size();
        this.mApp.getWrapper().getExtImpl().updateExecutingComponent(this.mApp, "broadcast", 1);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Deprecated
    public void removeCurReceiver(BroadcastRecord broadcastRecord) {
        this.mCurReceivers.remove(broadcastRecord);
        this.mCurReceiversSize = this.mCurReceivers.size();
        this.mApp.getWrapper().getExtImpl().updateExecutingComponent(this.mApp, "broadcast", 2);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int numberOfReceivers() {
        return this.mReceivers.size();
    }

    ReceiverList getReceiverAt(int i) {
        return this.mReceivers.valueAt(i);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void addReceiver(ReceiverList receiverList) {
        this.mReceivers.add(receiverList);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void removeReceiver(ReceiverList receiverList) {
        this.mReceivers.remove(receiverList);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ProcessReceiverRecord(ProcessRecord processRecord) {
        this.mApp = processRecord;
        this.mService = processRecord.mService;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mService"})
    public void onCleanupApplicationRecordLocked() {
        for (int size = this.mReceivers.size() - 1; size >= 0; size--) {
            this.mService.removeReceiverLocked(this.mReceivers.valueAt(size));
        }
        this.mReceivers.clear();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void dump(PrintWriter printWriter, String str, long j) {
        if (!this.mCurReceivers.isEmpty()) {
            printWriter.print(str);
            printWriter.println("Current mReceivers:");
            int size = this.mCurReceivers.size();
            for (int i = 0; i < size; i++) {
                printWriter.print(str);
                printWriter.print("  - ");
                printWriter.println(this.mCurReceivers.valueAt(i));
            }
        }
        if (this.mReceivers.size() > 0) {
            printWriter.print(str);
            printWriter.println("mReceivers:");
            int size2 = this.mReceivers.size();
            for (int i2 = 0; i2 < size2; i2++) {
                printWriter.print(str);
                printWriter.print("  - ");
                printWriter.println(this.mReceivers.valueAt(i2));
            }
        }
    }
}
