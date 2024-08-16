package androidx.cardview.widget;

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
import androidx.cardview.R$attr;
import androidx.cardview.R$color;
import androidx.cardview.R$style;
import androidx.cardview.R$styleable;

/* loaded from: classes.dex */
public class CardView extends FrameLayout {

    /* renamed from: l, reason: collision with root package name */
    private static final int[] f1355l = {R.attr.colorBackground};

    /* renamed from: m, reason: collision with root package name */
    private static final c f1356m;

    /* renamed from: e, reason: collision with root package name */
    private boolean f1357e;

    /* renamed from: f, reason: collision with root package name */
    private boolean f1358f;

    /* renamed from: g, reason: collision with root package name */
    int f1359g;

    /* renamed from: h, reason: collision with root package name */
    int f1360h;

    /* renamed from: i, reason: collision with root package name */
    final Rect f1361i;

    /* renamed from: j, reason: collision with root package name */
    final Rect f1362j;

    /* renamed from: k, reason: collision with root package name */
    private final b f1363k;

    /* loaded from: classes.dex */
    class a implements b {

        /* renamed from: a, reason: collision with root package name */
        private Drawable f1364a;

        a() {
        }

        @Override // androidx.cardview.widget.b
        public void a(int i10, int i11, int i12, int i13) {
            CardView.this.f1362j.set(i10, i11, i12, i13);
            CardView cardView = CardView.this;
            Rect rect = cardView.f1361i;
            CardView.super.setPadding(i10 + rect.left, i11 + rect.top, i12 + rect.right, i13 + rect.bottom);
        }

        @Override // androidx.cardview.widget.b
        public void b(Drawable drawable) {
            this.f1364a = drawable;
            CardView.this.setBackgroundDrawable(drawable);
        }

        @Override // androidx.cardview.widget.b
        public boolean c() {
            return CardView.this.getPreventCornerOverlap();
        }

        @Override // androidx.cardview.widget.b
        public boolean d() {
            return CardView.this.getUseCompatPadding();
        }

        @Override // androidx.cardview.widget.b
        public Drawable e() {
            return this.f1364a;
        }

        @Override // androidx.cardview.widget.b
        public View f() {
            return CardView.this;
        }
    }

    static {
        androidx.cardview.widget.a aVar = new androidx.cardview.widget.a();
        f1356m = aVar;
        aVar.a();
    }

    public CardView(Context context) {
        this(context, null);
    }

    public void f(int i10, int i11, int i12, int i13) {
        this.f1361i.set(i10, i11, i12, i13);
        f1356m.j(this.f1363k);
    }

    public ColorStateList getCardBackgroundColor() {
        return f1356m.i(this.f1363k);
    }

    public float getCardElevation() {
        return f1356m.d(this.f1363k);
    }

    public int getContentPaddingBottom() {
        return this.f1361i.bottom;
    }

    public int getContentPaddingLeft() {
        return this.f1361i.left;
    }

    public int getContentPaddingRight() {
        return this.f1361i.right;
    }

    public int getContentPaddingTop() {
        return this.f1361i.top;
    }

    public float getMaxCardElevation() {
        return f1356m.h(this.f1363k);
    }

    public boolean getPreventCornerOverlap() {
        return this.f1358f;
    }

    public float getRadius() {
        return f1356m.e(this.f1363k);
    }

    public boolean getUseCompatPadding() {
        return this.f1357e;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.widget.FrameLayout, android.view.View
    public void onMeasure(int i10, int i11) {
        if (!(f1356m instanceof androidx.cardview.widget.a)) {
            int mode = View.MeasureSpec.getMode(i10);
            if (mode == Integer.MIN_VALUE || mode == 1073741824) {
                i10 = View.MeasureSpec.makeMeasureSpec(Math.max((int) Math.ceil(r0.l(this.f1363k)), View.MeasureSpec.getSize(i10)), mode);
            }
            int mode2 = View.MeasureSpec.getMode(i11);
            if (mode2 == Integer.MIN_VALUE || mode2 == 1073741824) {
                i11 = View.MeasureSpec.makeMeasureSpec(Math.max((int) Math.ceil(r0.k(this.f1363k)), View.MeasureSpec.getSize(i11)), mode2);
            }
            super.onMeasure(i10, i11);
            return;
        }
        super.onMeasure(i10, i11);
    }

    public void setCardBackgroundColor(int i10) {
        f1356m.n(this.f1363k, ColorStateList.valueOf(i10));
    }

    public void setCardElevation(float f10) {
        f1356m.g(this.f1363k, f10);
    }

    public void setMaxCardElevation(float f10) {
        f1356m.o(this.f1363k, f10);
    }

    @Override // android.view.View
    public void setMinimumHeight(int i10) {
        this.f1360h = i10;
        super.setMinimumHeight(i10);
    }

    @Override // android.view.View
    public void setMinimumWidth(int i10) {
        this.f1359g = i10;
        super.setMinimumWidth(i10);
    }

    @Override // android.view.View
    public void setPadding(int i10, int i11, int i12, int i13) {
    }

    @Override // android.view.View
    public void setPaddingRelative(int i10, int i11, int i12, int i13) {
    }

    public void setPreventCornerOverlap(boolean z10) {
        if (z10 != this.f1358f) {
            this.f1358f = z10;
            f1356m.m(this.f1363k);
        }
    }

    public void setRadius(float f10) {
        f1356m.c(this.f1363k, f10);
    }

    public void setUseCompatPadding(boolean z10) {
        if (this.f1357e != z10) {
            this.f1357e = z10;
            f1356m.f(this.f1363k);
        }
    }

    public CardView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, R$attr.cardViewStyle);
    }

    public void setCardBackgroundColor(ColorStateList colorStateList) {
        f1356m.n(this.f1363k, colorStateList);
    }

    public CardView(Context context, AttributeSet attributeSet, int i10) {
        super(context, attributeSet, i10);
        int color;
        ColorStateList valueOf;
        Rect rect = new Rect();
        this.f1361i = rect;
        this.f1362j = new Rect();
        a aVar = new a();
        this.f1363k = aVar;
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.CardView, i10, R$style.CardView);
        int i11 = R$styleable.CardView_cardBackgroundColor;
        if (obtainStyledAttributes.hasValue(i11)) {
            valueOf = obtainStyledAttributes.getColorStateList(i11);
        } else {
            TypedArray obtainStyledAttributes2 = getContext().obtainStyledAttributes(f1355l);
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
        float dimension = obtainStyledAttributes.getDimension(R$styleable.CardView_cardCornerRadius, 0.0f);
        float dimension2 = obtainStyledAttributes.getDimension(R$styleable.CardView_cardElevation, 0.0f);
        float dimension3 = obtainStyledAttributes.getDimension(R$styleable.CardView_cardMaxElevation, 0.0f);
        this.f1357e = obtainStyledAttributes.getBoolean(R$styleable.CardView_cardUseCompatPadding, false);
        this.f1358f = obtainStyledAttributes.getBoolean(R$styleable.CardView_cardPreventCornerOverlap, true);
        int dimensionPixelSize = obtainStyledAttributes.getDimensionPixelSize(R$styleable.CardView_contentPadding, 0);
        rect.left = obtainStyledAttributes.getDimensionPixelSize(R$styleable.CardView_contentPaddingLeft, dimensionPixelSize);
        rect.top = obtainStyledAttributes.getDimensionPixelSize(R$styleable.CardView_contentPaddingTop, dimensionPixelSize);
        rect.right = obtainStyledAttributes.getDimensionPixelSize(R$styleable.CardView_contentPaddingRight, dimensionPixelSize);
        rect.bottom = obtainStyledAttributes.getDimensionPixelSize(R$styleable.CardView_contentPaddingBottom, dimensionPixelSize);
        float f10 = dimension2 > dimension3 ? dimension2 : dimension3;
        this.f1359g = obtainStyledAttributes.getDimensionPixelSize(R$styleable.CardView_android_minWidth, 0);
        this.f1360h = obtainStyledAttributes.getDimensionPixelSize(R$styleable.CardView_android_minHeight, 0);
        obtainStyledAttributes.recycle();
        f1356m.b(aVar, context, colorStateList, dimension, dimension2, f10);
    }
}
