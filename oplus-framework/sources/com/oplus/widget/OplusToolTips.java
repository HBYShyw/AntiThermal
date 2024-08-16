package com.oplus.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.TouchDelegate;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.Interpolator;
import android.view.animation.PathInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.TextView;
import com.oplus.internal.R;
import com.oplus.util.OplusChangeTextUtil;
import com.oplus.util.ViewGroupUtils;

/* loaded from: classes.dex */
public class OplusToolTips extends PopupWindow {
    public static final int ALIGN_BOTTOM = 128;
    public static final int ALIGN_END = 64;
    public static final int ALIGN_LEFT = 16;
    public static final int ALIGN_RIGHT = 8;
    public static final int ALIGN_START = 32;
    public static final int ANIMATION_DURATION = 300;
    public static final int DEFAULT_ALIGN_DIRECTION = 4;
    private static final float ONE = 1.0f;
    private static final float POINT_EIGHT = 0.8f;
    private static final float POINT_FIVE = 0.5f;
    private static final float ZERO = 0.0f;
    private View mAnchor;
    private Interpolator mAnimationInterpolator;
    private Drawable mArrowDownDrawable;
    private Drawable mArrowLeftDrawable;
    private int mArrowOverflow;
    private Drawable mArrowRightDrawable;
    private Drawable mArrowUpDrawable;
    private ImageView mArrowView;
    private ViewGroup mContentContainer;
    private Rect mContentRectOnScreen;
    private TextView mContentTv;
    private final Context mContext;
    private boolean mIsDismissing;
    private ViewGroup mMainPanel;
    private View mParent;
    private Rect mParentRectOnScreen;
    private float mPivotX;
    private float mPivotY;
    private ScrollView mScrollView;
    private final int[] mTmpCoords = new int[2];
    private final Point mCoordsOnWindow = new Point();
    private final Rect mViewPortOnScreen = new Rect();
    private final View.OnLayoutChangeListener mOnLayoutChangeListener = new View.OnLayoutChangeListener() { // from class: com.oplus.widget.OplusToolTips.1
        @Override // android.view.View.OnLayoutChangeListener
        public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
            Rect newRect = new Rect(left, top, right, bottom);
            Rect oldRect = new Rect(oldLeft, oldTop, oldRight, oldBottom);
            if (OplusToolTips.this.isShowing() && !newRect.equals(oldRect) && OplusToolTips.this.mAnchor != null) {
                OplusToolTips.this.dismissPopupWindow();
            }
        }
    };
    private boolean mLeftOrTop = false;
    private int mShowDirection = 4;
    private int[] mWindowLocationOnScreen = new int[2];
    private PopupWindow.OnDismissListener mOnPopupWindowDismissListener = new PopupWindow.OnDismissListener() { // from class: com.oplus.widget.OplusToolTips.2
        @Override // android.widget.PopupWindow.OnDismissListener
        public void onDismiss() {
            OplusToolTips.this.mContentContainer.removeAllViews();
        }
    };

    public OplusToolTips(Context context) {
        this.mContext = context;
        init();
    }

    private void init() {
        TypedArray a = this.mContext.obtainStyledAttributes(null, R.styleable.OplusToolTips, 201392187, 201523245);
        Drawable backgroundDrawable = a.getDrawable(5);
        backgroundDrawable.setDither(true);
        this.mArrowUpDrawable = a.getDrawable(4);
        this.mArrowDownDrawable = a.getDrawable(0);
        this.mArrowLeftDrawable = a.getDrawable(1);
        this.mArrowRightDrawable = a.getDrawable(3);
        this.mArrowOverflow = a.getDimensionPixelSize(2, 0);
        int minWidth = a.getDimensionPixelSize(12, 0);
        int containerLayoutGravity = a.getInt(6, 0);
        int containerLayoutMarginStart = a.getDimensionPixelSize(9, 0);
        int containerLayoutMarginTop = a.getDimensionPixelSize(10, 0);
        int containerLayoutMarginEnd = a.getDimensionPixelSize(8, 0);
        int containerLayoutMarginBottom = a.getDimensionPixelSize(7, 0);
        ColorStateList contentTextColor = a.getColorStateList(11);
        final int insect = this.mContext.getResources().getDimensionPixelOffset(201654498);
        a.recycle();
        this.mAnimationInterpolator = new OplusMoveEaseInterpolator();
        ViewGroup viewGroup = (ViewGroup) LayoutInflater.from(this.mContext).inflate(201917501, (ViewGroup) null);
        this.mMainPanel = viewGroup;
        viewGroup.setBackground(backgroundDrawable);
        this.mMainPanel.setMinimumWidth(minWidth);
        ViewGroup createContentContainer = createContentContainer(this.mContext);
        this.mContentContainer = createContentContainer;
        createContentContainer.setForceDarkAllowed(false);
        TextView textView = (TextView) this.mMainPanel.findViewById(201457838);
        this.mContentTv = textView;
        textView.setMovementMethod(LinkMovementMethod.getInstance());
        ScrollView scrollView = (ScrollView) this.mMainPanel.findViewById(201457839);
        this.mScrollView = scrollView;
        FrameLayout.LayoutParams scrollViewLayoutParams = (FrameLayout.LayoutParams) scrollView.getLayoutParams();
        scrollViewLayoutParams.gravity = containerLayoutGravity;
        scrollViewLayoutParams.setMargins(containerLayoutMarginStart, containerLayoutMarginTop, containerLayoutMarginEnd, containerLayoutMarginBottom);
        scrollViewLayoutParams.setMarginStart(containerLayoutMarginStart);
        scrollViewLayoutParams.setMarginEnd(containerLayoutMarginEnd);
        this.mScrollView.setLayoutParams(scrollViewLayoutParams);
        float fontScale = this.mContext.getResources().getConfiguration().fontScale;
        int contentTextSize = this.mContext.getResources().getDimensionPixelSize(201654500);
        this.mContentTv.setTextSize(0, (int) OplusChangeTextUtil.getSuitableFontSize(contentTextSize, fontScale, 4));
        this.mContentTv.setTextColor(contentTextColor);
        final ImageView dismissIv = (ImageView) this.mMainPanel.findViewById(201457840);
        dismissIv.setVisibility(0);
        dismissIv.setOnClickListener(new View.OnClickListener() { // from class: com.oplus.widget.OplusToolTips$$ExternalSyntheticLambda0
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                OplusToolTips.this.lambda$init$0(view);
            }
        });
        dismissIv.post(new Runnable() { // from class: com.oplus.widget.OplusToolTips$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                OplusToolTips.this.lambda$init$1(dismissIv, insect);
            }
        });
        setClippingEnabled(false);
        setAnimationStyle(0);
        setBackgroundDrawable(new ColorDrawable(0));
        setOnDismissListener(this.mOnPopupWindowDismissListener);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$init$0(View v) {
        dismiss();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$init$1(ImageView dismissIv, int insect) {
        Rect rect = new Rect();
        ViewGroupUtils.getDescendantRect(this.mMainPanel, dismissIv, rect);
        rect.inset(-insect, -insect);
        this.mMainPanel.setTouchDelegate(new TouchDelegate(rect, dismissIv));
    }

    public void refresh() {
        TypedArray a = this.mContext.obtainStyledAttributes(null, R.styleable.OplusToolTips, 201392187, 201523245);
        Drawable backgroundDrawable = a.getDrawable(5);
        backgroundDrawable.setDither(true);
        this.mArrowUpDrawable = a.getDrawable(4);
        this.mArrowDownDrawable = a.getDrawable(0);
        this.mArrowLeftDrawable = a.getDrawable(1);
        this.mArrowRightDrawable = a.getDrawable(3);
        this.mArrowOverflow = a.getDimensionPixelSize(2, 0);
        ColorStateList contentTextColor = a.getColorStateList(11);
        a.recycle();
        this.mMainPanel.setBackground(backgroundDrawable);
        this.mContentTv.setTextColor(contentTextColor);
        int i = this.mShowDirection;
        if (i == 4 || i == 128) {
            this.mArrowView.setBackground(this.mLeftOrTop ? this.mArrowUpDrawable : this.mArrowDownDrawable);
        } else {
            this.mArrowView.setBackground(this.mLeftOrTop ? this.mArrowRightDrawable : this.mArrowLeftDrawable);
        }
    }

    private static ViewGroup createContentContainer(Context context) {
        FrameLayout contentContainer = new FrameLayout(context);
        contentContainer.setLayoutParams(new ViewGroup.LayoutParams(-2, -2));
        return contentContainer;
    }

    public void show(View anchor) {
        show(anchor, true);
    }

    public void show(View anchor, boolean hasIndicator) {
        showWithDirection(anchor, 4, hasIndicator);
    }

    public void showWithDirection(View anchor, int direction) {
        showWithDirection(anchor, direction, true);
    }

    public void showWithDirection(View anchor, int direction, boolean hasIndicator) {
        showWithDirection(anchor, direction, hasIndicator, 0, 0);
    }

    public void showWithDirection(View anchor, int direction, boolean hasIndicator, int offsetX, int offsetY) {
        if (isShowing()) {
            return;
        }
        this.mShowDirection = direction;
        this.mParent = anchor.getRootView();
        int i = this.mShowDirection;
        if (i == 32 || i == 64) {
            if (isLayoutRtl(anchor)) {
                this.mShowDirection = this.mShowDirection == 32 ? 8 : 16;
            } else {
                this.mShowDirection = this.mShowDirection != 32 ? 8 : 16;
            }
        }
        this.mAnchor = anchor;
        this.mParent.getWindowVisibleDisplayFrame(this.mViewPortOnScreen);
        registerOrientationHandler();
        Rect rect = new Rect();
        this.mContentRectOnScreen = rect;
        anchor.getGlobalVisibleRect(rect);
        Rect rect2 = new Rect();
        this.mParentRectOnScreen = rect2;
        this.mParent.getGlobalVisibleRect(rect2);
        int[] rootViewPosition = new int[2];
        this.mParent.getLocationOnScreen(rootViewPosition);
        this.mContentRectOnScreen.offset(rootViewPosition[0], rootViewPosition[1]);
        this.mParentRectOnScreen.offset(rootViewPosition[0], rootViewPosition[1]);
        Rect rect3 = this.mViewPortOnScreen;
        rect3.left = Math.max(rect3.left, this.mParentRectOnScreen.left);
        Rect rect4 = this.mViewPortOnScreen;
        rect4.top = Math.max(rect4.top, this.mParentRectOnScreen.top);
        Rect rect5 = this.mViewPortOnScreen;
        rect5.right = Math.min(rect5.right, this.mParentRectOnScreen.right);
        Rect rect6 = this.mViewPortOnScreen;
        rect6.bottom = Math.min(rect6.bottom, this.mParentRectOnScreen.bottom);
        sizePopupWindow();
        refreshCoordinated(this.mContentRectOnScreen);
        prepareContent(this.mContentRectOnScreen, hasIndicator);
        setContentView(this.mContentContainer);
        calculatePivot();
        animateEnter();
        this.mCoordsOnWindow.x += offsetX;
        this.mCoordsOnWindow.y += offsetY;
        showAtLocation(this.mParent, 0, this.mCoordsOnWindow.x, this.mCoordsOnWindow.y);
    }

    private void prepareContent(Rect contentRectOnScreen, boolean hasIndicator) {
        this.mContentContainer.removeAllViews();
        this.mContentContainer.addView(this.mMainPanel);
        if (hasIndicator) {
            addIndicator(contentRectOnScreen);
        }
    }

    private void sizePopupWindow() {
        int maxWidth = this.mContext.getResources().getDimensionPixelSize(201654499) + this.mMainPanel.getPaddingLeft() + this.mMainPanel.getPaddingRight();
        int i = this.mShowDirection;
        if (i == 8) {
            maxWidth = Math.min(this.mViewPortOnScreen.right - this.mContentRectOnScreen.right, maxWidth);
        } else if (i == 16) {
            maxWidth = Math.min(this.mContentRectOnScreen.left - this.mViewPortOnScreen.left, maxWidth);
        }
        int maxWidth2 = Math.min(this.mViewPortOnScreen.right - this.mViewPortOnScreen.left, maxWidth);
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) this.mScrollView.getLayoutParams();
        this.mContentTv.setMaxWidth((((maxWidth2 - this.mMainPanel.getPaddingLeft()) - this.mMainPanel.getPaddingRight()) - params.leftMargin) - params.rightMargin);
        this.mMainPanel.measure(0, 0);
        setWidth(Math.min(this.mMainPanel.getMeasuredWidth(), maxWidth2));
        setHeight(this.mMainPanel.getMeasuredHeight());
        int availableHeight = (this.mContentRectOnScreen.centerY() - (((getViewportHeight() + this.mMainPanel.getPaddingTop()) - this.mMainPanel.getPaddingBottom()) / 2)) + getViewportHeight();
        if (availableHeight >= this.mViewPortOnScreen.bottom) {
            this.mShowDirection = 4;
            int maxWidth3 = Math.min(this.mViewPortOnScreen.right - this.mViewPortOnScreen.left, this.mContext.getResources().getDimensionPixelSize(201654499) + this.mMainPanel.getPaddingLeft() + this.mMainPanel.getPaddingRight());
            this.mContentTv.setMaxWidth((((maxWidth3 - this.mMainPanel.getPaddingLeft()) - this.mMainPanel.getPaddingRight()) - params.leftMargin) - params.rightMargin);
            this.mMainPanel.measure(0, 0);
            setWidth(Math.min(this.mMainPanel.getMeasuredWidth(), maxWidth3));
            setHeight(this.mMainPanel.getMeasuredHeight());
        }
    }

    private void addIndicator(Rect contentRectOnScreen) {
        this.mArrowView = new ImageView(this.mContext);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(-2, -2);
        int i = this.mShowDirection;
        if (i == 4 || i == 128) {
            this.mParent.getRootView().getLocationOnScreen(this.mTmpCoords);
            int rootViewLeftOnScreen = this.mTmpCoords[0];
            this.mParent.getRootView().getLocationInWindow(this.mTmpCoords);
            int rootViewLeftOnWindow = this.mTmpCoords[0];
            int windowLeftOnScreen = rootViewLeftOnScreen - rootViewLeftOnWindow;
            params.leftMargin = ((contentRectOnScreen.centerX() - this.mCoordsOnWindow.x) - windowLeftOnScreen) - (this.mArrowUpDrawable.getIntrinsicWidth() / 2);
            params.rightMargin = (getWidth() - params.leftMargin) - this.mArrowUpDrawable.getIntrinsicWidth();
            int contentRectTopOnWindow = contentRectOnScreen.top - this.mWindowLocationOnScreen[1];
            if (this.mCoordsOnWindow.y >= contentRectTopOnWindow) {
                this.mArrowView.setBackground(this.mArrowUpDrawable);
                this.mLeftOrTop = true;
                params.topMargin = (this.mMainPanel.getPaddingTop() - this.mArrowUpDrawable.getIntrinsicHeight()) + this.mArrowOverflow;
            } else {
                this.mArrowView.setBackground(this.mArrowDownDrawable);
                params.gravity = 80;
                params.bottomMargin = (this.mMainPanel.getPaddingBottom() - this.mArrowDownDrawable.getIntrinsicHeight()) + this.mArrowOverflow;
            }
        } else if (i == 16) {
            this.mLeftOrTop = true;
            params.rightMargin = (this.mMainPanel.getPaddingRight() - this.mArrowRightDrawable.getIntrinsicWidth()) + this.mArrowOverflow;
            params.leftMargin = (getWidth() - params.rightMargin) - this.mArrowRightDrawable.getIntrinsicWidth();
            params.topMargin = ((contentRectOnScreen.centerY() - this.mCoordsOnWindow.y) - this.mWindowLocationOnScreen[1]) - (this.mArrowRightDrawable.getIntrinsicHeight() / 2);
            params.bottomMargin = (getHeight() - params.topMargin) - this.mArrowRightDrawable.getIntrinsicHeight();
            this.mArrowView.setBackground(this.mArrowRightDrawable);
        } else {
            params.leftMargin = (this.mMainPanel.getPaddingLeft() - this.mArrowLeftDrawable.getIntrinsicWidth()) + this.mArrowOverflow;
            params.rightMargin = (getWidth() - params.leftMargin) - this.mArrowLeftDrawable.getIntrinsicWidth();
            params.topMargin = ((contentRectOnScreen.centerY() - this.mCoordsOnWindow.y) - this.mWindowLocationOnScreen[1]) - (this.mArrowRightDrawable.getIntrinsicHeight() / 2);
            params.bottomMargin = (getHeight() - params.topMargin) - this.mArrowRightDrawable.getIntrinsicHeight();
            this.mArrowView.setBackground(this.mArrowLeftDrawable);
        }
        this.mContentContainer.addView(this.mArrowView, params);
    }

    private void refreshCoordinated(Rect contentRectOnScreen) {
        int x;
        int y;
        int i = this.mShowDirection;
        if (i == 4) {
            x = Math.min(contentRectOnScreen.centerX() - (getViewportWidth() / 2), this.mViewPortOnScreen.right - getViewportWidth());
            int availableHeightAboveContent = contentRectOnScreen.top - this.mViewPortOnScreen.top;
            int availableHeightBelowContent = this.mViewPortOnScreen.bottom - contentRectOnScreen.bottom;
            int viewportHeight = getViewportHeight();
            if (availableHeightAboveContent >= viewportHeight) {
                y = contentRectOnScreen.top - viewportHeight;
            } else if (availableHeightBelowContent >= viewportHeight) {
                y = contentRectOnScreen.bottom;
            } else if (availableHeightAboveContent > availableHeightBelowContent) {
                y = this.mViewPortOnScreen.top;
                setHeight(availableHeightAboveContent);
            } else {
                y = contentRectOnScreen.bottom;
                setHeight(availableHeightBelowContent);
            }
        } else if (i == 128) {
            x = Math.min(contentRectOnScreen.centerX() - (getViewportWidth() / 2), this.mViewPortOnScreen.right - getViewportWidth());
            int availableHeightAboveContent2 = contentRectOnScreen.top - this.mViewPortOnScreen.top;
            int availableHeightBelowContent2 = this.mViewPortOnScreen.bottom - contentRectOnScreen.bottom;
            int viewportHeight2 = getViewportHeight();
            if (availableHeightBelowContent2 >= viewportHeight2) {
                y = contentRectOnScreen.bottom;
            } else if (availableHeightAboveContent2 >= viewportHeight2) {
                y = contentRectOnScreen.top - viewportHeight2;
            } else if (availableHeightAboveContent2 > availableHeightBelowContent2) {
                y = this.mViewPortOnScreen.top;
                setHeight(availableHeightAboveContent2);
            } else {
                y = contentRectOnScreen.bottom;
                setHeight(availableHeightBelowContent2);
            }
        } else {
            x = i == 16 ? contentRectOnScreen.left - getViewportWidth() : contentRectOnScreen.right;
            y = contentRectOnScreen.centerY() - (((getViewportHeight() + this.mMainPanel.getPaddingTop()) - this.mMainPanel.getPaddingBottom()) / 2);
        }
        this.mParent.getRootView().getLocationOnScreen(this.mTmpCoords);
        int[] iArr = this.mTmpCoords;
        int rootViewLeftOnScreen = iArr[0];
        int rootViewTopOnScreen = iArr[1];
        this.mParent.getRootView().getLocationInWindow(this.mTmpCoords);
        int[] iArr2 = this.mTmpCoords;
        int rootViewLeftOnWindow = iArr2[0];
        int rootViewTopOnWindow = iArr2[1];
        int[] iArr3 = this.mWindowLocationOnScreen;
        int i2 = rootViewLeftOnScreen - rootViewLeftOnWindow;
        iArr3[0] = i2;
        iArr3[1] = rootViewTopOnScreen - rootViewTopOnWindow;
        this.mCoordsOnWindow.set(Math.max(0, x - i2), Math.max(0, y - this.mWindowLocationOnScreen[1]));
    }

    private int getViewportWidth() {
        return getWidth();
    }

    private int getViewportHeight() {
        return getHeight();
    }

    public void setContent(CharSequence content) {
        this.mContentTv.setText(content);
    }

    public void setTextDirection(int textDirection) {
        this.mContentTv.setTextDirection(textDirection);
    }

    public void setContent(View view) {
        this.mScrollView.removeAllViews();
        this.mScrollView.addView(view);
    }

    private void calculatePivot() {
        int i = this.mShowDirection;
        if (i == 4) {
            if ((this.mContentRectOnScreen.centerX() - this.mWindowLocationOnScreen[0]) - this.mCoordsOnWindow.x >= getViewportWidth()) {
                this.mPivotX = 1.0f;
            } else if (getViewportWidth() != 0) {
                int scale = (this.mContentRectOnScreen.centerX() - this.mWindowLocationOnScreen[0]) - this.mCoordsOnWindow.x;
                this.mPivotX = (scale > 0 ? scale : -scale) / getViewportWidth();
            } else {
                this.mPivotX = 0.5f;
            }
            if (this.mCoordsOnWindow.y >= this.mContentRectOnScreen.top - this.mWindowLocationOnScreen[1]) {
                this.mPivotY = 0.0f;
                return;
            } else {
                this.mPivotY = 1.0f;
                return;
            }
        }
        this.mPivotX = i == 16 ? 1.0f : 0.0f;
        this.mPivotY = ((this.mContentRectOnScreen.centerY() - this.mCoordsOnWindow.y) - this.mWindowLocationOnScreen[1]) / getViewportHeight();
    }

    private void animateEnter() {
        Animation scaleAnimation = new ScaleAnimation(POINT_EIGHT, 1.0f, POINT_EIGHT, 1.0f, 1, this.mPivotX, 1, this.mPivotY);
        Animation alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
        AnimationSet animationSet = new AnimationSet(true);
        animationSet.setInterpolator(this.mAnimationInterpolator);
        animationSet.setDuration(300L);
        animationSet.addAnimation(scaleAnimation);
        animationSet.addAnimation(alphaAnimation);
        animationSet.setAnimationListener(new Animation.AnimationListener() { // from class: com.oplus.widget.OplusToolTips.3
            @Override // android.view.animation.Animation.AnimationListener
            public void onAnimationStart(Animation animation) {
            }

            @Override // android.view.animation.Animation.AnimationListener
            public void onAnimationEnd(Animation animation) {
            }

            @Override // android.view.animation.Animation.AnimationListener
            public void onAnimationRepeat(Animation animation) {
            }
        });
        this.mContentContainer.startAnimation(animationSet);
    }

    private void animateExit() {
        Animation alphaAnimation = new AlphaAnimation(1.0f, 0.0f);
        Animation scaleAnimation = new ScaleAnimation(1.0f, POINT_EIGHT, 1.0f, POINT_EIGHT, 1, this.mPivotX, 1, this.mPivotY);
        AnimationSet animationSet = new AnimationSet(true);
        animationSet.setDuration(300L);
        animationSet.setInterpolator(this.mAnimationInterpolator);
        animationSet.addAnimation(alphaAnimation);
        animationSet.addAnimation(scaleAnimation);
        animationSet.setAnimationListener(new Animation.AnimationListener() { // from class: com.oplus.widget.OplusToolTips.4
            @Override // android.view.animation.Animation.AnimationListener
            public void onAnimationStart(Animation animation) {
                OplusToolTips.this.mIsDismissing = true;
            }

            @Override // android.view.animation.Animation.AnimationListener
            public void onAnimationEnd(Animation animation) {
                OplusToolTips.this.dismissPopupWindow();
                OplusToolTips.this.mIsDismissing = false;
            }

            @Override // android.view.animation.Animation.AnimationListener
            public void onAnimationRepeat(Animation animation) {
            }
        });
        this.mContentContainer.startAnimation(animationSet);
    }

    @Override // android.widget.PopupWindow
    public void dismiss() {
        if (!this.mIsDismissing) {
            animateExit();
        } else {
            dismissPopupWindow();
            this.mIsDismissing = false;
        }
    }

    public void dismissNoAnimation() {
        dismissPopupWindow();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void dismissPopupWindow() {
        super.dismiss();
        unregisterOrientationHandler();
        this.mContentContainer.removeAllViews();
    }

    public void setDismissOnTouchOutside(boolean cancel) {
        if (cancel) {
            setTouchable(true);
            setFocusable(true);
            setOutsideTouchable(true);
        } else {
            setFocusable(false);
            setOutsideTouchable(false);
        }
        update();
    }

    private void registerOrientationHandler() {
        unregisterOrientationHandler();
        this.mParent.addOnLayoutChangeListener(this.mOnLayoutChangeListener);
    }

    private void unregisterOrientationHandler() {
        this.mParent.removeOnLayoutChangeListener(this.mOnLayoutChangeListener);
    }

    public boolean isLayoutRtl(View view) {
        return view.getLayoutDirection() == 1;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class OplusMoveEaseInterpolator extends PathInterpolator {
        private static final float CONTROL_X1 = 0.3f;
        private static final float CONTROL_X2 = 0.1f;
        private static final float CONTROL_Y1 = 0.0f;
        private static final float CONTROL_Y2 = 1.0f;

        public OplusMoveEaseInterpolator() {
            super(0.3f, 0.0f, 0.1f, 1.0f);
        }
    }
}
