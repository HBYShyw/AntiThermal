package com.android.server.cpu;

import android.util.IntArray;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public final class CpuAvailabilityMonitoringConfig {
    public static final int CPUSET_ALL = 1;
    public static final int CPUSET_BACKGROUND = 2;
    public final int cpuset;
    private final IntArray mThresholds;

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public @interface Cpuset {
    }

    public IntArray getThresholds() {
        return this.mThresholds;
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static final class Builder {
        private final int mCpuset;
        private final IntArray mThresholds = new IntArray();

        public Builder(int i, int... iArr) {
            this.mCpuset = i;
            for (int i2 : iArr) {
                addThreshold(i2);
            }
        }

        public Builder addThreshold(int i) {
            if (this.mThresholds.indexOf(i) == -1) {
                this.mThresholds.add(i);
            }
            return this;
        }

        public CpuAvailabilityMonitoringConfig build() {
            return new CpuAvailabilityMonitoringConfig(this);
        }
    }

    public String toString() {
        return "CpuAvailabilityMonitoringConfig{cpuset=" + toCpusetString(this.cpuset) + ", mThresholds=" + this.mThresholds + ')';
    }

    public static String toCpusetString(int i) {
        if (i == 1) {
            return "CPUSET_ALL";
        }
        if (i == 2) {
            return "CPUSET_BACKGROUND";
        }
        return "Invalid cpuset: " + i;
    }

    private CpuAvailabilityMonitoringConfig(Builder builder) {
        if (builder.mCpuset != 1 && builder.mCpuset != 2) {
            throw new IllegalStateException("Cpuset must be either CPUSET_ALL (1) or CPUSET_BACKGROUND (2). Builder contains " + builder.mCpuset);
        }
        if (builder.mThresholds.size() == 0) {
            throw new IllegalStateException("Must provide at least one threshold");
        }
        this.cpuset = builder.mCpuset;
        this.mThresholds = builder.mThresholds.clone();
    }
}
