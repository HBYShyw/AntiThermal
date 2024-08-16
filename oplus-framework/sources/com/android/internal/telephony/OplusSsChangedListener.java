package com.android.internal.telephony;

import android.telephony.ServiceState;

/* loaded from: classes.dex */
public abstract class OplusSsChangedListener implements IOplusSsChangedListener {
    public abstract void onServiceStateChanged(int i, ServiceState serviceState, ServiceState serviceState2);
}
