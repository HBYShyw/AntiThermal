package com.google.android.material.timepicker;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.view.ViewCompat;
import c4.MaterialShapeDrawable;
import c4.RelativeCornerSize;
import com.google.android.material.R$id;
import com.google.android.material.R$layout;
import com.google.android.material.R$styleable;

/* loaded from: classes.dex */
class RadialViewGroup extends ConstraintLayout {
    private final Runnable B;
    private int C;
    private MaterialShapeDrawable D;

    /* loaded from: classes.dex */
    class a implements Runnable {
        a() {
        }

        @Override // java.lang.Runnable
        public void run() {
            RadialViewGroup.this.H();
        }
    }

    public RadialViewGroup(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    private Drawable D() {
        MaterialShapeDrawable materialShapeDrawable = new MaterialShapeDrawable();
        this.D = materialShapeDrawable;
        materialShapeDrawable.Y(new RelativeCornerSize(0.5f));
        this.D.a0(ColorStateList.valueOf(-1));
        return this.D;
    }

    private static boolean G(View view) {
        return "skip".equals(view.getTag());
    }

    private void I() {
        Handler handler = getHandler();
        if (handler != null) {
            handler.removeCallbacks(this.B);
            handler.post(this.B);
        }
    }

    public int E() {
        return this.C;
    }

    public void F(int i10) {
        this.C = i10;
        H();
    }

    protected void H() {
        int childCount = getChildCount();
        int i10 = 1;
        for (int i11 = 0; i11 < childCount; i11++) {
            if (G(getChildAt(i11))) {
                i10++;
            }
        }
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.j(this);
        float f10 = 0.0f;
        for (int i12 = 0; i12 < childCount; i12++) {
            View childAt = getChildAt(i12);
            int id2 = childAt.getId();
            int i13 = R$id.circle_center;
            if (id2 != i13 && !G(childAt)) {
                constraintSet.m(childAt.getId(), i13, this.C, f10);
                f10 += 360.0f / (childCount - i10);
            }
        }
        constraintSet.d(this);
    }

    @Override // androidx.constraintlayout.widget.ConstraintLayout, android.view.ViewGroup
    public void addView(View view, int i10, ViewGroup.LayoutParams layoutParams) {
        super.addView(view, i10, layoutParams);
        if (view.getId() == -1) {
            view.setId(ViewCompat.i());
        }
        I();
    }

    @Override // android.view.View
    protected void onFinishInflate() {
        super.onFinishInflate();
        H();
    }

    @Override // androidx.constraintlayout.widget.ConstraintLayout, android.view.ViewGroup
    public void onViewRemoved(View view) {
        super.onViewRemoved(view);
        I();
    }

    @Override // android.view.View
    public void setBackgroundColor(int i10) {
        this.D.a0(ColorStateList.valueOf(i10));
    }

    public RadialViewGroup(Context context, AttributeSet attributeSet, int i10) {
        super(context, attributeSet, i10);
        LayoutInflater.from(context).inflate(R$layout.material_radial_view_group, this);
        ViewCompat.p0(this, D());
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.RadialViewGroup, i10, 0);
        this.C = obtainStyledAttributes.getDimensionPixelSize(R$styleable.RadialViewGroup_materialCircleRadius, 0);
        this.B = new a();
        obtainStyledAttributes.recycle();
    }
}
