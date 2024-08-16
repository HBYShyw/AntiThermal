package com.oplus.resolver.widget;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import com.android.internal.widget.ResolverDrawerLayout;
import com.oplus.internal.R;
import com.oplus.resolver.OplusResolverDialogHelper;
import com.oplus.resolver.OplusResolverUtils;

/* loaded from: classes.dex */
public class OplusResolverDrawerLayout extends ResolverDrawerLayout implements OplusResolverDialogHelper.OnProfileSelectedListener {
    private static final int DEFAULT_CHECK_SCREEN_WIDTH = 640;
    private static final String TAG = "OplusResolverDrawerLayout";
    private int mCurrentPager;
    private int mDefaultMaxHeight;
    private int mDefaultMaxWidth;
    private boolean mIsCenterShow;
    private boolean mIsUserMaxHeight;
    private int mLandscapeMaxHeight;
    private int mLandscapeMaxWidth;
    private int mOffsetX;
    private int mOffsetY;
    private SparseIntArray mPagerHeight;
    private int mPortraitMaxHeight;
    private int mPortraitMaxWidth;
    private View mSpaceView;
    private View mViewBottom;

    public OplusResolverDrawerLayout(Context context) {
        this(context, null);
    }

    public OplusResolverDrawerLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public OplusResolverDrawerLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mPagerHeight = new SparseIntArray();
        this.mIsCenterShow = false;
        this.mOffsetX = -1;
        this.mOffsetY = -1;
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.OplusResolverDrawerLayout);
        this.mPortraitMaxWidth = typedArray.getDimensionPixelSize(0, -1);
        int dimensionPixelSize = typedArray.getDimensionPixelSize(1, -1);
        this.mLandscapeMaxWidth = dimensionPixelSize;
        this.mDefaultMaxWidth = dimensionPixelSize;
        typedArray.recycle();
        TypedArray typedArray2 = context.obtainStyledAttributes(attrs, R.styleable.OplusMaxLinearLayout);
        this.mPortraitMaxHeight = typedArray2.getDimensionPixelSize(0, 0);
        this.mLandscapeMaxHeight = typedArray2.getDimensionPixelSize(1, 0);
        this.mDefaultMaxHeight = this.mPortraitMaxHeight;
        typedArray2.recycle();
        View view = new View(getContext());
        this.mSpaceView = view;
        view.setTag("spaceView");
        this.mSpaceView.setLayoutParams(new ResolverDrawerLayout.LayoutParams(-2, 0));
        this.mSpaceView.setBackgroundResource(201719864);
        boolean calculateNotSmallScreen = OplusResolverUtils.calculateNotSmallScreen(context, getResources().getConfiguration());
        this.mIsCenterShow = calculateNotSmallScreen;
        if (calculateNotSmallScreen) {
            this.mSpaceView.setBackgroundResource(android.R.color.transparent);
        }
    }

    public void setIsUserMaxHeight(boolean isUserMaxHeight, View viewBottom) {
        this.mIsUserMaxHeight = isUserMaxHeight;
        this.mViewBottom = viewBottom;
    }

    protected void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        boolean calculateNotSmallScreen = OplusResolverUtils.calculateNotSmallScreen(this.mContext, newConfig);
        this.mIsCenterShow = calculateNotSmallScreen;
        if (calculateNotSmallScreen) {
            this.mSpaceView.setBackgroundResource(android.R.color.transparent);
        }
    }

    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (getContext() instanceof Activity) {
            updateNavigationBarColor(this.mIsCenterShow, ((Activity) getContext()).isInMultiWindowMode());
        }
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int sourceWidth = View.MeasureSpec.getSize(widthMeasureSpec);
        int widthSpec = widthMeasureSpec;
        Point screenSize = getScreenSize();
        boolean port = screenSize.x < screenSize.y;
        if ((port && this.mPortraitMaxWidth == -1) || (!port && this.mLandscapeMaxWidth == -1)) {
            super.onMeasure(widthSpec, heightMeasureSpec);
        } else {
            int width = Math.min(sourceWidth, port ? this.mPortraitMaxWidth : this.mLandscapeMaxWidth);
            widthSpec = View.MeasureSpec.makeMeasureSpec(width, 1073741824);
            super.onMeasure(widthSpec, heightMeasureSpec);
        }
        int width2 = this.mOffsetX;
        if (width2 != -1 && this.mOffsetY != -1) {
            super.onMeasure(widthSpec, heightMeasureSpec);
            setMeasuredDimension(sourceWidth, getMeasuredHeight());
            return;
        }
        int useHeight = 0;
        for (int i = 0; i < getChildCount(); i++) {
            useHeight += getChildAt(i).getMeasuredHeight();
        }
        if (this.mSpaceView.getParent() != null) {
            useHeight -= this.mSpaceView.getMeasuredHeight();
        }
        int totalHeight = getMeasuredHeight();
        if (this.mIsUserMaxHeight) {
            this.mPagerHeight.put(this.mCurrentPager, useHeight);
            if (this.mPagerHeight.size() > 1) {
                int maxHeight = 0;
                for (int i2 = 0; i2 < this.mPagerHeight.size(); i2++) {
                    int value = this.mPagerHeight.valueAt(i2);
                    if (maxHeight < value) {
                        maxHeight = value;
                    }
                }
                if (this.mIsCenterShow && this.mViewBottom == null) {
                    this.mSpaceView.getLayoutParams().height = (maxHeight - useHeight) + (totalHeight > maxHeight ? (totalHeight - maxHeight) / 2 : 0);
                    if (this.mSpaceView.getParent() == null) {
                        View view = this.mViewBottom;
                        if (view != null && (view.getParent() instanceof ViewGroup)) {
                            int index = ((ViewGroup) this.mViewBottom.getParent()).indexOfChild(this.mViewBottom);
                            ((ViewGroup) this.mViewBottom.getParent()).addView(this.mSpaceView, index + 1);
                        } else {
                            addView(this.mSpaceView);
                        }
                    }
                } else if (useHeight < maxHeight) {
                    this.mSpaceView.getLayoutParams().height = maxHeight - useHeight;
                    if (this.mSpaceView.getParent() == null) {
                        View view2 = this.mViewBottom;
                        if (view2 != null && (view2.getParent() instanceof ViewGroup)) {
                            int index2 = ((ViewGroup) this.mViewBottom.getParent()).indexOfChild(this.mViewBottom);
                            ((ViewGroup) this.mViewBottom.getParent()).addView(this.mSpaceView, index2 + 1);
                        } else {
                            addView(this.mSpaceView);
                        }
                    }
                } else {
                    View view3 = this.mViewBottom;
                    if (view3 != null && (view3.getParent() instanceof ViewGroup)) {
                        ((ViewGroup) this.mViewBottom.getParent()).removeView(this.mSpaceView);
                    } else {
                        removeView(this.mSpaceView);
                    }
                }
            }
        } else if (this.mIsCenterShow && this.mViewBottom == null) {
            this.mSpaceView.getLayoutParams().height = totalHeight > useHeight ? (totalHeight - useHeight) / 2 : 0;
            if (this.mSpaceView.getParent() == null) {
                View view4 = this.mViewBottom;
                if (view4 != null && (view4.getParent() instanceof ViewGroup)) {
                    int index3 = ((ViewGroup) this.mViewBottom.getParent()).indexOfChild(this.mViewBottom);
                    ((ViewGroup) this.mViewBottom.getParent()).addView(this.mSpaceView, index3 + 1);
                } else {
                    addView(this.mSpaceView);
                }
            }
        } else if (this.mSpaceView.getParent() != null) {
            View view5 = this.mViewBottom;
            if (view5 != null && (view5.getParent() instanceof ViewGroup)) {
                ((ViewGroup) this.mViewBottom.getParent()).removeView(this.mSpaceView);
            } else {
                removeView(this.mSpaceView);
            }
        }
        super.onMeasure(widthSpec, heightMeasureSpec);
        setMeasuredDimension(sourceWidth, getMeasuredHeight());
    }

    public void onNestedScrollAccepted(View child, View target, int axes) {
        super.onNestedScrollAccepted(child, target, 0);
    }

    public void updateAnchorCoordinate(int x, int y, int anchorHeight, int anchorWidth) {
        this.mOffsetX = x;
        this.mOffsetY = y;
        getWrapper().getResolverDrawerLayoutExt().setOffsetXY(x, y, anchorHeight, anchorWidth);
    }

    public void setConfiguration(boolean isMainScreen, Configuration config, boolean isInMultiWindowMode, boolean enforceTransparency) {
        if (!(getContext() instanceof Activity)) {
            return;
        }
        DisplayMetrics outMetrics = new DisplayMetrics();
        if (this.mContext.getDisplay() != null) {
            this.mContext.getDisplay().getRealMetrics(outMetrics);
            float density = outMetrics.density;
            if (config.getOplusExtraConfiguration().getScenario() == 3) {
                density = config.densityDpi * 0.00625f;
            }
            int calculateResponsiveUIPanelWidth = (int) (OplusResolverUtils.calculateResponsiveUIPanelWidth((Activity) getContext()) * density);
            this.mPortraitMaxWidth = calculateResponsiveUIPanelWidth;
            this.mLandscapeMaxWidth = calculateResponsiveUIPanelWidth;
        } else {
            this.mPortraitMaxWidth = -1;
            this.mLandscapeMaxWidth = -1;
        }
        updateNavigationBarColor(enforceTransparency, isInMultiWindowMode);
    }

    private void updateNavigationBarColor(boolean enforceTransparency, boolean isInMultiWindowMode) {
        if (!(getContext() instanceof Activity)) {
            return;
        }
        Point screenSize = getScreenSize();
        boolean port = screenSize.x < screenSize.y;
        setMaxCollapsedHeight(port ? this.mPortraitMaxHeight : this.mLandscapeMaxHeight);
        if (enforceTransparency) {
            ((Activity) getContext()).getWindow().setNavigationBarContrastEnforced(false);
        }
        Log.d(TAG, "port: " + port + " isInMultiWindowMode: " + isInMultiWindowMode + " enforceTransparency: " + enforceTransparency);
        int i = this.mOffsetY;
        int i2 = android.R.color.transparent;
        if (i != -1 && this.mOffsetX != -1) {
            ((Activity) getContext()).getWindow().setNavigationBarContrastEnforced(false);
            ((Activity) getContext()).getWindow().setNavigationBarColor(getContext().getColor(android.R.color.transparent));
        } else {
            Window window = ((Activity) getContext()).getWindow();
            Context context = getContext();
            if ((port || isInMultiWindowMode) && !enforceTransparency) {
                i2 = 201719864;
            }
            window.setNavigationBarColor(context.getColor(i2));
        }
        requestLayout();
    }

    private Point getScreenSize() {
        Point point = new Point();
        WindowManager windowManager = (WindowManager) getContext().getSystemService("window");
        windowManager.getDefaultDisplay().getRealSize(point);
        return point;
    }

    @Override // com.oplus.resolver.OplusResolverDialogHelper.OnProfileSelectedListener
    public void onProfileSelected(int profileIndex) {
        this.mCurrentPager = profileIndex;
    }
}
