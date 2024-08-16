package com.oplus.deepthinker.sdk.app.feature.eventassociation;

import java.io.Serializable;

/* loaded from: classes.dex */
public class PerformanceOfLongCharging implements Serializable {
    private static final long serialVersionUID = 3578136292978560538L;
    private long mDuration;
    private float mProbabilityOfApps;
    private float mProbabilityOfLocation;
    private float mProbabilityOfTime;
    private float mProbabilityOfTotal;

    public long getDuration() {
        return this.mDuration;
    }

    public float getProbabilityOfApps() {
        return this.mProbabilityOfApps;
    }

    public float getProbabilityOfLocation() {
        return this.mProbabilityOfLocation;
    }

    public float getProbabilityOfTime() {
        return this.mProbabilityOfTime;
    }

    public float getProbabilityOfTotal() {
        return this.mProbabilityOfTotal;
    }

    public void setDuration(long j10) {
        this.mDuration = j10;
    }

    public void setProbabilityOfLocation(float f10) {
        this.mProbabilityOfLocation = f10;
    }

    public void setProbabilityOfTime(float f10) {
        this.mProbabilityOfTime = f10;
    }

    public void setProbabilityOfTotal(float f10) {
        this.mProbabilityOfTotal = f10;
    }

    public void setPropOfApps(float f10) {
        this.mProbabilityOfApps = f10;
    }

    public String toString() {
        return "PerformanceOfLongCharging{probabilityOfLocation=" + this.mProbabilityOfLocation + ", probabilityOfApps=" + this.mProbabilityOfApps + ", probabilityOfTime=" + this.mProbabilityOfTime + ", probabilityOfTotal=" + this.mProbabilityOfTotal + ", duration=" + this.mDuration + '}';
    }
}
