package androidx.constraintlayout.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.SparseArray;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import m.ConstraintWidget;
import m.ConstraintWidgetContainer;
import m.HelperWidget;

/* loaded from: classes.dex */
public class Barrier extends ConstraintHelper {

    /* renamed from: m, reason: collision with root package name */
    private int f1813m;

    /* renamed from: n, reason: collision with root package name */
    private int f1814n;

    /* renamed from: o, reason: collision with root package name */
    private m.a f1815o;

    public Barrier(Context context) {
        super(context);
        super.setVisibility(8);
    }

    private void w(ConstraintWidget constraintWidget, int i10, boolean z10) {
        this.f1814n = i10;
        if (z10) {
            int i11 = this.f1813m;
            if (i11 == 5) {
                this.f1814n = 1;
            } else if (i11 == 6) {
                this.f1814n = 0;
            }
        } else {
            int i12 = this.f1813m;
            if (i12 == 5) {
                this.f1814n = 0;
            } else if (i12 == 6) {
                this.f1814n = 1;
            }
        }
        if (constraintWidget instanceof m.a) {
            ((m.a) constraintWidget).Q0(this.f1814n);
        }
    }

    public int getMargin() {
        return this.f1815o.N0();
    }

    public int getType() {
        return this.f1813m;
    }

    @Override // androidx.constraintlayout.widget.ConstraintHelper
    protected void m(AttributeSet attributeSet) {
        super.m(attributeSet);
        this.f1815o = new m.a();
        if (attributeSet != null) {
            TypedArray obtainStyledAttributes = getContext().obtainStyledAttributes(attributeSet, R$styleable.ConstraintLayout_Layout);
            int indexCount = obtainStyledAttributes.getIndexCount();
            for (int i10 = 0; i10 < indexCount; i10++) {
                int index = obtainStyledAttributes.getIndex(i10);
                if (index == R$styleable.ConstraintLayout_Layout_barrierDirection) {
                    setType(obtainStyledAttributes.getInt(index, 0));
                } else if (index == R$styleable.ConstraintLayout_Layout_barrierAllowsGoneWidgets) {
                    this.f1815o.P0(obtainStyledAttributes.getBoolean(index, true));
                } else if (index == R$styleable.ConstraintLayout_Layout_barrierMargin) {
                    this.f1815o.R0(obtainStyledAttributes.getDimensionPixelSize(index, 0));
                }
            }
        }
        this.f1819h = this.f1815o;
        u();
    }

    @Override // androidx.constraintlayout.widget.ConstraintHelper
    public void n(ConstraintSet.a aVar, HelperWidget helperWidget, ConstraintLayout.LayoutParams layoutParams, SparseArray<ConstraintWidget> sparseArray) {
        super.n(aVar, helperWidget, layoutParams, sparseArray);
        if (helperWidget instanceof m.a) {
            m.a aVar2 = (m.a) helperWidget;
            w(aVar2, aVar.f1955d.f1962b0, ((ConstraintWidgetContainer) helperWidget.H()).c1());
            aVar2.P0(aVar.f1955d.f1978j0);
            aVar2.R0(aVar.f1955d.f1964c0);
        }
    }

    @Override // androidx.constraintlayout.widget.ConstraintHelper
    public void o(ConstraintWidget constraintWidget, boolean z10) {
        w(constraintWidget, this.f1813m, z10);
    }

    public void setAllowsGoneWidget(boolean z10) {
        this.f1815o.P0(z10);
    }

    public void setDpMargin(int i10) {
        this.f1815o.R0((int) ((i10 * getResources().getDisplayMetrics().density) + 0.5f));
    }

    public void setMargin(int i10) {
        this.f1815o.R0(i10);
    }

    public void setType(int i10) {
        this.f1813m = i10;
    }

    public boolean v() {
        return this.f1815o.L0();
    }

    public Barrier(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        super.setVisibility(8);
    }

    public Barrier(Context context, AttributeSet attributeSet, int i10) {
        super(context, attributeSet, i10);
        super.setVisibility(8);
    }
}
