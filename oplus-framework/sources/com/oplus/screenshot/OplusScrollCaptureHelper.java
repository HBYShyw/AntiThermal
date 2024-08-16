package com.oplus.screenshot;

import android.graphics.Rect;
import android.os.CancellationSignal;
import android.util.MathUtils;
import android.view.View;
import com.android.internal.view.ScrollCaptureViewHelper;
import java.util.concurrent.Executor;
import java.util.function.Consumer;

/* loaded from: classes.dex */
public class OplusScrollCaptureHelper<V extends View> extends OplusScrollCaptureViewHelper<V> {
    private static final String TAG = "OplusScrollCaptureHelper";
    private final Executor mExecutor;
    private int mOriginScrollX;
    private int mOriginScrollY;
    private final IOplusScrollable<V> mScrollable;
    private final Rect mRequestWebViewLocal = new Rect();
    private final Rect mWebViewBounds = new Rect();

    public OplusScrollCaptureHelper(IOplusScrollable<V> scrollable, Executor executor) {
        this.mScrollable = scrollable;
        this.mExecutor = executor;
    }

    public boolean onAcceptSession(V view) {
        return view.isVisibleToUser() && this.mScrollable.getVerticalScrollRange(view) > this.mScrollable.getVerticalScrollExtent(view);
    }

    public void onPrepareForStart(V view, Rect scrollBounds) {
        this.mOriginScrollX = this.mScrollable.getHorizontalScrollOffset(view);
        this.mOriginScrollY = this.mScrollable.getVerticalScrollOffset(view);
    }

    public void onScrollRequested(final V view, Rect scrollBounds, Rect requestRect, CancellationSignal cancellationSignal, final Consumer<ScrollCaptureViewHelper.ScrollResult> resultConsumer) {
        int scrollY = this.mScrollable.getVerticalScrollOffset(view);
        int scrollDelta = scrollY - this.mOriginScrollY;
        final ScrollCaptureViewHelper.ScrollResult result = new ScrollCaptureViewHelper.ScrollResult();
        result.requestedArea = new Rect(requestRect);
        result.availableArea = new Rect();
        result.scrollDelta = scrollDelta;
        this.mWebViewBounds.set(0, 0, view.getWidth(), view.getHeight());
        if (view.isVisibleToUser()) {
            this.mRequestWebViewLocal.set(requestRect);
            this.mRequestWebViewLocal.offset(0, -scrollDelta);
            int upLimit = Math.min(0, -scrollY);
            int contentHeightPx = this.mScrollable.getVerticalScrollRange(view);
            int downLimit = Math.max(0, (contentHeightPx - this.mScrollable.getVerticalScrollExtent(view)) - scrollY);
            int scrollToCenter = this.mRequestWebViewLocal.centerY() - this.mWebViewBounds.centerY();
            final int scrollMovement = Math.max(MathUtils.constrain(scrollToCenter, upLimit, downLimit), 0);
            this.mScrollable.scrollBy(view, 0, scrollMovement);
            this.mExecutor.execute(new Runnable() { // from class: com.oplus.screenshot.OplusScrollCaptureHelper$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    OplusScrollCaptureHelper.this.lambda$onScrollRequested$0(view, result, scrollMovement, resultConsumer);
                }
            });
            return;
        }
        resultConsumer.accept(result);
    }

    public void onPrepareForEnd(View view) {
        this.mScrollable.scrollTo(view, this.mOriginScrollX, this.mOriginScrollY);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public IOplusScrollable<V> getScrollable() {
        return this.mScrollable;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: notifyResultConsumer, reason: merged with bridge method [inline-methods] */
    public void lambda$onScrollRequested$0(View view, ScrollCaptureViewHelper.ScrollResult result, int scrollMovement, Consumer<ScrollCaptureViewHelper.ScrollResult> resultConsumer) {
        int scrollDelta = this.mScrollable.getVerticalScrollOffset(view) - this.mOriginScrollY;
        this.mRequestWebViewLocal.offset(0, -scrollMovement);
        result.scrollDelta = scrollDelta;
        if (this.mRequestWebViewLocal.intersect(this.mWebViewBounds)) {
            result.availableArea = new Rect(this.mRequestWebViewLocal);
            result.availableArea.offset(0, result.scrollDelta);
        }
        resultConsumer.accept(result);
    }
}
