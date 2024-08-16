package com.google.android.material.floatingactionbutton;

import a4.RippleUtils;
import android.R;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.content.res.ColorStateList;
import android.graphics.Matrix;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.InsetDrawable;
import android.util.Property;
import android.view.View;
import android.view.ViewTreeObserver;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.util.Preconditions;
import androidx.core.view.ViewCompat;
import b4.ShadowViewDelegate;
import c4.MaterialShapeDrawable;
import c4.MaterialShapeUtils;
import c4.ShapeAppearanceModel;
import c4.Shapeable;
import com.google.android.material.R$attr;
import com.google.android.material.R$integer;
import com.google.android.material.internal.StateListAnimator;
import java.util.ArrayList;
import java.util.Iterator;
import p3.AnimationUtils;
import p3.AnimatorSetCompat;
import p3.ImageMatrixProperty;
import p3.MatrixEvaluator;
import p3.MotionSpec;
import x3.MotionUtils;

/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: FloatingActionButtonImpl.java */
/* renamed from: com.google.android.material.floatingactionbutton.d, reason: use source file name */
/* loaded from: classes.dex */
public class FloatingActionButtonImpl {
    static final TimeInterpolator D = AnimationUtils.f16557c;
    static final int[] E = {R.attr.state_pressed, R.attr.state_enabled};
    static final int[] F = {R.attr.state_hovered, R.attr.state_focused, R.attr.state_enabled};
    static final int[] G = {R.attr.state_focused, R.attr.state_enabled};
    static final int[] H = {R.attr.state_hovered, R.attr.state_enabled};
    static final int[] I = {R.attr.state_enabled};
    static final int[] J = new int[0];
    private ViewTreeObserver.OnPreDrawListener C;

    /* renamed from: a, reason: collision with root package name */
    ShapeAppearanceModel f8869a;

    /* renamed from: b, reason: collision with root package name */
    MaterialShapeDrawable f8870b;

    /* renamed from: c, reason: collision with root package name */
    Drawable f8871c;

    /* renamed from: d, reason: collision with root package name */
    BorderDrawable f8872d;

    /* renamed from: e, reason: collision with root package name */
    Drawable f8873e;

    /* renamed from: f, reason: collision with root package name */
    boolean f8874f;

    /* renamed from: h, reason: collision with root package name */
    float f8876h;

    /* renamed from: i, reason: collision with root package name */
    float f8877i;

    /* renamed from: j, reason: collision with root package name */
    float f8878j;

    /* renamed from: k, reason: collision with root package name */
    int f8879k;

    /* renamed from: l, reason: collision with root package name */
    private final StateListAnimator f8880l;

    /* renamed from: m, reason: collision with root package name */
    private Animator f8881m;

    /* renamed from: n, reason: collision with root package name */
    private MotionSpec f8882n;

    /* renamed from: o, reason: collision with root package name */
    private MotionSpec f8883o;

    /* renamed from: p, reason: collision with root package name */
    private float f8884p;

    /* renamed from: r, reason: collision with root package name */
    private int f8886r;

    /* renamed from: t, reason: collision with root package name */
    private ArrayList<Animator.AnimatorListener> f8888t;

    /* renamed from: u, reason: collision with root package name */
    private ArrayList<Animator.AnimatorListener> f8889u;

    /* renamed from: v, reason: collision with root package name */
    private ArrayList<i> f8890v;

    /* renamed from: w, reason: collision with root package name */
    final FloatingActionButton f8891w;

    /* renamed from: x, reason: collision with root package name */
    final ShadowViewDelegate f8892x;

    /* renamed from: g, reason: collision with root package name */
    boolean f8875g = true;

    /* renamed from: q, reason: collision with root package name */
    private float f8885q = 1.0f;

    /* renamed from: s, reason: collision with root package name */
    private int f8887s = 0;

    /* renamed from: y, reason: collision with root package name */
    private final Rect f8893y = new Rect();

    /* renamed from: z, reason: collision with root package name */
    private final RectF f8894z = new RectF();
    private final RectF A = new RectF();
    private final Matrix B = new Matrix();

    /* compiled from: FloatingActionButtonImpl.java */
    /* renamed from: com.google.android.material.floatingactionbutton.d$a */
    /* loaded from: classes.dex */
    class a extends AnimatorListenerAdapter {

        /* renamed from: a, reason: collision with root package name */
        private boolean f8895a;

        /* renamed from: b, reason: collision with root package name */
        final /* synthetic */ boolean f8896b;

        /* renamed from: c, reason: collision with root package name */
        final /* synthetic */ j f8897c;

        a(boolean z10, j jVar) {
            this.f8896b = z10;
            this.f8897c = jVar;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationCancel(Animator animator) {
            this.f8895a = true;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            FloatingActionButtonImpl.this.f8887s = 0;
            FloatingActionButtonImpl.this.f8881m = null;
            if (this.f8895a) {
                return;
            }
            FloatingActionButton floatingActionButton = FloatingActionButtonImpl.this.f8891w;
            boolean z10 = this.f8896b;
            floatingActionButton.internalSetVisibility(z10 ? 8 : 4, z10);
            j jVar = this.f8897c;
            if (jVar != null) {
                jVar.b();
            }
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationStart(Animator animator) {
            FloatingActionButtonImpl.this.f8891w.internalSetVisibility(0, this.f8896b);
            FloatingActionButtonImpl.this.f8887s = 1;
            FloatingActionButtonImpl.this.f8881m = animator;
            this.f8895a = false;
        }
    }

    /* compiled from: FloatingActionButtonImpl.java */
    /* renamed from: com.google.android.material.floatingactionbutton.d$b */
    /* loaded from: classes.dex */
    class b extends AnimatorListenerAdapter {

        /* renamed from: a, reason: collision with root package name */
        final /* synthetic */ boolean f8899a;

        /* renamed from: b, reason: collision with root package name */
        final /* synthetic */ j f8900b;

        b(boolean z10, j jVar) {
            this.f8899a = z10;
            this.f8900b = jVar;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            FloatingActionButtonImpl.this.f8887s = 0;
            FloatingActionButtonImpl.this.f8881m = null;
            j jVar = this.f8900b;
            if (jVar != null) {
                jVar.a();
            }
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationStart(Animator animator) {
            FloatingActionButtonImpl.this.f8891w.internalSetVisibility(0, this.f8899a);
            FloatingActionButtonImpl.this.f8887s = 2;
            FloatingActionButtonImpl.this.f8881m = animator;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: FloatingActionButtonImpl.java */
    /* renamed from: com.google.android.material.floatingactionbutton.d$c */
    /* loaded from: classes.dex */
    public class c extends MatrixEvaluator {
        c() {
        }

        @Override // android.animation.TypeEvaluator
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public Matrix evaluate(float f10, Matrix matrix, Matrix matrix2) {
            FloatingActionButtonImpl.this.f8885q = f10;
            return super.evaluate(f10, matrix, matrix2);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: FloatingActionButtonImpl.java */
    /* renamed from: com.google.android.material.floatingactionbutton.d$d */
    /* loaded from: classes.dex */
    public class d implements ValueAnimator.AnimatorUpdateListener {

        /* renamed from: a, reason: collision with root package name */
        final /* synthetic */ float f8903a;

        /* renamed from: b, reason: collision with root package name */
        final /* synthetic */ float f8904b;

        /* renamed from: c, reason: collision with root package name */
        final /* synthetic */ float f8905c;

        /* renamed from: d, reason: collision with root package name */
        final /* synthetic */ float f8906d;

        /* renamed from: e, reason: collision with root package name */
        final /* synthetic */ float f8907e;

        /* renamed from: f, reason: collision with root package name */
        final /* synthetic */ float f8908f;

        /* renamed from: g, reason: collision with root package name */
        final /* synthetic */ float f8909g;

        /* renamed from: h, reason: collision with root package name */
        final /* synthetic */ Matrix f8910h;

        d(float f10, float f11, float f12, float f13, float f14, float f15, float f16, Matrix matrix) {
            this.f8903a = f10;
            this.f8904b = f11;
            this.f8905c = f12;
            this.f8906d = f13;
            this.f8907e = f14;
            this.f8908f = f15;
            this.f8909g = f16;
            this.f8910h = matrix;
        }

        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            float floatValue = ((Float) valueAnimator.getAnimatedValue()).floatValue();
            FloatingActionButtonImpl.this.f8891w.setAlpha(AnimationUtils.b(this.f8903a, this.f8904b, 0.0f, 0.2f, floatValue));
            FloatingActionButtonImpl.this.f8891w.setScaleX(AnimationUtils.a(this.f8905c, this.f8906d, floatValue));
            FloatingActionButtonImpl.this.f8891w.setScaleY(AnimationUtils.a(this.f8907e, this.f8906d, floatValue));
            FloatingActionButtonImpl.this.f8885q = AnimationUtils.a(this.f8908f, this.f8909g, floatValue);
            FloatingActionButtonImpl.this.h(AnimationUtils.a(this.f8908f, this.f8909g, floatValue), this.f8910h);
            FloatingActionButtonImpl.this.f8891w.setImageMatrix(this.f8910h);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: FloatingActionButtonImpl.java */
    /* renamed from: com.google.android.material.floatingactionbutton.d$e */
    /* loaded from: classes.dex */
    public class e implements ViewTreeObserver.OnPreDrawListener {
        e() {
        }

        @Override // android.view.ViewTreeObserver.OnPreDrawListener
        public boolean onPreDraw() {
            FloatingActionButtonImpl.this.G();
            return true;
        }
    }

    /* compiled from: FloatingActionButtonImpl.java */
    /* renamed from: com.google.android.material.floatingactionbutton.d$f */
    /* loaded from: classes.dex */
    private class f extends l {
        f() {
            super(FloatingActionButtonImpl.this, null);
        }

        @Override // com.google.android.material.floatingactionbutton.FloatingActionButtonImpl.l
        protected float a() {
            return 0.0f;
        }
    }

    /* compiled from: FloatingActionButtonImpl.java */
    /* renamed from: com.google.android.material.floatingactionbutton.d$g */
    /* loaded from: classes.dex */
    private class g extends l {
        g() {
            super(FloatingActionButtonImpl.this, null);
        }

        @Override // com.google.android.material.floatingactionbutton.FloatingActionButtonImpl.l
        protected float a() {
            FloatingActionButtonImpl floatingActionButtonImpl = FloatingActionButtonImpl.this;
            return floatingActionButtonImpl.f8876h + floatingActionButtonImpl.f8877i;
        }
    }

    /* compiled from: FloatingActionButtonImpl.java */
    /* renamed from: com.google.android.material.floatingactionbutton.d$h */
    /* loaded from: classes.dex */
    private class h extends l {
        h() {
            super(FloatingActionButtonImpl.this, null);
        }

        @Override // com.google.android.material.floatingactionbutton.FloatingActionButtonImpl.l
        protected float a() {
            FloatingActionButtonImpl floatingActionButtonImpl = FloatingActionButtonImpl.this;
            return floatingActionButtonImpl.f8876h + floatingActionButtonImpl.f8878j;
        }
    }

    /* compiled from: FloatingActionButtonImpl.java */
    /* renamed from: com.google.android.material.floatingactionbutton.d$i */
    /* loaded from: classes.dex */
    interface i {
        void a();

        void b();
    }

    /* compiled from: FloatingActionButtonImpl.java */
    /* renamed from: com.google.android.material.floatingactionbutton.d$j */
    /* loaded from: classes.dex */
    interface j {
        void a();

        void b();
    }

    /* compiled from: FloatingActionButtonImpl.java */
    /* renamed from: com.google.android.material.floatingactionbutton.d$k */
    /* loaded from: classes.dex */
    private class k extends l {
        k() {
            super(FloatingActionButtonImpl.this, null);
        }

        @Override // com.google.android.material.floatingactionbutton.FloatingActionButtonImpl.l
        protected float a() {
            return FloatingActionButtonImpl.this.f8876h;
        }
    }

    /* compiled from: FloatingActionButtonImpl.java */
    /* renamed from: com.google.android.material.floatingactionbutton.d$l */
    /* loaded from: classes.dex */
    private abstract class l extends AnimatorListenerAdapter implements ValueAnimator.AnimatorUpdateListener {

        /* renamed from: a, reason: collision with root package name */
        private boolean f8917a;

        /* renamed from: b, reason: collision with root package name */
        private float f8918b;

        /* renamed from: c, reason: collision with root package name */
        private float f8919c;

        private l() {
        }

        protected abstract float a();

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            FloatingActionButtonImpl.this.f0((int) this.f8919c);
            this.f8917a = false;
        }

        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            if (!this.f8917a) {
                MaterialShapeDrawable materialShapeDrawable = FloatingActionButtonImpl.this.f8870b;
                this.f8918b = materialShapeDrawable == null ? 0.0f : materialShapeDrawable.v();
                this.f8919c = a();
                this.f8917a = true;
            }
            FloatingActionButtonImpl floatingActionButtonImpl = FloatingActionButtonImpl.this;
            float f10 = this.f8918b;
            floatingActionButtonImpl.f0((int) (f10 + ((this.f8919c - f10) * valueAnimator.getAnimatedFraction())));
        }

        /* synthetic */ l(FloatingActionButtonImpl floatingActionButtonImpl, a aVar) {
            this();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public FloatingActionButtonImpl(FloatingActionButton floatingActionButton, ShadowViewDelegate shadowViewDelegate) {
        this.f8891w = floatingActionButton;
        this.f8892x = shadowViewDelegate;
        StateListAnimator stateListAnimator = new StateListAnimator();
        this.f8880l = stateListAnimator;
        stateListAnimator.addState(E, k(new h()));
        stateListAnimator.addState(F, k(new g()));
        stateListAnimator.addState(G, k(new g()));
        stateListAnimator.addState(H, k(new g()));
        stateListAnimator.addState(I, k(new k()));
        stateListAnimator.addState(J, k(new f()));
        this.f8884p = floatingActionButton.getRotation();
    }

    private boolean Z() {
        return ViewCompat.Q(this.f8891w) && !this.f8891w.isInEditMode();
    }

    private void g0(ObjectAnimator objectAnimator) {
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void h(float f10, Matrix matrix) {
        matrix.reset();
        if (this.f8891w.getDrawable() == null || this.f8886r == 0) {
            return;
        }
        RectF rectF = this.f8894z;
        RectF rectF2 = this.A;
        rectF.set(0.0f, 0.0f, r0.getIntrinsicWidth(), r0.getIntrinsicHeight());
        int i10 = this.f8886r;
        rectF2.set(0.0f, 0.0f, i10, i10);
        matrix.setRectToRect(rectF, rectF2, Matrix.ScaleToFit.CENTER);
        int i11 = this.f8886r;
        matrix.postScale(f10, f10, i11 / 2.0f, i11 / 2.0f);
    }

    private AnimatorSet i(MotionSpec motionSpec, float f10, float f11, float f12) {
        ArrayList arrayList = new ArrayList();
        ObjectAnimator ofFloat = ObjectAnimator.ofFloat(this.f8891w, (Property<FloatingActionButton, Float>) View.ALPHA, f10);
        motionSpec.h("opacity").a(ofFloat);
        arrayList.add(ofFloat);
        ObjectAnimator ofFloat2 = ObjectAnimator.ofFloat(this.f8891w, (Property<FloatingActionButton, Float>) View.SCALE_X, f11);
        motionSpec.h("scale").a(ofFloat2);
        g0(ofFloat2);
        arrayList.add(ofFloat2);
        ObjectAnimator ofFloat3 = ObjectAnimator.ofFloat(this.f8891w, (Property<FloatingActionButton, Float>) View.SCALE_Y, f11);
        motionSpec.h("scale").a(ofFloat3);
        g0(ofFloat3);
        arrayList.add(ofFloat3);
        h(f12, this.B);
        ObjectAnimator ofObject = ObjectAnimator.ofObject(this.f8891w, new ImageMatrixProperty(), new c(), new Matrix(this.B));
        motionSpec.h("iconScale").a(ofObject);
        arrayList.add(ofObject);
        AnimatorSet animatorSet = new AnimatorSet();
        AnimatorSetCompat.a(animatorSet, arrayList);
        return animatorSet;
    }

    private AnimatorSet j(float f10, float f11, float f12) {
        AnimatorSet animatorSet = new AnimatorSet();
        ArrayList arrayList = new ArrayList();
        ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
        ofFloat.addUpdateListener(new d(this.f8891w.getAlpha(), f10, this.f8891w.getScaleX(), f11, this.f8891w.getScaleY(), this.f8885q, f12, new Matrix(this.B)));
        arrayList.add(ofFloat);
        AnimatorSetCompat.a(animatorSet, arrayList);
        animatorSet.setDuration(MotionUtils.d(this.f8891w.getContext(), R$attr.motionDurationLong1, this.f8891w.getContext().getResources().getInteger(R$integer.material_motion_duration_long_1)));
        animatorSet.setInterpolator(MotionUtils.e(this.f8891w.getContext(), R$attr.motionEasingStandard, AnimationUtils.f16556b));
        return animatorSet;
    }

    private ValueAnimator k(l lVar) {
        ValueAnimator valueAnimator = new ValueAnimator();
        valueAnimator.setInterpolator(D);
        valueAnimator.setDuration(100L);
        valueAnimator.addListener(lVar);
        valueAnimator.addUpdateListener(lVar);
        valueAnimator.setFloatValues(0.0f, 1.0f);
        return valueAnimator;
    }

    private ViewTreeObserver.OnPreDrawListener q() {
        if (this.C == null) {
            this.C = new e();
        }
        return this.C;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void A() {
        MaterialShapeDrawable materialShapeDrawable = this.f8870b;
        if (materialShapeDrawable != null) {
            MaterialShapeUtils.f(this.f8891w, materialShapeDrawable);
        }
        if (J()) {
            this.f8891w.getViewTreeObserver().addOnPreDrawListener(q());
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void B() {
        throw null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void C() {
        ViewTreeObserver viewTreeObserver = this.f8891w.getViewTreeObserver();
        ViewTreeObserver.OnPreDrawListener onPreDrawListener = this.C;
        if (onPreDrawListener != null) {
            viewTreeObserver.removeOnPreDrawListener(onPreDrawListener);
            this.C = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void D(int[] iArr) {
        throw null;
    }

    void E(float f10, float f11, float f12) {
        throw null;
    }

    void F(Rect rect) {
        Preconditions.e(this.f8873e, "Didn't initialize content background");
        if (Y()) {
            this.f8892x.c(new InsetDrawable(this.f8873e, rect.left, rect.top, rect.right, rect.bottom));
        } else {
            this.f8892x.c(this.f8873e);
        }
    }

    void G() {
        float rotation = this.f8891w.getRotation();
        if (this.f8884p != rotation) {
            this.f8884p = rotation;
            c0();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void H() {
        ArrayList<i> arrayList = this.f8890v;
        if (arrayList != null) {
            Iterator<i> it = arrayList.iterator();
            while (it.hasNext()) {
                it.next().b();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void I() {
        ArrayList<i> arrayList = this.f8890v;
        if (arrayList != null) {
            Iterator<i> it = arrayList.iterator();
            while (it.hasNext()) {
                it.next().a();
            }
        }
    }

    boolean J() {
        throw null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void K(ColorStateList colorStateList) {
        MaterialShapeDrawable materialShapeDrawable = this.f8870b;
        if (materialShapeDrawable != null) {
            materialShapeDrawable.setTintList(colorStateList);
        }
        BorderDrawable borderDrawable = this.f8872d;
        if (borderDrawable != null) {
            borderDrawable.c(colorStateList);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void L(PorterDuff.Mode mode) {
        MaterialShapeDrawable materialShapeDrawable = this.f8870b;
        if (materialShapeDrawable != null) {
            materialShapeDrawable.setTintMode(mode);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void M(float f10) {
        if (this.f8876h != f10) {
            this.f8876h = f10;
            E(f10, this.f8877i, this.f8878j);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void N(boolean z10) {
        this.f8874f = z10;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void O(MotionSpec motionSpec) {
        this.f8883o = motionSpec;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void P(float f10) {
        if (this.f8877i != f10) {
            this.f8877i = f10;
            E(this.f8876h, f10, this.f8878j);
        }
    }

    final void Q(float f10) {
        this.f8885q = f10;
        Matrix matrix = this.B;
        h(f10, matrix);
        this.f8891w.setImageMatrix(matrix);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void R(int i10) {
        if (this.f8886r != i10) {
            this.f8886r = i10;
            d0();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void S(int i10) {
        this.f8879k = i10;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void T(float f10) {
        if (this.f8878j != f10) {
            this.f8878j = f10;
            E(this.f8876h, this.f8877i, f10);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void U(ColorStateList colorStateList) {
        Drawable drawable = this.f8871c;
        if (drawable != null) {
            DrawableCompat.i(drawable, RippleUtils.d(colorStateList));
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void V(boolean z10) {
        this.f8875g = z10;
        e0();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void W(ShapeAppearanceModel shapeAppearanceModel) {
        this.f8869a = shapeAppearanceModel;
        MaterialShapeDrawable materialShapeDrawable = this.f8870b;
        if (materialShapeDrawable != null) {
            materialShapeDrawable.setShapeAppearanceModel(shapeAppearanceModel);
        }
        Object obj = this.f8871c;
        if (obj instanceof Shapeable) {
            ((Shapeable) obj).setShapeAppearanceModel(shapeAppearanceModel);
        }
        BorderDrawable borderDrawable = this.f8872d;
        if (borderDrawable != null) {
            borderDrawable.f(shapeAppearanceModel);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void X(MotionSpec motionSpec) {
        this.f8882n = motionSpec;
    }

    boolean Y() {
        throw null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final boolean a0() {
        return !this.f8874f || this.f8891w.getSizeDimension() >= this.f8879k;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void b0(j jVar, boolean z10) {
        AnimatorSet j10;
        if (y()) {
            return;
        }
        Animator animator = this.f8881m;
        if (animator != null) {
            animator.cancel();
        }
        boolean z11 = this.f8882n == null;
        if (Z()) {
            if (this.f8891w.getVisibility() != 0) {
                this.f8891w.setAlpha(0.0f);
                this.f8891w.setScaleY(z11 ? 0.4f : 0.0f);
                this.f8891w.setScaleX(z11 ? 0.4f : 0.0f);
                Q(z11 ? 0.4f : 0.0f);
            }
            MotionSpec motionSpec = this.f8882n;
            if (motionSpec != null) {
                j10 = i(motionSpec, 1.0f, 1.0f, 1.0f);
            } else {
                j10 = j(1.0f, 1.0f, 1.0f);
            }
            j10.addListener(new b(z10, jVar));
            ArrayList<Animator.AnimatorListener> arrayList = this.f8888t;
            if (arrayList != null) {
                Iterator<Animator.AnimatorListener> it = arrayList.iterator();
                while (it.hasNext()) {
                    j10.addListener(it.next());
                }
            }
            j10.start();
            return;
        }
        this.f8891w.internalSetVisibility(0, z10);
        this.f8891w.setAlpha(1.0f);
        this.f8891w.setScaleY(1.0f);
        this.f8891w.setScaleX(1.0f);
        Q(1.0f);
        if (jVar != null) {
            jVar.a();
        }
    }

    void c0() {
        throw null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void d0() {
        Q(this.f8885q);
    }

    public void e(Animator.AnimatorListener animatorListener) {
        if (this.f8889u == null) {
            this.f8889u = new ArrayList<>();
        }
        this.f8889u.add(animatorListener);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void e0() {
        Rect rect = this.f8893y;
        r(rect);
        F(rect);
        this.f8892x.a(rect.left, rect.top, rect.right, rect.bottom);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void f(Animator.AnimatorListener animatorListener) {
        if (this.f8888t == null) {
            this.f8888t = new ArrayList<>();
        }
        this.f8888t.add(animatorListener);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void f0(float f10) {
        MaterialShapeDrawable materialShapeDrawable = this.f8870b;
        if (materialShapeDrawable != null) {
            materialShapeDrawable.Z(f10);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void g(i iVar) {
        if (this.f8890v == null) {
            this.f8890v = new ArrayList<>();
        }
        this.f8890v.add(iVar);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final Drawable l() {
        return this.f8873e;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public float m() {
        throw null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean n() {
        return this.f8874f;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final MotionSpec o() {
        return this.f8883o;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public float p() {
        return this.f8877i;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void r(Rect rect) {
        int sizeDimension = this.f8874f ? (this.f8879k - this.f8891w.getSizeDimension()) / 2 : 0;
        int max = Math.max(sizeDimension, (int) Math.ceil(this.f8875g ? m() + this.f8878j : 0.0f));
        int max2 = Math.max(sizeDimension, (int) Math.ceil(r1 * 1.5f));
        rect.set(max, max2, max, max2);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public float s() {
        return this.f8878j;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final ShapeAppearanceModel t() {
        return this.f8869a;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final MotionSpec u() {
        return this.f8882n;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void v(j jVar, boolean z10) {
        AnimatorSet j10;
        if (x()) {
            return;
        }
        Animator animator = this.f8881m;
        if (animator != null) {
            animator.cancel();
        }
        if (Z()) {
            MotionSpec motionSpec = this.f8883o;
            if (motionSpec != null) {
                j10 = i(motionSpec, 0.0f, 0.0f, 0.0f);
            } else {
                j10 = j(0.0f, 0.4f, 0.4f);
            }
            j10.addListener(new a(z10, jVar));
            ArrayList<Animator.AnimatorListener> arrayList = this.f8889u;
            if (arrayList != null) {
                Iterator<Animator.AnimatorListener> it = arrayList.iterator();
                while (it.hasNext()) {
                    j10.addListener(it.next());
                }
            }
            j10.start();
            return;
        }
        this.f8891w.internalSetVisibility(z10 ? 8 : 4, z10);
        if (jVar != null) {
            jVar.b();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void w(ColorStateList colorStateList, PorterDuff.Mode mode, ColorStateList colorStateList2, int i10) {
        throw null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean x() {
        return this.f8891w.getVisibility() == 0 ? this.f8887s == 1 : this.f8887s != 2;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean y() {
        return this.f8891w.getVisibility() != 0 ? this.f8887s == 2 : this.f8887s != 1;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void z() {
        throw null;
    }
}
