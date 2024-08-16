package com.oplus.deepthinker.sdk.app.awareness.capability;

import android.os.Bundle;
import kotlin.Metadata;
import za.k;

/* compiled from: BundleCapabilityEvent.kt */
@Metadata(d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001B\u0015\u0012\u0006\u0010\u0003\u001a\u00020\u0002\u0012\u0006\u0010\u0004\u001a\u00020\u0005¢\u0006\u0002\u0010\u0006¨\u0006\u0007"}, d2 = {"Lcom/oplus/deepthinker/sdk/app/awareness/capability/BundleCapabilityEvent;", "Lcom/oplus/deepthinker/sdk/app/awareness/capability/CapabilityEvent;", "Landroid/os/Bundle;", "event", "eventId", "", "(Landroid/os/Bundle;I)V", "com.oplus.deepthinker.sdk_release"}, k = 1, mv = {1, 6, 0}, xi = 48)
/* loaded from: classes.dex */
public final class BundleCapabilityEvent extends CapabilityEvent<Bundle> {
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public BundleCapabilityEvent(Bundle bundle, int i10) {
        super(Bundle.class, bundle, i10);
        k.e(bundle, "event");
    }
}
