package com.oplus.os;

import android.os.SystemProperties;

/* loaded from: classes.dex */
public class OplusSystemProperties {
    public static void set(String key, String val) {
        SystemProperties.set(key, val);
    }
}
