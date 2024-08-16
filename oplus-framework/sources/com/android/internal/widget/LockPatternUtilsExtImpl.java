package com.android.internal.widget;

import android.app.ActivityThread;
import android.content.ContentResolver;
import android.os.SystemClock;
import android.provider.Settings;
import android.util.Log;
import android.widget.ILockPatternUtilsWrapper;

/* loaded from: classes.dex */
public class LockPatternUtilsExtImpl implements ILockPatternUtilsExt {
    public static final String LOCKOUT_ATTEMPT_DEADLINE = "lockscreen.lockoutattemptdeadline";
    public static final String LOCKOUT_ATTEMPT_TIMEOUT_MS = "lockscreen.lockoutattempttimeoutmss";
    public static final String LOCKSCREEN_TIMEOUT_FLAG = "lockscreen.timeout_flag";
    private LockPatternUtils mLockPatternUtils;

    public LockPatternUtilsExtImpl(Object base) {
        this.mLockPatternUtils = (LockPatternUtils) base;
    }

    public boolean getTimeoutFlag(int userId, LockPatternUtils utils) {
        return utils.getWrapper().getBoolean("lockscreen.timeout_flag", false, userId);
    }

    public void setTimeoutFlag(boolean flag, int userId, LockPatternUtils utils) {
        utils.getWrapper().setBoolean("lockscreen.timeout_flag", flag, userId);
    }

    public long getLockoutAttemptDeadline(int userId, LockPatternUtils utils) {
        synchronized (utils) {
            ILockPatternUtilsWrapper wrapper = utils.getWrapper();
            long deadline = wrapper.getLong("lockscreen.lockoutattemptdeadline", 0L, userId);
            long timeoutMs = wrapper.getLong("lockscreen.lockoutattempttimeoutmss", 0L, userId);
            long now = SystemClock.elapsedRealtime();
            if (deadline < now && deadline != 0) {
                wrapper.setLong("lockscreen.lockoutattemptdeadline", 0L, userId);
                wrapper.setLong("lockscreen.lockoutattempttimeoutmss", 0L, userId);
                return 0L;
            }
            if (deadline > now + timeoutMs) {
                deadline = now + timeoutMs;
                wrapper.setLong("lockscreen.lockoutattemptdeadline", deadline, userId);
                if (wrapper.getDebug()) {
                    Log.d(wrapper.getTag(), "getLockoutAttemptDeadline: now= " + now + "deadline= " + deadline);
                }
            }
            return deadline;
        }
    }

    public void setLockoutAttemptDeadline(int userId, long deadline, int timeoutMs, LockPatternUtils utils) {
        synchronized (utils) {
            ILockPatternUtilsWrapper wrapper = utils.getWrapper();
            wrapper.setLong("lockscreen.lockoutattemptdeadline", deadline, userId);
            wrapper.setLong("lockscreen.lockoutattempttimeoutmss", timeoutMs, userId);
            if (wrapper.getDebug()) {
                Log.d(wrapper.getTag(), "setLockoutAttemptDeadline: userId =" + userId + " timeoutMs =" + timeoutMs);
            }
        }
    }

    public void savePasswordLenght(ContentResolver resover, String key, int value, int userId) {
        if (ActivityThread.currentApplication() != null && ActivityThread.currentApplication().getApplicationInfo() != null && (ActivityThread.currentApplication().getApplicationInfo().privateFlags & 8) != 0) {
            Settings.System.putIntForUser(resover, key, value, userId);
        }
    }
}
