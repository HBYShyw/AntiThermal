package com.android.internal.widget;

/* loaded from: classes.dex */
public abstract class OplusBaseLockPatternUtils {
    public static final String LOCKOUT_ATTEMPT_DEADLINE = "lockscreen.lockoutattemptdeadline";
    public static final String LOCKOUT_ATTEMPT_TIMEOUT_MS = "lockscreen.lockoutattempttimeoutmss";
    public static final String LOCKSCREEN_TIMEOUT_FLAG = "lockscreen.timeout_flag";

    public boolean getTimeoutFlag(int userId) {
        return false;
    }

    public void setTimeoutFlag(boolean flag, int userId) {
    }
}
