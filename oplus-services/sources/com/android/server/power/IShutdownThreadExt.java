package com.android.server.power;

import android.content.Context;
import android.media.AudioAttributes;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public interface IShutdownThreadExt {

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public interface IStaticExt {
        default void beginShutdownSequence(Context context) {
        }

        default void doShutdownDetect(String str) {
        }

        default boolean hasFeatureOriginalShutdownAnimation() {
            return false;
        }

        default boolean interceptReboot(Context context, String str) {
            return false;
        }

        default boolean interceptShutdown(Context context, String str) {
            return false;
        }

        default boolean rebootOrShutdownSubsystem() {
            return true;
        }

        default boolean shouldDoLowLevelShutdown(Context context) {
            return false;
        }
    }

    default void checkShutdownTimeout(Context context, boolean z, String str, int i, AudioAttributes audioAttributes) {
    }

    default void delayForPlayAnimation(Context context) {
    }

    default void shutdownStorageManagerService(Context context) {
    }
}
