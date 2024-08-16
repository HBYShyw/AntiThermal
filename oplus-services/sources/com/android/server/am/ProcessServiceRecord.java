package com.android.server.am;

import android.os.IBinder;
import android.os.SystemClock;
import android.util.ArrayMap;
import android.util.ArraySet;
import com.android.internal.annotations.GuardedBy;
import com.android.server.wm.WindowProcessController;
import java.io.PrintWriter;
import java.util.ArrayList;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public final class ProcessServiceRecord {
    boolean mAllowlistManager;
    final ProcessRecord mApp;
    private int mConnectionGroup;
    private int mConnectionImportance;
    private ServiceRecord mConnectionService;
    private boolean mExecServicesFg;
    private int mFgServiceTypes;
    private boolean mHasAboveClient;
    private boolean mHasClientActivities;
    private boolean mHasForegroundServices;
    private boolean mHasTopStartedAlmostPerceptibleServices;
    private boolean mHasTypeNoneFgs;
    private long mLastTopStartedAlmostPerceptibleBindRequestUptimeMs;
    private int mRepFgServiceTypes;
    private boolean mRepHasForegroundServices;
    private final ActivityManagerService mService;
    private boolean mTreatLikeActivity;
    final ArraySet<ServiceRecord> mServices = new ArraySet<>();
    private final ArraySet<ServiceRecord> mExecutingServices = new ArraySet<>();
    private final ArraySet<ConnectionRecord> mConnections = new ArraySet<>();
    private ArraySet<Integer> mBoundClientUids = new ArraySet<>();

    /* JADX INFO: Access modifiers changed from: package-private */
    public ProcessServiceRecord(ProcessRecord processRecord) {
        this.mApp = processRecord;
        this.mService = processRecord.mService;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setHasClientActivities(boolean z) {
        this.mHasClientActivities = z;
        this.mApp.getWindowProcessController().setHasClientActivities(z);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean hasClientActivities() {
        return this.mHasClientActivities;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setHasForegroundServices(boolean z, int i, boolean z2) {
        if (ActivityManagerDebugConfig.DEBUG_SERVICE) {
            if (z != (i != 0 || z2)) {
                throw new IllegalStateException("hasForegroundServices mismatch");
            }
        }
        this.mHasForegroundServices = z;
        this.mFgServiceTypes = i;
        this.mHasTypeNoneFgs = z2;
        this.mApp.getWindowProcessController().setHasForegroundServices(z);
        if (z) {
            this.mApp.mProfile.addHostingComponentType(256);
        } else {
            this.mApp.mProfile.clearHostingComponentType(256);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean hasForegroundServices() {
        return this.mHasForegroundServices;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setHasReportedForegroundServices(boolean z) {
        this.mRepHasForegroundServices = z;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean hasReportedForegroundServices() {
        return this.mRepHasForegroundServices;
    }

    private int getForegroundServiceTypes() {
        if (this.mHasForegroundServices) {
            return this.mFgServiceTypes;
        }
        return 0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean areForegroundServiceTypesSame(int i, boolean z) {
        return (getForegroundServiceTypes() & i) == i && this.mHasTypeNoneFgs == z;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean containsAnyForegroundServiceTypes(int i) {
        return (getForegroundServiceTypes() & i) != 0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean hasNonShortForegroundServices() {
        if (this.mHasForegroundServices) {
            return this.mHasTypeNoneFgs || this.mFgServiceTypes != 2048;
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean areAllShortForegroundServicesProcstateTimedOut(long j) {
        if (!this.mHasForegroundServices || hasNonShortForegroundServices()) {
            return false;
        }
        for (int size = this.mServices.size() - 1; size >= 0; size--) {
            ServiceRecord valueAt = this.mServices.valueAt(size);
            if (valueAt.isShortFgs() && valueAt.hasShortFgsInfo() && valueAt.getShortFgsInfo().getProcStateDemoteTime() >= j) {
                return false;
            }
        }
        return true;
    }

    int getReportedForegroundServiceTypes() {
        return this.mRepFgServiceTypes;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setReportedForegroundServiceTypes(int i) {
        this.mRepFgServiceTypes = i;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getNumForegroundServices() {
        int size = this.mServices.size();
        int i = 0;
        for (int i2 = 0; i2 < size; i2++) {
            if (this.mServices.valueAt(i2).isForeground) {
                i++;
            }
        }
        return i;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void updateHasTopStartedAlmostPerceptibleServices() {
        this.mHasTopStartedAlmostPerceptibleServices = false;
        this.mLastTopStartedAlmostPerceptibleBindRequestUptimeMs = 0L;
        for (int size = this.mServices.size() - 1; size >= 0; size--) {
            ServiceRecord valueAt = this.mServices.valueAt(size);
            this.mLastTopStartedAlmostPerceptibleBindRequestUptimeMs = Math.max(this.mLastTopStartedAlmostPerceptibleBindRequestUptimeMs, valueAt.lastTopAlmostPerceptibleBindRequestUptimeMs);
            if (!this.mHasTopStartedAlmostPerceptibleServices && isAlmostPerceptible(valueAt)) {
                this.mHasTopStartedAlmostPerceptibleServices = true;
            }
        }
    }

    private boolean isAlmostPerceptible(ServiceRecord serviceRecord) {
        if (serviceRecord.lastTopAlmostPerceptibleBindRequestUptimeMs <= 0) {
            return false;
        }
        ArrayMap<IBinder, ArrayList<ConnectionRecord>> connections = serviceRecord.getConnections();
        for (int size = connections.size() - 1; size >= 0; size--) {
            ArrayList<ConnectionRecord> valueAt = connections.valueAt(size);
            for (int size2 = valueAt.size() - 1; size2 >= 0; size2--) {
                if (valueAt.get(size2).hasFlag(65536)) {
                    return true;
                }
            }
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean hasTopStartedAlmostPerceptibleServices() {
        return this.mHasTopStartedAlmostPerceptibleServices || (this.mLastTopStartedAlmostPerceptibleBindRequestUptimeMs > 0 && SystemClock.uptimeMillis() - this.mLastTopStartedAlmostPerceptibleBindRequestUptimeMs < this.mService.mConstants.mServiceBindAlmostPerceptibleTimeoutMs);
    }

    ServiceRecord getConnectionService() {
        return this.mConnectionService;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setConnectionService(ServiceRecord serviceRecord) {
        this.mConnectionService = serviceRecord;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getConnectionGroup() {
        return this.mConnectionGroup;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setConnectionGroup(int i) {
        this.mConnectionGroup = i;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getConnectionImportance() {
        return this.mConnectionImportance;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setConnectionImportance(int i) {
        this.mConnectionImportance = i;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void updateHasAboveClientLocked() {
        this.mHasAboveClient = false;
        for (int size = this.mConnections.size() - 1; size >= 0; size--) {
            if (this.mConnections.valueAt(size).hasFlag(8)) {
                this.mHasAboveClient = true;
                return;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setHasAboveClient(boolean z) {
        this.mHasAboveClient = z;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean hasAboveClient() {
        return this.mHasAboveClient;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int modifyRawOomAdj(int i) {
        if (!this.mHasAboveClient || i < 0) {
            return i;
        }
        int i2 = 100;
        if (i >= 100) {
            i2 = 200;
            if (i >= 200) {
                i2 = 250;
                if (i >= 250) {
                    i2 = ProcessList.CACHED_APP_MIN_ADJ;
                    if (i >= 900) {
                        return i < 999 ? i + 1 : i;
                    }
                }
            }
        }
        return i2;
    }

    public boolean isTreatedLikeActivity() {
        return this.mTreatLikeActivity;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setTreatLikeActivity(boolean z) {
        this.mTreatLikeActivity = z;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean shouldExecServicesFg() {
        return this.mExecServicesFg;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setExecServicesFg(boolean z) {
        this.mExecServicesFg = z;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean startService(ServiceRecord serviceRecord) {
        if (serviceRecord == null) {
            return false;
        }
        boolean add = this.mServices.add(serviceRecord);
        if (add && serviceRecord.serviceInfo != null) {
            this.mApp.getWindowProcessController().onServiceStarted(serviceRecord.serviceInfo);
            updateHostingComonentTypeForBindingsLocked();
        }
        long j = serviceRecord.lastTopAlmostPerceptibleBindRequestUptimeMs;
        if (j > 0) {
            this.mLastTopStartedAlmostPerceptibleBindRequestUptimeMs = Math.max(this.mLastTopStartedAlmostPerceptibleBindRequestUptimeMs, j);
            if (!this.mHasTopStartedAlmostPerceptibleServices) {
                this.mHasTopStartedAlmostPerceptibleServices = isAlmostPerceptible(serviceRecord);
            }
        }
        return add;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean stopService(ServiceRecord serviceRecord) {
        boolean remove = this.mServices.remove(serviceRecord);
        if (serviceRecord.lastTopAlmostPerceptibleBindRequestUptimeMs > 0) {
            updateHasTopStartedAlmostPerceptibleServices();
        }
        if (remove) {
            updateHostingComonentTypeForBindingsLocked();
        }
        return remove;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void stopAllServices() {
        this.mServices.clear();
        updateHasTopStartedAlmostPerceptibleServices();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int numberOfRunningServices() {
        return this.mServices.size();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ServiceRecord getRunningServiceAt(int i) {
        return this.mServices.valueAt(i);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void startExecutingService(ServiceRecord serviceRecord) {
        this.mExecutingServices.add(serviceRecord);
        this.mApp.getWrapper().getExtImpl().updateExecutingComponent(this.mApp, HostingRecord.HOSTING_TYPE_SERVICE, 1);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void stopExecutingService(ServiceRecord serviceRecord) {
        this.mExecutingServices.remove(serviceRecord);
        this.mApp.getWrapper().getExtImpl().updateExecutingComponent(this.mApp, HostingRecord.HOSTING_TYPE_SERVICE, 2);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void stopAllExecutingServices() {
        this.mExecutingServices.clear();
        this.mApp.getWrapper().getExtImpl().updateExecutingComponent(this.mApp, HostingRecord.HOSTING_TYPE_SERVICE, -1);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ServiceRecord getExecutingServiceAt(int i) {
        return this.mExecutingServices.valueAt(i);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int numberOfExecutingServices() {
        return this.mExecutingServices.size();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void addConnection(ConnectionRecord connectionRecord) {
        this.mConnections.add(connectionRecord);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void removeConnection(ConnectionRecord connectionRecord) {
        this.mConnections.remove(connectionRecord);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void removeAllConnections() {
        this.mConnections.clear();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ConnectionRecord getConnectionAt(int i) {
        return this.mConnections.valueAt(i);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int numberOfConnections() {
        return this.mConnections.size();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void addBoundClientUid(int i, String str, long j) {
        this.mBoundClientUids.add(Integer.valueOf(i));
        this.mApp.getWindowProcessController().addBoundClientUid(i, str, j);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void updateBoundClientUids() {
        clearBoundClientUids();
        if (this.mServices.isEmpty()) {
            return;
        }
        ArraySet<Integer> arraySet = new ArraySet<>();
        int size = this.mServices.size();
        WindowProcessController windowProcessController = this.mApp.getWindowProcessController();
        for (int i = 0; i < size; i++) {
            ArrayMap<IBinder, ArrayList<ConnectionRecord>> connections = this.mServices.valueAt(i).getConnections();
            int size2 = connections.size();
            for (int i2 = 0; i2 < size2; i2++) {
                ArrayList<ConnectionRecord> valueAt = connections.valueAt(i2);
                for (int i3 = 0; i3 < valueAt.size(); i3++) {
                    ConnectionRecord connectionRecord = valueAt.get(i3);
                    arraySet.add(Integer.valueOf(connectionRecord.clientUid));
                    windowProcessController.addBoundClientUid(connectionRecord.clientUid, connectionRecord.clientPackageName, connectionRecord.getFlags());
                }
            }
        }
        this.mBoundClientUids = arraySet;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void addBoundClientUidsOfNewService(ServiceRecord serviceRecord) {
        if (serviceRecord == null) {
            return;
        }
        ArrayMap<IBinder, ArrayList<ConnectionRecord>> connections = serviceRecord.getConnections();
        for (int size = connections.size() - 1; size >= 0; size--) {
            ArrayList<ConnectionRecord> valueAt = connections.valueAt(size);
            for (int i = 0; i < valueAt.size(); i++) {
                ConnectionRecord connectionRecord = valueAt.get(i);
                this.mBoundClientUids.add(Integer.valueOf(connectionRecord.clientUid));
                this.mApp.getWindowProcessController().addBoundClientUid(connectionRecord.clientUid, connectionRecord.clientPackageName, connectionRecord.getFlags());
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void clearBoundClientUids() {
        this.mBoundClientUids.clear();
        this.mApp.getWindowProcessController().clearBoundClientUids();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mService"})
    public void updateHostingComonentTypeForBindingsLocked() {
        boolean z = true;
        int numberOfRunningServices = numberOfRunningServices() - 1;
        while (true) {
            if (numberOfRunningServices >= 0) {
                ServiceRecord runningServiceAt = getRunningServiceAt(numberOfRunningServices);
                if (runningServiceAt != null && !runningServiceAt.getConnections().isEmpty()) {
                    break;
                } else {
                    numberOfRunningServices--;
                }
            } else {
                z = false;
                break;
            }
        }
        if (z) {
            this.mApp.mProfile.addHostingComponentType(512);
        } else {
            this.mApp.mProfile.clearHostingComponentType(512);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mService"})
    public boolean incServiceCrashCountLocked(long j) {
        boolean z = false;
        boolean z2 = this.mApp.mState.getCurProcState() == 5;
        for (int numberOfRunningServices = numberOfRunningServices() - 1; numberOfRunningServices >= 0; numberOfRunningServices--) {
            ServiceRecord runningServiceAt = getRunningServiceAt(numberOfRunningServices);
            if (j > runningServiceAt.restartTime + ActivityManagerConstants.MIN_CRASH_INTERVAL) {
                runningServiceAt.crashCount = 1;
            } else {
                runningServiceAt.crashCount++;
            }
            if (runningServiceAt.crashCount < this.mService.mConstants.BOUND_SERVICE_MAX_CRASH_RETRY && (runningServiceAt.isForeground || z2)) {
                z = true;
            }
        }
        return z;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mService"})
    public void onCleanupApplicationRecordLocked() {
        this.mTreatLikeActivity = false;
        this.mHasAboveClient = false;
        setHasClientActivities(false);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void dump(PrintWriter printWriter, String str, long j) {
        if (this.mHasForegroundServices || this.mApp.mState.getForcingToImportant() != null) {
            printWriter.print(str);
            printWriter.print("mHasForegroundServices=");
            printWriter.print(this.mHasForegroundServices);
            printWriter.print(" forcingToImportant=");
            printWriter.println(this.mApp.mState.getForcingToImportant());
        }
        if (this.mHasTopStartedAlmostPerceptibleServices || this.mLastTopStartedAlmostPerceptibleBindRequestUptimeMs > 0) {
            printWriter.print(str);
            printWriter.print("mHasTopStartedAlmostPerceptibleServices=");
            printWriter.print(this.mHasTopStartedAlmostPerceptibleServices);
            printWriter.print(" mLastTopStartedAlmostPerceptibleBindRequestUptimeMs=");
            printWriter.println(this.mLastTopStartedAlmostPerceptibleBindRequestUptimeMs);
        }
        if (this.mHasClientActivities || this.mHasAboveClient || this.mTreatLikeActivity) {
            printWriter.print(str);
            printWriter.print("hasClientActivities=");
            printWriter.print(this.mHasClientActivities);
            printWriter.print(" hasAboveClient=");
            printWriter.print(this.mHasAboveClient);
            printWriter.print(" treatLikeActivity=");
            printWriter.println(this.mTreatLikeActivity);
        }
        if (this.mConnectionService != null || this.mConnectionGroup != 0) {
            printWriter.print(str);
            printWriter.print("connectionGroup=");
            printWriter.print(this.mConnectionGroup);
            printWriter.print(" Importance=");
            printWriter.print(this.mConnectionImportance);
            printWriter.print(" Service=");
            printWriter.println(this.mConnectionService);
        }
        if (this.mAllowlistManager) {
            printWriter.print(str);
            printWriter.print("allowlistManager=");
            printWriter.println(this.mAllowlistManager);
        }
        if (this.mServices.size() > 0) {
            printWriter.print(str);
            printWriter.println("Services:");
            int size = this.mServices.size();
            for (int i = 0; i < size; i++) {
                printWriter.print(str);
                printWriter.print("  - ");
                printWriter.println(this.mServices.valueAt(i));
            }
        }
        if (this.mExecutingServices.size() > 0) {
            printWriter.print(str);
            printWriter.print("Executing Services (fg=");
            printWriter.print(this.mExecServicesFg);
            printWriter.println(")");
            int size2 = this.mExecutingServices.size();
            for (int i2 = 0; i2 < size2; i2++) {
                printWriter.print(str);
                printWriter.print("  - ");
                printWriter.println(this.mExecutingServices.valueAt(i2));
            }
        }
        if (this.mConnections.size() > 0) {
            printWriter.print(str);
            printWriter.println("mConnections:");
            int size3 = this.mConnections.size();
            for (int i3 = 0; i3 < size3; i3++) {
                printWriter.print(str);
                printWriter.print("  - ");
                printWriter.println(this.mConnections.valueAt(i3));
            }
        }
    }
}
