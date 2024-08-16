package com.oplus.wrapper.telephony;

import android.os.Looper;

/* loaded from: classes.dex */
public class PhoneStateListener {
    public static final int LISTEN_PRECISE_CALL_STATE = getListenPreciseCallState();
    private final android.telephony.PhoneStateListener mPhoneStateListener;

    public PhoneStateListener(Looper looper) {
        this.mPhoneStateListener = new android.telephony.PhoneStateListener(looper);
    }

    public android.telephony.PhoneStateListener getPhoneStateListener() {
        return this.mPhoneStateListener;
    }

    private static int getListenPreciseCallState() {
        return 2048;
    }
}
