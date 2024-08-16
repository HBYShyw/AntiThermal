package com.oplus.widget;

import com.oplus.widget.OplusViewPager;

/* loaded from: classes.dex */
class OplusPagerMenuDelegate {
    private static final float MENU_SCROLL_OFFSET = 0.3f;
    private static final float MENU_SCROLL_OFFSET_HIGH = 0.7f;
    private static final float MENU_SCROLL_OFFSET_LOW = 0.3f;
    private static final int MENU_SCROLL_STATE_DOWN = 1;
    private static final int MENU_SCROLL_STATE_IDLE = 0;
    private static final int MENU_SCROLL_STATE_OUT = 3;
    private static final int MENU_SCROLL_STATE_UP = 2;
    private OplusViewPager mPager;
    private float mLastMenuOffset = -1.0f;
    private boolean mLastDirection = true;
    private boolean mNextDirection = true;
    private boolean mIsBeingSettled = false;
    private int mLastItem = -1;
    private int mNextItem = -1;
    private int mMenuScrollState = 0;
    private OplusViewPager.OnPageMenuChangeListener mOnPageMenuChangeListener = null;
    private OplusBottomMenuCallback mCallback = null;

    public OplusPagerMenuDelegate(OplusViewPager pager) {
        this.mPager = null;
        this.mPager = pager;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setOnPageMenuChangeListener(OplusViewPager.OnPageMenuChangeListener listener) {
        this.mOnPageMenuChangeListener = listener;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void bindSplitMenuCallback(OplusBottomMenuCallback callback) {
        this.mCallback = callback;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setSettleState() {
        this.mIsBeingSettled = true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onPageMenuSelected(int position) {
        this.mLastItem = this.mPager.getCurrentItem();
        this.mNextItem = position;
        if (this.mPager.getDragState() || this.mIsBeingSettled) {
            setMenuUpdateMode(2);
        }
        OplusViewPager.OnPageMenuChangeListener onPageMenuChangeListener = this.mOnPageMenuChangeListener;
        if (onPageMenuChangeListener != null) {
            onPageMenuChangeListener.onPageMenuSelected(position);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onPageMenuScrollStateChanged(int state) {
        if (this.mPager.getScrollState() == 0) {
            this.mIsBeingSettled = false;
            setMenuUpdateMode(1);
        }
        OplusViewPager.OnPageMenuChangeListener onPageMenuChangeListener = this.mOnPageMenuChangeListener;
        if (onPageMenuChangeListener != null) {
            onPageMenuChangeListener.onPageMenuScrollStateChanged(state);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void pageMenuScrolled(int currentPage, float pageOffset) {
        float menuOffset = getMenuOffset(currentPage, pageOffset);
        float f = this.mLastMenuOffset;
        if (f != menuOffset) {
            if (menuOffset == 1.0f || menuOffset < f) {
                onPageMenuScrollDataChanged();
            }
            this.mLastMenuOffset = menuOffset;
        }
        onPageMenuScrolled(-1, menuOffset);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void updateNextItem(float deltaX) {
        OplusViewPager.ItemInfo ii = this.mPager.infoForCurrentScrollPosition();
        if (ii == null) {
            return;
        }
        int currentPage = ii.position;
        boolean z = false;
        if (!this.mPager.isLayoutRtl() ? deltaX > 0.0f : deltaX < 0.0f) {
            z = true;
        }
        updateDirection(z);
        if (this.mNextDirection) {
            this.mLastItem = currentPage;
            this.mNextItem = Math.min(currentPage + 1, this.mPager.getAdapter().getCount() - 1);
        } else {
            this.mLastItem = currentPage;
            this.mNextItem = currentPage;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void updateDirection(boolean direction) {
        this.mLastDirection = this.mNextDirection;
        this.mNextDirection = direction;
    }

    private void setMenuUpdateMode(int updateMode) {
        OplusBottomMenuCallback oplusBottomMenuCallback = this.mCallback;
        if (oplusBottomMenuCallback != null) {
            oplusBottomMenuCallback.setMenuUpdateMode(updateMode);
        }
    }

    private float getMenuOffset(int currentPage, float pageOffset) {
        float totalOffset;
        int i = this.mNextItem;
        int i2 = this.mLastItem;
        if (i == i2) {
            totalOffset = pageOffset;
        } else {
            int itemOffset = Math.min(i, i2);
            int itemCount = Math.abs(this.mNextItem - this.mLastItem);
            totalOffset = ((currentPage + pageOffset) - itemOffset) / itemCount;
        }
        if (totalOffset > 0.0f && totalOffset <= 0.3f) {
            float menuOffset = totalOffset / 0.3f;
            return menuOffset;
        }
        if (totalOffset > 0.3f && totalOffset < 0.7f) {
            return 1.0f;
        }
        if (totalOffset >= 0.7f) {
            float menuOffset2 = (1.0f - totalOffset) / 0.3f;
            return menuOffset2;
        }
        return 0.0f;
    }

    private void onPageMenuScrolled(int index, float offset) {
        OplusViewPager.OnPageMenuChangeListener onPageMenuChangeListener = this.mOnPageMenuChangeListener;
        if (onPageMenuChangeListener != null) {
            onPageMenuChangeListener.onPageMenuScrolled(index, offset);
        }
    }

    private void onPageMenuScrollDataChanged() {
        OplusViewPager.OnPageMenuChangeListener onPageMenuChangeListener = this.mOnPageMenuChangeListener;
        if (onPageMenuChangeListener != null) {
            onPageMenuChangeListener.onPageMenuScrollDataChanged();
        }
    }
}
