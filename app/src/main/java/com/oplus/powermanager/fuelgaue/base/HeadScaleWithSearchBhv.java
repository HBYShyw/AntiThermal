package com.oplus.powermanager.fuelgaue.base;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import b6.LocalLog;
import com.coui.appcompat.list.COUIListView;
import com.coui.appcompat.searchview.COUISearchViewAnimate;
import com.coui.appcompat.toolbar.COUIToolbar;
import com.google.android.material.appbar.AppBarLayout;
import com.oplus.battery.R;
import n3.SimpleSpringListener;
import n3.SpringSystem;
import n3.f;

/* loaded from: classes.dex */
public class HeadScaleWithSearchBhv extends CoordinatorLayout.Behavior<AppBarLayout> {
    private static final int SEARCHVIEW_MIN_HEIGHT = 1;
    private static final float SEARCH_TEXT_ICON_ALPHA = 0.3f;
    private static final int SEARCH_VIEW_BG_CHANGE_ALPHA = 51;
    private static final int SEARCH_VIEW_BG_INIT_ALPHA = 38;
    private static final String TAG = "HeadScaleWithSearchBhv";
    private AppBarLayout mAppBarLayout;
    private View mChild;
    private Context mContext;
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
    private boolean mFlagListScroll;
    private int mHintInitHeight;
    private ViewGroup.LayoutParams mLargeTitleParams;
    private int mListFirstChildEndY;
    private int mListFirstChildInitY;
    private int[] mLocation;
    private int mLocationY;
    private int mMarginLeftRight;
    private int mNewOffset;
    private ReboundListener mReboundListener;
    private Resources mResources;
    private COUIListView mScrollView;
    private int mSearchAlphaChangeEndY;
    private int mSearchAlphaChangeOffset;
    private int mSearchChangeOffset;
    private int mSearchHeightChangeEndY;
    private int mSearchHeightChangeOffset;
    private int mSearchHintTextColor;
    private int mSearchPaddingChangeOffset;
    private int mSearchPaddingRLEndChangeY;
    private int mSearchPaddingRLStartChangeY;
    private COUISearchViewAnimate mSearchView;
    private Drawable mSearchViewBackGround;
    private ImageView mSearchViewIcon;
    private int mSearchViewInitHeight;
    public int mSearchViewInitPaddingRL;
    private FrameLayout.LayoutParams mSearchViewLayoutParams;
    private int mSearchViewMinHeight;
    private f mSpring;
    private SpringSystem mSpringSystem;
    private int mStandardScroll;
    private int mTempLocationY;
    private TextView mTextView;
    private int mTextViewPaddingTop;
    private int mTitleColor;
    private int mTitleMarginChangeEndY;
    private int mTitleMarginChangeInitY;
    private int mTitleMarginChangeOffset;
    private COUIToolbar mToolbar;
    private int mToolbarTitleAlphaChangeInitY;
    private int mToolbarTitleAlphaChangeOffset;
    private int mTotalScaleRange;
    private int mTransparentColor;
    private float searchAlphaRange;
    private float searchHeightRange;
    private float searchPaddingRange;
    private float titleMarginRange;
    private float toolBarTitleRange;

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class ReboundListener extends SimpleSpringListener {
        private ReboundListener() {
        }

        @Override // n3.SimpleSpringListener, n3.SpringListener
        public void onSpringUpdate(f fVar) {
            if (HeadScaleWithSearchBhv.this.mTempLocationY != ((int) HeadScaleWithSearchBhv.this.mSpring.e())) {
                HeadScaleWithSearchBhv.this.mScrollView.scrollBy(0, (int) (fVar.c() - HeadScaleWithSearchBhv.this.mTempLocationY));
            } else {
                HeadScaleWithSearchBhv.this.mSpring.l();
            }
            HeadScaleWithSearchBhv.this.mTempLocationY = (int) fVar.c();
        }
    }

    public HeadScaleWithSearchBhv() {
        this.mListFirstChildEndY = 0;
        this.mSearchHeightChangeEndY = 0;
        this.mTotalScaleRange = 0;
        this.mLocation = new int[2];
        SpringSystem g6 = SpringSystem.g();
        this.mSpringSystem = g6;
        this.mSpring = g6.c();
        this.mFlagListScroll = true;
    }

    private void changeSearchHeight() {
        FrameLayout.LayoutParams layoutParams = this.mSearchViewLayoutParams;
        layoutParams.height = (int) (this.mSearchViewMinHeight + ((this.mSearchViewInitHeight - r1) * (1.0f - this.searchHeightRange)));
        this.mSearchView.setLayoutParams(layoutParams);
        this.mSearchView.setSearchViewAnimateHeightPercent(((1.0f - this.searchHeightRange) + 0.42857146f) * 0.7f);
        this.mSearchView.setTranslationY((-this.searchHeightRange) * 2.0f);
    }

    private void init(Context context) {
        Resources resources = context.getResources();
        this.mResources = resources;
        this.mContext = context;
        this.mSearchHeightChangeOffset = resources.getDimensionPixelOffset(R.dimen.search_height_range_min_height);
        this.mSearchAlphaChangeOffset = this.mResources.getDimensionPixelOffset(R.dimen.search_alpha_range_min_count_height);
        this.mStandardScroll = this.mResources.getDimensionPixelOffset(R.dimen.standard_scroll_height);
        this.mTransparentColor = context.getResources().getColor(R.color.coui_transparence);
        this.mTitleColor = context.getResources().getColor(R.color.coui_color_primary_neutral);
        this.mSearchHintTextColor = context.getResources().getColor(R.color.coui_color_hint_neutral);
        float f10 = this.mResources.getDisplayMetrics().density;
        this.mSearchViewMinHeight = 0;
        this.mReboundListener = new ReboundListener();
        this.mMarginLeftRight = this.mResources.getDimensionPixelOffset(R.dimen.common_margin);
        this.mDividerAlphaChangeOffset = this.mResources.getDimensionPixelOffset(R.dimen.line_alpha_range_change_offset);
        this.mDividerWidthChangeOffset = this.mResources.getDimensionPixelOffset(R.dimen.divider_width_change_offset);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onListScroll() {
        int i10;
        int i11;
        this.mChild = null;
        COUIListView cOUIListView = this.mScrollView;
        if ((cOUIListView instanceof ViewGroup) && cOUIListView.getChildCount() > 0) {
            int i12 = 0;
            while (true) {
                if (i12 >= cOUIListView.getChildCount()) {
                    break;
                }
                if (cOUIListView.getChildAt(i12).getVisibility() == 0) {
                    this.mChild = cOUIListView.getChildAt(i12);
                    break;
                }
                i12++;
            }
        }
        if (this.mChild == null) {
            this.mChild = this.mScrollView;
        }
        int[] iArr = new int[2];
        this.mChild.getLocationInWindow(iArr);
        int i13 = iArr[1];
        if (i13 < this.mSearchHeightChangeEndY) {
            i10 = this.mSearchChangeOffset;
        } else {
            int i14 = this.mListFirstChildInitY;
            i10 = i13 > i14 ? 0 : i14 - i13;
        }
        int abs = Math.abs(i10);
        this.mCurrentOffset = abs;
        if (i13 >= this.mSearchHeightChangeEndY) {
            this.searchHeightRange = abs / this.mSearchChangeOffset;
        } else {
            this.searchHeightRange = 1.0f;
        }
        if (ThemeBundleUtils.getImmersiveTheme()) {
            this.mSearchView.setAlpha(1.0f - (this.searchHeightRange * 2.0f));
        } else {
            changeSearchHeight();
        }
        if (i13 < this.mTitleMarginChangeEndY) {
            i11 = this.mTitleMarginChangeOffset;
        } else {
            int i15 = this.mTitleMarginChangeInitY;
            i11 = i13 > i15 ? 0 : i15 - i13;
        }
        this.mCurrentOffset = Math.abs(i11);
        this.mDividerAlphaRange = Math.abs(r1) / this.mDividerAlphaChangeOffset;
        if (Math.abs(r1 - this.mDivider.getAlpha()) > 0.01d) {
            this.mDivider.setAlpha(this.mDividerAlphaRange);
            LocalLog.a(TAG, "Divider alpha=" + this.mDividerAlphaRange + " mCurrentOffset:" + this.mCurrentOffset + " mDividerAlphaChangeOffset:" + this.mDividerAlphaChangeOffset);
        }
        float abs2 = Math.abs(this.mCurrentOffset) / this.mTitleMarginChangeOffset;
        this.mDividerWidthRange = abs2;
        AppBarLayout.LayoutParams layoutParams = this.mDividerParams;
        int i16 = this.mMarginLeftRight;
        layoutParams.setMargins((int) (i16 * (1.0f - abs2)), ((LinearLayout.LayoutParams) layoutParams).topMargin, (int) (i16 * (1.0f - abs2)), ((LinearLayout.LayoutParams) layoutParams).bottomMargin);
        this.mDivider.setLayoutParams(this.mDividerParams);
        if (i13 >= this.mTitleMarginChangeEndY) {
            this.titleMarginRange = this.mCurrentOffset / this.mTitleMarginChangeOffset;
            if (ThemeBundleUtils.getImmersiveTheme()) {
                TextView textView = this.mTextView;
                textView.setPadding(textView.getPaddingLeft(), (int) (this.mTextViewPaddingTop * (1.0f - this.titleMarginRange)), this.mTextView.getPaddingRight(), this.mTextView.getPaddingBottom());
            } else {
                ((LinearLayout.LayoutParams) this.mLargeTitleParams).topMargin = (int) ((-this.mTextView.getHeight()) * this.titleMarginRange);
            }
        } else {
            this.titleMarginRange = 1.0f;
            if (ThemeBundleUtils.getImmersiveTheme()) {
                TextView textView2 = this.mTextView;
                textView2.setPadding(textView2.getPaddingLeft(), 0, this.mTextView.getPaddingRight(), this.mTextView.getPaddingBottom());
            } else {
                ((LinearLayout.LayoutParams) this.mLargeTitleParams).topMargin = (int) ((-this.mTextView.getHeight()) * this.titleMarginRange);
            }
        }
        if (i13 > this.mSearchPaddingRLStartChangeY) {
            this.searchPaddingRange = 0.0f;
            this.mTextView.setAlpha(1.0f);
        } else {
            int i17 = this.mCurrentOffset;
            int i18 = this.mSearchPaddingChangeOffset;
            if (i17 > i18) {
                i17 = i18;
            }
            float f10 = i17 / i18;
            this.searchPaddingRange = f10;
            this.mTextView.setAlpha(1.0f - f10);
        }
        int i19 = this.mToolbarTitleAlphaChangeInitY;
        if (i13 > i19) {
            this.toolBarTitleRange = 0.0f;
            return;
        }
        int i20 = this.mCurrentOffset - (i19 - this.mListFirstChildEndY);
        this.mCurrentOffset = i20;
        float f11 = i20 / this.mToolbarTitleAlphaChangeOffset;
        this.toolBarTitleRange = f11;
        this.toolBarTitleRange = Math.min(f11, 1.0f);
    }

    public void setListFirstChildInitY() {
        this.mListFirstChildInitY = 0;
    }

    public void setScaleEnable(boolean z10) {
        this.mFlagListScroll = z10;
    }

    @Override // androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, AppBarLayout appBarLayout, View view, View view2, int i10, int i11) {
        if (((i10 & 2) != 0 && coordinatorLayout.getHeight() - view.getHeight() <= appBarLayout.getHeight()) && this.mListFirstChildInitY <= 0) {
            this.mContext = coordinatorLayout.getContext();
            this.mAppBarLayout = appBarLayout;
            this.mToolbar = (COUIToolbar) appBarLayout.findViewById(R.id.power_usage_layout_toolbar);
            COUISearchViewAnimate cOUISearchViewAnimate = (COUISearchViewAnimate) this.mAppBarLayout.findViewById(R.id.searchView);
            this.mSearchView = cOUISearchViewAnimate;
            this.mSearchViewInitPaddingRL = cOUISearchViewAnimate.getPaddingStart();
            this.mSearchViewLayoutParams = (FrameLayout.LayoutParams) this.mSearchView.getLayoutParams();
            this.mSearchViewInitHeight = this.mSearchView.getHeight();
            this.mSearchViewIcon = (ImageView) this.mSearchView.findViewById(R.id.animated_search_icon);
            this.mScrollView = (COUIListView) view2;
            int measuredHeight = this.mAppBarLayout.getMeasuredHeight() + this.mResources.getDimensionPixelOffset(R.dimen.list_to_ex_top_padding);
            this.mListFirstChildInitY = measuredHeight;
            int i12 = this.mStandardScroll;
            this.mListFirstChildEndY = measuredHeight - i12;
            this.mSearchPaddingRLStartChangeY = measuredHeight - (i12 / 2);
            int dimensionPixelOffset = measuredHeight - this.mResources.getDimensionPixelOffset(R.dimen.search_width_range_min_count_height);
            this.mSearchPaddingRLEndChangeY = dimensionPixelOffset;
            this.mSearchPaddingChangeOffset = this.mSearchPaddingRLStartChangeY - dimensionPixelOffset;
            int i13 = this.mListFirstChildInitY;
            this.mTitleMarginChangeInitY = i13 - (this.mStandardScroll / 2);
            int dimensionPixelOffset2 = i13 - this.mResources.getDimensionPixelOffset(R.dimen.with_search_title_margin_range_min_height);
            this.mTitleMarginChangeEndY = dimensionPixelOffset2;
            this.mSearchAlphaChangeEndY = this.mListFirstChildInitY - this.mSearchAlphaChangeOffset;
            this.mTitleMarginChangeOffset = this.mTitleMarginChangeInitY - dimensionPixelOffset2;
            this.mToolbarTitleAlphaChangeInitY = this.mListFirstChildEndY + this.mResources.getDimensionPixelOffset(R.dimen.toolbar_title_alpha_range_max_count_height);
            this.mToolbarTitleAlphaChangeOffset = this.mSearchPaddingRLEndChangeY - this.mTitleMarginChangeEndY;
            int i14 = this.mListFirstChildEndY + this.mSearchHeightChangeOffset;
            this.mSearchHeightChangeEndY = i14;
            this.mSearchChangeOffset = this.mListFirstChildInitY - i14;
            TextView textView = (TextView) this.mAppBarLayout.findViewById(R.id.toolbar_title);
            this.mTextView = textView;
            this.mTextViewPaddingTop = textView.getPaddingTop();
            this.mLargeTitleParams = this.mTextView.getLayoutParams();
            this.mSpring.a(this.mReboundListener);
            View findViewById = appBarLayout.findViewById(R.id.divider_line);
            this.mDivider = findViewById;
            this.mDividerParams = (AppBarLayout.LayoutParams) findViewById.getLayoutParams();
            int i15 = this.mListFirstChildInitY;
            this.mDividerAlphaChangeEndY = i15 - this.mDividerAlphaChangeOffset;
            int dimensionPixelOffset3 = i15 - this.mResources.getDimensionPixelOffset(R.dimen.divider_width_start_count_offset);
            this.mDividerWidthChangeInitY = dimensionPixelOffset3;
            this.mDividerWidthChangeEndY = dimensionPixelOffset3 - this.mDividerWidthChangeOffset;
            this.mScrollView.setOnScrollChangeListener(new View.OnScrollChangeListener() { // from class: com.oplus.powermanager.fuelgaue.base.HeadScaleWithSearchBhv.1
                @Override // android.view.View.OnScrollChangeListener
                public void onScrollChange(View view3, int i16, int i17, int i18, int i19) {
                    if (HeadScaleWithSearchBhv.this.mFlagListScroll) {
                        HeadScaleWithSearchBhv.this.onListScroll();
                    }
                }
            });
        }
        return false;
    }

    public HeadScaleWithSearchBhv(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mListFirstChildEndY = 0;
        this.mSearchHeightChangeEndY = 0;
        this.mTotalScaleRange = 0;
        this.mLocation = new int[2];
        SpringSystem g6 = SpringSystem.g();
        this.mSpringSystem = g6;
        this.mSpring = g6.c();
        this.mFlagListScroll = true;
        init(context);
    }
}
