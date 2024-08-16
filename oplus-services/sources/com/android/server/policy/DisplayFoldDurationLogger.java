package com.android.server.policy;

import android.metrics.LogMaker;
import android.os.SystemClock;
import com.android.internal.logging.MetricsLogger;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
class DisplayFoldDurationLogger {
    private static final int LOG_SUBTYPE_DURATION_MASK = Integer.MIN_VALUE;
    private static final int LOG_SUBTYPE_FOLDED = 1;
    private static final int LOG_SUBTYPE_UNFOLDED = 0;
    static final int SCREEN_STATE_OFF = 0;
    static final int SCREEN_STATE_ON_FOLDED = 2;
    static final int SCREEN_STATE_ON_UNFOLDED = 1;
    static final int SCREEN_STATE_UNKNOWN = -1;
    private volatile int mScreenState = -1;
    private volatile Long mLastChanged = null;
    private final MetricsLogger mLogger = new MetricsLogger();

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public @interface ScreenState {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onFinishedWakingUp(Boolean bool) {
        if (bool == null) {
            this.mScreenState = -1;
        } else if (bool.booleanValue()) {
            this.mScreenState = 2;
        } else {
            this.mScreenState = 1;
        }
        this.mLastChanged = Long.valueOf(SystemClock.uptimeMillis());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onFinishedGoingToSleep() {
        log();
        this.mScreenState = 0;
        this.mLastChanged = null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setDeviceFolded(boolean z) {
        if (isOn()) {
            log();
            this.mScreenState = z ? 2 : 1;
            this.mLastChanged = Long.valueOf(SystemClock.uptimeMillis());
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void logFocusedAppWithFoldState(boolean z, String str) {
        this.mLogger.write(new LogMaker(1594).setType(4).setSubtype(z ? 1 : 0).setPackageName(str));
    }

    private void log() {
        int i;
        Long l = this.mLastChanged;
        if (l == null) {
            return;
        }
        int i2 = this.mScreenState;
        if (i2 == 1) {
            i = Integer.MIN_VALUE;
        } else if (i2 != 2) {
            return;
        } else {
            i = -2147483647;
        }
        this.mLogger.write(new LogMaker(1594).setType(4).setSubtype(i).setLatency(SystemClock.uptimeMillis() - l.longValue()));
    }

    private boolean isOn() {
        return this.mScreenState == 1 || this.mScreenState == 2;
    }
}
