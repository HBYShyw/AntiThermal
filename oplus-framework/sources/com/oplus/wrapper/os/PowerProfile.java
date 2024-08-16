package com.oplus.wrapper.os;

import android.content.Context;

/* loaded from: classes.dex */
public class PowerProfile {
    private final com.android.internal.os.PowerProfile mPowerProfile;

    public PowerProfile(Context context) {
        this.mPowerProfile = new com.android.internal.os.PowerProfile(context);
    }

    public double getBatteryCapacity() {
        return this.mPowerProfile.getBatteryCapacity();
    }

    public double getAveragePower(String type) {
        return this.mPowerProfile.getAveragePower(type);
    }
}
