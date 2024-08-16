package androidx.cardview.widget;

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

/* compiled from: RoundRectDrawable.java */
/* loaded from: classes.dex */
class d extends Drawable {

    /* renamed from: a, reason: collision with root package name */
    private float f1366a;

    /* renamed from: c, reason: collision with root package name */
    private final RectF f1368c;

    /* renamed from: d, reason: collision with root package name */
    private final Rect f1369d;

    /* renamed from: e, reason: collision with root package name */
    private float f1370e;

    /* renamed from: h, reason: collision with root package name */
    private ColorStateList f1373h;

    /* renamed from: i, reason: collision with root package name */
    private PorterDuffColorFilter f1374i;

    /* renamed from: j, reason: collision with root package name */
    private ColorStateList f1375j;

    /* renamed from: f, reason: collision with root package name */
    private boolean f1371f = false;

    /* renamed from: g, reason: collision with root package name */
    private boolean f1372g = true;

    /* renamed from: k, reason: collision with root package name */
    private PorterDuff.Mode f1376k = PorterDuff.Mode.SRC_IN;

    /* renamed from: b, reason: collision with root package name */
    private final Paint f1367b = new Paint(5);

    /* JADX INFO: Access modifiers changed from: package-private */
    public d(ColorStateList colorStateList, float f10) {
        this.f1366a = f10;
        e(colorStateList);
        this.f1368c = new RectF();
        this.f1369d = new Rect();
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
        this.f1373h = colorStateList;
        this.f1367b.setColor(colorStateList.getColorForState(getState(), this.f1373h.getDefaultColor()));
    }

    private void i(Rect rect) {
        if (rect == null) {
            rect = getBounds();
        }
        this.f1368c.set(rect.left, rect.top, rect.right, rect.bottom);
        this.f1369d.set(rect);
        if (this.f1371f) {
            this.f1369d.inset((int) Math.ceil(e.a(this.f1370e, this.f1366a, this.f1372g)), (int) Math.ceil(e.b(this.f1370e, this.f1366a, this.f1372g)));
            this.f1368c.set(this.f1369d);
        }
    }

    public ColorStateList b() {
        return this.f1373h;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public float c() {
        return this.f1370e;
    }

    public float d() {
        return this.f1366a;
    }

    @Override // android.graphics.drawable.Drawable
    public void draw(Canvas canvas) {
        boolean z10;
        Paint paint = this.f1367b;
        if (this.f1374i == null || paint.getColorFilter() != null) {
            z10 = false;
        } else {
            paint.setColorFilter(this.f1374i);
            z10 = true;
        }
        RectF rectF = this.f1368c;
        float f10 = this.f1366a;
        canvas.drawRoundRect(rectF, f10, f10, paint);
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
        if (f10 == this.f1370e && this.f1371f == z10 && this.f1372g == z11) {
            return;
        }
        this.f1370e = f10;
        this.f1371f = z10;
        this.f1372g = z11;
        i(null);
        invalidateSelf();
    }

    @Override // android.graphics.drawable.Drawable
    public int getOpacity() {
        return -3;
    }

    @Override // android.graphics.drawable.Drawable
    public void getOutline(Outline outline) {
        outline.setRoundRect(this.f1369d, this.f1366a);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void h(float f10) {
        if (f10 == this.f1366a) {
            return;
        }
        this.f1366a = f10;
        i(null);
        invalidateSelf();
    }

    @Override // android.graphics.drawable.Drawable
    public boolean isStateful() {
        ColorStateList colorStateList;
        ColorStateList colorStateList2 = this.f1375j;
        return (colorStateList2 != null && colorStateList2.isStateful()) || ((colorStateList = this.f1373h) != null && colorStateList.isStateful()) || super.isStateful();
    }

    @Override // android.graphics.drawable.Drawable
    protected void onBoundsChange(Rect rect) {
        super.onBoundsChange(rect);
        i(rect);
    }

    @Override // android.graphics.drawable.Drawable
    protected boolean onStateChange(int[] iArr) {
        PorterDuff.Mode mode;
        ColorStateList colorStateList = this.f1373h;
        int colorForState = colorStateList.getColorForState(iArr, colorStateList.getDefaultColor());
        boolean z10 = colorForState != this.f1367b.getColor();
        if (z10) {
            this.f1367b.setColor(colorForState);
        }
        ColorStateList colorStateList2 = this.f1375j;
        if (colorStateList2 == null || (mode = this.f1376k) == null) {
            return z10;
        }
        this.f1374i = a(colorStateList2, mode);
        return true;
    }

    @Override // android.graphics.drawable.Drawable
    public void setAlpha(int i10) {
        this.f1367b.setAlpha(i10);
    }

    @Override // android.graphics.drawable.Drawable
    public void setColorFilter(ColorFilter colorFilter) {
        this.f1367b.setColorFilter(colorFilter);
    }

    @Override // android.graphics.drawable.Drawable
    public void setTintList(ColorStateList colorStateList) {
        this.f1375j = colorStateList;
        this.f1374i = a(colorStateList, this.f1376k);
        invalidateSelf();
    }

    @Override // android.graphics.drawable.Drawable
    public void setTintMode(PorterDuff.Mode mode) {
        this.f1376k = mode;
        this.f1374i = a(this.f1375j, mode);
        invalidateSelf();
    }
}
