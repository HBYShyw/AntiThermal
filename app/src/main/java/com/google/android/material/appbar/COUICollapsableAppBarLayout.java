package com.google.android.material.appbar;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.COUIRecyclerView;
import com.coui.appcompat.grid.COUIResponsiveUtils;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.COUIDividerAppBarLayout;
import com.support.appcompat.R$dimen;
import com.support.appcompat.R$id;
import com.support.appcompat.R$styleable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/* loaded from: classes.dex */
public class COUICollapsableAppBarLayout extends COUIDividerAppBarLayout {
    private static boolean DEBUG = false;
    public static final int DEFAULT_SCROLL_FLAG = 19;
    public static final int MODE_COLLAPSABLE = 0;
    public static final int MODE_FIXED_COLLAPSED = 1;
    public static final int MODE_FIXED_EXPANDED = 2;
    private static final String MODE_STATE_KEY = "MODE_STATE_KEY";
    private static final String OFFSET_STATE_KEY = "OFFSET_STATE_KEY";
    private static final String SUPER_STATE_KEY = "SUPER_STATE_KEY";
    private static final String TAG = "COUICollapsableAppBarLayout";
    private static final String TITLE_FRACTION_STATE_KEY = "TITLE_FRACTION_STATE_KEY";
    private boolean mAutoExpand;
    private int mMode;
    private boolean mNeedUpdateModeAfterOffsetChanged;
    private int mOffset;
    private boolean mSubtitleHideEnable;

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes.dex */
    public @interface Mode {
    }

    /* loaded from: classes.dex */
    private static class ScrollBehavior extends COUIDividerAppBarLayout.DividerBehavior {
        private int mLastStartedType;
        private boolean mShouldSnapToBottom;

        private ScrollBehavior() {
        }

        public boolean contentInScreen(View view) {
            return (view.canScrollVertically(1) || view.canScrollVertically(-1)) ? false : true;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @Override // com.google.android.material.appbar.AppBarLayout.BaseBehavior, com.google.android.material.appbar.HeaderBehavior
        public int getTopBottomOffsetForScrollingSibling() {
            if (this.mShouldSnapToBottom) {
                this.mShouldSnapToBottom = false;
                return 0;
            }
            return super.getTopBottomOffsetForScrollingSibling();
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @Override // com.google.android.material.appbar.AppBarLayout.BaseBehavior, com.google.android.material.appbar.HeaderBehavior
        public boolean canDragView(AppBarLayout appBarLayout) {
            return super.canDragView((ScrollBehavior) appBarLayout) && (!(appBarLayout instanceof COUICollapsableAppBarLayout) || ((COUICollapsableAppBarLayout) appBarLayout).getMode() == 0);
        }

        @Override // com.google.android.material.appbar.AppBarLayout.BaseBehavior, androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
        public void onNestedPreScroll(CoordinatorLayout coordinatorLayout, AppBarLayout appBarLayout, View view, int i10, int i11, int[] iArr, int i12) {
            if ((view instanceof COUIRecyclerView) && view.getScrollY() < 0) {
                i11 = 0;
            }
            super.onNestedPreScroll(coordinatorLayout, appBarLayout, view, i10, i11, iArr, i12);
        }

        @Override // com.google.android.material.appbar.COUIDividerAppBarLayout.DividerBehavior, com.google.android.material.appbar.AppBarLayout.BaseBehavior, androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
        public void onNestedScroll(CoordinatorLayout coordinatorLayout, AppBarLayout appBarLayout, View view, int i10, int i11, int i12, int i13, int i14, int[] iArr) {
            super.onNestedScroll(coordinatorLayout, appBarLayout, view, i10, i11, i12, ((appBarLayout instanceof COUICollapsableAppBarLayout) && ((COUICollapsableAppBarLayout) appBarLayout).mMode == 1) ? 0 : i13, i14, iArr);
        }

        @Override // com.google.android.material.appbar.COUIDividerAppBarLayout.DividerBehavior, com.google.android.material.appbar.AppBarLayout.BaseBehavior, androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
        public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, AppBarLayout appBarLayout, View view, View view2, int i10, int i11) {
            this.mLastStartedType = i11;
            return super.onStartNestedScroll(coordinatorLayout, appBarLayout, view, view2, i10, i11);
        }

        @Override // com.google.android.material.appbar.COUIDividerAppBarLayout.DividerBehavior, com.google.android.material.appbar.AppBarLayout.BaseBehavior, androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
        public void onStopNestedScroll(CoordinatorLayout coordinatorLayout, AppBarLayout appBarLayout, View view, int i10) {
            if (appBarLayout.getChildCount() > 0) {
                if (appBarLayout.getChildAt(0) instanceof COUICollapsingToolbarLayout) {
                    int scrollFlags = ((AppBarLayout.LayoutParams) appBarLayout.getChildAt(0).getLayoutParams()).getScrollFlags() & 17;
                    boolean z10 = scrollFlags == 17 && contentInScreen(view) && ((COUICollapsableAppBarLayout) appBarLayout).mCollapsable;
                    this.mShouldSnapToBottom = z10;
                    this.mShouldSnapToBottom = z10 && ((COUICollapsableAppBarLayout) appBarLayout).mAutoExpand;
                    if (COUICollapsableAppBarLayout.DEBUG) {
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append("((mLastStartedType == ViewCompat.TYPE_TOUCH) || (type == ViewCompat.TYPE_NON_TOUCH)) = ");
                        sb2.append(this.mLastStartedType == 0 || i10 == 1);
                        sb2.append("\n((snapFlag & LayoutParams.FLAG_SNAP) == LayoutParams.FLAG_SNAP) = ");
                        sb2.append(scrollFlags == 17);
                        sb2.append("\n(contentInScreen(target)) = ");
                        sb2.append(contentInScreen(view));
                        sb2.append("\n(((COUICollapsableAppBarLayout) abl).mCollapsable = ");
                        sb2.append(((COUICollapsableAppBarLayout) appBarLayout).mCollapsable);
                        Log.d(COUICollapsableAppBarLayout.TAG, sb2.toString());
                    }
                }
            }
            super.onStopNestedScroll(coordinatorLayout, appBarLayout, view, i10);
        }
    }

    public COUICollapsableAppBarLayout(Context context) {
        this(context, null);
    }

    private void adjustSubtitleIfNeed(int i10) {
        View findSubtitleContentView = findSubtitleContentView();
        boolean z10 = findSubtitleContentView != null && findSubtitleContentView.getVisibility() == 0;
        if ((findCollapsingToolbarLayout() != null) && z10) {
            float abs = Math.abs(i10) / getTotalScrollRange();
            float f10 = this.mSubtitleHideEnable ? 1.0f - abs : 1.0f;
            int i11 = (int) (0 + (((-findSubtitleContentView.getMeasuredHeight()) - 0) * abs));
            ViewGroup.LayoutParams layoutParams = findSubtitleContentView.getLayoutParams();
            if (layoutParams instanceof ViewGroup.MarginLayoutParams) {
                ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) layoutParams;
                marginLayoutParams.topMargin = i11;
                if (COUIResponsiveUtils.i(getContext(), getMeasuredWidth())) {
                    marginLayoutParams.setMarginStart(getContext().getResources().getDimensionPixelOffset(R$dimen.toolbar_normal_padding_left_compat));
                } else if (COUIResponsiveUtils.h(getContext(), getMeasuredWidth())) {
                    marginLayoutParams.setMarginStart(getContext().getResources().getDimensionPixelOffset(R$dimen.toolbar_normal_padding_left_medium));
                } else if (COUIResponsiveUtils.g(getContext(), getMeasuredWidth())) {
                    marginLayoutParams.setMarginStart(getContext().getResources().getDimensionPixelOffset(R$dimen.toolbar_normal_padding_left_expanded));
                } else {
                    marginLayoutParams.setMarginStart(0);
                }
            }
            findSubtitleContentView.setLayoutParams(layoutParams);
            findSubtitleContentView.setAlpha(f10);
        }
    }

    private View findCollapsingToolbarLayout() {
        int childCount = getChildCount();
        for (int i10 = 0; i10 < childCount; i10++) {
            View childAt = getChildAt(i10);
            if (childAt instanceof CollapsingToolbarLayout) {
                return childAt;
            }
        }
        return null;
    }

    private float getExpansionFraction() {
        View findCollapsingToolbarLayout = findCollapsingToolbarLayout();
        if (findCollapsingToolbarLayout instanceof COUICollapsingToolbarLayout) {
            return ((COUICollapsingToolbarLayout) findCollapsingToolbarLayout).collapsingTextHelper.getExpansionFraction();
        }
        return 0.0f;
    }

    private void init(AttributeSet attributeSet) {
        TypedArray obtainStyledAttributes = getContext().obtainStyledAttributes(attributeSet, R$styleable.COUICollapsableAppBarLayout);
        this.mMode = obtainStyledAttributes.getInt(R$styleable.COUICollapsableAppBarLayout_mode, 0);
        this.mSubtitleHideEnable = obtainStyledAttributes.getBoolean(R$styleable.COUICollapsableAppBarLayout_subtitleHideEnable, true);
        if (this.mMode == 0) {
            this.mCollapsable = true;
        }
        obtainStyledAttributes.recycle();
    }

    private boolean isCollapsed() {
        View findCollapsingToolbarLayout = findCollapsingToolbarLayout();
        if (findCollapsingToolbarLayout instanceof COUICollapsingToolbarLayout) {
            return ((COUICollapsingToolbarLayout) findCollapsingToolbarLayout).isCollapsed();
        }
        return false;
    }

    private boolean isExpanded() {
        View findCollapsingToolbarLayout = findCollapsingToolbarLayout();
        if (findCollapsingToolbarLayout instanceof COUICollapsingToolbarLayout) {
            return ((COUICollapsingToolbarLayout) findCollapsingToolbarLayout).isExpanded();
        }
        return false;
    }

    private void setScrollFlags(int i10) {
        int childCount = getChildCount();
        for (int i11 = 0; i11 < childCount; i11++) {
            View childAt = getChildAt(i11);
            AppBarLayout.LayoutParams layoutParams = (AppBarLayout.LayoutParams) childAt.getLayoutParams();
            layoutParams.setScrollFlags(i10);
            childAt.setLayoutParams(layoutParams);
        }
    }

    private void updateIconViewLocationIfNeed(float f10) {
        View findCollapsingToolbarLayout = findCollapsingToolbarLayout();
        if (findCollapsingToolbarLayout instanceof COUICollapsingToolbarLayout) {
            COUICollapsingToolbarLayout cOUICollapsingToolbarLayout = (COUICollapsingToolbarLayout) findCollapsingToolbarLayout;
            cOUICollapsingToolbarLayout.collapsingTextHelper.setExpansionFraction(f10);
            cOUICollapsingToolbarLayout.updateIconViewLocationIfNeed();
        }
    }

    public void enableAutoExpand(boolean z10) {
        this.mAutoExpand = z10;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public View findSubtitleContentView() {
        return findViewById(R$id.coui_appbar_subtitle_content);
    }

    @Override // com.google.android.material.appbar.COUIDividerAppBarLayout, com.google.android.material.appbar.AppBarLayout, androidx.coordinatorlayout.widget.CoordinatorLayout.b
    public CoordinatorLayout.Behavior<AppBarLayout> getBehavior() {
        return new ScrollBehavior();
    }

    @Override // com.google.android.material.appbar.COUIDividerAppBarLayout
    protected int getDividerScrollRange() {
        View findCollapsingToolbarLayout = findCollapsingToolbarLayout();
        if (findCollapsingToolbarLayout != null) {
            int i10 = this.mMode;
            if (i10 == 0) {
                return getTotalScrollRange();
            }
            if (i10 == 1) {
                return findCollapsingToolbarLayout.getMinimumHeight();
            }
        }
        return getMeasuredHeight();
    }

    public int getMode() {
        return this.mMode;
    }

    public boolean isSubtitleHideEnable() {
        return this.mSubtitleHideEnable;
    }

    @Override // com.google.android.material.appbar.COUIDividerAppBarLayout, com.google.android.material.appbar.AppBarLayout, android.view.ViewGroup, android.view.View
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        setMode(this.mMode);
    }

    @Override // android.view.View
    protected void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        post(new Runnable() { // from class: com.google.android.material.appbar.a
            @Override // java.lang.Runnable
            public final void run() {
                COUICollapsableAppBarLayout.this.updateSubtitle();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.android.material.appbar.AppBarLayout
    public void onOffsetChanged(int i10) {
        super.onOffsetChanged(i10);
        if (i10 == this.mOffset) {
            return;
        }
        if (this.mMode == 0) {
            this.mScrollDyByOffset = Math.max(0, -i10);
        }
        this.mOffset = i10;
        onDividerChanged();
        adjustSubtitleIfNeed(i10);
        View findCollapsingToolbarLayout = findCollapsingToolbarLayout();
        if (findCollapsingToolbarLayout instanceof COUICollapsingToolbarLayout) {
            ((COUICollapsingToolbarLayout) findCollapsingToolbarLayout).updateIconViewLocationIfNeed();
        }
        if (this.mNeedUpdateModeAfterOffsetChanged) {
            int i11 = this.mMode;
            if (i11 == 0) {
                this.mNeedUpdateModeAfterOffsetChanged = false;
            } else if (i11 == 1 && this.mOffset == (-getDividerScrollRange())) {
                updateIconViewLocationIfNeed(1.0f);
                this.mNeedUpdateModeAfterOffsetChanged = false;
            } else if (this.mMode == 2 && this.mOffset == 0) {
                setScrollFlags(0);
                updateIconViewLocationIfNeed(0.0f);
                this.mNeedUpdateModeAfterOffsetChanged = false;
            }
        }
        if (this.mTargetViewState == 2 && this.mOffset == (-getDividerScrollRange()) && !this.mTargetView.canScrollVertically(1) && !this.mTargetView.canScrollVertically(-1) && this.mMode == 0) {
            setExpanded(true);
        }
    }

    @Override // com.google.android.material.appbar.COUIDividerAppBarLayout, android.view.View
    protected void onRestoreInstanceState(Parcelable parcelable) {
        if (parcelable instanceof Bundle) {
            Bundle bundle = (Bundle) parcelable;
            this.mOffset = bundle.getInt(OFFSET_STATE_KEY);
            this.mMode = bundle.getInt(MODE_STATE_KEY);
            updateIconViewLocationIfNeed(bundle.getFloat(TITLE_FRACTION_STATE_KEY, 0.0f));
            parcelable = bundle.getParcelable(SUPER_STATE_KEY);
        }
        super.onRestoreInstanceState(parcelable);
    }

    @Override // com.google.android.material.appbar.COUIDividerAppBarLayout, android.view.View
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable(SUPER_STATE_KEY, super.onSaveInstanceState());
        bundle.putInt(OFFSET_STATE_KEY, this.mOffset);
        bundle.putInt(MODE_STATE_KEY, this.mMode);
        bundle.putFloat(TITLE_FRACTION_STATE_KEY, getExpansionFraction());
        return bundle;
    }

    @Override // com.google.android.material.appbar.COUIDividerAppBarLayout
    public boolean refreshAppBar(View view) {
        boolean z10;
        boolean refreshAppBar = super.refreshAppBar(view);
        if (!view.canScrollVertically(1) && !view.canScrollVertically(-1) && this.mMode == 0 && isCollapsed() && this.mTargetViewState == 2) {
            setExpanded(true);
            z10 = true;
        } else {
            z10 = false;
        }
        return refreshAppBar || z10;
    }

    @Deprecated
    public void refreshExpand(View view) {
        if (view.canScrollVertically(1) || view.canScrollVertically(-1) || this.mMode != 0 || !isCollapsed()) {
            return;
        }
        setExpanded(true);
    }

    @Override // com.google.android.material.appbar.COUIDividerAppBarLayout
    public void reset() {
        super.reset();
        this.mCollapsable = true;
        setMode(0);
        setExpanded(true);
    }

    @Override // com.google.android.material.appbar.COUIDividerAppBarLayout
    public void setDebug(boolean z10) {
        super.setDebug(z10);
        DEBUG = z10;
    }

    public void setMode(int i10) {
        if (this.mMode == i10) {
            return;
        }
        if (i10 == 0) {
            this.mCollapsable = true;
            setScrollFlags(19);
            if (this.mMode == 1) {
                if (this.mScrollDyByScroll > 0) {
                    this.mScrollDyByOffset -= this.mOffset;
                    onDividerChanged();
                } else {
                    setExpanded(true);
                }
            }
            if (this.mMode == 2 && this.mScrollDyByScroll > 0) {
                this.mScrollDyByOffset -= this.mOffset;
                setExpanded(false);
            }
        } else if (i10 == 1) {
            this.mCollapsable = false;
            setExpanded(false);
            setScrollFlags(19);
            this.mScrollDyByOffset = 0;
            if (isCollapsed()) {
                updateIconViewLocationIfNeed(1.0f);
            } else {
                this.mNeedUpdateModeAfterOffsetChanged = true;
            }
            onDividerChanged();
        } else if (i10 == 2) {
            this.mCollapsable = false;
            setExpanded(true);
            this.mScrollDyByOffset = 0;
            if (isExpanded()) {
                setScrollFlags(0);
                updateIconViewLocationIfNeed(0.0f);
            } else {
                this.mNeedUpdateModeAfterOffsetChanged = true;
            }
            onDividerChanged();
        }
        this.mMode = i10;
    }

    public void setSubtitleHideEnable(boolean z10) {
        if (this.mSubtitleHideEnable != z10) {
            this.mSubtitleHideEnable = z10;
            updateSubtitle();
        }
    }

    public void updateSubtitle() {
        adjustSubtitleIfNeed(this.mOffset);
    }

    public COUICollapsableAppBarLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mMode = 0;
        this.mAutoExpand = true;
        this.mNeedUpdateModeAfterOffsetChanged = false;
        init(attributeSet);
    }

    public COUICollapsableAppBarLayout(Context context, AttributeSet attributeSet, int i10) {
        super(context, attributeSet, i10);
        this.mMode = 0;
        this.mAutoExpand = true;
        this.mNeedUpdateModeAfterOffsetChanged = false;
        init(attributeSet);
    }
}
