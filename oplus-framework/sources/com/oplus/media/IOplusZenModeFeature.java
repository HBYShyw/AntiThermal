package com.oplus.media;

import android.common.IOplusCommonFeature;
import android.common.OplusFeatureList;
import android.os.Parcel;

/* loaded from: classes.dex */
public interface IOplusZenModeFeature extends IOplusCommonFeature {
    public static final IOplusZenModeFeature DEFAULT = new IOplusZenModeFeature() { // from class: com.oplus.media.IOplusZenModeFeature.1
    };
    public static final int KEY_PARAMETER_INTERCEPT = 10011;
    public static final String NAME = "IOplusZenModeFeature";

    default OplusFeatureList.OplusIndex index() {
        return OplusFeatureList.OplusIndex.IOplusZenModeFeature;
    }

    default IOplusCommonFeature getDefault() {
        return DEFAULT;
    }

    default Parcel checkZenMode() {
        return null;
    }

    default Parcel checkWechatMute() {
        return null;
    }

    default void resetZenModeFlag() {
    }

    default void setAudioStreamType(int type) {
    }
}
