package com.google.android.material.floatingactionbutton;

import android.annotation.TargetApi;
import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.LinearGradient;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import androidx.core.graphics.ColorUtils;
import c4.ShapeAppearanceModel;
import c4.ShapeAppearancePathProvider;

/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: BorderDrawable.java */
/* renamed from: com.google.android.material.floatingactionbutton.c, reason: use source file name */
/* loaded from: classes.dex */
public class BorderDrawable extends Drawable {

    /* renamed from: b, reason: collision with root package name */
    private final Paint f8853b;

    /* renamed from: h, reason: collision with root package name */
    float f8859h;

    /* renamed from: i, reason: collision with root package name */
    private int f8860i;

    /* renamed from: j, reason: collision with root package name */
    private int f8861j;

    /* renamed from: k, reason: collision with root package name */
    private int f8862k;

    /* renamed from: l, reason: collision with root package name */
    private int f8863l;

    /* renamed from: m, reason: collision with root package name */
    private int f8864m;

    /* renamed from: o, reason: collision with root package name */
    private ShapeAppearanceModel f8866o;

    /* renamed from: p, reason: collision with root package name */
    private ColorStateList f8867p;

    /* renamed from: a, reason: collision with root package name */
    private final ShapeAppearancePathProvider f8852a = ShapeAppearancePathProvider.k();

    /* renamed from: c, reason: collision with root package name */
    private final Path f8854c = new Path();

    /* renamed from: d, reason: collision with root package name */
    private final Rect f8855d = new Rect();

    /* renamed from: e, reason: collision with root package name */
    private final RectF f8856e = new RectF();

    /* renamed from: f, reason: collision with root package name */
    private final RectF f8857f = new RectF();

    /* renamed from: g, reason: collision with root package name */
    private final b f8858g = new b();

    /* renamed from: n, reason: collision with root package name */
    private boolean f8865n = true;

    /* compiled from: BorderDrawable.java */
    /* renamed from: com.google.android.material.floatingactionbutton.c$b */
    /* loaded from: classes.dex */
    private class b extends Drawable.ConstantState {
        private b() {
        }

        @Override // android.graphics.drawable.Drawable.ConstantState
        public int getChangingConfigurations() {
            return 0;
        }

        @Override // android.graphics.drawable.Drawable.ConstantState
        public Drawable newDrawable() {
            return BorderDrawable.this;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public BorderDrawable(ShapeAppearanceModel shapeAppearanceModel) {
        this.f8866o = shapeAppearanceModel;
        Paint paint = new Paint(1);
        this.f8853b = paint;
        paint.setStyle(Paint.Style.STROKE);
    }

    private Shader a() {
        copyBounds(this.f8855d);
        float height = this.f8859h / r0.height();
        return new LinearGradient(0.0f, r0.top, 0.0f, r0.bottom, new int[]{ColorUtils.i(this.f8860i, this.f8864m), ColorUtils.i(this.f8861j, this.f8864m), ColorUtils.i(ColorUtils.n(this.f8861j, 0), this.f8864m), ColorUtils.i(ColorUtils.n(this.f8863l, 0), this.f8864m), ColorUtils.i(this.f8863l, this.f8864m), ColorUtils.i(this.f8862k, this.f8864m)}, new float[]{0.0f, height, 0.5f, 0.5f, 1.0f - height, 1.0f}, Shader.TileMode.CLAMP);
    }

    protected RectF b() {
        this.f8857f.set(getBounds());
        return this.f8857f;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void c(ColorStateList colorStateList) {
        if (colorStateList != null) {
            this.f8864m = colorStateList.getColorForState(getState(), this.f8864m);
        }
        this.f8867p = colorStateList;
        this.f8865n = true;
        invalidateSelf();
    }

    public void d(float f10) {
        if (this.f8859h != f10) {
            this.f8859h = f10;
            this.f8853b.setStrokeWidth(f10 * 1.3333f);
            this.f8865n = true;
            invalidateSelf();
        }
    }

    @Override // android.graphics.drawable.Drawable
    public void draw(Canvas canvas) {
        if (this.f8865n) {
            this.f8853b.setShader(a());
            this.f8865n = false;
        }
        float strokeWidth = this.f8853b.getStrokeWidth() / 2.0f;
        copyBounds(this.f8855d);
        this.f8856e.set(this.f8855d);
        float min = Math.min(this.f8866o.r().a(b()), this.f8856e.width() / 2.0f);
        if (this.f8866o.u(b())) {
            this.f8856e.inset(strokeWidth, strokeWidth);
            canvas.drawRoundRect(this.f8856e, min, min, this.f8853b);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void e(int i10, int i11, int i12, int i13) {
        this.f8860i = i10;
        this.f8861j = i11;
        this.f8862k = i12;
        this.f8863l = i13;
    }

    public void f(ShapeAppearanceModel shapeAppearanceModel) {
        this.f8866o = shapeAppearanceModel;
        invalidateSelf();
    }

    @Override // android.graphics.drawable.Drawable
    public Drawable.ConstantState getConstantState() {
        return this.f8858g;
    }

    @Override // android.graphics.drawable.Drawable
    public int getOpacity() {
        return this.f8859h > 0.0f ? -3 : -2;
    }

    @Override // android.graphics.drawable.Drawable
    @TargetApi(21)
    public void getOutline(Outline outline) {
        if (this.f8866o.u(b())) {
            outline.setRoundRect(getBounds(), this.f8866o.r().a(b()));
            return;
        }
        copyBounds(this.f8855d);
        this.f8856e.set(this.f8855d);
        this.f8852a.d(this.f8866o, 1.0f, this.f8856e, this.f8854c);
        if (this.f8854c.isConvex()) {
            outline.setConvexPath(this.f8854c);
        }
    }

    @Override // android.graphics.drawable.Drawable
    public boolean getPadding(Rect rect) {
        if (!this.f8866o.u(b())) {
            return true;
        }
        int round = Math.round(this.f8859h);
        rect.set(round, round, round, round);
        return true;
    }

    @Override // android.graphics.drawable.Drawable
    public boolean isStateful() {
        ColorStateList colorStateList = this.f8867p;
        return (colorStateList != null && colorStateList.isStateful()) || super.isStateful();
    }

    @Override // android.graphics.drawable.Drawable
    protected void onBoundsChange(Rect rect) {
        this.f8865n = true;
    }

    @Override // android.graphics.drawable.Drawable
    protected boolean onStateChange(int[] iArr) {
        int colorForState;
        ColorStateList colorStateList = this.f8867p;
        if (colorStateList != null && (colorForState = colorStateList.getColorForState(iArr, this.f8864m)) != this.f8864m) {
            this.f8865n = true;
            this.f8864m = colorForState;
        }
        if (this.f8865n) {
            invalidateSelf();
        }
        return this.f8865n;
    }

    @Override // android.graphics.drawable.Drawable
    public void setAlpha(int i10) {
        this.f8853b.setAlpha(i10);
        invalidateSelf();
    }

    @Override // android.graphics.drawable.Drawable
    public void setColorFilter(ColorFilter colorFilter) {
        this.f8853b.setColorFilter(colorFilter);
        invalidateSelf();
    }
}
