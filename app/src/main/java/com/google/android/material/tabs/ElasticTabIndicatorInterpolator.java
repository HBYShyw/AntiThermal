package com.google.android.material.tabs;

import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.view.View;
import p3.AnimationUtils;

/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: ElasticTabIndicatorInterpolator.java */
/* renamed from: com.google.android.material.tabs.a, reason: use source file name */
/* loaded from: classes.dex */
public class ElasticTabIndicatorInterpolator extends TabIndicatorInterpolator {
    private static float e(float f10) {
        return (float) (1.0d - Math.cos((f10 * 3.141592653589793d) / 2.0d));
    }

    private static float f(float f10) {
        return (float) Math.sin((f10 * 3.141592653589793d) / 2.0d);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.android.material.tabs.TabIndicatorInterpolator
    public void d(TabLayout tabLayout, View view, View view2, float f10, Drawable drawable) {
        float f11;
        float e10;
        RectF a10 = TabIndicatorInterpolator.a(tabLayout, view);
        RectF a11 = TabIndicatorInterpolator.a(tabLayout, view2);
        if (a10.left < a11.left) {
            f11 = e(f10);
            e10 = f(f10);
        } else {
            f11 = f(f10);
            e10 = e(f10);
        }
        drawable.setBounds(AnimationUtils.c((int) a10.left, (int) a11.left, f11), drawable.getBounds().top, AnimationUtils.c((int) a10.right, (int) a11.right, e10), drawable.getBounds().bottom);
    }
}
