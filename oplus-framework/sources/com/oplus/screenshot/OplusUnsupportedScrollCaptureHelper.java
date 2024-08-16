package com.oplus.screenshot;

import android.graphics.Rect;
import android.os.CancellationSignal;
import android.view.View;
import com.android.internal.view.ScrollCaptureViewHelper;
import com.oplus.util.OplusLog;
import java.util.function.Consumer;

/* loaded from: classes.dex */
public class OplusUnsupportedScrollCaptureHelper implements ScrollCaptureViewHelper<View> {
    public static final String TAG = "OplusUnsupportedScrollCaptureHelper";

    public boolean onAcceptSession(View view) {
        return false;
    }

    public void onPrepareForStart(View view, Rect scrollBounds) {
        OplusLog.w(TAG, "not supported");
    }

    public void onScrollRequested(View view, Rect scrollBounds, Rect requestRect, CancellationSignal cancellationSignal, Consumer<ScrollCaptureViewHelper.ScrollResult> resultConsumer) {
        OplusLog.w(TAG, "not supported");
        ScrollCaptureViewHelper.ScrollResult result = new ScrollCaptureViewHelper.ScrollResult();
        result.requestedArea = new Rect(requestRect);
        result.availableArea = new Rect();
        result.scrollDelta = 0;
        resultConsumer.accept(result);
    }

    public void onPrepareForEnd(View view) {
        OplusLog.w(TAG, "not supported");
    }
}
