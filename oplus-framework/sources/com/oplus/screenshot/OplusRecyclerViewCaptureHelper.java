package com.oplus.screenshot;

import android.graphics.Rect;
import android.os.CancellationSignal;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import com.android.internal.view.ScrollCaptureViewHelper;
import com.oplus.util.OplusLog;
import com.oplus.widget.OplusMaxLinearLayout;
import java.util.function.Consumer;

/* loaded from: classes.dex */
public class OplusRecyclerViewCaptureHelper extends OplusScrollCaptureViewHelper<ViewGroup> {
    private static final String TAG = "OplusRVCaptureHelper";
    private int mOverScrollMode;
    private boolean mScrollBarWasEnabled;
    private int mScrollDelta;

    public /* bridge */ /* synthetic */ void onScrollRequested(View view, Rect rect, Rect rect2, CancellationSignal cancellationSignal, Consumer consumer) {
        onScrollRequested((ViewGroup) view, rect, rect2, cancellationSignal, (Consumer<ScrollCaptureViewHelper.ScrollResult>) consumer);
    }

    public boolean onAcceptSession(ViewGroup view) {
        return view.isVisibleToUser() && (view.canScrollVertically(-1) || view.canScrollVertically(1));
    }

    public void onPrepareForStart(ViewGroup view, Rect scrollBounds) {
        this.mScrollDelta = 0;
        this.mOverScrollMode = view.getOverScrollMode();
        view.setOverScrollMode(2);
        this.mScrollBarWasEnabled = view.isVerticalScrollBarEnabled();
        view.setVerticalScrollBarEnabled(false);
    }

    public void onScrollRequested(ViewGroup recyclerView, Rect scrollBounds, Rect requestRect, CancellationSignal signal, Consumer<ScrollCaptureViewHelper.ScrollResult> resultConsumer) {
        ScrollCaptureViewHelper.ScrollResult result = new ScrollCaptureViewHelper.ScrollResult();
        result.requestedArea = new Rect(requestRect);
        result.scrollDelta = this.mScrollDelta;
        result.availableArea = new Rect();
        if (!recyclerView.isVisibleToUser() || recyclerView.getChildCount() == 0) {
            OplusLog.w(TAG, "recyclerView is empty or not visible, cannot continue");
            resultConsumer.accept(result);
            return;
        }
        Rect requestedContainerBounds = new Rect(requestRect);
        requestedContainerBounds.offset(0, -this.mScrollDelta);
        requestedContainerBounds.offset(scrollBounds.left, scrollBounds.top);
        View anchor = findChildNearestTarget(recyclerView, requestedContainerBounds);
        if (anchor == null) {
            OplusLog.w(TAG, "Failed to locate anchor view");
            resultConsumer.accept(result);
            return;
        }
        Rect requestedContentBounds = new Rect(requestedContainerBounds);
        recyclerView.offsetRectIntoDescendantCoords(anchor, requestedContentBounds);
        int prevAnchorTop = anchor.getTop();
        Rect input = new Rect(requestedContentBounds);
        int remainingHeight = ((recyclerView.getHeight() - recyclerView.getPaddingTop()) - recyclerView.getPaddingBottom()) - input.height();
        if (remainingHeight > 0) {
            input.inset(0, (-remainingHeight) / 2);
        }
        Rect tmp = new Rect(input);
        recyclerView.offsetDescendantRectToMyCoords(anchor, tmp);
        if (tmp.top < scrollBounds.top) {
            input.top = scrollBounds.top;
        }
        boolean isScrolled = recyclerView.requestChildRectangleOnScreen(anchor, input, true);
        if (anchor.getParent() != recyclerView) {
            Log.w(TAG, "anchor is remove from ancestor view");
            resultConsumer.accept(result);
            return;
        }
        if (isScrolled) {
            int scrolled = prevAnchorTop - anchor.getTop();
            int i = this.mScrollDelta + scrolled;
            this.mScrollDelta = i;
            result.scrollDelta = i;
        }
        requestedContainerBounds.set(requestedContentBounds);
        recyclerView.offsetDescendantRectToMyCoords(anchor, requestedContainerBounds);
        Rect recyclerLocalVisible = new Rect(scrollBounds);
        recyclerView.getLocalVisibleRect(recyclerLocalVisible);
        if (!requestedContainerBounds.intersect(recyclerLocalVisible)) {
            resultConsumer.accept(result);
            return;
        }
        Rect available = new Rect(requestedContainerBounds);
        available.offset(-scrollBounds.left, -scrollBounds.top);
        available.offset(0, this.mScrollDelta);
        result.availableArea = available;
        resultConsumer.accept(result);
    }

    static View findChildNearestTarget(ViewGroup parent, Rect targetRect) {
        View selected = null;
        int minCenterDistance = OplusMaxLinearLayout.INVALID_MAX_VALUE;
        int preferredDistance = (int) (targetRect.height() * 0.25f);
        Rect parentLocalVis = new Rect();
        parent.getLocalVisibleRect(parentLocalVis);
        Rect frame = new Rect();
        for (int i = 0; i < parent.getChildCount(); i++) {
            View child = parent.getChildAt(i);
            child.getHitRect(frame);
            if (child.getVisibility() == 0) {
                int centerDistance = Math.abs(targetRect.centerY() - frame.centerY());
                if (centerDistance < minCenterDistance) {
                    minCenterDistance = centerDistance;
                    selected = child;
                } else if (frame.intersect(targetRect) && frame.height() > preferredDistance) {
                    selected = child;
                }
            }
        }
        return selected;
    }

    public void onPrepareForEnd(ViewGroup view) {
        view.scrollBy(0, -this.mScrollDelta);
        view.setOverScrollMode(this.mOverScrollMode);
        view.setVerticalScrollBarEnabled(this.mScrollBarWasEnabled);
    }
}
