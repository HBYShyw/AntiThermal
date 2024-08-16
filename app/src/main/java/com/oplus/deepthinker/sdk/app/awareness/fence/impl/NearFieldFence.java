package com.oplus.deepthinker.sdk.app.awareness.fence.impl;

import android.os.Bundle;
import com.oplus.deepthinker.sdk.app.awareness.fence.AwarenessFence;
import com.oplus.thermalcontrol.config.ThermalBaseConfig;
import java.util.UUID;
import kotlin.Metadata;
import za.k;

/* compiled from: NearFieldFence.kt */
@Metadata(d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0016\n\u0002\u0018\u0002\n\u0002\b\u0007\bÆ\u0002\u0018\u00002\u00020\u0001:\u0001$B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u0010\u0010\u001d\u001a\u00020\u001e2\u0006\u0010\u001f\u001a\u00020\u0004H\u0007J\u0010\u0010 \u001a\u00020\u001e2\u0006\u0010!\u001a\u00020\u0004H\u0007J\u0010\u0010\"\u001a\u00020\u001e2\u0006\u0010\u001f\u001a\u00020\u0004H\u0007J\u0010\u0010#\u001a\u00020\u001e2\u0006\u0010!\u001a\u00020\u0004H\u0007R\u000e\u0010\u0003\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0007X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0007X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u0007X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\u0004X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\u0004X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u0007X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u0007X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0012\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0013\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0014\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0015\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0016\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0017\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0018\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0019\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u001a\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u001b\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u001c\u001a\u00020\u0004X\u0082T¢\u0006\u0002\n\u0000¨\u0006%"}, d2 = {"Lcom/oplus/deepthinker/sdk/app/awareness/fence/impl/NearFieldFence;", "", "()V", "BRAND_ID_OPPO", "", "BRAND_ID_UNKNOWN", "BUNDLE_KEY_BRAND_ID", "", "BUNDLE_KEY_NAME", "BUNDLE_KEY_STATE", "BUNDLE_KEY_TYPE", "ENTER_VALUE", "EXIT_VALUE", "FENCE_NAME", "FENCE_TYPE", "TAG_ID_3A", "TAG_ID_4A", "TAG_ID_5A", "TYPE_AIRPORT", "TYPE_ALL", "TYPE_ATTRACTIONS", "TYPE_HOSPITAL", "TYPE_MALL", "TYPE_RAILWAY_STATION", "TYPE_RECOMMEND_SHOP", "TYPE_SUBWAY_STATION", "TYPE_TEST", "TYPE_UNKNOWN", "UNKNOWN_VALUE", "enter", "Lcom/oplus/deepthinker/sdk/app/awareness/fence/AwarenessFence;", "type", "enterRecommend", "brandId", "exit", "exitRecommend", "State", "com.oplus.deepthinker.sdk_release"}, k = 1, mv = {1, 6, 0}, xi = 48)
/* loaded from: classes.dex */
public final class NearFieldFence {
    public static final int BRAND_ID_OPPO = 1666;
    public static final int BRAND_ID_UNKNOWN = 0;
    public static final String BUNDLE_KEY_BRAND_ID = "brand_id";
    public static final String BUNDLE_KEY_NAME = "name";
    public static final String BUNDLE_KEY_STATE = "state";
    public static final String BUNDLE_KEY_TYPE = "type";
    private static final int ENTER_VALUE = 1;
    private static final int EXIT_VALUE = 2;
    public static final String FENCE_NAME = "near_field_fence";
    public static final String FENCE_TYPE = "near_field_fence";
    public static final NearFieldFence INSTANCE = new NearFieldFence();
    public static final int TAG_ID_3A = 18;
    public static final int TAG_ID_4A = 16;
    public static final int TAG_ID_5A = 17;
    public static final int TYPE_AIRPORT = 3;
    public static final int TYPE_ALL = 6;
    public static final int TYPE_ATTRACTIONS = 8;
    public static final int TYPE_HOSPITAL = 7;
    public static final int TYPE_MALL = 2;
    public static final int TYPE_RAILWAY_STATION = 4;
    public static final int TYPE_RECOMMEND_SHOP = 9;
    public static final int TYPE_SUBWAY_STATION = 1;
    public static final int TYPE_TEST = 5;
    public static final int TYPE_UNKNOWN = 0;
    private static final int UNKNOWN_VALUE = 0;

    /* compiled from: NearFieldFence.kt */
    @Metadata(d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0000\n\u0002\u0010\b\n\u0002\b\u0007\b\u0086\u0001\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B\u000f\b\u0002\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006j\u0002\b\u0007j\u0002\b\bj\u0002\b\t¨\u0006\n"}, d2 = {"Lcom/oplus/deepthinker/sdk/app/awareness/fence/impl/NearFieldFence$State;", "", ThermalBaseConfig.Item.ATTR_VALUE, "", "(Ljava/lang/String;II)V", "getValue", "()I", "UNKNOWN", "ENTER", "EXIT", "com.oplus.deepthinker.sdk_release"}, k = 1, mv = {1, 6, 0}, xi = 48)
    /* loaded from: classes.dex */
    public enum State {
        UNKNOWN(0),
        ENTER(1),
        EXIT(2);

        private final int value;

        State(int i10) {
            this.value = i10;
        }

        public final int getValue() {
            return this.value;
        }
    }

    private NearFieldFence() {
    }

    public static final AwarenessFence enter(int type) {
        AwarenessFence.Companion companion = AwarenessFence.INSTANCE;
        AwarenessFence.Builder builder = new AwarenessFence.Builder();
        UUID randomUUID = UUID.randomUUID();
        k.d(randomUUID, "randomUUID()");
        builder.setFenceName(k.l("near_field_fence_", randomUUID));
        builder.setFenceType("near_field_fence");
        Bundle bundle = new Bundle();
        bundle.putInt("type", type);
        bundle.putInt("state", State.ENTER.getValue());
        builder.setFenceArgs(bundle);
        builder.setFenceCategory(1);
        return builder.build();
    }

    public static final AwarenessFence enterRecommend(int brandId) {
        AwarenessFence.Companion companion = AwarenessFence.INSTANCE;
        AwarenessFence.Builder builder = new AwarenessFence.Builder();
        UUID randomUUID = UUID.randomUUID();
        k.d(randomUUID, "randomUUID()");
        builder.setFenceName(k.l("near_field_fence_", randomUUID));
        builder.setFenceType("near_field_fence");
        Bundle bundle = new Bundle();
        bundle.putInt("type", 9);
        bundle.putInt("state", State.ENTER.getValue());
        bundle.putInt(BUNDLE_KEY_BRAND_ID, brandId);
        builder.setFenceArgs(bundle);
        builder.setFenceCategory(1);
        return builder.build();
    }

    public static final AwarenessFence exit(int type) {
        AwarenessFence.Companion companion = AwarenessFence.INSTANCE;
        AwarenessFence.Builder builder = new AwarenessFence.Builder();
        UUID randomUUID = UUID.randomUUID();
        k.d(randomUUID, "randomUUID()");
        builder.setFenceName(k.l("near_field_fence_", randomUUID));
        builder.setFenceType("near_field_fence");
        Bundle bundle = new Bundle();
        bundle.putInt("type", type);
        bundle.putInt("state", State.EXIT.getValue());
        builder.setFenceArgs(bundle);
        builder.setFenceCategory(1);
        return builder.build();
    }

    public static final AwarenessFence exitRecommend(int brandId) {
        AwarenessFence.Companion companion = AwarenessFence.INSTANCE;
        AwarenessFence.Builder builder = new AwarenessFence.Builder();
        UUID randomUUID = UUID.randomUUID();
        k.d(randomUUID, "randomUUID()");
        builder.setFenceName(k.l("near_field_fence_", randomUUID));
        builder.setFenceType("near_field_fence");
        Bundle bundle = new Bundle();
        bundle.putInt("type", 9);
        bundle.putInt("state", State.EXIT.getValue());
        bundle.putInt(BUNDLE_KEY_BRAND_ID, brandId);
        builder.setFenceArgs(bundle);
        builder.setFenceCategory(1);
        return builder.build();
    }
}
