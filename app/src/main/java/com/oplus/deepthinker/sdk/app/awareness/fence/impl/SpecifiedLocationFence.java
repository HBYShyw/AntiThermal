package com.oplus.deepthinker.sdk.app.awareness.fence.impl;

import android.os.Bundle;
import com.oplus.deepthinker.sdk.app.awareness.fence.AwarenessFence;
import com.oplus.thermalcontrol.config.ThermalBaseConfig;
import java.util.UUID;
import kotlin.Metadata;
import za.k;

/* compiled from: SpecifiedLocationFence.kt */
@Metadata(d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0006\n\u0002\u0010\b\n\u0002\b\f\n\u0002\u0018\u0002\n\u0002\b\b\bÆ\u0002\u0018\u00002\u00020\u0001:\u0001\u001fB\u0007\b\u0002¢\u0006\u0002\u0010\u0002J,\u0010\u0017\u001a\u00020\u00182\u0006\u0010\u0019\u001a\u00020\u000b2\u0006\u0010\u001a\u001a\u00020\u00042\b\b\u0002\u0010\u001b\u001a\u00020\u000b2\b\b\u0002\u0010\u001c\u001a\u00020\u000bH\u0007J\"\u0010\u001d\u001a\u00020\u00182\u0006\u0010\u0019\u001a\u00020\u000b2\u0006\u0010\u001a\u001a\u00020\u00042\b\b\u0002\u0010\u001b\u001a\u00020\u000bH\u0007J\"\u0010\u001e\u001a\u00020\u00182\u0006\u0010\u0019\u001a\u00020\u000b2\u0006\u0010\u001a\u001a\u00020\u00042\b\b\u0002\u0010\u001b\u001a\u00020\u000bH\u0007R\u000e\u0010\u0003\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\u000bX\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u000bX\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u000bX\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\u000bX\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0012\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0013\u001a\u00020\u000bX\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0014\u001a\u00020\u000bX\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0015\u001a\u00020\u000bX\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0016\u001a\u00020\u000bX\u0082T¢\u0006\u0002\n\u0000¨\u0006 "}, d2 = {"Lcom/oplus/deepthinker/sdk/app/awareness/fence/impl/SpecifiedLocationFence;", "", "()V", "BUNDLE_KEY_AWARE_RADIUS", "", "BUNDLE_KEY_FUZZY_NAME", "BUNDLE_KEY_PATTERN", "BUNDLE_KEY_POI_RADIUS", "BUNDLE_KEY_STATE", "BUNDLE_KEY_TYPE_VALUE", "CHECK_VALUE", "", "DEFAULT_AWARE_RADIUS", "DEFAULT_FUZZY_NAME", "DEFAULT_POI_RADIUS", "ENTER_VALUE", "EXIT_VALUE", "FENCE_NAME", "FENCE_TYPE", "SPECIFIED_AIRPORT_VALUE", "SPECIFIED_RAILWAY_STATION_VALUE", "SPECIFIED_UNKNOWN_VALUE", "UNKNOWN_VALUE", "check", "Lcom/oplus/deepthinker/sdk/app/awareness/fence/AwarenessFence;", "type", "name", "poiRadius", "awareRadius", "enter", "exit", "State", "com.oplus.deepthinker.sdk_release"}, k = 1, mv = {1, 6, 0}, xi = 48)
/* loaded from: classes.dex */
public final class SpecifiedLocationFence {
    public static final String BUNDLE_KEY_AWARE_RADIUS = "aware_radius";
    public static final String BUNDLE_KEY_FUZZY_NAME = "fuzzy_name";
    public static final String BUNDLE_KEY_PATTERN = "pattern";
    public static final String BUNDLE_KEY_POI_RADIUS = "poi_radius";
    public static final String BUNDLE_KEY_STATE = "state";
    public static final String BUNDLE_KEY_TYPE_VALUE = "type";
    private static final int CHECK_VALUE = 3;
    public static final int DEFAULT_AWARE_RADIUS = 0;
    public static final String DEFAULT_FUZZY_NAME = "";
    public static final int DEFAULT_POI_RADIUS = 0;
    private static final int ENTER_VALUE = 1;
    private static final int EXIT_VALUE = 2;
    public static final String FENCE_NAME = "specified_location_fence";
    public static final String FENCE_TYPE = "specified_location_fence";
    public static final SpecifiedLocationFence INSTANCE = new SpecifiedLocationFence();
    public static final int SPECIFIED_AIRPORT_VALUE = 1;
    public static final int SPECIFIED_RAILWAY_STATION_VALUE = 2;
    public static final int SPECIFIED_UNKNOWN_VALUE = 0;
    private static final int UNKNOWN_VALUE = 0;

    /* compiled from: SpecifiedLocationFence.kt */
    @Metadata(d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0000\n\u0002\u0010\b\n\u0002\b\b\b\u0086\u0001\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B\u000f\b\u0002\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006j\u0002\b\u0007j\u0002\b\bj\u0002\b\tj\u0002\b\n¨\u0006\u000b"}, d2 = {"Lcom/oplus/deepthinker/sdk/app/awareness/fence/impl/SpecifiedLocationFence$State;", "", ThermalBaseConfig.Item.ATTR_VALUE, "", "(Ljava/lang/String;II)V", "getValue", "()I", "UNKNOWN", "ENTER", "EXIT", "CHECK", "com.oplus.deepthinker.sdk_release"}, k = 1, mv = {1, 6, 0}, xi = 48)
    /* loaded from: classes.dex */
    public enum State {
        UNKNOWN(0),
        ENTER(1),
        EXIT(2),
        CHECK(3);

        private final int value;

        State(int i10) {
            this.value = i10;
        }

        public final int getValue() {
            return this.value;
        }
    }

    private SpecifiedLocationFence() {
    }

    public static final AwarenessFence check(int type, String name, int poiRadius, int awareRadius) {
        k.e(name, "name");
        AwarenessFence.Companion companion = AwarenessFence.INSTANCE;
        AwarenessFence.Builder builder = new AwarenessFence.Builder();
        UUID randomUUID = UUID.randomUUID();
        k.d(randomUUID, "randomUUID()");
        builder.setFenceName(k.l("specified_location_fence_", randomUUID));
        builder.setFenceType("specified_location_fence");
        Bundle bundle = new Bundle();
        bundle.putInt("type", type);
        bundle.putString(BUNDLE_KEY_FUZZY_NAME, name);
        bundle.putInt("poi_radius", poiRadius);
        bundle.putInt("aware_radius", awareRadius);
        bundle.putInt("state", State.CHECK.getValue());
        builder.setFenceArgs(bundle);
        builder.setFenceCategory(1);
        return builder.build();
    }

    public static /* synthetic */ AwarenessFence check$default(int i10, String str, int i11, int i12, int i13, Object obj) {
        if ((i13 & 4) != 0) {
            i11 = 0;
        }
        if ((i13 & 8) != 0) {
            i12 = 0;
        }
        return check(i10, str, i11, i12);
    }

    public static final AwarenessFence enter(int type, String name, int poiRadius) {
        k.e(name, "name");
        AwarenessFence.Companion companion = AwarenessFence.INSTANCE;
        AwarenessFence.Builder builder = new AwarenessFence.Builder();
        UUID randomUUID = UUID.randomUUID();
        k.d(randomUUID, "randomUUID()");
        builder.setFenceName(k.l("specified_location_fence_", randomUUID));
        builder.setFenceType("specified_location_fence");
        Bundle bundle = new Bundle();
        bundle.putInt("type", type);
        bundle.putString(BUNDLE_KEY_FUZZY_NAME, name);
        bundle.putInt("poi_radius", poiRadius);
        bundle.putInt("state", State.ENTER.getValue());
        builder.setFenceArgs(bundle);
        builder.setFenceCategory(1);
        return builder.build();
    }

    public static /* synthetic */ AwarenessFence enter$default(int i10, String str, int i11, int i12, Object obj) {
        if ((i12 & 4) != 0) {
            i11 = 0;
        }
        return enter(i10, str, i11);
    }

    public static final AwarenessFence exit(int type, String name, int poiRadius) {
        k.e(name, "name");
        AwarenessFence.Companion companion = AwarenessFence.INSTANCE;
        AwarenessFence.Builder builder = new AwarenessFence.Builder();
        UUID randomUUID = UUID.randomUUID();
        k.d(randomUUID, "randomUUID()");
        builder.setFenceName(k.l("specified_location_fence_", randomUUID));
        builder.setFenceType("specified_location_fence");
        Bundle bundle = new Bundle();
        bundle.putInt("type", type);
        bundle.putString(BUNDLE_KEY_FUZZY_NAME, name);
        bundle.putInt("poi_radius", poiRadius);
        bundle.putInt("state", State.EXIT.getValue());
        builder.setFenceArgs(bundle);
        builder.setFenceCategory(1);
        return builder.build();
    }

    public static /* synthetic */ AwarenessFence exit$default(int i10, String str, int i11, int i12, Object obj) {
        if ((i12 & 4) != 0) {
            i11 = 0;
        }
        return exit(i10, str, i11);
    }
}
