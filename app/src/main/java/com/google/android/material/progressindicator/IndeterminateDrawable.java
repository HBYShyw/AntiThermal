package com.google.android.material.progressindicator;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import com.google.android.material.progressindicator.BaseProgressIndicatorSpec;
import g0.Animatable2Compat;

/* compiled from: IndeterminateDrawable.java */
/* renamed from: com.google.android.material.progressindicator.h, reason: use source file name */
/* loaded from: classes.dex */
public final class IndeterminateDrawable<S extends BaseProgressIndicatorSpec> extends DrawableWithAnimatedVisibilityChange {

    /* renamed from: t, reason: collision with root package name */
    private DrawingDelegate<S> f9114t;

    /* renamed from: u, reason: collision with root package name */
    private IndeterminateAnimatorDelegate<ObjectAnimator> f9115u;

    IndeterminateDrawable(Context context, BaseProgressIndicatorSpec baseProgressIndicatorSpec, DrawingDelegate<S> drawingDelegate, IndeterminateAnimatorDelegate<ObjectAnimator> indeterminateAnimatorDelegate) {
        super(context, baseProgressIndicatorSpec);
        x(drawingDelegate);
        w(indeterminateAnimatorDelegate);
    }

    public static IndeterminateDrawable<CircularProgressIndicatorSpec> s(Context context, CircularProgressIndicatorSpec circularProgressIndicatorSpec) {
        return new IndeterminateDrawable<>(context, circularProgressIndicatorSpec, new CircularDrawingDelegate(circularProgressIndicatorSpec), new CircularIndeterminateAnimatorDelegate(circularProgressIndicatorSpec));
    }

    public static IndeterminateDrawable<LinearProgressIndicatorSpec> t(Context context, LinearProgressIndicatorSpec linearProgressIndicatorSpec) {
        IndeterminateAnimatorDelegate linearIndeterminateDisjointAnimatorDelegate;
        LinearDrawingDelegate linearDrawingDelegate = new LinearDrawingDelegate(linearProgressIndicatorSpec);
        if (linearProgressIndicatorSpec.f9058g == 0) {
            linearIndeterminateDisjointAnimatorDelegate = new LinearIndeterminateContiguousAnimatorDelegate(linearProgressIndicatorSpec);
        } else {
            linearIndeterminateDisjointAnimatorDelegate = new LinearIndeterminateDisjointAnimatorDelegate(context, linearProgressIndicatorSpec);
        }
        return new IndeterminateDrawable<>(context, linearProgressIndicatorSpec, linearDrawingDelegate, linearIndeterminateDisjointAnimatorDelegate);
    }

    @Override // android.graphics.drawable.Drawable
    public void draw(Canvas canvas) {
        Rect rect = new Rect();
        if (getBounds().isEmpty() || !isVisible() || !canvas.getClipBounds(rect)) {
            return;
        }
        canvas.save();
        this.f9114t.g(canvas, g());
        this.f9114t.c(canvas, this.f9105q);
        int i10 = 0;
        while (true) {
            IndeterminateAnimatorDelegate<ObjectAnimator> indeterminateAnimatorDelegate = this.f9115u;
            int[] iArr = indeterminateAnimatorDelegate.f9113c;
            if (i10 < iArr.length) {
                DrawingDelegate<S> drawingDelegate = this.f9114t;
                Paint paint = this.f9105q;
                float[] fArr = indeterminateAnimatorDelegate.f9112b;
                int i11 = i10 * 2;
                drawingDelegate.b(canvas, paint, fArr[i11], fArr[i11 + 1], iArr[i10]);
                i10++;
            } else {
                canvas.restore();
                return;
            }
        }
    }

    @Override // com.google.android.material.progressindicator.DrawableWithAnimatedVisibilityChange, android.graphics.drawable.Drawable
    public /* bridge */ /* synthetic */ int getAlpha() {
        return super.getAlpha();
    }

    @Override // android.graphics.drawable.Drawable
    public int getIntrinsicHeight() {
        return this.f9114t.d();
    }

    @Override // android.graphics.drawable.Drawable
    public int getIntrinsicWidth() {
        return this.f9114t.e();
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

    @Override // com.google.android.material.progressindicator.DrawableWithAnimatedVisibilityChange
    public /* bridge */ /* synthetic */ void l(Animatable2Compat animatable2Compat) {
        super.l(animatable2Compat);
    }

    @Override // com.google.android.material.progressindicator.DrawableWithAnimatedVisibilityChange
    public /* bridge */ /* synthetic */ boolean p(boolean z10, boolean z11, boolean z12) {
        return super.p(z10, z11, z12);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.android.material.progressindicator.DrawableWithAnimatedVisibilityChange
    public boolean q(boolean z10, boolean z11, boolean z12) {
        boolean q10 = super.q(z10, z11, z12);
        if (!isRunning()) {
            this.f9115u.a();
        }
        this.f9095g.a(this.f9093e.getContentResolver());
        if (z10 && z12) {
            this.f9115u.g();
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
    public IndeterminateAnimatorDelegate<ObjectAnimator> u() {
        return this.f9115u;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public DrawingDelegate<S> v() {
        return this.f9114t;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void w(IndeterminateAnimatorDelegate<ObjectAnimator> indeterminateAnimatorDelegate) {
        this.f9115u = indeterminateAnimatorDelegate;
        indeterminateAnimatorDelegate.e(this);
    }

    void x(DrawingDelegate<S> drawingDelegate) {
        this.f9114t = drawingDelegate;
        drawingDelegate.f(this);
    }
}
