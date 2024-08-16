package com.oplus.wrapper.os;

import android.content.Context;
import android.net.Uri;

/* loaded from: classes.dex */
public class VibrationEffect {
    public static android.os.VibrationEffect get(int effectId) {
        return android.os.VibrationEffect.get(effectId);
    }

    public static android.os.VibrationEffect get(int effectId, boolean fallback) {
        return android.os.VibrationEffect.get(effectId, fallback);
    }

    public static android.os.VibrationEffect get(Uri uri, Context context) {
        return android.os.VibrationEffect.get(uri, context);
    }
}
