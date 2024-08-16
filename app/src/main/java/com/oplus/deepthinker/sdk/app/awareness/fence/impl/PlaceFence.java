package com.oplus.deepthinker.sdk.app.awareness.fence.impl;

import android.os.Bundle;
import com.oplus.deepthinker.sdk.app.awareness.fence.AwarenessFence;
import com.oplus.thermalcontrol.config.ThermalBaseConfig;
import java.util.UUID;
import kotlin.Metadata;
import za.k;

/* compiled from: PlaceFence.kt */
@Metadata(d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0007\n\u0002\u0010\u0006\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\b\u000b\bÆ\u0002\u0018\u00002\u00020\u0001:\u0002 !B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u0012\u0010\u0016\u001a\u00020\u00172\b\b\u0002\u0010\u0018\u001a\u00020\u000eH\u0007J\u0012\u0010\u0019\u001a\u00020\u00172\b\b\u0002\u0010\u0018\u001a\u00020\u000eH\u0007J\"\u0010\u001a\u001a\u00020\u00172\u0006\u0010\u001b\u001a\u00020\u000e2\u0006\u0010\u001c\u001a\u00020\u000e2\b\b\u0002\u0010\u0018\u001a\u00020\u000eH\u0007J\u0012\u0010\u001d\u001a\u00020\u00172\b\b\u0002\u0010\u0018\u001a\u00020\u000eH\u0007J\u0012\u0010\u001e\u001a\u00020\u00172\b\b\u0002\u0010\u0018\u001a\u00020\u000eH\u0007J\"\u0010\u001f\u001a\u00020\u00172\u0006\u0010\u001b\u001a\u00020\u000e2\u0006\u0010\u001c\u001a\u00020\u000e2\b\b\u0002\u0010\u0018\u001a\u00020\u000eH\u0007R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0006X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0006X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0006X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u0006X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\u0006X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\u0004X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u000eX\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u0006X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\u0006X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\u0004X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u0012\u001a\u00020\u0004X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u0013\u001a\u00020\u0004X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u0014\u001a\u00020\u0004X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u0015\u001a\u00020\u0004X\u0082T¢\u0006\u0002\n\u0000¨\u0006\""}, d2 = {"Lcom/oplus/deepthinker/sdk/app/awareness/fence/impl/PlaceFence;", "", "()V", "ARRIVING_VALUE", "", "BUNDLE_KEY_LATITUDE", "", "BUNDLE_KEY_LOITERING_DELAY", "BUNDLE_KEY_LONGITUDE", "BUNDLE_KEY_PLACE", "BUNDLE_KEY_RADIUS", "BUNDLE_KEY_TRANSITION_TYPE", "COMPANY_VALUE", "DEFAULT_RADIUS", "", "FENCE_NAME", "FENCE_TYPE", "HOME_VALUE", "LEAVING_VALUE", "OTHER_RESIDENCE_VALUE", "UNKNOWN_PLACE_VALUE", "UNKNOWN_TRANSITION_VALUE", "arrivingCompany", "Lcom/oplus/deepthinker/sdk/app/awareness/fence/AwarenessFence;", "radius", "arrivingHome", "arrivingResidence", "longitude", "latitude", "leavingCompany", "leavingHome", "leavingResidence", "PlaceType", "TransitionType", "com.oplus.deepthinker.sdk_release"}, k = 1, mv = {1, 6, 0}, xi = 48)
/* loaded from: classes.dex */
public final class PlaceFence {
    private static final int ARRIVING_VALUE = 0;
    public static final String BUNDLE_KEY_LATITUDE = "latitude";
    public static final String BUNDLE_KEY_LOITERING_DELAY = "loitering_delay";
    public static final String BUNDLE_KEY_LONGITUDE = "longitude";
    public static final String BUNDLE_KEY_PLACE = "place";
    public static final String BUNDLE_KEY_RADIUS = "radius";
    public static final String BUNDLE_KEY_TRANSITION_TYPE = "transition_type";
    private static final int COMPANY_VALUE = 1;
    public static final double DEFAULT_RADIUS = 150.0d;
    public static final String FENCE_NAME = "place_fence";
    public static final String FENCE_TYPE = "place_fence";
    private static final int HOME_VALUE = 0;
    public static final PlaceFence INSTANCE = new PlaceFence();
    private static final int LEAVING_VALUE = 1;
    private static final int OTHER_RESIDENCE_VALUE = 2;
    private static final int UNKNOWN_PLACE_VALUE = -1;
    private static final int UNKNOWN_TRANSITION_VALUE = -1;

    /* compiled from: PlaceFence.kt */
    @Metadata(d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0000\n\u0002\u0010\b\n\u0002\b\b\b\u0086\u0001\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B\u000f\b\u0002\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006j\u0002\b\u0007j\u0002\b\bj\u0002\b\tj\u0002\b\n¨\u0006\u000b"}, d2 = {"Lcom/oplus/deepthinker/sdk/app/awareness/fence/impl/PlaceFence$PlaceType;", "", ThermalBaseConfig.Item.ATTR_VALUE, "", "(Ljava/lang/String;II)V", "getValue", "()I", "UNKNOWN", "HOME", "COMPANY", "OTHER_RESIDENCE", "com.oplus.deepthinker.sdk_release"}, k = 1, mv = {1, 6, 0}, xi = 48)
    /* loaded from: classes.dex */
    public enum PlaceType {
        UNKNOWN(-1),
        HOME(0),
        COMPANY(1),
        OTHER_RESIDENCE(2);

        private final int value;

        PlaceType(int i10) {
            this.value = i10;
        }

        public final int getValue() {
            return this.value;
        }
    }

    /* compiled from: PlaceFence.kt */
    @Metadata(d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0000\n\u0002\u0010\b\n\u0002\b\u0007\b\u0086\u0001\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B\u000f\b\u0002\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006j\u0002\b\u0007j\u0002\b\bj\u0002\b\t¨\u0006\n"}, d2 = {"Lcom/oplus/deepthinker/sdk/app/awareness/fence/impl/PlaceFence$TransitionType;", "", ThermalBaseConfig.Item.ATTR_VALUE, "", "(Ljava/lang/String;II)V", "getValue", "()I", "UNKNOWN", "ARRIVING", "LEAVING", "com.oplus.deepthinker.sdk_release"}, k = 1, mv = {1, 6, 0}, xi = 48)
    /* loaded from: classes.dex */
    public enum TransitionType {
        UNKNOWN(-1),
        ARRIVING(0),
        LEAVING(1);

        private final int value;

        TransitionType(int i10) {
            this.value = i10;
        }

        public final int getValue() {
            return this.value;
        }
    }

    private PlaceFence() {
    }

    public static final AwarenessFence arrivingCompany(double radius) {
        AwarenessFence.Companion companion = AwarenessFence.INSTANCE;
        AwarenessFence.Builder builder = new AwarenessFence.Builder();
        UUID randomUUID = UUID.randomUUID();
        k.d(randomUUID, "randomUUID()");
        builder.setFenceName(k.l("place_fence_", randomUUID));
        builder.setFenceType("place_fence");
        Bundle bundle = new Bundle();
        bundle.putDouble("radius", radius);
        bundle.putInt(BUNDLE_KEY_PLACE, PlaceType.COMPANY.getValue());
        bundle.putInt("transition_type", TransitionType.ARRIVING.getValue());
        builder.setFenceArgs(bundle);
        builder.setFenceCategory(1);
        return builder.build();
    }

    public static /* synthetic */ AwarenessFence arrivingCompany$default(double d10, int i10, Object obj) {
        if ((i10 & 1) != 0) {
            d10 = 150.0d;
        }
        return arrivingCompany(d10);
    }

    public static final AwarenessFence arrivingHome(double radius) {
        AwarenessFence.Companion companion = AwarenessFence.INSTANCE;
        AwarenessFence.Builder builder = new AwarenessFence.Builder();
        UUID randomUUID = UUID.randomUUID();
        k.d(randomUUID, "randomUUID()");
        builder.setFenceName(k.l("place_fence_", randomUUID));
        builder.setFenceType("place_fence");
        Bundle bundle = new Bundle();
        bundle.putDouble("radius", radius);
        bundle.putInt(BUNDLE_KEY_PLACE, PlaceType.HOME.getValue());
        bundle.putInt("transition_type", TransitionType.ARRIVING.getValue());
        builder.setFenceArgs(bundle);
        builder.setFenceCategory(1);
        return builder.build();
    }

    public static /* synthetic */ AwarenessFence arrivingHome$default(double d10, int i10, Object obj) {
        if ((i10 & 1) != 0) {
            d10 = 150.0d;
        }
        return arrivingHome(d10);
    }

    public static final AwarenessFence arrivingResidence(double longitude, double latitude, double radius) {
        AwarenessFence.Companion companion = AwarenessFence.INSTANCE;
        AwarenessFence.Builder builder = new AwarenessFence.Builder();
        UUID randomUUID = UUID.randomUUID();
        k.d(randomUUID, "randomUUID()");
        builder.setFenceName(k.l("place_fence_", randomUUID));
        builder.setFenceType("place_fence");
        Bundle bundle = new Bundle();
        bundle.putDouble("longitude", longitude);
        bundle.putDouble("latitude", latitude);
        bundle.putDouble("radius", radius);
        bundle.putInt(BUNDLE_KEY_PLACE, PlaceType.OTHER_RESIDENCE.getValue());
        bundle.putInt("transition_type", TransitionType.ARRIVING.getValue());
        builder.setFenceArgs(bundle);
        builder.setFenceCategory(1);
        return builder.build();
    }

    public static /* synthetic */ AwarenessFence arrivingResidence$default(double d10, double d11, double d12, int i10, Object obj) {
        if ((i10 & 4) != 0) {
            d12 = 150.0d;
        }
        return arrivingResidence(d10, d11, d12);
    }

    public static final AwarenessFence leavingCompany(double radius) {
        AwarenessFence.Companion companion = AwarenessFence.INSTANCE;
        AwarenessFence.Builder builder = new AwarenessFence.Builder();
        UUID randomUUID = UUID.randomUUID();
        k.d(randomUUID, "randomUUID()");
        builder.setFenceName(k.l("place_fence_", randomUUID));
        builder.setFenceType("place_fence");
        Bundle bundle = new Bundle();
        bundle.putInt(BUNDLE_KEY_PLACE, PlaceType.COMPANY.getValue());
        bundle.putDouble("radius", radius);
        bundle.putInt("transition_type", TransitionType.LEAVING.getValue());
        builder.setFenceArgs(bundle);
        builder.setFenceCategory(1);
        return builder.build();
    }

    public static /* synthetic */ AwarenessFence leavingCompany$default(double d10, int i10, Object obj) {
        if ((i10 & 1) != 0) {
            d10 = 150.0d;
        }
        return leavingCompany(d10);
    }

    public static final AwarenessFence leavingHome(double radius) {
        AwarenessFence.Companion companion = AwarenessFence.INSTANCE;
        AwarenessFence.Builder builder = new AwarenessFence.Builder();
        UUID randomUUID = UUID.randomUUID();
        k.d(randomUUID, "randomUUID()");
        builder.setFenceName(k.l("place_fence_", randomUUID));
        builder.setFenceType("place_fence");
        Bundle bundle = new Bundle();
        bundle.putInt(BUNDLE_KEY_PLACE, PlaceType.HOME.getValue());
        bundle.putDouble("radius", radius);
        bundle.putInt("transition_type", TransitionType.LEAVING.getValue());
        builder.setFenceArgs(bundle);
        builder.setFenceCategory(1);
        return builder.build();
    }

    public static /* synthetic */ AwarenessFence leavingHome$default(double d10, int i10, Object obj) {
        if ((i10 & 1) != 0) {
            d10 = 150.0d;
        }
        return leavingHome(d10);
    }

    public static final AwarenessFence leavingResidence(double longitude, double latitude, double radius) {
        AwarenessFence.Companion companion = AwarenessFence.INSTANCE;
        AwarenessFence.Builder builder = new AwarenessFence.Builder();
        UUID randomUUID = UUID.randomUUID();
        k.d(randomUUID, "randomUUID()");
        builder.setFenceName(k.l("place_fence_", randomUUID));
        builder.setFenceType("place_fence");
        Bundle bundle = new Bundle();
        bundle.putDouble("longitude", longitude);
        bundle.putDouble("latitude", latitude);
        bundle.putDouble("radius", radius);
        bundle.putInt(BUNDLE_KEY_PLACE, PlaceType.OTHER_RESIDENCE.getValue());
        bundle.putInt("transition_type", TransitionType.LEAVING.getValue());
        builder.setFenceArgs(bundle);
        builder.setFenceCategory(1);
        return builder.build();
    }

    public static /* synthetic */ AwarenessFence leavingResidence$default(double d10, double d11, double d12, int i10, Object obj) {
        if ((i10 & 4) != 0) {
            d12 = 150.0d;
        }
        return leavingResidence(d10, d11, d12);
    }
}
