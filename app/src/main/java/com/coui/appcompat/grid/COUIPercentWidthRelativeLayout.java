package com.coui.appcompat.grid;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import com.support.appcompat.R$integer;
import com.support.appcompat.R$styleable;

/* loaded from: classes.dex */
public class COUIPercentWidthRelativeLayout extends RelativeLayout {

    /* renamed from: e, reason: collision with root package name */
    public int f6143e;

    /* renamed from: f, reason: collision with root package name */
    private int f6144f;

    /* renamed from: g, reason: collision with root package name */
    private int f6145g;

    /* renamed from: h, reason: collision with root package name */
    private boolean f6146h;

    /* renamed from: i, reason: collision with root package name */
    private int f6147i;

    /* renamed from: j, reason: collision with root package name */
    private int f6148j;

    /* renamed from: k, reason: collision with root package name */
    private int f6149k;

    /* renamed from: l, reason: collision with root package name */
    private int f6150l;

    /* renamed from: m, reason: collision with root package name */
    private boolean f6151m;

    /* renamed from: n, reason: collision with root package name */
    private boolean f6152n;

    /* renamed from: o, reason: collision with root package name */
    private int f6153o;

    public COUIPercentWidthRelativeLayout(Context context) {
        this(context, null);
    }

    private void d(AttributeSet attributeSet) {
        if (getContext() != null) {
            TypedArray obtainStyledAttributes = getContext().obtainStyledAttributes(attributeSet, R$styleable.COUIPercentWidthRelativeLayout);
            int i10 = R$styleable.COUIPercentWidthRelativeLayout_gridNumber;
            int i11 = R$integer.grid_guide_column_preference;
            this.f6145g = obtainStyledAttributes.getResourceId(i10, i11);
            this.f6144f = obtainStyledAttributes.getInteger(i10, getContext().getResources().getInteger(i11));
            this.f6149k = obtainStyledAttributes.getInteger(R$styleable.COUIPercentWidthRelativeLayout_paddingType, 0);
            this.f6150l = obtainStyledAttributes.getInteger(R$styleable.COUIPercentWidthRelativeLayout_paddingSize, 0);
            this.f6146h = obtainStyledAttributes.getBoolean(R$styleable.COUIPercentWidthRelativeLayout_percentIndentEnabled, true);
            this.f6143e = obtainStyledAttributes.getInt(R$styleable.COUIPercentWidthRelativeLayout_percentMode, 0);
            this.f6151m = obtainStyledAttributes.getBoolean(R$styleable.COUIPercentWidthRelativeLayout_isParentChildHierarchy, false);
            this.f6147i = getPaddingStart();
            this.f6148j = getPaddingEnd();
            obtainStyledAttributes.recycle();
        }
    }

    private void e() {
        Context context = getContext();
        if (context != null) {
            this.f6152n = COUIResponsiveUtils.f(getContext());
            if (context instanceof Activity) {
                this.f6153o = COUIResponsiveUtils.e((Activity) context);
            } else {
                this.f6153o = -1;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.widget.RelativeLayout, android.view.ViewGroup
    /* renamed from: a, reason: merged with bridge method [inline-methods] */
    public LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(-1, -1);
    }

    @Override // android.widget.RelativeLayout, android.view.ViewGroup
    /* renamed from: b, reason: merged with bridge method [inline-methods] */
    public LayoutParams generateLayoutParams(AttributeSet attributeSet) {
        return new LayoutParams(getContext(), attributeSet);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.widget.RelativeLayout, android.view.ViewGroup
    /* renamed from: c, reason: merged with bridge method [inline-methods] */
    public LayoutParams generateLayoutParams(ViewGroup.LayoutParams layoutParams) {
        return new LayoutParams(layoutParams);
    }

    @Override // android.widget.RelativeLayout, android.view.ViewGroup
    protected boolean checkLayoutParams(ViewGroup.LayoutParams layoutParams) {
        return layoutParams instanceof LayoutParams;
    }

    @Override // android.view.View
    protected void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        if (getContext() != null && this.f6145g != 0) {
            this.f6144f = getContext().getResources().getInteger(this.f6145g);
            e();
        }
        requestLayout();
    }

    @Override // android.widget.RelativeLayout, android.view.View
    protected void onMeasure(int i10, int i11) {
        int i12;
        if (this.f6146h) {
            i12 = COUIResponsiveUtils.l(this, i10, this.f6144f, this.f6149k, this.f6150l, this.f6143e, this.f6147i, this.f6148j, this.f6153o, this.f6151m, this.f6152n);
            for (int i13 = 0; i13 < getChildCount(); i13++) {
                LayoutParams layoutParams = (LayoutParams) getChildAt(i13).getLayoutParams();
                COUIResponsiveUtils.k(getContext(), getChildAt(i13), i12, this.f6149k, this.f6150l, layoutParams.f6154a, layoutParams.f6155b);
            }
        } else {
            i12 = i10;
        }
        super.onMeasure(i12, i11);
    }

    public void setIsParentChildHierarchy(boolean z10) {
        this.f6151m = z10;
        requestLayout();
    }

    public void setPercentIndentEnabled(boolean z10) {
        this.f6146h = z10;
        requestLayout();
    }

    /* loaded from: classes.dex */
    public static class LayoutParams extends RelativeLayout.LayoutParams {

        /* renamed from: a, reason: collision with root package name */
        public int f6154a;

        /* renamed from: b, reason: collision with root package name */
        public int f6155b;

        public LayoutParams(Context context, AttributeSet attributeSet) {
            super(context, attributeSet);
            a(context, attributeSet);
        }

        private void a(Context context, AttributeSet attributeSet) {
            TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.COUIPercentWidthRelativeLayout_Layout);
            this.f6154a = obtainStyledAttributes.getInt(R$styleable.COUIPercentWidthRelativeLayout_Layout_layout_gridNumber, 0);
            this.f6155b = obtainStyledAttributes.getInt(R$styleable.COUIPercentWidthRelativeLayout_Layout_layout_percentMode, 0);
            obtainStyledAttributes.recycle();
        }

        public LayoutParams(int i10, int i11) {
            super(i10, i11);
        }

        public LayoutParams(ViewGroup.LayoutParams layoutParams) {
            super(layoutParams);
        }
    }

    public COUIPercentWidthRelativeLayout(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public COUIPercentWidthRelativeLayout(Context context, AttributeSet attributeSet, int i10) {
        this(context, attributeSet, i10, 0);
    }

    public COUIPercentWidthRelativeLayout(Context context, AttributeSet attributeSet, int i10, int i11) {
        super(context, attributeSet, i10, i11);
        this.f6143e = 0;
        this.f6146h = true;
        this.f6152n = false;
        this.f6153o = 0;
        d(attributeSet);
        e();
    }
}
