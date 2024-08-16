package com.android.server.biometrics.sensors.fingerprint.hidl;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.audio.common.V2_0.AudioFormat;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.util.Slog;
import android.util.SparseBooleanArray;
import android.util.SparseIntArray;
import com.android.server.biometrics.sensors.LockoutTracker;
import com.android.server.biometrics.sensors.fingerprint.IOplusFingerUtilsExt;
import system.ext.loader.core.ExtLoader;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class LockoutFrameworkImpl implements LockoutTracker {
    private static final String ACTION_LOCKOUT_RESET = "com.android.server.biometrics.sensors.fingerprint.ACTION_LOCKOUT_RESET";
    private static final long FAIL_LOCKOUT_TIMEOUT_MS = 30000;
    private static final String KEY_LOCKOUT_RESET_USER = "lockout_reset_user";
    private static final int MAX_FAILED_ATTEMPTS_LOCKOUT_PERMANENT = 20;
    private static final int MAX_FAILED_ATTEMPTS_LOCKOUT_TIMED = 5;
    private static final String TAG = "LockoutTracker";
    private static IOplusFingerUtilsExt mOplusFingerUtilsExt = (IOplusFingerUtilsExt) ExtLoader.type(IOplusFingerUtilsExt.class).create();
    private final AlarmManager mAlarmManager;
    private final Context mContext;
    private final Handler mHandler;
    private final LockoutReceiver mLockoutReceiver;
    private final LockoutResetCallback mLockoutResetCallback;
    private boolean mSetLockoutAlarm = false;
    private boolean mCancelLockoutAlarm = false;
    private final SparseBooleanArray mTimedLockoutCleared = new SparseBooleanArray();
    private final SparseIntArray mFailedAttempts = new SparseIntArray();

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public interface LockoutResetCallback {
        void onLockoutReset(int i);
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    private final class LockoutReceiver extends BroadcastReceiver {
        private LockoutReceiver() {
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            Slog.v(LockoutFrameworkImpl.TAG, "Resetting lockout: " + intent.getAction());
            if (LockoutFrameworkImpl.ACTION_LOCKOUT_RESET.equals(intent.getAction())) {
                int intExtra = intent.getIntExtra(LockoutFrameworkImpl.KEY_LOCKOUT_RESET_USER, 0);
                LockoutFrameworkImpl.this.mCancelLockoutAlarm = true;
                LockoutFrameworkImpl.this.cancelLockoutResetForUser(intExtra);
                LockoutFrameworkImpl.this.resetFailedAttemptsForUser(false, intExtra);
            }
        }
    }

    public LockoutFrameworkImpl(Context context, LockoutResetCallback lockoutResetCallback) {
        this.mContext = context;
        this.mLockoutResetCallback = lockoutResetCallback;
        this.mAlarmManager = (AlarmManager) context.getSystemService(AlarmManager.class);
        LockoutReceiver lockoutReceiver = new LockoutReceiver();
        this.mLockoutReceiver = lockoutReceiver;
        this.mHandler = new Handler(Looper.getMainLooper());
        context.registerReceiver(lockoutReceiver, new IntentFilter(ACTION_LOCKOUT_RESET), "android.permission.RESET_FINGERPRINT_LOCKOUT", null, 2);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void resetFailedAttemptsForUser(boolean z, int i) {
        if (getLockoutModeForUser(i) != 0) {
            Slog.v(TAG, "Reset biometric lockout for user: " + i + ", clearAttemptCounter: " + z);
        }
        if (z) {
            this.mFailedAttempts.put(i, 0);
        }
        this.mTimedLockoutCleared.put(i, true);
        if (this.mSetLockoutAlarm && !this.mCancelLockoutAlarm) {
            cancelLockoutResetForUser(i);
        }
        this.mCancelLockoutAlarm = false;
        this.mSetLockoutAlarm = false;
        this.mLockoutResetCallback.onLockoutReset(i);
        mOplusFingerUtilsExt.notifyResetLockoutAttemptDeadline(0L, i);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void addFailedAttemptForUser(int i) {
        SparseIntArray sparseIntArray = this.mFailedAttempts;
        sparseIntArray.put(i, sparseIntArray.get(i, 0) + 1);
        this.mTimedLockoutCleared.put(i, false);
        if (getLockoutModeForUser(i) != 0) {
            scheduleLockoutResetForUser(i);
        }
    }

    @Override // com.android.server.biometrics.sensors.LockoutTracker
    public int getLockoutModeForUser(int i) {
        int i2 = this.mFailedAttempts.get(i, 0);
        if (i2 >= 20) {
            return 2;
        }
        return (i2 <= 0 || this.mTimedLockoutCleared.get(i, false) || i2 % 5 != 0) ? 0 : 1;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void cancelLockoutResetForUser(int i) {
        this.mAlarmManager.cancel(getLockoutResetIntentForUser(i));
    }

    private void scheduleLockoutResetForUser(final int i) {
        this.mSetLockoutAlarm = true;
        this.mHandler.post(new Runnable() { // from class: com.android.server.biometrics.sensors.fingerprint.hidl.LockoutFrameworkImpl$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                LockoutFrameworkImpl.this.lambda$scheduleLockoutResetForUser$0(i);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$scheduleLockoutResetForUser$0(int i) {
        this.mAlarmManager.setExact(2, SystemClock.elapsedRealtime() + 30000, getLockoutResetIntentForUser(i));
    }

    private PendingIntent getLockoutResetIntentForUser(int i) {
        return PendingIntent.getBroadcast(this.mContext, i, new Intent(ACTION_LOCKOUT_RESET).putExtra(KEY_LOCKOUT_RESET_USER, i), AudioFormat.DTS_HD);
    }
}
