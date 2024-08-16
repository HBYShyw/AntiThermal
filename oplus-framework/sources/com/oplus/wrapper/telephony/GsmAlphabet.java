package com.oplus.wrapper.telephony;

/* loaded from: classes.dex */
public class GsmAlphabet {
    private GsmAlphabet() {
    }

    public static int charToGsm(char c) {
        return com.android.internal.telephony.GsmAlphabet.charToGsm(c);
    }

    public static int charToGsmExtended(char c) {
        return com.android.internal.telephony.GsmAlphabet.charToGsmExtended(c);
    }
}
