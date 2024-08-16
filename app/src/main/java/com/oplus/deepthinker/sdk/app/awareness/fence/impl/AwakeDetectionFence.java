package com.oplus.deepthinker.sdk.app.awareness.fence.impl;

import com.oplus.deepthinker.sdk.app.awareness.fence.AwarenessFence;
import java.util.UUID;
import kotlin.Metadata;
import za.k;

/* compiled from: AwakeDetectionFence.kt */
@Metadata(d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\bÆ\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\b\u0010\u0005\u001a\u00020\u0006H\u0007R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T¢\u0006\u0002\n\u0000¨\u0006\u0007"}, d2 = {"Lcom/oplus/deepthinker/sdk/app/awareness/fence/impl/AwakeDetectionFence;", "", "()V", "FENCE_NAME", "", "enter", "Lcom/oplus/deepthinker/sdk/app/awareness/fence/AwarenessFence;", "com.oplus.deepthinker.sdk_release"}, k = 1, mv = {1, 6, 0}, xi = 48)
/* loaded from: classes.dex */
public final class AwakeDetectionFence {
    private static final String FENCE_NAME = "awake_detection_fence";
    public static final AwakeDetectionFence INSTANCE = new AwakeDetectionFence();

    private AwakeDetectionFence() {
    }

    public static final AwarenessFence enter() {
        AwarenessFence.Companion companion = AwarenessFence.INSTANCE;
        AwarenessFence.Builder builder = new AwarenessFence.Builder();
        UUID randomUUID = UUID.randomUUID();
        k.d(randomUUID, "randomUUID()");
        builder.setFenceName(k.l("awake_detection_fence_", randomUUID));
        builder.setFenceType("awake_detection_fence");
        return builder.build();
    }
}
