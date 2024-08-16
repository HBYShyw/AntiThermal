package com.google.android.material.appbar;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.COUIRecyclerView;
import androidx.recyclerview.widget.RecyclerView;
import com.coui.appcompat.scrollview.COUINestedScrollView;
import com.google.android.material.appbar.AppBarLayout;
import com.support.appcompat.R$attr;
import com.support.appcompat.R$dimen;
import com.support.appcompat.R$layout;
import com.support.appcompat.R$styleable;
import v1.COUIContextUtil;
import w1.COUIDarkModeUtil;

/* loaded from: classes.dex */
public class COUIDividerAppBarLayout extends AppBarLayout {
    private static boolean DEBUG = false;
    private static final String DIVIDER_FRACTION_STATE_KEY = "DIVIDER_FRACTION_STATE_KEY";
    private static final float MAX_FRACTION = 1.0f;
    private static final String OFFSET_DY_SCROLL_STATE_KEY = "OFFSET_DY_SCROLL_STATE_KEY";
    private static final String OVERSCROLL_DY_SCROLL_STATE_KEY = "OVERSCROLL_DY_SCROLL_STATE_KEY";
    private static final String SCROLL_DY_SCROLL_STATE_KEY = "SCROLL_DY_SCROLL_STATE_KEY";
    private static final String SUPER_STATE_KEY = "SUPER_STATE_KEY";
    private static final String TAG = "COUIDividerAppBarLayout";
    protected boolean mCollapsable;
    private float mDividerEndAlpha;
    private int mDividerEndMarginHorizontal;
    private float mDividerFraction;
    private float mDividerStartAlpha;
    private int mDividerStartMarginHorizontal;
    private View mDividerView;
    private boolean mHasDivider;
    private OnDividerProgressChangedListener mOnDividerProgressChangedListener;
    private View.OnLayoutChangeListener mOnLayoutChangeListener;
    private RecyclerView.t mOnScrollListener;
    protected int mScrollDyByOffset;
    protected int mScrollDyByOverScroll;
    protected int mScrollDyByScroll;
    protected RecyclerView mTargetView;
    protected int mTargetViewState;

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class DividerAppBarOnScrollChangedListener implements ViewTreeObserver.OnScrollChangedListener {
        private AppBarLayout mAbl;
        private View mTarget;

        private DividerAppBarOnScrollChangedListener() {
        }

        @Override // android.view.ViewTreeObserver.OnScrollChangedListener
        public void onScrollChanged() {
            View view = this.mTarget;
            if (view instanceof COUIRecyclerView) {
                int scrollY = view.getScrollY();
                ((COUIDividerAppBarLayout) this.mAbl).mScrollDyByOverScroll = Math.max(0, scrollY);
                ((COUIDividerAppBarLayout) this.mAbl).onDividerChanged();
                if (((COUIDividerAppBarLayout) this.mAbl).mScrollDyByOverScroll == 0) {
                    this.mTarget.getViewTreeObserver().removeOnScrollChangedListener(this);
                }
            }
        }

        public void setAbl(AppBarLayout appBarLayout) {
            this.mAbl = appBarLayout;
        }

        public void setTarget(View view) {
            this.mTarget = view;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static class DividerBehavior extends AppBarLayout.Behavior {
        private DividerAppBarOnScrollChangedListener mScrollChangedListener;

        public boolean canScrollUp(View view) {
            return view.canScrollVertically(1);
        }

        @Override // com.google.android.material.appbar.AppBarLayout.BaseBehavior, androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
        public void onNestedScroll(CoordinatorLayout coordinatorLayout, AppBarLayout appBarLayout, View view, int i10, int i11, int i12, int i13, int i14, int[] iArr) {
            if ((appBarLayout instanceof COUIDividerAppBarLayout) && (view instanceof COUIRecyclerView)) {
                COUIDividerAppBarLayout cOUIDividerAppBarLayout = (COUIDividerAppBarLayout) appBarLayout;
                int i15 = cOUIDividerAppBarLayout.mScrollDyByOverScroll;
                int max = Math.max(0, view.getScrollY());
                cOUIDividerAppBarLayout.mScrollDyByOverScroll = max;
                if (i15 != max) {
                    cOUIDividerAppBarLayout.onDividerChanged();
                }
            }
            super.onNestedScroll(coordinatorLayout, appBarLayout, view, i10, i11, i12, i13, i14, iArr);
        }

        @Override // com.google.android.material.appbar.AppBarLayout.BaseBehavior, androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
        public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, AppBarLayout appBarLayout, View view, View view2, int i10, int i11) {
            return super.onStartNestedScroll(coordinatorLayout, appBarLayout, view, view2, i10, i11) || ((appBarLayout instanceof COUIDividerAppBarLayout) && ((COUIDividerAppBarLayout) appBarLayout).isDividerAnimEnable());
        }

        @Override // com.google.android.material.appbar.AppBarLayout.BaseBehavior, androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
        public void onStopNestedScroll(CoordinatorLayout coordinatorLayout, AppBarLayout appBarLayout, View view, int i10) {
            if (view instanceof COUIRecyclerView) {
                if (this.mScrollChangedListener == null) {
                    this.mScrollChangedListener = new DividerAppBarOnScrollChangedListener();
                }
                this.mScrollChangedListener.setTarget(view);
                this.mScrollChangedListener.setAbl(appBarLayout);
                view.getViewTreeObserver().addOnScrollChangedListener(this.mScrollChangedListener);
            }
            super.onStopNestedScroll(coordinatorLayout, appBarLayout, view, i10);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public interface OnDividerProgressChangedListener {
        void onDividerProgressChanged(float f10);
    }

    public COUIDividerAppBarLayout(Context context) {
        this(context, null);
    }

    private void bindListener() {
        RecyclerView recyclerView = this.mTargetView;
        if (recyclerView != null) {
            recyclerView.addOnScrollListener(this.mOnScrollListener);
            this.mTargetView.addOnLayoutChangeListener(this.mOnLayoutChangeListener);
        } else if (DEBUG) {
            Log.d(TAG, "Can not find RecyclerView");
        }
    }

    private void findRecyclerView() {
        ViewGroup viewGroup = (ViewGroup) getParent();
        for (int i10 = 0; i10 < viewGroup.getChildCount(); i10++) {
            if (viewGroup.getChildAt(i10) instanceof RecyclerView) {
                this.mTargetView = (RecyclerView) viewGroup.getChildAt(i10);
                return;
            }
        }
    }

    private void init(AttributeSet attributeSet) {
        TypedArray obtainStyledAttributes = getContext().obtainStyledAttributes(attributeSet, R$styleable.COUIDividerAppBarLayout);
        this.mHasDivider = obtainStyledAttributes.getBoolean(R$styleable.COUIDividerAppBarLayout_hasDivider, true);
        this.mDividerStartAlpha = obtainStyledAttributes.getFloat(R$styleable.COUIDividerAppBarLayout_dividerStartAlpha, 0.0f);
        this.mDividerEndAlpha = obtainStyledAttributes.getFloat(R$styleable.COUIDividerAppBarLayout_dividerEndAlpha, this.mHasDivider ? 1.0f : 0.0f);
        this.mDividerStartMarginHorizontal = obtainStyledAttributes.getDimensionPixelOffset(R$styleable.COUIDividerAppBarLayout_dividerStartMarginHorizontal, getContext().getResources().getDimensionPixelOffset(R$dimen.coui_appbar_divider_expanded_margin_horizontal));
        this.mDividerEndMarginHorizontal = obtainStyledAttributes.getDimensionPixelOffset(R$styleable.COUIDividerAppBarLayout_dividerEndMarginHorizontal, getContext().getResources().getDimensionPixelOffset(R$dimen.coui_appbar_divider_collapsed_margin_horizontal));
        obtainStyledAttributes.recycle();
        this.mDividerEndAlpha = Math.max(0.0f, Math.min(this.mDividerEndAlpha, MAX_FRACTION));
        this.mDividerStartAlpha = Math.max(0.0f, Math.min(this.mDividerStartAlpha, MAX_FRACTION));
        this.mOnScrollListener = new RecyclerView.t() { // from class: com.google.android.material.appbar.COUIDividerAppBarLayout.1
            @Override // androidx.recyclerview.widget.RecyclerView.t
            public void onScrollStateChanged(RecyclerView recyclerView, int i10) {
                super.onScrollStateChanged(recyclerView, i10);
                COUIDividerAppBarLayout.this.mTargetViewState = i10;
            }

            @Override // androidx.recyclerview.widget.RecyclerView.t
            public void onScrolled(RecyclerView recyclerView, int i10, int i11) {
                super.onScrolled(recyclerView, i10, i11);
                COUIDividerAppBarLayout.this.mScrollDyByScroll = recyclerView.computeVerticalScrollOffset();
                COUIDividerAppBarLayout.this.onDividerChanged();
            }
        };
        this.mOnLayoutChangeListener = new View.OnLayoutChangeListener() { // from class: com.google.android.material.appbar.COUIDividerAppBarLayout.2
            @Override // android.view.View.OnLayoutChangeListener
            public void onLayoutChange(View view, int i10, int i11, int i12, int i13, int i14, int i15, int i16, int i17) {
                COUIDividerAppBarLayout.this.refreshAppBar(view);
            }
        };
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isDividerAnimEnable() {
        return (this.mDividerView != null && this.mHasDivider) && ((this.mDividerStartAlpha > this.mDividerEndAlpha ? 1 : (this.mDividerStartAlpha == this.mDividerEndAlpha ? 0 : -1)) != 0 || this.mDividerStartMarginHorizontal != this.mDividerEndMarginHorizontal);
    }

    private void setDividerHorizontalMargin(int i10) {
        ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) this.mDividerView.getLayoutParams();
        marginLayoutParams.leftMargin = i10;
        marginLayoutParams.rightMargin = i10;
        this.mDividerView.setLayoutParams(marginLayoutParams);
    }

    public void bindRecyclerView(RecyclerView recyclerView) {
        RecyclerView recyclerView2 = this.mTargetView;
        if (recyclerView2 != null) {
            recyclerView2.removeOnScrollListener(this.mOnScrollListener);
            this.mTargetView.removeOnLayoutChangeListener(this.mOnLayoutChangeListener);
        }
        this.mTargetView = recyclerView;
        if (recyclerView != null) {
            recyclerView.addOnScrollListener(this.mOnScrollListener);
            this.mTargetView.addOnLayoutChangeListener(this.mOnLayoutChangeListener);
        }
    }

    @Override // com.google.android.material.appbar.AppBarLayout, androidx.coordinatorlayout.widget.CoordinatorLayout.b
    public CoordinatorLayout.Behavior<AppBarLayout> getBehavior() {
        return new DividerBehavior();
    }

    public float getDividerEndAlpha() {
        return this.mDividerEndAlpha;
    }

    public int getDividerEndMarginHorizontal() {
        return this.mDividerEndMarginHorizontal;
    }

    protected int getDividerScrollRange() {
        return getMeasuredHeight();
    }

    public float getDividerStartAlpha() {
        return this.mDividerStartAlpha;
    }

    public int getDividerStartMarginHorizontal() {
        return this.mDividerStartMarginHorizontal;
    }

    public boolean hasDivider() {
        return this.mHasDivider;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.google.android.material.appbar.AppBarLayout, android.view.ViewGroup, android.view.View
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (this.mDividerView == null) {
            View inflate = LayoutInflater.from(getContext()).inflate(R$layout.coui_appbar_divider_layout, (ViewGroup) this, false);
            this.mDividerView = inflate;
            addView(inflate, getChildCount());
            this.mDividerView.setAlpha(this.mDividerStartAlpha);
        }
        this.mDividerView.setBackgroundColor(COUIContextUtil.a(getContext(), R$attr.couiColorDivider));
        refreshDivider();
        this.mDividerView.setVisibility(this.mHasDivider ? 0 : 8);
        COUIDarkModeUtil.b(this.mDividerView, false);
        findRecyclerView();
        bindListener();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.google.android.material.appbar.AppBarLayout, android.view.ViewGroup, android.view.View
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        RecyclerView recyclerView = this.mTargetView;
        if (recyclerView != null) {
            recyclerView.removeOnScrollListener(this.mOnScrollListener);
            this.mTargetView.removeOnLayoutChangeListener(this.mOnLayoutChangeListener);
        } else if (DEBUG) {
            Log.d(TAG, "Can not find RecyclerView");
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onDividerChanged() {
        if (DEBUG) {
            Log.d(TAG, "onDividerChanged: mScrollDyByScroll = " + this.mScrollDyByScroll + " mScrollDyByOffset = " + this.mScrollDyByOffset + " mScrollDyByOverScroll = " + this.mScrollDyByOverScroll);
        }
        int max = Math.max(0, this.mScrollDyByScroll) + this.mScrollDyByOffset + this.mScrollDyByOverScroll;
        if (max < 0 || !isDividerAnimEnable()) {
            return;
        }
        float f10 = this.mDividerFraction;
        float dividerScrollRange = getDividerScrollRange();
        if (dividerScrollRange == 0.0f) {
            this.mDividerFraction = dividerScrollRange;
        } else {
            this.mDividerFraction = Math.min(max / dividerScrollRange, MAX_FRACTION);
        }
        float f11 = this.mDividerStartAlpha;
        float f12 = this.mDividerEndAlpha - f11;
        float f13 = this.mDividerFraction;
        float f14 = f11 + (f12 * f13);
        int i10 = this.mDividerStartMarginHorizontal + ((int) ((this.mDividerEndMarginHorizontal - r2) * f13));
        this.mDividerView.setAlpha(f14);
        setDividerHorizontalMargin(i10);
        OnDividerProgressChangedListener onDividerProgressChangedListener = this.mOnDividerProgressChangedListener;
        if (onDividerProgressChangedListener != null) {
            float f15 = this.mDividerFraction;
            if (f10 != f15) {
                onDividerProgressChangedListener.onDividerProgressChanged(f15);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.view.View
    public void onRestoreInstanceState(Parcelable parcelable) {
        if (parcelable instanceof Bundle) {
            Bundle bundle = (Bundle) parcelable;
            this.mScrollDyByOffset = bundle.getInt(OFFSET_DY_SCROLL_STATE_KEY);
            this.mScrollDyByScroll = bundle.getInt(SCROLL_DY_SCROLL_STATE_KEY);
            this.mScrollDyByOverScroll = bundle.getInt(OVERSCROLL_DY_SCROLL_STATE_KEY);
            this.mDividerFraction = bundle.getFloat(DIVIDER_FRACTION_STATE_KEY);
            parcelable = bundle.getParcelable(SUPER_STATE_KEY);
        }
        super.onRestoreInstanceState(parcelable);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.view.View
    public Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable(SUPER_STATE_KEY, super.onSaveInstanceState());
        bundle.putInt(OFFSET_DY_SCROLL_STATE_KEY, this.mScrollDyByOffset);
        bundle.putInt(SCROLL_DY_SCROLL_STATE_KEY, this.mScrollDyByScroll);
        bundle.putInt(OVERSCROLL_DY_SCROLL_STATE_KEY, this.mScrollDyByOverScroll);
        bundle.putFloat(DIVIDER_FRACTION_STATE_KEY, this.mDividerFraction);
        return bundle;
    }

    public boolean refreshAppBar(View view) {
        int scrollY;
        if (view instanceof COUIRecyclerView) {
            scrollY = ((COUIRecyclerView) view).computeVerticalScrollOffset();
        } else {
            if (!(view instanceof COUINestedScrollView)) {
                return false;
            }
            scrollY = view.getScrollY();
        }
        if (scrollY == this.mScrollDyByScroll) {
            return false;
        }
        this.mScrollDyByScroll = scrollY;
        return true;
    }

    public void refreshDivider() {
        float f10 = this.mDividerStartAlpha;
        float f11 = this.mDividerEndAlpha - f10;
        float f12 = this.mDividerFraction;
        float f13 = f10 + (f11 * f12);
        int i10 = this.mDividerStartMarginHorizontal + ((int) ((this.mDividerEndMarginHorizontal - r1) * f12));
        View view = this.mDividerView;
        if (view != null) {
            view.setBackgroundColor(COUIContextUtil.a(getContext(), R$attr.couiColorDivider));
            this.mDividerView.setAlpha(f13);
        }
        onDividerChanged();
        setDividerHorizontalMargin(i10);
        OnDividerProgressChangedListener onDividerProgressChangedListener = this.mOnDividerProgressChangedListener;
        if (onDividerProgressChangedListener != null) {
            onDividerProgressChangedListener.onDividerProgressChanged(this.mDividerFraction);
        }
    }

    public void reset() {
        this.mScrollDyByScroll = 0;
        this.mScrollDyByOffset = 0;
        this.mScrollDyByOverScroll = 0;
    }

    public void setDebug(boolean z10) {
        DEBUG = z10;
    }

    public void setDividerEndAlpha(float f10) {
        this.mDividerEndAlpha = f10;
    }

    public void setDividerEndMarginHorizontal(int i10) {
        this.mDividerEndMarginHorizontal = i10;
    }

    public void setDividerStartAlpha(float f10) {
        this.mDividerStartAlpha = f10;
    }

    public void setDividerStartMarginHorizontal(int i10) {
        this.mDividerStartMarginHorizontal = i10;
    }

    public void setHasDivider(boolean z10) {
        this.mHasDivider = z10;
        View view = this.mDividerView;
        if (view != null) {
            view.setVisibility(z10 ? 0 : 8);
        }
    }

    public void setOnDividerProgressChangedListener(OnDividerProgressChangedListener onDividerProgressChangedListener) {
        this.mOnDividerProgressChangedListener = onDividerProgressChangedListener;
    }

    public COUIDividerAppBarLayout(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public COUIDividerAppBarLayout(Context context, AttributeSet attributeSet, int i10) {
        super(context, attributeSet, i10);
        this.mCollapsable = false;
        this.mTargetViewState = 0;
        this.mTargetView = null;
        this.mDividerFraction = 0.0f;
        this.mOnDividerProgressChangedListener = null;
        this.mOnScrollListener = null;
        this.mOnLayoutChangeListener = null;
        init(attributeSet);
    }
}
