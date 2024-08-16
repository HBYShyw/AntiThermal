package com.google.android.material.floatingactionbutton;

import android.animation.Animator;

/* compiled from: AnimatorTracker.java */
/* renamed from: com.google.android.material.floatingactionbutton.a, reason: use source file name */
/* loaded from: classes.dex */
class AnimatorTracker {

    /* renamed from: a, reason: collision with root package name */
    private Animator f8844a;

    public void a() {
        Animator animator = this.f8844a;
        if (animator != null) {
            animator.cancel();
        }
    }

    public void b() {
        this.f8844a = null;
    }

    public void c(Animator animator) {
        a();
        this.f8844a = animator;
    }
}
