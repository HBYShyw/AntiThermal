package com.google.android.material.progressindicator;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Rect;
import com.google.android.material.progressindicator.BaseProgressIndicatorSpec;
import g0.Animatable2Compat;
import r3.MaterialColors;
import s.FloatPropertyCompat;
import s.SpringAnimation;
import s.SpringForce;

/* compiled from: DeterminateDrawable.java */
/* renamed from: com.google.android.material.progressindicator.d, reason: use source file name */
/* loaded from: classes.dex */
public final class DeterminateDrawable<S extends BaseProgressIndicatorSpec> extends DrawableWithAnimatedVisibilityChange {

    /* renamed from: y, reason: collision with root package name */
    private static final FloatPropertyCompat<DeterminateDrawable> f9086y = new a("indicatorLevel");

    /* renamed from: t, reason: collision with root package name */
    private DrawingDelegate<S> f9087t;

    /* renamed from: u, reason: collision with root package name */
    private final SpringForce f9088u;

    /* renamed from: v, reason: collision with root package name */
    private final SpringAnimation f9089v;

    /* renamed from: w, reason: collision with root package name */
    private float f9090w;

    /* renamed from: x, reason: collision with root package name */
    private boolean f9091x;

    /* compiled from: DeterminateDrawable.java */
    /* renamed from: com.google.android.material.progressindicator.d$a */
    /* loaded from: classes.dex */
    class a extends FloatPropertyCompat<DeterminateDrawable> {
        a(String str) {
            super(str);
        }

        @Override // s.FloatPropertyCompat
        /* renamed from: c, reason: merged with bridge method [inline-methods] */
        public float a(DeterminateDrawable determinateDrawable) {
            return determinateDrawable.x() * 10000.0f;
        }

        @Override // s.FloatPropertyCompat
        /* renamed from: d, reason: merged with bridge method [inline-methods] */
        public void b(DeterminateDrawable determinateDrawable, float f10) {
            determinateDrawable.z(f10 / 10000.0f);
        }
    }

    DeterminateDrawable(Context context, BaseProgressIndicatorSpec baseProgressIndicatorSpec, DrawingDelegate<S> drawingDelegate) {
        super(context, baseProgressIndicatorSpec);
        this.f9091x = false;
        y(drawingDelegate);
        SpringForce springForce = new SpringForce();
        this.f9088u = springForce;
        springForce.d(1.0f);
        springForce.f(50.0f);
        SpringAnimation springAnimation = new SpringAnimation(this, f9086y);
        this.f9089v = springAnimation;
        springAnimation.v(springForce);
        m(1.0f);
    }

    public static DeterminateDrawable<CircularProgressIndicatorSpec> u(Context context, CircularProgressIndicatorSpec circularProgressIndicatorSpec) {
        return new DeterminateDrawable<>(context, circularProgressIndicatorSpec, new CircularDrawingDelegate(circularProgressIndicatorSpec));
    }

    public static DeterminateDrawable<LinearProgressIndicatorSpec> v(Context context, LinearProgressIndicatorSpec linearProgressIndicatorSpec) {
        return new DeterminateDrawable<>(context, linearProgressIndicatorSpec, new LinearDrawingDelegate(linearProgressIndicatorSpec));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public float x() {
        return this.f9090w;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void z(float f10) {
        this.f9090w = f10;
        invalidateSelf();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void A(float f10) {
        setLevel((int) (f10 * 10000.0f));
    }

    @Override // android.graphics.drawable.Drawable
    public void draw(Canvas canvas) {
        Rect rect = new Rect();
        if (!getBounds().isEmpty() && isVisible() && canvas.getClipBounds(rect)) {
            canvas.save();
            this.f9087t.g(canvas, g());
            this.f9087t.c(canvas, this.f9105q);
            this.f9087t.b(canvas, this.f9105q, 0.0f, x(), MaterialColors.a(this.f9094f.f9063c[0], getAlpha()));
            canvas.restore();
        }
    }

    @Override // com.google.android.material.progressindicator.DrawableWithAnimatedVisibilityChange, android.graphics.drawable.Drawable
    public /* bridge */ /* synthetic */ int getAlpha() {
        return super.getAlpha();
    }

    @Override // android.graphics.drawable.Drawable
    public int getIntrinsicHeight() {
        return this.f9087t.d();
    }

    @Override // android.graphics.drawable.Drawable
    public int getIntrinsicWidth() {
        return this.f9087t.e();
    }

    @Override // com.google.android.material.progressindicator.DrawableWithAnimatedVisibilityChange, android.graphics.drawable.Drawable
    public /* bridge */ /* synthetic */ int getOpacity() {
        return super.getOpacity();
    }

    @Override // com.google.android.material.progressindicator.DrawableWithAnimatedVisibilityChange
    public /* bridge */ /* synthetic */ boolean h() {
        return super.h();
    }

    @Override // com.google.android.material.progressindicator.DrawableWithAnimatedVisibilityChange
    public /* bridge */ /* synthetic */ boolean i() {
        return super.i();
    }

    @Override // com.google.android.material.progressindicator.DrawableWithAnimatedVisibilityChange, android.graphics.drawable.Animatable
    public /* bridge */ /* synthetic */ boolean isRunning() {
        return super.isRunning();
    }

    @Override // com.google.android.material.progressindicator.DrawableWithAnimatedVisibilityChange
    public /* bridge */ /* synthetic */ boolean j() {
        return super.j();
    }

    @Override // android.graphics.drawable.Drawable
    public void jumpToCurrentState() {
        this.f9089v.w();
        z(getLevel() / 10000.0f);
    }

    @Override // com.google.android.material.progressindicator.DrawableWithAnimatedVisibilityChange
    public /* bridge */ /* synthetic */ void l(Animatable2Compat animatable2Compat) {
        super.l(animatable2Compat);
    }

    @Override // android.graphics.drawable.Drawable
    protected boolean onLevelChange(int i10) {
        if (this.f9091x) {
            this.f9089v.w();
            z(i10 / 10000.0f);
            return true;
        }
        this.f9089v.l(x() * 10000.0f);
        this.f9089v.q(i10);
        return true;
    }

    @Override // com.google.android.material.progressindicator.DrawableWithAnimatedVisibilityChange
    public /* bridge */ /* synthetic */ boolean p(boolean z10, boolean z11, boolean z12) {
        return super.p(z10, z11, z12);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.android.material.progressindicator.DrawableWithAnimatedVisibilityChange
    public boolean q(boolean z10, boolean z11, boolean z12) {
        boolean q10 = super.q(z10, z11, z12);
        float a10 = this.f9095g.a(this.f9093e.getContentResolver());
        if (a10 == 0.0f) {
            this.f9091x = true;
        } else {
            this.f9091x = false;
            this.f9088u.f(50.0f / a10);
        }
        return q10;
    }

    @Override // com.google.android.material.progressindicator.DrawableWithAnimatedVisibilityChange
    public /* bridge */ /* synthetic */ boolean r(Animatable2Compat animatable2Compat) {
        return super.r(animatable2Compat);
    }

    @Override // com.google.android.material.progressindicator.DrawableWithAnimatedVisibilityChange, android.graphics.drawable.Drawable
    public /* bridge */ /* synthetic */ void setAlpha(int i10) {
        super.setAlpha(i10);
    }

    @Override // com.google.android.material.progressindicator.DrawableWithAnimatedVisibilityChange, android.graphics.drawable.Drawable
    public /* bridge */ /* synthetic */ void setColorFilter(ColorFilter colorFilter) {
        super.setColorFilter(colorFilter);
    }

    @Override // com.google.android.material.progressindicator.DrawableWithAnimatedVisibilityChange, android.graphics.drawable.Drawable
    public /* bridge */ /* synthetic */ boolean setVisible(boolean z10, boolean z11) {
        return super.setVisible(z10, z11);
    }

    @Override // com.google.android.material.progressindicator.DrawableWithAnimatedVisibilityChange, android.graphics.drawable.Animatable
    public /* bridge */ /* synthetic */ void start() {
        super.start();
    }

    @Override // com.google.android.material.progressindicator.DrawableWithAnimatedVisibilityChange, android.graphics.drawable.Animatable
    public /* bridge */ /* synthetic */ void stop() {
        super.stop();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public DrawingDelegate<S> w() {
        return this.f9087t;
    }

    void y(DrawingDelegate<S> drawingDelegate) {
        this.f9087t = drawingDelegate;
        drawingDelegate.f(this);
    }
}
