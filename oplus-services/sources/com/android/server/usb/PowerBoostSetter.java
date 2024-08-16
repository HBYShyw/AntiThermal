package com.android.server.usb;

import android.os.PowerManagerInternal;
import android.util.Log;
import com.android.server.LocalServices;
import java.time.Instant;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class PowerBoostSetter {
    private static final int POWER_BOOST_TIMEOUT_MS = 15000;
    private static final String TAG = "PowerBoostSetter";
    PowerManagerInternal mPowerManagerInternal;
    Instant mPreviousTimeout = null;

    /* JADX INFO: Access modifiers changed from: package-private */
    public PowerBoostSetter() {
        this.mPowerManagerInternal = null;
        this.mPowerManagerInternal = (PowerManagerInternal) LocalServices.getService(PowerManagerInternal.class);
    }

    public void boostPower() {
        if (this.mPowerManagerInternal == null) {
            this.mPowerManagerInternal = (PowerManagerInternal) LocalServices.getService(PowerManagerInternal.class);
        }
        if (this.mPowerManagerInternal == null) {
            Log.w(TAG, "PowerManagerInternal null");
        } else if (this.mPreviousTimeout == null || Instant.now().isAfter(this.mPreviousTimeout.plusMillis(7500L))) {
            this.mPreviousTimeout = Instant.now();
            this.mPowerManagerInternal.setPowerBoost(0, POWER_BOOST_TIMEOUT_MS);
        }
    }
}
