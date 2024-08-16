package com.oplus.deepthinker.sdk.app.awareness.capability.impl;

import com.oplus.deepthinker.sdk.app.awareness.capability.AwarenessEventQueryListener;
import com.oplus.deepthinker.sdk.app.awareness.capability.CapabilityEvent;
import kotlin.Metadata;
import za.k;

/* compiled from: AwakeDetectionEventListener.kt */
@Metadata(bv = {}, d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0006\b&\u0018\u00002\u00020\u0001B\u0007¢\u0006\u0004\b\r\u0010\u000eJ\u0010\u0010\u0005\u001a\u00020\u00042\u0006\u0010\u0003\u001a\u00020\u0002H&J\u0010\u0010\b\u001a\u00020\u00042\u0006\u0010\u0007\u001a\u00020\u0006H&J\u0012\u0010\u000b\u001a\u00020\u00042\n\u0010\n\u001a\u0006\u0012\u0002\b\u00030\tJ\u000e\u0010\f\u001a\u00020\u00042\u0006\u0010\u0007\u001a\u00020\u0006¨\u0006\u000f"}, d2 = {"Lcom/oplus/deepthinker/sdk/app/awareness/capability/impl/AwakeDetectionEventListener;", "Lcom/oplus/deepthinker/sdk/app/awareness/capability/AwarenessEventQueryListener;", "Lcom/oplus/deepthinker/sdk/app/awareness/capability/impl/AwakeDetectionEvent;", "event", "Lma/f0;", "onQueryAwakeDetectionEventSuccess", "", "errorCode", "onQueryAwakeDetectionEventFailure", "Lcom/oplus/deepthinker/sdk/app/awareness/capability/CapabilityEvent;", "capabilityEvent", "onQueryEventSuccess", "onQueryEventFailure", "<init>", "()V", "com.oplus.deepthinker.sdk_release"}, k = 1, mv = {1, 6, 0})
/* loaded from: classes.dex */
public abstract class AwakeDetectionEventListener extends AwarenessEventQueryListener {
    public abstract void onQueryAwakeDetectionEventFailure(int i10);

    public abstract void onQueryAwakeDetectionEventSuccess(AwakeDetectionEvent awakeDetectionEvent);

    @Override // com.oplus.deepthinker.sdk.app.awareness.capability.AwarenessEventQueryListener
    public final void onQueryEventFailure(int i10) {
        onQueryAwakeDetectionEventFailure(i10);
    }

    @Override // com.oplus.deepthinker.sdk.app.awareness.capability.AwarenessEventQueryListener
    public final void onQueryEventSuccess(CapabilityEvent<?> capabilityEvent) {
        k.e(capabilityEvent, "capabilityEvent");
        Object capabilityEvent2 = capabilityEvent.getCapabilityEvent();
        if (capabilityEvent2 instanceof AwakeDetectionEvent) {
            onQueryAwakeDetectionEventSuccess((AwakeDetectionEvent) capabilityEvent2);
        } else {
            onQueryAwakeDetectionEventFailure(32);
        }
    }
}
