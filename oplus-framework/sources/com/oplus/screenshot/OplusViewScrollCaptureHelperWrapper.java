package com.oplus.screenshot;

import android.graphics.Rect;
import android.os.CancellationSignal;
import android.view.View;
import com.android.internal.view.ScrollCaptureViewHelper;
import java.util.function.Consumer;

/* loaded from: classes.dex */
public class OplusViewScrollCaptureHelperWrapper<T extends View> implements ScrollCaptureViewHelper<T> {
    private final boolean mIsResetScroll;
    private int mOverScrollMode;
    private boolean mScrollBarEnabled;
    private final ScrollCaptureViewHelper<T> mWrapperd;

    public OplusViewScrollCaptureHelperWrapper(ScrollCaptureViewHelper<T> helper, boolean isResetScroll) {
        this.mWrapperd = helper;
        this.mIsResetScroll = isResetScroll;
    }

    public boolean onAcceptSession(T view) {
        return this.mWrapperd.onAcceptSession(view);
    }

    public Rect onComputeScrollBounds(T view) {
        return this.mWrapperd.onComputeScrollBounds(view);
    }

    public void onPrepareForStart(T view, Rect scrollBounds) {
        if (!this.mIsResetScroll) {
            this.mOverScrollMode = view.getOverScrollMode();
            this.mScrollBarEnabled = view.isVerticalScrollBarEnabled();
        }
        this.mWrapperd.onPrepareForStart(view, scrollBounds);
    }

    public void onScrollRequested(T view, Rect scrollBounds, Rect requestRect, CancellationSignal cancellationSignal, Consumer<ScrollCaptureViewHelper.ScrollResult> resultConsumer) {
        this.mWrapperd.onScrollRequested(view, scrollBounds, requestRect, cancellationSignal, resultConsumer);
    }

    public void onPrepareForEnd(T view) {
        if (this.mIsResetScroll) {
            this.mWrapperd.onPrepareForEnd(view);
        } else {
            view.setOverScrollMode(this.mOverScrollMode);
            view.setVerticalScrollBarEnabled(this.mScrollBarEnabled);
        }
    }

    public String toString() {
        return super.toString() + "@{" + this.mWrapperd.toString() + "}";
    }
}
