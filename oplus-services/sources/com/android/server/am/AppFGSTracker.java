package com.android.server.am;

import android.app.ActivityManagerInternal;
import android.app.IProcessObserver;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.ServiceInfo;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.os.SystemClock;
import android.os.UserHandle;
import android.provider.DeviceConfig;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.ArrayMap;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import android.util.proto.ProtoOutputStream;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.os.SomeArgs;
import com.android.server.am.AppRestrictionController;
import com.android.server.am.BaseAppStateEvents;
import com.android.server.am.BaseAppStateEventsTracker;
import com.android.server.am.BaseAppStateTimeEvents;
import com.android.server.am.BaseAppStateTracker;
import com.android.server.backup.BackupManagerConstants;
import java.io.PrintWriter;
import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public final class AppFGSTracker extends BaseAppStateDurationsTracker<AppFGSPolicy, PackageDurations> implements ActivityManagerInternal.ForegroundServiceStateListener {
    static final boolean DEBUG_BACKGROUND_FGS_TRACKER = false;
    static final String TAG = "ActivityManager";

    @GuardedBy({"mLock"})
    private final UidProcessMap<SparseBooleanArray> mFGSNotificationIDs;
    private final MyHandler mHandler;

    @VisibleForTesting
    final NotificationListener mNotificationListener;
    final IProcessObserver.Stub mProcessObserver;
    private final ArrayMap<PackageDurations, Long> mTmpPkgDurations;

    @Override // com.android.server.am.BaseAppStateTracker
    @AppRestrictionController.TrackerType
    int getType() {
        return 3;
    }

    public void onForegroundServiceStateChanged(String str, int i, int i2, boolean z) {
        this.mHandler.obtainMessage(!z ? 1 : 0, i2, i, str).sendToTarget();
    }

    public void onForegroundServiceNotificationUpdated(String str, int i, int i2, boolean z) {
        SomeArgs obtain = SomeArgs.obtain();
        obtain.argi1 = i;
        obtain.argi2 = i2;
        obtain.arg1 = str;
        obtain.arg2 = z ? Boolean.TRUE : Boolean.FALSE;
        this.mHandler.obtainMessage(3, obtain).sendToTarget();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static class MyHandler extends Handler {
        static final int MSG_CHECK_LONG_RUNNING_FGS = 4;
        static final int MSG_FOREGROUND_SERVICES_CHANGED = 2;
        static final int MSG_FOREGROUND_SERVICES_NOTIFICATION_UPDATED = 3;
        static final int MSG_FOREGROUND_SERVICES_STARTED = 0;
        static final int MSG_FOREGROUND_SERVICES_STOPPED = 1;
        static final int MSG_NOTIFICATION_POSTED = 5;
        static final int MSG_NOTIFICATION_REMOVED = 6;
        private final AppFGSTracker mTracker;

        MyHandler(AppFGSTracker appFGSTracker) {
            super(appFGSTracker.mBgHandler.getLooper());
            this.mTracker = appFGSTracker;
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            switch (message.what) {
                case 0:
                    this.mTracker.handleForegroundServicesChanged((String) message.obj, message.arg1, message.arg2, true);
                    return;
                case 1:
                    this.mTracker.handleForegroundServicesChanged((String) message.obj, message.arg1, message.arg2, false);
                    return;
                case 2:
                    this.mTracker.handleForegroundServicesChanged((String) message.obj, message.arg1, message.arg2);
                    return;
                case 3:
                    SomeArgs someArgs = (SomeArgs) message.obj;
                    this.mTracker.handleForegroundServiceNotificationUpdated((String) someArgs.arg1, someArgs.argi1, someArgs.argi2, ((Boolean) someArgs.arg2).booleanValue());
                    someArgs.recycle();
                    return;
                case 4:
                    this.mTracker.checkLongRunningFgs();
                    return;
                case 5:
                    this.mTracker.handleNotificationPosted((String) message.obj, message.arg1, message.arg2);
                    return;
                case 6:
                    this.mTracker.handleNotificationRemoved((String) message.obj, message.arg1, message.arg2);
                    return;
                default:
                    return;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public AppFGSTracker(Context context, AppRestrictionController appRestrictionController) {
        this(context, appRestrictionController, null, null);
    }

    AppFGSTracker(Context context, AppRestrictionController appRestrictionController, Constructor<? extends BaseAppStateTracker.Injector<AppFGSPolicy>> constructor, Object obj) {
        super(context, appRestrictionController, constructor, obj);
        this.mFGSNotificationIDs = new UidProcessMap<>();
        this.mTmpPkgDurations = new ArrayMap<>();
        this.mNotificationListener = new NotificationListener();
        this.mProcessObserver = new IProcessObserver.Stub() { // from class: com.android.server.am.AppFGSTracker.1
            public void onForegroundActivitiesChanged(int i, int i2, boolean z) {
            }

            public void onProcessDied(int i, int i2) {
            }

            public void onForegroundServicesChanged(int i, int i2, int i3) {
                String packageName = AppFGSTracker.this.mAppRestrictionController.getPackageName(i);
                if (packageName != null) {
                    AppFGSTracker.this.mHandler.obtainMessage(2, i2, i3, packageName).sendToTarget();
                }
            }
        };
        this.mHandler = new MyHandler(this);
        BaseAppStateTracker.Injector<T> injector = this.mInjector;
        injector.setPolicy(new AppFGSPolicy(injector, this));
    }

    @Override // com.android.server.am.BaseAppStateTracker
    void onSystemReady() {
        super.onSystemReady();
        this.mInjector.getActivityManagerInternal().addForegroundServiceStateListener(this);
        this.mInjector.getActivityManagerInternal().registerProcessObserver(this.mProcessObserver);
    }

    @Override // com.android.server.am.BaseAppStateDurationsTracker, com.android.server.am.BaseAppStateEventsTracker
    @VisibleForTesting
    void reset() {
        this.mHandler.removeMessages(4);
        super.reset();
    }

    @Override // com.android.server.am.BaseAppStateEvents.Factory
    public PackageDurations createAppStateEvents(int i, String str) {
        return new PackageDurations(i, str, (BaseAppStateEvents.MaxTrackingDurationConfig) this.mInjector.getPolicy(), this);
    }

    @Override // com.android.server.am.BaseAppStateEvents.Factory
    public PackageDurations createAppStateEvents(PackageDurations packageDurations) {
        return new PackageDurations(packageDurations);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleForegroundServicesChanged(String str, int i, int i2, boolean z) {
        boolean z2;
        if (((AppFGSPolicy) this.mInjector.getPolicy()).isEnabled()) {
            long elapsedRealtime = SystemClock.elapsedRealtime();
            int shouldExemptUid = ((AppFGSPolicy) this.mInjector.getPolicy()).shouldExemptUid(i2);
            synchronized (this.mLock) {
                PackageDurations packageDurations = (PackageDurations) this.mPkgEvents.get(i2, str);
                if (packageDurations == null) {
                    packageDurations = createAppStateEvents(i2, str);
                    this.mPkgEvents.put(i2, str, packageDurations);
                }
                boolean isLongRunning = packageDurations.isLongRunning();
                packageDurations.addEvent(z, elapsedRealtime);
                z2 = isLongRunning && !packageDurations.hasForegroundServices();
                if (z2) {
                    packageDurations.setIsLongRunning(false);
                }
                packageDurations.mExemptReason = shouldExemptUid;
                scheduleDurationCheckLocked(elapsedRealtime);
            }
            if (z2) {
                ((AppFGSPolicy) this.mInjector.getPolicy()).onLongRunningFgsGone(str, i2);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleForegroundServiceNotificationUpdated(String str, int i, int i2, boolean z) {
        int indexOfKey;
        synchronized (this.mLock) {
            SparseBooleanArray sparseBooleanArray = this.mFGSNotificationIDs.get(i, str);
            if (!z) {
                if (sparseBooleanArray == null) {
                    sparseBooleanArray = new SparseBooleanArray();
                    this.mFGSNotificationIDs.put(i, str, sparseBooleanArray);
                }
                sparseBooleanArray.put(i2, false);
            } else if (sparseBooleanArray != null && (indexOfKey = sparseBooleanArray.indexOfKey(i2)) >= 0) {
                boolean valueAt = sparseBooleanArray.valueAt(indexOfKey);
                sparseBooleanArray.removeAt(indexOfKey);
                if (sparseBooleanArray.size() == 0) {
                    this.mFGSNotificationIDs.remove(i, str);
                }
                for (int size = sparseBooleanArray.size() - 1; size >= 0; size--) {
                    if (sparseBooleanArray.valueAt(size)) {
                        return;
                    }
                }
                if (valueAt) {
                    notifyListenersOnStateChange(i, str, false, SystemClock.elapsedRealtime(), 8);
                }
            }
        }
    }

    @GuardedBy({"mLock"})
    private boolean hasForegroundServiceNotificationsLocked(String str, int i) {
        SparseBooleanArray sparseBooleanArray = this.mFGSNotificationIDs.get(i, str);
        if (sparseBooleanArray != null && sparseBooleanArray.size() != 0) {
            for (int size = sparseBooleanArray.size() - 1; size >= 0; size--) {
                if (sparseBooleanArray.valueAt(size)) {
                    return true;
                }
            }
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleNotificationPosted(String str, int i, int i2) {
        int indexOfKey;
        boolean z;
        synchronized (this.mLock) {
            SparseBooleanArray sparseBooleanArray = this.mFGSNotificationIDs.get(i, str);
            if (sparseBooleanArray != null && (indexOfKey = sparseBooleanArray.indexOfKey(i2)) >= 0) {
                if (sparseBooleanArray.valueAt(indexOfKey)) {
                    return;
                }
                int size = sparseBooleanArray.size() - 1;
                while (true) {
                    if (size < 0) {
                        z = false;
                        break;
                    } else {
                        if (sparseBooleanArray.valueAt(size)) {
                            z = true;
                            break;
                        }
                        size--;
                    }
                }
                sparseBooleanArray.setValueAt(indexOfKey, true);
                if (!z) {
                    notifyListenersOnStateChange(i, str, true, SystemClock.elapsedRealtime(), 8);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleNotificationRemoved(String str, int i, int i2) {
        int indexOfKey;
        synchronized (this.mLock) {
            SparseBooleanArray sparseBooleanArray = this.mFGSNotificationIDs.get(i, str);
            if (sparseBooleanArray != null && (indexOfKey = sparseBooleanArray.indexOfKey(i2)) >= 0) {
                if (sparseBooleanArray.valueAt(indexOfKey)) {
                    sparseBooleanArray.setValueAt(indexOfKey, false);
                    for (int size = sparseBooleanArray.size() - 1; size >= 0; size--) {
                        if (sparseBooleanArray.valueAt(size)) {
                            return;
                        }
                    }
                    notifyListenersOnStateChange(i, str, false, SystemClock.elapsedRealtime(), 8);
                }
            }
        }
    }

    @GuardedBy({"mLock"})
    private void scheduleDurationCheckLocked(long j) {
        SparseArray map = this.mPkgEvents.getMap();
        long j2 = -1;
        for (int size = map.size() - 1; size >= 0; size--) {
            ArrayMap arrayMap = (ArrayMap) map.valueAt(size);
            for (int size2 = arrayMap.size() - 1; size2 >= 0; size2--) {
                PackageDurations packageDurations = (PackageDurations) arrayMap.valueAt(size2);
                if (packageDurations.hasForegroundServices() && !packageDurations.isLongRunning()) {
                    j2 = Math.max(getTotalDurations(packageDurations, j), j2);
                }
            }
        }
        this.mHandler.removeMessages(4);
        if (j2 >= 0) {
            this.mHandler.sendEmptyMessageDelayed(4, this.mInjector.getServiceStartForegroundTimeout() + Math.max(0L, ((AppFGSPolicy) this.mInjector.getPolicy()).getFgsLongRunningThreshold() - j2));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void checkLongRunningFgs() {
        AppFGSPolicy appFGSPolicy = (AppFGSPolicy) this.mInjector.getPolicy();
        final ArrayMap<PackageDurations, Long> arrayMap = this.mTmpPkgDurations;
        long elapsedRealtime = SystemClock.elapsedRealtime();
        long fgsLongRunningThreshold = appFGSPolicy.getFgsLongRunningThreshold();
        long max = Math.max(0L, elapsedRealtime - appFGSPolicy.getFgsLongRunningWindowSize());
        synchronized (this.mLock) {
            SparseArray map = this.mPkgEvents.getMap();
            int i = 1;
            int size = map.size() - 1;
            while (size >= 0) {
                ArrayMap arrayMap2 = (ArrayMap) map.valueAt(size);
                for (int size2 = arrayMap2.size() - i; size2 >= 0; size2--) {
                    PackageDurations packageDurations = (PackageDurations) arrayMap2.valueAt(size2);
                    if (packageDurations.hasForegroundServices() && !packageDurations.isLongRunning()) {
                        long totalDurations = getTotalDurations(packageDurations, elapsedRealtime);
                        if (totalDurations >= fgsLongRunningThreshold) {
                            arrayMap.put(packageDurations, Long.valueOf(totalDurations));
                            packageDurations.setIsLongRunning(true);
                        }
                    }
                }
                size--;
                i = 1;
            }
            trim(max);
        }
        int size3 = arrayMap.size();
        if (size3 > 0) {
            Integer[] numArr = new Integer[size3];
            for (int i2 = 0; i2 < size3; i2++) {
                numArr[i2] = Integer.valueOf(i2);
            }
            Arrays.sort(numArr, new Comparator() { // from class: com.android.server.am.AppFGSTracker$$ExternalSyntheticLambda0
                @Override // java.util.Comparator
                public final int compare(Object obj, Object obj2) {
                    int lambda$checkLongRunningFgs$0;
                    lambda$checkLongRunningFgs$0 = AppFGSTracker.lambda$checkLongRunningFgs$0(arrayMap, (Integer) obj, (Integer) obj2);
                    return lambda$checkLongRunningFgs$0;
                }
            });
            for (int i3 = size3 - 1; i3 >= 0; i3--) {
                PackageDurations keyAt = arrayMap.keyAt(numArr[i3].intValue());
                appFGSPolicy.onLongRunningFgs(keyAt.mPackageName, keyAt.mUid, keyAt.mExemptReason);
            }
            arrayMap.clear();
        }
        synchronized (this.mLock) {
            scheduleDurationCheckLocked(elapsedRealtime);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ int lambda$checkLongRunningFgs$0(ArrayMap arrayMap, Integer num, Integer num2) {
        return Long.compare(((Long) arrayMap.valueAt(num.intValue())).longValue(), ((Long) arrayMap.valueAt(num2.intValue())).longValue());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleForegroundServicesChanged(String str, int i, int i2) {
        if (((AppFGSPolicy) this.mInjector.getPolicy()).isEnabled()) {
            int shouldExemptUid = ((AppFGSPolicy) this.mInjector.getPolicy()).shouldExemptUid(i);
            long elapsedRealtime = SystemClock.elapsedRealtime();
            synchronized (this.mLock) {
                PackageDurations packageDurations = (PackageDurations) this.mPkgEvents.get(i, str);
                if (packageDurations == null) {
                    packageDurations = new PackageDurations(i, str, (BaseAppStateEvents.MaxTrackingDurationConfig) this.mInjector.getPolicy(), this);
                    this.mPkgEvents.put(i, str, packageDurations);
                }
                packageDurations.setForegroundServiceType(i2, elapsedRealtime);
                packageDurations.mExemptReason = shouldExemptUid;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onBgFgsMonitorEnabled(boolean z) {
        if (z) {
            synchronized (this.mLock) {
                scheduleDurationCheckLocked(SystemClock.elapsedRealtime());
            }
            try {
                this.mNotificationListener.registerAsSystemService(this.mContext, new ComponentName(this.mContext, (Class<?>) NotificationListener.class), -1);
                return;
            } catch (RemoteException unused) {
                return;
            }
        }
        try {
            this.mNotificationListener.unregisterAsSystemService();
        } catch (RemoteException unused2) {
        }
        this.mHandler.removeMessages(4);
        synchronized (this.mLock) {
            this.mPkgEvents.clear();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onBgFgsLongRunningThresholdChanged() {
        synchronized (this.mLock) {
            if (((AppFGSPolicy) this.mInjector.getPolicy()).isEnabled()) {
                scheduleDurationCheckLocked(SystemClock.elapsedRealtime());
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int foregroundServiceTypeToIndex(int i) {
        if (i == 0) {
            return 0;
        }
        return Integer.numberOfTrailingZeros(i) + 1;
    }

    static int indexToForegroundServiceType(int i) {
        if (i == PackageDurations.DEFAULT_INDEX) {
            return 0;
        }
        return 1 << (i - 1);
    }

    long getTotalDurations(PackageDurations packageDurations, long j) {
        return getTotalDurations(packageDurations.mPackageName, packageDurations.mUid, j, foregroundServiceTypeToIndex(0));
    }

    @Override // com.android.server.am.BaseAppStateDurationsTracker
    long getTotalDurations(int i, long j) {
        return getTotalDurations(i, j, foregroundServiceTypeToIndex(0));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean hasForegroundServices(String str, int i) {
        boolean z;
        synchronized (this.mLock) {
            PackageDurations packageDurations = (PackageDurations) this.mPkgEvents.get(i, str);
            z = packageDurations != null && packageDurations.hasForegroundServices();
        }
        return z;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean hasForegroundServices(int i) {
        synchronized (this.mLock) {
            ArrayMap arrayMap = (ArrayMap) this.mPkgEvents.getMap().get(i);
            if (arrayMap != null) {
                for (int size = arrayMap.size() - 1; size >= 0; size--) {
                    if (((PackageDurations) arrayMap.valueAt(size)).hasForegroundServices()) {
                        return true;
                    }
                }
            }
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean hasForegroundServiceNotifications(String str, int i) {
        boolean hasForegroundServiceNotificationsLocked;
        synchronized (this.mLock) {
            hasForegroundServiceNotificationsLocked = hasForegroundServiceNotificationsLocked(str, i);
        }
        return hasForegroundServiceNotificationsLocked;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean hasForegroundServiceNotifications(int i) {
        synchronized (this.mLock) {
            ArrayMap<String, SparseBooleanArray> arrayMap = this.mFGSNotificationIDs.getMap().get(i);
            if (arrayMap != null) {
                for (int size = arrayMap.size() - 1; size >= 0; size--) {
                    if (hasForegroundServiceNotificationsLocked(arrayMap.keyAt(size), i)) {
                        return true;
                    }
                }
            }
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.am.BaseAppStateTracker
    public byte[] getTrackerInfoForStatsd(int i) {
        long totalDurations = getTotalDurations(i, SystemClock.elapsedRealtime());
        if (totalDurations == 0) {
            return null;
        }
        ProtoOutputStream protoOutputStream = new ProtoOutputStream();
        protoOutputStream.write(1133871366145L, hasForegroundServiceNotifications(i));
        protoOutputStream.write(1112396529666L, totalDurations);
        protoOutputStream.flush();
        return protoOutputStream.getBytes();
    }

    @Override // com.android.server.am.BaseAppStateEventsTracker, com.android.server.am.BaseAppStateTracker
    void dump(PrintWriter printWriter, String str) {
        printWriter.print(str);
        printWriter.println("APP FOREGROUND SERVICE TRACKER:");
        super.dump(printWriter, "  " + str);
    }

    @Override // com.android.server.am.BaseAppStateEventsTracker
    void dumpOthers(PrintWriter printWriter, String str) {
        printWriter.print(str);
        printWriter.println("APPS WITH ACTIVE FOREGROUND SERVICES:");
        String str2 = "  " + str;
        synchronized (this.mLock) {
            SparseArray<ArrayMap<String, SparseBooleanArray>> map = this.mFGSNotificationIDs.getMap();
            if (map.size() == 0) {
                printWriter.print(str2);
                printWriter.println("(none)");
            }
            int size = map.size();
            for (int i = 0; i < size; i++) {
                int keyAt = map.keyAt(i);
                String formatUid = UserHandle.formatUid(keyAt);
                ArrayMap<String, SparseBooleanArray> valueAt = map.valueAt(i);
                int size2 = valueAt.size();
                for (int i2 = 0; i2 < size2; i2++) {
                    String keyAt2 = valueAt.keyAt(i2);
                    printWriter.print(str2);
                    printWriter.print(keyAt2);
                    printWriter.print('/');
                    printWriter.print(formatUid);
                    printWriter.print(" notification=");
                    printWriter.println(hasForegroundServiceNotificationsLocked(keyAt2, keyAt));
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static class PackageDurations extends BaseAppStateDurations<BaseAppStateTimeEvents.BaseTimeEvent> {
        static final int DEFAULT_INDEX = AppFGSTracker.foregroundServiceTypeToIndex(0);
        private int mForegroundServiceTypes;
        private boolean mIsLongRunning;
        private final AppFGSTracker mTracker;

        /* JADX WARN: Multi-variable type inference failed */
        PackageDurations(int i, String str, BaseAppStateEvents.MaxTrackingDurationConfig maxTrackingDurationConfig, AppFGSTracker appFGSTracker) {
            super(i, str, 31, "ActivityManager", maxTrackingDurationConfig);
            this.mEvents[DEFAULT_INDEX] = new LinkedList();
            this.mTracker = appFGSTracker;
        }

        PackageDurations(PackageDurations packageDurations) {
            super(packageDurations);
            this.mIsLongRunning = packageDurations.mIsLongRunning;
            this.mForegroundServiceTypes = packageDurations.mForegroundServiceTypes;
            this.mTracker = packageDurations.mTracker;
        }

        void addEvent(boolean z, long j) {
            addEvent(z, (boolean) new BaseAppStateTimeEvents.BaseTimeEvent(j), DEFAULT_INDEX);
            if (!z && !hasForegroundServices()) {
                this.mIsLongRunning = false;
            }
            if (z || this.mForegroundServiceTypes == 0) {
                return;
            }
            int i = 1;
            while (true) {
                Object[] objArr = this.mEvents;
                if (i < objArr.length) {
                    if (objArr[i] != null && isActive(i)) {
                        this.mEvents[i].add(new BaseAppStateTimeEvents.BaseTimeEvent(j));
                        notifyListenersOnStateChangeIfNecessary(false, j, AppFGSTracker.indexToForegroundServiceType(i));
                    }
                    i++;
                } else {
                    this.mForegroundServiceTypes = 0;
                    return;
                }
            }
        }

        void setForegroundServiceType(int i, long j) {
            if (i == this.mForegroundServiceTypes || !hasForegroundServices()) {
                return;
            }
            int i2 = this.mForegroundServiceTypes ^ i;
            int highestOneBit = Integer.highestOneBit(i2);
            while (highestOneBit != 0) {
                int foregroundServiceTypeToIndex = AppFGSTracker.foregroundServiceTypeToIndex(highestOneBit);
                Object[] objArr = this.mEvents;
                if (foregroundServiceTypeToIndex < objArr.length) {
                    if ((i & highestOneBit) != 0) {
                        if (objArr[foregroundServiceTypeToIndex] == null) {
                            objArr[foregroundServiceTypeToIndex] = new LinkedList();
                        }
                        if (!isActive(foregroundServiceTypeToIndex)) {
                            this.mEvents[foregroundServiceTypeToIndex].add(new BaseAppStateTimeEvents.BaseTimeEvent(j));
                            notifyListenersOnStateChangeIfNecessary(true, j, highestOneBit);
                        }
                    } else if (objArr[foregroundServiceTypeToIndex] != null && isActive(foregroundServiceTypeToIndex)) {
                        this.mEvents[foregroundServiceTypeToIndex].add(new BaseAppStateTimeEvents.BaseTimeEvent(j));
                        notifyListenersOnStateChangeIfNecessary(false, j, highestOneBit);
                    }
                }
                i2 &= ~highestOneBit;
                highestOneBit = Integer.highestOneBit(i2);
            }
            this.mForegroundServiceTypes = i;
        }

        private void notifyListenersOnStateChangeIfNecessary(boolean z, long j, int i) {
            int i2 = 2;
            if (i != 2) {
                if (i != 8) {
                    return;
                } else {
                    i2 = 4;
                }
            }
            this.mTracker.notifyListenersOnStateChange(this.mUid, this.mPackageName, z, j, i2);
        }

        void setIsLongRunning(boolean z) {
            this.mIsLongRunning = z;
        }

        boolean isLongRunning() {
            return this.mIsLongRunning;
        }

        boolean hasForegroundServices() {
            return isActive(DEFAULT_INDEX);
        }

        @Override // com.android.server.am.BaseAppStateEvents
        String formatEventTypeLabel(int i) {
            if (i == DEFAULT_INDEX) {
                return "Overall foreground services: ";
            }
            return ServiceInfo.foregroundServiceTypeToLabel(AppFGSTracker.indexToForegroundServiceType(i)) + ": ";
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @VisibleForTesting
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public class NotificationListener extends NotificationListenerService {
        NotificationListener() {
        }

        @Override // android.service.notification.NotificationListenerService
        public void onNotificationPosted(StatusBarNotification statusBarNotification, NotificationListenerService.RankingMap rankingMap) {
            AppFGSTracker.this.mHandler.obtainMessage(5, statusBarNotification.getUid(), statusBarNotification.getId(), statusBarNotification.getPackageName()).sendToTarget();
        }

        @Override // android.service.notification.NotificationListenerService
        public void onNotificationRemoved(StatusBarNotification statusBarNotification, NotificationListenerService.RankingMap rankingMap, int i) {
            AppFGSTracker.this.mHandler.obtainMessage(6, statusBarNotification.getUid(), statusBarNotification.getId(), statusBarNotification.getPackageName()).sendToTarget();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static final class AppFGSPolicy extends BaseAppStateEventsTracker.BaseAppStateEventsPolicy<AppFGSTracker> {
        static final long DEFAULT_BG_FGS_LOCATION_THRESHOLD = 14400000;
        static final long DEFAULT_BG_FGS_LONG_RUNNING_THRESHOLD = 72000000;
        static final long DEFAULT_BG_FGS_LONG_RUNNING_WINDOW = 86400000;
        static final long DEFAULT_BG_FGS_MEDIA_PLAYBACK_THRESHOLD = 14400000;
        static final boolean DEFAULT_BG_FGS_MONITOR_ENABLED = true;
        static final String KEY_BG_FGS_LOCATION_THRESHOLD = "bg_fgs_location_threshold";
        static final String KEY_BG_FGS_LONG_RUNNING_THRESHOLD = "bg_fgs_long_running_threshold";
        static final String KEY_BG_FGS_LONG_RUNNING_WINDOW = "bg_fgs_long_running_window";
        static final String KEY_BG_FGS_MEDIA_PLAYBACK_THRESHOLD = "bg_fgs_media_playback_threshold";
        static final String KEY_BG_FGS_MONITOR_ENABLED = "bg_fgs_monitor_enabled";
        private volatile long mBgFgsLocationThresholdMs;
        private volatile long mBgFgsLongRunningThresholdMs;
        private volatile long mBgFgsMediaPlaybackThresholdMs;

        AppFGSPolicy(BaseAppStateTracker.Injector injector, AppFGSTracker appFGSTracker) {
            super(injector, appFGSTracker, KEY_BG_FGS_MONITOR_ENABLED, true, KEY_BG_FGS_LONG_RUNNING_WINDOW, 86400000L);
            this.mBgFgsLongRunningThresholdMs = DEFAULT_BG_FGS_LONG_RUNNING_THRESHOLD;
            this.mBgFgsMediaPlaybackThresholdMs = BackupManagerConstants.DEFAULT_KEY_VALUE_BACKUP_INTERVAL_MILLISECONDS;
            this.mBgFgsLocationThresholdMs = BackupManagerConstants.DEFAULT_KEY_VALUE_BACKUP_INTERVAL_MILLISECONDS;
        }

        @Override // com.android.server.am.BaseAppStateEventsTracker.BaseAppStateEventsPolicy, com.android.server.am.BaseAppStatePolicy
        public void onSystemReady() {
            super.onSystemReady();
            updateBgFgsLongRunningThreshold();
            updateBgFgsMediaPlaybackThreshold();
            updateBgFgsLocationThreshold();
        }

        @Override // com.android.server.am.BaseAppStateEventsTracker.BaseAppStateEventsPolicy, com.android.server.am.BaseAppStatePolicy
        public void onPropertiesChanged(String str) {
            str.hashCode();
            char c = 65535;
            switch (str.hashCode()) {
                case -2001687768:
                    if (str.equals(KEY_BG_FGS_LOCATION_THRESHOLD)) {
                        c = 0;
                        break;
                    }
                    break;
                case 351955503:
                    if (str.equals(KEY_BG_FGS_LONG_RUNNING_THRESHOLD)) {
                        c = 1;
                        break;
                    }
                    break;
                case 803245321:
                    if (str.equals(KEY_BG_FGS_MEDIA_PLAYBACK_THRESHOLD)) {
                        c = 2;
                        break;
                    }
                    break;
            }
            switch (c) {
                case 0:
                    updateBgFgsLocationThreshold();
                    return;
                case 1:
                    updateBgFgsLongRunningThreshold();
                    return;
                case 2:
                    updateBgFgsMediaPlaybackThreshold();
                    return;
                default:
                    super.onPropertiesChanged(str);
                    return;
            }
        }

        @Override // com.android.server.am.BaseAppStatePolicy
        public void onTrackerEnabled(boolean z) {
            ((AppFGSTracker) this.mTracker).onBgFgsMonitorEnabled(z);
        }

        @Override // com.android.server.am.BaseAppStateEventsTracker.BaseAppStateEventsPolicy
        public void onMaxTrackingDurationChanged(long j) {
            ((AppFGSTracker) this.mTracker).onBgFgsLongRunningThresholdChanged();
        }

        private void updateBgFgsLongRunningThreshold() {
            long j = DeviceConfig.getLong("activity_manager", KEY_BG_FGS_LONG_RUNNING_THRESHOLD, DEFAULT_BG_FGS_LONG_RUNNING_THRESHOLD);
            if (j != this.mBgFgsLongRunningThresholdMs) {
                this.mBgFgsLongRunningThresholdMs = j;
                ((AppFGSTracker) this.mTracker).onBgFgsLongRunningThresholdChanged();
            }
        }

        private void updateBgFgsMediaPlaybackThreshold() {
            this.mBgFgsMediaPlaybackThresholdMs = DeviceConfig.getLong("activity_manager", KEY_BG_FGS_MEDIA_PLAYBACK_THRESHOLD, BackupManagerConstants.DEFAULT_KEY_VALUE_BACKUP_INTERVAL_MILLISECONDS);
        }

        private void updateBgFgsLocationThreshold() {
            this.mBgFgsLocationThresholdMs = DeviceConfig.getLong("activity_manager", KEY_BG_FGS_LOCATION_THRESHOLD, BackupManagerConstants.DEFAULT_KEY_VALUE_BACKUP_INTERVAL_MILLISECONDS);
        }

        long getFgsLongRunningThreshold() {
            return this.mBgFgsLongRunningThresholdMs;
        }

        long getFgsLongRunningWindowSize() {
            return getMaxTrackingDuration();
        }

        long getFGSMediaPlaybackThreshold() {
            return this.mBgFgsMediaPlaybackThresholdMs;
        }

        long getLocationFGSThreshold() {
            return this.mBgFgsLocationThresholdMs;
        }

        void onLongRunningFgs(String str, int i, int i2) {
            if (i2 != -1) {
                return;
            }
            long elapsedRealtime = SystemClock.elapsedRealtime();
            long fgsLongRunningWindowSize = getFgsLongRunningWindowSize();
            long max = Math.max(0L, elapsedRealtime - fgsLongRunningWindowSize);
            if (shouldExemptMediaPlaybackFGS(str, i, elapsedRealtime, fgsLongRunningWindowSize) || shouldExemptLocationFGS(str, i, elapsedRealtime, max)) {
                return;
            }
            ((AppFGSTracker) this.mTracker).mAppRestrictionController.postLongRunningFgsIfNecessary(str, i);
        }

        boolean shouldExemptMediaPlaybackFGS(String str, int i, long j, long j2) {
            long compositeMediaPlaybackDurations = ((AppFGSTracker) this.mTracker).mAppRestrictionController.getCompositeMediaPlaybackDurations(str, i, j, j2);
            return compositeMediaPlaybackDurations > 0 && compositeMediaPlaybackDurations >= getFGSMediaPlaybackThreshold();
        }

        boolean shouldExemptLocationFGS(String str, int i, long j, long j2) {
            long foregroundServiceTotalDurationsSince = ((AppFGSTracker) this.mTracker).mAppRestrictionController.getForegroundServiceTotalDurationsSince(str, i, j2, j, 8);
            return foregroundServiceTotalDurationsSince > 0 && foregroundServiceTotalDurationsSince >= getLocationFGSThreshold();
        }

        @Override // com.android.server.am.BaseAppStateEventsTracker.BaseAppStateEventsPolicy
        String getExemptionReasonString(String str, int i, int i2) {
            if (i2 != -1) {
                return super.getExemptionReasonString(str, i, i2);
            }
            long elapsedRealtime = SystemClock.elapsedRealtime();
            return "{mediaPlayback=" + shouldExemptMediaPlaybackFGS(str, i, elapsedRealtime, getFgsLongRunningWindowSize()) + ", location=" + shouldExemptLocationFGS(str, i, elapsedRealtime, Math.max(0L, elapsedRealtime - getFgsLongRunningWindowSize())) + "}";
        }

        void onLongRunningFgsGone(String str, int i) {
            ((AppFGSTracker) this.mTracker).mAppRestrictionController.cancelLongRunningFGSNotificationIfNecessary(str, i);
        }

        @Override // com.android.server.am.BaseAppStateEventsTracker.BaseAppStateEventsPolicy, com.android.server.am.BaseAppStatePolicy
        void dump(PrintWriter printWriter, String str) {
            printWriter.print(str);
            printWriter.println("APP FOREGROUND SERVICE TRACKER POLICY SETTINGS:");
            String str2 = "  " + str;
            super.dump(printWriter, str2);
            if (isEnabled()) {
                printWriter.print(str2);
                printWriter.print(KEY_BG_FGS_LONG_RUNNING_THRESHOLD);
                printWriter.print('=');
                printWriter.println(this.mBgFgsLongRunningThresholdMs);
                printWriter.print(str2);
                printWriter.print(KEY_BG_FGS_MEDIA_PLAYBACK_THRESHOLD);
                printWriter.print('=');
                printWriter.println(this.mBgFgsMediaPlaybackThresholdMs);
                printWriter.print(str2);
                printWriter.print(KEY_BG_FGS_LOCATION_THRESHOLD);
                printWriter.print('=');
                printWriter.println(this.mBgFgsLocationThresholdMs);
            }
        }
    }
}
