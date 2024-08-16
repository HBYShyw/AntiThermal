package com.android.server.hdmi;

import android.content.Context;
import android.os.PowerManager;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class PowerManagerWrapper {
    private static final String TAG = "PowerManagerWrapper";
    private final PowerManager mPowerManager;

    public PowerManagerWrapper(Context context) {
        this.mPowerManager = (PowerManager) context.getSystemService(PowerManager.class);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isInteractive() {
        return this.mPowerManager.isInteractive();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void wakeUp(long j, int i, String str) {
        this.mPowerManager.wakeUp(j, i, str);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void goToSleep(long j, int i, int i2) {
        this.mPowerManager.goToSleep(j, i, i2);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public PowerManager.WakeLock newWakeLock(int i, String str) {
        return this.mPowerManager.newWakeLock(i, str);
    }
}
