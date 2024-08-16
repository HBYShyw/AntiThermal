package com.android.server.pm.permission;

import android.app.ActivityManager;
import android.app.ActivityManagerInternal;
import android.app.AlarmManager;
import android.app.IActivityManager;
import android.app.IUidObserver;
import android.app.UidObserver;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.RemoteException;
import android.permission.PermissionControllerManager;
import android.provider.DeviceConfig;
import android.util.Log;
import android.util.SparseArray;
import com.android.internal.annotations.GuardedBy;
import com.android.server.LocalServices;
import com.android.server.PermissionThread;
import com.android.server.pm.permission.OneTimePermissionUserManager;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class OneTimePermissionUserManager {
    private static final boolean DEBUG = false;
    private static final long DEFAULT_KILLED_DELAY_MILLIS = 5000;
    private static final String LOG_TAG = "OneTimePermissionUserManager";
    public static final String PROPERTY_KILLED_DELAY_CONFIG_KEY = "one_time_permissions_killed_delay_millis";
    private final AlarmManager mAlarmManager;
    private final Context mContext;
    private final Handler mHandler;
    private final PermissionControllerManager mPermissionControllerManager;
    private final Object mLock = new Object();
    private final BroadcastReceiver mUninstallListener = new BroadcastReceiver() { // from class: com.android.server.pm.permission.OneTimePermissionUserManager.1
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            if ("android.intent.action.UID_REMOVED".equals(intent.getAction())) {
                int intExtra = intent.getIntExtra("android.intent.extra.UID", -1);
                PackageInactivityListener packageInactivityListener = (PackageInactivityListener) OneTimePermissionUserManager.this.mListeners.get(intExtra);
                if (packageInactivityListener != null) {
                    packageInactivityListener.cancel();
                    OneTimePermissionUserManager.this.mListeners.remove(intExtra);
                }
            }
        }
    };

    @GuardedBy({"mLock"})
    private final SparseArray<PackageInactivityListener> mListeners = new SparseArray<>();
    private final IActivityManager mIActivityManager = ActivityManager.getService();
    private final ActivityManagerInternal mActivityManagerInternal = (ActivityManagerInternal) LocalServices.getService(ActivityManagerInternal.class);

    /* JADX INFO: Access modifiers changed from: package-private */
    public OneTimePermissionUserManager(Context context) {
        this.mContext = context;
        this.mAlarmManager = (AlarmManager) context.getSystemService(AlarmManager.class);
        this.mPermissionControllerManager = new PermissionControllerManager(context, PermissionThread.getHandler());
        this.mHandler = context.getMainThreadHandler();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void startPackageOneTimeSession(String str, long j, long j2) {
        try {
            int packageUid = this.mContext.getPackageManager().getPackageUid(str, 0);
            synchronized (this.mLock) {
                PackageInactivityListener packageInactivityListener = this.mListeners.get(packageUid);
                if (packageInactivityListener != null) {
                    packageInactivityListener.updateSessionParameters(j, j2);
                    return;
                }
                PackageInactivityListener packageInactivityListener2 = new PackageInactivityListener(packageUid, str, j, j2);
                synchronized (this.mLock) {
                    this.mListeners.put(packageUid, packageInactivityListener2);
                }
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(LOG_TAG, "Unknown package name " + str, e);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void stopPackageOneTimeSession(String str) {
        PackageInactivityListener packageInactivityListener;
        try {
            int packageUid = this.mContext.getPackageManager().getPackageUid(str, 0);
            synchronized (this.mLock) {
                packageInactivityListener = this.mListeners.get(packageUid);
                if (packageInactivityListener != null) {
                    this.mListeners.remove(packageUid);
                }
            }
            if (packageInactivityListener != null) {
                packageInactivityListener.cancel();
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(LOG_TAG, "Unknown package name " + str, e);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void registerUninstallListener() {
        this.mContext.registerReceiver(this.mUninstallListener, new IntentFilter("android.intent.action.UID_REMOVED"));
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public class PackageInactivityListener implements AlarmManager.OnAlarmListener {
        private static final int STATE_ACTIVE = 2;
        private static final int STATE_GONE = 0;
        private static final int STATE_TIMER = 1;
        private static final long TIMER_INACTIVE = -1;
        private final Object mInnerLock;
        private boolean mIsAlarmSet;
        private boolean mIsFinished;
        private final IUidObserver mObserver;
        private final String mPackageName;
        private long mRevokeAfterKilledDelay;
        private long mTimeout;
        private long mTimerStart;
        private final Object mToken;
        private final int mUid;

        private int getStateFromProcState(int i) {
            if (i == 20) {
                return 0;
            }
            return i > 4 ? 1 : 2;
        }

        private PackageInactivityListener(int i, String str, long j, long j2) {
            this.mTimerStart = -1L;
            this.mInnerLock = new Object();
            this.mToken = new Object();
            UidObserver uidObserver = new UidObserver() { // from class: com.android.server.pm.permission.OneTimePermissionUserManager.PackageInactivityListener.1
                public void onUidGone(int i2, boolean z) {
                    if (i2 == PackageInactivityListener.this.mUid) {
                        PackageInactivityListener.this.updateUidState(0);
                    }
                }

                public void onUidStateChanged(int i2, int i3, long j3, int i4) {
                    if (i2 == PackageInactivityListener.this.mUid) {
                        if (i3 > 4 && i3 != 20) {
                            PackageInactivityListener.this.updateUidState(1);
                        } else {
                            PackageInactivityListener.this.updateUidState(2);
                        }
                    }
                }
            };
            this.mObserver = uidObserver;
            Log.i(OneTimePermissionUserManager.LOG_TAG, "Start tracking " + str + ". uid=" + i + " timeout=" + j + " killedDelay=" + j2);
            this.mUid = i;
            this.mPackageName = str;
            this.mTimeout = j;
            this.mRevokeAfterKilledDelay = j2 == -1 ? DeviceConfig.getLong("permissions", OneTimePermissionUserManager.PROPERTY_KILLED_DELAY_CONFIG_KEY, OneTimePermissionUserManager.DEFAULT_KILLED_DELAY_MILLIS) : j2;
            try {
                OneTimePermissionUserManager.this.mIActivityManager.registerUidObserver(uidObserver, 3, 4, (String) null);
            } catch (RemoteException e) {
                Log.e(OneTimePermissionUserManager.LOG_TAG, "Couldn't check uid proc state", e);
                synchronized (this.mInnerLock) {
                    onPackageInactiveLocked();
                }
            }
            updateUidState();
        }

        public void updateSessionParameters(long j, long j2) {
            synchronized (this.mInnerLock) {
                this.mTimeout = Math.min(this.mTimeout, j);
                long j3 = this.mRevokeAfterKilledDelay;
                if (j2 == -1) {
                    j2 = DeviceConfig.getLong("permissions", OneTimePermissionUserManager.PROPERTY_KILLED_DELAY_CONFIG_KEY, OneTimePermissionUserManager.DEFAULT_KILLED_DELAY_MILLIS);
                }
                this.mRevokeAfterKilledDelay = Math.min(j3, j2);
                Log.v(OneTimePermissionUserManager.LOG_TAG, "Updated params for " + this.mPackageName + ". timeout=" + this.mTimeout + " killedDelay=" + this.mRevokeAfterKilledDelay);
                updateUidState();
            }
        }

        private int getCurrentState() {
            return getStateFromProcState(OneTimePermissionUserManager.this.mActivityManagerInternal.getUidProcessState(this.mUid));
        }

        private void updateUidState() {
            updateUidState(getCurrentState());
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void updateUidState(int i) {
            Log.v(OneTimePermissionUserManager.LOG_TAG, "Updating state for " + this.mPackageName + " (" + this.mUid + "). state=" + i);
            synchronized (this.mInnerLock) {
                OneTimePermissionUserManager.this.mHandler.removeCallbacksAndMessages(this.mToken);
                if (i == 0) {
                    if (this.mRevokeAfterKilledDelay == 0) {
                        onPackageInactiveLocked();
                        return;
                    } else {
                        OneTimePermissionUserManager.this.mHandler.postDelayed(new Runnable() { // from class: com.android.server.pm.permission.OneTimePermissionUserManager$PackageInactivityListener$$ExternalSyntheticLambda1
                            @Override // java.lang.Runnable
                            public final void run() {
                                OneTimePermissionUserManager.PackageInactivityListener.this.lambda$updateUidState$0();
                            }
                        }, this.mToken, this.mRevokeAfterKilledDelay);
                        return;
                    }
                }
                if (i == 1) {
                    if (this.mTimerStart == -1) {
                        this.mTimerStart = System.currentTimeMillis();
                        setAlarmLocked();
                    }
                } else if (i == 2) {
                    this.mTimerStart = -1L;
                    cancelAlarmLocked();
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$updateUidState$0() {
            synchronized (this.mInnerLock) {
                int currentState = getCurrentState();
                if (currentState == 0) {
                    onPackageInactiveLocked();
                } else {
                    updateUidState(currentState);
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void cancel() {
            synchronized (this.mInnerLock) {
                this.mIsFinished = true;
                cancelAlarmLocked();
                try {
                    OneTimePermissionUserManager.this.mIActivityManager.unregisterUidObserver(this.mObserver);
                } catch (RemoteException e) {
                    Log.e(OneTimePermissionUserManager.LOG_TAG, "Unable to unregister uid observer.", e);
                }
            }
        }

        @GuardedBy({"mInnerLock"})
        private void setAlarmLocked() {
            if (this.mIsAlarmSet) {
                return;
            }
            long j = this.mTimerStart + this.mTimeout;
            if (j > System.currentTimeMillis()) {
                OneTimePermissionUserManager.this.mAlarmManager.setExact(0, j, OneTimePermissionUserManager.LOG_TAG, this, OneTimePermissionUserManager.this.mHandler);
                this.mIsAlarmSet = true;
            } else {
                this.mIsAlarmSet = true;
                onAlarm();
            }
        }

        @GuardedBy({"mInnerLock"})
        private void cancelAlarmLocked() {
            if (this.mIsAlarmSet) {
                OneTimePermissionUserManager.this.mAlarmManager.cancel(this);
                this.mIsAlarmSet = false;
            }
        }

        @GuardedBy({"mInnerLock"})
        private void onPackageInactiveLocked() {
            if (this.mIsFinished) {
                return;
            }
            this.mIsFinished = true;
            cancelAlarmLocked();
            OneTimePermissionUserManager.this.mHandler.post(new Runnable() { // from class: com.android.server.pm.permission.OneTimePermissionUserManager$PackageInactivityListener$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    OneTimePermissionUserManager.PackageInactivityListener.this.lambda$onPackageInactiveLocked$1();
                }
            });
            try {
                OneTimePermissionUserManager.this.mIActivityManager.unregisterUidObserver(this.mObserver);
            } catch (RemoteException e) {
                Log.e(OneTimePermissionUserManager.LOG_TAG, "Unable to unregister uid observer.", e);
            }
            synchronized (OneTimePermissionUserManager.this.mLock) {
                OneTimePermissionUserManager.this.mListeners.remove(this.mUid);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onPackageInactiveLocked$1() {
            Log.i(OneTimePermissionUserManager.LOG_TAG, "One time session expired for " + this.mPackageName + " (" + this.mUid + ").");
            OneTimePermissionUserManager.this.mPermissionControllerManager.notifyOneTimePermissionSessionTimeout(this.mPackageName);
        }

        @Override // android.app.AlarmManager.OnAlarmListener
        public void onAlarm() {
            synchronized (this.mInnerLock) {
                if (this.mIsAlarmSet) {
                    this.mIsAlarmSet = false;
                    onPackageInactiveLocked();
                }
            }
        }
    }
}
