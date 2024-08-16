package com.google.android.material.internal;

import android.R;
import android.annotation.TargetApi;
import android.content.Context;
import android.view.Window;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsControllerCompat;
import r3.MaterialColors;

/* loaded from: classes.dex */
public class EdgeToEdgeUtils {
    private static final int EDGE_TO_EDGE_BAR_ALPHA = 128;

    private EdgeToEdgeUtils() {
    }

    public static void applyEdgeToEdge(Window window, boolean z10) {
        applyEdgeToEdge(window, z10, null, null);
    }

    @TargetApi(21)
    private static int getNavigationBarColor(Context context, boolean z10) {
        if (z10) {
            return 0;
        }
        return MaterialColors.b(context, R.attr.navigationBarColor, -16777216);
    }

    @TargetApi(21)
    private static int getStatusBarColor(Context context, boolean z10) {
        if (z10) {
            return 0;
        }
        return MaterialColors.b(context, R.attr.statusBarColor, -16777216);
    }

    private static boolean isUsingLightSystemBar(int i10, boolean z10) {
        return MaterialColors.f(i10) || (i10 == 0 && z10);
    }

    public static void applyEdgeToEdge(Window window, boolean z10, Integer num, Integer num2) {
        boolean z11 = num == null || num.intValue() == 0;
        boolean z12 = num2 == null || num2.intValue() == 0;
        if (z11 || z12) {
            int b10 = MaterialColors.b(window.getContext(), R.attr.colorBackground, -16777216);
            if (z11) {
                num = Integer.valueOf(b10);
            }
            if (z12) {
                num2 = Integer.valueOf(b10);
            }
        }
        WindowCompat.b(window, !z10);
        int statusBarColor = getStatusBarColor(window.getContext(), z10);
        int navigationBarColor = getNavigationBarColor(window.getContext(), z10);
        window.setStatusBarColor(statusBarColor);
        window.setNavigationBarColor(navigationBarColor);
        boolean isUsingLightSystemBar = isUsingLightSystemBar(statusBarColor, MaterialColors.f(num.intValue()));
        boolean isUsingLightSystemBar2 = isUsingLightSystemBar(navigationBarColor, MaterialColors.f(num2.intValue()));
        WindowInsetsControllerCompat a10 = WindowCompat.a(window, window.getDecorView());
        if (a10 != null) {
            a10.b(isUsingLightSystemBar);
            a10.a(isUsingLightSystemBar2);
        }
    }
}
