package g0;

import android.content.Context;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;

/* compiled from: AnimationUtilsCompat.java */
/* renamed from: g0.b, reason: use source file name */
/* loaded from: classes.dex */
public class AnimationUtilsCompat {
    public static Interpolator a(Context context, int i10) {
        return AnimationUtils.loadInterpolator(context, i10);
    }
}
