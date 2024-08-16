package com.coui.appcompat.cardview;

import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import m2.COUIRoundRectUtil;

/* compiled from: RoundRectDrawable.java */
/* loaded from: classes.dex */
class g extends Drawable {

    /* renamed from: a, reason: collision with root package name */
    private float f5592a;

    /* renamed from: c, reason: collision with root package name */
    private final RectF f5594c;

    /* renamed from: d, reason: collision with root package name */
    private final Rect f5595d;

    /* renamed from: e, reason: collision with root package name */
    private float f5596e;

    /* renamed from: h, reason: collision with root package name */
    private ColorStateList f5599h;

    /* renamed from: i, reason: collision with root package name */
    private PorterDuffColorFilter f5600i;

    /* renamed from: j, reason: collision with root package name */
    private ColorStateList f5601j;

    /* renamed from: f, reason: collision with root package name */
    private boolean f5597f = false;

    /* renamed from: g, reason: collision with root package name */
    private boolean f5598g = true;

    /* renamed from: k, reason: collision with root package name */
    private PorterDuff.Mode f5602k = PorterDuff.Mode.SRC_IN;

    /* renamed from: b, reason: collision with root package name */
    private final Paint f5593b = new Paint(5);

    /* JADX INFO: Access modifiers changed from: package-private */
    public g(ColorStateList colorStateList, float f10) {
        this.f5592a = f10;
        e(colorStateList);
        this.f5594c = new RectF();
        this.f5595d = new Rect();
    }

    private PorterDuffColorFilter a(ColorStateList colorStateList, PorterDuff.Mode mode) {
        if (colorStateList == null || mode == null) {
            return null;
        }
        return new PorterDuffColorFilter(colorStateList.getColorForState(getState(), 0), mode);
    }

    private void e(ColorStateList colorStateList) {
        if (colorStateList == null) {
            colorStateList = ColorStateList.valueOf(0);
        }
        this.f5599h = colorStateList;
        this.f5593b.setColor(colorStateList.getColorForState(getState(), this.f5599h.getDefaultColor()));
    }

    private void i(Rect rect) {
        if (rect == null) {
            rect = getBounds();
        }
        this.f5594c.set(rect.left, rect.top, rect.right, rect.bottom);
        this.f5595d.set(rect);
        if (this.f5597f) {
            this.f5595d.inset((int) Math.ceil(h.a(this.f5596e, this.f5592a, this.f5598g)), (int) Math.ceil(h.b(this.f5596e, this.f5592a, this.f5598g)));
            this.f5594c.set(this.f5595d);
        }
    }

    public ColorStateList b() {
        return this.f5599h;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public float c() {
        return this.f5596e;
    }

    public float d() {
        return this.f5592a;
    }

    @Override // android.graphics.drawable.Drawable
    public void draw(Canvas canvas) {
        boolean z10;
        Paint paint = this.f5593b;
        if (this.f5600i == null || paint.getColorFilter() != null) {
            z10 = false;
        } else {
            paint.setColorFilter(this.f5600i);
            z10 = true;
        }
        canvas.drawPath(COUIRoundRectUtil.a().c(this.f5594c, this.f5592a), this.f5593b);
        if (z10) {
            paint.setColorFilter(null);
        }
    }

    public void f(ColorStateList colorStateList) {
        e(colorStateList);
        invalidateSelf();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void g(float f10, boolean z10, boolean z11) {
        if (f10 == this.f5596e && this.f5597f == z10 && this.f5598g == z11) {
            return;
        }
        this.f5596e = f10;
        this.f5597f = z10;
        this.f5598g = z11;
        i(null);
        invalidateSelf();
    }

    @Override // android.graphics.drawable.Drawable
    public int getOpacity() {
        return -3;
    }

    @Override // android.graphics.drawable.Drawable
    public void getOutline(Outline outline) {
        outline.setRoundRect(this.f5595d, this.f5592a);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void h(float f10) {
        if (f10 == this.f5592a) {
            return;
        }
        this.f5592a = f10;
        i(null);
        invalidateSelf();
    }

    @Override // android.graphics.drawable.Drawable
    public boolean isStateful() {
        ColorStateList colorStateList;
        ColorStateList colorStateList2 = this.f5601j;
        return (colorStateList2 != null && colorStateList2.isStateful()) || ((colorStateList = this.f5599h) != null && colorStateList.isStateful()) || super.isStateful();
    }

    @Override // android.graphics.drawable.Drawable
    protected void onBoundsChange(Rect rect) {
        super.onBoundsChange(rect);
        i(rect);
    }

    @Override // android.graphics.drawable.Drawable
    protected boolean onStateChange(int[] iArr) {
        PorterDuff.Mode mode;
        ColorStateList colorStateList = this.f5599h;
        int colorForState = colorStateList.getColorForState(iArr, colorStateList.getDefaultColor());
        boolean z10 = colorForState != this.f5593b.getColor();
        if (z10) {
            this.f5593b.setColor(colorForState);
        }
        ColorStateList colorStateList2 = this.f5601j;
        if (colorStateList2 == null || (mode = this.f5602k) == null) {
            return z10;
        }
        this.f5600i = a(colorStateList2, mode);
        return true;
    }

    @Override // android.graphics.drawable.Drawable
    public void setAlpha(int i10) {
        this.f5593b.setAlpha(i10);
    }

    @Override // android.graphics.drawable.Drawable
    public void setColorFilter(ColorFilter colorFilter) {
        this.f5593b.setColorFilter(colorFilter);
    }

    @Override // android.graphics.drawable.Drawable
    public void setTintList(ColorStateList colorStateList) {
        this.f5601j = colorStateList;
        this.f5600i = a(colorStateList, this.f5602k);
        invalidateSelf();
    }

    @Override // android.graphics.drawable.Drawable
    public void setTintMode(PorterDuff.Mode mode) {
        this.f5602k = mode;
        this.f5600i = a(this.f5601j, mode);
        invalidateSelf();
    }
}
