package androidx.recyclerview.widget;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.coui.appcompat.grid.COUIPercentWidthRecyclerView;
import com.coui.component.responsiveui.ResponsiveUIModel;
import com.coui.component.responsiveui.layoutgrid.MarginType;
import com.support.appcompat.R$styleable;
import java.util.Arrays;

/* loaded from: classes.dex */
public class COUIGridRecyclerView extends COUIPercentWidthRecyclerView {
    private float B0;
    private float C0;
    private float D0;
    private float E0;
    private float F0;
    private float G0;
    private float H0;
    private int I0;
    private int J0;
    private int K0;
    private int L0;
    private int M0;
    private boolean N0;

    /* loaded from: classes.dex */
    public class COUIGridLayoutManager extends GridLayoutManager {
        public COUIGridLayoutManager(Context context) {
            super(context, 1);
        }

        private float g3() {
            if (COUIGridRecyclerView.this.G0 != 0.0f) {
                return COUIGridRecyclerView.this.G0;
            }
            if (COUIGridRecyclerView.this.F0 == 0.0f) {
                return 0.0f;
            }
            return (COUIGridRecyclerView.this.F0 / COUIGridRecyclerView.this.E0) * COUIGridRecyclerView.this.H0;
        }

        private void h3() {
            MarginType marginType = COUIGridRecyclerView.this.K0 == 1 ? MarginType.MARGIN_SMALL : MarginType.MARGIN_LARGE;
            ResponsiveUIModel chooseMargin = new ResponsiveUIModel(COUIGridRecyclerView.this.getContext(), COUIGridRecyclerView.this.getMeasuredWidth(), 0).chooseMargin(marginType);
            chooseMargin.chooseMargin(marginType);
            COUIGridRecyclerView.this.H0 = chooseMargin.width(0, r0.J0 - 1);
            COUIGridRecyclerView.this.B0 = chooseMargin.gutter();
            COUIGridRecyclerView.this.M0 = chooseMargin.margin();
            COUIGridRecyclerView.this.I0 = chooseMargin.columnCount() / COUIGridRecyclerView.this.J0;
            Log.d("COUIGridRecyclerView", "mChildWidth = " + COUIGridRecyclerView.this.H0 + " mHorizontalGap = " + COUIGridRecyclerView.this.B0 + " mColumn = " + COUIGridRecyclerView.this.I0 + " mGridPadding = " + COUIGridRecyclerView.this.M0 + " getWidthWithoutPadding() = " + k3());
        }

        private void i3() {
            COUIGridRecyclerView.this.I0 = Math.max(1, (int) ((k3() + COUIGridRecyclerView.this.B0) / (COUIGridRecyclerView.this.B0 + COUIGridRecyclerView.this.E0)));
            COUIGridRecyclerView.this.H0 = (k3() - (COUIGridRecyclerView.this.B0 * (COUIGridRecyclerView.this.I0 - 1))) / COUIGridRecyclerView.this.I0;
            COUIGridRecyclerView.this.G0 = g3();
        }

        private void j3() {
            COUIGridRecyclerView.this.I0 = Math.max(1, (int) ((k3() + COUIGridRecyclerView.this.C0) / (COUIGridRecyclerView.this.C0 + COUIGridRecyclerView.this.H0)));
            COUIGridRecyclerView.this.B0 = (k3() - (COUIGridRecyclerView.this.H0 * COUIGridRecyclerView.this.I0)) / (COUIGridRecyclerView.this.I0 - 1);
        }

        private int k3() {
            return (q0() - h0()) - e0();
        }

        @Override // androidx.recyclerview.widget.GridLayoutManager, androidx.recyclerview.widget.LinearLayoutManager, androidx.recyclerview.widget.RecyclerView.p
        public void Z0(RecyclerView.v vVar, RecyclerView.z zVar) {
            int i10 = COUIGridRecyclerView.this.L0;
            if (i10 == 0) {
                h3();
            } else if (i10 == 1) {
                i3();
            } else if (i10 == 2) {
                j3();
            }
            if (COUIGridRecyclerView.this.I0 > 0 && this.J != COUIGridRecyclerView.this.I0) {
                e3(COUIGridRecyclerView.this.I0);
            }
            super.Z0(vVar, zVar);
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @Override // androidx.recyclerview.widget.GridLayoutManager, androidx.recyclerview.widget.LinearLayoutManager
        public View j2(RecyclerView.v vVar, RecyclerView.z zVar, boolean z10, boolean z11) {
            return super.j2(vVar, zVar, z10, z11);
        }

        @Override // androidx.recyclerview.widget.GridLayoutManager, androidx.recyclerview.widget.LinearLayoutManager
        void s2(RecyclerView.v vVar, RecyclerView.z zVar, LinearLayoutManager.c cVar, LinearLayoutManager.b bVar) {
            int f10;
            int i10;
            int i11;
            int i12;
            boolean z10;
            View d10;
            int h02 = h0() + COUIGridRecyclerView.this.M0;
            View[] viewArr = this.L;
            if (viewArr == null || viewArr.length != COUIGridRecyclerView.this.I0) {
                this.L = new View[COUIGridRecyclerView.this.I0];
            }
            int i13 = 0;
            int i14 = 0;
            while (i14 < COUIGridRecyclerView.this.I0 && cVar.c(zVar) && (d10 = cVar.d(vVar)) != null) {
                this.L[i14] = d10;
                i14++;
            }
            if (i14 == 0) {
                bVar.f3437b = true;
                return;
            }
            boolean z11 = cVar.f3444e == 1;
            float f11 = 0.0f;
            int i15 = 0;
            int i16 = 0;
            float f12 = 0.0f;
            while (i15 < COUIGridRecyclerView.this.I0) {
                View view = this.L[i15];
                if (view != null) {
                    if (cVar.f3451l == null) {
                        if (z11) {
                            d(view);
                        } else {
                            e(view, i13);
                        }
                    } else if (z11) {
                        b(view);
                    } else {
                        c(view, i13);
                    }
                    j(view, this.P);
                    GridLayoutManager.LayoutParams layoutParams = (GridLayoutManager.LayoutParams) view.getLayoutParams();
                    Rect rect = layoutParams.f3454b;
                    int i17 = rect.top + rect.bottom + (COUIGridRecyclerView.this.N0 ? i13 : ((ViewGroup.MarginLayoutParams) layoutParams).topMargin + ((ViewGroup.MarginLayoutParams) layoutParams).bottomMargin);
                    int i18 = rect.left + rect.right + (COUIGridRecyclerView.this.N0 ? i13 : ((ViewGroup.MarginLayoutParams) layoutParams).leftMargin + ((ViewGroup.MarginLayoutParams) layoutParams).rightMargin);
                    if (COUIGridRecyclerView.this.G0 == f11) {
                        COUIGridRecyclerView.this.G0 = ((ViewGroup.MarginLayoutParams) layoutParams).height;
                    }
                    float round = Math.round(f12 + COUIGridRecyclerView.this.H0);
                    float f13 = COUIGridRecyclerView.this.H0 - round;
                    z10 = z11;
                    int K = RecyclerView.p.K((int) (round + rect.left + rect.right), this.f3422u.m(), i18, ((ViewGroup.MarginLayoutParams) layoutParams).width, false);
                    view.measure(K, RecyclerView.p.K(this.f3422u.o(), X(), i17, (int) COUIGridRecyclerView.this.G0, true));
                    int e10 = this.f3422u.e(view);
                    Log.d("COUIGridRecyclerView", "childWidthSpec = " + View.MeasureSpec.getSize(K) + " horizontalInsets = " + i18 + " lp.leftMargin = " + ((ViewGroup.MarginLayoutParams) layoutParams).leftMargin + "  lp.rightMargin = " + ((ViewGroup.MarginLayoutParams) layoutParams).rightMargin + " decorInsets = " + rect.left + " - " + rect.right + " mCurrentPosition = " + cVar.f3443d + " x = " + h02);
                    if (e10 > i16) {
                        i16 = e10;
                    }
                    f12 = f13;
                } else {
                    z10 = z11;
                }
                i15++;
                z11 = z10;
                i13 = 0;
                f11 = 0.0f;
            }
            bVar.f3436a = i16;
            int i19 = h02;
            float f14 = 0.0f;
            float f15 = 0.0f;
            for (int i20 = 0; i20 < COUIGridRecyclerView.this.I0; i20++) {
                View view2 = this.L[i20];
                if (view2 != null) {
                    GridLayoutManager.LayoutParams layoutParams2 = (GridLayoutManager.LayoutParams) view2.getLayoutParams();
                    if (q2()) {
                        int q02 = q0() - i19;
                        f10 = q02;
                        i10 = q02 - this.f3422u.f(view2);
                    } else {
                        f10 = this.f3422u.f(view2) + i19;
                        i10 = i19;
                    }
                    if (cVar.f3445f == -1) {
                        int i21 = cVar.f3441b;
                        i12 = i21;
                        i11 = i21 - bVar.f3436a;
                    } else {
                        int i22 = cVar.f3441b;
                        i11 = i22;
                        i12 = bVar.f3436a + i22;
                    }
                    B0(view2, i10, i11, f10, i12);
                    int round2 = Math.round(f14 + COUIGridRecyclerView.this.H0);
                    float f16 = COUIGridRecyclerView.this.H0 - round2;
                    int round3 = Math.round(f15 + COUIGridRecyclerView.this.B0);
                    float f17 = COUIGridRecyclerView.this.B0 - round3;
                    i19 = i19 + round3 + round2;
                    if (layoutParams2.c() || layoutParams2.b()) {
                        bVar.f3438c = true;
                    }
                    bVar.f3439d |= view2.hasFocusable();
                    f14 = f16;
                    f15 = f17;
                }
            }
            Arrays.fill(this.L, (Object) null);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class a extends RecyclerView.o {
        public a() {
        }

        @Override // androidx.recyclerview.widget.RecyclerView.o
        public void e(Rect rect, View view, RecyclerView recyclerView, RecyclerView.z zVar) {
            super.e(rect, view, recyclerView, zVar);
            if (recyclerView.getChildAdapterPosition(view) % COUIGridRecyclerView.this.I0 != COUIGridRecyclerView.this.I0 - 1) {
                float childAdapterPosition = (recyclerView.getChildAdapterPosition(view) % COUIGridRecyclerView.this.I0) + 1.0f;
                int round = Math.round(Math.round(COUIGridRecyclerView.this.B0 + ((COUIGridRecyclerView.this.B0 * childAdapterPosition) - Math.round(COUIGridRecyclerView.this.B0 * childAdapterPosition))));
                if (COUIGridRecyclerView.this.e0()) {
                    rect.left = round;
                } else {
                    rect.right = round;
                }
                Log.d("COUIGridRecyclerView", "   mHorizontalGap = " + COUIGridRecyclerView.this.B0 + " horizontalGap = " + round + " getChildAdapterPosition = " + recyclerView.getChildAdapterPosition(view) + " outRect = " + rect);
            }
            if (recyclerView.getChildAdapterPosition(view) < COUIGridRecyclerView.this.I0 * (((int) Math.ceil(recyclerView.mAdapter.getItemCount() / COUIGridRecyclerView.this.I0)) - 1)) {
                rect.bottom = (int) COUIGridRecyclerView.this.D0;
            }
        }
    }

    public COUIGridRecyclerView(Context context) {
        super(context);
        this.N0 = true;
    }

    private void c0(AttributeSet attributeSet, int i10) {
        if (getContext() != null) {
            TypedArray obtainStyledAttributes = getContext().obtainStyledAttributes(attributeSet, R$styleable.COUIGridRecyclerView, i10, 0);
            this.B0 = obtainStyledAttributes.getDimension(R$styleable.COUIGridRecyclerView_couiHorizontalGap, 0.0f);
            this.C0 = obtainStyledAttributes.getDimension(R$styleable.COUIGridRecyclerView_minHorizontalGap, 0.0f);
            this.D0 = obtainStyledAttributes.getDimension(R$styleable.COUIGridRecyclerView_couiVerticalGap, 0.0f);
            this.E0 = obtainStyledAttributes.getDimension(R$styleable.COUIGridRecyclerView_childMinWidth, 0.0f);
            this.F0 = obtainStyledAttributes.getDimension(R$styleable.COUIGridRecyclerView_childMinHeight, 0.0f);
            this.G0 = obtainStyledAttributes.getDimension(R$styleable.COUIGridRecyclerView_childHeight, 0.0f);
            this.H0 = obtainStyledAttributes.getDimension(R$styleable.COUIGridRecyclerView_childWidth, 0.0f);
            this.J0 = obtainStyledAttributes.getInteger(R$styleable.COUIGridRecyclerView_childGridNumber, 0);
            this.K0 = obtainStyledAttributes.getInteger(R$styleable.COUIGridRecyclerView_gridMarginType, 1);
            this.L0 = obtainStyledAttributes.getInteger(R$styleable.COUIGridRecyclerView_specificType, -1);
            obtainStyledAttributes.recycle();
        }
    }

    private void d0() {
        setLayoutManager(new COUIGridLayoutManager(getContext()));
        addItemDecoration(new a());
        setPercentIndentEnabled(false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean e0() {
        return getLayoutDirection() == 1;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.coui.appcompat.grid.COUIPercentWidthRecyclerView, android.view.View
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        invalidateItemDecorations();
    }

    public void setChildGridNumber(int i10) {
        this.J0 = i10;
        requestLayout();
    }

    public void setChildHeight(float f10) {
        this.G0 = f10;
        requestLayout();
    }

    public void setChildMinHeight(float f10) {
        this.F0 = f10;
        requestLayout();
    }

    public void setChildMinWidth(float f10) {
        this.E0 = f10;
        requestLayout();
    }

    public void setChildWidth(float f10) {
        this.H0 = f10;
        requestLayout();
    }

    public void setGridMarginType(int i10) {
        this.K0 = i10;
        requestLayout();
    }

    public void setHorizontalGap(float f10) {
        this.B0 = f10;
        requestLayout();
    }

    public void setIsIgnoreChildMargin(boolean z10) {
        this.N0 = z10;
    }

    public void setMinHorizontalGap(float f10) {
        this.C0 = f10;
        requestLayout();
    }

    public void setType(int i10) {
        this.L0 = i10;
        requestLayout();
    }

    public void setVerticalGap(float f10) {
        this.D0 = f10;
        requestLayout();
    }

    public COUIGridRecyclerView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.N0 = true;
        c0(attributeSet, 0);
        d0();
    }

    public COUIGridRecyclerView(Context context, AttributeSet attributeSet, int i10) {
        super(context, attributeSet, i10);
        this.N0 = true;
        c0(attributeSet, i10);
        d0();
    }
}
