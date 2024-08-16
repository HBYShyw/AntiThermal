package com.oplus.deepthinker.sdk.app.awareness.fence.impl;

import android.os.Bundle;
import com.oplus.deepthinker.sdk.app.awareness.fence.AwarenessFence;
import java.util.UUID;
import kotlin.Metadata;
import za.k;

/* compiled from: ProvinceCityFence.kt */
@Metadata(d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\bÆ\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u0010\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\bH\u0007R\u000e\u0010\u0003\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\bX\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\bX\u0086T¢\u0006\u0002\n\u0000¨\u0006\u000e"}, d2 = {"Lcom/oplus/deepthinker/sdk/app/awareness/fence/impl/ProvinceCityFence;", "", "()V", "BUNDLE_KEY_TYPE", "", "FENCE_NAME", "FENCE_TYPE", "TYPE_CITY", "", "TYPE_PROVINCE", "TYPE_UNKNOWN", "enter", "Lcom/oplus/deepthinker/sdk/app/awareness/fence/AwarenessFence;", "type", "com.oplus.deepthinker.sdk_release"}, k = 1, mv = {1, 6, 0}, xi = 48)
/* loaded from: classes.dex */
public final class ProvinceCityFence {
    public static final String BUNDLE_KEY_TYPE = "type";
    public static final String FENCE_NAME = "province_city_fence";
    public static final String FENCE_TYPE = "province_city_fence";
    public static final ProvinceCityFence INSTANCE = new ProvinceCityFence();
    public static final int TYPE_CITY = 10010;
    public static final int TYPE_PROVINCE = 10011;
    public static final int TYPE_UNKNOWN = 0;

    private ProvinceCityFence() {
    }

    public static final AwarenessFence enter(int type) {
        AwarenessFence.Companion companion = AwarenessFence.INSTANCE;
        AwarenessFence.Builder builder = new AwarenessFence.Builder();
        UUID randomUUID = UUID.randomUUID();
        k.d(randomUUID, "randomUUID()");
        builder.setFenceName(k.l("province_city_fence_", randomUUID));
        builder.setFenceType("province_city_fence");
        Bundle bundle = new Bundle();
        bundle.putInt("type", type);
        builder.setFenceArgs(bundle);
        builder.setFenceCategory(1);
        return builder.build();
    }
}
