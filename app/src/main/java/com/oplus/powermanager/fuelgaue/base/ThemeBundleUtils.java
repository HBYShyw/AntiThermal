package com.oplus.powermanager.fuelgaue.base;

/* loaded from: classes.dex */
public class ThemeBundleUtils {
    private static boolean sImmersiveTheme = false;
    private static boolean sStatusWhite = false;

    public static boolean getImmersiveTheme() {
        return sImmersiveTheme;
    }

    public static boolean getStatusWhite() {
        return sStatusWhite;
    }

    public static void setImmersiveTheme(boolean z10) {
        sImmersiveTheme = z10;
    }

    public static void setStatusWhite(boolean z10) {
        sStatusWhite = z10;
    }
}
