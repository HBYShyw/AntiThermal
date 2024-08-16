package com.android.server.locksettings;

import android.app.AlarmManager;
import android.app.admin.DevicePolicyManager;
import android.app.trust.IStrongAuthTracker;
import android.content.Context;
import android.os.Build;
import android.os.Debug;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.os.SystemClock;
import android.os.SystemProperties;
import android.util.ArrayMap;
import android.util.Log;
import android.util.Slog;
import android.util.SparseBooleanArray;
import android.util.SparseIntArray;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.util.IndentingPrintWriter;
import com.android.internal.widget.LockPatternUtils;

@VisibleForTesting
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class LockSettingsStrongAuth {
    private static final boolean DEBUG;
    private static final boolean DEBUG_LOCK;
    public static final long DEFAULT_NON_STRONG_BIOMETRIC_IDLE_TIMEOUT_MS = 14400000;
    public static final long DEFAULT_NON_STRONG_BIOMETRIC_TIMEOUT_MS = 86400000;
    private static final int MSG_NO_LONGER_REQUIRE_STRONG_AUTH = 6;
    private static final int MSG_REFRESH_STRONG_AUTH_TIMEOUT = 10;
    private static final int MSG_REGISTER_TRACKER = 2;
    private static final int MSG_REMOVE_USER = 4;
    private static final int MSG_REQUIRE_STRONG_AUTH = 1;
    private static final int MSG_SCHEDULE_NON_STRONG_BIOMETRIC_IDLE_TIMEOUT = 9;
    private static final int MSG_SCHEDULE_NON_STRONG_BIOMETRIC_TIMEOUT = 7;
    private static final int MSG_SCHEDULE_STRONG_AUTH_TIMEOUT = 5;
    private static final int MSG_STRONG_BIOMETRIC_UNLOCK = 8;
    private static final int MSG_UNREGISTER_TRACKER = 3;

    @VisibleForTesting
    protected static final String NON_STRONG_BIOMETRIC_IDLE_TIMEOUT_ALARM_TAG = "LockSettingsPrimaryAuth.nonStrongBiometricIdleTimeoutForUser";

    @VisibleForTesting
    protected static final String NON_STRONG_BIOMETRIC_TIMEOUT_ALARM_TAG = "LockSettingsPrimaryAuth.nonStrongBiometricTimeoutForUser";

    @VisibleForTesting
    protected static final String STRONG_AUTH_TIMEOUT_ALARM_TAG = "LockSettingsStrongAuth.timeoutForUser";
    private static final String TAG = "LockSettingsStrongAuth";
    private final AlarmManager mAlarmManager;
    private final Context mContext;
    private final boolean mDefaultIsNonStrongBiometricAllowed;
    private final int mDefaultStrongAuthFlags;

    @VisibleForTesting
    protected final Handler mHandler;
    private final Injector mInjector;

    @VisibleForTesting
    protected final SparseBooleanArray mIsNonStrongBiometricAllowedForUser;

    @VisibleForTesting
    protected final ArrayMap<Integer, NonStrongBiometricIdleTimeoutAlarmListener> mNonStrongBiometricIdleTimeoutAlarmListener;

    @VisibleForTesting
    protected final ArrayMap<Integer, NonStrongBiometricTimeoutAlarmListener> mNonStrongBiometricTimeoutAlarmListener;

    @VisibleForTesting
    protected final SparseIntArray mStrongAuthForUser;

    @VisibleForTesting
    protected final ArrayMap<Integer, StrongAuthTimeoutAlarmListener> mStrongAuthTimeoutAlarmListenerForUser;
    private final RemoteCallbackList<IStrongAuthTracker> mTrackers;

    static {
        boolean z = true;
        DEBUG = Build.IS_DEBUGGABLE && Log.isLoggable(TAG, 3);
        if (!SystemProperties.getBoolean("persist.sys.assert.panic", false) && !SystemProperties.getBoolean("persist.sys.alwayson.enable", false)) {
            z = false;
        }
        DEBUG_LOCK = z;
    }

    public LockSettingsStrongAuth(Context context) {
        this(context, new Injector());
    }

    @VisibleForTesting
    protected LockSettingsStrongAuth(Context context, Injector injector) {
        this.mTrackers = new RemoteCallbackList<>();
        this.mStrongAuthForUser = new SparseIntArray();
        this.mIsNonStrongBiometricAllowedForUser = new SparseBooleanArray();
        this.mStrongAuthTimeoutAlarmListenerForUser = new ArrayMap<>();
        this.mNonStrongBiometricTimeoutAlarmListener = new ArrayMap<>();
        this.mNonStrongBiometricIdleTimeoutAlarmListener = new ArrayMap<>();
        this.mDefaultIsNonStrongBiometricAllowed = true;
        this.mHandler = new Handler(Looper.getMainLooper()) { // from class: com.android.server.locksettings.LockSettingsStrongAuth.1
            @Override // android.os.Handler
            public void handleMessage(Message message) {
                switch (message.what) {
                    case 1:
                        LockSettingsStrongAuth.this.handleRequireStrongAuth(message.arg1, message.arg2);
                        return;
                    case 2:
                        LockSettingsStrongAuth.this.handleAddStrongAuthTracker((IStrongAuthTracker) message.obj);
                        return;
                    case 3:
                        LockSettingsStrongAuth.this.handleRemoveStrongAuthTracker((IStrongAuthTracker) message.obj);
                        return;
                    case 4:
                        LockSettingsStrongAuth.this.handleRemoveUser(message.arg1);
                        return;
                    case 5:
                        LockSettingsStrongAuth.this.handleScheduleStrongAuthTimeout(message.arg1);
                        return;
                    case 6:
                        LockSettingsStrongAuth.this.handleNoLongerRequireStrongAuth(message.arg1, message.arg2);
                        return;
                    case 7:
                        LockSettingsStrongAuth.this.handleScheduleNonStrongBiometricTimeout(message.arg1);
                        return;
                    case 8:
                        LockSettingsStrongAuth.this.handleStrongBiometricUnlock(message.arg1);
                        return;
                    case 9:
                        LockSettingsStrongAuth.this.handleScheduleNonStrongBiometricIdleTimeout(message.arg1);
                        return;
                    case 10:
                        LockSettingsStrongAuth.this.handleRefreshStrongAuthTimeout(message.arg1);
                        return;
                    default:
                        return;
                }
            }
        };
        this.mContext = context;
        this.mInjector = injector;
        this.mDefaultStrongAuthFlags = injector.getDefaultStrongAuthFlags(context);
        this.mAlarmManager = injector.getAlarmManager(context);
    }

    @VisibleForTesting
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static class Injector {
        @VisibleForTesting
        public AlarmManager getAlarmManager(Context context) {
            return (AlarmManager) context.getSystemService(AlarmManager.class);
        }

        @VisibleForTesting
        public int getDefaultStrongAuthFlags(Context context) {
            return LockPatternUtils.StrongAuthTracker.getDefaultFlags(context);
        }

        @VisibleForTesting
        public long getNextAlarmTimeMs(long j) {
            return SystemClock.elapsedRealtime() + j;
        }

        @VisibleForTesting
        public long getElapsedRealtimeMs() {
            return SystemClock.elapsedRealtime();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleAddStrongAuthTracker(IStrongAuthTracker iStrongAuthTracker) {
        this.mTrackers.register(iStrongAuthTracker);
        for (int i = 0; i < this.mStrongAuthForUser.size(); i++) {
            try {
                iStrongAuthTracker.onStrongAuthRequiredChanged(this.mStrongAuthForUser.valueAt(i), this.mStrongAuthForUser.keyAt(i));
            } catch (RemoteException e) {
                Slog.e(TAG, "Exception while adding StrongAuthTracker.", e);
            }
        }
        for (int i2 = 0; i2 < this.mIsNonStrongBiometricAllowedForUser.size(); i2++) {
            try {
                iStrongAuthTracker.onIsNonStrongBiometricAllowedChanged(this.mIsNonStrongBiometricAllowedForUser.valueAt(i2), this.mIsNonStrongBiometricAllowedForUser.keyAt(i2));
            } catch (RemoteException e2) {
                Slog.e(TAG, "Exception while adding StrongAuthTracker: IsNonStrongBiometricAllowedChanged.", e2);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleRemoveStrongAuthTracker(IStrongAuthTracker iStrongAuthTracker) {
        this.mTrackers.unregister(iStrongAuthTracker);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleRequireStrongAuth(int i, int i2) {
        if (i2 == -1) {
            for (int i3 = 0; i3 < this.mStrongAuthForUser.size(); i3++) {
                handleRequireStrongAuthOneUser(i, this.mStrongAuthForUser.keyAt(i3));
            }
            return;
        }
        handleRequireStrongAuthOneUser(i, i2);
    }

    private void handleRequireStrongAuthOneUser(int i, int i2) {
        int i3 = this.mStrongAuthForUser.get(i2, this.mDefaultStrongAuthFlags);
        int i4 = i == 0 ? 0 : i3 | i;
        Slog.d("LockSettingsService", "handleRequireStrongAuthOneUser oldValue: " + i3 + " newValue: " + i4 + " strongAuthReason: " + i + " userId: " + i2);
        if (i3 != i4) {
            this.mStrongAuthForUser.put(i2, i4);
            notifyStrongAuthTrackers(i4, i2);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleNoLongerRequireStrongAuth(int i, int i2) {
        if (i2 == -1) {
            for (int i3 = 0; i3 < this.mStrongAuthForUser.size(); i3++) {
                handleNoLongerRequireStrongAuthOneUser(i, this.mStrongAuthForUser.keyAt(i3));
            }
            return;
        }
        handleNoLongerRequireStrongAuthOneUser(i, i2);
    }

    private void handleNoLongerRequireStrongAuthOneUser(int i, int i2) {
        int i3 = this.mStrongAuthForUser.get(i2, this.mDefaultStrongAuthFlags);
        int i4 = (~i) & i3;
        if (DEBUG_LOCK) {
            Slog.d("LockSettingsService", "handleNoLongerRequireStrongAuthOneUser oldValue:" + i3 + " newValue:" + i4 + " strongAuthReason:" + i + " userId:" + i2);
        }
        if (i3 != i4) {
            this.mStrongAuthForUser.put(i2, i4);
            notifyStrongAuthTrackers(i4, i2);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleRemoveUser(int i) {
        int indexOfKey = this.mStrongAuthForUser.indexOfKey(i);
        if (indexOfKey >= 0) {
            this.mStrongAuthForUser.removeAt(indexOfKey);
            notifyStrongAuthTrackers(this.mDefaultStrongAuthFlags, i);
        }
        int indexOfKey2 = this.mIsNonStrongBiometricAllowedForUser.indexOfKey(i);
        if (indexOfKey2 >= 0) {
            this.mIsNonStrongBiometricAllowedForUser.removeAt(indexOfKey2);
            notifyStrongAuthTrackersForIsNonStrongBiometricAllowed(true, i);
        }
    }

    private void rescheduleStrongAuthTimeoutAlarm(long j, int i) {
        DevicePolicyManager devicePolicyManager = (DevicePolicyManager) this.mContext.getSystemService("device_policy");
        StrongAuthTimeoutAlarmListener strongAuthTimeoutAlarmListener = this.mStrongAuthTimeoutAlarmListenerForUser.get(Integer.valueOf(i));
        if (strongAuthTimeoutAlarmListener != null) {
            this.mAlarmManager.cancel(strongAuthTimeoutAlarmListener);
            strongAuthTimeoutAlarmListener.setLatestStrongAuthTime(j);
        } else {
            strongAuthTimeoutAlarmListener = new StrongAuthTimeoutAlarmListener(j, i);
            this.mStrongAuthTimeoutAlarmListenerForUser.put(Integer.valueOf(i), strongAuthTimeoutAlarmListener);
        }
        long requiredStrongAuthTimeout = j + devicePolicyManager.getRequiredStrongAuthTimeout(null, i);
        this.mAlarmManager.setExact(2, requiredStrongAuthTimeout, STRONG_AUTH_TIMEOUT_ALARM_TAG, strongAuthTimeoutAlarmListener, this.mHandler);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleScheduleStrongAuthTimeout(int i) {
        if (DEBUG) {
            Slog.d(TAG, "handleScheduleStrongAuthTimeout for userId=" + i);
        }
        rescheduleStrongAuthTimeoutAlarm(this.mInjector.getElapsedRealtimeMs(), i);
        cancelNonStrongBiometricAlarmListener(i);
        cancelNonStrongBiometricIdleAlarmListener(i);
        setIsNonStrongBiometricAllowed(true, i);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleRefreshStrongAuthTimeout(int i) {
        StrongAuthTimeoutAlarmListener strongAuthTimeoutAlarmListener = this.mStrongAuthTimeoutAlarmListenerForUser.get(Integer.valueOf(i));
        if (strongAuthTimeoutAlarmListener != null) {
            rescheduleStrongAuthTimeoutAlarm(strongAuthTimeoutAlarmListener.getLatestStrongAuthTime(), i);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleScheduleNonStrongBiometricTimeout(int i) {
        boolean z = DEBUG;
        if (z) {
            Slog.d(TAG, "handleScheduleNonStrongBiometricTimeout for userId=" + i);
        }
        long nextAlarmTimeMs = this.mInjector.getNextAlarmTimeMs(86400000L);
        if (this.mNonStrongBiometricTimeoutAlarmListener.get(Integer.valueOf(i)) == null) {
            if (z) {
                Slog.d(TAG, "Schedule a new alarm for non-strong biometric fallback timeout");
            }
            NonStrongBiometricTimeoutAlarmListener nonStrongBiometricTimeoutAlarmListener = new NonStrongBiometricTimeoutAlarmListener(i);
            this.mNonStrongBiometricTimeoutAlarmListener.put(Integer.valueOf(i), nonStrongBiometricTimeoutAlarmListener);
            this.mAlarmManager.setExact(2, nextAlarmTimeMs, NON_STRONG_BIOMETRIC_TIMEOUT_ALARM_TAG, nonStrongBiometricTimeoutAlarmListener, this.mHandler);
        } else if (z) {
            Slog.d(TAG, "There is an existing alarm for non-strong biometric fallback timeout, so do not re-schedule");
        }
        cancelNonStrongBiometricIdleAlarmListener(i);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleStrongBiometricUnlock(int i) {
        if (DEBUG) {
            Slog.d(TAG, "handleStrongBiometricUnlock for userId=" + i);
        }
        cancelNonStrongBiometricAlarmListener(i);
        cancelNonStrongBiometricIdleAlarmListener(i);
        setIsNonStrongBiometricAllowed(true, i);
    }

    private void cancelNonStrongBiometricAlarmListener(int i) {
        boolean z = DEBUG;
        if (z) {
            Slog.d(TAG, "cancelNonStrongBiometricAlarmListener for userId=" + i);
        }
        NonStrongBiometricTimeoutAlarmListener nonStrongBiometricTimeoutAlarmListener = this.mNonStrongBiometricTimeoutAlarmListener.get(Integer.valueOf(i));
        if (nonStrongBiometricTimeoutAlarmListener != null) {
            if (z) {
                Slog.d(TAG, "Cancel alarm for non-strong biometric fallback timeout");
            }
            this.mAlarmManager.cancel(nonStrongBiometricTimeoutAlarmListener);
            this.mNonStrongBiometricTimeoutAlarmListener.remove(Integer.valueOf(i));
        }
    }

    private void cancelNonStrongBiometricIdleAlarmListener(int i) {
        boolean z = DEBUG;
        if (z) {
            Slog.d(TAG, "cancelNonStrongBiometricIdleAlarmListener for userId=" + i);
        }
        NonStrongBiometricIdleTimeoutAlarmListener nonStrongBiometricIdleTimeoutAlarmListener = this.mNonStrongBiometricIdleTimeoutAlarmListener.get(Integer.valueOf(i));
        if (nonStrongBiometricIdleTimeoutAlarmListener != null) {
            if (z) {
                Slog.d(TAG, "Cancel alarm for non-strong biometric idle timeout");
            }
            this.mAlarmManager.cancel(nonStrongBiometricIdleTimeoutAlarmListener);
        }
    }

    @VisibleForTesting
    protected void setIsNonStrongBiometricAllowed(boolean z, int i) {
        if (DEBUG) {
            Slog.d(TAG, "setIsNonStrongBiometricAllowed for allowed=" + z + ", userId=" + i);
        }
        if (i == -1) {
            for (int i2 = 0; i2 < this.mIsNonStrongBiometricAllowedForUser.size(); i2++) {
                setIsNonStrongBiometricAllowedOneUser(z, this.mIsNonStrongBiometricAllowedForUser.keyAt(i2));
            }
            return;
        }
        setIsNonStrongBiometricAllowedOneUser(z, i);
    }

    private void setIsNonStrongBiometricAllowedOneUser(boolean z, int i) {
        boolean z2 = DEBUG;
        if (z2) {
            Slog.d(TAG, "setIsNonStrongBiometricAllowedOneUser for allowed=" + z + ", userId=" + i);
        }
        boolean z3 = this.mIsNonStrongBiometricAllowedForUser.get(i, true);
        if (z != z3) {
            if (z2) {
                Slog.d(TAG, "mIsNonStrongBiometricAllowedForUser value changed: oldValue=" + z3 + ", allowed=" + z);
            }
            this.mIsNonStrongBiometricAllowedForUser.put(i, z);
            notifyStrongAuthTrackersForIsNonStrongBiometricAllowed(z, i);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleScheduleNonStrongBiometricIdleTimeout(int i) {
        boolean z = DEBUG;
        if (z) {
            Slog.d(TAG, "handleScheduleNonStrongBiometricIdleTimeout for userId=" + i);
        }
        long nextAlarmTimeMs = this.mInjector.getNextAlarmTimeMs(14400000L);
        NonStrongBiometricIdleTimeoutAlarmListener nonStrongBiometricIdleTimeoutAlarmListener = this.mNonStrongBiometricIdleTimeoutAlarmListener.get(Integer.valueOf(i));
        if (nonStrongBiometricIdleTimeoutAlarmListener != null) {
            if (z) {
                Slog.d(TAG, "Cancel existing alarm for non-strong biometric idle timeout");
            }
            this.mAlarmManager.cancel(nonStrongBiometricIdleTimeoutAlarmListener);
        } else {
            nonStrongBiometricIdleTimeoutAlarmListener = new NonStrongBiometricIdleTimeoutAlarmListener(i);
            this.mNonStrongBiometricIdleTimeoutAlarmListener.put(Integer.valueOf(i), nonStrongBiometricIdleTimeoutAlarmListener);
        }
        NonStrongBiometricIdleTimeoutAlarmListener nonStrongBiometricIdleTimeoutAlarmListener2 = nonStrongBiometricIdleTimeoutAlarmListener;
        if (z) {
            Slog.d(TAG, "Schedule a new alarm for non-strong biometric idle timeout");
        }
        this.mAlarmManager.setExact(2, nextAlarmTimeMs, NON_STRONG_BIOMETRIC_IDLE_TIMEOUT_ALARM_TAG, nonStrongBiometricIdleTimeoutAlarmListener2, this.mHandler);
    }

    private void notifyStrongAuthTrackers(int i, int i2) {
        int beginBroadcast = this.mTrackers.beginBroadcast();
        while (beginBroadcast > 0) {
            beginBroadcast--;
            try {
                try {
                    this.mTrackers.getBroadcastItem(beginBroadcast).onStrongAuthRequiredChanged(i, i2);
                } catch (RemoteException e) {
                    Slog.e(TAG, "Exception while notifying StrongAuthTracker.", e);
                }
            } finally {
                this.mTrackers.finishBroadcast();
            }
        }
    }

    private void notifyStrongAuthTrackersForIsNonStrongBiometricAllowed(boolean z, int i) {
        if (DEBUG) {
            Slog.d(TAG, "notifyStrongAuthTrackersForIsNonStrongBiometricAllowed for allowed=" + z + ", userId=" + i);
        }
        int beginBroadcast = this.mTrackers.beginBroadcast();
        while (beginBroadcast > 0) {
            beginBroadcast--;
            try {
                try {
                    this.mTrackers.getBroadcastItem(beginBroadcast).onIsNonStrongBiometricAllowedChanged(z, i);
                } catch (RemoteException e) {
                    Slog.e(TAG, "Exception while notifying StrongAuthTracker: IsNonStrongBiometricAllowedChanged.", e);
                }
            } finally {
                this.mTrackers.finishBroadcast();
            }
        }
    }

    public void registerStrongAuthTracker(IStrongAuthTracker iStrongAuthTracker) {
        this.mHandler.obtainMessage(2, iStrongAuthTracker).sendToTarget();
    }

    public void unregisterStrongAuthTracker(IStrongAuthTracker iStrongAuthTracker) {
        this.mHandler.obtainMessage(3, iStrongAuthTracker).sendToTarget();
    }

    public void removeUser(int i) {
        this.mHandler.obtainMessage(4, i, 0).sendToTarget();
    }

    public void requireStrongAuth(int i, int i2) {
        if (DEBUG_LOCK) {
            Slog.d("LockSettingsService", "requireStrongAuth  strongAuthReason:" + i + " userId:" + i2 + " (" + Debug.getCallers(8) + ")");
        }
        if (i2 == -1 || i2 >= 0) {
            this.mHandler.obtainMessage(1, i, i2).sendToTarget();
            return;
        }
        throw new IllegalArgumentException("userId must be an explicit user id or USER_ALL");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void noLongerRequireStrongAuth(int i, int i2) {
        if (DEBUG_LOCK) {
            Slog.d("LockSettingsService", "noLongerRequireStrongAuth  strongAuthReason:" + i + " userId:" + i2);
        }
        if (i2 == -1 || i2 >= 0) {
            this.mHandler.obtainMessage(6, i, i2).sendToTarget();
            return;
        }
        throw new IllegalArgumentException("userId must be an explicit user id or USER_ALL");
    }

    public void reportUnlock(int i) {
        requireStrongAuth(0, i);
    }

    public void reportSuccessfulStrongAuthUnlock(int i) {
        this.mHandler.obtainMessage(5, i, 0).sendToTarget();
    }

    public void refreshStrongAuthTimeout(int i) {
        this.mHandler.obtainMessage(10, i, 0).sendToTarget();
    }

    public void reportSuccessfulBiometricUnlock(boolean z, int i) {
        if (DEBUG) {
            Slog.d(TAG, "reportSuccessfulBiometricUnlock for isStrongBiometric=" + z + ", userId=" + i);
        }
        if (z) {
            this.mHandler.obtainMessage(8, i, 0).sendToTarget();
        } else {
            this.mHandler.obtainMessage(7, i, 0).sendToTarget();
        }
    }

    public void scheduleNonStrongBiometricIdleTimeout(int i) {
        if (DEBUG) {
            Slog.d(TAG, "scheduleNonStrongBiometricIdleTimeout for userId=" + i);
        }
        this.mHandler.obtainMessage(9, i, 0).sendToTarget();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @VisibleForTesting
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public class StrongAuthTimeoutAlarmListener implements AlarmManager.OnAlarmListener {
        private long mLatestStrongAuthTime;
        private final int mUserId;

        public StrongAuthTimeoutAlarmListener(long j, int i) {
            this.mLatestStrongAuthTime = j;
            this.mUserId = i;
        }

        public void setLatestStrongAuthTime(long j) {
            this.mLatestStrongAuthTime = j;
        }

        public long getLatestStrongAuthTime() {
            return this.mLatestStrongAuthTime;
        }

        @Override // android.app.AlarmManager.OnAlarmListener
        public void onAlarm() {
            LockSettingsStrongAuth.this.requireStrongAuth(16, this.mUserId);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @VisibleForTesting
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public class NonStrongBiometricTimeoutAlarmListener implements AlarmManager.OnAlarmListener {
        private final int mUserId;

        NonStrongBiometricTimeoutAlarmListener(int i) {
            this.mUserId = i;
        }

        @Override // android.app.AlarmManager.OnAlarmListener
        public void onAlarm() {
            LockSettingsStrongAuth.this.requireStrongAuth(128, this.mUserId);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @VisibleForTesting
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public class NonStrongBiometricIdleTimeoutAlarmListener implements AlarmManager.OnAlarmListener {
        private final int mUserId;

        NonStrongBiometricIdleTimeoutAlarmListener(int i) {
            this.mUserId = i;
        }

        @Override // android.app.AlarmManager.OnAlarmListener
        public void onAlarm() {
            LockSettingsStrongAuth.this.setIsNonStrongBiometricAllowed(false, this.mUserId);
        }
    }

    public void dump(IndentingPrintWriter indentingPrintWriter) {
        indentingPrintWriter.println("PrimaryAuthFlags state:");
        indentingPrintWriter.increaseIndent();
        for (int i = 0; i < this.mStrongAuthForUser.size(); i++) {
            indentingPrintWriter.println("userId=" + this.mStrongAuthForUser.keyAt(i) + ", primaryAuthFlags=" + Integer.toHexString(this.mStrongAuthForUser.valueAt(i)));
        }
        indentingPrintWriter.println();
        indentingPrintWriter.decreaseIndent();
        indentingPrintWriter.println("NonStrongBiometricAllowed state:");
        indentingPrintWriter.increaseIndent();
        for (int i2 = 0; i2 < this.mIsNonStrongBiometricAllowedForUser.size(); i2++) {
            indentingPrintWriter.println("userId=" + this.mIsNonStrongBiometricAllowedForUser.keyAt(i2) + ", allowed=" + this.mIsNonStrongBiometricAllowedForUser.valueAt(i2));
        }
        indentingPrintWriter.println();
        indentingPrintWriter.decreaseIndent();
    }
}
