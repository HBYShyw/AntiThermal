package com.android.server.notification;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class RateEstimator {
    private static final double MINIMUM_DT = 5.0E-4d;
    private static final double RATE_ALPHA = 0.8d;
    private double mInterarrivalTime = 1000.0d;
    private Long mLastEventTime;

    public float update(long j) {
        float f;
        if (this.mLastEventTime == null) {
            f = 0.0f;
        } else {
            double interarrivalEstimate = getInterarrivalEstimate(j);
            this.mInterarrivalTime = interarrivalEstimate;
            f = (float) (1.0d / interarrivalEstimate);
        }
        this.mLastEventTime = Long.valueOf(j);
        return f;
    }

    public float getRate(long j) {
        if (this.mLastEventTime == null) {
            return 0.0f;
        }
        return (float) (1.0d / getInterarrivalEstimate(j));
    }

    private double getInterarrivalEstimate(long j) {
        return (this.mInterarrivalTime * RATE_ALPHA) + (Math.max((j - this.mLastEventTime.longValue()) / 1000.0d, MINIMUM_DT) * 0.19999999999999996d);
    }
}
