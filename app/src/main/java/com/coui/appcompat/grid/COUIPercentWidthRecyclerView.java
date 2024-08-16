package com.coui.appcompat.grid;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import androidx.recyclerview.widget.COUIRecyclerView;
import com.support.appcompat.R$integer;
import com.support.appcompat.R$styleable;

/* loaded from: classes.dex */
public class COUIPercentWidthRecyclerView extends COUIRecyclerView {
    private int A0;

    /* renamed from: r0, reason: collision with root package name */
    private int f6134r0;

    /* renamed from: s0, reason: collision with root package name */
    private int f6135s0;

    /* renamed from: t0, reason: collision with root package name */
    private int f6136t0;

    /* renamed from: u0, reason: collision with root package name */
    private int f6137u0;

    /* renamed from: v0, reason: collision with root package name */
    private int f6138v0;

    /* renamed from: w0, reason: collision with root package name */
    private int f6139w0;

    /* renamed from: x0, reason: collision with root package name */
    private boolean f6140x0;

    /* renamed from: y0, reason: collision with root package name */
    private boolean f6141y0;

    /* renamed from: z0, reason: collision with root package name */
    private boolean f6142z0;

    public COUIPercentWidthRecyclerView(Context context) {
        this(context, null);
    }

    private void H(AttributeSet attributeSet) {
        if (getContext() != null) {
            TypedArray obtainStyledAttributes = getContext().obtainStyledAttributes(attributeSet, R$styleable.COUIPercentWidthRecyclerView);
            int i10 = R$styleable.COUIPercentWidthRecyclerView_couiRecyclerGridNumber;
            int i11 = R$integer.grid_guide_column_preference;
            this.f6135s0 = obtainStyledAttributes.getResourceId(i10, i11);
            this.f6134r0 = obtainStyledAttributes.getInteger(i10, getContext().getResources().getInteger(i11));
            this.f6138v0 = obtainStyledAttributes.getInteger(R$styleable.COUIPercentWidthRecyclerView_paddingType, 1);
            this.f6139w0 = obtainStyledAttributes.getInteger(R$styleable.COUIPercentWidthRecyclerView_paddingSize, 0);
            this.f6140x0 = obtainStyledAttributes.getBoolean(R$styleable.COUIPercentWidthRecyclerView_isParentChildHierarchy, false);
            obtainStyledAttributes.recycle();
        }
    }

    private void I() {
        Context context = getContext();
        if (context != null) {
            this.f6142z0 = COUIResponsiveUtils.f(getContext());
            if (context instanceof Activity) {
                this.A0 = COUIResponsiveUtils.e((Activity) context);
            } else {
                this.A0 = -1;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.view.View
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        if (getContext() != null && this.f6135s0 != 0) {
            this.f6134r0 = getContext().getResources().getInteger(this.f6135s0);
            I();
        }
        requestLayout();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.recyclerview.widget.RecyclerView, android.view.View
    public void onMeasure(int i10, int i11) {
        if (this.f6141y0) {
            i10 = COUIResponsiveUtils.l(this, i10, this.f6134r0, this.f6138v0, this.f6139w0, 0, this.f6136t0, this.f6137u0, this.A0, this.f6140x0, this.f6142z0);
        } else if (getPaddingStart() != this.f6136t0 || getPaddingEnd() != this.f6137u0) {
            setPaddingRelative(this.f6136t0, getPaddingTop(), this.f6137u0, getPaddingBottom());
        }
        super.onMeasure(i10, i11);
    }

    public void setIsParentChildHierarchy(boolean z10) {
        this.f6140x0 = z10;
        requestLayout();
    }

    public void setPercentIndentEnabled(boolean z10) {
        this.f6141y0 = z10;
        requestLayout();
    }

    public COUIPercentWidthRecyclerView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public COUIPercentWidthRecyclerView(Context context, AttributeSet attributeSet, int i10) {
        super(context, attributeSet, i10);
        this.f6141y0 = true;
        this.f6142z0 = false;
        this.A0 = 0;
        H(attributeSet);
        this.f6136t0 = getPaddingStart();
        this.f6137u0 = getPaddingEnd();
        setScrollBarStyle(33554432);
        I();
    }
}
