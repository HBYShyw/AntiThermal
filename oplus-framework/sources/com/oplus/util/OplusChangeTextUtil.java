package com.oplus.util;

/* loaded from: classes.dex */
public class OplusChangeTextUtil {
    public static final int G1 = 1;
    public static final int G2 = 2;
    public static final int G3 = 3;
    public static final int G4 = 4;
    public static final int G5 = 5;
    public static final int GN = 6;
    private static final float H2 = 1.0f;
    private static final float H4 = 1.25f;
    private static final float H5 = 1.45f;
    private static final float H6 = 1.65f;
    private static final String TAG = "OplusChangeTextUtil";
    private static final float H1 = 0.9f;
    private static final float H3 = 1.1f;
    public static final float[] SCALE_LEVEL = {H1, 1.0f, H3, 1.25f, 1.45f, 1.65f};

    public static float getSuitableFontSize(float textSize, float scale, int level) {
        if (level < 2) {
            return textSize;
        }
        float[] fArr = SCALE_LEVEL;
        if (level > fArr.length) {
            level = fArr.length;
        }
        float textSizeNoScale = textSize / scale;
        switch (level) {
            case 2:
                if (scale < H3) {
                    return 1.0f * textSizeNoScale;
                }
                return H3 * textSizeNoScale;
            case 3:
                if (scale < H3) {
                    return 1.0f * textSizeNoScale;
                }
                if (scale < 1.45f) {
                    return H3 * textSizeNoScale;
                }
                return 1.25f * textSizeNoScale;
            default:
                if (scale > fArr[level - 1]) {
                    return fArr[level - 1] * textSizeNoScale;
                }
                return textSizeNoScale * scale;
        }
    }

    private static float getSuitableFontScale(float scale, int level) {
        if (level < 2) {
            return scale;
        }
        float[] fArr = SCALE_LEVEL;
        if (level > fArr.length) {
            level = fArr.length;
        }
        switch (level) {
            case 2:
                if (scale < H3) {
                    return 1.0f;
                }
                return H3;
            case 3:
                if (scale < H3) {
                    return 1.0f;
                }
                if (scale < 1.45f) {
                    return H3;
                }
                return 1.25f;
            default:
                if (scale > fArr[level - 1]) {
                    return fArr[level - 1];
                }
                return scale;
        }
    }
}
