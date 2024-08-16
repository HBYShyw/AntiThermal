package com.oplus.screenshot;

import android.view.View;
import com.oplus.util.OplusLog;
import java.util.concurrent.Executor;

/* loaded from: classes.dex */
public class OplusAsyncScrollCaptureHelper<V extends View> extends OplusScrollCaptureHelper<V> {
    private static final String TAG = "OplusAsyncScrollCaptureHelper";

    public OplusAsyncScrollCaptureHelper(IOplusScrollable<V> scrollable, Executor executor) {
        super(scrollable, executor);
    }

    @Override // com.oplus.screenshot.OplusScrollCaptureHelper
    public boolean onAcceptSession(V view) {
        IOplusScrollable<V> scrollable = getScrollable();
        int scrollY = scrollable.getVerticalScrollOffset(view);
        if (scrollY == 0) {
            OplusLog.d(TAG, "vertical scroll offset is 0.");
            return false;
        }
        if (scrollY == 1) {
            OplusLog.d(TAG, "vertical scroll offset is 1.");
            scrollable.scrollBy(view, 0, -1);
        }
        return super.onAcceptSession(view);
    }
}
