package com.oplus.deepthinker.sdk.app.aidl.eventfountain;

import com.oplus.deepthinker.sdk.app.aidl.eventfountain.IEventCallback;

/* loaded from: classes.dex */
public abstract class EventCallback extends IEventCallback.Stub {
    @Override // com.oplus.deepthinker.sdk.app.aidl.eventfountain.IEventCallback
    public abstract void onEventStateChanged(DeviceEventResult deviceEventResult);
}
