package com.coui.appcompat.cardview;

import android.R;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import com.support.control.R$color;
import com.support.control.R$styleable;

/* loaded from: classes.dex */
public class COUICardView extends FrameLayout {

    /* renamed from: l, reason: collision with root package name */
    private static final int[] f5581l = {R.attr.colorBackground};

    /* renamed from: m, reason: collision with root package name */
    private static final f f5582m;

    /* renamed from: e, reason: collision with root package name */
    private boolean f5583e;

    /* renamed from: f, reason: collision with root package name */
    private boolean f5584f;

    /* renamed from: g, reason: collision with root package name */
    int f5585g;

    /* renamed from: h, reason: collision with root package name */
    int f5586h;

    /* renamed from: i, reason: collision with root package name */
    final Rect f5587i;

    /* renamed from: j, reason: collision with root package name */
    final Rect f5588j;

    /* renamed from: k, reason: collision with root package name */
    private final e f5589k;

    /* loaded from: classes.dex */
    class a implements e {

        /* renamed from: a, reason: collision with root package name */
        private Drawable f5590a;

        a() {
        }

        @Override // com.coui.appcompat.cardview.e
        public void a(int i10, int i11, int i12, int i13) {
            COUICardView.this.f5588j.set(i10, i11, i12, i13);
            COUICardView cOUICardView = COUICardView.this;
            Rect rect = cOUICardView.f5587i;
            COUICardView.super.setPadding(i10 + rect.left, i11 + rect.top, i12 + rect.right, i13 + rect.bottom);
        }

        @Override // com.coui.appcompat.cardview.e
        public void b(Drawable drawable) {
            this.f5590a = drawable;
            COUICardView.this.setBackgroundDrawable(drawable);
        }

        @Override // com.coui.appcompat.cardview.e
        public boolean c() {
            return COUICardView.this.getPreventCornerOverlap();
        }

        @Override // com.coui.appcompat.cardview.e
        public boolean d() {
            return COUICardView.this.getUseCompatPadding();
        }

        @Override // com.coui.appcompat.cardview.e
        public Drawable e() {
            return this.f5590a;
        }

        @Override // com.coui.appcompat.cardview.e
        public View f() {
            return COUICardView.this;
        }
    }

    static {
        d dVar = new d();
        f5582m = dVar;
        dVar.a();
    }

    public COUICardView(Context context) {
        super(context);
        this.f5587i = new Rect();
        this.f5588j = new Rect();
        this.f5589k = new a();
        b(context, null, 0);
    }

    private void b(Context context, AttributeSet attributeSet, int i10) {
        int color;
        ColorStateList valueOf;
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.COUICardView, i10, 0);
        int i11 = R$styleable.COUICardView_cardBackgroundColor;
        if (obtainStyledAttributes.hasValue(i11)) {
            valueOf = obtainStyledAttributes.getColorStateList(i11);
        } else {
            TypedArray obtainStyledAttributes2 = getContext().obtainStyledAttributes(f5581l);
            int color2 = obtainStyledAttributes2.getColor(0, 0);
            obtainStyledAttributes2.recycle();
            float[] fArr = new float[3];
            Color.colorToHSV(color2, fArr);
            if (fArr[2] > 0.5f) {
                color = getResources().getColor(R$color.cardview_light_background);
            } else {
                color = getResources().getColor(R$color.cardview_dark_background);
            }
            valueOf = ColorStateList.valueOf(color);
        }
        ColorStateList colorStateList = valueOf;
        float dimension = obtainStyledAttributes.getDimension(R$styleable.COUICardView_cardCornerRadius, 0.0f);
        float dimension2 = obtainStyledAttributes.getDimension(R$styleable.COUICardView_cardElevation, 0.0f);
        float dimension3 = obtainStyledAttributes.getDimension(R$styleable.COUICardView_cardMaxElevation, 0.0f);
        this.f5583e = obtainStyledAttributes.getBoolean(R$styleable.COUICardView_cardUseCompatPadding, false);
        this.f5584f = obtainStyledAttributes.getBoolean(R$styleable.COUICardView_cardPreventCornerOverlap, true);
        int dimensionPixelSize = obtainStyledAttributes.getDimensionPixelSize(R$styleable.COUICardView_contentPadding, 0);
        this.f5587i.left = obtainStyledAttributes.getDimensionPixelSize(R$styleable.COUICardView_contentPaddingLeft, dimensionPixelSize);
        this.f5587i.top = obtainStyledAttributes.getDimensionPixelSize(R$styleable.COUICardView_contentPaddingTop, dimensionPixelSize);
        this.f5587i.right = obtainStyledAttributes.getDimensionPixelSize(R$styleable.COUICardView_contentPaddingRight, dimensionPixelSize);
        this.f5587i.bottom = obtainStyledAttributes.getDimensionPixelSize(R$styleable.COUICardView_contentPaddingBottom, dimensionPixelSize);
        float f10 = dimension2 > dimension3 ? dimension2 : dimension3;
        this.f5585g = obtainStyledAttributes.getDimensionPixelSize(R$styleable.COUICardView_android_minWidth, 0);
        this.f5586h = obtainStyledAttributes.getDimensionPixelSize(R$styleable.COUICardView_android_minHeight, 0);
        obtainStyledAttributes.recycle();
        f5582m.b(this.f5589k, context, colorStateList, dimension, dimension2, f10);
    }

    public ColorStateList getCardBackgroundColor() {
        return f5582m.g(this.f5589k);
    }

    public float getCardElevation() {
        return f5582m.k(this.f5589k);
    }

    public int getContentPaddingBottom() {
        return this.f5587i.bottom;
    }

    public int getContentPaddingLeft() {
        return this.f5587i.left;
    }

    public int getContentPaddingRight() {
        return this.f5587i.right;
    }

    public int getContentPaddingTop() {
        return this.f5587i.top;
    }

    public float getMaxCardElevation() {
        return f5582m.i(this.f5589k);
    }

    public boolean getPreventCornerOverlap() {
        return this.f5584f;
    }

    public float getRadius() {
        return f5582m.l(this.f5589k);
    }

    public boolean getUseCompatPadding() {
        return this.f5583e;
    }

    @Override // android.widget.FrameLayout, android.view.View
    protected void onMeasure(int i10, int i11) {
        if (!(f5582m instanceof d)) {
            int mode = View.MeasureSpec.getMode(i10);
            if (mode == Integer.MIN_VALUE || mode == 1073741824) {
                i10 = View.MeasureSpec.makeMeasureSpec(Math.max((int) Math.ceil(r0.f(this.f5589k)), View.MeasureSpec.getSize(i10)), mode);
            }
            int mode2 = View.MeasureSpec.getMode(i11);
            if (mode2 == Integer.MIN_VALUE || mode2 == 1073741824) {
                i11 = View.MeasureSpec.makeMeasureSpec(Math.max((int) Math.ceil(r0.h(this.f5589k)), View.MeasureSpec.getSize(i11)), mode2);
            }
            super.onMeasure(i10, i11);
            return;
        }
        super.onMeasure(i10, i11);
    }

    public void setCardBackgroundColor(int i10) {
        f5582m.j(this.f5589k, ColorStateList.valueOf(i10));
    }

    public void setCardElevation(float f10) {
        f5582m.m(this.f5589k, f10);
    }

    public void setMaxCardElevation(float f10) {
        f5582m.d(this.f5589k, f10);
    }

    @Override // android.view.View
    public void setMinimumHeight(int i10) {
        this.f5586h = i10;
        super.setMinimumHeight(i10);
    }

    @Override // android.view.View
    public void setMinimumWidth(int i10) {
        this.f5585g = i10;
        super.setMinimumWidth(i10);
    }

    @Override // android.view.View
    public void setPadding(int i10, int i11, int i12, int i13) {
    }

    @Override // android.view.View
    public void setPaddingRelative(int i10, int i11, int i12, int i13) {
    }

    public void setPreventCornerOverlap(boolean z10) {
        if (z10 != this.f5584f) {
            this.f5584f = z10;
            f5582m.e(this.f5589k);
        }
    }

    public void setRadius(float f10) {
        f5582m.c(this.f5589k, f10);
    }

    public void setUseCompatPadding(boolean z10) {
        if (this.f5583e != z10) {
            this.f5583e = z10;
            f5582m.n(this.f5589k);
        }
    }

    public void setCardBackgroundColor(ColorStateList colorStateList) {
        f5582m.j(this.f5589k, colorStateList);
    }

    public COUICardView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.f5587i = new Rect();
        this.f5588j = new Rect();
        this.f5589k = new a();
        b(context, attributeSet, 0);
    }

    public COUICardView(Context context, AttributeSet attributeSet, int i10) {
        super(context, attributeSet, i10);
        this.f5587i = new Rect();
        this.f5588j = new Rect();
        this.f5589k = new a();
        b(context, attributeSet, i10);
    }
}
