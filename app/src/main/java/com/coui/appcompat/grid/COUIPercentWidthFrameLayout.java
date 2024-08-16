package com.coui.appcompat.grid;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import com.support.appcompat.R$integer;
import com.support.appcompat.R$styleable;

/* loaded from: classes.dex */
public class COUIPercentWidthFrameLayout extends FrameLayout {

    /* renamed from: e, reason: collision with root package name */
    public int f6108e;

    /* renamed from: f, reason: collision with root package name */
    private int f6109f;

    /* renamed from: g, reason: collision with root package name */
    private int f6110g;

    /* renamed from: h, reason: collision with root package name */
    private boolean f6111h;

    /* renamed from: i, reason: collision with root package name */
    private int f6112i;

    /* renamed from: j, reason: collision with root package name */
    private int f6113j;

    /* renamed from: k, reason: collision with root package name */
    private int f6114k;

    /* renamed from: l, reason: collision with root package name */
    private int f6115l;

    /* renamed from: m, reason: collision with root package name */
    private boolean f6116m;

    /* renamed from: n, reason: collision with root package name */
    private boolean f6117n;

    /* renamed from: o, reason: collision with root package name */
    private int f6118o;

    public COUIPercentWidthFrameLayout(Context context) {
        this(context, null);
    }

    private void c(AttributeSet attributeSet) {
        if (getContext() != null) {
            TypedArray obtainStyledAttributes = getContext().obtainStyledAttributes(attributeSet, R$styleable.COUIPercentWidthFrameLayout);
            int i10 = R$styleable.COUIPercentWidthFrameLayout_gridNumber;
            int i11 = R$integer.grid_guide_column_preference;
            this.f6110g = obtainStyledAttributes.getResourceId(i10, i11);
            this.f6109f = obtainStyledAttributes.getInteger(i10, getContext().getResources().getInteger(i11));
            this.f6114k = obtainStyledAttributes.getInteger(R$styleable.COUIPercentWidthFrameLayout_paddingType, 0);
            this.f6115l = obtainStyledAttributes.getInteger(R$styleable.COUIPercentWidthFrameLayout_paddingSize, 0);
            this.f6111h = obtainStyledAttributes.getBoolean(R$styleable.COUIPercentWidthFrameLayout_percentIndentEnabled, true);
            this.f6108e = obtainStyledAttributes.getInt(R$styleable.COUIPercentWidthFrameLayout_percentMode, 0);
            this.f6116m = obtainStyledAttributes.getBoolean(R$styleable.COUIPercentWidthFrameLayout_isParentChildHierarchy, false);
            this.f6112i = getPaddingStart();
            this.f6113j = getPaddingEnd();
            obtainStyledAttributes.recycle();
        }
    }

    private void d() {
        Context context = getContext();
        if (context != null) {
            this.f6117n = COUIResponsiveUtils.f(getContext());
            if (context instanceof Activity) {
                this.f6118o = COUIResponsiveUtils.e((Activity) context);
            } else {
                this.f6118o = -1;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.widget.FrameLayout, android.view.ViewGroup
    /* renamed from: a, reason: merged with bridge method [inline-methods] */
    public LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(-1, -1);
    }

    @Override // android.widget.FrameLayout, android.view.ViewGroup
    /* renamed from: b, reason: merged with bridge method [inline-methods] */
    public LayoutParams generateLayoutParams(AttributeSet attributeSet) {
        return new LayoutParams(getContext(), attributeSet);
    }

    @Override // android.widget.FrameLayout, android.view.ViewGroup
    protected boolean checkLayoutParams(ViewGroup.LayoutParams layoutParams) {
        return layoutParams instanceof LayoutParams;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.view.View
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        if (getContext() != null && this.f6110g != 0) {
            this.f6109f = getContext().getResources().getInteger(this.f6110g);
            d();
        }
        requestLayout();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.widget.FrameLayout, android.view.View
    public void onMeasure(int i10, int i11) {
        int i12;
        if (this.f6111h) {
            i12 = COUIResponsiveUtils.l(this, i10, this.f6109f, this.f6114k, this.f6115l, this.f6108e, this.f6112i, this.f6113j, this.f6118o, this.f6116m, this.f6117n);
            for (int i13 = 0; i13 < getChildCount(); i13++) {
                LayoutParams layoutParams = (LayoutParams) getChildAt(i13).getLayoutParams();
                COUIResponsiveUtils.k(getContext(), getChildAt(i13), i12, this.f6114k, this.f6115l, layoutParams.f6119a, layoutParams.f6120b);
            }
        } else {
            i12 = i10;
        }
        super.onMeasure(i12, i11);
    }

    public void setIsParentChildHierarchy(boolean z10) {
        this.f6116m = z10;
        requestLayout();
    }

    public void setPercentIndentEnabled(boolean z10) {
        this.f6111h = z10;
        requestLayout();
    }

    /* loaded from: classes.dex */
    public static class LayoutParams extends FrameLayout.LayoutParams {

        /* renamed from: a, reason: collision with root package name */
        public int f6119a;

        /* renamed from: b, reason: collision with root package name */
        public int f6120b;

        public LayoutParams(Context context, AttributeSet attributeSet) {
            super(context, attributeSet);
            a(context, attributeSet);
        }

        private void a(Context context, AttributeSet attributeSet) {
            TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.COUIPercentWidthFrameLayout_Layout);
            this.f6119a = obtainStyledAttributes.getInt(R$styleable.COUIPercentWidthFrameLayout_Layout_layout_gridNumber, 0);
            this.f6120b = obtainStyledAttributes.getInt(R$styleable.COUIPercentWidthFrameLayout_Layout_layout_percentMode, 0);
            obtainStyledAttributes.recycle();
        }

        public LayoutParams(int i10, int i11) {
            super(i10, i11);
        }

        public LayoutParams(ViewGroup.LayoutParams layoutParams) {
            super(layoutParams);
        }
    }

    public COUIPercentWidthFrameLayout(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public COUIPercentWidthFrameLayout(Context context, AttributeSet attributeSet, int i10) {
        super(context, attributeSet, i10);
        this.f6108e = 0;
        this.f6111h = true;
        this.f6117n = false;
        this.f6118o = 0;
        c(attributeSet);
        d();
    }

    @Override // android.widget.FrameLayout, android.view.ViewGroup
    protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams layoutParams) {
        return new LayoutParams(layoutParams);
    }
}
