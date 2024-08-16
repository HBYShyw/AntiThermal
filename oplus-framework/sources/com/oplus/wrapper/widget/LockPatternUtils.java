package com.oplus.wrapper.widget;

import android.content.Context;
import android.os.RemoteException;
import android.util.Log;

/* loaded from: classes.dex */
public class LockPatternUtils {
    public static final String PASSWORD_TYPE_KEY = getPasswordTypeKey();
    private static final String TAG = "LockPatternUtils";
    private final com.android.internal.widget.LockPatternUtils mLockPatternUtils;

    public LockPatternUtils(Context context) {
        this.mLockPatternUtils = new com.android.internal.widget.LockPatternUtils(context);
    }

    private static String getPasswordTypeKey() {
        return "lockscreen.password_type";
    }

    public void sanitizePassword() {
        try {
            this.mLockPatternUtils.getLockSettings().sanitizePassword();
        } catch (RemoteException re) {
            Log.e(TAG, "Couldn't sanitize password" + re);
        }
    }

    public void reportSuccessfulPasswordAttempt(int userId) {
        this.mLockPatternUtils.reportSuccessfulPasswordAttempt(userId);
    }

    public long setLockoutAttemptDeadline(int userId, int timeoutMs) {
        return this.mLockPatternUtils.setLockoutAttemptDeadline(userId, timeoutMs);
    }

    public boolean isVisiblePatternEnabled(int userId) {
        return this.mLockPatternUtils.isVisiblePatternEnabled(userId);
    }

    public boolean isLockPasswordEnabled(int userId) {
        return this.mLockPatternUtils.isLockPasswordEnabled(userId);
    }

    public boolean isLockPatternEnabled(int userId) {
        return this.mLockPatternUtils.isLockPatternEnabled(userId);
    }

    public boolean isLockScreenDisabled(int userId) {
        return this.mLockPatternUtils.isLockScreenDisabled(userId);
    }

    public boolean isSecure(int userId) {
        return this.mLockPatternUtils.isSecure(userId);
    }
}
