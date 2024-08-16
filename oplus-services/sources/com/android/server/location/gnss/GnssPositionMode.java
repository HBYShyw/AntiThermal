package com.android.server.location.gnss;

import java.util.Arrays;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class GnssPositionMode {
    private final boolean mLowPowerMode;
    private final int mMinInterval;
    private final int mMode;
    private final int mPreferredAccuracy;
    private final int mPreferredTime;
    private final int mRecurrence;

    public GnssPositionMode(int i, int i2, int i3, int i4, int i5, boolean z) {
        this.mMode = i;
        this.mRecurrence = i2;
        this.mMinInterval = i3;
        this.mPreferredAccuracy = i4;
        this.mPreferredTime = i5;
        this.mLowPowerMode = z;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof GnssPositionMode)) {
            return false;
        }
        GnssPositionMode gnssPositionMode = (GnssPositionMode) obj;
        return this.mMode == gnssPositionMode.mMode && this.mRecurrence == gnssPositionMode.mRecurrence && this.mMinInterval == gnssPositionMode.mMinInterval && this.mPreferredAccuracy == gnssPositionMode.mPreferredAccuracy && this.mPreferredTime == gnssPositionMode.mPreferredTime && this.mLowPowerMode == gnssPositionMode.mLowPowerMode && getClass() == gnssPositionMode.getClass();
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{Integer.valueOf(this.mMode), Integer.valueOf(this.mRecurrence), Integer.valueOf(this.mMinInterval), Integer.valueOf(this.mPreferredAccuracy), Integer.valueOf(this.mPreferredTime), Boolean.valueOf(this.mLowPowerMode), getClass()});
    }
}
