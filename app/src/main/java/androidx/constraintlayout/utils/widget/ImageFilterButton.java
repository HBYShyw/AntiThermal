package androidx.constraintlayout.utils.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Outline;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewOutlineProvider;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.constraintlayout.utils.widget.ImageFilterView;
import androidx.constraintlayout.widget.R$styleable;

/* loaded from: classes.dex */
public class ImageFilterButton extends AppCompatImageButton {

    /* renamed from: e, reason: collision with root package name */
    private ImageFilterView.c f1764e;

    /* renamed from: f, reason: collision with root package name */
    private float f1765f;

    /* renamed from: g, reason: collision with root package name */
    private float f1766g;

    /* renamed from: h, reason: collision with root package name */
    private float f1767h;

    /* renamed from: i, reason: collision with root package name */
    private Path f1768i;

    /* renamed from: j, reason: collision with root package name */
    ViewOutlineProvider f1769j;

    /* renamed from: k, reason: collision with root package name */
    RectF f1770k;

    /* renamed from: l, reason: collision with root package name */
    Drawable[] f1771l;

    /* renamed from: m, reason: collision with root package name */
    LayerDrawable f1772m;

    /* renamed from: n, reason: collision with root package name */
    private boolean f1773n;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class a extends ViewOutlineProvider {
        a() {
        }

        @Override // android.view.ViewOutlineProvider
        public void getOutline(View view, Outline outline) {
            outline.setRoundRect(0, 0, ImageFilterButton.this.getWidth(), ImageFilterButton.this.getHeight(), (Math.min(r3, r4) * ImageFilterButton.this.f1766g) / 2.0f);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class b extends ViewOutlineProvider {
        b() {
        }

        @Override // android.view.ViewOutlineProvider
        public void getOutline(View view, Outline outline) {
            outline.setRoundRect(0, 0, ImageFilterButton.this.getWidth(), ImageFilterButton.this.getHeight(), ImageFilterButton.this.f1767h);
        }
    }

    public ImageFilterButton(Context context) {
        super(context);
        this.f1764e = new ImageFilterView.c();
        this.f1765f = 0.0f;
        this.f1766g = 0.0f;
        this.f1767h = Float.NaN;
        this.f1773n = true;
        c(context, null);
    }

    private void c(Context context, AttributeSet attributeSet) {
        setPadding(0, 0, 0, 0);
        if (attributeSet != null) {
            TypedArray obtainStyledAttributes = getContext().obtainStyledAttributes(attributeSet, R$styleable.ImageFilterView);
            int indexCount = obtainStyledAttributes.getIndexCount();
            Drawable drawable = obtainStyledAttributes.getDrawable(R$styleable.ImageFilterView_altSrc);
            for (int i10 = 0; i10 < indexCount; i10++) {
                int index = obtainStyledAttributes.getIndex(i10);
                if (index == R$styleable.ImageFilterView_crossfade) {
                    this.f1765f = obtainStyledAttributes.getFloat(index, 0.0f);
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
                    setOverlay(obtainStyledAttributes.getBoolean(index, this.f1773n));
                }
            }
            obtainStyledAttributes.recycle();
            if (drawable != null) {
                Drawable[] drawableArr = new Drawable[2];
                this.f1771l = drawableArr;
                drawableArr[0] = getDrawable();
                this.f1771l[1] = drawable;
                LayerDrawable layerDrawable = new LayerDrawable(this.f1771l);
                this.f1772m = layerDrawable;
                layerDrawable.getDrawable(1).setAlpha((int) (this.f1765f * 255.0f));
                super.setImageDrawable(this.f1772m);
            }
        }
    }

    private void setOverlay(boolean z10) {
        this.f1773n = z10;
    }

    @Override // android.view.View
    public void draw(Canvas canvas) {
        super.draw(canvas);
    }

    public float getContrast() {
        return this.f1764e.f1793f;
    }

    public float getCrossfade() {
        return this.f1765f;
    }

    public float getRound() {
        return this.f1767h;
    }

    public float getRoundPercent() {
        return this.f1766g;
    }

    public float getSaturation() {
        return this.f1764e.f1792e;
    }

    public float getWarmth() {
        return this.f1764e.f1794g;
    }

    public void setBrightness(float f10) {
        ImageFilterView.c cVar = this.f1764e;
        cVar.f1791d = f10;
        cVar.c(this);
    }

    public void setContrast(float f10) {
        ImageFilterView.c cVar = this.f1764e;
        cVar.f1793f = f10;
        cVar.c(this);
    }

    public void setCrossfade(float f10) {
        this.f1765f = f10;
        if (this.f1771l != null) {
            if (!this.f1773n) {
                this.f1772m.getDrawable(0).setAlpha((int) ((1.0f - this.f1765f) * 255.0f));
            }
            this.f1772m.getDrawable(1).setAlpha((int) (this.f1765f * 255.0f));
            super.setImageDrawable(this.f1772m);
        }
    }

    public void setRound(float f10) {
        if (Float.isNaN(f10)) {
            this.f1767h = f10;
            float f11 = this.f1766g;
            this.f1766g = -1.0f;
            setRoundPercent(f11);
            return;
        }
        boolean z10 = this.f1767h != f10;
        this.f1767h = f10;
        if (f10 != 0.0f) {
            if (this.f1768i == null) {
                this.f1768i = new Path();
            }
            if (this.f1770k == null) {
                this.f1770k = new RectF();
            }
            if (this.f1769j == null) {
                b bVar = new b();
                this.f1769j = bVar;
                setOutlineProvider(bVar);
            }
            setClipToOutline(true);
            this.f1770k.set(0.0f, 0.0f, getWidth(), getHeight());
            this.f1768i.reset();
            Path path = this.f1768i;
            RectF rectF = this.f1770k;
            float f12 = this.f1767h;
            path.addRoundRect(rectF, f12, f12, Path.Direction.CW);
        } else {
            setClipToOutline(false);
        }
        if (z10) {
            invalidateOutline();
        }
    }

    public void setRoundPercent(float f10) {
        boolean z10 = this.f1766g != f10;
        this.f1766g = f10;
        if (f10 != 0.0f) {
            if (this.f1768i == null) {
                this.f1768i = new Path();
            }
            if (this.f1770k == null) {
                this.f1770k = new RectF();
            }
            if (this.f1769j == null) {
                a aVar = new a();
                this.f1769j = aVar;
                setOutlineProvider(aVar);
            }
            setClipToOutline(true);
            int width = getWidth();
            int height = getHeight();
            float min = (Math.min(width, height) * this.f1766g) / 2.0f;
            this.f1770k.set(0.0f, 0.0f, width, height);
            this.f1768i.reset();
            this.f1768i.addRoundRect(this.f1770k, min, min, Path.Direction.CW);
        } else {
            setClipToOutline(false);
        }
        if (z10) {
            invalidateOutline();
        }
    }

    public void setSaturation(float f10) {
        ImageFilterView.c cVar = this.f1764e;
        cVar.f1792e = f10;
        cVar.c(this);
    }

    public void setWarmth(float f10) {
        ImageFilterView.c cVar = this.f1764e;
        cVar.f1794g = f10;
        cVar.c(this);
    }

    public ImageFilterButton(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.f1764e = new ImageFilterView.c();
        this.f1765f = 0.0f;
        this.f1766g = 0.0f;
        this.f1767h = Float.NaN;
        this.f1773n = true;
        c(context, attributeSet);
    }

    public ImageFilterButton(Context context, AttributeSet attributeSet, int i10) {
        super(context, attributeSet, i10);
        this.f1764e = new ImageFilterView.c();
        this.f1765f = 0.0f;
        this.f1766g = 0.0f;
        this.f1767h = Float.NaN;
        this.f1773n = true;
        c(context, attributeSet);
    }
}
