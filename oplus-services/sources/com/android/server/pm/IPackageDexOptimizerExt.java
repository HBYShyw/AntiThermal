package com.android.server.pm;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public interface IPackageDexOptimizerExt {
    default void afterDexoptDone(int i, String str, String str2, long j, int i2) {
    }

    default String configDexoptBeforDoing(String str, String str2, String str3) {
        return "speed-profile";
    }

    default boolean configGenerateCompactDex(int i, boolean z) {
        return z;
    }

    default boolean shouldInterceptDexOptSecondary() {
        return false;
    }

    default boolean skipDexoptInDexOptPath(String str, int i) {
        return false;
    }
}
