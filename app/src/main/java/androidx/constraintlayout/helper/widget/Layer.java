package androidx.constraintlayout.helper.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import androidx.constraintlayout.widget.ConstraintHelper;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.R$styleable;
import m.ConstraintWidget;

/* loaded from: classes.dex */
public class Layer extends ConstraintHelper {
    private float A;
    private float B;
    private boolean C;
    private boolean D;

    /* renamed from: m, reason: collision with root package name */
    private float f1379m;

    /* renamed from: n, reason: collision with root package name */
    private float f1380n;

    /* renamed from: o, reason: collision with root package name */
    private float f1381o;

    /* renamed from: p, reason: collision with root package name */
    ConstraintLayout f1382p;

    /* renamed from: q, reason: collision with root package name */
    private float f1383q;

    /* renamed from: r, reason: collision with root package name */
    private float f1384r;

    /* renamed from: s, reason: collision with root package name */
    protected float f1385s;

    /* renamed from: t, reason: collision with root package name */
    protected float f1386t;

    /* renamed from: u, reason: collision with root package name */
    protected float f1387u;

    /* renamed from: v, reason: collision with root package name */
    protected float f1388v;

    /* renamed from: w, reason: collision with root package name */
    protected float f1389w;

    /* renamed from: x, reason: collision with root package name */
    protected float f1390x;

    /* renamed from: y, reason: collision with root package name */
    boolean f1391y;

    /* renamed from: z, reason: collision with root package name */
    View[] f1392z;

    public Layer(Context context) {
        super(context);
        this.f1379m = Float.NaN;
        this.f1380n = Float.NaN;
        this.f1381o = Float.NaN;
        this.f1383q = 1.0f;
        this.f1384r = 1.0f;
        this.f1385s = Float.NaN;
        this.f1386t = Float.NaN;
        this.f1387u = Float.NaN;
        this.f1388v = Float.NaN;
        this.f1389w = Float.NaN;
        this.f1390x = Float.NaN;
        this.f1391y = true;
        this.f1392z = null;
        this.A = 0.0f;
        this.B = 0.0f;
    }

    private void w() {
        int i10;
        if (this.f1382p == null || (i10 = this.f1817f) == 0) {
            return;
        }
        View[] viewArr = this.f1392z;
        if (viewArr == null || viewArr.length != i10) {
            this.f1392z = new View[i10];
        }
        for (int i11 = 0; i11 < this.f1817f; i11++) {
            this.f1392z[i11] = this.f1382p.r(this.f1816e[i11]);
        }
    }

    private void x() {
        if (this.f1382p == null) {
            return;
        }
        if (this.f1392z == null) {
            w();
        }
        v();
        double radians = Math.toRadians(this.f1381o);
        float sin = (float) Math.sin(radians);
        float cos = (float) Math.cos(radians);
        float f10 = this.f1383q;
        float f11 = f10 * cos;
        float f12 = this.f1384r;
        float f13 = (-f12) * sin;
        float f14 = f10 * sin;
        float f15 = f12 * cos;
        for (int i10 = 0; i10 < this.f1817f; i10++) {
            View view = this.f1392z[i10];
            int left = (view.getLeft() + view.getRight()) / 2;
            int top = (view.getTop() + view.getBottom()) / 2;
            float f16 = left - this.f1385s;
            float f17 = top - this.f1386t;
            float f18 = (((f11 * f16) + (f13 * f17)) - f16) + this.A;
            float f19 = (((f16 * f14) + (f15 * f17)) - f17) + this.B;
            view.setTranslationX(f18);
            view.setTranslationY(f19);
            view.setScaleY(this.f1384r);
            view.setScaleX(this.f1383q);
            view.setRotation(this.f1381o);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.constraintlayout.widget.ConstraintHelper
    public void m(AttributeSet attributeSet) {
        super.m(attributeSet);
        this.f1820i = false;
        if (attributeSet != null) {
            TypedArray obtainStyledAttributes = getContext().obtainStyledAttributes(attributeSet, R$styleable.ConstraintLayout_Layout);
            int indexCount = obtainStyledAttributes.getIndexCount();
            for (int i10 = 0; i10 < indexCount; i10++) {
                int index = obtainStyledAttributes.getIndex(i10);
                if (index == R$styleable.ConstraintLayout_Layout_android_visibility) {
                    this.C = true;
                } else if (index == R$styleable.ConstraintLayout_Layout_android_elevation) {
                    this.D = true;
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.constraintlayout.widget.ConstraintHelper, android.view.View
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.f1382p = (ConstraintLayout) getParent();
        if (this.C || this.D) {
            int visibility = getVisibility();
            float elevation = getElevation();
            for (int i10 = 0; i10 < this.f1817f; i10++) {
                View r10 = this.f1382p.r(this.f1816e[i10]);
                if (r10 != null) {
                    if (this.C) {
                        r10.setVisibility(visibility);
                    }
                    if (this.D && elevation > 0.0f) {
                        r10.setTranslationZ(r10.getTranslationZ() + elevation);
                    }
                }
            }
        }
    }

    @Override // androidx.constraintlayout.widget.ConstraintHelper
    public void p(ConstraintLayout constraintLayout) {
        w();
        this.f1385s = Float.NaN;
        this.f1386t = Float.NaN;
        ConstraintWidget b10 = ((ConstraintLayout.LayoutParams) getLayoutParams()).b();
        b10.F0(0);
        b10.i0(0);
        v();
        layout(((int) this.f1389w) - getPaddingLeft(), ((int) this.f1390x) - getPaddingTop(), ((int) this.f1387u) + getPaddingRight(), ((int) this.f1388v) + getPaddingBottom());
        if (Float.isNaN(this.f1381o)) {
            return;
        }
        x();
    }

    @Override // androidx.constraintlayout.widget.ConstraintHelper
    public void r(ConstraintLayout constraintLayout) {
        this.f1382p = constraintLayout;
        float rotation = getRotation();
        if (rotation == 0.0f) {
            if (Float.isNaN(this.f1381o)) {
                return;
            }
            this.f1381o = rotation;
            return;
        }
        this.f1381o = rotation;
    }

    @Override // android.view.View
    public void setElevation(float f10) {
        super.setElevation(f10);
        g();
    }

    @Override // android.view.View
    public void setPivotX(float f10) {
        this.f1379m = f10;
        x();
    }

    @Override // android.view.View
    public void setPivotY(float f10) {
        this.f1380n = f10;
        x();
    }

    @Override // android.view.View
    public void setRotation(float f10) {
        this.f1381o = f10;
        x();
    }

    @Override // android.view.View
    public void setScaleX(float f10) {
        this.f1383q = f10;
        x();
    }

    @Override // android.view.View
    public void setScaleY(float f10) {
        this.f1384r = f10;
        x();
    }

    @Override // android.view.View
    public void setTranslationX(float f10) {
        this.A = f10;
        x();
    }

    @Override // android.view.View
    public void setTranslationY(float f10) {
        this.B = f10;
        x();
    }

    @Override // android.view.View
    public void setVisibility(int i10) {
        super.setVisibility(i10);
        g();
    }

    protected void v() {
        if (this.f1382p == null) {
            return;
        }
        if (this.f1391y || Float.isNaN(this.f1385s) || Float.isNaN(this.f1386t)) {
            if (!Float.isNaN(this.f1379m) && !Float.isNaN(this.f1380n)) {
                this.f1386t = this.f1380n;
                this.f1385s = this.f1379m;
                return;
            }
            View[] l10 = l(this.f1382p);
            int left = l10[0].getLeft();
            int top = l10[0].getTop();
            int right = l10[0].getRight();
            int bottom = l10[0].getBottom();
            for (int i10 = 0; i10 < this.f1817f; i10++) {
                View view = l10[i10];
                left = Math.min(left, view.getLeft());
                top = Math.min(top, view.getTop());
                right = Math.max(right, view.getRight());
                bottom = Math.max(bottom, view.getBottom());
            }
            this.f1387u = right;
            this.f1388v = bottom;
            this.f1389w = left;
            this.f1390x = top;
            if (Float.isNaN(this.f1379m)) {
                this.f1385s = (left + right) / 2;
            } else {
                this.f1385s = this.f1379m;
            }
            if (Float.isNaN(this.f1380n)) {
                this.f1386t = (top + bottom) / 2;
            } else {
                this.f1386t = this.f1380n;
            }
        }
    }

    public Layer(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.f1379m = Float.NaN;
        this.f1380n = Float.NaN;
        this.f1381o = Float.NaN;
        this.f1383q = 1.0f;
        this.f1384r = 1.0f;
        this.f1385s = Float.NaN;
        this.f1386t = Float.NaN;
        this.f1387u = Float.NaN;
        this.f1388v = Float.NaN;
        this.f1389w = Float.NaN;
        this.f1390x = Float.NaN;
        this.f1391y = true;
        this.f1392z = null;
        this.A = 0.0f;
        this.B = 0.0f;
    }

    public Layer(Context context, AttributeSet attributeSet, int i10) {
        super(context, attributeSet, i10);
        this.f1379m = Float.NaN;
        this.f1380n = Float.NaN;
        this.f1381o = Float.NaN;
        this.f1383q = 1.0f;
        this.f1384r = 1.0f;
        this.f1385s = Float.NaN;
        this.f1386t = Float.NaN;
        this.f1387u = Float.NaN;
        this.f1388v = Float.NaN;
        this.f1389w = Float.NaN;
        this.f1390x = Float.NaN;
        this.f1391y = true;
        this.f1392z = null;
        this.A = 0.0f;
        this.B = 0.0f;
    }
}
