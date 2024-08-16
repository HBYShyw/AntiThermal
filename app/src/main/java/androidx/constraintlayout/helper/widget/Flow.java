package androidx.constraintlayout.helper.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.View;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.constraintlayout.widget.R$styleable;
import androidx.constraintlayout.widget.VirtualLayout;
import m.ConstraintWidget;
import m.HelperWidget;
import m.g;
import m.l;

/* loaded from: classes.dex */
public class Flow extends VirtualLayout {

    /* renamed from: o, reason: collision with root package name */
    private g f1378o;

    public Flow(Context context) {
        super(context);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.constraintlayout.widget.VirtualLayout, androidx.constraintlayout.widget.ConstraintHelper
    public void m(AttributeSet attributeSet) {
        super.m(attributeSet);
        this.f1378o = new g();
        if (attributeSet != null) {
            TypedArray obtainStyledAttributes = getContext().obtainStyledAttributes(attributeSet, R$styleable.ConstraintLayout_Layout);
            int indexCount = obtainStyledAttributes.getIndexCount();
            for (int i10 = 0; i10 < indexCount; i10++) {
                int index = obtainStyledAttributes.getIndex(i10);
                if (index == R$styleable.ConstraintLayout_Layout_android_orientation) {
                    this.f1378o.T1(obtainStyledAttributes.getInt(index, 0));
                } else if (index == R$styleable.ConstraintLayout_Layout_android_padding) {
                    this.f1378o.Z0(obtainStyledAttributes.getDimensionPixelSize(index, 0));
                } else if (index == R$styleable.ConstraintLayout_Layout_android_paddingStart) {
                    this.f1378o.e1(obtainStyledAttributes.getDimensionPixelSize(index, 0));
                } else if (index == R$styleable.ConstraintLayout_Layout_android_paddingEnd) {
                    this.f1378o.b1(obtainStyledAttributes.getDimensionPixelSize(index, 0));
                } else if (index == R$styleable.ConstraintLayout_Layout_android_paddingLeft) {
                    this.f1378o.c1(obtainStyledAttributes.getDimensionPixelSize(index, 0));
                } else if (index == R$styleable.ConstraintLayout_Layout_android_paddingTop) {
                    this.f1378o.f1(obtainStyledAttributes.getDimensionPixelSize(index, 0));
                } else if (index == R$styleable.ConstraintLayout_Layout_android_paddingRight) {
                    this.f1378o.d1(obtainStyledAttributes.getDimensionPixelSize(index, 0));
                } else if (index == R$styleable.ConstraintLayout_Layout_android_paddingBottom) {
                    this.f1378o.a1(obtainStyledAttributes.getDimensionPixelSize(index, 0));
                } else if (index == R$styleable.ConstraintLayout_Layout_flow_wrapMode) {
                    this.f1378o.Y1(obtainStyledAttributes.getInt(index, 0));
                } else if (index == R$styleable.ConstraintLayout_Layout_flow_horizontalStyle) {
                    this.f1378o.N1(obtainStyledAttributes.getInt(index, 0));
                } else if (index == R$styleable.ConstraintLayout_Layout_flow_verticalStyle) {
                    this.f1378o.X1(obtainStyledAttributes.getInt(index, 0));
                } else if (index == R$styleable.ConstraintLayout_Layout_flow_firstHorizontalStyle) {
                    this.f1378o.H1(obtainStyledAttributes.getInt(index, 0));
                } else if (index == R$styleable.ConstraintLayout_Layout_flow_lastHorizontalStyle) {
                    this.f1378o.P1(obtainStyledAttributes.getInt(index, 0));
                } else if (index == R$styleable.ConstraintLayout_Layout_flow_firstVerticalStyle) {
                    this.f1378o.J1(obtainStyledAttributes.getInt(index, 0));
                } else if (index == R$styleable.ConstraintLayout_Layout_flow_lastVerticalStyle) {
                    this.f1378o.R1(obtainStyledAttributes.getInt(index, 0));
                } else if (index == R$styleable.ConstraintLayout_Layout_flow_horizontalBias) {
                    this.f1378o.L1(obtainStyledAttributes.getFloat(index, 0.5f));
                } else if (index == R$styleable.ConstraintLayout_Layout_flow_firstHorizontalBias) {
                    this.f1378o.G1(obtainStyledAttributes.getFloat(index, 0.5f));
                } else if (index == R$styleable.ConstraintLayout_Layout_flow_lastHorizontalBias) {
                    this.f1378o.O1(obtainStyledAttributes.getFloat(index, 0.5f));
                } else if (index == R$styleable.ConstraintLayout_Layout_flow_firstVerticalBias) {
                    this.f1378o.I1(obtainStyledAttributes.getFloat(index, 0.5f));
                } else if (index == R$styleable.ConstraintLayout_Layout_flow_lastVerticalBias) {
                    this.f1378o.Q1(obtainStyledAttributes.getFloat(index, 0.5f));
                } else if (index == R$styleable.ConstraintLayout_Layout_flow_verticalBias) {
                    this.f1378o.V1(obtainStyledAttributes.getFloat(index, 0.5f));
                } else if (index == R$styleable.ConstraintLayout_Layout_flow_horizontalAlign) {
                    this.f1378o.K1(obtainStyledAttributes.getInt(index, 2));
                } else if (index == R$styleable.ConstraintLayout_Layout_flow_verticalAlign) {
                    this.f1378o.U1(obtainStyledAttributes.getInt(index, 2));
                } else if (index == R$styleable.ConstraintLayout_Layout_flow_horizontalGap) {
                    this.f1378o.M1(obtainStyledAttributes.getDimensionPixelSize(index, 0));
                } else if (index == R$styleable.ConstraintLayout_Layout_flow_verticalGap) {
                    this.f1378o.W1(obtainStyledAttributes.getDimensionPixelSize(index, 0));
                } else if (index == R$styleable.ConstraintLayout_Layout_flow_maxElementsWrap) {
                    this.f1378o.S1(obtainStyledAttributes.getInt(index, -1));
                }
            }
        }
        this.f1819h = this.f1378o;
        u();
    }

    @Override // androidx.constraintlayout.widget.ConstraintHelper
    public void n(ConstraintSet.a aVar, HelperWidget helperWidget, ConstraintLayout.LayoutParams layoutParams, SparseArray<ConstraintWidget> sparseArray) {
        super.n(aVar, helperWidget, layoutParams, sparseArray);
        if (helperWidget instanceof g) {
            g gVar = (g) helperWidget;
            int i10 = layoutParams.S;
            if (i10 != -1) {
                gVar.T1(i10);
            }
        }
    }

    @Override // androidx.constraintlayout.widget.ConstraintHelper
    public void o(ConstraintWidget constraintWidget, boolean z10) {
        this.f1378o.L0(z10);
    }

    @Override // androidx.constraintlayout.widget.ConstraintHelper, android.view.View
    @SuppressLint({"WrongCall"})
    protected void onMeasure(int i10, int i11) {
        v(this.f1378o, i10, i11);
    }

    public void setFirstHorizontalBias(float f10) {
        this.f1378o.G1(f10);
        requestLayout();
    }

    public void setFirstHorizontalStyle(int i10) {
        this.f1378o.H1(i10);
        requestLayout();
    }

    public void setFirstVerticalBias(float f10) {
        this.f1378o.I1(f10);
        requestLayout();
    }

    public void setFirstVerticalStyle(int i10) {
        this.f1378o.J1(i10);
        requestLayout();
    }

    public void setHorizontalAlign(int i10) {
        this.f1378o.K1(i10);
        requestLayout();
    }

    public void setHorizontalBias(float f10) {
        this.f1378o.L1(f10);
        requestLayout();
    }

    public void setHorizontalGap(int i10) {
        this.f1378o.M1(i10);
        requestLayout();
    }

    public void setHorizontalStyle(int i10) {
        this.f1378o.N1(i10);
        requestLayout();
    }

    public void setMaxElementsWrap(int i10) {
        this.f1378o.S1(i10);
        requestLayout();
    }

    public void setOrientation(int i10) {
        this.f1378o.T1(i10);
        requestLayout();
    }

    public void setPadding(int i10) {
        this.f1378o.Z0(i10);
        requestLayout();
    }

    public void setPaddingBottom(int i10) {
        this.f1378o.a1(i10);
        requestLayout();
    }

    public void setPaddingLeft(int i10) {
        this.f1378o.c1(i10);
        requestLayout();
    }

    public void setPaddingRight(int i10) {
        this.f1378o.d1(i10);
        requestLayout();
    }

    public void setPaddingTop(int i10) {
        this.f1378o.f1(i10);
        requestLayout();
    }

    public void setVerticalAlign(int i10) {
        this.f1378o.U1(i10);
        requestLayout();
    }

    public void setVerticalBias(float f10) {
        this.f1378o.V1(f10);
        requestLayout();
    }

    public void setVerticalGap(int i10) {
        this.f1378o.W1(i10);
        requestLayout();
    }

    public void setVerticalStyle(int i10) {
        this.f1378o.X1(i10);
        requestLayout();
    }

    public void setWrapMode(int i10) {
        this.f1378o.Y1(i10);
        requestLayout();
    }

    @Override // androidx.constraintlayout.widget.VirtualLayout
    public void v(l lVar, int i10, int i11) {
        int mode = View.MeasureSpec.getMode(i10);
        int size = View.MeasureSpec.getSize(i10);
        int mode2 = View.MeasureSpec.getMode(i11);
        int size2 = View.MeasureSpec.getSize(i11);
        if (lVar != null) {
            lVar.T0(mode, size, mode2, size2);
            setMeasuredDimension(lVar.O0(), lVar.N0());
        } else {
            setMeasuredDimension(0, 0);
        }
    }

    public Flow(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public Flow(Context context, AttributeSet attributeSet, int i10) {
        super(context, attributeSet, i10);
    }
}
