package com.google.android.material.bottomnavigation;

import android.content.Context;
import android.content.res.Resources;
import android.view.View;
import android.widget.FrameLayout;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.core.view.ViewCompat;
import com.google.android.material.R$dimen;
import com.google.android.material.navigation.NavigationBarItemView;
import com.google.android.material.navigation.NavigationBarMenuView;

/* loaded from: classes.dex */
public class BottomNavigationMenuView extends NavigationBarMenuView {
    private final int I;
    private final int J;
    private final int K;
    private final int L;
    private boolean M;
    private int[] N;

    public BottomNavigationMenuView(Context context) {
        super(context);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(-2, -2);
        layoutParams.gravity = 17;
        setLayoutParams(layoutParams);
        Resources resources = getResources();
        this.I = resources.getDimensionPixelSize(R$dimen.design_bottom_navigation_item_max_width);
        this.J = resources.getDimensionPixelSize(R$dimen.design_bottom_navigation_item_min_width);
        this.K = resources.getDimensionPixelSize(R$dimen.design_bottom_navigation_active_item_max_width);
        this.L = resources.getDimensionPixelSize(R$dimen.design_bottom_navigation_active_item_min_width);
        this.N = new int[5];
    }

    @Override // com.google.android.material.navigation.NavigationBarMenuView
    protected NavigationBarItemView f(Context context) {
        return new BottomNavigationItemView(context);
    }

    public boolean o() {
        return this.M;
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onLayout(boolean z10, int i10, int i11, int i12, int i13) {
        int childCount = getChildCount();
        int i14 = i12 - i10;
        int i15 = i13 - i11;
        int i16 = 0;
        for (int i17 = 0; i17 < childCount; i17++) {
            View childAt = getChildAt(i17);
            if (childAt.getVisibility() != 8) {
                if (ViewCompat.x(this) == 1) {
                    int i18 = i14 - i16;
                    childAt.layout(i18 - childAt.getMeasuredWidth(), 0, i18, i15);
                } else {
                    childAt.layout(i16, 0, childAt.getMeasuredWidth() + i16, i15);
                }
                i16 += childAt.getMeasuredWidth();
            }
        }
    }

    @Override // android.view.View
    protected void onMeasure(int i10, int i11) {
        MenuBuilder menu = getMenu();
        int size = View.MeasureSpec.getSize(i10);
        int size2 = menu.getVisibleItems().size();
        int childCount = getChildCount();
        int size3 = View.MeasureSpec.getSize(i11);
        int makeMeasureSpec = View.MeasureSpec.makeMeasureSpec(size3, 1073741824);
        if (h(getLabelVisibilityMode(), size2) && o()) {
            View childAt = getChildAt(getSelectedItemPosition());
            int i12 = this.L;
            if (childAt.getVisibility() != 8) {
                childAt.measure(View.MeasureSpec.makeMeasureSpec(this.K, Integer.MIN_VALUE), makeMeasureSpec);
                i12 = Math.max(i12, childAt.getMeasuredWidth());
            }
            int i13 = size2 - (childAt.getVisibility() != 8 ? 1 : 0);
            int min = Math.min(size - (this.J * i13), Math.min(i12, this.K));
            int i14 = size - min;
            int min2 = Math.min(i14 / (i13 == 0 ? 1 : i13), this.I);
            int i15 = i14 - (i13 * min2);
            int i16 = 0;
            while (i16 < childCount) {
                if (getChildAt(i16).getVisibility() != 8) {
                    this.N[i16] = i16 == getSelectedItemPosition() ? min : min2;
                    if (i15 > 0) {
                        int[] iArr = this.N;
                        iArr[i16] = iArr[i16] + 1;
                        i15--;
                    }
                } else {
                    this.N[i16] = 0;
                }
                i16++;
            }
        } else {
            int min3 = Math.min(size / (size2 == 0 ? 1 : size2), this.K);
            int i17 = size - (size2 * min3);
            for (int i18 = 0; i18 < childCount; i18++) {
                if (getChildAt(i18).getVisibility() != 8) {
                    int[] iArr2 = this.N;
                    iArr2[i18] = min3;
                    if (i17 > 0) {
                        iArr2[i18] = iArr2[i18] + 1;
                        i17--;
                    }
                } else {
                    this.N[i18] = 0;
                }
            }
        }
        int i19 = 0;
        for (int i20 = 0; i20 < childCount; i20++) {
            View childAt2 = getChildAt(i20);
            if (childAt2.getVisibility() != 8) {
                childAt2.measure(View.MeasureSpec.makeMeasureSpec(this.N[i20], 1073741824), makeMeasureSpec);
                childAt2.getLayoutParams().width = childAt2.getMeasuredWidth();
                i19 += childAt2.getMeasuredWidth();
            }
        }
        setMeasuredDimension(View.resolveSizeAndState(i19, View.MeasureSpec.makeMeasureSpec(i19, 1073741824), 0), View.resolveSizeAndState(size3, i11, 0));
    }

    public void setItemHorizontalTranslationEnabled(boolean z10) {
        this.M = z10;
    }
}
