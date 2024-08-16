package com.oplus.deepthinker.sdk.app.awareness.fence.impl;

import android.os.Bundle;
import com.oplus.deepthinker.sdk.app.awareness.fence.AwarenessFence;
import com.oplus.thermalcontrol.config.ThermalBaseConfig;
import java.util.UUID;
import kotlin.Metadata;
import za.k;

/* compiled from: ActivityRecognizeFence.kt */
@Metadata(d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u000e\n\u0002\u0010\u000e\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0004\bÆ\u0002\u0018\u00002\u00020\u0001:\u0001\u001eB\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u0010\u0010\u001a\u001a\u00020\u001b2\u0006\u0010\u001c\u001a\u00020\u0004H\u0007J\u0010\u0010\u001d\u001a\u00020\u001b2\u0006\u0010\u001c\u001a\u00020\u0004H\u0007R\u000e\u0010\u0003\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0012\u001a\u00020\u0013X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0014\u001a\u00020\u0013X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0015\u001a\u00020\u0004X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u0016\u001a\u00020\u0004X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u0017\u001a\u00020\u0013X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0018\u001a\u00020\u0013X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0019\u001a\u00020\u0004X\u0082T¢\u0006\u0002\n\u0000¨\u0006\u001f"}, d2 = {"Lcom/oplus/deepthinker/sdk/app/awareness/fence/impl/ActivityRecognizeFence;", "", "()V", "ACTIVITY_MODE_IN_ELEVATOR", "", "ACTIVITY_MODE_IN_FOUR_WHEELER_VEHICLE", "ACTIVITY_MODE_IN_RAIL_VEHICLE", "ACTIVITY_MODE_IN_ROAD_VEHICLE", "ACTIVITY_MODE_IN_TRANSPORTATION", "ACTIVITY_MODE_IN_TWO_WHEELER_VEHICLE", "ACTIVITY_MODE_IN_VEHICLE", "ACTIVITY_MODE_ON_BICYCLE", "ACTIVITY_MODE_ON_FOOT", "ACTIVITY_MODE_RUNNING", "ACTIVITY_MODE_STILL", "ACTIVITY_MODE_TILTING", "ACTIVITY_MODE_UNKNOWN_ACTIVITY", "ACTIVITY_MODE_WALKING", "BUNDLE_KEY_ACTIVITY_MODE", "", "BUNDLE_KEY_ACTIVITY_STATUS", "ENTER_VALUE", "EXIT_VALUE", "FENCE_NAME", "FENCE_TYPE", "UNKNOWN_VALUE", "enter", "Lcom/oplus/deepthinker/sdk/app/awareness/fence/AwarenessFence;", "activityMode", "exit", "StatusType", "com.oplus.deepthinker.sdk_release"}, k = 1, mv = {1, 6, 0}, xi = 48)
/* loaded from: classes.dex */
public final class ActivityRecognizeFence {
    public static final int ACTIVITY_MODE_IN_ELEVATOR = 312;
    public static final int ACTIVITY_MODE_IN_FOUR_WHEELER_VEHICLE = 311;
    public static final int ACTIVITY_MODE_IN_RAIL_VEHICLE = 309;
    public static final int ACTIVITY_MODE_IN_ROAD_VEHICLE = 308;
    public static final int ACTIVITY_MODE_IN_TRANSPORTATION = 313;
    public static final int ACTIVITY_MODE_IN_TWO_WHEELER_VEHICLE = 310;
    public static final int ACTIVITY_MODE_IN_VEHICLE = 300;
    public static final int ACTIVITY_MODE_ON_BICYCLE = 301;
    public static final int ACTIVITY_MODE_ON_FOOT = 302;
    public static final int ACTIVITY_MODE_RUNNING = 307;
    public static final int ACTIVITY_MODE_STILL = 303;
    public static final int ACTIVITY_MODE_TILTING = 305;
    public static final int ACTIVITY_MODE_UNKNOWN_ACTIVITY = 304;
    public static final int ACTIVITY_MODE_WALKING = 306;
    public static final String BUNDLE_KEY_ACTIVITY_MODE = "activity_mode";
    public static final String BUNDLE_KEY_ACTIVITY_STATUS = "activity_status";
    private static final int ENTER_VALUE = 0;
    private static final int EXIT_VALUE = 1;
    public static final String FENCE_NAME = "activity_recognize_fence";
    public static final String FENCE_TYPE = "activity_recognize_fence";
    public static final ActivityRecognizeFence INSTANCE = new ActivityRecognizeFence();
    private static final int UNKNOWN_VALUE = -1;

    /* compiled from: ActivityRecognizeFence.kt */
    @Metadata(d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0000\n\u0002\u0010\b\n\u0002\b\u0007\b\u0086\u0001\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B\u000f\b\u0002\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006j\u0002\b\u0007j\u0002\b\bj\u0002\b\t¨\u0006\n"}, d2 = {"Lcom/oplus/deepthinker/sdk/app/awareness/fence/impl/ActivityRecognizeFence$StatusType;", "", ThermalBaseConfig.Item.ATTR_VALUE, "", "(Ljava/lang/String;II)V", "getValue", "()I", "UNKNOWN", "ENTER", "EXIT", "com.oplus.deepthinker.sdk_release"}, k = 1, mv = {1, 6, 0}, xi = 48)
    /* loaded from: classes.dex */
    public enum StatusType {
        UNKNOWN(-1),
        ENTER(0),
        EXIT(1);

        private final int value;

        StatusType(int i10) {
            this.value = i10;
        }

        public final int getValue() {
            return this.value;
        }
    }

    private ActivityRecognizeFence() {
    }

    public static final AwarenessFence enter(int activityMode) {
        AwarenessFence.Companion companion = AwarenessFence.INSTANCE;
        AwarenessFence.Builder builder = new AwarenessFence.Builder();
        UUID randomUUID = UUID.randomUUID();
        k.d(randomUUID, "randomUUID()");
        builder.setFenceName(k.l("activity_recognize_fence_", randomUUID));
        builder.setFenceType("activity_recognize_fence");
        Bundle bundle = new Bundle();
        bundle.putInt(BUNDLE_KEY_ACTIVITY_MODE, activityMode);
        bundle.putInt(BUNDLE_KEY_ACTIVITY_STATUS, StatusType.ENTER.getValue());
        builder.setFenceArgs(bundle);
        builder.setFenceCategory(1);
        return builder.build();
    }

    public static final AwarenessFence exit(int activityMode) {
        AwarenessFence.Companion companion = AwarenessFence.INSTANCE;
        AwarenessFence.Builder builder = new AwarenessFence.Builder();
        UUID randomUUID = UUID.randomUUID();
        k.d(randomUUID, "randomUUID()");
        builder.setFenceName(k.l("activity_recognize_fence_", randomUUID));
        builder.setFenceType("activity_recognize_fence");
        Bundle bundle = new Bundle();
        bundle.putInt(BUNDLE_KEY_ACTIVITY_MODE, activityMode);
        bundle.putInt(BUNDLE_KEY_ACTIVITY_STATUS, StatusType.EXIT.getValue());
        builder.setFenceArgs(bundle);
        builder.setFenceCategory(1);
        return builder.build();
    }
}
