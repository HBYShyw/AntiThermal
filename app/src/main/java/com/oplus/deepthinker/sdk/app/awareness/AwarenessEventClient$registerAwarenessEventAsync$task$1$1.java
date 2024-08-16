package com.oplus.deepthinker.sdk.app.awareness;

import android.os.Bundle;
import com.oplus.deepthinker.sdk.app.DataLinkConstants;
import com.oplus.deepthinker.sdk.app.aidl.eventfountain.EventType;
import com.oplus.deepthinker.sdk.app.api.InternalApiCall;
import com.oplus.deepthinker.sdk.app.awareness.capability.CapabilityEventCategory;
import kotlin.Metadata;
import za.Lambda;

/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: AwarenessEventClient.kt */
@Metadata(d1 = {"\u0000\b\n\u0000\n\u0002\u0018\u0002\n\u0000\u0010\u0000\u001a\u00020\u0001H\n¢\u0006\u0002\b\u0002"}, d2 = {"<anonymous>", "Landroid/os/Bundle;", "invoke"}, k = 3, mv = {1, 6, 0}, xi = 48)
/* loaded from: classes.dex */
public final class AwarenessEventClient$registerAwarenessEventAsync$task$1$1 extends Lambda implements ya.a<Bundle> {
    final /* synthetic */ CapabilityEventCategory $category;
    final /* synthetic */ AwarenessEventClient this$0;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public AwarenessEventClient$registerAwarenessEventAsync$task$1$1(AwarenessEventClient awarenessEventClient, CapabilityEventCategory capabilityEventCategory) {
        super(0);
        this.this$0 = awarenessEventClient;
        this.$category = capabilityEventCategory;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // ya.a
    public final Bundle invoke() {
        Bundle bundle = new Bundle();
        AwarenessEventClient awarenessEventClient = this.this$0;
        CapabilityEventCategory capabilityEventCategory = this.$category;
        InternalApiCall.Companion companion = InternalApiCall.INSTANCE;
        companion.putApiCode(bundle, DataLinkConstants.AWARENESS_EVENT);
        companion.putFunction(bundle, 101);
        companion.putEventType(bundle, EventType.AWARENESS_CAPABILITY);
        awarenessEventClient.putEventCategory(bundle, capabilityEventCategory);
        return bundle;
    }
}
