package com.coui.appcompat.tablayout;

import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import m1.COUILinearInterpolator;
import v.FastOutLinearInInterpolator;
import v.FastOutSlowInInterpolator;
import v.LinearOutSlowInInterpolator;

/* compiled from: COUIAnimationUtils.java */
/* renamed from: com.coui.appcompat.tablayout.a, reason: use source file name */
/* loaded from: classes.dex */
public class COUIAnimationUtils {

    /* renamed from: a, reason: collision with root package name */
    public static final Interpolator f7834a = new COUILinearInterpolator();

    /* renamed from: b, reason: collision with root package name */
    public static final Interpolator f7835b = new FastOutSlowInInterpolator();

    /* renamed from: c, reason: collision with root package name */
    public static final Interpolator f7836c = new FastOutLinearInInterpolator();

    /* renamed from: d, reason: collision with root package name */
    public static final Interpolator f7837d = new LinearOutSlowInInterpolator();

    /* renamed from: e, reason: collision with root package name */
    public static final Interpolator f7838e = new DecelerateInterpolator();

    public static int a(int i10, int i11, float f10) {
        return i10 + Math.round(f10 * (i11 - i10));
    }
}
