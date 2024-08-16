package com.oplus.wrapper.telephony;

/* loaded from: classes.dex */
public class Phone {
    public static final int PREFERRED_NT_MODE = getPreferredNtMode();

    private static int getPreferredNtMode() {
        return com.android.internal.telephony.Phone.PREFERRED_NT_MODE;
    }
}
