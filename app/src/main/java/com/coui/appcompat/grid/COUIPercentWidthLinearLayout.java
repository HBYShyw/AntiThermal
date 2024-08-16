package com.coui.appcompat.grid;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import com.support.appcompat.R$integer;
import com.support.appcompat.R$styleable;

/* loaded from: classes.dex */
public class COUIPercentWidthLinearLayout extends LinearLayout {

    /* renamed from: e, reason: collision with root package name */
    public int f6121e;

    /* renamed from: f, reason: collision with root package name */
    private int f6122f;

    /* renamed from: g, reason: collision with root package name */
    private int f6123g;

    /* renamed from: h, reason: collision with root package name */
    private boolean f6124h;

    /* renamed from: i, reason: collision with root package name */
    private int f6125i;

    /* renamed from: j, reason: collision with root package name */
    private int f6126j;

    /* renamed from: k, reason: collision with root package name */
    private int f6127k;

    /* renamed from: l, reason: collision with root package name */
    private int f6128l;

    /* renamed from: m, reason: collision with root package name */
    private boolean f6129m;

    /* renamed from: n, reason: collision with root package name */
    private boolean f6130n;

    /* renamed from: o, reason: collision with root package name */
    private int f6131o;

    public COUIPercentWidthLinearLayout(Context context) {
        this(context, null);
    }

    private void d(AttributeSet attributeSet) {
        if (getContext() != null) {
            TypedArray obtainStyledAttributes = getContext().obtainStyledAttributes(attributeSet, R$styleable.COUIPercentWidthLinearLayout);
            int i10 = R$styleable.COUIPercentWidthLinearLayout_gridNumber;
            int i11 = R$integer.grid_guide_column_preference;
            this.f6123g = obtainStyledAttributes.getResourceId(i10, i11);
            this.f6122f = obtainStyledAttributes.getInteger(i10, getContext().getResources().getInteger(i11));
            this.f6127k = obtainStyledAttributes.getInteger(R$styleable.COUIPercentWidthLinearLayout_paddingType, 0);
            this.f6128l = obtainStyledAttributes.getInteger(R$styleable.COUIPercentWidthLinearLayout_paddingSize, 0);
            this.f6124h = obtainStyledAttributes.getBoolean(R$styleable.COUIPercentWidthLinearLayout_percentIndentEnabled, true);
            this.f6121e = obtainStyledAttributes.getInt(R$styleable.COUIPercentWidthLinearLayout_percentMode, 0);
            this.f6129m = obtainStyledAttributes.getBoolean(R$styleable.COUIPercentWidthLinearLayout_isParentChildHierarchy, false);
            this.f6125i = getPaddingStart();
            this.f6126j = getPaddingEnd();
            obtainStyledAttributes.recycle();
        }
    }

    private void e() {
        Context context = getContext();
        if (context != null) {
            this.f6130n = COUIResponsiveUtils.f(getContext());
            if (context instanceof Activity) {
                this.f6131o = COUIResponsiveUtils.e((Activity) context);
            } else {
                this.f6131o = -1;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.widget.LinearLayout, android.view.ViewGroup
    /* renamed from: a, reason: merged with bridge method [inline-methods] */
    public LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(-1, -1);
    }

    @Override // android.widget.LinearLayout, android.view.ViewGroup
    /* renamed from: b, reason: merged with bridge method [inline-methods] */
    public LayoutParams generateLayoutParams(AttributeSet attributeSet) {
        return new LayoutParams(getContext(), attributeSet);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.widget.LinearLayout, android.view.ViewGroup
    /* renamed from: c, reason: merged with bridge method [inline-methods] */
    public LayoutParams generateLayoutParams(ViewGroup.LayoutParams layoutParams) {
        return new LayoutParams(layoutParams);
    }

    @Override // android.widget.LinearLayout, android.view.ViewGroup
    protected boolean checkLayoutParams(ViewGroup.LayoutParams layoutParams) {
        return layoutParams instanceof LayoutParams;
    }

    @Override // android.view.View
    protected void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        if (getContext() != null && this.f6123g != 0) {
            this.f6122f = getContext().getResources().getInteger(this.f6123g);
            e();
        }
        requestLayout();
    }

    @Override // android.widget.LinearLayout, android.view.View
    protected void onMeasure(int i10, int i11) {
        int i12;
        if (this.f6124h) {
            i12 = COUIResponsiveUtils.l(this, i10, this.f6122f, this.f6127k, this.f6128l, this.f6121e, this.f6125i, this.f6126j, this.f6131o, this.f6129m, this.f6130n);
            for (int i13 = 0; i13 < getChildCount(); i13++) {
                LayoutParams layoutParams = (LayoutParams) getChildAt(i13).getLayoutParams();
                COUIResponsiveUtils.k(getContext(), getChildAt(i13), i12, this.f6127k, this.f6128l, layoutParams.f6132a, layoutParams.f6133b);
            }
        } else {
            i12 = i10;
        }
        super.onMeasure(i12, i11);
    }

    public void setIsParentChildHierarchy(boolean z10) {
        this.f6129m = z10;
        requestLayout();
    }

    public void setPercentIndentEnabled(boolean z10) {
        this.f6124h = z10;
        requestLayout();
    }

    /* loaded from: classes.dex */
    public static class LayoutParams extends LinearLayout.LayoutParams {

        /* renamed from: a, reason: collision with root package name */
        public int f6132a;

        /* renamed from: b, reason: collision with root package name */
        public int f6133b;

        public LayoutParams(Context context, AttributeSet attributeSet) {
            super(context, attributeSet);
            a(context, attributeSet);
        }

        private void a(Context context, AttributeSet attributeSet) {
            TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.COUIPercentWidthLinearLayout_Layout);
            this.f6132a = obtainStyledAttributes.getInt(R$styleable.COUIPercentWidthLinearLayout_Layout_layout_gridNumber, 0);
            this.f6133b = obtainStyledAttributes.getInt(R$styleable.COUIPercentWidthLinearLayout_Layout_layout_percentMode, 0);
            obtainStyledAttributes.recycle();
        }

        public LayoutParams(int i10, int i11) {
            super(i10, i11);
        }

        public LayoutParams(ViewGroup.LayoutParams layoutParams) {
            super(layoutParams);
        }
    }

    public COUIPercentWidthLinearLayout(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public COUIPercentWidthLinearLayout(Context context, AttributeSet attributeSet, int i10) {
        this(context, attributeSet, i10, 0);
    }

    public COUIPercentWidthLinearLayout(Context context, AttributeSet attributeSet, int i10, int i11) {
        super(context, attributeSet, i10, i11);
        this.f6121e = 0;
        this.f6124h = true;
        this.f6130n = false;
        this.f6131o = 0;
        d(attributeSet);
        e();
    }
}
