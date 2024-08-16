package com.android.internal.policy;

import com.oplus.debug.InputLog;

/* loaded from: classes.dex */
public class PhoneFallbackEventHandlerExtImpl implements IPhoneFallbackEventHandlerExt {
    public PhoneFallbackEventHandlerExtImpl(Object base) {
    }

    public void inputLog(String tag, String msg) {
        if (InputLog.isLevelVerbose()) {
            InputLog.v(tag, msg);
        }
    }
}
