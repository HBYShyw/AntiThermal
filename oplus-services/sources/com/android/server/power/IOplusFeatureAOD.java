package com.android.server.power;

import android.common.IOplusCommonFeature;
import android.common.OplusFeatureList;
import android.service.dreams.DreamManagerInternal;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public interface IOplusFeatureAOD extends IOplusCommonFeature {
    public static final IOplusFeatureAOD DEFAULT = new IOplusFeatureAOD() { // from class: com.android.server.power.IOplusFeatureAOD.1
    };
    public static final String NAME = "IOplusFeatureAOD";

    default void clearDozeStateMap() {
    }

    default void handleAodChanged() {
    }

    default boolean isShouldGoAod() {
        return false;
    }

    default void notifySfUnBlockScreenOn() {
    }

    default void onDisplayStateChange(DreamManagerInternal dreamManagerInternal, int i) {
    }

    default void setAodSettingStatus() {
    }

    default void setDozeOverride(int i, int i2) {
    }

    default void setDozeOverrideFromDreamManager(int i, int i2) {
    }

    default int setDozeOverrideFromDreamManagerInternal(int i, int i2) {
        return i;
    }

    default void systemReady() {
    }

    default OplusFeatureList.OplusIndex index() {
        return OplusFeatureList.OplusIndex.IOplusFeatureAOD;
    }

    default IOplusCommonFeature getDefault() {
        return DEFAULT;
    }
}
