package com.coui.appcompat.grid;

import android.content.Context;
import android.content.res.Configuration;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.FragmentContainerView;
import com.coui.component.responsiveui.unit.Dp;
import com.coui.component.responsiveui.window.WindowSizeClass;
import com.coui.component.responsiveui.window.WindowWidthSizeClass;
import com.support.appcompat.R$attr;
import com.support.appcompat.R$dimen;
import v1.COUIContextUtil;
import w1.COUIDarkModeUtil;

/* loaded from: classes.dex */
public class COUIListDetailView extends FrameLayout {

    /* renamed from: e, reason: collision with root package name */
    private FragmentContainerView f6097e;

    /* renamed from: f, reason: collision with root package name */
    private FragmentContainerView f6098f;

    /* renamed from: g, reason: collision with root package name */
    private FragmentContainerView f6099g;

    /* renamed from: h, reason: collision with root package name */
    private View f6100h;

    /* renamed from: i, reason: collision with root package name */
    private int f6101i;

    /* renamed from: j, reason: collision with root package name */
    private int f6102j;

    /* renamed from: k, reason: collision with root package name */
    private int f6103k;

    /* renamed from: l, reason: collision with root package name */
    private int f6104l;

    /* renamed from: m, reason: collision with root package name */
    private float f6105m;

    public COUIListDetailView(Context context) {
        this(context, null);
    }

    private void a(Context context) {
        this.f6097e = new FragmentContainerView(context);
        this.f6098f = new FragmentContainerView(context);
        this.f6099g = new FragmentContainerView(context);
        this.f6100h = new View(context);
        addView(this.f6099g);
        addView(this.f6097e);
        addView(this.f6100h);
        addView(this.f6098f);
        this.f6099g.setId(FrameLayout.generateViewId());
        this.f6097e.setId(FrameLayout.generateViewId());
        this.f6098f.setId(FrameLayout.generateViewId());
        int a10 = COUIContextUtil.a(getContext(), R$attr.couiColorDivider);
        this.f6101i = a10;
        setDividerColor(a10);
        COUIDarkModeUtil.b(this.f6100h, false);
        this.f6102j = context.getResources().getDimensionPixelSize(R$dimen.coui_main_fragment_max_width);
        this.f6103k = context.getResources().getDimensionPixelSize(R$dimen.coui_main_fragment_min_width);
        this.f6104l = context.getResources().getDimensionPixelSize(R$dimen.coui_fragment_gap_width);
    }

    public FrameLayout getEmptyPageFragmentContainer() {
        return this.f6099g;
    }

    public FrameLayout getMainFragmentContainer() {
        return this.f6097e;
    }

    public FrameLayout getSubFragmentContainer() {
        return this.f6098f;
    }

    @Override // android.view.View
    protected void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        int a10 = COUIContextUtil.a(getContext(), R$attr.couiColorDivider);
        this.f6101i = a10;
        setDividerColor(a10);
    }

    @Override // android.widget.FrameLayout, android.view.ViewGroup, android.view.View
    protected void onLayout(boolean z10, int i10, int i11, int i12, int i13) {
        super.onLayout(z10, i10, i11, i12, i13);
        boolean z11 = ViewCompat.x(this) == 1;
        WindowSizeClass.Companion companion = WindowSizeClass.INSTANCE;
        Dp.Companion companion2 = Dp.INSTANCE;
        WindowWidthSizeClass windowWidthSizeClass = companion.calculateFromSize(companion2.pixel2Dp(getContext(), Math.abs(getWidth())), companion2.pixel2Dp(getContext(), Math.abs(getWidth()))).getWindowWidthSizeClass();
        if (z11) {
            if (windowWidthSizeClass == WindowWidthSizeClass.Compact) {
                this.f6099g.setVisibility(8);
                FragmentContainerView fragmentContainerView = this.f6097e;
                fragmentContainerView.layout(0, 0, fragmentContainerView.getWidth(), this.f6097e.getHeight());
                FragmentContainerView fragmentContainerView2 = this.f6098f;
                fragmentContainerView2.layout(0, 0, fragmentContainerView2.getWidth(), this.f6098f.getHeight());
                return;
            }
            this.f6099g.setVisibility(0);
            this.f6099g.layout(0, 0, this.f6098f.getWidth(), this.f6098f.getHeight());
            FragmentContainerView fragmentContainerView3 = this.f6098f;
            fragmentContainerView3.layout(0, 0, fragmentContainerView3.getWidth(), this.f6098f.getHeight());
            this.f6100h.layout(this.f6098f.getWidth(), 0, this.f6098f.getWidth() + this.f6100h.getWidth(), this.f6100h.getHeight());
            this.f6097e.layout(this.f6098f.getWidth() + this.f6100h.getWidth(), 0, this.f6098f.getWidth() + this.f6100h.getWidth() + this.f6097e.getWidth(), this.f6097e.getHeight());
            return;
        }
        if (windowWidthSizeClass == WindowWidthSizeClass.Compact) {
            this.f6099g.setVisibility(8);
            FragmentContainerView fragmentContainerView4 = this.f6097e;
            fragmentContainerView4.layout(0, 0, fragmentContainerView4.getWidth(), this.f6097e.getHeight());
            FragmentContainerView fragmentContainerView5 = this.f6098f;
            fragmentContainerView5.layout(0, 0, fragmentContainerView5.getWidth(), this.f6098f.getHeight());
            return;
        }
        this.f6099g.setVisibility(0);
        this.f6099g.layout(this.f6097e.getWidth() + this.f6100h.getWidth(), 0, this.f6097e.getWidth() + this.f6100h.getWidth() + this.f6098f.getWidth(), this.f6098f.getHeight());
        FragmentContainerView fragmentContainerView6 = this.f6097e;
        fragmentContainerView6.layout(0, 0, fragmentContainerView6.getWidth(), this.f6097e.getHeight());
        this.f6100h.layout(this.f6097e.getWidth(), 0, this.f6097e.getWidth() + this.f6100h.getWidth(), this.f6100h.getHeight());
        this.f6098f.layout(this.f6097e.getWidth() + this.f6100h.getWidth(), 0, this.f6097e.getWidth() + this.f6100h.getWidth() + this.f6098f.getWidth(), this.f6098f.getHeight());
    }

    @Override // android.widget.FrameLayout, android.view.View
    protected void onMeasure(int i10, int i11) {
        int min;
        int i12;
        int i13;
        super.onMeasure(i10, i11);
        int measuredWidth = getMeasuredWidth();
        WindowSizeClass.Companion companion = WindowSizeClass.INSTANCE;
        Dp.Companion companion2 = Dp.INSTANCE;
        WindowWidthSizeClass windowWidthSizeClass = companion.calculateFromSize(companion2.pixel2Dp(getContext(), Math.abs(measuredWidth)), companion2.pixel2Dp(getContext(), Math.abs(measuredWidth))).getWindowWidthSizeClass();
        int max = (int) Math.max(Math.min(measuredWidth * this.f6105m, this.f6102j), this.f6103k);
        if (windowWidthSizeClass == WindowWidthSizeClass.Compact) {
            min = measuredWidth;
            i12 = min;
            i13 = 0;
        } else {
            min = Math.min(Math.max(max, this.f6103k), this.f6102j);
            i12 = measuredWidth - min;
            i13 = this.f6104l;
        }
        measureChild(this.f6097e, FrameLayout.getChildMeasureSpec(i10, 0, Math.min(measuredWidth, min)), i11);
        int childMeasureSpec = FrameLayout.getChildMeasureSpec(i10, 0, i12);
        measureChild(this.f6098f, childMeasureSpec, i11);
        measureChild(this.f6099g, childMeasureSpec, i11);
        measureChild(this.f6100h, FrameLayout.getChildMeasureSpec(i10, 0, i13), i11);
    }

    public void setDividerColor(int i10) {
        this.f6101i = i10;
        this.f6100h.setBackgroundColor(i10);
    }

    public void setMainFragmentPercent(float f10) {
        this.f6105m = f10;
        requestLayout();
    }

    public COUIListDetailView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public COUIListDetailView(Context context, AttributeSet attributeSet, int i10) {
        super(context, attributeSet, i10);
        this.f6105m = 0.4f;
        a(context);
    }
}
