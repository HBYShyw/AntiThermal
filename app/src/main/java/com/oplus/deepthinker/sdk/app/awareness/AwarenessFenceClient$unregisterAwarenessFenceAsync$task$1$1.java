package com.oplus.deepthinker.sdk.app.awareness;

import android.os.Bundle;
import com.oplus.deepthinker.sdk.app.DataLinkConstants;
import com.oplus.deepthinker.sdk.app.aidl.eventfountain.EventType;
import com.oplus.deepthinker.sdk.app.api.InternalApiCall;
import kotlin.Metadata;
import za.Lambda;

/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: AwarenessFenceClient.kt */
@Metadata(d1 = {"\u0000\b\n\u0000\n\u0002\u0018\u0002\n\u0000\u0010\u0000\u001a\u00020\u0001H\n¢\u0006\u0002\b\u0002"}, d2 = {"<anonymous>", "Landroid/os/Bundle;", "invoke"}, k = 3, mv = {1, 6, 0}, xi = 48)
/* loaded from: classes.dex */
public final class AwarenessFenceClient$unregisterAwarenessFenceAsync$task$1$1 extends Lambda implements ya.a<Bundle> {
    public static final AwarenessFenceClient$unregisterAwarenessFenceAsync$task$1$1 INSTANCE = new AwarenessFenceClient$unregisterAwarenessFenceAsync$task$1$1();

    AwarenessFenceClient$unregisterAwarenessFenceAsync$task$1$1() {
        super(0);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // ya.a
    public final Bundle invoke() {
        Bundle bundle = new Bundle();
        InternalApiCall.Companion companion = InternalApiCall.INSTANCE;
        companion.putApiCode(bundle, DataLinkConstants.AWARENESS_FENCE);
        companion.putFunction(bundle, 102);
        companion.putEventType(bundle, EventType.AWARENESS_FENCE);
        return bundle;
    }
}
