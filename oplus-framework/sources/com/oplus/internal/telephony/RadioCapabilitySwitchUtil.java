package com.oplus.internal.telephony;

import android.os.SystemProperties;

/* loaded from: classes.dex */
public class RadioCapabilitySwitchUtil {
    private static final String PROPERTY_CAPABILITY_SWITCH = "persist.vendor.radio.simswitch";

    public static int getMainCapabilityPhoneId() {
        int phoneId = SystemProperties.getInt("persist.vendor.radio.simswitch", 1) - 1;
        return phoneId;
    }
}
