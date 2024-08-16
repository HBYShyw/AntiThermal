package y3;

import android.content.ContentResolver;
import android.provider.Settings;

/* compiled from: AnimatorDurationScaleProvider.java */
/* renamed from: y3.a, reason: use source file name */
/* loaded from: classes.dex */
public class AnimatorDurationScaleProvider {
    public float a(ContentResolver contentResolver) {
        return Settings.Global.getFloat(contentResolver, "animator_duration_scale", 1.0f);
    }
}
