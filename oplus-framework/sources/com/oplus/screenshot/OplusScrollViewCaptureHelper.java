package com.oplus.screenshot;

import android.graphics.Point;
import android.graphics.Rect;
import android.os.CancellationSignal;
import android.view.View;
import android.view.ViewGroup;
import com.android.internal.view.ScrollCaptureViewHelper;
import java.util.function.Consumer;

/* loaded from: classes.dex */
public class OplusScrollViewCaptureHelper extends OplusScrollCaptureViewHelper<ViewGroup> {
    private static final String TAG = "OplusSVCaptureHelper";
    private int mOverScrollMode;
    private boolean mScrollBarEnabled;
    private int mStartScrollY;

    public /* bridge */ /* synthetic */ void onScrollRequested(View view, Rect rect, Rect rect2, CancellationSignal cancellationSignal, Consumer consumer) {
        onScrollRequested((ViewGroup) view, rect, rect2, cancellationSignal, (Consumer<ScrollCaptureViewHelper.ScrollResult>) consumer);
    }

    public boolean onAcceptSession(ViewGroup view) {
        return view.isVisibleToUser() && (view.canScrollVertically(-1) || view.canScrollVertically(1));
    }

    public void onPrepareForStart(ViewGroup view, Rect scrollBounds) {
        this.mStartScrollY = view.getScrollY();
        int overScrollMode = view.getOverScrollMode();
        this.mOverScrollMode = overScrollMode;
        if (overScrollMode != 2) {
            view.setOverScrollMode(2);
        }
        boolean isVerticalScrollBarEnabled = view.isVerticalScrollBarEnabled();
        this.mScrollBarEnabled = isVerticalScrollBarEnabled;
        if (isVerticalScrollBarEnabled) {
            view.setVerticalScrollBarEnabled(false);
        }
    }

    public void onScrollRequested(ViewGroup view, Rect scrollBounds, Rect requestRect, CancellationSignal signal, Consumer<ScrollCaptureViewHelper.ScrollResult> resultConsumer) {
        int scrollDelta = view.getScrollY() - this.mStartScrollY;
        ScrollCaptureViewHelper.ScrollResult result = new ScrollCaptureViewHelper.ScrollResult();
        result.requestedArea = new Rect(requestRect);
        result.scrollDelta = scrollDelta;
        result.availableArea = new Rect();
        View contentView = view.getChildAt(0);
        if (contentView == null) {
            resultConsumer.accept(result);
            return;
        }
        Rect requestedContainerBounds = getRequestedContainerBounds(scrollBounds, requestRect, scrollDelta);
        Rect requestedContentBounds = getRequestedContentBounds(view, contentView, requestedContainerBounds);
        Rect input = getInput(view, scrollBounds, contentView, requestedContentBounds);
        contentView.requestRectangleOnScreen(input, true);
        int scrollDelta2 = view.getScrollY() - this.mStartScrollY;
        result.scrollDelta = scrollDelta2;
        Point offset = new Point();
        Rect available = new Rect(requestedContentBounds);
        if (!view.getChildVisibleRect(contentView, available, offset)) {
            available.setEmpty();
            result.availableArea = available;
            resultConsumer.accept(result);
        } else {
            available.offset(-offset.x, -offset.y);
            available.offset(contentView.getLeft() - view.getScrollX(), contentView.getTop() - view.getScrollY());
            available.offset(-scrollBounds.left, -scrollBounds.top);
            available.offset(0, scrollDelta2);
            result.availableArea = new Rect(available);
            resultConsumer.accept(result);
        }
    }

    public void onPrepareForEnd(ViewGroup view) {
        view.scrollTo(0, this.mStartScrollY);
        int i = this.mOverScrollMode;
        if (i != 2) {
            view.setOverScrollMode(i);
        }
        if (this.mScrollBarEnabled) {
            view.setVerticalScrollBarEnabled(true);
        }
    }

    private static Rect getRequestedContainerBounds(Rect scrollBounds, Rect requestRect, int scrollDelta) {
        Rect requestedContainerBounds = new Rect(requestRect);
        requestedContainerBounds.offset(0, -scrollDelta);
        requestedContainerBounds.offset(scrollBounds.left, scrollBounds.top);
        return requestedContainerBounds;
    }

    private static Rect getRequestedContentBounds(ViewGroup view, View contentView, Rect requestedContainerBounds) {
        Rect requestedContentBounds = new Rect(requestedContainerBounds);
        requestedContentBounds.offset(view.getScrollX() - contentView.getLeft(), view.getScrollY() - contentView.getTop());
        return requestedContentBounds;
    }

    private static Rect getInput(ViewGroup view, Rect scrollBounds, View contentView, Rect requestedContentBounds) {
        Rect input = new Rect(requestedContentBounds);
        int remainingHeight = scrollBounds.height() - input.height();
        if (remainingHeight > 0) {
            input.inset(0, (-remainingHeight) / 2);
        }
        Rect localScrollBounds = new Rect(scrollBounds);
        localScrollBounds.offset(0, -contentView.getTop());
        localScrollBounds.offset(0, view.getScrollY());
        int scrollMovement = input.top - localScrollBounds.top;
        if (scrollMovement < 0) {
            input.offset(0, -scrollMovement);
        }
        input.bottom = Math.min(input.bottom, contentView.getHeight());
        return input;
    }
}
