package com.coui.appcompat.grid;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.ViewGroup;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.support.appcompat.R$integer;
import com.support.appcompat.R$styleable;

/* loaded from: classes.dex */
public class COUIPercentWidthConstraintLayout extends ConstraintLayout {
    public int B;
    private int C;
    private int D;
    private boolean E;
    private int F;
    private int G;
    private int H;
    private int I;
    private boolean J;
    private boolean K;
    private int L;

    public COUIPercentWidthConstraintLayout(Context context) {
        this(context, null);
    }

    private void G(AttributeSet attributeSet) {
        if (getContext() != null) {
            TypedArray obtainStyledAttributes = getContext().obtainStyledAttributes(attributeSet, R$styleable.COUIPercentWidthConstraintLayout);
            int i10 = R$styleable.COUIPercentWidthConstraintLayout_gridNumber;
            int i11 = R$integer.grid_guide_column_preference;
            this.D = obtainStyledAttributes.getResourceId(i10, i11);
            this.C = obtainStyledAttributes.getInteger(i10, getContext().getResources().getInteger(i11));
            this.H = obtainStyledAttributes.getInteger(R$styleable.COUIPercentWidthConstraintLayout_paddingType, 0);
            this.I = obtainStyledAttributes.getInteger(R$styleable.COUIPercentWidthConstraintLayout_paddingSize, 0);
            this.E = obtainStyledAttributes.getBoolean(R$styleable.COUIPercentWidthConstraintLayout_percentIndentEnabled, true);
            this.B = obtainStyledAttributes.getInt(R$styleable.COUIPercentWidthConstraintLayout_percentMode, 0);
            this.J = obtainStyledAttributes.getBoolean(R$styleable.COUIPercentWidthConstraintLayout_isParentChildHierarchy, false);
            this.F = getPaddingStart();
            this.G = getPaddingEnd();
            obtainStyledAttributes.recycle();
        }
    }

    private void H() {
        Context context = getContext();
        if (context != null) {
            this.K = COUIResponsiveUtils.f(getContext());
            if (context instanceof Activity) {
                this.L = COUIResponsiveUtils.e((Activity) context);
            } else {
                this.L = -1;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.constraintlayout.widget.ConstraintLayout
    /* renamed from: D, reason: merged with bridge method [inline-methods] */
    public LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(-1, -1);
    }

    @Override // androidx.constraintlayout.widget.ConstraintLayout
    /* renamed from: E, reason: merged with bridge method [inline-methods] */
    public LayoutParams generateLayoutParams(AttributeSet attributeSet) {
        return new LayoutParams(getContext(), attributeSet);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.constraintlayout.widget.ConstraintLayout, android.view.ViewGroup
    /* renamed from: F, reason: merged with bridge method [inline-methods] */
    public LayoutParams generateLayoutParams(ViewGroup.LayoutParams layoutParams) {
        return new LayoutParams(layoutParams);
    }

    @Override // androidx.constraintlayout.widget.ConstraintLayout, android.view.ViewGroup
    protected boolean checkLayoutParams(ViewGroup.LayoutParams layoutParams) {
        return layoutParams instanceof LayoutParams;
    }

    @Override // android.view.View
    protected void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        if (getContext() != null && this.D != 0) {
            this.C = getContext().getResources().getInteger(this.D);
            H();
        }
        requestLayout();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.constraintlayout.widget.ConstraintLayout, android.view.View
    public void onMeasure(int i10, int i11) {
        int i12;
        if (this.E) {
            i12 = COUIResponsiveUtils.l(this, i10, this.C, this.H, this.I, this.B, this.F, this.G, this.L, this.J, this.K);
            for (int i13 = 0; i13 < getChildCount(); i13++) {
                LayoutParams layoutParams = (LayoutParams) getChildAt(i13).getLayoutParams();
                COUIResponsiveUtils.k(getContext(), getChildAt(i13), i12, this.H, this.I, layoutParams.f6106p0, layoutParams.f6107q0);
            }
        } else {
            i12 = i10;
        }
        super.onMeasure(i12, i11);
    }

    public void setIsParentChildHierarchy(boolean z10) {
        this.J = z10;
        requestLayout();
    }

    public void setPercentIndentEnabled(boolean z10) {
        this.E = z10;
        requestLayout();
    }

    /* loaded from: classes.dex */
    public static class LayoutParams extends ConstraintLayout.LayoutParams {

        /* renamed from: p0, reason: collision with root package name */
        public int f6106p0;

        /* renamed from: q0, reason: collision with root package name */
        public int f6107q0;

        public LayoutParams(Context context, AttributeSet attributeSet) {
            super(context, attributeSet);
            d(context, attributeSet);
        }

        private void d(Context context, AttributeSet attributeSet) {
            TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.COUIPercentWidthConstraintLayout_Layout);
            this.f6106p0 = obtainStyledAttributes.getInt(R$styleable.COUIPercentWidthConstraintLayout_Layout_layout_gridNumber, 0);
            this.f6107q0 = obtainStyledAttributes.getInt(R$styleable.COUIPercentWidthConstraintLayout_Layout_layout_percentMode, 0);
            obtainStyledAttributes.recycle();
        }

        public LayoutParams(int i10, int i11) {
            super(i10, i11);
        }

        public LayoutParams(ViewGroup.LayoutParams layoutParams) {
            super(layoutParams);
        }
    }

    public COUIPercentWidthConstraintLayout(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public COUIPercentWidthConstraintLayout(Context context, AttributeSet attributeSet, int i10) {
        super(context, attributeSet, i10);
        this.B = 0;
        this.E = true;
        this.K = false;
        this.L = 0;
        G(attributeSet);
        H();
    }
}
