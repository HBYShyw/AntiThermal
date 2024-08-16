package com.coui.appcompat.floatingactionbutton;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.animation.ScaleAnimation;
import android.view.animation.Transformation;
import androidx.core.graphics.ColorUtils;
import java.lang.ref.WeakReference;

/* compiled from: COUIFloatingButtonTouchAnimation.java */
/* renamed from: com.coui.appcompat.floatingactionbutton.i, reason: use source file name */
/* loaded from: classes.dex */
public class COUIFloatingButtonTouchAnimation extends ScaleAnimation {

    /* renamed from: e, reason: collision with root package name */
    private WeakReference<View> f6068e;

    /* renamed from: f, reason: collision with root package name */
    private final float f6069f;

    /* renamed from: g, reason: collision with root package name */
    private final float f6070g;

    /* renamed from: h, reason: collision with root package name */
    private float f6071h;

    /* renamed from: i, reason: collision with root package name */
    private float f6072i;

    /* renamed from: j, reason: collision with root package name */
    private int f6073j;

    public COUIFloatingButtonTouchAnimation(float f10, float f11, float f12, float f13) {
        super(f10, f11, f10, f11, f12, f13);
        this.f6073j = 0;
        this.f6069f = f10;
        this.f6070g = f11;
    }

    private int a(int i10, float f10) {
        ColorUtils.f(i10, r2);
        float[] fArr = {0.0f, 0.0f, fArr[2] * f10};
        int a10 = ColorUtils.a(fArr);
        return Color.argb(Color.alpha(i10), Math.min(255, Color.red(a10)), Math.min(255, Color.green(a10)), Math.min(255, Color.blue(a10)));
    }

    private float b(float f10) {
        float f11 = this.f6069f;
        float f12 = this.f6070g;
        if (f11 > f12) {
            return 1.0f + (f10 * (-0.19999999f));
        }
        if (f11 < f12) {
            return (f10 * 0.19999999f) + 0.8f;
        }
        return 1.0f;
    }

    @Override // android.view.animation.ScaleAnimation, android.view.animation.Animation
    protected void applyTransformation(float f10, Transformation transformation) {
        int color;
        super.applyTransformation(f10, transformation);
        float f11 = this.f6069f;
        this.f6071h = f11 + ((this.f6070g - f11) * f10);
        WeakReference<View> weakReference = this.f6068e;
        if (weakReference != null) {
            View view = weakReference.get();
            ColorStateList backgroundTintList = view.getBackgroundTintList();
            if (backgroundTintList != null) {
                color = backgroundTintList.getDefaultColor();
            } else {
                color = view.getBackground() instanceof ColorDrawable ? ((ColorDrawable) view.getBackground()).getColor() : Integer.MIN_VALUE;
            }
            if (color != Integer.MIN_VALUE) {
                float b10 = b(f10);
                this.f6072i = b10;
                this.f6073j = a(color, b10);
                view.getBackground().setTint(this.f6073j);
            }
        }
    }

    public void c(View view) {
        this.f6068e = new WeakReference<>(view);
    }

    @Override // android.view.animation.Animation
    public int getBackgroundColor() {
        return this.f6073j;
    }
}
