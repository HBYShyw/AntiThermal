package com.oplus.wrapper.telephony;

/* loaded from: classes.dex */
public class SignalStrength {
    private android.telephony.SignalStrength mSignalStrength;

    public SignalStrength(android.telephony.SignalStrength signalStrength) {
        this.mSignalStrength = signalStrength;
    }

    public int[] getSignalStrengthLevel() {
        return this.mSignalStrength.getSignalStrengthLevel();
    }
}
