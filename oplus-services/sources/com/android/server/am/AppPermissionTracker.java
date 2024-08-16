package com.android.server.am;

import android.app.AppOpsManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManagerInternal;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.os.SystemClock;
import android.os.UserHandle;
import android.permission.PermissionManager;
import android.provider.DeviceConfig;
import android.text.TextUtils;
import android.util.ArraySet;
import android.util.Pair;
import android.util.SparseArray;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.app.IAppOpsCallback;
import com.android.internal.app.IAppOpsService;
import com.android.server.am.AppRestrictionController;
import com.android.server.am.BaseAppStateTracker;
import java.io.PrintWriter;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public final class AppPermissionTracker extends BaseAppStateTracker<AppPermissionPolicy> implements PackageManager.OnPermissionsChangedListener {
    static final boolean DEBUG_PERMISSION_TRACKER = false;
    static final String TAG = "ActivityManager";

    @GuardedBy({"mAppOpsCallbacks"})
    private final SparseArray<MyAppOpsCallback> mAppOpsCallbacks;
    private final MyHandler mHandler;
    private volatile boolean mLockedBootCompleted;

    @GuardedBy({"mLock"})
    private SparseArray<ArraySet<UidGrantedPermissionState>> mUidGrantedPermissionsInMonitor;

    @Override // com.android.server.am.BaseAppStateTracker
    @AppRestrictionController.TrackerType
    int getType() {
        return 5;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public AppPermissionTracker(Context context, AppRestrictionController appRestrictionController) {
        this(context, appRestrictionController, null, null);
    }

    AppPermissionTracker(Context context, AppRestrictionController appRestrictionController, Constructor<? extends BaseAppStateTracker.Injector<AppPermissionPolicy>> constructor, Object obj) {
        super(context, appRestrictionController, constructor, obj);
        this.mAppOpsCallbacks = new SparseArray<>();
        this.mUidGrantedPermissionsInMonitor = new SparseArray<>();
        this.mLockedBootCompleted = false;
        this.mHandler = new MyHandler(this);
        this.mInjector.setPolicy(new AppPermissionPolicy(this.mInjector, this));
    }

    public void onPermissionsChanged(int i) {
        this.mHandler.obtainMessage(2, i, 0).sendToTarget();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleAppOpsInit() {
        ArrayList arrayList = new ArrayList();
        for (Pair pair : ((AppPermissionPolicy) this.mInjector.getPolicy()).getBgPermissionsInMonitor()) {
            if (((Integer) pair.second).intValue() != -1) {
                arrayList.add((Integer) pair.second);
            }
        }
        startWatchingMode((Integer[]) arrayList.toArray(new Integer[arrayList.size()]));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handlePermissionsInit() {
        int i;
        int i2;
        ApplicationInfo applicationInfo;
        int i3;
        int[] userIds = this.mInjector.getUserManagerInternal().getUserIds();
        PackageManagerInternal packageManagerInternal = this.mInjector.getPackageManagerInternal();
        this.mInjector.getPermissionManagerServiceInternal();
        Pair[] bgPermissionsInMonitor = ((AppPermissionPolicy) this.mInjector.getPolicy()).getBgPermissionsInMonitor();
        SparseArray<ArraySet<UidGrantedPermissionState>> sparseArray = this.mUidGrantedPermissionsInMonitor;
        for (int i4 : userIds) {
            List<ApplicationInfo> installedApplications = packageManagerInternal.getInstalledApplications(0L, i4, 1000);
            if (installedApplications != null) {
                long elapsedRealtime = SystemClock.elapsedRealtime();
                int size = installedApplications.size();
                int i5 = 0;
                while (i5 < size) {
                    ApplicationInfo applicationInfo2 = installedApplications.get(i5);
                    int length = bgPermissionsInMonitor.length;
                    int i6 = 0;
                    while (i6 < length) {
                        Pair pair = bgPermissionsInMonitor[i6];
                        int i7 = i6;
                        UidGrantedPermissionState uidGrantedPermissionState = new UidGrantedPermissionState(applicationInfo2.uid, (String) pair.first, ((Integer) pair.second).intValue());
                        if (uidGrantedPermissionState.isGranted()) {
                            synchronized (this.mLock) {
                                ArraySet<UidGrantedPermissionState> arraySet = sparseArray.get(applicationInfo2.uid);
                                if (arraySet == null) {
                                    ArraySet<UidGrantedPermissionState> arraySet2 = new ArraySet<>();
                                    sparseArray.put(applicationInfo2.uid, arraySet2);
                                    i = length;
                                    i2 = i5;
                                    applicationInfo = applicationInfo2;
                                    i3 = size;
                                    notifyListenersOnStateChange(applicationInfo2.uid, "", true, elapsedRealtime, 16);
                                    arraySet = arraySet2;
                                    uidGrantedPermissionState = uidGrantedPermissionState;
                                } else {
                                    i = length;
                                    i2 = i5;
                                    applicationInfo = applicationInfo2;
                                    i3 = size;
                                }
                                arraySet.add(uidGrantedPermissionState);
                            }
                        } else {
                            i = length;
                            i2 = i5;
                            applicationInfo = applicationInfo2;
                            i3 = size;
                        }
                        i6 = i7 + 1;
                        length = i;
                        i5 = i2;
                        applicationInfo2 = applicationInfo;
                        size = i3;
                    }
                    i5++;
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleAppOpsDestroy() {
        stopWatchingMode();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handlePermissionsDestroy() {
        synchronized (this.mLock) {
            SparseArray<ArraySet<UidGrantedPermissionState>> sparseArray = this.mUidGrantedPermissionsInMonitor;
            long elapsedRealtime = SystemClock.elapsedRealtime();
            int size = sparseArray.size();
            for (int i = 0; i < size; i++) {
                int keyAt = sparseArray.keyAt(i);
                if (sparseArray.valueAt(i).size() > 0) {
                    notifyListenersOnStateChange(keyAt, "", false, elapsedRealtime, 16);
                }
            }
            sparseArray.clear();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleOpChanged(int i, int i2, String str) {
        Pair[] bgPermissionsInMonitor = ((AppPermissionPolicy) this.mInjector.getPolicy()).getBgPermissionsInMonitor();
        if (bgPermissionsInMonitor == null || bgPermissionsInMonitor.length <= 0) {
            return;
        }
        for (Pair pair : bgPermissionsInMonitor) {
            if (((Integer) pair.second).intValue() == i) {
                UidGrantedPermissionState uidGrantedPermissionState = new UidGrantedPermissionState(i2, (String) pair.first, i);
                synchronized (this.mLock) {
                    handlePermissionsChangedLocked(i2, new UidGrantedPermissionState[]{uidGrantedPermissionState});
                }
                return;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handlePermissionsChanged(int i) {
        Pair[] bgPermissionsInMonitor = ((AppPermissionPolicy) this.mInjector.getPolicy()).getBgPermissionsInMonitor();
        if (bgPermissionsInMonitor == null || bgPermissionsInMonitor.length <= 0) {
            return;
        }
        this.mInjector.getPermissionManagerServiceInternal();
        UidGrantedPermissionState[] uidGrantedPermissionStateArr = new UidGrantedPermissionState[bgPermissionsInMonitor.length];
        for (int i2 = 0; i2 < bgPermissionsInMonitor.length; i2++) {
            Pair pair = bgPermissionsInMonitor[i2];
            uidGrantedPermissionStateArr[i2] = new UidGrantedPermissionState(i, (String) pair.first, ((Integer) pair.second).intValue());
        }
        synchronized (this.mLock) {
            handlePermissionsChangedLocked(i, uidGrantedPermissionStateArr);
        }
    }

    @GuardedBy({"mLock"})
    private void handlePermissionsChangedLocked(int i, UidGrantedPermissionState[] uidGrantedPermissionStateArr) {
        int indexOfKey = this.mUidGrantedPermissionsInMonitor.indexOfKey(i);
        ArraySet<UidGrantedPermissionState> valueAt = indexOfKey >= 0 ? this.mUidGrantedPermissionsInMonitor.valueAt(indexOfKey) : null;
        long elapsedRealtime = SystemClock.elapsedRealtime();
        for (int i2 = 0; i2 < uidGrantedPermissionStateArr.length; i2++) {
            boolean isGranted = uidGrantedPermissionStateArr[i2].isGranted();
            boolean z = true;
            if (isGranted) {
                if (valueAt == null) {
                    valueAt = new ArraySet<>();
                    this.mUidGrantedPermissionsInMonitor.put(i, valueAt);
                } else {
                    z = false;
                }
                valueAt.add(uidGrantedPermissionStateArr[i2]);
            } else if (valueAt == null || valueAt.isEmpty() || !valueAt.remove(uidGrantedPermissionStateArr[i2]) || !valueAt.isEmpty()) {
                z = false;
            } else {
                this.mUidGrantedPermissionsInMonitor.removeAt(indexOfKey);
            }
            if (z) {
                notifyListenersOnStateChange(i, "", isGranted, elapsedRealtime, 16);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public class UidGrantedPermissionState {
        final int mAppOp;
        private boolean mAppOpAllowed;
        final String mPermission;
        private boolean mPermissionGranted;
        final int mUid;

        UidGrantedPermissionState(int i, String str, int i2) {
            this.mUid = i;
            this.mPermission = str;
            this.mAppOp = i2;
            updatePermissionState();
            updateAppOps();
        }

        void updatePermissionState() {
            if (TextUtils.isEmpty(this.mPermission)) {
                this.mPermissionGranted = true;
            } else {
                this.mPermissionGranted = AppPermissionTracker.this.mInjector.getPermissionManagerServiceInternal().checkUidPermission(this.mUid, this.mPermission) == 0;
            }
        }

        void updateAppOps() {
            if (this.mAppOp == -1) {
                this.mAppOpAllowed = true;
                return;
            }
            String[] packagesForUid = AppPermissionTracker.this.mInjector.getPackageManager().getPackagesForUid(this.mUid);
            if (packagesForUid != null) {
                IAppOpsService iAppOpsService = AppPermissionTracker.this.mInjector.getIAppOpsService();
                for (String str : packagesForUid) {
                    if (iAppOpsService.checkOperation(this.mAppOp, this.mUid, str) == 0) {
                        this.mAppOpAllowed = true;
                        return;
                    }
                    continue;
                }
            }
            this.mAppOpAllowed = false;
        }

        boolean isGranted() {
            return this.mPermissionGranted && this.mAppOpAllowed;
        }

        public boolean equals(Object obj) {
            if (obj == null || !(obj instanceof UidGrantedPermissionState)) {
                return false;
            }
            UidGrantedPermissionState uidGrantedPermissionState = (UidGrantedPermissionState) obj;
            return this.mUid == uidGrantedPermissionState.mUid && this.mAppOp == uidGrantedPermissionState.mAppOp && Objects.equals(this.mPermission, uidGrantedPermissionState.mPermission);
        }

        public int hashCode() {
            int hashCode = ((Integer.hashCode(this.mUid) * 31) + Integer.hashCode(this.mAppOp)) * 31;
            String str = this.mPermission;
            return hashCode + (str == null ? 0 : str.hashCode());
        }

        public String toString() {
            String str = "UidGrantedPermissionState{" + System.identityHashCode(this) + " " + UserHandle.formatUid(this.mUid) + ": ";
            boolean isEmpty = TextUtils.isEmpty(this.mPermission);
            if (!isEmpty) {
                str = str + this.mPermission + "=" + this.mPermissionGranted;
            }
            if (this.mAppOp != -1) {
                if (!isEmpty) {
                    str = str + ",";
                }
                str = str + AppOpsManager.opToPublicName(this.mAppOp) + "=" + this.mAppOpAllowed;
            }
            return str + "}";
        }
    }

    private void startWatchingMode(Integer[] numArr) {
        synchronized (this.mAppOpsCallbacks) {
            stopWatchingMode();
            IAppOpsService iAppOpsService = this.mInjector.getIAppOpsService();
            try {
                for (Integer num : numArr) {
                    int intValue = num.intValue();
                    MyAppOpsCallback myAppOpsCallback = new MyAppOpsCallback();
                    this.mAppOpsCallbacks.put(intValue, myAppOpsCallback);
                    iAppOpsService.startWatchingModeWithFlags(intValue, (String) null, 1, myAppOpsCallback);
                }
            } catch (RemoteException unused) {
            }
        }
    }

    private void stopWatchingMode() {
        synchronized (this.mAppOpsCallbacks) {
            IAppOpsService iAppOpsService = this.mInjector.getIAppOpsService();
            for (int size = this.mAppOpsCallbacks.size() - 1; size >= 0; size--) {
                try {
                    iAppOpsService.stopWatchingMode(this.mAppOpsCallbacks.valueAt(size));
                } catch (RemoteException unused) {
                }
            }
            this.mAppOpsCallbacks.clear();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public class MyAppOpsCallback extends IAppOpsCallback.Stub {
        private MyAppOpsCallback() {
        }

        public void opChanged(int i, int i2, String str) {
            AppPermissionTracker.this.mHandler.obtainMessage(3, i, i2, str).sendToTarget();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static class MyHandler extends Handler {
        static final int MSG_APPOPS_CHANGED = 3;
        static final int MSG_PERMISSIONS_CHANGED = 2;
        static final int MSG_PERMISSIONS_DESTROY = 1;
        static final int MSG_PERMISSIONS_INIT = 0;
        private AppPermissionTracker mTracker;

        MyHandler(AppPermissionTracker appPermissionTracker) {
            super(appPermissionTracker.mBgHandler.getLooper());
            this.mTracker = appPermissionTracker;
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            int i = message.what;
            if (i == 0) {
                this.mTracker.handleAppOpsInit();
                this.mTracker.handlePermissionsInit();
            } else if (i == 1) {
                this.mTracker.handlePermissionsDestroy();
                this.mTracker.handleAppOpsDestroy();
            } else if (i == 2) {
                this.mTracker.handlePermissionsChanged(message.arg1);
            } else {
                if (i != 3) {
                    return;
                }
                this.mTracker.handleOpChanged(message.arg1, message.arg2, (String) message.obj);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onPermissionTrackerEnabled(boolean z) {
        if (this.mLockedBootCompleted) {
            PermissionManager permissionManager = this.mInjector.getPermissionManager();
            if (z) {
                permissionManager.addOnPermissionsChangeListener(this);
                this.mHandler.obtainMessage(0).sendToTarget();
            } else {
                permissionManager.removeOnPermissionsChangeListener(this);
                this.mHandler.obtainMessage(1).sendToTarget();
            }
        }
    }

    @Override // com.android.server.am.BaseAppStateTracker
    void onLockedBootCompleted() {
        this.mLockedBootCompleted = true;
        onPermissionTrackerEnabled(((AppPermissionPolicy) this.mInjector.getPolicy()).isEnabled());
    }

    @Override // com.android.server.am.BaseAppStateTracker
    void dump(PrintWriter printWriter, String str) {
        Pair[] pairArr;
        String str2;
        Pair[] pairArr2;
        String str3;
        AppPermissionTracker appPermissionTracker = this;
        printWriter.print(str);
        printWriter.println("APP PERMISSIONS TRACKER:");
        Pair[] bgPermissionsInMonitor = ((AppPermissionPolicy) appPermissionTracker.mInjector.getPolicy()).getBgPermissionsInMonitor();
        String str4 = "  " + str;
        String str5 = "  " + str4;
        int length = bgPermissionsInMonitor.length;
        int i = 0;
        while (i < length) {
            Pair pair = bgPermissionsInMonitor[i];
            printWriter.print(str4);
            boolean isEmpty = TextUtils.isEmpty((CharSequence) pair.first);
            if (!isEmpty) {
                printWriter.print((String) pair.first);
            }
            if (((Integer) pair.second).intValue() != -1) {
                if (!isEmpty) {
                    printWriter.print('+');
                }
                printWriter.print(AppOpsManager.opToPublicName(((Integer) pair.second).intValue()));
            }
            printWriter.println(':');
            synchronized (appPermissionTracker.mLock) {
                SparseArray<ArraySet<UidGrantedPermissionState>> sparseArray = appPermissionTracker.mUidGrantedPermissionsInMonitor;
                printWriter.print(str5);
                printWriter.print('[');
                int size = sparseArray.size();
                int i2 = 0;
                boolean z = false;
                while (i2 < size) {
                    ArraySet<UidGrantedPermissionState> valueAt = sparseArray.valueAt(i2);
                    int size2 = valueAt.size() - 1;
                    while (true) {
                        if (size2 < 0) {
                            pairArr2 = bgPermissionsInMonitor;
                            str3 = str4;
                            break;
                        }
                        UidGrantedPermissionState valueAt2 = valueAt.valueAt(size2);
                        pairArr2 = bgPermissionsInMonitor;
                        str3 = str4;
                        if (valueAt2.mAppOp == ((Integer) pair.second).intValue() && TextUtils.equals(valueAt2.mPermission, (CharSequence) pair.first)) {
                            if (z) {
                                printWriter.print(',');
                            }
                            printWriter.print(UserHandle.formatUid(valueAt2.mUid));
                            z = true;
                        } else {
                            size2--;
                            bgPermissionsInMonitor = pairArr2;
                            str4 = str3;
                        }
                    }
                    i2++;
                    bgPermissionsInMonitor = pairArr2;
                    str4 = str3;
                }
                pairArr = bgPermissionsInMonitor;
                str2 = str4;
                printWriter.println(']');
            }
            i++;
            appPermissionTracker = this;
            bgPermissionsInMonitor = pairArr;
            str4 = str2;
        }
        super.dump(printWriter, str);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static final class AppPermissionPolicy extends BaseAppStatePolicy<AppPermissionTracker> {
        static final String[] DEFAULT_BG_PERMISSIONS_IN_MONITOR = {"android.permission.ACCESS_FINE_LOCATION", "android:fine_location", "android.permission.CAMERA", "android:camera", "android.permission.RECORD_AUDIO", "android:record_audio"};
        static final boolean DEFAULT_BG_PERMISSION_MONITOR_ENABLED = true;
        static final String KEY_BG_PERMISSIONS_IN_MONITOR = "bg_permission_in_monitor";
        static final String KEY_BG_PERMISSION_MONITOR_ENABLED = "bg_permission_monitor_enabled";
        volatile Pair[] mBgPermissionsInMonitor;

        AppPermissionPolicy(BaseAppStateTracker.Injector injector, AppPermissionTracker appPermissionTracker) {
            super(injector, appPermissionTracker, KEY_BG_PERMISSION_MONITOR_ENABLED, true);
            this.mBgPermissionsInMonitor = parsePermissionConfig(DEFAULT_BG_PERMISSIONS_IN_MONITOR);
        }

        @Override // com.android.server.am.BaseAppStatePolicy
        public void onSystemReady() {
            super.onSystemReady();
            updateBgPermissionsInMonitor();
        }

        @Override // com.android.server.am.BaseAppStatePolicy
        public void onPropertiesChanged(String str) {
            str.hashCode();
            if (str.equals(KEY_BG_PERMISSIONS_IN_MONITOR)) {
                updateBgPermissionsInMonitor();
            } else {
                super.onPropertiesChanged(str);
            }
        }

        Pair[] getBgPermissionsInMonitor() {
            return this.mBgPermissionsInMonitor;
        }

        private Pair[] parsePermissionConfig(String[] strArr) {
            Pair[] pairArr = new Pair[strArr.length / 2];
            int i = 0;
            int i2 = 0;
            while (i < strArr.length) {
                try {
                    int i3 = i + 1;
                    pairArr[i2] = Pair.create(TextUtils.isEmpty(strArr[i]) ? null : strArr[i], Integer.valueOf(TextUtils.isEmpty(strArr[i3]) ? -1 : AppOpsManager.strOpToOp(strArr[i3])));
                } catch (Exception unused) {
                }
                i += 2;
                i2++;
            }
            return pairArr;
        }

        private void updateBgPermissionsInMonitor() {
            String string = DeviceConfig.getString("activity_manager", KEY_BG_PERMISSIONS_IN_MONITOR, (String) null);
            Pair[] parsePermissionConfig = parsePermissionConfig(string != null ? string.split(",") : DEFAULT_BG_PERMISSIONS_IN_MONITOR);
            if (Arrays.equals(this.mBgPermissionsInMonitor, parsePermissionConfig)) {
                return;
            }
            this.mBgPermissionsInMonitor = parsePermissionConfig;
            if (isEnabled()) {
                onTrackerEnabled(false);
                onTrackerEnabled(true);
            }
        }

        @Override // com.android.server.am.BaseAppStatePolicy
        public void onTrackerEnabled(boolean z) {
            ((AppPermissionTracker) this.mTracker).onPermissionTrackerEnabled(z);
        }

        @Override // com.android.server.am.BaseAppStatePolicy
        void dump(PrintWriter printWriter, String str) {
            printWriter.print(str);
            printWriter.println("APP PERMISSION TRACKER POLICY SETTINGS:");
            String str2 = "  " + str;
            super.dump(printWriter, str2);
            printWriter.print(str2);
            printWriter.print(KEY_BG_PERMISSIONS_IN_MONITOR);
            printWriter.print('=');
            printWriter.print('[');
            for (int i = 0; i < this.mBgPermissionsInMonitor.length; i++) {
                if (i > 0) {
                    printWriter.print(',');
                }
                Pair pair = this.mBgPermissionsInMonitor[i];
                Object obj = pair.first;
                if (obj != null) {
                    printWriter.print((String) obj);
                }
                printWriter.print(',');
                if (((Integer) pair.second).intValue() != -1) {
                    printWriter.print(AppOpsManager.opToPublicName(((Integer) pair.second).intValue()));
                }
            }
            printWriter.println(']');
        }
    }
}
