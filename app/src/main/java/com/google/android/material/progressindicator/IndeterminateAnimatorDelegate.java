package com.google.android.material.progressindicator;

import android.animation.Animator;
import g0.Animatable2Compat;

/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: IndeterminateAnimatorDelegate.java */
/* renamed from: com.google.android.material.progressindicator.g, reason: use source file name */
/* loaded from: classes.dex */
public abstract class IndeterminateAnimatorDelegate<T extends Animator> {

    /* renamed from: a, reason: collision with root package name */
    protected IndeterminateDrawable f9111a;

    /* renamed from: b, reason: collision with root package name */
    protected final float[] f9112b;

    /* renamed from: c, reason: collision with root package name */
    protected final int[] f9113c;

    /* JADX INFO: Access modifiers changed from: protected */
    public IndeterminateAnimatorDelegate(int i10) {
        this.f9112b = new float[i10 * 2];
        this.f9113c = new int[i10];
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract void a();

    /* JADX INFO: Access modifiers changed from: protected */
    public float b(int i10, int i11, int i12) {
        return (i10 - i11) / i12;
    }

    public abstract void c();

    public abstract void d(Animatable2Compat animatable2Compat);

    /* JADX INFO: Access modifiers changed from: protected */
    public void e(IndeterminateDrawable indeterminateDrawable) {
        this.f9111a = indeterminateDrawable;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract void f();

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract void g();

    public abstract void h();
}
