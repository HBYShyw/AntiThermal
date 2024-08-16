package com.android.server.am;

import android.app.ContentProviderHolder;
import android.app.IApplicationThread;
import android.content.ComponentName;
import android.content.IContentProvider;
import android.content.pm.ApplicationInfo;
import android.content.pm.ProviderInfo;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.UserHandle;
import android.util.ArrayMap;
import android.util.Slog;
import com.android.internal.app.procstats.AssociationState;
import com.android.internal.app.procstats.ProcessStats;
import java.io.PrintWriter;
import java.util.ArrayList;
import system.ext.loader.core.ExtLoader;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public final class ContentProviderRecord implements ComponentName.WithComponentName {
    static final int MAX_RETRY_COUNT = 3;
    final ApplicationInfo appInfo;
    int externalProcessNoHandleCount;
    ArrayMap<IBinder, ExternalProcessHandle> externalProcessTokenToHandle;
    public final ProviderInfo info;
    ProcessRecord launchingApp;
    int mRestartCount;
    final ComponentName name;
    public boolean noReleaseNeeded;
    ProcessRecord proc;
    public IContentProvider provider;
    final ActivityManagerService service;
    String shortStringName;
    final boolean singleton;
    String stringName;
    final int uid;
    final ArrayList<ContentProviderConnection> connections = new ArrayList<>();
    public IContentProviderRecordExt mContentProviderRecordExt = (IContentProviderRecordExt) ExtLoader.type(IContentProviderRecordExt.class).create();

    public ContentProviderRecord(ActivityManagerService activityManagerService, ProviderInfo providerInfo, ApplicationInfo applicationInfo, ComponentName componentName, boolean z) {
        this.service = activityManagerService;
        this.info = providerInfo;
        int i = applicationInfo.uid;
        this.uid = i;
        this.appInfo = applicationInfo;
        this.name = componentName;
        this.singleton = z;
        boolean z2 = (i == 0 || i == 1000) && (componentName == null || !"com.android.settings".equals(componentName.getPackageName()));
        this.noReleaseNeeded = z2;
        if (componentName == null || !z2) {
            return;
        }
        this.noReleaseNeeded = !this.mContentProviderRecordExt.isNeedRelease(componentName.getPackageName());
    }

    public ContentProviderRecord(ContentProviderRecord contentProviderRecord) {
        this.service = contentProviderRecord.service;
        this.info = contentProviderRecord.info;
        this.uid = contentProviderRecord.uid;
        this.appInfo = contentProviderRecord.appInfo;
        this.name = contentProviderRecord.name;
        this.singleton = contentProviderRecord.singleton;
        this.noReleaseNeeded = contentProviderRecord.noReleaseNeeded;
    }

    public ContentProviderHolder newHolder(ContentProviderConnection contentProviderConnection, boolean z) {
        ContentProviderHolder contentProviderHolder = new ContentProviderHolder(this.info);
        contentProviderHolder.provider = this.provider;
        contentProviderHolder.noReleaseNeeded = this.noReleaseNeeded;
        contentProviderHolder.connection = contentProviderConnection;
        contentProviderHolder.mLocal = z;
        return contentProviderHolder;
    }

    public void setProcess(ProcessRecord processRecord) {
        this.proc = processRecord;
        for (int size = this.connections.size() - 1; size >= 0; size--) {
            ContentProviderConnection contentProviderConnection = this.connections.get(size);
            if (processRecord != null) {
                contentProviderConnection.startAssociationIfNeeded();
            } else {
                contentProviderConnection.stopAssociation();
            }
        }
        ArrayMap<IBinder, ExternalProcessHandle> arrayMap = this.externalProcessTokenToHandle;
        if (arrayMap != null) {
            for (int size2 = arrayMap.size() - 1; size2 >= 0; size2--) {
                ExternalProcessHandle valueAt = this.externalProcessTokenToHandle.valueAt(size2);
                if (processRecord != null) {
                    valueAt.startAssociationIfNeeded(this);
                } else {
                    valueAt.stopAssociation();
                }
            }
        }
    }

    public boolean canRunHere(ProcessRecord processRecord) {
        ProviderInfo providerInfo = this.info;
        return (providerInfo.multiprocess || providerInfo.processName.equals(processRecord.processName)) && this.uid == processRecord.info.uid;
    }

    public void addExternalProcessHandleLocked(IBinder iBinder, int i, String str) {
        if (iBinder == null) {
            this.externalProcessNoHandleCount++;
            return;
        }
        if (this.externalProcessTokenToHandle == null) {
            this.externalProcessTokenToHandle = new ArrayMap<>();
        }
        ExternalProcessHandle externalProcessHandle = this.externalProcessTokenToHandle.get(iBinder);
        if (externalProcessHandle == null) {
            externalProcessHandle = new ExternalProcessHandle(iBinder, i, str);
            this.externalProcessTokenToHandle.put(iBinder, externalProcessHandle);
            externalProcessHandle.startAssociationIfNeeded(this);
        }
        externalProcessHandle.mAcquisitionCount++;
    }

    public boolean removeExternalProcessHandleLocked(IBinder iBinder) {
        boolean z;
        ExternalProcessHandle externalProcessHandle;
        if (hasExternalProcessHandles()) {
            ArrayMap<IBinder, ExternalProcessHandle> arrayMap = this.externalProcessTokenToHandle;
            if (arrayMap == null || (externalProcessHandle = arrayMap.get(iBinder)) == null) {
                z = false;
            } else {
                int i = externalProcessHandle.mAcquisitionCount - 1;
                externalProcessHandle.mAcquisitionCount = i;
                if (i == 0) {
                    removeExternalProcessHandleInternalLocked(iBinder);
                    return true;
                }
                z = true;
            }
            if (!z) {
                this.externalProcessNoHandleCount--;
                return true;
            }
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void removeExternalProcessHandleInternalLocked(IBinder iBinder) {
        ExternalProcessHandle externalProcessHandle = this.externalProcessTokenToHandle.get(iBinder);
        externalProcessHandle.unlinkFromOwnDeathLocked();
        externalProcessHandle.stopAssociation();
        this.externalProcessTokenToHandle.remove(iBinder);
        if (this.externalProcessTokenToHandle.size() == 0) {
            this.externalProcessTokenToHandle = null;
        }
    }

    public boolean hasExternalProcessHandles() {
        return this.externalProcessTokenToHandle != null || this.externalProcessNoHandleCount > 0;
    }

    public boolean hasConnectionOrHandle() {
        return !this.connections.isEmpty() || hasExternalProcessHandles();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onProviderPublishStatusLocked(boolean z) {
        ProcessRecord processRecord;
        int size = this.connections.size();
        for (int i = 0; i < size; i++) {
            try {
                ContentProviderConnection contentProviderConnection = this.connections.get(i);
                if (contentProviderConnection.waiting && (processRecord = contentProviderConnection.client) != null) {
                    if (!z) {
                        ProcessRecord processRecord2 = this.launchingApp;
                        if (processRecord2 == null) {
                            Slog.w(IActivityManagerServiceExt.TAG, "Unable to launch app " + this.appInfo.packageName + "/" + this.appInfo.uid + " for provider " + this.info.authority + ": launching app became null");
                            int userId = UserHandle.getUserId(this.appInfo.uid);
                            ApplicationInfo applicationInfo = this.appInfo;
                            EventLogTags.writeAmProviderLostProcess(userId, applicationInfo.packageName, applicationInfo.uid, this.info.authority);
                        } else {
                            this.mContentProviderRecordExt.hookProviderTimeout(this.service, processRecord2, this.appInfo);
                            Slog.wtf(IActivityManagerServiceExt.TAG, "Timeout waiting for provider " + this.appInfo.packageName + "/" + this.appInfo.uid + " for provider " + this.info.authority + " caller=" + processRecord);
                        }
                    }
                    IApplicationThread thread = processRecord.getThread();
                    if (thread != null) {
                        try {
                            thread.notifyContentProviderPublishStatus(newHolder(z ? contentProviderConnection : null, false), this.info.authority, contentProviderConnection.mExpectedUserId, z);
                        } catch (RemoteException unused) {
                        }
                    }
                }
                contentProviderConnection.waiting = false;
            } catch (IndexOutOfBoundsException unused2) {
                Slog.e(IActivityManagerServiceExt.TAG, "cpr.connections.get cause IndexOutOfBoundsException, hosting process is " + this.proc + ", launching proc is " + this.launchingApp + ", provider is " + this.info);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void dump(PrintWriter printWriter, String str, boolean z) {
        if (z) {
            printWriter.print(str);
            printWriter.print("package=");
            printWriter.print(this.info.applicationInfo.packageName);
            printWriter.print(" process=");
            printWriter.println(this.info.processName);
        }
        printWriter.print(str);
        printWriter.print("proc=");
        printWriter.println(this.proc);
        if (this.launchingApp != null) {
            printWriter.print(str);
            printWriter.print("launchingApp=");
            printWriter.println(this.launchingApp);
        }
        if (z) {
            printWriter.print(str);
            printWriter.print("uid=");
            printWriter.print(this.uid);
            printWriter.print(" provider=");
            printWriter.println(this.provider);
        }
        if (this.singleton) {
            printWriter.print(str);
            printWriter.print("singleton=");
            printWriter.println(this.singleton);
        }
        printWriter.print(str);
        printWriter.print("authority=");
        printWriter.println(this.info.authority);
        if (z) {
            ProviderInfo providerInfo = this.info;
            if (providerInfo.isSyncable || providerInfo.multiprocess || providerInfo.initOrder != 0) {
                printWriter.print(str);
                printWriter.print("isSyncable=");
                printWriter.print(this.info.isSyncable);
                printWriter.print(" multiprocess=");
                printWriter.print(this.info.multiprocess);
                printWriter.print(" initOrder=");
                printWriter.println(this.info.initOrder);
            }
        }
        if (z) {
            if (hasExternalProcessHandles()) {
                printWriter.print(str);
                printWriter.print("externals:");
                if (this.externalProcessTokenToHandle != null) {
                    printWriter.print(" w/token=");
                    printWriter.print(this.externalProcessTokenToHandle.size());
                }
                if (this.externalProcessNoHandleCount > 0) {
                    printWriter.print(" notoken=");
                    printWriter.print(this.externalProcessNoHandleCount);
                }
                printWriter.println();
            }
        } else if (this.connections.size() > 0 || this.externalProcessNoHandleCount > 0) {
            printWriter.print(str);
            printWriter.print(this.connections.size());
            printWriter.print(" connections, ");
            printWriter.print(this.externalProcessNoHandleCount);
            printWriter.println(" external handles");
        }
        if (this.connections.size() > 0) {
            if (z) {
                printWriter.print(str);
                printWriter.println("Connections:");
            }
            for (int i = 0; i < this.connections.size(); i++) {
                ContentProviderConnection contentProviderConnection = this.connections.get(i);
                printWriter.print(str);
                printWriter.print("  -> ");
                printWriter.println(contentProviderConnection.toClientString());
                if (contentProviderConnection.provider != this) {
                    printWriter.print(str);
                    printWriter.print("    *** WRONG PROVIDER: ");
                    printWriter.println(contentProviderConnection.provider);
                }
            }
        }
    }

    public String toString() {
        String str = this.stringName;
        if (str != null) {
            return str;
        }
        StringBuilder sb = new StringBuilder(128);
        sb.append("ContentProviderRecord{");
        sb.append(Integer.toHexString(System.identityHashCode(this)));
        sb.append(" u");
        sb.append(UserHandle.getUserId(this.uid));
        sb.append(' ');
        sb.append(this.name.flattenToShortString());
        sb.append('}');
        String sb2 = sb.toString();
        this.stringName = sb2;
        return sb2;
    }

    public String toShortString() {
        String str = this.shortStringName;
        if (str != null) {
            return str;
        }
        StringBuilder sb = new StringBuilder(128);
        sb.append(Integer.toHexString(System.identityHashCode(this)));
        sb.append('/');
        sb.append(this.name.flattenToShortString());
        String sb2 = sb.toString();
        this.shortStringName = sb2;
        return sb2;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public class ExternalProcessHandle implements IBinder.DeathRecipient {
        private static final String LOG_TAG = "ExternalProcessHanldle";
        int mAcquisitionCount;
        AssociationState.SourceState mAssociation;
        final String mOwningProcessName;
        final int mOwningUid;
        private Object mProcStatsLock;
        final IBinder mToken;

        public ExternalProcessHandle(IBinder iBinder, int i, String str) {
            this.mToken = iBinder;
            this.mOwningUid = i;
            this.mOwningProcessName = str;
            try {
                iBinder.linkToDeath(this, 0);
            } catch (RemoteException e) {
                Slog.e(LOG_TAG, "Couldn't register for death for token: " + this.mToken, e);
            }
        }

        public void unlinkFromOwnDeathLocked() {
            this.mToken.unlinkToDeath(this, 0);
        }

        public void startAssociationIfNeeded(ContentProviderRecord contentProviderRecord) {
            if (this.mAssociation != null || contentProviderRecord.proc == null) {
                return;
            }
            if (contentProviderRecord.appInfo.uid == this.mOwningUid && contentProviderRecord.info.processName.equals(this.mOwningProcessName)) {
                return;
            }
            ProcessStats.ProcessStateHolder processStateHolder = contentProviderRecord.proc.getPkgList().get(contentProviderRecord.name.getPackageName());
            if (processStateHolder == null) {
                Slog.wtf(IActivityManagerServiceExt.TAG, "No package in referenced provider " + contentProviderRecord.name.toShortString() + ": proc=" + contentProviderRecord.proc);
                return;
            }
            if (processStateHolder.pkg == null) {
                Slog.wtf(IActivityManagerServiceExt.TAG, "Inactive holder in referenced provider " + contentProviderRecord.name.toShortString() + ": proc=" + contentProviderRecord.proc);
                return;
            }
            Object obj = contentProviderRecord.proc.mService.mProcessStats.mLock;
            this.mProcStatsLock = obj;
            synchronized (obj) {
                this.mAssociation = processStateHolder.pkg.getAssociationStateLocked(processStateHolder.state, contentProviderRecord.name.getClassName()).startSource(this.mOwningUid, this.mOwningProcessName, (String) null);
            }
        }

        public void stopAssociation() {
            if (this.mAssociation != null) {
                synchronized (this.mProcStatsLock) {
                    this.mAssociation.stop();
                }
                this.mAssociation = null;
            }
        }

        @Override // android.os.IBinder.DeathRecipient
        public void binderDied() {
            ActivityManagerService activityManagerService = ContentProviderRecord.this.service;
            ActivityManagerService.boostPriorityForLockedSection();
            synchronized (activityManagerService) {
                try {
                    if (ContentProviderRecord.this.hasExternalProcessHandles() && ContentProviderRecord.this.externalProcessTokenToHandle.get(this.mToken) != null) {
                        ContentProviderRecord.this.removeExternalProcessHandleInternalLocked(this.mToken);
                    }
                } catch (Throwable th) {
                    ActivityManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            ActivityManagerService.resetPriorityAfterLockedSection();
        }
    }

    public ComponentName getComponentName() {
        return this.name;
    }
}
