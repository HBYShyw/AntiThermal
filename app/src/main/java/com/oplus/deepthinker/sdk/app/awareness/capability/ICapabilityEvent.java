package com.oplus.deepthinker.sdk.app.awareness.capability;

import kotlin.Metadata;

/* compiled from: ICapabilityEvent.kt */
@Metadata(d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\b\n\u0000\bf\u0018\u0000*\u0004\b\u0000\u0010\u00012\u00020\u0002J\r\u0010\u0003\u001a\u00028\u0000H&¢\u0006\u0002\u0010\u0004J\b\u0010\u0005\u001a\u00020\u0006H&¨\u0006\u0007"}, d2 = {"Lcom/oplus/deepthinker/sdk/app/awareness/capability/ICapabilityEvent;", "T", "", "getCapabilityEvent", "()Ljava/lang/Object;", "getCapabilityEventId", "", "com.oplus.deepthinker.sdk_release"}, k = 1, mv = {1, 6, 0}, xi = 48)
/* loaded from: classes.dex */
public interface ICapabilityEvent<T> {
    T getCapabilityEvent();

    int getCapabilityEventId();
}
