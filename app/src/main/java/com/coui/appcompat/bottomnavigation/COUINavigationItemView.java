package com.coui.appcompat.bottomnavigation;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Rect;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.core.view.ViewCompat;
import com.coui.appcompat.reddot.COUIHintRedDot;
import com.google.android.material.R$id;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.support.bars.R$bool;
import com.support.bars.R$dimen;
import com.support.bars.R$layout;

@SuppressLint({"RestrictedApi"})
/* loaded from: classes.dex */
public class COUINavigationItemView extends BottomNavigationItemView {
    private int J;
    private int K;
    private int L;
    private int M;
    private int N;
    private int O;
    private int P;
    private int Q;
    private int R;
    private TextView S;
    private TextView T;
    private ImageView U;
    private FrameLayout V;
    private FrameLayout W;

    /* renamed from: a0, reason: collision with root package name */
    private COUIHintRedDot f5459a0;

    /* renamed from: b0, reason: collision with root package name */
    private Rect f5460b0;

    /* renamed from: c0, reason: collision with root package name */
    private int f5461c0;

    /* renamed from: d0, reason: collision with root package name */
    private boolean f5462d0;

    /* renamed from: e0, reason: collision with root package name */
    private boolean f5463e0;

    /* renamed from: f0, reason: collision with root package name */
    private boolean f5464f0;

    public COUINavigationItemView(Context context) {
        super(context);
        this.J = getResources().getDimensionPixelSize(R$dimen.coui_navigation_enlarge_icon_size);
        this.K = getResources().getDimensionPixelSize(R$dimen.coui_navigation_normal_icon_size);
        this.L = getResources().getDimensionPixelSize(R$dimen.coui_navigation_enlarge_item_height);
        this.M = getResources().getDimensionPixelSize(R$dimen.coui_navigation_normal_item_height);
        this.N = getResources().getDimensionPixelSize(R$dimen.coui_navigation_enlarge_icon_margin_top);
        this.O = getResources().getDimensionPixelSize(com.google.android.material.R$dimen.m3_badge_vertical_offset);
        this.P = getResources().getDimensionPixelSize(com.google.android.material.R$dimen.mtrl_badge_text_horizontal_edge_offset);
        this.Q = getResources().getDimensionPixelSize(com.google.android.material.R$dimen.mtrl_badge_horizontal_edge_offset);
        this.f5462d0 = false;
        this.f5463e0 = false;
        this.f5464f0 = false;
        this.S = (TextView) findViewById(R$id.navigation_bar_item_small_label_view);
        this.T = (TextView) findViewById(R$id.navigation_bar_item_large_label_view);
        this.U = (ImageView) findViewById(R$id.navigation_bar_item_icon_view);
        this.V = (FrameLayout) findViewById(R$id.navigation_bar_item_icon_container);
        this.W = (FrameLayout) findViewById(com.support.bars.R$id.fl_root);
        this.f5459a0 = (COUIHintRedDot) findViewById(com.support.bars.R$id.red_dot);
        setTextSize(context.getResources().getDimensionPixelSize(R$dimen.coui_navigation_item_text_size));
        setClipChildren(false);
        setClipToPadding(false);
        setHapticFeedbackEnabled(false);
    }

    private void A() {
        int i10;
        int i11;
        int i12;
        int i13;
        if (this.f5459a0.getVisibility() == 8) {
            return;
        }
        if (this.f5460b0 == null) {
            this.f5460b0 = new Rect();
        }
        if (ViewCompat.x(this) == 1) {
            this.f5460b0.set(this.V.getLeft() - this.f5459a0.getMeasuredWidth(), this.V.getTop() - this.f5459a0.getMeasuredHeight(), this.V.getLeft(), this.V.getTop());
            if (this.f5459a0.getPointMode() == 1) {
                i12 = this.Q;
            } else {
                i12 = this.P;
            }
            if (this.f5459a0.getPointMode() == 1) {
                i13 = this.Q;
            } else {
                i13 = this.P + this.O;
            }
            this.f5460b0.offset(i12, i13);
        } else {
            this.f5460b0.set(this.V.getRight(), this.V.getTop() - this.f5459a0.getMeasuredHeight(), this.V.getRight() + this.f5459a0.getMeasuredWidth(), this.V.getTop());
            if (this.f5459a0.getPointMode() == 1) {
                i10 = this.Q;
            } else {
                i10 = this.P;
            }
            int i14 = -i10;
            if (this.f5459a0.getPointMode() == 1) {
                i11 = this.Q;
            } else {
                i11 = this.P + this.O;
            }
            this.f5460b0.offset(i14, i11);
        }
        COUIHintRedDot cOUIHintRedDot = this.f5459a0;
        Rect rect = this.f5460b0;
        cOUIHintRedDot.layout(rect.left, rect.top, rect.right, rect.bottom);
    }

    private void B() {
        int measuredWidth;
        int i10;
        View childAt = this.W.getChildAt(0);
        View childAt2 = this.W.getChildAt(1);
        int dimensionPixelSize = getResources().getDimensionPixelSize(R$dimen.coui_navigation_enlarge_icon_horizontal_margin);
        int measuredWidth2 = ((this.W.getMeasuredWidth() - childAt.getMeasuredWidth()) - childAt2.getMeasuredWidth()) - dimensionPixelSize;
        int i11 = measuredWidth2 > 0 ? measuredWidth2 / 2 : 0;
        if (this.f5463e0) {
            i10 = (this.W.getMeasuredWidth() / 2) - (childAt.getMeasuredWidth() / 2);
            measuredWidth = (this.W.getMeasuredWidth() / 2) + (childAt.getMeasuredWidth() / 2);
        } else {
            measuredWidth = i11 + childAt.getMeasuredWidth();
            i10 = i11;
        }
        childAt.layout(i10, (this.W.getMeasuredHeight() / 2) - (childAt.getMeasuredHeight() / 2), measuredWidth, (this.W.getMeasuredHeight() / 2) + (childAt.getMeasuredHeight() / 2));
        int right = childAt.getRight() + dimensionPixelSize;
        int measuredWidth3 = this.W.getMeasuredWidth() - i11;
        int measuredHeight = (this.W.getMeasuredHeight() / 2) - (childAt2.getMeasuredHeight() / 2);
        int measuredHeight2 = (this.W.getMeasuredHeight() / 2) + (childAt2.getMeasuredHeight() / 2);
        childAt2.measure(View.MeasureSpec.makeMeasureSpec(measuredWidth3 - right, 1073741824), View.MeasureSpec.makeMeasureSpec(childAt2.getMeasuredHeight(), 1073741824));
        childAt2.layout(right, measuredHeight, measuredWidth3, measuredHeight2);
        if (this.f5463e0) {
            childAt2.setVisibility(8);
        }
        this.f5464f0 = true;
    }

    private void C() {
        View childAt = this.W.getChildAt(0);
        View childAt2 = this.W.getChildAt(1);
        int dimensionPixelSize = getResources().getDimensionPixelSize(R$dimen.coui_navigation_enlarge_icon_horizontal_margin);
        int measuredWidth = ((this.W.getMeasuredWidth() - childAt.getMeasuredWidth()) - childAt2.getMeasuredWidth()) - dimensionPixelSize;
        int i10 = measuredWidth > 0 ? measuredWidth / 2 : 0;
        if (this.f5463e0) {
            childAt.layout((this.W.getMeasuredWidth() / 2) - (childAt.getMeasuredWidth() / 2), (this.W.getMeasuredHeight() / 2) - (childAt.getMeasuredHeight() / 2), (this.W.getMeasuredWidth() / 2) + (childAt.getMeasuredWidth() / 2), (this.W.getMeasuredHeight() / 2) + (childAt.getMeasuredHeight() / 2));
        } else {
            int measuredWidth2 = this.W.getMeasuredWidth() - i10;
            childAt.layout(measuredWidth2 - childAt.getMeasuredWidth(), (this.W.getMeasuredHeight() / 2) - (childAt.getMeasuredHeight() / 2), measuredWidth2, (this.W.getMeasuredHeight() / 2) + (childAt.getMeasuredHeight() / 2));
        }
        int left = childAt.getLeft() - dimensionPixelSize;
        int measuredHeight = (this.W.getMeasuredHeight() / 2) - (childAt2.getMeasuredHeight() / 2);
        int measuredHeight2 = (this.W.getMeasuredHeight() / 2) + (childAt2.getMeasuredHeight() / 2);
        childAt2.measure(View.MeasureSpec.makeMeasureSpec(left - i10, 1073741824), View.MeasureSpec.makeMeasureSpec(childAt2.getMeasuredHeight(), 1073741824));
        childAt2.layout(i10, measuredHeight, left, measuredHeight2);
        if (this.f5463e0) {
            childAt2.setVisibility(8);
        }
        this.f5464f0 = true;
    }

    private void y(boolean z10) {
        if (this.f5463e0) {
            setIconSize(z10 ? this.J : this.K);
            this.S.setVisibility(z10 ? 8 : 0);
            if (this.f5464f0) {
                return;
            }
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) this.V.getLayoutParams();
            layoutParams.setMargins(0, z10 ? 0 : this.N, 0, 0);
            this.V.setLayoutParams(layoutParams);
        }
    }

    private boolean z() {
        return getLayoutDirection() == 1;
    }

    public void D(boolean z10, boolean z11) {
        this.f5462d0 = z10;
        this.f5463e0 = z11;
    }

    public void E() {
        if (this.f5463e0) {
            return;
        }
        this.U.setColorFilter(-1);
    }

    public void F() {
        View childAt = this.W.getChildAt(0);
        View childAt2 = this.W.getChildAt(1);
        int dimensionPixelSize = (this.f5463e0 && isSelected()) ? 0 : getResources().getDimensionPixelSize(R$dimen.coui_navigation_icon_margin_top);
        childAt.layout((this.W.getMeasuredWidth() / 2) - (childAt.getMeasuredWidth() / 2), dimensionPixelSize, (this.W.getMeasuredWidth() / 2) + (childAt.getMeasuredWidth() / 2), childAt.getMeasuredHeight() + dimensionPixelSize);
        int measuredWidth = (this.W.getMeasuredWidth() / 2) - (childAt2.getMeasuredWidth() / 2);
        int measuredWidth2 = (this.W.getMeasuredWidth() / 2) + (childAt2.getMeasuredWidth() / 2);
        int measuredHeight = this.W.getMeasuredHeight() - childAt2.getMeasuredHeight();
        Resources resources = getResources();
        int i10 = R$dimen.coui_navigation_icon_margin_top;
        childAt2.layout(measuredWidth, measuredHeight - resources.getDimensionPixelSize(i10), measuredWidth2, this.W.getMeasuredHeight() - getResources().getDimensionPixelSize(i10));
        if (this.f5463e0) {
            childAt2.setVisibility(0);
        }
        this.f5464f0 = false;
    }

    public COUIHintRedDot getCOUIHintRedDot() {
        return this.f5459a0;
    }

    @Override // com.google.android.material.bottomnavigation.BottomNavigationItemView, com.google.android.material.navigation.NavigationBarItemView
    protected int getItemDefaultMarginResId() {
        return R$dimen.coui_navigation_enlarge_default_margin;
    }

    @Override // com.google.android.material.bottomnavigation.BottomNavigationItemView, com.google.android.material.navigation.NavigationBarItemView
    protected int getItemLayoutResId() {
        return R$layout.coui_navigation_item_layout;
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (this.f5462d0) {
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) this.W.getLayoutParams();
            layoutParams.gravity = 80;
            if (this.f5463e0) {
                layoutParams.height = this.L;
                setIconSize(this.K);
                setIconTintList(null);
            } else {
                layoutParams.height = this.M;
            }
            this.W.setLayoutParams(layoutParams);
            y(isSelected());
        }
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.f5459a0.setPointMode(0);
        this.f5459a0.setPointText("");
        this.f5459a0.setVisibility(4);
    }

    @Override // android.widget.FrameLayout, android.view.ViewGroup, android.view.View
    protected void onLayout(boolean z10, int i10, int i11, int i12, int i13) {
        super.onLayout(z10, i10, i11, i12, i13);
        boolean z11 = getResources().getConfiguration().orientation == 2;
        boolean z12 = getContext().getResources().getBoolean(R$bool.is_normal_layout);
        boolean z13 = getContext().getResources().getBoolean(R$bool.is_big_layout);
        boolean z14 = getContext().getResources().getBoolean(R$bool.is_small_layout);
        if (this.R == 1) {
            F();
        } else if ((z11 || z13) && !z() && !z14) {
            B();
        } else if ((z11 || z13) && z()) {
            C();
        } else if (z12 || z14) {
            F();
        }
        A();
    }

    @Override // com.google.android.material.navigation.NavigationBarItemView
    public void setChecked(boolean z10) {
        y(z10);
        setSelected(z10);
    }

    public void setTextSize(int i10) {
        this.f5461c0 = i10;
        this.S.setTextSize(0, i10);
        this.T.setTextSize(0, this.f5461c0);
        requestLayout();
    }

    public void w(int i10) {
        this.R = i10;
        requestLayout();
    }

    public void x() {
        this.U.clearColorFilter();
    }
}
