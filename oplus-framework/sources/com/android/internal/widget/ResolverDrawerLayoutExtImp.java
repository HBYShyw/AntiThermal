package com.android.internal.widget;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ScrollView;
import com.android.internal.widget.ResolverDrawerLayout;
import com.oplus.resolver.OplusResolverUtils;

/* loaded from: classes.dex */
public class ResolverDrawerLayoutExtImp implements IResolverDrawerLayoutExt {
    private static final String TAG = "ResolverDrawerLayoutExtImp";
    private ScrollView mNestedScrollChild;
    private final ResolverDrawerLayout mResolverDrawerLayout;
    private int mStatusBarHeight;
    private int mOffsetX = -1;
    private int mOffsetY = -1;
    private int mAnchorHeight = 0;
    private int mAnchorWidth = 0;
    private boolean mMayShowSide = false;

    public ResolverDrawerLayoutExtImp(Object base) {
        this.mStatusBarHeight = 0;
        ResolverDrawerLayout resolverDrawerLayout = (ResolverDrawerLayout) base;
        this.mResolverDrawerLayout = resolverDrawerLayout;
        this.mStatusBarHeight = resolverDrawerLayout.getResources().getDimensionPixelOffset(201654272);
    }

    public boolean isNestedScrollChildScrolled(boolean upToDown, float mCollapseOffset) {
        ScrollView scrollView = this.mNestedScrollChild;
        if (scrollView == null || scrollView.getChildCount() <= 0 || mCollapseOffset != 0.0f) {
            return false;
        }
        return !upToDown || this.mNestedScrollChild.getScrollY() > 0;
    }

    public boolean shouldHookOnTouchEventMove() {
        return true;
    }

    public void customOnTouchEventMove(float dy, AbsListView mNestedListChild, RecyclerView mNestedRecyclerChild, float mCollapseOffset) {
        if (dy > 0.0f && this.mResolverDrawerLayout.getWrapper().isNestedListChildScrolled()) {
            mNestedListChild.smoothScrollBy((int) (-dy), 0);
            return;
        }
        if (dy > 0.0f && this.mResolverDrawerLayout.getWrapper().isNestedListChildScrolled()) {
            mNestedRecyclerChild.scrollBy(0, (int) (-dy));
            return;
        }
        if (isNestedScrollChildScrolled(dy > 0.0f, mCollapseOffset)) {
            this.mNestedScrollChild.scrollBy(0, (int) (-dy));
        } else {
            this.mResolverDrawerLayout.getWrapper().performDrag(dy);
        }
    }

    public boolean shouldHookOnTouchEventUpFling() {
        return true;
    }

    public void customOnTouchEventUpFling(float yvel, float mCollapseOffset, int mCollapsibleHeight, int mUncollapsibleHeight) {
        if (this.mResolverDrawerLayout.getWrapper().isDismissable() && yvel > 0.0f && mCollapseOffset > mCollapsibleHeight) {
            this.mResolverDrawerLayout.getWrapper().smoothScrollTo(mCollapsibleHeight + mUncollapsibleHeight, yvel);
            this.mResolverDrawerLayout.getWrapper().setDismissOnScrollerFinished(true);
        } else {
            this.mResolverDrawerLayout.scrollNestedScrollableChildBackToTop();
            this.mResolverDrawerLayout.getWrapper().smoothScrollTo(yvel < 0.0f ? 0 : mCollapsibleHeight, yvel);
        }
    }

    public boolean shouldHookOnTouchEventUpScrollBack() {
        return true;
    }

    public void customOnTouchEventUpScrollBack(float mCollapseOffset, int mCollapsibleHeight, int mUncollapsibleHeight) {
        this.mResolverDrawerLayout.getWrapper().smoothScrollTo(mCollapseOffset < ((float) mCollapsibleHeight) / 2.0f ? 0 : mCollapsibleHeight, 0.0f);
    }

    public boolean shouldHookonNestedScroll() {
        return true;
    }

    public void customOnNestedScroll(float velocityY, int mCollapsibleHeight, int mUncollapsibleHeight) {
        if (velocityY > 0.0f) {
            this.mResolverDrawerLayout.getWrapper().smoothScrollTo(0, velocityY);
        } else {
            this.mResolverDrawerLayout.getWrapper().smoothScrollTo(mCollapsibleHeight + mUncollapsibleHeight, velocityY);
            this.mResolverDrawerLayout.getWrapper().setDismissOnScrollerFinished(true);
        }
    }

    public void hookresetTouch() {
        this.mNestedScrollChild = null;
    }

    public void hooksmoothScrollTo(final boolean mDismissOnScrollerFinished, final ResolverDrawerLayout.OnDismissedListener mOnDismissedListener) {
        this.mResolverDrawerLayout.post(new Runnable() { // from class: com.android.internal.widget.ResolverDrawerLayoutExtImp$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                ResolverDrawerLayoutExtImp.this.lambda$hooksmoothScrollTo$0(mDismissOnScrollerFinished, mOnDismissedListener);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$hooksmoothScrollTo$0(boolean mDismissOnScrollerFinished, ResolverDrawerLayout.OnDismissedListener mOnDismissedListener) {
        if (mDismissOnScrollerFinished && mOnDismissedListener != null) {
            this.mResolverDrawerLayout.getWrapper().dismiss();
        }
    }

    public void hookonStartNestedScroll(View targetView) {
        if (targetView instanceof ScrollView) {
            this.mNestedScrollChild = (ScrollView) targetView;
        }
    }

    public boolean shouldHookonStopNestedScroll() {
        return true;
    }

    public void customOnStopNestedScroll(float mCollapseOffset, int mCollapsibleHeight, int mUncollapsibleHeight) {
        if (mCollapseOffset == mCollapsibleHeight) {
            this.mResolverDrawerLayout.getWrapper().smoothScrollTo(mCollapsibleHeight, 0.0f);
            return;
        }
        if (mCollapseOffset < mCollapsibleHeight && mCollapseOffset > (mCollapsibleHeight + mUncollapsibleHeight) / 2.0f) {
            this.mResolverDrawerLayout.getWrapper().smoothScrollTo(mCollapsibleHeight, 0.0f);
        } else if (mCollapseOffset < (mCollapsibleHeight + mUncollapsibleHeight) / 2.0f) {
            this.mResolverDrawerLayout.getWrapper().smoothScrollTo(0, 0.0f);
        } else {
            this.mResolverDrawerLayout.getWrapper().smoothScrollTo(mCollapsibleHeight + mUncollapsibleHeight, 0.0f);
            this.mResolverDrawerLayout.getWrapper().setDismissOnScrollerFinished(true);
        }
    }

    public void setOffsetXY(int offsetX, int offsetY, int anchorHeight, int anchorWidth) {
        this.mOffsetX = offsetX;
        if (offsetY != -1) {
            this.mOffsetY = offsetY - this.mStatusBarHeight;
        }
        this.mAnchorHeight = anchorHeight;
        this.mAnchorWidth = anchorWidth;
        Log.d(TAG, "offsetX: " + offsetX + " offsetY: " + offsetY + " anchorHeight: " + anchorHeight + " anchorWidth: " + anchorWidth);
    }

    public int calculateOffsetX(int leftEdge, int widthAvailable, int childWidth) {
        int result;
        if (!(this.mResolverDrawerLayout.getContext() instanceof Activity)) {
            return (widthAvailable - childWidth) / 2;
        }
        if (!OplusResolverUtils.asFollowerPanel((Activity) this.mResolverDrawerLayout.getContext()) || this.mOffsetY == -1 || this.mOffsetX == -1) {
            return (widthAvailable - childWidth) / 2;
        }
        int baseWidthOffset = this.mResolverDrawerLayout.getResources().getDimensionPixelOffset(201654471);
        int baseHeightOffset = this.mResolverDrawerLayout.getResources().getDimensionPixelOffset(201654469);
        int totalWidth = this.mResolverDrawerLayout.getWidth();
        if (widthAvailable <= (baseWidthOffset * 2) + childWidth) {
            return (widthAvailable - childWidth) / 2;
        }
        if (this.mMayShowSide) {
            int i = this.mOffsetX;
            int i2 = this.mAnchorWidth;
            if ((i2 / 2) + i + baseHeightOffset + childWidth + baseWidthOffset <= totalWidth) {
                result = i + (i2 / 2) + baseHeightOffset;
            } else if (childWidth + baseWidthOffset <= (i - (i2 / 2)) - baseHeightOffset) {
                result = ((i - (i2 / 2)) - childWidth) - baseHeightOffset;
            } else {
                result = (widthAvailable - childWidth) / 2;
            }
        } else {
            int result2 = this.mOffsetX;
            if (result2 <= (childWidth / 2) + baseWidthOffset) {
                result = baseWidthOffset;
            } else if (result2 >= (totalWidth - baseWidthOffset) - (childWidth / 2)) {
                result = (totalWidth - baseWidthOffset) - childWidth;
            } else {
                result = result2 - (childWidth / 2);
            }
        }
        Log.d(TAG, "totalWidth: " + totalWidth + " baseHeightOffset: " + baseHeightOffset + " baseWidthOffset: " + baseWidthOffset + " result: " + result + " mMayShowSide: " + this.mMayShowSide);
        return result;
    }

    public int calculateOffsetY(int topOffset, float collapseOffset) {
        int result;
        if (!(this.mResolverDrawerLayout.getContext() instanceof Activity)) {
            return topOffset;
        }
        if (!OplusResolverUtils.asFollowerPanel((Activity) this.mResolverDrawerLayout.getContext()) || this.mOffsetY == -1 || this.mOffsetX == -1) {
            return topOffset;
        }
        int totalHeight = this.mResolverDrawerLayout.getHeight();
        int baseOffset = this.mResolverDrawerLayout.getResources().getDimensionPixelOffset(201654469);
        int usedHeight = 0;
        this.mMayShowSide = false;
        int childCount = this.mResolverDrawerLayout.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = this.mResolverDrawerLayout.getChildAt(i);
            ResolverDrawerLayout.LayoutParams lp = child.getLayoutParams();
            if (lp.hasNestedScrollIndicator) {
            }
            if (child.getVisibility() != 8) {
                int usedHeight2 = usedHeight + lp.topMargin;
                if (lp.ignoreOffset) {
                    usedHeight2 -= (int) collapseOffset;
                }
                usedHeight = child.getMeasuredHeight() + usedHeight2 + lp.bottomMargin;
            }
        }
        int i2 = this.mOffsetY;
        int i3 = this.mAnchorHeight;
        if (i2 + i3 + baseOffset <= totalHeight - usedHeight) {
            result = i2 + i3 + baseOffset;
        } else if (i2 - baseOffset >= usedHeight) {
            result = (i2 - usedHeight) - baseOffset;
        } else {
            this.mMayShowSide = true;
            result = (totalHeight - usedHeight) / 2;
        }
        Log.d(TAG, "topOffset: " + topOffset + " totalHeight: " + totalHeight + " usedHeight: " + usedHeight + " result: " + result);
        return result;
    }

    public int calculateUnCollapsibleHeight(int heightUsed, int collapsibleHeight, int totalHeight) {
        int result;
        if (this.mOffsetY == -1 || this.mOffsetX == -1) {
            int baseUnCollapsibleHeight = heightUsed - collapsibleHeight;
            return baseUnCollapsibleHeight;
        }
        int i = heightUsed - collapsibleHeight;
        int baseOffset = this.mResolverDrawerLayout.getResources().getDimensionPixelOffset(201654469);
        int result2 = this.mOffsetY;
        int i2 = this.mAnchorHeight;
        if (result2 + i2 + baseOffset <= totalHeight - heightUsed) {
            result = result2 + i2 + baseOffset;
        } else if (result2 - baseOffset >= heightUsed) {
            result = (result2 - baseOffset) - heightUsed;
        } else {
            result = (totalHeight - heightUsed) / 2;
        }
        int result3 = totalHeight - result;
        Log.d(TAG, "result: " + result3 + " totalHeight: " + totalHeight + " heightUsed: " + heightUsed + " collapsibleHeight: " + collapsibleHeight);
        return result3;
    }
}
