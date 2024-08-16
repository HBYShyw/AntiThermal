package com.android.server.hdmi;

import android.os.PowerManagerInternal;
import com.android.server.LocalServices;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class PowerManagerInternalWrapper {
    private static final String TAG = "PowerManagerInternalWrapper";
    private PowerManagerInternal mPowerManagerInternal = (PowerManagerInternal) LocalServices.getService(PowerManagerInternal.class);

    public boolean wasDeviceIdleFor(long j) {
        return this.mPowerManagerInternal.wasDeviceIdleFor(j);
    }
}
