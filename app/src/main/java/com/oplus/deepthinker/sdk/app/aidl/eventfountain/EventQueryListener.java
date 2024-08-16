package com.oplus.deepthinker.sdk.app.aidl.eventfountain;

import com.oplus.deepthinker.sdk.app.aidl.eventfountain.IEventQueryListener;

/* loaded from: classes.dex */
public abstract class EventQueryListener extends IEventQueryListener.Stub {
    @Override // com.oplus.deepthinker.sdk.app.aidl.eventfountain.IEventQueryListener
    public abstract void onFailure(int i10);

    @Override // com.oplus.deepthinker.sdk.app.aidl.eventfountain.IEventQueryListener
    public abstract void onSuccess(DeviceEventResult deviceEventResult);
}
