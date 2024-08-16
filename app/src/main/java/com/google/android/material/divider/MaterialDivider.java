package com.google.android.material.divider;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import c4.MaterialShapeDrawable;
import com.google.android.material.R$attr;
import com.google.android.material.R$dimen;
import com.google.android.material.R$style;
import com.google.android.material.R$styleable;
import com.google.android.material.internal.ThemeEnforcement;
import d4.MaterialThemeOverlay;
import z3.MaterialResources;

/* loaded from: classes.dex */
public class MaterialDivider extends View {

    /* renamed from: j, reason: collision with root package name */
    private static final int f8787j = R$style.Widget_MaterialComponents_MaterialDivider;

    /* renamed from: e, reason: collision with root package name */
    private final MaterialShapeDrawable f8788e;

    /* renamed from: f, reason: collision with root package name */
    private int f8789f;

    /* renamed from: g, reason: collision with root package name */
    private int f8790g;

    /* renamed from: h, reason: collision with root package name */
    private int f8791h;

    /* renamed from: i, reason: collision with root package name */
    private int f8792i;

    public MaterialDivider(Context context) {
        this(context, null);
    }

    public int getDividerColor() {
        return this.f8790g;
    }

    public int getDividerInsetEnd() {
        return this.f8792i;
    }

    public int getDividerInsetStart() {
        return this.f8791h;
    }

    public int getDividerThickness() {
        return this.f8789f;
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        int width;
        int i10;
        super.onDraw(canvas);
        boolean z10 = ViewCompat.x(this) == 1;
        int i11 = z10 ? this.f8792i : this.f8791h;
        if (z10) {
            width = getWidth();
            i10 = this.f8791h;
        } else {
            width = getWidth();
            i10 = this.f8792i;
        }
        this.f8788e.setBounds(i11, 0, width - i10, getBottom() - getTop());
        this.f8788e.draw(canvas);
    }

    @Override // android.view.View
    protected void onMeasure(int i10, int i11) {
        super.onMeasure(i10, i11);
        int mode = View.MeasureSpec.getMode(i11);
        int measuredHeight = getMeasuredHeight();
        if (mode == Integer.MIN_VALUE || mode == 0) {
            int i12 = this.f8789f;
            if (i12 > 0 && measuredHeight != i12) {
                measuredHeight = i12;
            }
            setMeasuredDimension(getMeasuredWidth(), measuredHeight);
        }
    }

    public void setDividerColor(int i10) {
        if (this.f8790g != i10) {
            this.f8790g = i10;
            this.f8788e.a0(ColorStateList.valueOf(i10));
            invalidate();
        }
    }

    public void setDividerColorResource(int i10) {
        setDividerColor(ContextCompat.c(getContext(), i10));
    }

    public void setDividerInsetEnd(int i10) {
        this.f8792i = i10;
    }

    public void setDividerInsetEndResource(int i10) {
        setDividerInsetEnd(getContext().getResources().getDimensionPixelOffset(i10));
    }

    public void setDividerInsetStart(int i10) {
        this.f8791h = i10;
    }

    public void setDividerInsetStartResource(int i10) {
        setDividerInsetStart(getContext().getResources().getDimensionPixelOffset(i10));
    }

    public void setDividerThickness(int i10) {
        if (this.f8789f != i10) {
            this.f8789f = i10;
            requestLayout();
        }
    }

    public void setDividerThicknessResource(int i10) {
        setDividerThickness(getContext().getResources().getDimensionPixelSize(i10));
    }

    public MaterialDivider(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, R$attr.materialDividerStyle);
    }

    /* JADX WARN: Illegal instructions before constructor call */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public MaterialDivider(Context context, AttributeSet attributeSet, int i10) {
        super(MaterialThemeOverlay.c(context, attributeSet, i10, r4), attributeSet, i10);
        int i11 = f8787j;
        Context context2 = getContext();
        this.f8788e = new MaterialShapeDrawable();
        TypedArray obtainStyledAttributes = ThemeEnforcement.obtainStyledAttributes(context2, attributeSet, R$styleable.MaterialDivider, i10, i11, new int[0]);
        this.f8789f = obtainStyledAttributes.getDimensionPixelSize(R$styleable.MaterialDivider_dividerThickness, getResources().getDimensionPixelSize(R$dimen.material_divider_thickness));
        this.f8791h = obtainStyledAttributes.getDimensionPixelOffset(R$styleable.MaterialDivider_dividerInsetStart, 0);
        this.f8792i = obtainStyledAttributes.getDimensionPixelOffset(R$styleable.MaterialDivider_dividerInsetEnd, 0);
        setDividerColor(MaterialResources.a(context2, obtainStyledAttributes, R$styleable.MaterialDivider_dividerColor).getDefaultColor());
        obtainStyledAttributes.recycle();
    }
}
