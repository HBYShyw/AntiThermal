package com.oplus.powermanager.fuelgaue.base;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Interpolator;
import android.widget.TextView;
import androidx.appcompat.widget.SearchView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import b6.LocalLog;
import com.android.internal.policy.SystemBarUtils;
import com.coui.appcompat.grid.COUIPercentWidthFrameLayout;
import com.coui.appcompat.grid.COUIPercentWidthRecyclerView;
import com.coui.appcompat.searchview.COUISearchViewAnimate;
import com.coui.appcompat.theme.COUIThemeOverlay;
import com.coui.appcompat.toolbar.COUIToolbar;
import com.google.android.material.appbar.AppBarLayout;
import com.oplus.anim.EffectiveAnimationView;
import com.oplus.battery.R;
import f6.f;
import java.util.List;
import m1.COUIMoveEaseInterpolator;
import w1.COUIDarkModeUtil;
import w2.COUIStatusBarResponseUtil;

/* loaded from: classes.dex */
public abstract class BaseSearchViewActivity extends BaseAppCompatActivity {
    private static final int DELAY_ONE_HUNDRED = 100;
    private static final int EMPTY_SEARCH = 1;
    private static final int FADE_DURATION = 150;
    private static final int MESSAGE_SEARCH = 2;
    public static final int MIN_SEARCH_VIEW_HEIGHT = 5;
    private static final int NOT_SEARCH = 0;
    private static final String SAVE_SEARCH_STATUS = "save_search_status";
    private static final String SEARCH_INIT = "search_init";
    private static final String SEARCH_STATUS = "search_status_fix";
    private static final String TAG = "BaseSearchViewActivity";
    private static final float TITLE_ALPHA = 1.0f;
    private static final int TITLE_FADE_DURATION = 100;
    private static final int TRANSLATE_UP_DURATION = 450;
    private AnimatorListenerAdapter mAnimBackListener;
    private AnimatorListenerAdapter mAnimEnterListener;
    private AppBarLayout mAppBarLayout;
    private View mBackgroundMask;
    private CoordinatorLayout.Behavior<AppBarLayout> mBaseBehavior;
    private View mContainer;
    private int mContainerTopPadding;
    private View mContentLayout;
    private Interpolator mCubicBezierEnterInterpolator;
    private Interpolator mCubicBezierExitInterpolator;
    private EffectiveAnimationView mEmptyAnimation;
    private ViewGroup mEmptyContainer;
    private AnimatorSet mEnterSet;
    private AnimatorSet mExitSet;
    private ObjectAnimator mHeaderHeightEnterAnimator;
    private ObjectAnimator mHeaderHeightExitAnimator;
    private HeightView mHeaderHeightView;
    private View mHeaderView;
    private int mHeaderViewInitHeight;
    private boolean mIsQueryTextCleared;
    private CoordinatorLayout.e mLayoutParams;
    private ObjectAnimator mPaddingExitAnimator;
    private ObjectAnimator mPaddingTopEnterAnimator;
    private PaddingTopView mPaddingTopView;
    private ViewGroup mResultContainer;
    private COUIPercentWidthRecyclerView mResultListView;
    private ObjectAnimator mSearchHeightEnterAnimator;
    private ObjectAnimator mSearchHeightExitAnimator;
    private HeightView mSearchHeightView;
    private ObjectAnimator mSearchMarginEnterAnimator;
    private ObjectAnimator mSearchMarginExitAnimator;
    protected COUISearchViewAnimate mSearchView;
    protected COUIPercentWidthFrameLayout mSearchViewLayout;
    private int mStatusBarHeight;
    private COUIStatusBarResponseUtil mStatusBarUtil;
    private ObjectAnimator mTitleTopMarginEnterAnimator;
    private ObjectAnimator mTitleTopMarginExitAnimator;
    private TopMarginView mTitleTopMarginview;
    private TextView mTitleView;
    private COUIToolbar mToolbar;
    private ValueAnimator mToolbarBloomAnimator;
    private ValueAnimator mToolbarFadeAnimator;
    private TopMarginView mTopMarginView;
    protected Context mContext = null;
    private SharedPreferences mSharedPreferences = null;
    private SharedPreferences.Editor mEditor = null;
    private boolean mIsRestoreFlag = false;
    private int mSearchState = -1;
    private boolean mResultFlag = false;
    private boolean mInitAnimator = false;

    private void animBack() {
        this.mIsQueryTextCleared = true;
        this.mExitSet.start();
        this.mTitleView.animate().alpha(TITLE_ALPHA).setDuration(100L).setStartDelay(100L).start();
        if (ThemeBundleUtils.getImmersiveTheme()) {
            this.mToolbarBloomAnimator.start();
        } else {
            this.mToolbar.animate().alpha(TITLE_ALPHA).setListener(new AnimatorListenerAdapter() { // from class: com.oplus.powermanager.fuelgaue.base.BaseSearchViewActivity.9
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationStart(Animator animator) {
                    for (int i10 = 0; i10 < BaseSearchViewActivity.this.mToolbar.getChildCount(); i10++) {
                        BaseSearchViewActivity.this.mToolbar.getChildAt(i10).setVisibility(0);
                    }
                }
            }).setDuration(150L).start();
        }
        ViewGroup viewGroup = this.mEmptyContainer;
        if (viewGroup != null && viewGroup.getVisibility() == 0) {
            this.mEmptyContainer.setVisibility(8);
            this.mEmptyAnimation.j();
            this.mBackgroundMask.setVisibility(8);
            this.mBackgroundMask.setAlpha(0.0f);
        } else {
            this.mBackgroundMask.animate().alpha(0.0f).setDuration(150L).setListener(new AnimatorListenerAdapter() { // from class: com.oplus.powermanager.fuelgaue.base.BaseSearchViewActivity.10
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    super.onAnimationEnd(animator);
                    BaseSearchViewActivity.this.mBackgroundMask.setVisibility(8);
                }
            }).start();
        }
        this.mEditor.putInt(SEARCH_STATUS, 0);
        this.mEditor.apply();
        LocalLog.a(TAG, "animBack");
    }

    private void animToSearch() {
        this.mIsQueryTextCleared = false;
        this.mEnterSet.start();
        this.mTitleView.animate().alpha(0.0f).setDuration(100L).setStartDelay(0L).start();
        if (ThemeBundleUtils.getImmersiveTheme()) {
            this.mToolbarFadeAnimator.start();
        } else {
            this.mToolbar.animate().alpha(0.0f).setListener(new AnimatorListenerAdapter() { // from class: com.oplus.powermanager.fuelgaue.base.BaseSearchViewActivity.8
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    for (int i10 = 0; i10 < BaseSearchViewActivity.this.mToolbar.getChildCount(); i10++) {
                        BaseSearchViewActivity.this.mToolbar.getChildAt(i10).setVisibility(8);
                    }
                }
            }).setDuration(150L).start();
        }
        if (this.mSharedPreferences.getInt(SEARCH_STATUS, 0) == 0) {
            this.mEditor.putInt(SEARCH_STATUS, 1);
            this.mEditor.apply();
        }
        if (this.mSharedPreferences.getInt(SEARCH_STATUS, 0) == 1) {
            this.mBackgroundMask.setVisibility(0);
            this.mBackgroundMask.setAlpha(0.0f);
            this.mBackgroundMask.animate().alpha(TITLE_ALPHA).setDuration(150L).setListener(null).start();
            LocalLog.a(TAG, "not click in while search empty");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void doInitSearchState() {
        int i10 = this.mSearchState;
        if (i10 == 1) {
            animToSearch();
        } else if (i10 == 0) {
            this.mIsRestoreFlag = false;
            ViewGroup viewGroup = this.mResultContainer;
            if (viewGroup != null) {
                viewGroup.setVisibility(8);
                this.mBackgroundMask.setVisibility(8);
                this.mResultFlag = false;
                LocalLog.a(TAG, "onStateChange mResultContainer set false");
            }
            animBack();
        }
        this.mSearchState = -1;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void fadeToolbarChild(float f10) {
        for (int i10 = 0; i10 < this.mToolbar.getChildCount(); i10++) {
            this.mToolbar.getChildAt(i10).setAlpha(f10);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void initAnimators() {
        TopMarginView topMarginView = new TopMarginView();
        this.mTopMarginView = topMarginView;
        topMarginView.addView(this.mSearchViewLayout);
        int viewTopMargin = TopMarginView.getViewTopMargin(this.mSearchViewLayout);
        int i10 = -Math.max(this.mSearchViewLayout.getHeight(), this.mToolbar.getHeight() - this.mToolbar.getPaddingTop());
        this.mSearchMarginEnterAnimator = ObjectAnimator.ofInt(this.mTopMarginView, "topMargin", viewTopMargin, i10);
        if (ThemeBundleUtils.getImmersiveTheme()) {
            ValueAnimator ofFloat = ValueAnimator.ofFloat(TITLE_ALPHA, 0.0f);
            this.mToolbarFadeAnimator = ofFloat;
            ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.oplus.powermanager.fuelgaue.base.BaseSearchViewActivity.4
                @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    if (valueAnimator == null || valueAnimator.getAnimatedValue() == null) {
                        return;
                    }
                    BaseSearchViewActivity.this.fadeToolbarChild(((Float) valueAnimator.getAnimatedValue()).floatValue());
                }
            });
            this.mToolbarFadeAnimator.addListener(new AnimatorListenerAdapter() { // from class: com.oplus.powermanager.fuelgaue.base.BaseSearchViewActivity.5
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    for (int i11 = 0; i11 < BaseSearchViewActivity.this.mToolbar.getChildCount(); i11++) {
                        BaseSearchViewActivity.this.mToolbar.getChildAt(i11).setVisibility(8);
                    }
                }
            });
            ValueAnimator ofFloat2 = ValueAnimator.ofFloat(0.0f, TITLE_ALPHA);
            this.mToolbarBloomAnimator = ofFloat2;
            ofFloat2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.oplus.powermanager.fuelgaue.base.BaseSearchViewActivity.6
                @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    if (valueAnimator == null || valueAnimator.getAnimatedValue() == null) {
                        return;
                    }
                    BaseSearchViewActivity.this.fadeToolbarChild(((Float) valueAnimator.getAnimatedValue()).floatValue());
                }
            });
            this.mToolbarBloomAnimator.addListener(new AnimatorListenerAdapter() { // from class: com.oplus.powermanager.fuelgaue.base.BaseSearchViewActivity.7
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    for (int i11 = 0; i11 < BaseSearchViewActivity.this.mToolbar.getChildCount(); i11++) {
                        BaseSearchViewActivity.this.mToolbar.getChildAt(i11).setVisibility(0);
                    }
                }
            });
            this.mToolbarBloomAnimator.setDuration(150L);
            this.mToolbarFadeAnimator.setDuration(150L);
        }
        this.mSearchHeightView = new HeightView(this.mSearchView);
        int height = this.mSearchView.getHeight();
        int height2 = this.mToolbar.getHeight() - this.mToolbar.getPaddingTop();
        this.mSearchHeightEnterAnimator = ObjectAnimator.ofInt(this.mSearchHeightView, "height", height, height2);
        HeightView heightView = new HeightView(this.mHeaderView);
        this.mHeaderHeightView = heightView;
        this.mHeaderHeightEnterAnimator = ObjectAnimator.ofInt(heightView, "height", this.mHeaderViewInitHeight, this.mToolbar.getHeight() + this.mStatusBarHeight);
        TopMarginView topMarginView2 = new TopMarginView();
        this.mTitleTopMarginview = topMarginView2;
        topMarginView2.addView(this.mTitleView);
        ObjectAnimator ofInt = ObjectAnimator.ofInt(this.mTitleTopMarginview, "topMargin", TopMarginView.getViewTopMargin(this.mTitleView), -this.mTitleView.getHeight());
        this.mTitleTopMarginEnterAnimator = ofInt;
        AnimatorListenerAdapter animatorListenerAdapter = this.mAnimEnterListener;
        if (animatorListenerAdapter != null) {
            ofInt.addListener(animatorListenerAdapter);
        }
        PaddingTopView paddingTopView = new PaddingTopView(this.mContainer);
        this.mPaddingTopView = paddingTopView;
        this.mPaddingTopEnterAnimator = ObjectAnimator.ofInt(paddingTopView, "paddingTop", this.mContainer.getPaddingTop(), this.mToolbar.getHeight() + this.mStatusBarHeight);
        AnimatorSet animatorSet = new AnimatorSet();
        this.mEnterSet = animatorSet;
        animatorSet.playTogether(this.mSearchMarginEnterAnimator, this.mSearchHeightEnterAnimator, this.mPaddingTopEnterAnimator, this.mHeaderHeightEnterAnimator, this.mTitleTopMarginEnterAnimator);
        this.mEnterSet.setDuration(450L);
        this.mEnterSet.setInterpolator(this.mCubicBezierEnterInterpolator);
        this.mSearchMarginExitAnimator = ObjectAnimator.ofInt(this.mTopMarginView, "topMargin", i10, viewTopMargin);
        this.mSearchHeightExitAnimator = ObjectAnimator.ofInt(this.mSearchHeightView, "height", height2, height);
        this.mHeaderHeightExitAnimator = ObjectAnimator.ofInt(this.mHeaderHeightView, "height", this.mToolbar.getHeight() + this.mStatusBarHeight, this.mHeaderViewInitHeight);
        ObjectAnimator ofInt2 = ObjectAnimator.ofInt(this.mTitleTopMarginview, "topMargin", -this.mTitleView.getHeight(), 0);
        this.mTitleTopMarginExitAnimator = ofInt2;
        AnimatorListenerAdapter animatorListenerAdapter2 = this.mAnimBackListener;
        if (animatorListenerAdapter2 != null) {
            ofInt2.addListener(animatorListenerAdapter2);
        }
        this.mPaddingExitAnimator = ObjectAnimator.ofInt(this.mPaddingTopView, "paddingTop", this.mToolbar.getHeight() + this.mStatusBarHeight, this.mContainerTopPadding);
        AnimatorSet animatorSet2 = new AnimatorSet();
        this.mExitSet = animatorSet2;
        animatorSet2.playTogether(this.mSearchMarginExitAnimator, this.mSearchHeightExitAnimator, this.mPaddingExitAnimator, this.mHeaderHeightExitAnimator, this.mTitleTopMarginExitAnimator);
        this.mExitSet.setDuration(450L);
        this.mExitSet.setInterpolator(this.mCubicBezierExitInterpolator);
        this.mInitAnimator = true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$initBaseSearchView$0(View view) {
        this.mSearchView.setSearchViewAnimateHeightPercent(TITLE_ALPHA);
        this.mSearchView.L(0);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$initBaseSearchView$1(View view) {
        this.mSearchView.setSearchViewAnimateHeightPercent(TITLE_ALPHA);
        this.mSearchView.L(1);
    }

    public void addSearchStateChangeListener(COUISearchViewAnimate.u uVar) {
        this.mSearchView.K(uVar);
    }

    public void changeSearchState(boolean z10) {
        this.mSearchView.L(z10 ? 1 : 0);
    }

    public CoordinatorLayout.Behavior<AppBarLayout> getBaseBehavior() {
        return this.mBaseBehavior;
    }

    public int getHeaderHeight() {
        return this.mHeaderView.getMeasuredHeight();
    }

    public int getHeaderInitHeight() {
        return this.mHeaderViewInitHeight;
    }

    public View getHeaderView() {
        return this.mHeaderView;
    }

    public void handleContentVisibleIfNeeded() {
        if (this.mResultContainer.getVisibility() == 0) {
            this.mContentLayout.setVisibility(8);
        } else {
            this.mContentLayout.setVisibility(0);
        }
    }

    protected abstract void handleGlobalLayout();

    public boolean handleQueryText(String str, List list) {
        boolean z10 = true;
        if (!TextUtils.isEmpty(str) && !this.mIsQueryTextCleared) {
            this.mEditor.putBoolean(SEARCH_INIT, true);
            this.mContentLayout.setVisibility(8);
            this.mBackgroundMask.setVisibility(8);
            this.mResultContainer.setVisibility(0);
            this.mResultFlag = true;
            this.mEditor.putInt(SEARCH_STATUS, 2);
            if (list.isEmpty()) {
                this.mResultListView.setVisibility(8);
                this.mEmptyContainer.setVisibility(0);
                if (!this.mEmptyAnimation.r()) {
                    if (COUIDarkModeUtil.a(this.mContext)) {
                        this.mEmptyAnimation.setAnimation("search-results-empty-dark.json");
                    } else {
                        this.mEmptyAnimation.setAnimation("search-results-empty-light.json");
                    }
                    this.mEmptyAnimation.u();
                }
            } else {
                this.mEmptyContainer.setVisibility(8);
                this.mEmptyAnimation.j();
                this.mResultListView.setVisibility(0);
                RecyclerView.h adapter = this.mResultListView.getAdapter();
                if (adapter != null) {
                    adapter.notifyDataSetChanged();
                }
            }
        } else {
            this.mEditor.putBoolean(SEARCH_INIT, false);
            if (this.mIsRestoreFlag) {
                list.clear();
                this.mContentLayout.setVisibility(0);
                this.mBackgroundMask.setVisibility(0);
                this.mResultContainer.setVisibility(8);
                this.mResultFlag = false;
                this.mEditor.putInt(SEARCH_STATUS, 1);
                z10 = false;
            }
        }
        this.mEditor.apply();
        return z10;
    }

    public void handleSearchStateChange(int i10) {
        if (i10 == 1) {
            this.mIsRestoreFlag = true;
            if (this.mInitAnimator) {
                animToSearch();
                return;
            } else {
                this.mSearchState = 1;
                return;
            }
        }
        if (i10 == 0) {
            this.mEditor.putBoolean(SEARCH_INIT, false);
            this.mEditor.apply();
            if (this.mInitAnimator) {
                this.mIsRestoreFlag = false;
                if (this.mResultContainer != null) {
                    this.mContentLayout.setVisibility(0);
                    this.mResultContainer.setVisibility(8);
                    this.mBackgroundMask.setVisibility(8);
                    this.mResultFlag = false;
                    LocalLog.a(TAG, "onStateChange mResultContainer set false");
                }
                animBack();
                return;
            }
            this.mSearchState = 0;
        }
    }

    public void initBaseSearchView(RecyclerView.h hVar) {
        this.mSearchView = (COUISearchViewAnimate) findViewById(R.id.searchView);
        this.mSearchViewLayout = (COUIPercentWidthFrameLayout) findViewById(R.id.searchViewLayout);
        setSupportActionBar(this.mToolbar);
        this.mBackgroundMask = findViewById(R.id.background_mask_searchView_below_toolbar);
        this.mContainer = findViewById(R.id.container_searchView_below_toolbar);
        TopMarginView topMarginView = new TopMarginView();
        this.mTitleTopMarginview = topMarginView;
        topMarginView.addView(this.mTitleView);
        View view = new View(this);
        this.mHeaderView = view;
        view.setVisibility(4);
        this.mResultContainer = (ViewGroup) findViewById(R.id.resultContainer);
        this.mEmptyContainer = (ViewGroup) findViewById(R.id.emptyContainer);
        this.mEmptyAnimation = (EffectiveAnimationView) findViewById(R.id.empty_animation);
        this.mResultListView = (COUIPercentWidthRecyclerView) findViewById(R.id.resultList);
        this.mResultListView.setLayoutManager(new LinearLayoutManager(this.mContext));
        this.mResultListView.setFocusable(true);
        ViewCompat.y0(this.mResultListView, true);
        this.mResultListView.setAdapter(hVar);
        setSearchBackgroundTouchListener(new View.OnTouchListener() { // from class: com.oplus.powermanager.fuelgaue.base.BaseSearchViewActivity.1
            @Override // android.view.View.OnTouchListener
            public boolean onTouch(View view2, MotionEvent motionEvent) {
                if (motionEvent.getAction() == 0) {
                    if (BaseSearchViewActivity.this.isSearchAnimatorRunning()) {
                        LocalLog.a(BaseSearchViewActivity.TAG, "onTouch: mSearchMarginEnterAnimator.isRunning");
                        return true;
                    }
                    BaseSearchViewActivity.this.changeSearchState(false);
                    LocalLog.a(BaseSearchViewActivity.TAG, "onTouch: mSearchView.changeStateWithAnimation");
                }
                return true;
            }
        });
        this.mSearchView.setIconCanAnimate(false);
        this.mSearchView.getFunctionalButton().setOnClickListener(new View.OnClickListener() { // from class: com.oplus.powermanager.fuelgaue.base.b
            @Override // android.view.View.OnClickListener
            public final void onClick(View view2) {
                BaseSearchViewActivity.this.lambda$initBaseSearchView$0(view2);
            }
        });
        this.mSearchView.setOnClickListener(new View.OnClickListener() { // from class: com.oplus.powermanager.fuelgaue.base.c
            @Override // android.view.View.OnClickListener
            public final void onClick(View view2) {
                BaseSearchViewActivity.this.lambda$initBaseSearchView$1(view2);
            }
        });
        this.mCubicBezierEnterInterpolator = new COUIMoveEaseInterpolator();
        this.mCubicBezierExitInterpolator = new COUIMoveEaseInterpolator();
        StatusBarUtil.setStatusBarTransparentAndBlackFont(this);
        final View W0 = f.W0(this);
        this.mAppBarLayout.addView(W0, 0, W0.getLayoutParams());
        this.mStatusBarHeight = SystemBarUtils.getStatusBarHeight(this);
        this.mAppBarLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() { // from class: com.oplus.powermanager.fuelgaue.base.BaseSearchViewActivity.2
            @Override // android.view.ViewTreeObserver.OnGlobalLayoutListener
            public void onGlobalLayout() {
                if (f.o1(BaseSearchViewActivity.this) && f.g1(BaseSearchViewActivity.this)) {
                    Rect rect = new Rect();
                    BaseSearchViewActivity.this.getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
                    int i10 = rect.top;
                    if (i10 != 0) {
                        BaseSearchViewActivity.this.mStatusBarHeight = i10;
                        W0.setLayoutParams(new AppBarLayout.LayoutParams(-1, BaseSearchViewActivity.this.mStatusBarHeight));
                    }
                }
                BaseSearchViewActivity baseSearchViewActivity = BaseSearchViewActivity.this;
                baseSearchViewActivity.mContainerTopPadding = baseSearchViewActivity.mAppBarLayout.getMeasuredHeight();
                LocalLog.a(BaseSearchViewActivity.TAG, "mContainerTopPadding " + BaseSearchViewActivity.this.mContainerTopPadding + " statusHeight: " + BaseSearchViewActivity.this.mStatusBarHeight);
                BaseSearchViewActivity.this.mContainer.setPadding(0, BaseSearchViewActivity.this.mContainerTopPadding, 0, 0);
                BaseSearchViewActivity.this.mResultContainer.setPadding(0, BaseSearchViewActivity.this.mStatusBarHeight + BaseSearchViewActivity.this.mToolbar.getHeight(), 0, 0);
                BaseSearchViewActivity baseSearchViewActivity2 = BaseSearchViewActivity.this;
                baseSearchViewActivity2.mHeaderViewInitHeight = baseSearchViewActivity2.mContainerTopPadding + BaseSearchViewActivity.this.getResources().getDimensionPixelOffset(R.dimen.list_to_ex_top_padding);
                BaseSearchViewActivity.this.mHeaderView.setLayoutParams(new RecyclerView.LayoutParams(-1, BaseSearchViewActivity.this.mHeaderViewInitHeight));
                BaseSearchViewActivity.this.initAnimators();
                BaseSearchViewActivity.this.doInitSearchState();
                BaseSearchViewActivity.this.handleGlobalLayout();
                BaseSearchViewActivity.this.mSearchView.setSearchViewAnimateHeightPercent(BaseSearchViewActivity.TITLE_ALPHA);
                BaseSearchViewActivity.this.mAppBarLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
        addSearchStateChangeListener(new COUISearchViewAnimate.u() { // from class: com.oplus.powermanager.fuelgaue.base.BaseSearchViewActivity.3
            @Override // com.coui.appcompat.searchview.COUISearchViewAnimate.u
            public void onStateChange(int i10, int i11) {
                LocalLog.a(BaseSearchViewActivity.TAG, "search onStateChange " + i11);
                BaseSearchViewActivity.this.handleSearchStateChange(i11);
            }
        });
    }

    public void initBaseView() {
        this.mStatusBarUtil = new COUIStatusBarResponseUtil(this);
        this.mAppBarLayout = (AppBarLayout) findViewById(R.id.power_usage_appBarLayout);
        COUIToolbar cOUIToolbar = (COUIToolbar) findViewById(R.id.power_usage_layout_toolbar);
        this.mToolbar = cOUIToolbar;
        cOUIToolbar.setTitle(getString(R.string.power_control_title));
        this.mTitleView = (TextView) findViewById(R.id.toolbar_title);
        this.mToolbar.setTitleTextColor(getResources().getColor(R.color.coui_color_primary_neutral));
        CoordinatorLayout.e eVar = (CoordinatorLayout.e) this.mAppBarLayout.getLayoutParams();
        this.mLayoutParams = eVar;
        this.mBaseBehavior = eVar.f();
        setSupportActionBar(this.mToolbar);
        onGetActionBar().s(true);
        this.mContentLayout = findViewById(R.id.content_layout);
    }

    public boolean isInSearchStatus() {
        return this.mSharedPreferences.getInt(SEARCH_STATUS, 0) != 0;
    }

    public boolean isSearchAnimatorRunning() {
        ObjectAnimator objectAnimator = this.mSearchMarginEnterAnimator;
        return objectAnimator != null && objectAnimator.isRunning();
    }

    @Override // androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, android.view.ComponentActivity, android.app.Activity, android.content.ComponentCallbacks
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        if (this.mEmptyContainer != null) {
            this.mEmptyContainer.setPadding(0, this.mContext.getResources().getDimensionPixelOffset(R.dimen.search_empty_layout_padding_top), 0, 0);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.oplus.powermanager.fuelgaue.base.BaseAppCompatActivity, androidx.fragment.app.FragmentActivity, android.view.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        LocalLog.a(TAG, "onCreate");
        super.onCreate(bundle);
        COUIThemeOverlay.i().b(this);
        StatusBarUtil.setStatusBarTransparentAndBlackFont(this);
        Context applicationContext = getApplicationContext();
        this.mContext = applicationContext;
        SharedPreferences sharedPreferences = applicationContext.getSharedPreferences(SAVE_SEARCH_STATUS, 0);
        this.mSharedPreferences = sharedPreferences;
        SharedPreferences.Editor edit = sharedPreferences.edit();
        this.mEditor = edit;
        edit.putInt(SEARCH_STATUS, 0);
        this.mEditor.putBoolean(SEARCH_INIT, false);
        this.mEditor.apply();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onDestroy() {
        super.onDestroy();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onPause() {
        LocalLog.a(TAG, "onPause");
        super.onPause();
        this.mStatusBarUtil.e();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onResume() {
        LocalLog.a(TAG, "onResume");
        super.onResume();
        this.mStatusBarUtil.f();
        COUISearchViewAnimate cOUISearchViewAnimate = this.mSearchView;
        if (cOUISearchViewAnimate != null) {
            if (cOUISearchViewAnimate.getSearchView().getSearchAutoComplete().isFocused()) {
                this.mSearchView.L(1);
            } else if (this.mSharedPreferences.getBoolean(SEARCH_INIT, false)) {
                this.mSearchView.L(1);
                this.mEditor.putBoolean(SEARCH_INIT, false);
                this.mEditor.apply();
            }
        }
        if (this.mResultFlag) {
            this.mContentLayout.setVisibility(8);
            this.mBackgroundMask.setVisibility(8);
            this.mResultContainer.setVisibility(0);
            LocalLog.a(TAG, "resume set true");
        }
    }

    public void setAnimBackListener(AnimatorListenerAdapter animatorListenerAdapter) {
        this.mAnimBackListener = animatorListenerAdapter;
    }

    public void setAnimEnterListener(AnimatorListenerAdapter animatorListenerAdapter) {
        this.mAnimEnterListener = animatorListenerAdapter;
    }

    public void setSearchBackgroundTouchListener(View.OnTouchListener onTouchListener) {
        this.mBackgroundMask.setOnTouchListener(onTouchListener);
    }

    public void setSearchQueryListener(SearchView.m mVar) {
        this.mSearchView.getSearchView().setOnQueryTextListener(mVar);
    }

    public void setStatusBarClickListener(COUIStatusBarResponseUtil.c cVar) {
        this.mStatusBarUtil.g(cVar);
    }
}
