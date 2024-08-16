package com.google.android.material.progressindicator;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ProgressBar;
import androidx.core.view.ViewCompat;
import com.google.android.material.R$attr;
import com.google.android.material.R$style;
import com.google.android.material.R$styleable;
import com.google.android.material.internal.ThemeEnforcement;
import com.google.android.material.progressindicator.BaseProgressIndicatorSpec;
import d4.MaterialThemeOverlay;
import g0.Animatable2Compat;
import java.util.Arrays;
import r3.MaterialColors;
import y3.AnimatorDurationScaleProvider;

/* loaded from: classes.dex */
public abstract class BaseProgressIndicator<S extends BaseProgressIndicatorSpec> extends ProgressBar {

    /* renamed from: s, reason: collision with root package name */
    static final int f9034s = R$style.Widget_MaterialComponents_ProgressIndicator;

    /* renamed from: e, reason: collision with root package name */
    S f9035e;

    /* renamed from: f, reason: collision with root package name */
    private int f9036f;

    /* renamed from: g, reason: collision with root package name */
    private boolean f9037g;

    /* renamed from: h, reason: collision with root package name */
    private boolean f9038h;

    /* renamed from: i, reason: collision with root package name */
    private final int f9039i;

    /* renamed from: j, reason: collision with root package name */
    private final int f9040j;

    /* renamed from: k, reason: collision with root package name */
    private long f9041k;

    /* renamed from: l, reason: collision with root package name */
    AnimatorDurationScaleProvider f9042l;

    /* renamed from: m, reason: collision with root package name */
    private boolean f9043m;

    /* renamed from: n, reason: collision with root package name */
    private int f9044n;

    /* renamed from: o, reason: collision with root package name */
    private final Runnable f9045o;

    /* renamed from: p, reason: collision with root package name */
    private final Runnable f9046p;

    /* renamed from: q, reason: collision with root package name */
    private final Animatable2Compat f9047q;

    /* renamed from: r, reason: collision with root package name */
    private final Animatable2Compat f9048r;

    /* loaded from: classes.dex */
    class a implements Runnable {
        a() {
        }

        @Override // java.lang.Runnable
        public void run() {
            BaseProgressIndicator.this.k();
        }
    }

    /* loaded from: classes.dex */
    class b implements Runnable {
        b() {
        }

        @Override // java.lang.Runnable
        public void run() {
            BaseProgressIndicator.this.j();
            BaseProgressIndicator.this.f9041k = -1L;
        }
    }

    /* loaded from: classes.dex */
    class c extends Animatable2Compat {
        c() {
        }

        @Override // g0.Animatable2Compat
        public void a(Drawable drawable) {
            BaseProgressIndicator.this.setIndeterminate(false);
            BaseProgressIndicator baseProgressIndicator = BaseProgressIndicator.this;
            baseProgressIndicator.o(baseProgressIndicator.f9036f, BaseProgressIndicator.this.f9037g);
        }
    }

    /* loaded from: classes.dex */
    class d extends Animatable2Compat {
        d() {
        }

        @Override // g0.Animatable2Compat
        public void a(Drawable drawable) {
            super.a(drawable);
            if (BaseProgressIndicator.this.f9043m) {
                return;
            }
            BaseProgressIndicator baseProgressIndicator = BaseProgressIndicator.this;
            baseProgressIndicator.setVisibility(baseProgressIndicator.f9044n);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public BaseProgressIndicator(Context context, AttributeSet attributeSet, int i10, int i11) {
        super(MaterialThemeOverlay.c(context, attributeSet, i10, f9034s), attributeSet, i10);
        this.f9041k = -1L;
        this.f9043m = false;
        this.f9044n = 4;
        this.f9045o = new a();
        this.f9046p = new b();
        this.f9047q = new c();
        this.f9048r = new d();
        Context context2 = getContext();
        this.f9035e = i(context2, attributeSet);
        TypedArray obtainStyledAttributes = ThemeEnforcement.obtainStyledAttributes(context2, attributeSet, R$styleable.BaseProgressIndicator, i10, i11, new int[0]);
        this.f9039i = obtainStyledAttributes.getInt(R$styleable.BaseProgressIndicator_showDelay, -1);
        this.f9040j = Math.min(obtainStyledAttributes.getInt(R$styleable.BaseProgressIndicator_minHideDelay, -1), 1000);
        obtainStyledAttributes.recycle();
        this.f9042l = new AnimatorDurationScaleProvider();
        this.f9038h = true;
    }

    private DrawingDelegate<S> getCurrentDrawingDelegate() {
        if (isIndeterminate()) {
            if (getIndeterminateDrawable() == null) {
                return null;
            }
            return getIndeterminateDrawable().v();
        }
        if (getProgressDrawable() == null) {
            return null;
        }
        return getProgressDrawable().w();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void j() {
        ((DrawableWithAnimatedVisibilityChange) getCurrentDrawable()).p(false, false, true);
        if (m()) {
            setVisibility(4);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void k() {
        if (this.f9040j > 0) {
            this.f9041k = SystemClock.uptimeMillis();
        }
        setVisibility(0);
    }

    private boolean m() {
        return (getProgressDrawable() == null || !getProgressDrawable().isVisible()) && (getIndeterminateDrawable() == null || !getIndeterminateDrawable().isVisible());
    }

    private void n() {
        if (getProgressDrawable() != null && getIndeterminateDrawable() != null) {
            getIndeterminateDrawable().u().d(this.f9047q);
        }
        if (getProgressDrawable() != null) {
            getProgressDrawable().l(this.f9048r);
        }
        if (getIndeterminateDrawable() != null) {
            getIndeterminateDrawable().l(this.f9048r);
        }
    }

    private void p() {
        if (getIndeterminateDrawable() != null) {
            getIndeterminateDrawable().r(this.f9048r);
            getIndeterminateDrawable().u().h();
        }
        if (getProgressDrawable() != null) {
            getProgressDrawable().r(this.f9048r);
        }
    }

    @Override // android.widget.ProgressBar
    public Drawable getCurrentDrawable() {
        return isIndeterminate() ? getIndeterminateDrawable() : getProgressDrawable();
    }

    public int getHideAnimationBehavior() {
        return this.f9035e.f9066f;
    }

    public int[] getIndicatorColor() {
        return this.f9035e.f9063c;
    }

    public int getShowAnimationBehavior() {
        return this.f9035e.f9065e;
    }

    public int getTrackColor() {
        return this.f9035e.f9064d;
    }

    public int getTrackCornerRadius() {
        return this.f9035e.f9062b;
    }

    public int getTrackThickness() {
        return this.f9035e.f9061a;
    }

    protected void h(boolean z10) {
        if (this.f9038h) {
            ((DrawableWithAnimatedVisibilityChange) getCurrentDrawable()).p(q(), false, z10);
        }
    }

    abstract S i(Context context, AttributeSet attributeSet);

    @Override // android.view.View
    public void invalidate() {
        super.invalidate();
        if (getCurrentDrawable() != null) {
            getCurrentDrawable().invalidateSelf();
        }
    }

    boolean l() {
        View view = this;
        while (view.getVisibility() == 0) {
            Object parent = view.getParent();
            if (parent == null) {
                return getWindowVisibility() == 0;
            }
            if (!(parent instanceof View)) {
                return true;
            }
            view = (View) parent;
        }
        return false;
    }

    public void o(int i10, boolean z10) {
        if (isIndeterminate()) {
            if (getProgressDrawable() != null) {
                this.f9036f = i10;
                this.f9037g = z10;
                this.f9043m = true;
                if (getIndeterminateDrawable().isVisible() && this.f9042l.a(getContext().getContentResolver()) != 0.0f) {
                    getIndeterminateDrawable().u().f();
                    return;
                } else {
                    this.f9047q.a(getIndeterminateDrawable());
                    return;
                }
            }
            return;
        }
        super.setProgress(i10);
        if (getProgressDrawable() == null || z10) {
            return;
        }
        getProgressDrawable().jumpToCurrentState();
    }

    @Override // android.widget.ProgressBar, android.view.View
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        n();
        if (q()) {
            k();
        }
    }

    @Override // android.widget.ProgressBar, android.view.View
    protected void onDetachedFromWindow() {
        removeCallbacks(this.f9046p);
        removeCallbacks(this.f9045o);
        ((DrawableWithAnimatedVisibilityChange) getCurrentDrawable()).h();
        p();
        super.onDetachedFromWindow();
    }

    @Override // android.widget.ProgressBar, android.view.View
    protected synchronized void onDraw(Canvas canvas) {
        int save = canvas.save();
        if (getPaddingLeft() != 0 || getPaddingTop() != 0) {
            canvas.translate(getPaddingLeft(), getPaddingTop());
        }
        if (getPaddingRight() != 0 || getPaddingBottom() != 0) {
            canvas.clipRect(0, 0, getWidth() - (getPaddingLeft() + getPaddingRight()), getHeight() - (getPaddingTop() + getPaddingBottom()));
        }
        getCurrentDrawable().draw(canvas);
        canvas.restoreToCount(save);
    }

    @Override // android.widget.ProgressBar, android.view.View
    protected synchronized void onMeasure(int i10, int i11) {
        int paddingLeft;
        int paddingTop;
        super.onMeasure(i10, i11);
        DrawingDelegate<S> currentDrawingDelegate = getCurrentDrawingDelegate();
        if (currentDrawingDelegate == null) {
            return;
        }
        int e10 = currentDrawingDelegate.e();
        int d10 = currentDrawingDelegate.d();
        if (e10 < 0) {
            paddingLeft = getMeasuredWidth();
        } else {
            paddingLeft = e10 + getPaddingLeft() + getPaddingRight();
        }
        if (d10 < 0) {
            paddingTop = getMeasuredHeight();
        } else {
            paddingTop = d10 + getPaddingTop() + getPaddingBottom();
        }
        setMeasuredDimension(paddingLeft, paddingTop);
    }

    @Override // android.view.View
    protected void onVisibilityChanged(View view, int i10) {
        super.onVisibilityChanged(view, i10);
        h(i10 == 0);
    }

    @Override // android.view.View
    protected void onWindowVisibilityChanged(int i10) {
        super.onWindowVisibilityChanged(i10);
        h(false);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean q() {
        return ViewCompat.P(this) && getWindowVisibility() == 0 && l();
    }

    public void setAnimatorDurationScaleProvider(AnimatorDurationScaleProvider animatorDurationScaleProvider) {
        this.f9042l = animatorDurationScaleProvider;
        if (getProgressDrawable() != null) {
            getProgressDrawable().f9095g = animatorDurationScaleProvider;
        }
        if (getIndeterminateDrawable() != null) {
            getIndeterminateDrawable().f9095g = animatorDurationScaleProvider;
        }
    }

    public void setHideAnimationBehavior(int i10) {
        this.f9035e.f9066f = i10;
        invalidate();
    }

    @Override // android.widget.ProgressBar
    public synchronized void setIndeterminate(boolean z10) {
        if (z10 == isIndeterminate()) {
            return;
        }
        DrawableWithAnimatedVisibilityChange drawableWithAnimatedVisibilityChange = (DrawableWithAnimatedVisibilityChange) getCurrentDrawable();
        if (drawableWithAnimatedVisibilityChange != null) {
            drawableWithAnimatedVisibilityChange.h();
        }
        super.setIndeterminate(z10);
        DrawableWithAnimatedVisibilityChange drawableWithAnimatedVisibilityChange2 = (DrawableWithAnimatedVisibilityChange) getCurrentDrawable();
        if (drawableWithAnimatedVisibilityChange2 != null) {
            drawableWithAnimatedVisibilityChange2.p(q(), false, false);
        }
        if ((drawableWithAnimatedVisibilityChange2 instanceof IndeterminateDrawable) && q()) {
            ((IndeterminateDrawable) drawableWithAnimatedVisibilityChange2).u().g();
        }
        this.f9043m = false;
    }

    @Override // android.widget.ProgressBar
    public void setIndeterminateDrawable(Drawable drawable) {
        if (drawable == null) {
            super.setIndeterminateDrawable(null);
        } else {
            if (drawable instanceof IndeterminateDrawable) {
                ((DrawableWithAnimatedVisibilityChange) drawable).h();
                super.setIndeterminateDrawable(drawable);
                return;
            }
            throw new IllegalArgumentException("Cannot set framework drawable as indeterminate drawable.");
        }
    }

    public void setIndicatorColor(int... iArr) {
        if (iArr.length == 0) {
            iArr = new int[]{MaterialColors.b(getContext(), R$attr.colorPrimary, -1)};
        }
        if (Arrays.equals(getIndicatorColor(), iArr)) {
            return;
        }
        this.f9035e.f9063c = iArr;
        getIndeterminateDrawable().u().c();
        invalidate();
    }

    @Override // android.widget.ProgressBar
    public synchronized void setProgress(int i10) {
        if (isIndeterminate()) {
            return;
        }
        o(i10, false);
    }

    @Override // android.widget.ProgressBar
    public void setProgressDrawable(Drawable drawable) {
        if (drawable == null) {
            super.setProgressDrawable(null);
        } else {
            if (drawable instanceof DeterminateDrawable) {
                DeterminateDrawable determinateDrawable = (DeterminateDrawable) drawable;
                determinateDrawable.h();
                super.setProgressDrawable(determinateDrawable);
                determinateDrawable.A(getProgress() / getMax());
                return;
            }
            throw new IllegalArgumentException("Cannot set framework drawable as progress drawable.");
        }
    }

    public void setShowAnimationBehavior(int i10) {
        this.f9035e.f9065e = i10;
        invalidate();
    }

    public void setTrackColor(int i10) {
        S s7 = this.f9035e;
        if (s7.f9064d != i10) {
            s7.f9064d = i10;
            invalidate();
        }
    }

    public void setTrackCornerRadius(int i10) {
        S s7 = this.f9035e;
        if (s7.f9062b != i10) {
            s7.f9062b = Math.min(i10, s7.f9061a / 2);
        }
    }

    public void setTrackThickness(int i10) {
        S s7 = this.f9035e;
        if (s7.f9061a != i10) {
            s7.f9061a = i10;
            requestLayout();
        }
    }

    public void setVisibilityAfterHide(int i10) {
        if (i10 != 0 && i10 != 4 && i10 != 8) {
            throw new IllegalArgumentException("The component's visibility must be one of VISIBLE, INVISIBLE, and GONE defined in View.");
        }
        this.f9044n = i10;
    }

    @Override // android.widget.ProgressBar
    public IndeterminateDrawable<S> getIndeterminateDrawable() {
        return (IndeterminateDrawable) super.getIndeterminateDrawable();
    }

    @Override // android.widget.ProgressBar
    public DeterminateDrawable<S> getProgressDrawable() {
        return (DeterminateDrawable) super.getProgressDrawable();
    }
}
