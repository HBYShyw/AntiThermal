package com.google.android.material.navigationrail;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;
import com.google.android.material.navigation.NavigationBarItemView;
import com.google.android.material.navigation.NavigationBarMenuView;

/* loaded from: classes.dex */
public class NavigationRailMenuView extends NavigationBarMenuView {
    private int I;
    private final FrameLayout.LayoutParams J;

    public NavigationRailMenuView(Context context) {
        super(context);
        this.I = -1;
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(-1, -2);
        this.J = layoutParams;
        layoutParams.gravity = 49;
        setLayoutParams(layoutParams);
        setItemActiveIndicatorResizeable(true);
    }

    private int p(int i10, int i11, int i12) {
        int max = i11 / Math.max(1, i12);
        int i13 = this.I;
        if (i13 == -1) {
            i13 = View.MeasureSpec.getSize(i10);
        }
        return View.MeasureSpec.makeMeasureSpec(Math.min(i13, max), 0);
    }

    private int q(View view, int i10, int i11) {
        if (view.getVisibility() == 8) {
            return 0;
        }
        view.measure(i10, i11);
        return view.getMeasuredHeight();
    }

    private int r(int i10, int i11, int i12, View view) {
        int makeMeasureSpec;
        p(i10, i11, i12);
        if (view == null) {
            makeMeasureSpec = p(i10, i11, i12);
        } else {
            makeMeasureSpec = View.MeasureSpec.makeMeasureSpec(view.getMeasuredHeight(), 0);
        }
        int childCount = getChildCount();
        int i13 = 0;
        for (int i14 = 0; i14 < childCount; i14++) {
            View childAt = getChildAt(i14);
            if (childAt != view) {
                i13 += q(childAt, i10, makeMeasureSpec);
            }
        }
        return i13;
    }

    private int s(int i10, int i11, int i12) {
        int i13;
        View childAt = getChildAt(getSelectedItemPosition());
        if (childAt != null) {
            i13 = q(childAt, i10, p(i10, i11, i12));
            i11 -= i13;
            i12--;
        } else {
            i13 = 0;
        }
        return i13 + r(i10, i11, i12, childAt);
    }

    @Override // com.google.android.material.navigation.NavigationBarMenuView
    protected NavigationBarItemView f(Context context) {
        return new NavigationRailItemView(context);
    }

    public int getItemMinimumHeight() {
        return this.I;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getMenuGravity() {
        return this.J.gravity;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean o() {
        return (this.J.gravity & 112) == 48;
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onLayout(boolean z10, int i10, int i11, int i12, int i13) {
        int childCount = getChildCount();
        int i14 = i12 - i10;
        int i15 = 0;
        for (int i16 = 0; i16 < childCount; i16++) {
            View childAt = getChildAt(i16);
            if (childAt.getVisibility() != 8) {
                int measuredHeight = childAt.getMeasuredHeight() + i15;
                childAt.layout(0, i15, i14, measuredHeight);
                i15 = measuredHeight;
            }
        }
    }

    @Override // android.view.View
    protected void onMeasure(int i10, int i11) {
        int r10;
        int size = View.MeasureSpec.getSize(i11);
        int size2 = getMenu().getVisibleItems().size();
        if (size2 > 1 && h(getLabelVisibilityMode(), size2)) {
            r10 = s(i10, size, size2);
        } else {
            r10 = r(i10, size, size2, null);
        }
        setMeasuredDimension(View.resolveSizeAndState(View.MeasureSpec.getSize(i10), i10, 0), View.resolveSizeAndState(r10, i11, 0));
    }

    public void setItemMinimumHeight(int i10) {
        if (this.I != i10) {
            this.I = i10;
            requestLayout();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setMenuGravity(int i10) {
        FrameLayout.LayoutParams layoutParams = this.J;
        if (layoutParams.gravity != i10) {
            layoutParams.gravity = i10;
            setLayoutParams(layoutParams);
        }
    }
}
