package androidx.constraintlayout.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ViewGroup;
import androidx.constraintlayout.widget.ConstraintLayout;

/* loaded from: classes.dex */
public class Constraints extends ViewGroup {

    /* renamed from: e, reason: collision with root package name */
    ConstraintSet f1897e;

    public Constraints(Context context) {
        super(context);
        super.setVisibility(8);
    }

    private void c(AttributeSet attributeSet) {
        Log.v("Constraints", " ################# init");
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.view.ViewGroup
    /* renamed from: a, reason: merged with bridge method [inline-methods] */
    public LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(-2, -2);
    }

    @Override // android.view.ViewGroup
    /* renamed from: b, reason: merged with bridge method [inline-methods] */
    public LayoutParams generateLayoutParams(AttributeSet attributeSet) {
        return new LayoutParams(getContext(), attributeSet);
    }

    public ConstraintSet getConstraintSet() {
        if (this.f1897e == null) {
            this.f1897e = new ConstraintSet();
        }
        this.f1897e.k(this);
        return this.f1897e;
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onLayout(boolean z10, int i10, int i11, int i12, int i13) {
    }

    @Override // android.view.ViewGroup
    protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams layoutParams) {
        return new ConstraintLayout.LayoutParams(layoutParams);
    }

    public Constraints(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        c(attributeSet);
        super.setVisibility(8);
    }

    public Constraints(Context context, AttributeSet attributeSet, int i10) {
        super(context, attributeSet, i10);
        c(attributeSet);
        super.setVisibility(8);
    }

    /* loaded from: classes.dex */
    public static class LayoutParams extends ConstraintLayout.LayoutParams {
        public float A0;
        public float B0;

        /* renamed from: p0, reason: collision with root package name */
        public float f1898p0;

        /* renamed from: q0, reason: collision with root package name */
        public boolean f1899q0;

        /* renamed from: r0, reason: collision with root package name */
        public float f1900r0;

        /* renamed from: s0, reason: collision with root package name */
        public float f1901s0;

        /* renamed from: t0, reason: collision with root package name */
        public float f1902t0;

        /* renamed from: u0, reason: collision with root package name */
        public float f1903u0;

        /* renamed from: v0, reason: collision with root package name */
        public float f1904v0;

        /* renamed from: w0, reason: collision with root package name */
        public float f1905w0;

        /* renamed from: x0, reason: collision with root package name */
        public float f1906x0;

        /* renamed from: y0, reason: collision with root package name */
        public float f1907y0;

        /* renamed from: z0, reason: collision with root package name */
        public float f1908z0;

        public LayoutParams(int i10, int i11) {
            super(i10, i11);
            this.f1898p0 = 1.0f;
            this.f1899q0 = false;
            this.f1900r0 = 0.0f;
            this.f1901s0 = 0.0f;
            this.f1902t0 = 0.0f;
            this.f1903u0 = 0.0f;
            this.f1904v0 = 1.0f;
            this.f1905w0 = 1.0f;
            this.f1906x0 = 0.0f;
            this.f1907y0 = 0.0f;
            this.f1908z0 = 0.0f;
            this.A0 = 0.0f;
            this.B0 = 0.0f;
        }

        public LayoutParams(Context context, AttributeSet attributeSet) {
            super(context, attributeSet);
            this.f1898p0 = 1.0f;
            this.f1899q0 = false;
            this.f1900r0 = 0.0f;
            this.f1901s0 = 0.0f;
            this.f1902t0 = 0.0f;
            this.f1903u0 = 0.0f;
            this.f1904v0 = 1.0f;
            this.f1905w0 = 1.0f;
            this.f1906x0 = 0.0f;
            this.f1907y0 = 0.0f;
            this.f1908z0 = 0.0f;
            this.A0 = 0.0f;
            this.B0 = 0.0f;
            TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.ConstraintSet);
            int indexCount = obtainStyledAttributes.getIndexCount();
            for (int i10 = 0; i10 < indexCount; i10++) {
                int index = obtainStyledAttributes.getIndex(i10);
                if (index == R$styleable.ConstraintSet_android_alpha) {
                    this.f1898p0 = obtainStyledAttributes.getFloat(index, this.f1898p0);
                } else if (index == R$styleable.ConstraintSet_android_elevation) {
                    this.f1900r0 = obtainStyledAttributes.getFloat(index, this.f1900r0);
                    this.f1899q0 = true;
                } else if (index == R$styleable.ConstraintSet_android_rotationX) {
                    this.f1902t0 = obtainStyledAttributes.getFloat(index, this.f1902t0);
                } else if (index == R$styleable.ConstraintSet_android_rotationY) {
                    this.f1903u0 = obtainStyledAttributes.getFloat(index, this.f1903u0);
                } else if (index == R$styleable.ConstraintSet_android_rotation) {
                    this.f1901s0 = obtainStyledAttributes.getFloat(index, this.f1901s0);
                } else if (index == R$styleable.ConstraintSet_android_scaleX) {
                    this.f1904v0 = obtainStyledAttributes.getFloat(index, this.f1904v0);
                } else if (index == R$styleable.ConstraintSet_android_scaleY) {
                    this.f1905w0 = obtainStyledAttributes.getFloat(index, this.f1905w0);
                } else if (index == R$styleable.ConstraintSet_android_transformPivotX) {
                    this.f1906x0 = obtainStyledAttributes.getFloat(index, this.f1906x0);
                } else if (index == R$styleable.ConstraintSet_android_transformPivotY) {
                    this.f1907y0 = obtainStyledAttributes.getFloat(index, this.f1907y0);
                } else if (index == R$styleable.ConstraintSet_android_translationX) {
                    this.f1908z0 = obtainStyledAttributes.getFloat(index, this.f1908z0);
                } else if (index == R$styleable.ConstraintSet_android_translationY) {
                    this.A0 = obtainStyledAttributes.getFloat(index, this.A0);
                } else if (index == R$styleable.ConstraintSet_android_translationZ) {
                    this.B0 = obtainStyledAttributes.getFloat(index, this.B0);
                }
            }
        }
    }
}
