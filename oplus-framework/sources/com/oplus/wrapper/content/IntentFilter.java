package com.oplus.wrapper.content;

/* loaded from: classes.dex */
public class IntentFilter {
    private final android.content.IntentFilter mIntentFilter;

    public IntentFilter(android.content.IntentFilter intentFilter) {
        this.mIntentFilter = intentFilter;
    }

    public final void setAutoVerify(boolean autoVerify) {
        this.mIntentFilter.setAutoVerify(autoVerify);
    }
}
