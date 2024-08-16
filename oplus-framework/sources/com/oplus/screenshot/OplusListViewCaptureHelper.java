package com.oplus.screenshot;

import android.graphics.Rect;
import android.os.CancellationSignal;
import android.view.View;
import android.widget.ListView;
import com.android.internal.view.ScrollCaptureViewHelper;
import com.oplus.util.OplusLog;
import java.util.function.Consumer;

/* loaded from: classes.dex */
public class OplusListViewCaptureHelper extends OplusScrollCaptureViewHelper<ListView> {
    private static final boolean DEBUG = false;
    private static final String TAG = "OplusLVCaptureHelper";
    private int mOverScrollMode;
    private boolean mScrollBarWasEnabled;
    private int mScrollDelta;

    public /* bridge */ /* synthetic */ void onScrollRequested(View view, Rect rect, Rect rect2, CancellationSignal cancellationSignal, Consumer consumer) {
        onScrollRequested((ListView) view, rect, rect2, cancellationSignal, (Consumer<ScrollCaptureViewHelper.ScrollResult>) consumer);
    }

    public boolean onAcceptSession(ListView view) {
        return view.isVisibleToUser() && (view.canScrollVertically(-1) || view.canScrollVertically(1));
    }

    public void onPrepareForStart(ListView view, Rect scrollBounds) {
        this.mScrollDelta = 0;
        this.mOverScrollMode = view.getOverScrollMode();
        view.setOverScrollMode(2);
        this.mScrollBarWasEnabled = view.isVerticalScrollBarEnabled();
        view.setVerticalScrollBarEnabled(false);
    }

    public void onScrollRequested(ListView listView, Rect scrollBounds, Rect requestRect, CancellationSignal signal, Consumer<ScrollCaptureViewHelper.ScrollResult> resultConsumer) {
        OplusLog.d(false, TAG, "onScrollRequested(scrollBounds=" + scrollBounds + ", requestRect=" + requestRect + ")");
        ScrollCaptureViewHelper.ScrollResult result = new ScrollCaptureViewHelper.ScrollResult();
        result.requestedArea = new Rect(requestRect);
        result.scrollDelta = this.mScrollDelta;
        result.availableArea = new Rect();
        if (!listView.isVisibleToUser() || listView.getChildCount() == 0) {
            OplusLog.w(TAG, "listView is empty or not visible, cannot continue");
            resultConsumer.accept(result);
            return;
        }
        Rect requestedContainerBounds = OplusScrollCaptureViewSupport.transformFromRequestToContainer(this.mScrollDelta, scrollBounds, requestRect);
        Rect recyclerLocalVisible = new Rect();
        listView.getLocalVisibleRect(recyclerLocalVisible);
        OplusLog.d(false, TAG, "recyclerLocalVisible=" + recyclerLocalVisible + ", scrollBounds=" + scrollBounds);
        Rect adjustedContainerBounds = new Rect(requestedContainerBounds);
        int remainingHeight = recyclerLocalVisible.height() - requestedContainerBounds.height();
        if (remainingHeight > 0) {
            adjustedContainerBounds.inset(0, (-remainingHeight) / 2);
        }
        int scrollAmount = OplusScrollCaptureViewSupport.computeScrollAmount(recyclerLocalVisible, adjustedContainerBounds);
        OplusLog.d(false, TAG, "scrollAmount: " + scrollAmount);
        if (scrollAmount < 0) {
            scrollAmount = 0;
            OplusLog.d(false, TAG, "About to scroll UP (content moves down within parent)");
        } else if (scrollAmount > 0) {
            OplusLog.d(false, TAG, "About to scroll DOWN (content moves up within parent)");
        }
        View refView = OplusScrollCaptureViewSupport.findScrollingReferenceView(listView, scrollAmount);
        int refTop = refView.getTop();
        listView.scrollListBy(scrollAmount);
        int scrollDistance = refTop - refView.getTop();
        OplusLog.d(false, TAG, "Parent view has scrolled vertically by " + scrollDistance + " px");
        int i = this.mScrollDelta + scrollDistance;
        this.mScrollDelta = i;
        result.scrollDelta = i;
        if (scrollDistance != 0) {
            OplusLog.d(false, TAG, "Scroll delta is now " + this.mScrollDelta + " px");
        }
        Rect requestedContainerBounds2 = new Rect(OplusScrollCaptureViewSupport.transformFromRequestToContainer(this.mScrollDelta, scrollBounds, requestRect));
        listView.getLocalVisibleRect(recyclerLocalVisible);
        if (requestedContainerBounds2.intersect(recyclerLocalVisible)) {
            result.availableArea = OplusScrollCaptureViewSupport.transformFromContainerToRequest(this.mScrollDelta, scrollBounds, requestedContainerBounds2);
        }
        resultConsumer.accept(result);
    }

    public void onPrepareForEnd(ListView listView) {
        listView.scrollListBy(-this.mScrollDelta);
        listView.setOverScrollMode(this.mOverScrollMode);
        listView.setVerticalScrollBarEnabled(this.mScrollBarWasEnabled);
    }
}
