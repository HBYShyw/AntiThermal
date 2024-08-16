package com.oplus.deepthinker.sdk.app.awareness.fence.impl;

import android.os.Bundle;
import com.oplus.deepthinker.sdk.app.awareness.fence.AwarenessFence;
import com.oplus.thermalcontrol.config.ThermalBaseConfig;
import java.util.UUID;
import kotlin.Metadata;
import za.k;

/* compiled from: LocationFence.kt */
@Metadata(d1 = {"\u00008\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0005\n\u0002\u0010\u0006\n\u0000\n\u0002\u0010\b\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u0005\bÆ\u0002\u0018\u00002\u00020\u0001:\u0001\u001eB\u0007\b\u0002¢\u0006\u0002\u0010\u0002J2\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\n2\u0006\u0010\u0015\u001a\u00020\n2\b\b\u0002\u0010\u0016\u001a\u00020\n2\u0006\u0010\u0017\u001a\u00020\u00182\u0006\u0010\u0019\u001a\u00020\u001aH\u0007J*\u0010\u001b\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\n2\u0006\u0010\u0015\u001a\u00020\n2\b\b\u0002\u0010\u0016\u001a\u00020\n2\u0006\u0010\u0019\u001a\u00020\u001aH\u0007J\"\u0010\u001c\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\n2\u0006\u0010\u0015\u001a\u00020\n2\b\b\u0002\u0010\u0016\u001a\u00020\nH\u0007J\"\u0010\u001d\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\n2\u0006\u0010\u0015\u001a\u00020\n2\b\b\u0002\u0010\u0016\u001a\u00020\nH\u0007R\u000e\u0010\u0003\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\fX\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\fX\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\fX\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\fX\u0082T¢\u0006\u0002\n\u0000¨\u0006\u001f"}, d2 = {"Lcom/oplus/deepthinker/sdk/app/awareness/fence/impl/LocationFence;", "", "()V", "BUNDLE_KEY_LATITUDE", "", "BUNDLE_KEY_LOITERING_DELAY", "BUNDLE_KEY_LONGITUDE", "BUNDLE_KEY_RADIUS", "BUNDLE_KEY_TRANSITION_TYPE", "DEFAULT_RADIUS", "", "DWELL_VALUE", "", "ENTER_VALUE", "EXIT_VALUE", "FENCE_NAME", "FENCE_TYPE", "UNKNOWN_VALUE", "build", "Lcom/oplus/deepthinker/sdk/app/awareness/fence/AwarenessFence;", "longitude", "latitude", "radius", "transitionType", "Lcom/oplus/deepthinker/sdk/app/awareness/fence/impl/LocationFence$TransitionType;", "loiteringDelayMs", "", "dwell", "entering", "exiting", "TransitionType", "com.oplus.deepthinker.sdk_release"}, k = 1, mv = {1, 6, 0}, xi = 48)
/* loaded from: classes.dex */
public final class LocationFence {
    public static final String BUNDLE_KEY_LATITUDE = "latitude";
    public static final String BUNDLE_KEY_LOITERING_DELAY = "loitering_delay";
    public static final String BUNDLE_KEY_LONGITUDE = "longitude";
    public static final String BUNDLE_KEY_RADIUS = "radius";
    public static final String BUNDLE_KEY_TRANSITION_TYPE = "transition_type";
    public static final double DEFAULT_RADIUS = 150.0d;
    private static final int DWELL_VALUE = 2;
    private static final int ENTER_VALUE = 0;
    private static final int EXIT_VALUE = 1;
    public static final String FENCE_NAME = "location_fence";
    public static final String FENCE_TYPE = "location_fence";
    public static final LocationFence INSTANCE = new LocationFence();
    private static final int UNKNOWN_VALUE = -1;

    /* compiled from: LocationFence.kt */
    @Metadata(d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0000\n\u0002\u0010\b\n\u0002\b\b\b\u0086\u0001\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B\u000f\b\u0002\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006j\u0002\b\u0007j\u0002\b\bj\u0002\b\tj\u0002\b\n¨\u0006\u000b"}, d2 = {"Lcom/oplus/deepthinker/sdk/app/awareness/fence/impl/LocationFence$TransitionType;", "", ThermalBaseConfig.Item.ATTR_VALUE, "", "(Ljava/lang/String;II)V", "getValue", "()I", "ENTER", "EXIT", "DWELL", "UNKNOWN", "com.oplus.deepthinker.sdk_release"}, k = 1, mv = {1, 6, 0}, xi = 48)
    /* loaded from: classes.dex */
    public enum TransitionType {
        ENTER(0),
        EXIT(1),
        DWELL(2),
        UNKNOWN(-1);

        private final int value;

        TransitionType(int i10) {
            this.value = i10;
        }

        public final int getValue() {
            return this.value;
        }
    }

    private LocationFence() {
    }

    public static final AwarenessFence build(double longitude, double latitude, double radius, TransitionType transitionType, long loiteringDelayMs) {
        k.e(transitionType, "transitionType");
        AwarenessFence.Companion companion = AwarenessFence.INSTANCE;
        AwarenessFence.Builder builder = new AwarenessFence.Builder();
        UUID randomUUID = UUID.randomUUID();
        k.d(randomUUID, "randomUUID()");
        builder.setFenceName(k.l("location_fence_", randomUUID));
        builder.setFenceType("location_fence");
        Bundle bundle = new Bundle();
        bundle.putDouble("longitude", longitude);
        bundle.putDouble("latitude", latitude);
        bundle.putDouble("radius", radius);
        bundle.putInt("transition_type", transitionType.getValue());
        bundle.putLong("loitering_delay", loiteringDelayMs);
        builder.setFenceArgs(bundle);
        builder.setFenceCategory(1);
        return builder.build();
    }

    public static final AwarenessFence dwell(double longitude, double latitude, double radius, long loiteringDelayMs) {
        AwarenessFence.Companion companion = AwarenessFence.INSTANCE;
        AwarenessFence.Builder builder = new AwarenessFence.Builder();
        UUID randomUUID = UUID.randomUUID();
        k.d(randomUUID, "randomUUID()");
        builder.setFenceName(k.l("location_fence_", randomUUID));
        builder.setFenceType("location_fence");
        Bundle bundle = new Bundle();
        bundle.putDouble("longitude", longitude);
        bundle.putDouble("latitude", latitude);
        bundle.putDouble("radius", radius);
        bundle.putInt("transition_type", TransitionType.DWELL.getValue());
        bundle.putLong("loitering_delay", loiteringDelayMs);
        builder.setFenceArgs(bundle);
        builder.setFenceCategory(1);
        return builder.build();
    }

    public static final AwarenessFence entering(double longitude, double latitude, double radius) {
        AwarenessFence.Companion companion = AwarenessFence.INSTANCE;
        AwarenessFence.Builder builder = new AwarenessFence.Builder();
        UUID randomUUID = UUID.randomUUID();
        k.d(randomUUID, "randomUUID()");
        builder.setFenceName(k.l("location_fence_", randomUUID));
        builder.setFenceType("location_fence");
        Bundle bundle = new Bundle();
        bundle.putDouble("longitude", longitude);
        bundle.putDouble("latitude", latitude);
        bundle.putDouble("radius", radius);
        bundle.putInt("transition_type", TransitionType.ENTER.getValue());
        builder.setFenceArgs(bundle);
        builder.setFenceCategory(1);
        return builder.build();
    }

    public static /* synthetic */ AwarenessFence entering$default(double d10, double d11, double d12, int i10, Object obj) {
        if ((i10 & 4) != 0) {
            d12 = 150.0d;
        }
        return entering(d10, d11, d12);
    }

    public static final AwarenessFence exiting(double longitude, double latitude, double radius) {
        AwarenessFence.Companion companion = AwarenessFence.INSTANCE;
        AwarenessFence.Builder builder = new AwarenessFence.Builder();
        UUID randomUUID = UUID.randomUUID();
        k.d(randomUUID, "randomUUID()");
        builder.setFenceName(k.l("location_fence_", randomUUID));
        builder.setFenceType("location_fence");
        Bundle bundle = new Bundle();
        bundle.putDouble("longitude", longitude);
        bundle.putDouble("latitude", latitude);
        bundle.putDouble("radius", radius);
        bundle.putInt("transition_type", TransitionType.EXIT.getValue());
        builder.setFenceArgs(bundle);
        builder.setFenceCategory(1);
        return builder.build();
    }

    public static /* synthetic */ AwarenessFence exiting$default(double d10, double d11, double d12, int i10, Object obj) {
        if ((i10 & 4) != 0) {
            d12 = 150.0d;
        }
        return exiting(d10, d11, d12);
    }
}
