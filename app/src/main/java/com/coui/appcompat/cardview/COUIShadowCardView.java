package com.coui.appcompat.cardView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;
import c4.ShapeAppearanceModel;
import c4.ShapeAppearancePathProvider;
import com.coui.appcompat.theme.COUIThemeUtils;
import com.support.nearx.R$attr;
import com.support.nearx.R$styleable;

/* loaded from: classes.dex */
public class COUIShadowCardView extends FrameLayout {

    /* renamed from: e, reason: collision with root package name */
    private int f5540e;

    /* renamed from: f, reason: collision with root package name */
    private int f5541f;

    /* renamed from: g, reason: collision with root package name */
    private int f5542g;

    /* renamed from: h, reason: collision with root package name */
    private int f5543h;

    /* renamed from: i, reason: collision with root package name */
    private int f5544i;

    /* renamed from: j, reason: collision with root package name */
    private boolean f5545j;

    /* renamed from: k, reason: collision with root package name */
    private boolean f5546k;

    /* renamed from: l, reason: collision with root package name */
    private boolean f5547l;

    /* renamed from: m, reason: collision with root package name */
    private boolean f5548m;

    /* renamed from: n, reason: collision with root package name */
    private int f5549n;

    /* renamed from: o, reason: collision with root package name */
    private int f5550o;

    /* renamed from: p, reason: collision with root package name */
    private int f5551p;

    /* renamed from: q, reason: collision with root package name */
    private ColorStateList f5552q;

    /* renamed from: r, reason: collision with root package name */
    private int f5553r;

    /* renamed from: s, reason: collision with root package name */
    private float f5554s;

    /* renamed from: t, reason: collision with root package name */
    private int f5555t;

    /* renamed from: u, reason: collision with root package name */
    private ColorStateList f5556u;

    /* renamed from: v, reason: collision with root package name */
    private ShapeAppearanceModel f5557v;

    /* renamed from: w, reason: collision with root package name */
    private final Path f5558w;

    /* renamed from: x, reason: collision with root package name */
    private final RectF f5559x;

    /* renamed from: y, reason: collision with root package name */
    private COUIShapeDrawable f5560y;

    /* renamed from: z, reason: collision with root package name */
    private boolean f5561z;

    public COUIShadowCardView(Context context) {
        this(context, null);
    }

    private void a() {
        ShapeAppearanceModel.b t7 = new ShapeAppearanceModel.b().L(0, this.f5542g).y(0, this.f5544i).G(0, this.f5541f).t(0, this.f5543h);
        if (this.f5547l) {
            t7.F(new COUIEmptyEdgeTreatment());
        }
        if (this.f5548m) {
            t7.s(new COUIEmptyEdgeTreatment());
        }
        if (this.f5545j) {
            t7.D(new COUIEmptyEdgeTreatment());
        }
        if (this.f5546k) {
            t7.E(new COUIEmptyEdgeTreatment());
        }
        if (this.f5545j || this.f5547l) {
            t7.I(new COUIEmptyCornerTreatment());
        }
        if (this.f5548m || this.f5545j) {
            t7.v(new COUIEmptyCornerTreatment());
        }
        if (this.f5547l || this.f5546k) {
            t7.N(new COUIEmptyCornerTreatment());
        }
        if (this.f5548m || this.f5546k) {
            t7.A(new COUIEmptyCornerTreatment());
        }
        this.f5557v = t7.m();
        this.f5561z = true;
    }

    private void b() {
        COUIShapeDrawable cOUIShapeDrawable = this.f5560y;
        if (cOUIShapeDrawable == null) {
            this.f5560y = new COUIShapeDrawable(this.f5557v);
        } else {
            cOUIShapeDrawable.setShapeAppearanceModel(this.f5557v);
        }
        this.f5560y.i0(2);
        this.f5560y.P(getContext());
        this.f5560y.Z(this.f5550o);
        this.f5560y.g0(this.f5549n);
        this.f5560y.h0(this.f5553r);
        this.f5560y.r0(this.f5551p);
        this.f5560y.a0(this.f5552q);
        this.f5560y.l0(this.f5554s, this.f5556u);
    }

    private void c() {
        setBackground(this.f5560y);
    }

    public int getCardBLCornerRadius() {
        return this.f5543h;
    }

    public int getCardBRCornerRadius() {
        return this.f5544i;
    }

    public int getCardCornerRadius() {
        return this.f5540e;
    }

    public int getCardTLCornerRadius() {
        return this.f5541f;
    }

    public int getCardTRCornerRadius() {
        return this.f5542g;
    }

    public ColorStateList getColorStateList() {
        return this.f5552q;
    }

    public COUIShapeDrawable getMaterialShapeDrawable() {
        return this.f5560y;
    }

    public int getShadowAngle() {
        return this.f5553r;
    }

    public int getShadowColor() {
        return this.f5549n;
    }

    public int getShadowOffset() {
        return this.f5551p;
    }

    public int getShadowSize() {
        return this.f5550o;
    }

    public int getStrokeColor() {
        return this.f5555t;
    }

    public ColorStateList getStrokeStateColor() {
        return this.f5556u;
    }

    public float getStrokeWidth() {
        return this.f5554s;
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        ViewParent parent = getParent();
        if (parent != null) {
            ((ViewGroup) parent).setClipChildren(false);
        }
    }

    @Override // android.view.View
    @SuppressLint({"RestrictedApi"})
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (this.f5561z) {
            this.f5559x.set(getBackground().getBounds());
            ShapeAppearancePathProvider.k().d(this.f5557v, 1.0f, this.f5559x, this.f5558w);
            this.f5561z = false;
        }
        canvas.clipPath(this.f5558w);
    }

    @Override // android.widget.FrameLayout, android.view.ViewGroup, android.view.View
    protected void onLayout(boolean z10, int i10, int i11, int i12, int i13) {
        super.onLayout(z10, i10, i11, i12, i13);
        this.f5561z = true;
    }

    public void setCardBLCornerRadius(int i10) {
        this.f5543h = i10;
        a();
        b();
        c();
    }

    public void setCardBRCornerRadius(int i10) {
        this.f5544i = i10;
        a();
        b();
        c();
    }

    public void setCardCornerRadius(int i10) {
        this.f5540e = i10;
        this.f5543h = i10;
        this.f5544i = i10;
        this.f5541f = i10;
        this.f5542g = i10;
        a();
        b();
        c();
    }

    public void setCardTLCornerRadius(int i10) {
        this.f5541f = i10;
        a();
        b();
        c();
    }

    public void setCardTRCornerRadius(int i10) {
        this.f5542g = i10;
        a();
        b();
        c();
    }

    public void setColorStateList(ColorStateList colorStateList) {
        this.f5552q = colorStateList;
        a();
        b();
        c();
    }

    public void setHideBottomShadow(boolean z10) {
        this.f5548m = z10;
        a();
        b();
        c();
    }

    public void setHideLeftShadow(boolean z10) {
        this.f5545j = z10;
        a();
        b();
        c();
    }

    public void setHideRightShadow(boolean z10) {
        this.f5546k = z10;
        a();
        b();
        c();
    }

    public void setHideTopShadow(boolean z10) {
        this.f5547l = z10;
        a();
        b();
        c();
    }

    public void setShadowAngle(int i10) {
        this.f5553r = i10;
        a();
        b();
        c();
    }

    public void setShadowColor(int i10) {
        this.f5549n = i10;
        a();
        b();
        c();
    }

    public void setShadowOffset(int i10) {
        this.f5551p = i10;
        a();
        b();
        c();
    }

    public void setShadowSize(int i10) {
        this.f5550o = i10;
        a();
        b();
        c();
    }

    public void setStrokeColor(int i10) {
        this.f5555t = i10;
        setStrokeStateColor(ColorStateList.valueOf(i10));
    }

    public void setStrokeStateColor(ColorStateList colorStateList) {
        this.f5556u = colorStateList;
        a();
        b();
        c();
    }

    public void setStrokeWidth(float f10) {
        this.f5554s = f10;
        a();
        b();
        c();
    }

    public COUIShadowCardView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public COUIShadowCardView(Context context, AttributeSet attributeSet, int i10) {
        super(context, attributeSet, i10);
        this.f5554s = 0.0f;
        this.f5555t = 0;
        this.f5556u = ColorStateList.valueOf(0);
        this.f5558w = new Path();
        this.f5559x = new RectF();
        this.f5561z = true;
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.COUIShadowCardView);
        int dimensionPixelSize = obtainStyledAttributes.getDimensionPixelSize(R$styleable.COUIShadowCardView_couiCardCornerRadius, 0);
        this.f5540e = dimensionPixelSize;
        this.f5541f = obtainStyledAttributes.getDimensionPixelSize(R$styleable.COUIShadowCardView_couiCardTLCornerRadius, dimensionPixelSize);
        this.f5542g = obtainStyledAttributes.getDimensionPixelSize(R$styleable.COUIShadowCardView_couiCardTRCornerRadius, this.f5540e);
        this.f5543h = obtainStyledAttributes.getDimensionPixelSize(R$styleable.COUIShadowCardView_couiCardBLCornerRadius, this.f5540e);
        this.f5544i = obtainStyledAttributes.getDimensionPixelSize(R$styleable.COUIShadowCardView_couiCardBRCornerRadius, this.f5540e);
        this.f5545j = obtainStyledAttributes.getBoolean(R$styleable.COUIShadowCardView_couiHideLeftShadow, false);
        this.f5546k = obtainStyledAttributes.getBoolean(R$styleable.COUIShadowCardView_couiHideRightShadow, false);
        this.f5547l = obtainStyledAttributes.getBoolean(R$styleable.COUIShadowCardView_couiHideTopShadow, false);
        this.f5548m = obtainStyledAttributes.getBoolean(R$styleable.COUIShadowCardView_couiHideBottomShadow, false);
        this.f5549n = obtainStyledAttributes.getColor(R$styleable.COUIShadowCardView_couiShadowColor, 14606046);
        this.f5550o = obtainStyledAttributes.getDimensionPixelSize(R$styleable.COUIShadowCardView_couiShadowSize, 0);
        this.f5553r = obtainStyledAttributes.getInteger(R$styleable.COUIShadowCardView_couiShadowAngle, 0);
        this.f5551p = obtainStyledAttributes.getDimensionPixelSize(R$styleable.COUIShadowCardView_couiShadowOffset, 7);
        ColorStateList colorStateList = obtainStyledAttributes.getColorStateList(R$styleable.COUIShadowCardView_couiCardBackgroundColor);
        this.f5552q = colorStateList;
        if (colorStateList == null) {
            this.f5552q = ColorStateList.valueOf(COUIThemeUtils.a(context, R$attr.couiColorBackgroundWithCard));
        }
        ColorStateList colorStateList2 = obtainStyledAttributes.getColorStateList(R$styleable.COUIShadowCardView_couiStrokeColor);
        this.f5556u = colorStateList2;
        if (colorStateList2 == null) {
            this.f5556u = ColorStateList.valueOf(0);
        }
        this.f5554s = obtainStyledAttributes.getDimensionPixelSize(R$styleable.COUIShadowCardView_couiStrokeWidth, 0);
        a();
        b();
        c();
        obtainStyledAttributes.recycle();
    }
}
