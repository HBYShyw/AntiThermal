package com.oplus.wrapper.os;

/* loaded from: classes.dex */
public class PowerSaveState {
    private final android.os.PowerSaveState mPowerSaveState;

    /* JADX INFO: Access modifiers changed from: package-private */
    public PowerSaveState(android.os.PowerSaveState powerSaveState) {
        this.mPowerSaveState = powerSaveState;
    }

    public boolean getBatterySaverEnabled() {
        return this.mPowerSaveState.batterySaverEnabled;
    }
}
