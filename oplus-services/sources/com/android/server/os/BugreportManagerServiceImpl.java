package com.android.server.os;

import android.annotation.RequiresPermission;
import android.app.ActivityManager;
import android.app.AppOpsManager;
import android.app.admin.DevicePolicyManager;
import android.content.Context;
import android.content.pm.UserInfo;
import android.os.Binder;
import android.os.IBinder;
import android.os.IDumpstate;
import android.os.IDumpstateListener;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.SystemClock;
import android.os.SystemProperties;
import android.os.UserHandle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.ArraySet;
import android.util.LocalLog;
import android.util.Pair;
import android.util.Slog;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.util.DumpUtils;
import com.android.server.SystemConfig;
import com.android.server.sensorprivacy.SensorPrivacyService;
import com.android.server.slice.SliceClientPermissions;
import com.android.server.utils.Slogf;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.Objects;
import java.util.OptionalInt;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class BugreportManagerServiceImpl extends IDumpstate.Stub {
    private static final String BUGREPORT_SERVICE = "bugreportd";
    private static final boolean DEBUG = false;
    private static final long DEFAULT_BUGREPORT_SERVICE_TIMEOUT_MILLIS = 30000;
    private static final int LOCAL_LOG_SIZE = 20;
    private static final String TAG = "BugreportManagerService";
    private final AppOpsManager mAppOps;
    private final ArraySet<String> mBugreportAllowlistedPackages;
    private final BugreportFileManager mBugreportFileManager;
    private final Context mContext;

    @GuardedBy({"mLock"})
    private DumpstateListener mCurrentDumpstateListener;

    @GuardedBy({"mLock"})
    private final LocalLog mFinishedBugreports;
    private final Object mLock;

    @GuardedBy({"mLock"})
    private int mNumberFinishedBugreports;

    @GuardedBy({"mLock"})
    private OptionalInt mPreDumpedDataUid;
    private final TelephonyManager mTelephonyManager;

    private int clearBugreportFlag(int i, int i2) {
        return (~i2) & i;
    }

    @VisibleForTesting(visibility = VisibleForTesting.Visibility.PRIVATE)
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    static class BugreportFileManager {
        private final Object mLock = new Object();

        @GuardedBy({"mLock"})
        private final ArrayMap<Pair<Integer, String>, ArraySet<String>> mBugreportFiles = new ArrayMap<>();

        BugreportFileManager() {
        }

        void ensureCallerPreviouslyGeneratedFile(Pair<Integer, String> pair, String str) {
            synchronized (this.mLock) {
                ArraySet<String> arraySet = this.mBugreportFiles.get(pair);
                if (arraySet != null && arraySet.contains(str)) {
                    arraySet.remove(str);
                    if (arraySet.isEmpty()) {
                        this.mBugreportFiles.remove(pair);
                    }
                } else {
                    throw new IllegalArgumentException("File " + str + " was not generated on behalf of calling package " + ((String) pair.second));
                }
            }
        }

        void addBugreportFileForCaller(Pair<Integer, String> pair, String str) {
            synchronized (this.mLock) {
                if (!this.mBugreportFiles.containsKey(pair)) {
                    this.mBugreportFiles.put(pair, new ArraySet<>());
                }
                this.mBugreportFiles.get(pair).add(str);
            }
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    static class Injector {
        ArraySet<String> mAllowlistedPackages;
        Context mContext;

        Injector(Context context, ArraySet<String> arraySet) {
            this.mContext = context;
            this.mAllowlistedPackages = arraySet;
        }

        Context getContext() {
            return this.mContext;
        }

        ArraySet<String> getAllowlistedPackages() {
            return this.mAllowlistedPackages;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public BugreportManagerServiceImpl(Context context) {
        this(new Injector(context, SystemConfig.getInstance().getBugreportWhitelistedPackages()));
    }

    @VisibleForTesting(visibility = VisibleForTesting.Visibility.PRIVATE)
    BugreportManagerServiceImpl(Injector injector) {
        this.mLock = new Object();
        this.mPreDumpedDataUid = OptionalInt.empty();
        this.mFinishedBugreports = new LocalLog(20);
        Context context = injector.getContext();
        this.mContext = context;
        this.mAppOps = (AppOpsManager) context.getSystemService(AppOpsManager.class);
        this.mTelephonyManager = (TelephonyManager) context.getSystemService(TelephonyManager.class);
        this.mBugreportFileManager = new BugreportFileManager();
        this.mBugreportAllowlistedPackages = injector.getAllowlistedPackages();
    }

    @RequiresPermission("android.permission.DUMP")
    public void preDumpUiData(String str) {
        enforcePermission(str, Binder.getCallingUid(), true);
        synchronized (this.mLock) {
            preDumpUiDataLocked(str);
        }
    }

    @RequiresPermission("android.permission.DUMP")
    public void startBugreport(int i, String str, FileDescriptor fileDescriptor, FileDescriptor fileDescriptor2, int i2, int i3, IDumpstateListener iDumpstateListener, boolean z) {
        Objects.requireNonNull(str);
        Objects.requireNonNull(fileDescriptor);
        Objects.requireNonNull(iDumpstateListener);
        validateBugreportMode(i2);
        validateBugreportFlags(i3);
        int callingUid = Binder.getCallingUid();
        enforcePermission(str, callingUid, i2 == 4);
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            ensureUserCanTakeBugReport(i2);
            Binder.restoreCallingIdentity(clearCallingIdentity);
            Slogf.i(TAG, "Starting bugreport for %s / %d", str, Integer.valueOf(callingUid));
            synchronized (this.mLock) {
                startBugreportLocked(callingUid, str, fileDescriptor, fileDescriptor2, i2, i3, iDumpstateListener, z);
            }
        } catch (Throwable th) {
            Binder.restoreCallingIdentity(clearCallingIdentity);
            throw th;
        }
    }

    @RequiresPermission("android.permission.DUMP")
    public void cancelBugreport(int i, String str) {
        int callingUid = Binder.getCallingUid();
        enforcePermission(str, callingUid, true);
        Slogf.i(TAG, "Cancelling bugreport for %s / %d", str, Integer.valueOf(callingUid));
        synchronized (this.mLock) {
            IDumpstate dumpstateBinderServiceLocked = getDumpstateBinderServiceLocked();
            if (dumpstateBinderServiceLocked == null) {
                Slog.w(TAG, "cancelBugreport: Could not find native dumpstate service");
                return;
            }
            try {
                dumpstateBinderServiceLocked.cancelBugreport(callingUid, str);
            } catch (RemoteException e) {
                Slog.e(TAG, "RemoteException in cancelBugreport", e);
            }
            stopDumpstateBinderServiceLocked();
        }
    }

    @RequiresPermission("android.permission.DUMP")
    public void retrieveBugreport(int i, String str, FileDescriptor fileDescriptor, String str2, IDumpstateListener iDumpstateListener) {
        int callingUid = Binder.getCallingUid();
        enforcePermission(str, callingUid, false);
        Slogf.i(TAG, "Retrieving bugreport for %s / %d", str, Integer.valueOf(callingUid));
        try {
            this.mBugreportFileManager.ensureCallerPreviouslyGeneratedFile(new Pair<>(Integer.valueOf(callingUid), str), str2);
            synchronized (this.mLock) {
                if (isDumpstateBinderServiceRunningLocked()) {
                    Slog.w(TAG, "'dumpstate' is already running. Cannot retrieve a bugreport while another one is currently in progress.");
                    reportError(iDumpstateListener, 5);
                    return;
                }
                IDumpstate startAndGetDumpstateBinderServiceLocked = startAndGetDumpstateBinderServiceLocked();
                if (startAndGetDumpstateBinderServiceLocked == null) {
                    Slog.w(TAG, "Unable to get bugreport service");
                    reportError(iDumpstateListener, 2);
                    return;
                }
                DumpstateListener dumpstateListener = new DumpstateListener(iDumpstateListener, startAndGetDumpstateBinderServiceLocked, new Pair(Integer.valueOf(callingUid), str), true);
                setCurrentDumpstateListenerLocked(dumpstateListener);
                try {
                    startAndGetDumpstateBinderServiceLocked.retrieveBugreport(callingUid, str, fileDescriptor, str2, dumpstateListener);
                } catch (RemoteException e) {
                    Slog.e(TAG, "RemoteException in retrieveBugreport", e);
                }
            }
        } catch (IllegalArgumentException e2) {
            Slog.e(TAG, e2.getMessage());
            reportError(iDumpstateListener, 6);
        }
    }

    @GuardedBy({"mLock"})
    private void setCurrentDumpstateListenerLocked(DumpstateListener dumpstateListener) {
        DumpstateListener dumpstateListener2 = this.mCurrentDumpstateListener;
        if (dumpstateListener2 != null) {
            Slogf.w(TAG, "setCurrentDumpstateListenerLocked(%s): called when mCurrentDumpstateListener is already set (%s)", dumpstateListener, dumpstateListener2);
        }
        this.mCurrentDumpstateListener = dumpstateListener;
    }

    private void validateBugreportMode(int i) {
        if (i == 0 || i == 1 || i == 2 || i == 3 || i == 4 || i == 5) {
            return;
        }
        Slog.w(TAG, "Unknown bugreport mode: " + i);
        throw new IllegalArgumentException("Unknown bugreport mode: " + i);
    }

    private void validateBugreportFlags(int i) {
        int clearBugreportFlag = clearBugreportFlag(i, 3);
        if (clearBugreportFlag == 0) {
            return;
        }
        Slog.w(TAG, "Unknown bugreport flags: " + clearBugreportFlag);
        throw new IllegalArgumentException("Unknown bugreport flags: " + clearBugreportFlag);
    }

    private void enforcePermission(String str, int i, boolean z) {
        this.mAppOps.checkPackage(i, str);
        if (this.mBugreportAllowlistedPackages.contains(str) && this.mContext.checkCallingOrSelfPermission("android.permission.DUMP") == 0) {
            return;
        }
        long clearCallingIdentity = Binder.clearCallingIdentity();
        if (z) {
            try {
                if (this.mTelephonyManager.checkCarrierPrivilegesForPackageAnyPhone(str) == 1) {
                    return;
                }
            } finally {
                Binder.restoreCallingIdentity(clearCallingIdentity);
            }
        }
        Binder.restoreCallingIdentity(clearCallingIdentity);
        StringBuilder sb = new StringBuilder();
        sb.append(str);
        sb.append(" does not hold the DUMP permission or is not bugreport-whitelisted ");
        sb.append(z ? "and does not have carrier privileges " : "");
        sb.append("to request a bugreport");
        String sb2 = sb.toString();
        Slog.w(TAG, sb2);
        throw new SecurityException(sb2);
    }

    private void ensureUserCanTakeBugReport(int i) {
        UserInfo userInfo;
        try {
            userInfo = ActivityManager.getService().getCurrentUser();
        } catch (RemoteException unused) {
            userInfo = null;
        }
        if (userInfo == null) {
            logAndThrow("There is no current user, so no bugreport can be requested.");
        }
        if (userInfo.isAdmin()) {
            return;
        }
        if (i == 2 && isCurrentUserAffiliated(userInfo.id)) {
            return;
        }
        logAndThrow(TextUtils.formatSimple("Current user %s is not an admin user. Only admin users are allowed to take bugreport.", new Object[]{Integer.valueOf(userInfo.id)}));
    }

    private boolean isCurrentUserAffiliated(int i) {
        DevicePolicyManager devicePolicyManager = (DevicePolicyManager) this.mContext.getSystemService(DevicePolicyManager.class);
        int deviceOwnerUserId = devicePolicyManager.getDeviceOwnerUserId();
        if (deviceOwnerUserId == -10000) {
            return false;
        }
        int userId = UserHandle.getUserId(Binder.getCallingUid());
        Slog.i(TAG, "callingUid: " + userId + " deviceOwnerUid: " + deviceOwnerUserId + " currentUserId: " + i);
        if (userId != deviceOwnerUserId) {
            logAndThrow("Caller is not device owner on provisioned device.");
        }
        if (devicePolicyManager.isAffiliatedUser(i)) {
            return true;
        }
        logAndThrow("Current user is not affiliated to the device owner.");
        return true;
    }

    @GuardedBy({"mLock"})
    private void preDumpUiDataLocked(String str) {
        this.mPreDumpedDataUid = OptionalInt.empty();
        if (isDumpstateBinderServiceRunningLocked()) {
            Slog.e(TAG, "'dumpstate' is already running. Cannot pre-dump data while another operation is currently in progress.");
            return;
        }
        IDumpstate startAndGetDumpstateBinderServiceLocked = startAndGetDumpstateBinderServiceLocked();
        if (startAndGetDumpstateBinderServiceLocked == null) {
            Slog.e(TAG, "Unable to get bugreport service");
            return;
        }
        try {
            startAndGetDumpstateBinderServiceLocked.preDumpUiData(str);
            stopDumpstateBinderServiceLocked();
            this.mPreDumpedDataUid = OptionalInt.of(Binder.getCallingUid());
        } catch (RemoteException unused) {
            stopDumpstateBinderServiceLocked();
        } catch (Throwable th) {
            stopDumpstateBinderServiceLocked();
            throw th;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:16:0x0049  */
    /* JADX WARN: Removed duplicated region for block: B:19:0x0053  */
    /* JADX WARN: Removed duplicated region for block: B:21:0x005d  */
    /* JADX WARN: Removed duplicated region for block: B:28:0x004b  */
    @GuardedBy({"mLock"})
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void startBugreportLocked(int i, String str, FileDescriptor fileDescriptor, FileDescriptor fileDescriptor2, int i2, int i3, IDumpstateListener iDumpstateListener, boolean z) {
        IDumpstate startAndGetDumpstateBinderServiceLocked;
        int i4 = i3;
        if (isDumpstateBinderServiceRunningLocked()) {
            Slog.w(TAG, "'dumpstate' is already running. Cannot start a new bugreport while another operation is currently in progress.");
            reportError(iDumpstateListener, 5);
            return;
        }
        if ((i4 & 1) != 0) {
            if (this.mPreDumpedDataUid.isEmpty()) {
                i4 = clearBugreportFlag(i4, 1);
                Slog.w(TAG, "Ignoring BUGREPORT_FLAG_USE_PREDUMPED_UI_DATA. No pre-dumped data is available.");
            } else {
                if (this.mPreDumpedDataUid.getAsInt() != i) {
                    i4 = clearBugreportFlag(i4, 1);
                    Slog.w(TAG, "Ignoring BUGREPORT_FLAG_USE_PREDUMPED_UI_DATA. Data was pre-dumped by a different UID.");
                }
                int i5 = i4;
                boolean z2 = (i5 & 2) == 0;
                startAndGetDumpstateBinderServiceLocked = startAndGetDumpstateBinderServiceLocked();
                if (startAndGetDumpstateBinderServiceLocked != null) {
                    Slog.w(TAG, "Unable to get bugreport service");
                    reportError(iDumpstateListener, 2);
                    return;
                }
                DumpstateListener dumpstateListener = new DumpstateListener(iDumpstateListener, startAndGetDumpstateBinderServiceLocked, new Pair(Integer.valueOf(i), str), z2);
                setCurrentDumpstateListenerLocked(dumpstateListener);
                try {
                    startAndGetDumpstateBinderServiceLocked.startBugreport(i, str, fileDescriptor, fileDescriptor2, i2, i5, dumpstateListener, z);
                    return;
                } catch (RemoteException unused) {
                    cancelBugreport(i, str);
                    return;
                }
            }
        }
        int i52 = i4;
        if ((i52 & 2) == 0) {
        }
        startAndGetDumpstateBinderServiceLocked = startAndGetDumpstateBinderServiceLocked();
        if (startAndGetDumpstateBinderServiceLocked != null) {
        }
    }

    @GuardedBy({"mLock"})
    private boolean isDumpstateBinderServiceRunningLocked() {
        return getDumpstateBinderServiceLocked() != null;
    }

    @GuardedBy({"mLock"})
    private IDumpstate getDumpstateBinderServiceLocked() {
        return IDumpstate.Stub.asInterface(ServiceManager.getService("dumpstate"));
    }

    @GuardedBy({"mLock"})
    private IDumpstate startAndGetDumpstateBinderServiceLocked() {
        SystemProperties.set("ctl.start", BUGREPORT_SERVICE);
        IDumpstate iDumpstate = null;
        int i = SensorPrivacyService.REMINDER_DIALOG_DELAY_MILLIS;
        boolean z = false;
        int i2 = 0;
        while (true) {
            if (z) {
                break;
            }
            iDumpstate = getDumpstateBinderServiceLocked();
            if (iDumpstate != null) {
                Slog.i(TAG, "Got bugreport service handle.");
                break;
            }
            SystemClock.sleep(i);
            Slog.i(TAG, "Waiting to get dumpstate service handle (" + i2 + "ms)");
            i2 += i;
            i *= 2;
            z = ((long) i2) > DEFAULT_BUGREPORT_SERVICE_TIMEOUT_MILLIS;
        }
        if (z) {
            Slog.w(TAG, "Timed out waiting to get dumpstate service handle (" + i2 + "ms)");
        }
        return iDumpstate;
    }

    @GuardedBy({"mLock"})
    private void stopDumpstateBinderServiceLocked() {
        SystemProperties.set("ctl.stop", BUGREPORT_SERVICE);
    }

    @RequiresPermission("android.permission.DUMP")
    public void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        if (DumpUtils.checkDumpPermission(this.mContext, TAG, printWriter)) {
            printWriter.printf("Allow-listed packages: %s\n", this.mBugreportAllowlistedPackages);
            synchronized (this.mLock) {
                printWriter.print("Pre-dumped data UID: ");
                if (this.mPreDumpedDataUid.isEmpty()) {
                    printWriter.println("none");
                } else {
                    printWriter.println(this.mPreDumpedDataUid.getAsInt());
                }
                DumpstateListener dumpstateListener = this.mCurrentDumpstateListener;
                if (dumpstateListener == null) {
                    printWriter.println("Not taking a bug report");
                } else {
                    dumpstateListener.dump(printWriter);
                }
                int i = this.mNumberFinishedBugreports;
                if (i == 0) {
                    printWriter.println("No finished bugreports");
                } else {
                    Object[] objArr = new Object[3];
                    objArr[0] = Integer.valueOf(i);
                    int i2 = this.mNumberFinishedBugreports;
                    objArr[1] = i2 > 1 ? "s" : "";
                    objArr[2] = Integer.valueOf(Math.min(i2, 20));
                    printWriter.printf("%d finished bugreport%s. Last %d:\n", objArr);
                    this.mFinishedBugreports.dump("  ", printWriter);
                }
            }
            synchronized (this.mBugreportFileManager.mLock) {
                int size = this.mBugreportFileManager.mBugreportFiles.size();
                Object[] objArr2 = new Object[2];
                objArr2[0] = Integer.valueOf(size);
                objArr2[1] = size > 1 ? "s" : "";
                printWriter.printf("%d pending file%s", objArr2);
                if (size > 0) {
                    for (int i3 = 0; i3 < size; i3++) {
                        printWriter.printf("  %s: %s\n", callerToString((Pair) this.mBugreportFileManager.mBugreportFiles.keyAt(i3)), (ArraySet) this.mBugreportFileManager.mBugreportFiles.valueAt(i3));
                    }
                } else {
                    printWriter.println();
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String callerToString(Pair<Integer, String> pair) {
        if (pair == null) {
            return "N/A";
        }
        return ((String) pair.second) + SliceClientPermissions.SliceAuthority.DELIMITER + pair.first;
    }

    private void reportError(IDumpstateListener iDumpstateListener, int i) {
        try {
            iDumpstateListener.onError(i);
        } catch (RemoteException e) {
            Slog.w(TAG, "onError() transaction threw RemoteException: " + e.getMessage());
        }
    }

    private void logAndThrow(String str) {
        Slog.w(TAG, str);
        throw new IllegalArgumentException(str);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public final class DumpstateListener extends IDumpstateListener.Stub implements IBinder.DeathRecipient {
        private static int sNextId;
        private final Pair<Integer, String> mCaller;
        private boolean mDone;
        private final IDumpstate mDs;
        private final int mId;
        private final IDumpstateListener mListener;
        private int mProgress;
        private final boolean mReportFinishedFile;

        DumpstateListener(IDumpstateListener iDumpstateListener, IDumpstate iDumpstate, Pair<Integer, String> pair, boolean z) {
            int i = sNextId + 1;
            sNextId = i;
            this.mId = i;
            this.mListener = iDumpstateListener;
            this.mDs = iDumpstate;
            this.mCaller = pair;
            this.mReportFinishedFile = z;
            try {
                iDumpstate.asBinder().linkToDeath(this, 0);
            } catch (RemoteException e) {
                Slog.e(BugreportManagerServiceImpl.TAG, "Unable to register Death Recipient for IDumpstate", e);
            }
        }

        public void onProgress(int i) throws RemoteException {
            this.mProgress = i;
            this.mListener.onProgress(i);
        }

        public void onError(int i) throws RemoteException {
            Slogf.e(BugreportManagerServiceImpl.TAG, "onError(): %d", Integer.valueOf(i));
            synchronized (BugreportManagerServiceImpl.this.mLock) {
                releaseItselfLocked();
                reportFinishedLocked("ErroCode: " + i);
            }
            this.mListener.onError(i);
        }

        public void onFinished(String str) throws RemoteException {
            Slogf.i(BugreportManagerServiceImpl.TAG, "onFinished(): %s", str);
            synchronized (BugreportManagerServiceImpl.this.mLock) {
                releaseItselfLocked();
                reportFinishedLocked("File: " + str);
            }
            if (this.mReportFinishedFile) {
                BugreportManagerServiceImpl.this.mBugreportFileManager.addBugreportFileForCaller(this.mCaller, str);
            }
            this.mListener.onFinished(str);
        }

        public void onScreenshotTaken(boolean z) throws RemoteException {
            this.mListener.onScreenshotTaken(z);
        }

        public void onUiIntensiveBugreportDumpsFinished() throws RemoteException {
            this.mListener.onUiIntensiveBugreportDumpsFinished();
        }

        @Override // android.os.IBinder.DeathRecipient
        public void binderDied() {
            try {
                Thread.sleep(1000L);
            } catch (InterruptedException unused) {
            }
            synchronized (BugreportManagerServiceImpl.this.mLock) {
                if (!this.mDone) {
                    Slog.e(BugreportManagerServiceImpl.TAG, "IDumpstate likely crashed. Notifying listener");
                    try {
                        this.mListener.onError(2);
                    } catch (RemoteException unused2) {
                    }
                }
            }
            this.mDs.asBinder().unlinkToDeath(this, 0);
        }

        public String toString() {
            return "DumpstateListener[id=" + this.mId + ", progress=" + this.mProgress + "]";
        }

        @GuardedBy({"mLock"})
        private void reportFinishedLocked(String str) {
            BugreportManagerServiceImpl.this.mNumberFinishedBugreports++;
            BugreportManagerServiceImpl.this.mFinishedBugreports.log("Caller: " + BugreportManagerServiceImpl.callerToString(this.mCaller) + " " + str);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void dump(PrintWriter printWriter) {
            printWriter.println("DumpstateListener:");
            printWriter.printf("  id: %d\n", Integer.valueOf(this.mId));
            printWriter.printf("  caller: %s\n", BugreportManagerServiceImpl.callerToString(this.mCaller));
            printWriter.printf("  reports finished file: %b\n", Boolean.valueOf(this.mReportFinishedFile));
            printWriter.printf("  progress: %d\n", Integer.valueOf(this.mProgress));
            printWriter.printf("  done: %b\n", Boolean.valueOf(this.mDone));
        }

        @GuardedBy({"mLock"})
        private void releaseItselfLocked() {
            this.mDone = true;
            if (BugreportManagerServiceImpl.this.mCurrentDumpstateListener == this) {
                BugreportManagerServiceImpl.this.mCurrentDumpstateListener = null;
                return;
            }
            Slogf.w(BugreportManagerServiceImpl.TAG, "releaseItselfLocked(): " + this + " is finished, but current listener is " + BugreportManagerServiceImpl.this.mCurrentDumpstateListener);
        }
    }
}
