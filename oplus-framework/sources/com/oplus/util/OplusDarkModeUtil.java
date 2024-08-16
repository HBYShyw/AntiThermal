package com.oplus.util;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import com.android.internal.graphics.ColorUtils;

/* loaded from: classes.dex */
public class OplusDarkModeUtil {
    public static boolean isNightMode(Context context) {
        Configuration configuration = context.getResources().getConfiguration();
        int currentNightMode = configuration.uiMode & 48;
        return 32 == currentNightMode;
    }

    public static void setForceDarkAllow(View view, boolean allow) {
        view.setForceDarkAllowed(allow);
    }

    public static int makeDarkLimit(int color, float minLight) {
        float[] hsl = new float[3];
        ColorUtils.colorToHSL(color, hsl);
        float newL = Math.max(minLight, 1.0f - hsl[2]);
        if (newL < hsl[2]) {
            hsl[2] = newL;
            return ColorUtils.HSLToColor(hsl);
        }
        return color;
    }

    public static int makeLight(int color) {
        float[] hsl = new float[3];
        ColorUtils.colorToHSL(color, hsl);
        float newL = 1.0f - hsl[2];
        if (newL > hsl[2]) {
            hsl[2] = newL;
            return ColorUtils.HSLToColor(hsl);
        }
        return color;
    }

    public static int makeDark(int color) {
        float[] hsl = new float[3];
        ColorUtils.colorToHSL(color, hsl);
        float newL = 1.0f - hsl[2];
        if (newL < hsl[2]) {
            hsl[2] = newL;
            return ColorUtils.HSLToColor(hsl);
        }
        return color;
    }

    public static void makeImageViewDark(ImageView imageView) {
        if (imageView != null) {
            imageView.setColorFilter(getDarkFilter());
        }
    }

    public static void makeDrawableDark(Drawable drawable) {
        if (drawable != null) {
            drawable.setColorFilter(getDarkFilter());
        }
    }

    private static ColorFilter getDarkFilter() {
        return new LightingColorFilter(-2236963, 0);
    }
}
