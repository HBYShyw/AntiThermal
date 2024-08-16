package androidx.constraintlayout.utils.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Outline;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.widget.ImageView;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.constraintlayout.widget.R$styleable;

/* loaded from: classes.dex */
public class ImageFilterView extends AppCompatImageView {

    /* renamed from: h, reason: collision with root package name */
    private c f1776h;

    /* renamed from: i, reason: collision with root package name */
    private boolean f1777i;

    /* renamed from: j, reason: collision with root package name */
    private float f1778j;

    /* renamed from: k, reason: collision with root package name */
    private float f1779k;

    /* renamed from: l, reason: collision with root package name */
    private float f1780l;

    /* renamed from: m, reason: collision with root package name */
    private Path f1781m;

    /* renamed from: n, reason: collision with root package name */
    ViewOutlineProvider f1782n;

    /* renamed from: o, reason: collision with root package name */
    RectF f1783o;

    /* renamed from: p, reason: collision with root package name */
    Drawable[] f1784p;

    /* renamed from: q, reason: collision with root package name */
    LayerDrawable f1785q;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class a extends ViewOutlineProvider {
        a() {
        }

        @Override // android.view.ViewOutlineProvider
        public void getOutline(View view, Outline outline) {
            outline.setRoundRect(0, 0, ImageFilterView.this.getWidth(), ImageFilterView.this.getHeight(), (Math.min(r3, r4) * ImageFilterView.this.f1779k) / 2.0f);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class b extends ViewOutlineProvider {
        b() {
        }

        @Override // android.view.ViewOutlineProvider
        public void getOutline(View view, Outline outline) {
            outline.setRoundRect(0, 0, ImageFilterView.this.getWidth(), ImageFilterView.this.getHeight(), ImageFilterView.this.f1780l);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static class c {

        /* renamed from: a, reason: collision with root package name */
        float[] f1788a = new float[20];

        /* renamed from: b, reason: collision with root package name */
        ColorMatrix f1789b = new ColorMatrix();

        /* renamed from: c, reason: collision with root package name */
        ColorMatrix f1790c = new ColorMatrix();

        /* renamed from: d, reason: collision with root package name */
        float f1791d = 1.0f;

        /* renamed from: e, reason: collision with root package name */
        float f1792e = 1.0f;

        /* renamed from: f, reason: collision with root package name */
        float f1793f = 1.0f;

        /* renamed from: g, reason: collision with root package name */
        float f1794g = 1.0f;

        private void a(float f10) {
            float[] fArr = this.f1788a;
            fArr[0] = f10;
            fArr[1] = 0.0f;
            fArr[2] = 0.0f;
            fArr[3] = 0.0f;
            fArr[4] = 0.0f;
            fArr[5] = 0.0f;
            fArr[6] = f10;
            fArr[7] = 0.0f;
            fArr[8] = 0.0f;
            fArr[9] = 0.0f;
            fArr[10] = 0.0f;
            fArr[11] = 0.0f;
            fArr[12] = f10;
            fArr[13] = 0.0f;
            fArr[14] = 0.0f;
            fArr[15] = 0.0f;
            fArr[16] = 0.0f;
            fArr[17] = 0.0f;
            fArr[18] = 1.0f;
            fArr[19] = 0.0f;
        }

        private void b(float f10) {
            float f11 = 1.0f - f10;
            float f12 = 0.2999f * f11;
            float f13 = 0.587f * f11;
            float f14 = f11 * 0.114f;
            float[] fArr = this.f1788a;
            fArr[0] = f12 + f10;
            fArr[1] = f13;
            fArr[2] = f14;
            fArr[3] = 0.0f;
            fArr[4] = 0.0f;
            fArr[5] = f12;
            fArr[6] = f13 + f10;
            fArr[7] = f14;
            fArr[8] = 0.0f;
            fArr[9] = 0.0f;
            fArr[10] = f12;
            fArr[11] = f13;
            fArr[12] = f14 + f10;
            fArr[13] = 0.0f;
            fArr[14] = 0.0f;
            fArr[15] = 0.0f;
            fArr[16] = 0.0f;
            fArr[17] = 0.0f;
            fArr[18] = 1.0f;
            fArr[19] = 0.0f;
        }

        private void d(float f10) {
            float log;
            float f11;
            float f12;
            if (f10 <= 0.0f) {
                f10 = 0.01f;
            }
            float f13 = (5000.0f / f10) / 100.0f;
            if (f13 > 66.0f) {
                double d10 = f13 - 60.0f;
                f11 = ((float) Math.pow(d10, -0.13320475816726685d)) * 329.69873f;
                log = ((float) Math.pow(d10, 0.07551484555006027d)) * 288.12216f;
            } else {
                log = (((float) Math.log(f13)) * 99.4708f) - 161.11957f;
                f11 = 255.0f;
            }
            if (f13 < 66.0f) {
                f12 = f13 > 19.0f ? (((float) Math.log(f13 - 10.0f)) * 138.51773f) - 305.0448f : 0.0f;
            } else {
                f12 = 255.0f;
            }
            float min = Math.min(255.0f, Math.max(f11, 0.0f));
            float min2 = Math.min(255.0f, Math.max(log, 0.0f));
            float min3 = Math.min(255.0f, Math.max(f12, 0.0f));
            float log2 = (((float) Math.log(50.0f)) * 99.4708f) - 161.11957f;
            float log3 = (((float) Math.log(40.0f)) * 138.51773f) - 305.0448f;
            float min4 = Math.min(255.0f, Math.max(255.0f, 0.0f));
            float min5 = Math.min(255.0f, Math.max(log2, 0.0f));
            float min6 = min3 / Math.min(255.0f, Math.max(log3, 0.0f));
            float[] fArr = this.f1788a;
            fArr[0] = min / min4;
            fArr[1] = 0.0f;
            fArr[2] = 0.0f;
            fArr[3] = 0.0f;
            fArr[4] = 0.0f;
            fArr[5] = 0.0f;
            fArr[6] = min2 / min5;
            fArr[7] = 0.0f;
            fArr[8] = 0.0f;
            fArr[9] = 0.0f;
            fArr[10] = 0.0f;
            fArr[11] = 0.0f;
            fArr[12] = min6;
            fArr[13] = 0.0f;
            fArr[14] = 0.0f;
            fArr[15] = 0.0f;
            fArr[16] = 0.0f;
            fArr[17] = 0.0f;
            fArr[18] = 1.0f;
            fArr[19] = 0.0f;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public void c(ImageView imageView) {
            boolean z10;
            this.f1789b.reset();
            float f10 = this.f1792e;
            boolean z11 = true;
            if (f10 != 1.0f) {
                b(f10);
                this.f1789b.set(this.f1788a);
                z10 = true;
            } else {
                z10 = false;
            }
            float f11 = this.f1793f;
            if (f11 != 1.0f) {
                this.f1790c.setScale(f11, f11, f11, 1.0f);
                this.f1789b.postConcat(this.f1790c);
                z10 = true;
            }
            float f12 = this.f1794g;
            if (f12 != 1.0f) {
                d(f12);
                this.f1790c.set(this.f1788a);
                this.f1789b.postConcat(this.f1790c);
                z10 = true;
            }
            float f13 = this.f1791d;
            if (f13 != 1.0f) {
                a(f13);
                this.f1790c.set(this.f1788a);
                this.f1789b.postConcat(this.f1790c);
            } else {
                z11 = z10;
            }
            if (z11) {
                imageView.setColorFilter(new ColorMatrixColorFilter(this.f1789b));
            } else {
                imageView.clearColorFilter();
            }
        }
    }

    public ImageFilterView(Context context) {
        super(context);
        this.f1776h = new c();
        this.f1777i = true;
        this.f1778j = 0.0f;
        this.f1779k = 0.0f;
        this.f1780l = Float.NaN;
        e(context, null);
    }

    private void e(Context context, AttributeSet attributeSet) {
        if (attributeSet != null) {
            TypedArray obtainStyledAttributes = getContext().obtainStyledAttributes(attributeSet, R$styleable.ImageFilterView);
            int indexCount = obtainStyledAttributes.getIndexCount();
            Drawable drawable = obtainStyledAttributes.getDrawable(R$styleable.ImageFilterView_altSrc);
            for (int i10 = 0; i10 < indexCount; i10++) {
                int index = obtainStyledAttributes.getIndex(i10);
                if (index == R$styleable.ImageFilterView_crossfade) {
                    this.f1778j = obtainStyledAttributes.getFloat(index, 0.0f);
                } else if (index == R$styleable.ImageFilterView_warmth) {
                    setWarmth(obtainStyledAttributes.getFloat(index, 0.0f));
                } else if (index == R$styleable.ImageFilterView_saturation) {
                    setSaturation(obtainStyledAttributes.getFloat(index, 0.0f));
                } else if (index == R$styleable.ImageFilterView_contrast) {
                    setContrast(obtainStyledAttributes.getFloat(index, 0.0f));
                } else if (index == R$styleable.ImageFilterView_round) {
                    setRound(obtainStyledAttributes.getDimension(index, 0.0f));
                } else if (index == R$styleable.ImageFilterView_roundPercent) {
                    setRoundPercent(obtainStyledAttributes.getFloat(index, 0.0f));
                } else if (index == R$styleable.ImageFilterView_overlay) {
                    setOverlay(obtainStyledAttributes.getBoolean(index, this.f1777i));
                }
            }
            obtainStyledAttributes.recycle();
            if (drawable != null) {
                Drawable[] drawableArr = new Drawable[2];
                this.f1784p = drawableArr;
                drawableArr[0] = getDrawable();
                this.f1784p[1] = drawable;
                LayerDrawable layerDrawable = new LayerDrawable(this.f1784p);
                this.f1785q = layerDrawable;
                layerDrawable.getDrawable(1).setAlpha((int) (this.f1778j * 255.0f));
                super.setImageDrawable(this.f1785q);
            }
        }
    }

    private void setOverlay(boolean z10) {
        this.f1777i = z10;
    }

    @Override // android.view.View
    public void draw(Canvas canvas) {
        super.draw(canvas);
    }

    public float getBrightness() {
        return this.f1776h.f1791d;
    }

    public float getContrast() {
        return this.f1776h.f1793f;
    }

    public float getCrossfade() {
        return this.f1778j;
    }

    public float getRound() {
        return this.f1780l;
    }

    public float getRoundPercent() {
        return this.f1779k;
    }

    public float getSaturation() {
        return this.f1776h.f1792e;
    }

    public float getWarmth() {
        return this.f1776h.f1794g;
    }

    public void setBrightness(float f10) {
        c cVar = this.f1776h;
        cVar.f1791d = f10;
        cVar.c(this);
    }

    public void setContrast(float f10) {
        c cVar = this.f1776h;
        cVar.f1793f = f10;
        cVar.c(this);
    }

    public void setCrossfade(float f10) {
        this.f1778j = f10;
        if (this.f1784p != null) {
            if (!this.f1777i) {
                this.f1785q.getDrawable(0).setAlpha((int) ((1.0f - this.f1778j) * 255.0f));
            }
            this.f1785q.getDrawable(1).setAlpha((int) (this.f1778j * 255.0f));
            super.setImageDrawable(this.f1785q);
        }
    }

    public void setRound(float f10) {
        if (Float.isNaN(f10)) {
            this.f1780l = f10;
            float f11 = this.f1779k;
            this.f1779k = -1.0f;
            setRoundPercent(f11);
            return;
        }
        boolean z10 = this.f1780l != f10;
        this.f1780l = f10;
        if (f10 != 0.0f) {
            if (this.f1781m == null) {
                this.f1781m = new Path();
            }
            if (this.f1783o == null) {
                this.f1783o = new RectF();
            }
            if (this.f1782n == null) {
                b bVar = new b();
                this.f1782n = bVar;
                setOutlineProvider(bVar);
            }
            setClipToOutline(true);
            this.f1783o.set(0.0f, 0.0f, getWidth(), getHeight());
            this.f1781m.reset();
            Path path = this.f1781m;
            RectF rectF = this.f1783o;
            float f12 = this.f1780l;
            path.addRoundRect(rectF, f12, f12, Path.Direction.CW);
        } else {
            setClipToOutline(false);
        }
        if (z10) {
            invalidateOutline();
        }
    }

    public void setRoundPercent(float f10) {
        boolean z10 = this.f1779k != f10;
        this.f1779k = f10;
        if (f10 != 0.0f) {
            if (this.f1781m == null) {
                this.f1781m = new Path();
            }
            if (this.f1783o == null) {
                this.f1783o = new RectF();
            }
            if (this.f1782n == null) {
                a aVar = new a();
                this.f1782n = aVar;
                setOutlineProvider(aVar);
            }
            setClipToOutline(true);
            int width = getWidth();
            int height = getHeight();
            float min = (Math.min(width, height) * this.f1779k) / 2.0f;
            this.f1783o.set(0.0f, 0.0f, width, height);
            this.f1781m.reset();
            this.f1781m.addRoundRect(this.f1783o, min, min, Path.Direction.CW);
        } else {
            setClipToOutline(false);
        }
        if (z10) {
            invalidateOutline();
        }
    }

    public void setSaturation(float f10) {
        c cVar = this.f1776h;
        cVar.f1792e = f10;
        cVar.c(this);
    }

    public void setWarmth(float f10) {
        c cVar = this.f1776h;
        cVar.f1794g = f10;
        cVar.c(this);
    }

    public ImageFilterView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.f1776h = new c();
        this.f1777i = true;
        this.f1778j = 0.0f;
        this.f1779k = 0.0f;
        this.f1780l = Float.NaN;
        e(context, attributeSet);
    }

    public ImageFilterView(Context context, AttributeSet attributeSet, int i10) {
        super(context, attributeSet, i10);
        this.f1776h = new c();
        this.f1777i = true;
        this.f1778j = 0.0f;
        this.f1779k = 0.0f;
        this.f1780l = Float.NaN;
        e(context, attributeSet);
    }
}
