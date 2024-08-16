package com.coui.appcompat.tablayout;

import android.content.Context;
import android.content.res.Configuration;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import androidx.core.view.ViewCompat;
import com.coui.appcompat.grid.COUIResponsiveUtils;
import com.coui.appcompat.toolbar.COUIToolbar;
import com.coui.component.responsiveui.ResponsiveUIModel;
import com.coui.component.responsiveui.layoutgrid.MarginType;
import com.coui.component.responsiveui.window.WindowWidthSizeClass;
import com.support.bars.R$dimen;

/* loaded from: classes.dex */
public class COUIPercentTabWithSearchView extends FrameLayout {

    /* renamed from: e, reason: collision with root package name */
    private int f7740e;

    /* renamed from: f, reason: collision with root package name */
    private ResponsiveUIModel f7741f;

    /* renamed from: g, reason: collision with root package name */
    private WindowWidthSizeClass f7742g;

    public COUIPercentTabWithSearchView(Context context) {
        this(context, null);
    }

    private void a(Context context) {
        b();
        this.f7741f = new ResponsiveUIModel(context, 0, 0);
    }

    private void b() {
        if (getContext() != null) {
            this.f7740e = getContext().getResources().getDimensionPixelSize(R$dimen.coui_tab_search_horizontal_padding);
        }
    }

    private void c(View view, View view2) {
        int b10;
        int gutter;
        int b11;
        int gutter2;
        int i10;
        boolean z10 = ViewCompat.x(this) == 1;
        WindowWidthSizeClass windowWidthSizeClass = this.f7742g;
        if (windowWidthSizeClass == WindowWidthSizeClass.Compact) {
            if (view != null) {
                i10 = view.getHeight() + 0;
                if (z10) {
                    view.layout(0, 0, view.getWidth(), i10);
                } else {
                    view.layout(this.f7741f.margin(), 0, this.f7741f.margin() + view.getWidth(), i10);
                }
            } else {
                i10 = 0;
            }
            if (view2 != null) {
                view2.layout(0, i10, view2.getWidth(), view2.getHeight() + i10);
                return;
            }
            return;
        }
        if (z10) {
            if (view2 != null) {
                if (windowWidthSizeClass == WindowWidthSizeClass.Medium) {
                    view2.layout(getMeasuredWidth() - view2.getWidth(), (getMeasuredHeight() - view2.getHeight()) / 2, getMeasuredWidth(), view2.getHeight() + ((getMeasuredHeight() - view2.getHeight()) / 2));
                } else {
                    view2.layout((getMeasuredWidth() - view2.getWidth()) - this.f7740e, (getMeasuredHeight() - view2.getHeight()) / 2, getMeasuredWidth() - this.f7740e, view2.getHeight() + ((getMeasuredHeight() - view2.getHeight()) / 2));
                }
            }
            if (this.f7742g == WindowWidthSizeClass.Medium) {
                b11 = ((int) COUIResponsiveUtils.b(getMeasuredWidth(), 4, 0, 0, getContext())) + this.f7741f.margin();
                gutter2 = this.f7741f.gutter();
            } else {
                b11 = ((int) COUIResponsiveUtils.b(getMeasuredWidth(), 8, 0, 0, getContext())) + this.f7741f.margin();
                gutter2 = this.f7741f.gutter();
            }
            int measuredWidth = getMeasuredWidth() - (b11 + gutter2);
            if (view != null) {
                view.layout(measuredWidth - view.getWidth(), 0, measuredWidth, view.getHeight());
                return;
            }
            return;
        }
        if (view2 != null) {
            if (windowWidthSizeClass == WindowWidthSizeClass.Medium) {
                view2.layout(0, (getMeasuredHeight() - view2.getHeight()) / 2, view2.getWidth(), view2.getHeight() + ((getMeasuredHeight() - view2.getHeight()) / 2));
            } else {
                view2.layout(this.f7740e, (getMeasuredHeight() - view2.getHeight()) / 2, view2.getWidth() + this.f7740e, view2.getHeight() + ((getMeasuredHeight() - view2.getHeight()) / 2));
            }
        }
        if (this.f7742g == WindowWidthSizeClass.Medium) {
            b10 = ((int) COUIResponsiveUtils.b(getMeasuredWidth(), 4, 0, 0, getContext())) + this.f7741f.margin();
            gutter = this.f7741f.gutter();
        } else {
            b10 = ((int) COUIResponsiveUtils.b(getMeasuredWidth(), 8, 0, 0, getContext())) + this.f7741f.margin();
            gutter = this.f7741f.gutter();
        }
        int i11 = b10 + gutter;
        if (view != null) {
            view.layout(i11, 0, view.getWidth() + i11, view.getHeight());
        }
    }

    private void d(int i10, int i11, View view) {
        int b10;
        int margin;
        WindowWidthSizeClass windowWidthSizeClass = this.f7742g;
        if (windowWidthSizeClass == WindowWidthSizeClass.Compact) {
            margin = (int) COUIResponsiveUtils.b(getMeasuredWidth(), 4, 1, 0, getContext());
        } else {
            if (windowWidthSizeClass == WindowWidthSizeClass.Medium) {
                b10 = (int) COUIResponsiveUtils.b(getMeasuredWidth(), 4, 0, 0, getContext());
            } else {
                b10 = windowWidthSizeClass == WindowWidthSizeClass.Expanded ? ((int) COUIResponsiveUtils.b(getMeasuredWidth(), 8, 0, 0, getContext())) - this.f7740e : 0;
            }
            margin = b10 + this.f7741f.margin();
        }
        measureChild(view, FrameLayout.getChildMeasureSpec(i10, 0, Math.min(getMeasuredWidth(), margin)), FrameLayout.getChildMeasureSpec(i11, 0, getMeasuredHeight()));
    }

    private void e(int i10, int i11, View view) {
        int b10;
        if (this.f7742g == WindowWidthSizeClass.Compact) {
            b10 = ((int) COUIResponsiveUtils.b(getMeasuredWidth(), 4, 0, 0, getContext())) + this.f7741f.margin();
        } else {
            b10 = (int) COUIResponsiveUtils.b(getMeasuredWidth(), 4, 0, 0, getContext());
            if (this.f7742g == WindowWidthSizeClass.Expanded) {
                b10 -= this.f7740e;
            }
        }
        measureChild(view, FrameLayout.getChildMeasureSpec(i10, 0, Math.min(getMeasuredWidth(), b10 + this.f7741f.margin())), FrameLayout.getChildMeasureSpec(i11, 0, getMeasuredHeight()));
    }

    private void f() {
        this.f7741f.rebuild(getMeasuredWidth(), 0).chooseMargin(MarginType.MARGIN_SMALL);
        this.f7742g = this.f7741f.windowSizeClass().getWindowWidthSizeClass();
    }

    @Override // android.view.View
    protected void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        this.f7741f.onConfigurationChanged(configuration);
        this.f7741f.chooseMargin(MarginType.MARGIN_SMALL);
    }

    @Override // android.widget.FrameLayout, android.view.ViewGroup, android.view.View
    protected void onLayout(boolean z10, int i10, int i11, int i12, int i13) {
        super.onLayout(z10, i10, i11, i12, i13);
        int childCount = getChildCount();
        View view = null;
        View view2 = null;
        for (int i14 = 0; i14 < childCount && i14 < 2; i14++) {
            View childAt = getChildAt(i14);
            if (childAt instanceof COUIToolbar) {
                view = childAt;
            } else if (childAt instanceof COUITabLayout) {
                view2 = childAt;
            }
        }
        c(view, view2);
    }

    @Override // android.widget.FrameLayout, android.view.View
    protected void onMeasure(int i10, int i11) {
        super.onMeasure(i10, i11);
        f();
        int childCount = getChildCount();
        int i12 = 0;
        int i13 = 0;
        for (int i14 = 0; i14 < childCount && i14 < 2; i14++) {
            View childAt = getChildAt(i14);
            if (childAt instanceof COUIToolbar) {
                e(i10, i11, childAt);
                i12 = childAt.getHeight();
            } else if (childAt instanceof COUITabLayout) {
                d(i10, i11, childAt);
                i13 = childAt.getHeight();
            }
        }
        if (this.f7742g == WindowWidthSizeClass.Compact) {
            setMeasuredDimension(FrameLayout.resolveSizeAndState(View.MeasureSpec.getSize(i10), i10, 0), i12 + i13);
        } else {
            setMeasuredDimension(FrameLayout.resolveSizeAndState(View.MeasureSpec.getSize(i10), i10, 0), Math.max(i13, i12));
        }
    }

    public COUIPercentTabWithSearchView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public COUIPercentTabWithSearchView(Context context, AttributeSet attributeSet, int i10) {
        super(context, attributeSet, i10);
        this.f7742g = WindowWidthSizeClass.Compact;
        a(context);
    }
}
