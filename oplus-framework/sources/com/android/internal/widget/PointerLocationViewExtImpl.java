package com.android.internal.widget;

import com.oplus.debug.InputLog;

/* loaded from: classes.dex */
public class PointerLocationViewExtImpl implements IPointerLocationViewExt {
    public PointerLocationViewExtImpl(Object base) {
    }

    public boolean enableInputLogV() {
        return true;
    }

    public boolean inputLogd(String tag, String msg) {
        if (InputLog.getCurrentLogSwitchValue() == 1) {
            InputLog.d(tag, msg);
            return true;
        }
        return false;
    }
}
