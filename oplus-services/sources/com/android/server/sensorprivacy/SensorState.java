package com.android.server.sensorprivacy;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class SensorState {
    private long mLastChange;
    private int mStateType;

    private static int enabledToState(boolean z) {
        return z ? 1 : 2;
    }

    SensorState(int i) {
        this.mStateType = i;
        this.mLastChange = SensorPrivacyService.getCurrentTimeMillis();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public SensorState(int i, long j) {
        this.mStateType = i;
        this.mLastChange = Math.min(SensorPrivacyService.getCurrentTimeMillis(), j);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public SensorState(SensorState sensorState) {
        this.mStateType = sensorState.getState();
        this.mLastChange = sensorState.getLastChange();
    }

    boolean setState(int i) {
        if (this.mStateType == i) {
            return false;
        }
        this.mStateType = i;
        this.mLastChange = SensorPrivacyService.getCurrentTimeMillis();
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getState() {
        return this.mStateType;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public long getLastChange() {
        return this.mLastChange;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public SensorState(boolean z) {
        this(enabledToState(z));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean setEnabled(boolean z) {
        return setState(enabledToState(z));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isEnabled() {
        return getState() == 1;
    }
}
