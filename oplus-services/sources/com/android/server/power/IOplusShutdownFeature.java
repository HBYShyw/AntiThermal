package com.android.server.power;

import android.common.IOplusCommonFeature;
import android.common.OplusFeatureList;
import android.content.Context;
import android.media.AudioAttributes;
import android.util.Log;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public interface IOplusShutdownFeature extends IOplusCommonFeature {
    public static final IOplusShutdownFeature DEFAULT = new IOplusShutdownFeature() { // from class: com.android.server.power.IOplusShutdownFeature.1
    };
    public static final String NAME = "IOplusShutdownFeature";

    default OplusFeatureList.OplusIndex index() {
        return OplusFeatureList.OplusIndex.IOplusShutdownFeature;
    }

    default IOplusCommonFeature getDefault() {
        return DEFAULT;
    }

    default void showShutdownBacktrace(boolean z) {
        Log.d(NAME, "default showShutdownBacktrace");
    }

    default void setSpecialShutdownProperty(String str) {
        Log.d(NAME, "default setSpecialShutdownProperty");
    }

    default void resetBrightnessAdj(Context context) {
        Log.d(NAME, "default resetBrightnessAdj");
    }

    default void setBeginAnimationTime(long j, boolean z) {
        Log.d(NAME, "default setBeginAnimationTime");
    }

    default void shutdownService(Context context) {
        Log.d(NAME, "default shutdownService");
    }

    default void checkShutdownTimeout(Context context, boolean z, String str, int i, AudioAttributes audioAttributes) {
        Log.d(NAME, "default checkShutdownTimeout");
    }

    default void delayForPlayAnimation() {
        Log.d(NAME, "default delayForPlayAnimation");
    }

    default boolean shouldDoLowLevelShutdown() {
        Log.d(NAME, "default shouldDoLowLevelShutdown");
        return true;
    }

    default void setMaxDelayTimeForCustomizeRebootanim(int i) {
        Log.d(NAME, "default setMaxDelayTimeForCustomizeRebootanim");
    }

    default int getMaxDelayTimeForCustomizeRebootanim() {
        Log.d(NAME, "default getMaxDelayTimeForCustomizeRebootanim");
        return 0;
    }

    default void shutdownStorageManagerService() {
        Log.d(NAME, "default shutdownStorageManagerService");
    }
}
