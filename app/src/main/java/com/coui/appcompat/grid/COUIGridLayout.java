package com.coui.appcompat.grid;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import com.coui.component.responsiveui.ResponsiveUIModel;
import com.coui.component.responsiveui.layoutgrid.MarginType;
import com.support.appcompat.R$styleable;

/* loaded from: classes.dex */
public class COUIGridLayout extends GridLayout {

    /* renamed from: e, reason: collision with root package name */
    private float f6076e;

    /* renamed from: f, reason: collision with root package name */
    private float f6077f;

    /* renamed from: g, reason: collision with root package name */
    private float f6078g;

    /* renamed from: h, reason: collision with root package name */
    private float f6079h;

    /* renamed from: i, reason: collision with root package name */
    private float f6080i;

    /* renamed from: j, reason: collision with root package name */
    private float f6081j;

    /* renamed from: k, reason: collision with root package name */
    private float f6082k;

    /* renamed from: l, reason: collision with root package name */
    private int f6083l;

    /* renamed from: m, reason: collision with root package name */
    private int f6084m;

    /* renamed from: n, reason: collision with root package name */
    private int f6085n;

    /* renamed from: o, reason: collision with root package name */
    private int f6086o;

    /* renamed from: p, reason: collision with root package name */
    private int[] f6087p;

    /* renamed from: q, reason: collision with root package name */
    private int[] f6088q;

    /* renamed from: r, reason: collision with root package name */
    private int[] f6089r;

    /* renamed from: s, reason: collision with root package name */
    private int[] f6090s;

    /* renamed from: t, reason: collision with root package name */
    private int[] f6091t;

    /* renamed from: u, reason: collision with root package name */
    private int[] f6092u;

    /* renamed from: v, reason: collision with root package name */
    private float[] f6093v;

    /* renamed from: w, reason: collision with root package name */
    private int f6094w;

    /* renamed from: x, reason: collision with root package name */
    private boolean f6095x;

    /* renamed from: y, reason: collision with root package name */
    private ResponsiveUIModel f6096y;

    public COUIGridLayout(Context context) {
        this(context, null);
    }

    private int a() {
        if (this.f6095x) {
            return 0;
        }
        this.f6092u = new int[this.f6083l + 1];
        int i10 = 0;
        for (int i11 = 0; i11 <= this.f6083l; i11++) {
            int i12 = i11;
            while (true) {
                int[] iArr = this.f6090s;
                if (i12 < iArr.length) {
                    int i13 = this.f6083l;
                    if (i11 < i13) {
                        int[] iArr2 = this.f6092u;
                        if (iArr2[i11] < iArr[i12]) {
                            iArr2[i11] = iArr[i12];
                        }
                    }
                    if (i11 > 0 && i12 > 0) {
                        int[] iArr3 = this.f6091t;
                        if (i12 <= iArr3.length) {
                            int[] iArr4 = this.f6092u;
                            int i14 = i12 - 1;
                            if (iArr4[i11] < iArr3[i14]) {
                                iArr4[i11] = iArr3[i14];
                            }
                        }
                    }
                    i12 += i13;
                }
            }
            i10 += this.f6092u[i11];
        }
        return i10;
    }

    private float b() {
        float f10 = this.f6081j;
        if (f10 != 0.0f) {
            return f10;
        }
        float f11 = this.f6080i;
        if (f11 == 0.0f) {
            return 0.0f;
        }
        return (f11 / this.f6079h) * this.f6082k;
    }

    private int c() {
        if (this.f6095x) {
            return 0;
        }
        int i10 = 0;
        for (int i11 = 0; i11 <= this.f6083l; i11++) {
            int i12 = i11;
            int i13 = 0;
            while (true) {
                int[] iArr = this.f6090s;
                if (i12 < iArr.length) {
                    int i14 = this.f6083l;
                    if (i11 < i14 && i13 < iArr[i12]) {
                        i13 = iArr[i12];
                    }
                    if (i11 > 0 && i12 > 0) {
                        int[] iArr2 = this.f6091t;
                        if (i12 <= iArr2.length) {
                            int i15 = i12 - 1;
                            if (i13 < iArr2[i15]) {
                                i13 = iArr2[i15];
                            }
                        }
                    }
                    i12 += i14;
                }
            }
            i10 += i13;
        }
        return i10;
    }

    private void d() {
        if (getContext() == null) {
            return;
        }
        this.f6096y.rebuild(getMeasuredWidth(), getMeasuredHeight()).chooseMargin(this.f6085n == 1 ? MarginType.MARGIN_SMALL : MarginType.MARGIN_LARGE);
        this.f6094w = this.f6096y.margin();
        this.f6076e = this.f6096y.gutter();
        this.f6083l = this.f6096y.columnCount() / this.f6084m;
        int i10 = 0;
        this.f6082k = this.f6096y.width(0, r2 - 1);
        this.f6093v = new float[this.f6084m];
        while (true) {
            int i11 = this.f6083l;
            if (i10 < i11) {
                float[] fArr = this.f6093v;
                ResponsiveUIModel responsiveUIModel = this.f6096y;
                int i12 = this.f6084m;
                fArr[i10] = responsiveUIModel.width(i10 * i12, (i12 * r6) - 1);
                i10++;
            } else {
                this.f6092u = new int[i11 + 1];
                return;
            }
        }
    }

    private void e() {
        float widthWithoutPadding = getWidthWithoutPadding();
        float f10 = this.f6076e;
        this.f6083l = Math.max(1, (int) ((widthWithoutPadding + f10) / (f10 + this.f6079h)));
        float widthWithoutPadding2 = getWidthWithoutPadding() - c();
        float f11 = this.f6076e;
        this.f6083l = Math.max(1, (int) ((widthWithoutPadding2 + f11) / (f11 + this.f6079h)));
        float widthWithoutPadding3 = getWidthWithoutPadding() - a();
        float f12 = this.f6076e;
        this.f6082k = Math.max(0.0f, (widthWithoutPadding3 - (f12 * (r2 - 1))) / this.f6083l);
        this.f6081j = b();
    }

    private void f() {
        float widthWithoutPadding = getWidthWithoutPadding();
        float f10 = this.f6077f;
        this.f6083l = Math.max(1, (int) ((widthWithoutPadding + f10) / (f10 + this.f6082k)));
        float widthWithoutPadding2 = getWidthWithoutPadding() - c();
        float f11 = this.f6077f;
        this.f6083l = Math.max(1, (int) ((widthWithoutPadding2 + f11) / (f11 + this.f6082k)));
        this.f6076e = Math.max(0.0f, ((getWidthWithoutPadding() - a()) - (this.f6082k * this.f6083l)) / (r3 - 1));
    }

    private void g() {
        int childCount = getChildCount();
        this.f6087p = new int[childCount];
        this.f6088q = new int[childCount];
        this.f6090s = new int[childCount];
        this.f6091t = new int[childCount];
        if (this.f6095x) {
            return;
        }
        int i10 = 0;
        for (int i11 = 0; i11 < childCount; i11++) {
            View childAt = getChildAt(i11);
            ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) childAt.getLayoutParams();
            if (childAt.getVisibility() != 8) {
                this.f6087p[i10] = marginLayoutParams.topMargin;
                this.f6088q[i10] = marginLayoutParams.bottomMargin;
                this.f6090s[i10] = marginLayoutParams.getMarginStart();
                this.f6091t[i10] = marginLayoutParams.getMarginEnd();
                i10++;
            }
        }
    }

    private int getVisibleChildCount() {
        int i10 = 0;
        for (int i11 = 0; i11 < getChildCount(); i11++) {
            if (getChildAt(i11).getVisibility() != 8) {
                i10++;
            }
        }
        return i10;
    }

    private int getWidthWithoutPadding() {
        return (getMeasuredWidth() - getPaddingStart()) - getPaddingEnd();
    }

    private int h(int i10) {
        int i11;
        int i12 = 0;
        if (this.f6095x) {
            return 0;
        }
        this.f6089r = new int[i10 + 1];
        int i13 = 0;
        while (i12 <= i10) {
            int i14 = this.f6083l * i12;
            while (true) {
                i11 = i12 + 1;
                int i15 = this.f6083l;
                if (i14 < i11 * i15) {
                    int[] iArr = this.f6087p;
                    if (i14 < iArr.length) {
                        int[] iArr2 = this.f6089r;
                        if (iArr2[i12] < iArr[i14]) {
                            iArr2[i12] = iArr[i14];
                        }
                    }
                    if (i12 > 0 && i14 > 0) {
                        int i16 = i14 - i15;
                        int[] iArr3 = this.f6088q;
                        if (i16 < iArr3.length) {
                            int[] iArr4 = this.f6089r;
                            if (iArr4[i12] < iArr3[i14 - i15]) {
                                iArr4[i12] = iArr3[i14 - i15];
                            }
                        }
                    }
                    i14++;
                }
            }
            i13 += this.f6089r[i12];
            i12 = i11;
        }
        return i13;
    }

    private void i(AttributeSet attributeSet) {
        if (getContext() != null) {
            TypedArray obtainStyledAttributes = getContext().obtainStyledAttributes(attributeSet, R$styleable.COUIGridLayout);
            this.f6076e = obtainStyledAttributes.getDimension(R$styleable.COUIGridLayout_couiHorizontalGap, 0.0f);
            this.f6077f = obtainStyledAttributes.getDimension(R$styleable.COUIGridLayout_minHorizontalGap, 0.0f);
            this.f6078g = obtainStyledAttributes.getDimension(R$styleable.COUIGridLayout_couiVerticalGap, 0.0f);
            this.f6079h = obtainStyledAttributes.getDimension(R$styleable.COUIGridLayout_childMinWidth, 0.0f);
            this.f6080i = obtainStyledAttributes.getDimension(R$styleable.COUIGridLayout_childMinHeight, 0.0f);
            this.f6081j = obtainStyledAttributes.getDimension(R$styleable.COUIGridLayout_childHeight, 0.0f);
            this.f6082k = obtainStyledAttributes.getDimension(R$styleable.COUIGridLayout_childWidth, 0.0f);
            this.f6084m = obtainStyledAttributes.getInteger(R$styleable.COUIGridLayout_childGridNumber, 0);
            this.f6085n = obtainStyledAttributes.getInteger(R$styleable.COUIGridLayout_gridMarginType, 1);
            this.f6086o = obtainStyledAttributes.getInteger(R$styleable.COUIGridLayout_specificType, 0);
            obtainStyledAttributes.recycle();
        }
    }

    private void j() {
        if (getContext() != null) {
            this.f6096y = new ResponsiveUIModel(getContext(), 0, 0);
        }
    }

    private boolean k() {
        return getLayoutDirection() == 1;
    }

    private int l(int i10, double d10) {
        int h10 = h((int) d10);
        int mode = View.MeasureSpec.getMode(i10);
        int size = View.MeasureSpec.getSize(i10);
        if (mode == Integer.MIN_VALUE) {
            return Math.min(size, (int) ((this.f6081j * d10) + ((d10 - 1.0d) * this.f6078g) + h10));
        }
        if (mode == 0) {
            return (int) ((this.f6081j * d10) + ((d10 - 1.0d) * this.f6078g) + h10);
        }
        if (mode != 1073741824) {
            return 0;
        }
        return size;
    }

    @Override // android.widget.GridLayout, android.view.ViewGroup, android.view.View
    protected void onLayout(boolean z10, int i10, int i11, int i12, int i13) {
        int i14;
        int i15;
        super.onLayout(z10, i10, i11, i12, i13);
        int paddingStart = getPaddingStart() + this.f6094w;
        int paddingTop = getPaddingTop();
        int i16 = 0;
        for (int i17 = 0; i17 < getChildCount(); i17++) {
            View childAt = getChildAt(i17);
            float f10 = this.f6086o == 0 ? this.f6093v[i17 % this.f6083l] : this.f6082k;
            int max = this.f6095x ? 0 : Math.max(0, this.f6092u[i16 % this.f6083l]);
            int max2 = this.f6095x ? 0 : Math.max(0, this.f6089r[i16 / this.f6083l]);
            if (childAt.getVisibility() != 8) {
                if (k()) {
                    i15 = (getWidth() - paddingStart) - max;
                    i14 = (int) (i15 - f10);
                } else {
                    i14 = paddingStart + max;
                    i15 = (int) (i14 + f10);
                }
                int i18 = paddingTop + max2;
                childAt.layout(i14, i18, i15, (int) (i18 + this.f6081j));
                i16++;
                if (i16 % this.f6083l == 0) {
                    paddingStart = getPaddingStart() + this.f6094w;
                    paddingTop = (int) (paddingTop + this.f6081j + this.f6078g + max2);
                } else {
                    paddingStart = (int) (paddingStart + this.f6076e + f10 + max);
                }
            }
        }
    }

    @Override // android.widget.GridLayout, android.view.View
    protected void onMeasure(int i10, int i11) {
        super.onMeasure(i10, i11);
        g();
        int i12 = this.f6086o;
        if (i12 == 0) {
            d();
        } else if (i12 == 1) {
            e();
        } else if (i12 == 2) {
            f();
        }
        for (int i13 = 0; i13 < getChildCount(); i13++) {
            View childAt = getChildAt(i13);
            if (this.f6081j == 0.0f) {
                this.f6081j = childAt.getMeasuredHeight();
            }
            measureChild(childAt, GridLayout.getChildMeasureSpec(i10, 0, (int) this.f6082k), GridLayout.getChildMeasureSpec(i11, 0, (int) this.f6081j));
        }
        setMeasuredDimension(GridLayout.resolveSizeAndState(View.MeasureSpec.getSize(i10), i10, 0), l(i11, Math.ceil(getVisibleChildCount() / this.f6083l)));
    }

    public void setChildGridNumber(int i10) {
        this.f6084m = i10;
        requestLayout();
    }

    public void setChildHeight(float f10) {
        this.f6081j = f10;
        requestLayout();
    }

    public void setChildMinHeight(float f10) {
        this.f6080i = f10;
        requestLayout();
    }

    public void setChildMinWidth(float f10) {
        this.f6079h = f10;
        requestLayout();
    }

    public void setChildWidth(float f10) {
        this.f6082k = f10;
        requestLayout();
    }

    public void setGridMarginType(int i10) {
        this.f6085n = i10;
        requestLayout();
    }

    public void setHorizontalGap(float f10) {
        this.f6076e = f10;
        requestLayout();
    }

    public void setIsIgnoreChildMargin(boolean z10) {
        this.f6095x = z10;
    }

    public void setMinHorizontalGap(float f10) {
        this.f6077f = f10;
        requestLayout();
    }

    public void setType(int i10) {
        this.f6086o = i10;
        requestLayout();
    }

    public void setVerticalGap(float f10) {
        this.f6078g = f10;
        requestLayout();
    }

    public COUIGridLayout(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public COUIGridLayout(Context context, AttributeSet attributeSet, int i10) {
        this(context, attributeSet, i10, 0);
    }

    public COUIGridLayout(Context context, AttributeSet attributeSet, int i10, int i11) {
        super(context, attributeSet, i10, i11);
        this.f6095x = true;
        j();
        i(attributeSet);
    }
}
