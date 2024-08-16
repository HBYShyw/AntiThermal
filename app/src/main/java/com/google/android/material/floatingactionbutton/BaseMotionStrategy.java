package com.google.android.material.floatingactionbutton;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.util.Property;
import android.view.View;
import androidx.core.util.Preconditions;
import java.util.ArrayList;
import java.util.List;
import p3.AnimationUtils;
import p3.AnimatorSetCompat;
import p3.MotionSpec;

/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: BaseMotionStrategy.java */
/* renamed from: com.google.android.material.floatingactionbutton.b, reason: use source file name */
/* loaded from: classes.dex */
public abstract class BaseMotionStrategy implements MotionStrategy {

    /* renamed from: a, reason: collision with root package name */
    private final Context f8845a;

    /* renamed from: b, reason: collision with root package name */
    private final ExtendedFloatingActionButton f8846b;

    /* renamed from: c, reason: collision with root package name */
    private final ArrayList<Animator.AnimatorListener> f8847c = new ArrayList<>();

    /* renamed from: d, reason: collision with root package name */
    private final AnimatorTracker f8848d;

    /* renamed from: e, reason: collision with root package name */
    private MotionSpec f8849e;

    /* renamed from: f, reason: collision with root package name */
    private MotionSpec f8850f;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: BaseMotionStrategy.java */
    /* renamed from: com.google.android.material.floatingactionbutton.b$a */
    /* loaded from: classes.dex */
    public class a extends Property<ExtendedFloatingActionButton, Float> {
        a(Class cls, String str) {
            super(cls, str);
        }

        @Override // android.util.Property
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public Float get(ExtendedFloatingActionButton extendedFloatingActionButton) {
            return Float.valueOf(AnimationUtils.a(0.0f, 1.0f, (Color.alpha(extendedFloatingActionButton.getCurrentTextColor()) / 255.0f) / Color.alpha(extendedFloatingActionButton.K.getColorForState(extendedFloatingActionButton.getDrawableState(), BaseMotionStrategy.this.f8846b.K.getDefaultColor()))));
        }

        @Override // android.util.Property
        /* renamed from: b, reason: merged with bridge method [inline-methods] */
        public void set(ExtendedFloatingActionButton extendedFloatingActionButton, Float f10) {
            int colorForState = extendedFloatingActionButton.K.getColorForState(extendedFloatingActionButton.getDrawableState(), BaseMotionStrategy.this.f8846b.K.getDefaultColor());
            ColorStateList valueOf = ColorStateList.valueOf(Color.argb((int) (AnimationUtils.a(0.0f, Color.alpha(colorForState) / 255.0f, f10.floatValue()) * 255.0f), Color.red(colorForState), Color.green(colorForState), Color.blue(colorForState)));
            if (f10.floatValue() == 1.0f) {
                extendedFloatingActionButton.B(extendedFloatingActionButton.K);
            } else {
                extendedFloatingActionButton.B(valueOf);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public BaseMotionStrategy(ExtendedFloatingActionButton extendedFloatingActionButton, AnimatorTracker animatorTracker) {
        this.f8846b = extendedFloatingActionButton;
        this.f8845a = extendedFloatingActionButton.getContext();
        this.f8848d = animatorTracker;
    }

    @Override // com.google.android.material.floatingactionbutton.MotionStrategy
    public void a() {
        this.f8848d.b();
    }

    @Override // com.google.android.material.floatingactionbutton.MotionStrategy
    public final void b(MotionSpec motionSpec) {
        this.f8850f = motionSpec;
    }

    @Override // com.google.android.material.floatingactionbutton.MotionStrategy
    public MotionSpec e() {
        return this.f8850f;
    }

    @Override // com.google.android.material.floatingactionbutton.MotionStrategy
    public void g() {
        this.f8848d.b();
    }

    @Override // com.google.android.material.floatingactionbutton.MotionStrategy
    public AnimatorSet h() {
        return l(m());
    }

    @Override // com.google.android.material.floatingactionbutton.MotionStrategy
    public final List<Animator.AnimatorListener> i() {
        return this.f8847c;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public AnimatorSet l(MotionSpec motionSpec) {
        ArrayList arrayList = new ArrayList();
        if (motionSpec.j("opacity")) {
            arrayList.add(motionSpec.f("opacity", this.f8846b, View.ALPHA));
        }
        if (motionSpec.j("scale")) {
            arrayList.add(motionSpec.f("scale", this.f8846b, View.SCALE_Y));
            arrayList.add(motionSpec.f("scale", this.f8846b, View.SCALE_X));
        }
        if (motionSpec.j("width")) {
            arrayList.add(motionSpec.f("width", this.f8846b, ExtendedFloatingActionButton.M));
        }
        if (motionSpec.j("height")) {
            arrayList.add(motionSpec.f("height", this.f8846b, ExtendedFloatingActionButton.N));
        }
        if (motionSpec.j("paddingStart")) {
            arrayList.add(motionSpec.f("paddingStart", this.f8846b, ExtendedFloatingActionButton.O));
        }
        if (motionSpec.j("paddingEnd")) {
            arrayList.add(motionSpec.f("paddingEnd", this.f8846b, ExtendedFloatingActionButton.P));
        }
        if (motionSpec.j("labelOpacity")) {
            arrayList.add(motionSpec.f("labelOpacity", this.f8846b, new a(Float.class, "LABEL_OPACITY_PROPERTY")));
        }
        AnimatorSet animatorSet = new AnimatorSet();
        AnimatorSetCompat.a(animatorSet, arrayList);
        return animatorSet;
    }

    public final MotionSpec m() {
        MotionSpec motionSpec = this.f8850f;
        if (motionSpec != null) {
            return motionSpec;
        }
        if (this.f8849e == null) {
            this.f8849e = MotionSpec.d(this.f8845a, c());
        }
        return (MotionSpec) Preconditions.d(this.f8849e);
    }

    @Override // com.google.android.material.floatingactionbutton.MotionStrategy
    public void onAnimationStart(Animator animator) {
        this.f8848d.c(animator);
    }
}
