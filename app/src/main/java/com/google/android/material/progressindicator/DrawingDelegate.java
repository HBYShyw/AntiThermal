package com.google.android.material.progressindicator;

import android.graphics.Canvas;
import android.graphics.Paint;
import com.google.android.material.progressindicator.BaseProgressIndicatorSpec;

/* compiled from: DrawingDelegate.java */
/* renamed from: com.google.android.material.progressindicator.f, reason: use source file name */
/* loaded from: classes.dex */
abstract class DrawingDelegate<S extends BaseProgressIndicatorSpec> {

    /* renamed from: a, reason: collision with root package name */
    S f9109a;

    /* renamed from: b, reason: collision with root package name */
    protected DrawableWithAnimatedVisibilityChange f9110b;

    public DrawingDelegate(S s7) {
        this.f9109a = s7;
    }

    abstract void a(Canvas canvas, float f10);

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract void b(Canvas canvas, Paint paint, float f10, float f11, int i10);

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract void c(Canvas canvas, Paint paint);

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract int d();

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract int e();

    /* JADX INFO: Access modifiers changed from: protected */
    public void f(DrawableWithAnimatedVisibilityChange drawableWithAnimatedVisibilityChange) {
        this.f9110b = drawableWithAnimatedVisibilityChange;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void g(Canvas canvas, float f10) {
        this.f9109a.e();
        a(canvas, f10);
    }
}
