package com.android.modules.utils.build;

import android.os.Build;
import androidx.annotation.ChecksSdkIntAtLeast;
import androidx.annotation.NonNull;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public final class SdkLevel {
    @ChecksSdkIntAtLeast(api = 30)
    public static boolean isAtLeastR() {
        return true;
    }

    @ChecksSdkIntAtLeast(api = 31)
    public static boolean isAtLeastS() {
        return true;
    }

    @ChecksSdkIntAtLeast(api = 32)
    public static boolean isAtLeastSv2() {
        return true;
    }

    @ChecksSdkIntAtLeast(api = 33)
    public static boolean isAtLeastT() {
        return true;
    }

    @ChecksSdkIntAtLeast(api = 34, codename = "UpsideDownCake")
    public static boolean isAtLeastU() {
        return true;
    }

    private SdkLevel() {
    }

    @ChecksSdkIntAtLeast(codename = "VanillaIceCream")
    public static boolean isAtLeastV() {
        return isAtLeastPreReleaseCodename("VanillaIceCream");
    }

    private static boolean isAtLeastPreReleaseCodename(@NonNull String str) {
        String str2 = Build.VERSION.CODENAME;
        return !"REL".equals(str2) && str2.compareTo(str) >= 0;
    }
}
