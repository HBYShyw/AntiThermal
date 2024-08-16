package com.coui.appcompat.snackbar;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.animation.Interpolator;
import androidx.core.view.animation.PathInterpolatorCompat;

/* compiled from: COUICustomSnackAnimUtil.java */
/* renamed from: com.coui.appcompat.snackbar.a, reason: use source file name */
/* loaded from: classes.dex */
public class COUICustomSnackAnimUtil {

    /* renamed from: a, reason: collision with root package name */
    public static final Interpolator f7707a = PathInterpolatorCompat.a(0.1f, 0.0f, 0.1f, 1.0f);

    /* renamed from: b, reason: collision with root package name */
    public static final Interpolator f7708b = PathInterpolatorCompat.a(0.1f, 0.0f, 0.1f, 1.0f);

    /* renamed from: c, reason: collision with root package name */
    public static final Interpolator f7709c = PathInterpolatorCompat.a(0.3f, 0.0f, 1.0f, 1.0f);

    /* renamed from: d, reason: collision with root package name */
    public static final Interpolator f7710d = PathInterpolatorCompat.a(0.3f, 0.0f, 0.1f, 1.0f);

    /* renamed from: e, reason: collision with root package name */
    public static final Interpolator f7711e = PathInterpolatorCompat.a(0.22f, 0.34f, 0.05f, 1.0f);

    /* renamed from: f, reason: collision with root package name */
    public static final Interpolator f7712f = PathInterpolatorCompat.a(0.4f, 0.0f, 0.4f, 1.0f);

    /* JADX INFO: Access modifiers changed from: protected */
    public static AnimatorSet a(COUICustomSnackBar cOUICustomSnackBar) {
        ObjectAnimator ofFloat = ObjectAnimator.ofFloat(cOUICustomSnackBar, "alpha", 1.0f, 0.0f);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(ofFloat);
        animatorSet.setInterpolator(f7707a);
        animatorSet.setDuration(180L);
        return animatorSet;
    }
}
