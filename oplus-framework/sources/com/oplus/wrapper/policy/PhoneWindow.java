package com.oplus.wrapper.policy;

import android.content.Context;
import android.view.Window;

/* loaded from: classes.dex */
public class PhoneWindow {
    private final com.android.internal.policy.PhoneWindow mPhoneWindow;

    public PhoneWindow(Context context) {
        this.mPhoneWindow = new com.android.internal.policy.PhoneWindow(context);
    }

    public Window getWindow() {
        return this.mPhoneWindow;
    }
}
