package com.android.server.am;

import android.util.ArrayMap;
import android.util.TimeUtils;
import com.android.internal.annotations.GuardedBy;
import java.io.PrintWriter;
import java.util.ArrayList;
import system.ext.loader.core.ExtLoader;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public final class ProcessProviderRecord {
    final ProcessRecord mApp;
    private long mLastProviderTime;
    private final ActivityManagerService mService;
    public IProcessProviderRecordExt mProcessProviderRecordExt = (IProcessProviderRecordExt) ExtLoader.type(IProcessProviderRecordExt.class).create();
    private final ArrayMap<String, ContentProviderRecord> mPubProviders = new ArrayMap<>();
    private final ArrayList<ContentProviderConnection> mConProviders = new ArrayList<>();

    /* JADX INFO: Access modifiers changed from: package-private */
    public long getLastProviderTime() {
        return this.mLastProviderTime;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setLastProviderTime(long j) {
        this.mLastProviderTime = j;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean hasProvider(String str) {
        return this.mPubProviders.containsKey(str);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ContentProviderRecord getProvider(String str) {
        return this.mPubProviders.get(str);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int numberOfProviders() {
        return this.mPubProviders.size();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ContentProviderRecord getProviderAt(int i) {
        return this.mPubProviders.valueAt(i);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void installProvider(String str, ContentProviderRecord contentProviderRecord) {
        this.mPubProviders.put(str, contentProviderRecord);
    }

    void removeProvider(String str) {
        this.mPubProviders.remove(str);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void ensureProviderCapacity(int i) {
        this.mPubProviders.ensureCapacity(i);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int numberOfProviderConnections() {
        return this.mConProviders.size();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ContentProviderConnection getProviderConnectionAt(int i) {
        return this.mConProviders.get(i);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void addProviderConnection(ContentProviderConnection contentProviderConnection) {
        this.mConProviders.add(contentProviderConnection);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean removeProviderConnection(ContentProviderConnection contentProviderConnection) {
        return this.mConProviders.remove(contentProviderConnection);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ProcessProviderRecord(ProcessRecord processRecord) {
        this.mApp = processRecord;
        this.mService = processRecord.mService;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mService"})
    public boolean onCleanupApplicationRecordLocked(boolean z) {
        boolean z2 = false;
        for (int size = this.mPubProviders.size() - 1; size >= 0; size--) {
            ContentProviderRecord valueAt = this.mPubProviders.valueAt(size);
            ProcessRecord processRecord = valueAt.proc;
            ProcessRecord processRecord2 = this.mApp;
            if (processRecord == processRecord2) {
                boolean z3 = processRecord2.mErrorState.isBad() || !z;
                boolean removeDyingProviderLocked = this.mService.mCpHelper.removeDyingProviderLocked(this.mApp, valueAt, z3);
                if (!z3 && removeDyingProviderLocked && valueAt.hasConnectionOrHandle()) {
                    z2 = true;
                }
                valueAt.provider = null;
                valueAt.setProcess(null);
            }
        }
        this.mPubProviders.clear();
        ActivityManagerService activityManagerService = this.mService;
        ContentProviderHelper contentProviderHelper = activityManagerService.mCpHelper;
        ProcessRecord processRecord3 = this.mApp;
        if (contentProviderHelper.cleanupAppInLaunchingProvidersLocked(processRecord3, this.mProcessProviderRecordExt.checkIfAlwaysCleanupAppInLaunchingProviders(activityManagerService.mContext, processRecord3, z))) {
            this.mService.mProcessList.noteProcessDiedLocked(this.mApp);
            z2 = true;
        }
        if (!this.mConProviders.isEmpty()) {
            for (int size2 = this.mConProviders.size() - 1; size2 >= 0; size2--) {
                ContentProviderConnection contentProviderConnection = this.mConProviders.get(size2);
                contentProviderConnection.provider.connections.remove(contentProviderConnection);
                ActivityManagerService activityManagerService2 = this.mService;
                ProcessRecord processRecord4 = this.mApp;
                int i = processRecord4.uid;
                String str = processRecord4.processName;
                ContentProviderRecord contentProviderRecord = contentProviderConnection.provider;
                activityManagerService2.stopAssociationLocked(i, str, contentProviderRecord.uid, contentProviderRecord.appInfo.longVersionCode, contentProviderRecord.name, contentProviderRecord.info.processName);
            }
            this.mConProviders.clear();
        }
        return z2;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void dump(PrintWriter printWriter, String str, long j) {
        if (this.mLastProviderTime > 0) {
            printWriter.print(str);
            printWriter.print("lastProviderTime=");
            TimeUtils.formatDuration(this.mLastProviderTime, j, printWriter);
            printWriter.println();
        }
        if (this.mPubProviders.size() > 0) {
            printWriter.print(str);
            printWriter.println("Published Providers:");
            int size = this.mPubProviders.size();
            for (int i = 0; i < size; i++) {
                printWriter.print(str);
                printWriter.print("  - ");
                printWriter.println(this.mPubProviders.keyAt(i));
                printWriter.print(str);
                printWriter.print("    -> ");
                printWriter.println(this.mPubProviders.valueAt(i));
            }
        }
        if (this.mConProviders.size() > 0) {
            printWriter.print(str);
            printWriter.println("Connected Providers:");
            int size2 = this.mConProviders.size();
            for (int i2 = 0; i2 < size2; i2++) {
                printWriter.print(str);
                printWriter.print("  - ");
                printWriter.println(this.mConProviders.get(i2).toShortString());
            }
        }
    }
}
