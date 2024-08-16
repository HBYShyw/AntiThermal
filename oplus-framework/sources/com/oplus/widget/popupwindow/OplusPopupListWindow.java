package com.oplus.widget.popupwindow;

import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.view.animation.ScaleAnimation;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import com.oplus.widget.OplusMaxLinearLayout;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public class OplusPopupListWindow extends PopupWindow implements View.OnLayoutChangeListener {
    private BaseAdapter mAdapter;
    private int mAlphaAnimationDuration;
    private Interpolator mAlphaAnimationInterpolator;
    private View mAnchor;
    private Rect mAnchorRect;
    private Animation.AnimationListener mAnimationListener;
    private Rect mBackgroundPaddingRect;
    private FrameLayout mBottomPanel;
    private LinearLayout mContentPanel;
    private ViewGroup mContentView;
    private Context mContext;
    private Point mCoordinate;
    private BaseAdapter mCustomAdapter;
    private Rect mDecorViewRect;
    private BaseAdapter mDefaultAdapter;
    private boolean mHasHorizontalSpace;
    private boolean mHasVerticalSpace;
    private boolean mIsDismissing;
    private List<PopupListItem> mItemList;
    private OplusMaxLinearLayout mListContainer;
    private ListView mListView;
    private ListView mListViewUsedToMeasure;
    private AdapterView.OnItemClickListener mOnItemClickListener;
    private Rect mParentRectOnScreen;
    private float mPivotX;
    private float mPivotY;
    private int mPopupListMaxHeight;
    private int mPopupListMaxWidth;
    private int mPopupListSelection;
    private int mPopupListSelectionScrollOffset;
    private int mPopupListWindowMinWidth;
    private int[] mPopupWindowOffset;
    private int mScaleAnimationDuration;
    private Interpolator mScaleAnimationInterpolator;
    private boolean mShowAboveFirst;
    private int[] mTempLocation;
    private int[] mWindowLocationOnScreen;

    public OplusPopupListWindow(Context context) {
        super(context);
        this.mCoordinate = new Point();
        this.mTempLocation = new int[2];
        this.mWindowLocationOnScreen = new int[2];
        this.mPopupWindowOffset = new int[4];
        this.mAnimationListener = new Animation.AnimationListener() { // from class: com.oplus.widget.popupwindow.OplusPopupListWindow.1
            @Override // android.view.animation.Animation.AnimationListener
            public void onAnimationStart(Animation animation) {
                OplusPopupListWindow.this.mIsDismissing = true;
            }

            @Override // android.view.animation.Animation.AnimationListener
            public void onAnimationEnd(Animation animation) {
                OplusPopupListWindow.this.dismissPopupWindow();
                OplusPopupListWindow.this.mIsDismissing = false;
            }

            @Override // android.view.animation.Animation.AnimationListener
            public void onAnimationRepeat(Animation animation) {
            }
        };
        this.mContext = context;
        this.mItemList = new ArrayList();
        this.mScaleAnimationDuration = context.getResources().getInteger(202178596);
        this.mAlphaAnimationDuration = context.getResources().getInteger(202178596);
        Interpolator loadInterpolator = AnimationUtils.loadInterpolator(context, 202375200);
        this.mAlphaAnimationInterpolator = loadInterpolator;
        this.mScaleAnimationInterpolator = loadInterpolator;
        this.mPopupListWindowMinWidth = context.getResources().getDimensionPixelSize(201654456);
        this.mPopupListSelectionScrollOffset = context.getResources().getDimensionPixelSize(201654459);
        ListView listView = new ListView(context);
        this.mListViewUsedToMeasure = listView;
        listView.setDivider(null);
        this.mListViewUsedToMeasure.setLayoutParams(new ViewGroup.LayoutParams(-1, -1));
        this.mContentView = createContentView(context);
        setBackgroundDrawable(new ColorDrawable(0));
        setClippingEnabled(false);
        setExitTransition(null);
        setEnterTransition(null);
    }

    private ViewGroup createContentView(Context context) {
        FrameLayout contentView = (FrameLayout) LayoutInflater.from(context).inflate(201917491, (ViewGroup) null);
        this.mListView = (ListView) contentView.findViewById(201457743);
        this.mListContainer = (OplusMaxLinearLayout) contentView.findViewById(201457754);
        this.mBottomPanel = (FrameLayout) contentView.findViewById(201457756);
        this.mContentPanel = (LinearLayout) contentView.findViewById(201457755);
        Drawable background = context.getResources().getDrawable(201850959);
        Rect rect = new Rect();
        this.mBackgroundPaddingRect = rect;
        background.getPadding(rect);
        contentView.setBackground(background);
        return contentView;
    }

    public void show(View anchor) {
        if (anchor != null) {
            if ((this.mDefaultAdapter == null && this.mCustomAdapter == null) || isShowing()) {
                return;
            }
            this.mAnchor = anchor;
            anchor.getRootView().removeOnLayoutChangeListener(this);
            this.mAnchor.getRootView().addOnLayoutChangeListener(this);
            BaseAdapter baseAdapter = this.mCustomAdapter;
            if (baseAdapter == null) {
                this.mAdapter = this.mDefaultAdapter;
            } else {
                this.mAdapter = baseAdapter;
            }
            this.mListView.setAdapter((ListAdapter) this.mAdapter);
            int i = this.mPopupListSelection;
            if (i > 0) {
                this.mListView.setSelectionFromTop(i, this.mPopupListSelectionScrollOffset);
            }
            AdapterView.OnItemClickListener onItemClickListener = this.mOnItemClickListener;
            if (onItemClickListener != null) {
                this.mListView.setOnItemClickListener(onItemClickListener);
            }
            this.mDecorViewRect = new Rect();
            this.mAnchorRect = new Rect();
            this.mParentRectOnScreen = new Rect();
            this.mAnchor.getWindowVisibleDisplayFrame(this.mDecorViewRect);
            this.mAnchor.getGlobalVisibleRect(this.mAnchorRect);
            this.mAnchor.getRootView().getGlobalVisibleRect(this.mParentRectOnScreen);
            this.mAnchorRect.left -= this.mPopupWindowOffset[0];
            this.mAnchorRect.top -= this.mPopupWindowOffset[1];
            this.mAnchorRect.right += this.mPopupWindowOffset[2];
            this.mAnchorRect.bottom += this.mPopupWindowOffset[3];
            this.mAnchor.getRootView().getLocationOnScreen(this.mTempLocation);
            Rect rect = this.mAnchorRect;
            int[] iArr = this.mTempLocation;
            rect.offset(iArr[0], iArr[1]);
            Rect rect2 = this.mParentRectOnScreen;
            int[] iArr2 = this.mTempLocation;
            rect2.offset(iArr2[0], iArr2[1]);
            Rect rect3 = this.mDecorViewRect;
            rect3.left = Math.max(rect3.left, this.mParentRectOnScreen.left);
            Rect rect4 = this.mDecorViewRect;
            rect4.top = Math.max(rect4.top, this.mParentRectOnScreen.top);
            Rect rect5 = this.mDecorViewRect;
            rect5.right = Math.min(rect5.right, this.mParentRectOnScreen.right);
            Rect rect6 = this.mDecorViewRect;
            rect6.bottom = Math.min(rect6.bottom, this.mParentRectOnScreen.bottom);
            this.mPopupListMaxWidth = this.mListContainer.getMaxWidth();
            this.mPopupListMaxHeight = this.mListContainer.getMaxHeight();
            calculateWindowLocation();
            measurePopupWindow();
            calculateCoordinate();
            if (!this.mHasVerticalSpace || !this.mHasHorizontalSpace) {
                return;
            }
            setContentView(this.mContentView);
            calculatePivot();
            animateEnter();
            showAtLocation(this.mAnchor, 0, this.mCoordinate.x, this.mCoordinate.y);
        }
    }

    private void calculateWindowLocation() {
        this.mAnchor.getRootView().getLocationOnScreen(this.mTempLocation);
        int[] iArr = this.mTempLocation;
        int rootViewLeftOnScreen = iArr[0];
        int rootViewTopOnScreen = iArr[1];
        this.mAnchor.getRootView().getLocationInWindow(this.mTempLocation);
        int[] iArr2 = this.mTempLocation;
        int rootViewLeftOnWindow = iArr2[0];
        int rootViewTopOnWindow = iArr2[1];
        int[] iArr3 = this.mWindowLocationOnScreen;
        iArr3[0] = rootViewLeftOnScreen - rootViewLeftOnWindow;
        iArr3[1] = rootViewTopOnScreen - rootViewTopOnWindow;
    }

    public void measurePopupWindow() {
        ListAdapter adapter = this.mAdapter;
        int maxItemWidth = 0;
        int listTotalHeight = 0;
        View itemView = null;
        int itemType = 0;
        int widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(getPopupWindowMaxWidth(), Integer.MIN_VALUE);
        int heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, 0);
        int count = adapter.getCount();
        int i = 0;
        while (i < count) {
            int currentType = adapter.getItemViewType(i);
            if (currentType != itemType) {
                itemType = currentType;
                itemView = null;
            }
            itemView = adapter.getView(i, itemView, this.mListViewUsedToMeasure);
            AbsListView.LayoutParams lp = (AbsListView.LayoutParams) itemView.getLayoutParams();
            if (lp.height != -2) {
                heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(lp.height, 1073741824);
            }
            itemView.measure(widthMeasureSpec, heightMeasureSpec);
            int itemWidth = itemView.getMeasuredWidth();
            int itemHeight = itemView.getMeasuredHeight();
            if (itemWidth > maxItemWidth) {
                maxItemWidth = itemWidth;
            }
            ListAdapter adapter2 = adapter;
            if (maxItemWidth > this.mPopupListMaxWidth) {
                maxItemWidth = this.mPopupListMaxWidth;
            }
            listTotalHeight += itemHeight;
            if (listTotalHeight > this.mPopupListMaxHeight) {
                listTotalHeight = this.mPopupListMaxHeight;
            }
            i++;
            adapter = adapter2;
        }
        this.mBottomPanel.measure(widthMeasureSpec, heightMeasureSpec);
        int totalHeight = 0 + listTotalHeight;
        int totalHeight2 = totalHeight + this.mBottomPanel.getMeasuredHeight() + this.mContentPanel.getPaddingTop() + this.mContentPanel.getPaddingBottom();
        ViewGroup.LayoutParams listContainerLp = this.mListContainer.getLayoutParams();
        listContainerLp.height = listTotalHeight;
        this.mListContainer.setLayoutParams(listContainerLp);
        setWidth(Math.max(maxItemWidth, this.mPopupListWindowMinWidth) + this.mBackgroundPaddingRect.left + this.mBackgroundPaddingRect.right);
        setHeight(this.mBackgroundPaddingRect.top + totalHeight2 + this.mBackgroundPaddingRect.bottom);
    }

    private int getPopupWindowMaxWidth() {
        return ((this.mDecorViewRect.right - this.mDecorViewRect.left) - this.mBackgroundPaddingRect.left) - this.mBackgroundPaddingRect.right;
    }

    private void calculateCoordinate() {
        int y;
        this.mHasHorizontalSpace = true;
        this.mHasVerticalSpace = true;
        if (this.mDecorViewRect.right - this.mDecorViewRect.left < getWidth()) {
            this.mHasHorizontalSpace = false;
            return;
        }
        int x = Math.min(this.mAnchorRect.centerX() - (getWidth() / 2), this.mDecorViewRect.right - getWidth());
        int x2 = Math.max(this.mDecorViewRect.left, x) - this.mWindowLocationOnScreen[0];
        int availableHeightAboveAnchor = this.mAnchorRect.top - this.mDecorViewRect.top;
        int availableHeightBelowAnchor = this.mDecorViewRect.bottom - this.mAnchorRect.bottom;
        int popupWindowHeight = getHeight();
        boolean aboveHasSpace = availableHeightAboveAnchor >= popupWindowHeight;
        boolean belowHasSpace = availableHeightBelowAnchor >= popupWindowHeight;
        int aboveY = this.mAnchorRect.top - popupWindowHeight;
        int belowY = this.mAnchorRect.bottom;
        if (availableHeightBelowAnchor <= 0 && availableHeightAboveAnchor <= 0) {
            this.mHasVerticalSpace = false;
            return;
        }
        boolean z = this.mShowAboveFirst;
        if (!z ? belowHasSpace : aboveHasSpace) {
            y = z ? aboveY : belowY;
        } else if (!z ? aboveHasSpace : belowHasSpace) {
            y = z ? belowY : aboveY;
        } else if (availableHeightAboveAnchor > availableHeightBelowAnchor) {
            y = this.mDecorViewRect.top;
            setHeight(availableHeightAboveAnchor);
        } else {
            y = this.mAnchorRect.bottom;
            setHeight(availableHeightBelowAnchor);
        }
        this.mCoordinate.set(x2, y - this.mWindowLocationOnScreen[1]);
    }

    private void calculatePivot() {
        if ((this.mAnchorRect.centerX() - this.mWindowLocationOnScreen[0]) - this.mCoordinate.x >= getWidth()) {
            this.mPivotX = 1.0f;
        } else {
            this.mPivotX = ((this.mAnchorRect.centerX() - this.mWindowLocationOnScreen[0]) - this.mCoordinate.x) / getWidth();
        }
        if (this.mCoordinate.y >= this.mAnchorRect.top - this.mWindowLocationOnScreen[1]) {
            this.mPivotY = 0.0f;
        } else {
            this.mPivotY = 1.0f;
        }
    }

    private void animateEnter() {
        Animation scaleAnimation = new ScaleAnimation(0.5f, 1.0f, 0.5f, 1.0f, 1, this.mPivotX, 1, this.mPivotY);
        Animation alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
        scaleAnimation.setDuration(this.mScaleAnimationDuration);
        scaleAnimation.setInterpolator(this.mScaleAnimationInterpolator);
        alphaAnimation.setDuration(this.mAlphaAnimationDuration);
        alphaAnimation.setInterpolator(this.mAlphaAnimationInterpolator);
        AnimationSet animationSet = new AnimationSet(false);
        animationSet.addAnimation(scaleAnimation);
        animationSet.addAnimation(alphaAnimation);
        this.mContentView.startAnimation(animationSet);
    }

    private void animateExit() {
        Animation scaleAnimation = new ScaleAnimation(1.0f, 0.5f, 1.0f, 0.5f, 1, this.mPivotX, 1, this.mPivotY);
        Animation alphaAnimation = new AlphaAnimation(1.0f, 0.0f);
        scaleAnimation.setDuration(this.mScaleAnimationDuration);
        scaleAnimation.setInterpolator(this.mScaleAnimationInterpolator);
        alphaAnimation.setDuration(this.mAlphaAnimationDuration);
        alphaAnimation.setInterpolator(this.mAlphaAnimationInterpolator);
        AnimationSet animationSet = new AnimationSet(false);
        animationSet.addAnimation(scaleAnimation);
        animationSet.addAnimation(alphaAnimation);
        animationSet.setAnimationListener(this.mAnimationListener);
        this.mContentView.startAnimation(animationSet);
    }

    @Override // android.view.View.OnLayoutChangeListener
    public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
        Rect newRect = new Rect(left, top, right, bottom);
        Rect oldRect = new Rect(oldLeft, oldTop, oldRight, oldBottom);
        if (isShowing() && !newRect.equals(oldRect)) {
            dismissPopupWindow();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void dismissPopupWindow() {
        super.dismiss();
        this.mIsDismissing = false;
        this.mAnchor.getRootView().removeOnLayoutChangeListener(this);
        setContentView(null);
    }

    @Override // android.widget.PopupWindow
    public void dismiss() {
        dismiss(true);
    }

    public void dismiss(boolean animate) {
        if (this.mIsDismissing || !isShowing()) {
            return;
        }
        if (animate) {
            animateExit();
        } else {
            dismissPopupWindow();
        }
    }

    public List<PopupListItem> getItemList() {
        return this.mItemList;
    }

    public void setItemList(List<PopupListItem> itemList) {
        if (itemList != null) {
            this.mItemList = itemList;
            this.mDefaultAdapter = new DefaultAdapter(this.mContext, itemList);
        }
    }

    public void setOnItemClickListener(AdapterView.OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    public void setAdapter(BaseAdapter adapter) {
        this.mCustomAdapter = adapter;
    }

    public void setSelection(int position) {
        this.mPopupListSelection = position;
    }

    public ListView getListView() {
        return this.mListView;
    }

    public void setListPortraitMaxWidth(int maxValue) {
        this.mListContainer.setPortraitMaxWidth(maxValue);
    }

    public void setListPortraitMaxHeight(int maxValue) {
        this.mListContainer.setPortraitMaxHeight(maxValue);
    }

    public void setListLandscapeMaxWidth(int maxValue) {
        this.mListContainer.setLandscapeMaxWidth(maxValue);
    }

    public void setListLandscapeMaxHeight(int maxValue) {
        this.mListContainer.setLandscapeMaxHeight(maxValue);
    }

    public void addBottomPanel(View view) {
        this.mBottomPanel.addView(view);
    }

    public void setDismissTouchOutside(boolean isDismiss) {
        if (isDismiss) {
            setTouchable(true);
            setFocusable(true);
            setOutsideTouchable(true);
        } else {
            setFocusable(false);
            setOutsideTouchable(false);
        }
        update();
    }

    public void setOffset(int left, int top, int right, int bottom) {
        int[] iArr = this.mPopupWindowOffset;
        iArr[0] = left;
        iArr[1] = top;
        iArr[2] = right;
        iArr[3] = bottom;
    }

    public void showAboveFirst(boolean showAboveFirst) {
        this.mShowAboveFirst = showAboveFirst;
    }
}
