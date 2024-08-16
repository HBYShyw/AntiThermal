package com.oplus.powermanager.fuelgaue.base;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import com.google.android.material.appbar.AppBarLayout;
import com.oplus.battery.R;

/* loaded from: classes.dex */
public class ToolBarBehavior extends CoordinatorLayout.Behavior<AppBarLayout> implements AbsListView.OnScrollListener {
    private View mChild;
    private int mCurrentOffset;
    private View mDivider;
    private int mDividerAlphaChangeEndY;
    private int mDividerAlphaChangeOffset;
    private float mDividerAlphaRange;
    private AppBarLayout.LayoutParams mDividerParams;
    private int mDividerWidthChangeEndY;
    private int mDividerWidthChangeInitY;
    private int mDividerWidthChangeOffset;
    private float mDividerWidthRange;
    private View mFirst;
    private int mListFirstChildInitY;
    private int[] mLocation;
    private int mLocationY;
    private int mMarginLeftRight;
    private int mNewOffset;
    private Resources mResources;
    private View mScrollView;

    public ToolBarBehavior() {
        this.mLocation = new int[2];
    }

    private void init(Context context) {
        Resources resources = context.getResources();
        this.mResources = resources;
        this.mMarginLeftRight = resources.getDimensionPixelOffset(R.dimen.common_margin);
        this.mDividerAlphaChangeOffset = this.mResources.getDimensionPixelOffset(R.dimen.line_alpha_range_change_offset);
        this.mDividerWidthChangeOffset = this.mResources.getDimensionPixelOffset(R.dimen.divider_width_change_offset);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onListScroll() {
        boolean z10;
        this.mChild = null;
        View view = this.mScrollView;
        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            if (viewGroup.getChildCount() > 0) {
                int i10 = 0;
                while (true) {
                    if (i10 >= viewGroup.getChildCount()) {
                        break;
                    }
                    if (viewGroup.getChildAt(i10).getVisibility() == 0) {
                        this.mChild = viewGroup.getChildAt(i10);
                        int i11 = i10 + 1;
                        if (i11 < viewGroup.getChildCount() && viewGroup.getChildAt(i11) == this.mFirst) {
                            z10 = true;
                        }
                    } else {
                        i10++;
                    }
                }
            }
        }
        z10 = false;
        View view2 = this.mFirst;
        if (view2 == null) {
            this.mFirst = this.mChild;
        } else if (view2 != this.mChild && !z10) {
            return;
        }
        if (this.mChild == null) {
            this.mChild = this.mScrollView;
        }
        this.mChild.getLocationOnScreen(this.mLocation);
        int i12 = this.mLocation[1];
        this.mLocationY = i12;
        this.mNewOffset = 0;
        if (i12 < this.mDividerAlphaChangeEndY) {
            this.mNewOffset = this.mDividerAlphaChangeOffset;
        } else {
            int i13 = this.mListFirstChildInitY;
            if (i12 > i13) {
                this.mNewOffset = 0;
            } else {
                this.mNewOffset = i13 - i12;
            }
        }
        this.mCurrentOffset = this.mNewOffset;
        if (this.mDividerAlphaRange <= 1.0f) {
            float abs = Math.abs(r0) / this.mDividerAlphaChangeOffset;
            this.mDividerAlphaRange = abs;
            this.mDivider.setAlpha(abs);
        }
        int i14 = this.mLocationY;
        if (i14 < this.mDividerWidthChangeEndY) {
            this.mNewOffset = this.mDividerWidthChangeOffset;
        } else {
            int i15 = this.mDividerWidthChangeInitY;
            if (i14 > i15) {
                this.mNewOffset = 0;
            } else {
                this.mNewOffset = i15 - i14;
            }
        }
        this.mCurrentOffset = this.mNewOffset;
        float abs2 = Math.abs(r0) / this.mDividerWidthChangeOffset;
        this.mDividerWidthRange = abs2;
        AppBarLayout.LayoutParams layoutParams = this.mDividerParams;
        int i16 = this.mMarginLeftRight;
        layoutParams.setMargins((int) (i16 * (1.0f - abs2)), ((LinearLayout.LayoutParams) layoutParams).topMargin, (int) (i16 * (1.0f - abs2)), ((LinearLayout.LayoutParams) layoutParams).bottomMargin);
        this.mDivider.setLayoutParams(this.mDividerParams);
    }

    @Override // android.widget.AbsListView.OnScrollListener
    public void onScroll(AbsListView absListView, int i10, int i11, int i12) {
        onListScroll();
    }

    @Override // android.widget.AbsListView.OnScrollListener
    public void onScrollStateChanged(AbsListView absListView, int i10) {
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, AppBarLayout appBarLayout, View view, View view2, int i10, int i11) {
        if ((i10 & 2) != 0 && coordinatorLayout.getHeight() - view.getHeight() <= appBarLayout.getHeight()) {
            if (this.mListFirstChildInitY <= 0) {
                this.mListFirstChildInitY = appBarLayout.getMeasuredHeight();
                this.mScrollView = view2;
                View findViewById = appBarLayout.findViewById(R.id.divider_line);
                this.mDivider = findViewById;
                this.mDividerParams = (AppBarLayout.LayoutParams) findViewById.getLayoutParams();
                int i12 = this.mListFirstChildInitY;
                this.mDividerAlphaChangeEndY = i12 - this.mDividerAlphaChangeOffset;
                int dimensionPixelOffset = i12 - this.mResources.getDimensionPixelOffset(R.dimen.divider_width_start_count_offset);
                this.mDividerWidthChangeInitY = dimensionPixelOffset;
                this.mDividerWidthChangeEndY = dimensionPixelOffset - this.mDividerWidthChangeOffset;
            }
            view2.setOnScrollChangeListener(new View.OnScrollChangeListener() { // from class: com.oplus.powermanager.fuelgaue.base.ToolBarBehavior.1
                @Override // android.view.View.OnScrollChangeListener
                public void onScrollChange(View view3, int i13, int i14, int i15, int i16) {
                    ToolBarBehavior.this.onListScroll();
                }
            });
        }
        return false;
    }

    public ToolBarBehavior(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mLocation = new int[2];
        init(context);
    }
}
