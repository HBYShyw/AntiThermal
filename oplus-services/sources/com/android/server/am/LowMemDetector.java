package com.android.server.am;

import com.android.internal.annotations.GuardedBy;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public final class LowMemDetector {
    public static final int ADJ_MEM_FACTOR_NOTHING = -1;
    private static final String TAG = "LowMemDetector";
    private final ActivityManagerService mAm;
    private boolean mAvailable;
    private final LowMemThread mLowMemThread;
    private final Object mPressureStateLock = new Object();

    @GuardedBy({"mPressureStateLock"})
    private int mPressureState = 0;

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public @interface MemFactor {
    }

    private native int init();

    /* JADX INFO: Access modifiers changed from: private */
    public native int waitForPressure();

    /* JADX INFO: Access modifiers changed from: package-private */
    public LowMemDetector(ActivityManagerService activityManagerService) {
        this.mAm = activityManagerService;
        LowMemThread lowMemThread = new LowMemThread();
        this.mLowMemThread = lowMemThread;
        if (init() != 0) {
            this.mAvailable = false;
        } else {
            this.mAvailable = true;
            lowMemThread.start();
        }
    }

    public boolean isAvailable() {
        return this.mAvailable;
    }

    public int getMemFactor() {
        int i;
        synchronized (this.mPressureStateLock) {
            i = this.mPressureState;
        }
        return i;
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    private final class LowMemThread extends Thread {
        private LowMemThread() {
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            while (true) {
                int waitForPressure = LowMemDetector.this.waitForPressure();
                if (waitForPressure == -1) {
                    LowMemDetector.this.mAvailable = false;
                    return;
                } else {
                    synchronized (LowMemDetector.this.mPressureStateLock) {
                        LowMemDetector.this.mPressureState = waitForPressure;
                    }
                }
            }
        }
    }
}
