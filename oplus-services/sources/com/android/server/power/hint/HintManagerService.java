package com.android.server.power.hint;

import android.app.ActivityManager;
import android.app.ActivityManagerInternal;
import android.app.StatsManager;
import android.app.UidObserver;
import android.content.Context;
import android.os.Binder;
import android.os.IBinder;
import android.os.IHintManager;
import android.os.IHintSession;
import android.os.Process;
import android.os.RemoteException;
import android.os.SystemProperties;
import android.util.ArrayMap;
import android.util.ArraySet;
import android.util.SparseArray;
import android.util.StatsEvent;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.os.BackgroundThread;
import com.android.internal.util.DumpUtils;
import com.android.internal.util.FrameworkStatsLog;
import com.android.internal.util.Preconditions;
import com.android.server.FgThread;
import com.android.server.LocalServices;
import com.android.server.SystemService;
import com.android.server.power.hint.HintManagerService;
import com.android.server.utils.Slogf;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public final class HintManagerService extends SystemService {
    private static final boolean DEBUG = false;
    private static final String PROPERTY_HWUI_ENABLE_HINT_MANAGER = "debug.hwui.use_hint_manager";
    private static final String PROPERTY_SF_ENABLE_CPU_HINT = "debug.sf.enable_adpf_cpu_hint";
    private static final String TAG = "HintManagerService";

    @GuardedBy({"mLock"})
    private final ArrayMap<Integer, ArrayMap<IBinder, ArraySet<AppHintSession>>> mActiveSessions;
    private final ActivityManagerInternal mAmInternal;
    private final Context mContext;

    @VisibleForTesting
    final long mHintSessionPreferredRate;
    private final Object mLock;
    private final NativeWrapper mNativeWrapper;

    @VisibleForTesting
    final IHintManager.Stub mService;

    @VisibleForTesting
    final MyUidObserver mUidObserver;

    public HintManagerService(Context context) {
        this(context, new Injector());
    }

    @VisibleForTesting
    HintManagerService(Context context, Injector injector) {
        super(context);
        this.mLock = new Object();
        this.mService = new BinderService();
        this.mContext = context;
        this.mActiveSessions = new ArrayMap<>();
        NativeWrapper createNativeWrapper = injector.createNativeWrapper();
        this.mNativeWrapper = createNativeWrapper;
        createNativeWrapper.halInit();
        this.mHintSessionPreferredRate = createNativeWrapper.halGetHintSessionPreferredRate();
        this.mUidObserver = new MyUidObserver();
        ActivityManagerInternal activityManagerInternal = (ActivityManagerInternal) LocalServices.getService(ActivityManagerInternal.class);
        Objects.requireNonNull(activityManagerInternal);
        this.mAmInternal = activityManagerInternal;
    }

    @VisibleForTesting
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    static class Injector {
        Injector() {
        }

        NativeWrapper createNativeWrapper() {
            return new NativeWrapper();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isHalSupported() {
        return this.mHintSessionPreferredRate != -1;
    }

    public void onStart() {
        publishBinderService("performance_hint", this.mService);
    }

    public void onBootPhase(int i) {
        if (i == 500) {
            systemReady();
        }
        if (i == 1000) {
            registerStatsCallbacks();
        }
    }

    private void systemReady() {
        Slogf.v(TAG, "Initializing HintManager service...");
        try {
            ActivityManager.getService().registerUidObserver(this.mUidObserver, 3, -1, (String) null);
        } catch (RemoteException unused) {
        }
    }

    private void registerStatsCallbacks() {
        ((StatsManager) this.mContext.getSystemService(StatsManager.class)).setPullAtomCallback(10173, (StatsManager.PullAtomMetadata) null, BackgroundThread.getExecutor(), new StatsManager.StatsPullAtomCallback() { // from class: com.android.server.power.hint.HintManagerService$$ExternalSyntheticLambda0
            public final int onPullAtom(int i, List list) {
                int onPullAtom;
                onPullAtom = HintManagerService.this.onPullAtom(i, list);
                return onPullAtom;
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int onPullAtom(int i, List<StatsEvent> list) {
        if (i == 10173) {
            list.add(FrameworkStatsLog.buildStatsEvent(10173, SystemProperties.getBoolean(PROPERTY_SF_ENABLE_CPU_HINT, false), SystemProperties.getBoolean(PROPERTY_HWUI_ENABLE_HINT_MANAGER, false)));
        }
        return 0;
    }

    @VisibleForTesting
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static class NativeWrapper {
        private static native void nativeCloseHintSession(long j);

        private static native long nativeCreateHintSession(int i, int i2, int[] iArr, long j);

        private static native long nativeGetHintSessionPreferredRate();

        private native void nativeInit();

        private static native void nativePauseHintSession(long j);

        private static native void nativeReportActualWorkDuration(long j, long[] jArr, long[] jArr2);

        private static native void nativeResumeHintSession(long j);

        private static native void nativeSendHint(long j, int i);

        private static native void nativeSetThreads(long j, int[] iArr);

        private static native void nativeUpdateTargetWorkDuration(long j, long j2);

        public void halInit() {
            nativeInit();
        }

        public long halCreateHintSession(int i, int i2, int[] iArr, long j) {
            return nativeCreateHintSession(i, i2, iArr, j);
        }

        public void halPauseHintSession(long j) {
            nativePauseHintSession(j);
        }

        public void halResumeHintSession(long j) {
            nativeResumeHintSession(j);
        }

        public void halCloseHintSession(long j) {
            nativeCloseHintSession(j);
        }

        public void halUpdateTargetWorkDuration(long j, long j2) {
            nativeUpdateTargetWorkDuration(j, j2);
        }

        public void halReportActualWorkDuration(long j, long[] jArr, long[] jArr2) {
            nativeReportActualWorkDuration(j, jArr, jArr2);
        }

        public void halSendHint(long j, int i) {
            nativeSendHint(j, i);
        }

        public long halGetHintSessionPreferredRate() {
            return nativeGetHintSessionPreferredRate();
        }

        public void halSetThreads(long j, int[] iArr) {
            nativeSetThreads(j, iArr);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @VisibleForTesting
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public final class MyUidObserver extends UidObserver {
        private final SparseArray<Integer> mProcStatesCache = new SparseArray<>();

        MyUidObserver() {
        }

        public boolean isUidForeground(int i) {
            boolean z;
            synchronized (HintManagerService.this.mLock) {
                z = this.mProcStatesCache.get(i, 6).intValue() <= 6;
            }
            return z;
        }

        public void onUidGone(final int i, boolean z) {
            FgThread.getHandler().post(new Runnable() { // from class: com.android.server.power.hint.HintManagerService$MyUidObserver$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    HintManagerService.MyUidObserver.this.lambda$onUidGone$0(i);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onUidGone$0(int i) {
            synchronized (HintManagerService.this.mLock) {
                ArrayMap arrayMap = (ArrayMap) HintManagerService.this.mActiveSessions.get(Integer.valueOf(i));
                if (arrayMap == null) {
                    return;
                }
                for (int size = arrayMap.size() - 1; size >= 0; size--) {
                    ArraySet arraySet = (ArraySet) arrayMap.valueAt(size);
                    for (int size2 = arraySet.size() - 1; size2 >= 0; size2--) {
                        ((AppHintSession) arraySet.valueAt(size2)).close();
                    }
                }
                this.mProcStatesCache.delete(i);
            }
        }

        public void onUidStateChanged(final int i, final int i2, long j, int i3) {
            FgThread.getHandler().post(new Runnable() { // from class: com.android.server.power.hint.HintManagerService$MyUidObserver$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    HintManagerService.MyUidObserver.this.lambda$onUidStateChanged$1(i, i2);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onUidStateChanged$1(int i, int i2) {
            synchronized (HintManagerService.this.mLock) {
                this.mProcStatesCache.put(i, Integer.valueOf(i2));
                ArrayMap arrayMap = (ArrayMap) HintManagerService.this.mActiveSessions.get(Integer.valueOf(i));
                if (arrayMap == null) {
                    return;
                }
                Iterator it = arrayMap.values().iterator();
                while (it.hasNext()) {
                    Iterator it2 = ((ArraySet) it.next()).iterator();
                    while (it2.hasNext()) {
                        ((AppHintSession) it2.next()).onProcStateChanged();
                    }
                }
            }
        }
    }

    @VisibleForTesting
    IHintManager.Stub getBinderServiceInstance() {
        return this.mService;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean checkTidValid(int i, int i2, int[] iArr) {
        List list = null;
        for (int i3 : iArr) {
            long[] jArr = new long[2];
            Process.readProcLines("/proc/" + i3 + "/status", new String[]{"Uid:", "Tgid:"}, jArr);
            int i4 = (int) jArr[0];
            int i5 = (int) jArr[1];
            if (i5 != i2 && i4 != i && ((list == null && (i == 1000 || (list = this.mAmInternal.getIsolatedProcesses(i)) == null)) || !list.contains(Integer.valueOf(i5)))) {
                return false;
            }
        }
        return true;
    }

    @VisibleForTesting
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    final class BinderService extends IHintManager.Stub {
        BinderService() {
        }

        public IHintSession createHintSession(IBinder iBinder, int[] iArr, long j) {
            if (!HintManagerService.this.isHalSupported()) {
                return null;
            }
            Objects.requireNonNull(iBinder);
            Objects.requireNonNull(iArr);
            Preconditions.checkArgument(iArr.length != 0, "tids should not be empty.");
            int callingUid = Binder.getCallingUid();
            int threadGroupLeader = Process.getThreadGroupLeader(Binder.getCallingPid());
            long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                if (!HintManagerService.this.checkTidValid(callingUid, threadGroupLeader, iArr)) {
                    throw new SecurityException("Some tid doesn't belong to the application");
                }
                long halCreateHintSession = HintManagerService.this.mNativeWrapper.halCreateHintSession(threadGroupLeader, callingUid, iArr, j);
                if (halCreateHintSession == 0) {
                    return null;
                }
                AppHintSession appHintSession = new AppHintSession(callingUid, threadGroupLeader, iArr, iBinder, halCreateHintSession, j);
                logPerformanceHintSessionAtom(callingUid, halCreateHintSession, j, iArr);
                synchronized (HintManagerService.this.mLock) {
                    ArrayMap arrayMap = (ArrayMap) HintManagerService.this.mActiveSessions.get(Integer.valueOf(callingUid));
                    if (arrayMap == null) {
                        arrayMap = new ArrayMap(1);
                        HintManagerService.this.mActiveSessions.put(Integer.valueOf(callingUid), arrayMap);
                    }
                    ArraySet arraySet = (ArraySet) arrayMap.get(iBinder);
                    if (arraySet == null) {
                        arraySet = new ArraySet(1);
                        arrayMap.put(iBinder, arraySet);
                    }
                    arraySet.add(appHintSession);
                }
                return appHintSession;
            } finally {
                Binder.restoreCallingIdentity(clearCallingIdentity);
            }
        }

        public long getHintSessionPreferredRate() {
            return HintManagerService.this.mHintSessionPreferredRate;
        }

        public void setHintSessionThreads(IHintSession iHintSession, int[] iArr) {
            ((AppHintSession) iHintSession).setThreads(iArr);
        }

        public int[] getHintSessionThreadIds(IHintSession iHintSession) {
            return ((AppHintSession) iHintSession).getThreadIds();
        }

        public void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
            if (DumpUtils.checkDumpPermission(HintManagerService.this.getContext(), HintManagerService.TAG, printWriter)) {
                synchronized (HintManagerService.this.mLock) {
                    printWriter.println("HintSessionPreferredRate: " + HintManagerService.this.mHintSessionPreferredRate);
                    printWriter.println("HAL Support: " + HintManagerService.this.isHalSupported());
                    printWriter.println("Active Sessions:");
                    for (int i = 0; i < HintManagerService.this.mActiveSessions.size(); i++) {
                        printWriter.println("Uid " + ((Integer) HintManagerService.this.mActiveSessions.keyAt(i)).toString() + ":");
                        ArrayMap arrayMap = (ArrayMap) HintManagerService.this.mActiveSessions.valueAt(i);
                        for (int i2 = 0; i2 < arrayMap.size(); i2++) {
                            ArraySet arraySet = (ArraySet) arrayMap.valueAt(i2);
                            for (int i3 = 0; i3 < arraySet.size(); i3++) {
                                printWriter.println("  Session:");
                                ((AppHintSession) arraySet.valueAt(i3)).dump(printWriter, "    ");
                            }
                        }
                    }
                }
            }
        }

        private void logPerformanceHintSessionAtom(int i, long j, long j2, int[] iArr) {
            FrameworkStatsLog.write(574, i, j, j2, iArr.length);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @VisibleForTesting
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public final class AppHintSession extends IHintSession.Stub implements IBinder.DeathRecipient {
        protected long mHalSessionPtr;
        protected int[] mNewThreadIds;
        protected final int mPid;
        protected long mTargetDurationNanos;
        protected int[] mThreadIds;
        protected final IBinder mToken;
        protected final int mUid;
        protected boolean mUpdateAllowed = true;

        protected AppHintSession(int i, int i2, int[] iArr, IBinder iBinder, long j, long j2) {
            this.mUid = i;
            this.mPid = i2;
            this.mToken = iBinder;
            this.mThreadIds = iArr;
            this.mHalSessionPtr = j;
            this.mTargetDurationNanos = j2;
            updateHintAllowed();
            try {
                iBinder.linkToDeath(this, 0);
            } catch (RemoteException e) {
                HintManagerService.this.mNativeWrapper.halCloseHintSession(this.mHalSessionPtr);
                throw new IllegalStateException("Client already dead", e);
            }
        }

        @VisibleForTesting
        boolean updateHintAllowed() {
            boolean isUidForeground;
            synchronized (HintManagerService.this.mLock) {
                isUidForeground = HintManagerService.this.mUidObserver.isUidForeground(this.mUid);
                if (isUidForeground && !this.mUpdateAllowed) {
                    resume();
                }
                if (!isUidForeground && this.mUpdateAllowed) {
                    pause();
                }
                this.mUpdateAllowed = isUidForeground;
            }
            return isUidForeground;
        }

        public void updateTargetWorkDuration(long j) {
            synchronized (HintManagerService.this.mLock) {
                if (this.mHalSessionPtr != 0 && updateHintAllowed()) {
                    Preconditions.checkArgument(j > 0, "Expected the target duration to be greater than 0.");
                    HintManagerService.this.mNativeWrapper.halUpdateTargetWorkDuration(this.mHalSessionPtr, j);
                    this.mTargetDurationNanos = j;
                }
            }
        }

        public void reportActualWorkDuration(long[] jArr, long[] jArr2) {
            synchronized (HintManagerService.this.mLock) {
                if (this.mHalSessionPtr != 0 && updateHintAllowed()) {
                    Preconditions.checkArgument(jArr.length != 0, "the count of hint durations shouldn't be 0.");
                    Preconditions.checkArgument(jArr.length == jArr2.length, "The length of durations and timestamps should be the same.");
                    for (int i = 0; i < jArr.length; i++) {
                        if (jArr[i] <= 0) {
                            throw new IllegalArgumentException(String.format("durations[%d]=%d should be greater than 0", Integer.valueOf(i), Long.valueOf(jArr[i])));
                        }
                    }
                    HintManagerService.this.mNativeWrapper.halReportActualWorkDuration(this.mHalSessionPtr, jArr, jArr2);
                }
            }
        }

        public void close() {
            synchronized (HintManagerService.this.mLock) {
                if (this.mHalSessionPtr == 0) {
                    return;
                }
                HintManagerService.this.mNativeWrapper.halCloseHintSession(this.mHalSessionPtr);
                this.mHalSessionPtr = 0L;
                this.mToken.unlinkToDeath(this, 0);
                ArrayMap arrayMap = (ArrayMap) HintManagerService.this.mActiveSessions.get(Integer.valueOf(this.mUid));
                if (arrayMap == null) {
                    Slogf.w(HintManagerService.TAG, "UID %d is not present in active session map", Integer.valueOf(this.mUid));
                    return;
                }
                ArraySet arraySet = (ArraySet) arrayMap.get(this.mToken);
                if (arraySet == null) {
                    Slogf.w(HintManagerService.TAG, "Token %s is not present in token map", this.mToken.toString());
                    return;
                }
                arraySet.remove(this);
                if (arraySet.isEmpty()) {
                    arrayMap.remove(this.mToken);
                }
                if (arrayMap.isEmpty()) {
                    HintManagerService.this.mActiveSessions.remove(Integer.valueOf(this.mUid));
                }
            }
        }

        public void sendHint(int i) {
            synchronized (HintManagerService.this.mLock) {
                if (this.mHalSessionPtr != 0 && updateHintAllowed()) {
                    Preconditions.checkArgument(i >= 0, "the hint ID the hint value should be greater than zero.");
                    HintManagerService.this.mNativeWrapper.halSendHint(this.mHalSessionPtr, i);
                }
            }
        }

        public void setThreads(int[] iArr) {
            synchronized (HintManagerService.this.mLock) {
                if (this.mHalSessionPtr == 0) {
                    return;
                }
                if (iArr.length == 0) {
                    throw new IllegalArgumentException("Thread id list can't be empty.");
                }
                int callingUid = Binder.getCallingUid();
                int threadGroupLeader = Process.getThreadGroupLeader(Binder.getCallingPid());
                long clearCallingIdentity = Binder.clearCallingIdentity();
                try {
                    if (!HintManagerService.this.checkTidValid(callingUid, threadGroupLeader, iArr)) {
                        throw new SecurityException("Some tid doesn't belong to the application.");
                    }
                    Binder.restoreCallingIdentity(clearCallingIdentity);
                    if (!updateHintAllowed()) {
                        Slogf.v(HintManagerService.TAG, "update hint not allowed, storing tids.");
                        this.mNewThreadIds = iArr;
                    } else {
                        HintManagerService.this.mNativeWrapper.halSetThreads(this.mHalSessionPtr, iArr);
                        this.mThreadIds = iArr;
                    }
                } catch (Throwable th) {
                    Binder.restoreCallingIdentity(clearCallingIdentity);
                    throw th;
                }
            }
        }

        public int[] getThreadIds() {
            return this.mThreadIds;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void onProcStateChanged() {
            updateHintAllowed();
        }

        private void pause() {
            synchronized (HintManagerService.this.mLock) {
                if (this.mHalSessionPtr == 0) {
                    return;
                }
                HintManagerService.this.mNativeWrapper.halPauseHintSession(this.mHalSessionPtr);
            }
        }

        private void resume() {
            synchronized (HintManagerService.this.mLock) {
                if (this.mHalSessionPtr == 0) {
                    return;
                }
                HintManagerService.this.mNativeWrapper.halResumeHintSession(this.mHalSessionPtr);
                if (this.mNewThreadIds != null) {
                    HintManagerService.this.mNativeWrapper.halSetThreads(this.mHalSessionPtr, this.mNewThreadIds);
                    this.mThreadIds = this.mNewThreadIds;
                    this.mNewThreadIds = null;
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void dump(PrintWriter printWriter, String str) {
            synchronized (HintManagerService.this.mLock) {
                printWriter.println(str + "SessionPID: " + this.mPid);
                printWriter.println(str + "SessionUID: " + this.mUid);
                printWriter.println(str + "SessionTIDs: " + Arrays.toString(this.mThreadIds));
                printWriter.println(str + "SessionTargetDurationNanos: " + this.mTargetDurationNanos);
                printWriter.println(str + "SessionAllowed: " + updateHintAllowed());
            }
        }

        @Override // android.os.IBinder.DeathRecipient
        public void binderDied() {
            close();
        }
    }
}
