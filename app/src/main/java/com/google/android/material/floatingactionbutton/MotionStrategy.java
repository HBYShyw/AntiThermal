package com.google.android.material.floatingactionbutton;

import android.animation.Animator;
import android.animation.AnimatorSet;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import java.util.List;
import p3.MotionSpec;

/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: MotionStrategy.java */
/* renamed from: com.google.android.material.floatingactionbutton.f, reason: use source file name */
/* loaded from: classes.dex */
public interface MotionStrategy {
    void a();

    void b(MotionSpec motionSpec);

    int c();

    void d();

    MotionSpec e();

    boolean f();

    void g();

    AnimatorSet h();

    List<Animator.AnimatorListener> i();

    void j(ExtendedFloatingActionButton.j jVar);

    void onAnimationStart(Animator animator);
}
