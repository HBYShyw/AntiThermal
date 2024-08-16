package com.android.internal.view;

import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.IViewWrapper;
import android.view.ScrollCaptureCallback;
import android.view.View;
import android.view.accessibility.AccessibilityNodeInfo;
import android.webkit.WebView;
import android.widget.ListView;
import android.widget.ScrollView;
import com.oplus.screenshot.OplusAsyncScrollCaptureHelper;
import com.oplus.screenshot.OplusHandlerExecutor;
import com.oplus.screenshot.OplusListViewCaptureHelper;
import com.oplus.screenshot.OplusRecyclerViewCaptureHelper;
import com.oplus.screenshot.OplusScrollCaptureHelper;
import com.oplus.screenshot.OplusScrollCaptureViewSupport;
import com.oplus.screenshot.OplusScrollViewCaptureHelper;
import com.oplus.screenshot.OplusScrollableViewCaptureHelper;
import com.oplus.screenshot.OplusUnsupportedScrollCaptureHelper;
import com.oplus.screenshot.OplusViewScrollCaptureHelperWrapper;
import com.oplus.screenshot.OplusViewScrollable;
import com.oplus.screenshot.OplusWebViewCaptureHelper;
import com.oplus.screenshot.OplusWebViewScrollable;
import com.oplus.util.OplusLog;

/* loaded from: classes.dex */
public class ScrollCaptureInternalExtImpl implements IScrollCaptureInternalExt {
    private static final boolean DEBUG = false;
    private static final String SCROLL_VIEW = "android.widget.ScrollView";
    private static final String TAG = "ScrollCaptureInternalExtImpl";
    private final ScrollCaptureInternal mScrollcaptureInternal;
    private final Bundle mMaps = new Bundle();
    private boolean mIsResetScroll = true;
    private long mCaptureDelay = 0;

    public ScrollCaptureInternalExtImpl(Object base) {
        this.mScrollcaptureInternal = (ScrollCaptureInternal) base;
    }

    public ScrollCaptureCallback requestCallback(View view, Rect localVisibleRect, Point positionInWindow) {
        OplusLog.v(false, TAG, "requestCallback(" + view + ", " + localVisibleRect + ", " + positionInWindow + ")");
        ScrollCaptureViewHelper<? extends View> viewHelper = requestCallbackCompact(view);
        if (viewHelper != null) {
            OplusLog.v(TAG, "class matched: " + view + " -> " + viewHelper);
        } else if (!this.mIsResetScroll) {
            viewHelper = requestCallbackOrigin(view, localVisibleRect, positionInWindow);
        }
        if (viewHelper == null) {
            return null;
        }
        return new OplusScrollCaptureViewSupport(view, wrapper(viewHelper));
    }

    public void setExtras(Bundle extras) {
        if (extras == null) {
            OplusLog.d(TAG, "setExtras: extras is null");
            this.mMaps.clear();
            this.mCaptureDelay = 0L;
            return;
        }
        if (extras.containsKey("scroll_capture_compatible")) {
            OplusLog.d(TAG, "setExtras: extras contains EXTRA_SCROLL_CAPTURE_COMPATIBLE");
            setScrollCaptureType(extras.getBundle("scroll_capture_compatible"));
        }
        if (extras.containsKey("scroll_reset")) {
            OplusLog.d(TAG, "setExtras: extras contains EXTRA_SCROLL_RESET");
            this.mIsResetScroll = extras.getBoolean("scroll_reset", true);
        } else {
            this.mIsResetScroll = true;
        }
        if (extras.containsKey("scroll_capture_delay")) {
            OplusLog.d(TAG, "setExtras: extras contains EXTRA_SCROLL_CAPTURE_DELAY");
            this.mCaptureDelay = extras.getLong("scroll_capture_delay", 0L);
        } else {
            this.mCaptureDelay = 0L;
        }
    }

    private ScrollCaptureViewHelper<? extends View> wrapper(ScrollCaptureViewHelper<? extends View> helper) {
        if (!this.mIsResetScroll) {
            return new OplusViewScrollCaptureHelperWrapper(helper, false);
        }
        return helper;
    }

    private ScrollCaptureViewHelper<? extends View> requestCallbackOrigin(View view, Rect localVisibleRect, Point positionInWindow) {
        int i = this.mScrollcaptureInternal.getWrapper().detectScrollingType(view);
        switch (i) {
            case 1:
                if (view instanceof ScrollView) {
                    return new OplusScrollViewCaptureHelper();
                }
                AccessibilityNodeInfo info = view.createAccessibilityNodeInfo();
                if (info != null && SCROLL_VIEW.equals(info.getClassName())) {
                    return new OplusScrollViewCaptureHelper();
                }
                return new OplusScrollableViewCaptureHelper();
            case 2:
                if (view instanceof ListView) {
                    return new OplusListViewCaptureHelper();
                }
                return new OplusRecyclerViewCaptureHelper();
            case 3:
                if (view instanceof WebView) {
                    return new OplusWebViewCaptureHelper();
                }
                return null;
            default:
                return null;
        }
    }

    private void setScrollCaptureType(Bundle maps) {
        this.mMaps.clear();
        if (maps != null && !maps.isEmpty()) {
            OplusLog.v(TAG, "compateable map setted.");
            this.mMaps.putAll(maps);
        }
    }

    private ScrollCaptureViewHelper<? extends View> requestCallbackCompact(View view) {
        String className = view.getClass().getName();
        if (!this.mMaps.containsKey(className)) {
            OplusLog.d(false, TAG, "requestCallback: " + className + " is not compatible.");
            return null;
        }
        OplusLog.v(false, TAG, "view:" + view.getClass().getClassLoader() + ", app:" + view.getContext().getApplicationContext().getClass().getClassLoader() + ", system:" + getClass().getClassLoader());
        int type = this.mMaps.getInt(className);
        if (!canScrollVertical(view, type)) {
            OplusLog.d(false, TAG, "requestCallback: " + view + " can not scroll vertical");
            return new OplusUnsupportedScrollCaptureHelper();
        }
        switch (type) {
            case 1000:
                return createWebViewHelper();
            case 1001:
                return createViewHelper();
            case 2000:
                return createAsyncWebViewHelper(this.mCaptureDelay);
            case 2001:
                return createAsyncViewHelper(this.mCaptureDelay);
            default:
                OplusLog.d(TAG, "requestCallback: unexcepted type: " + type);
                return new OplusUnsupportedScrollCaptureHelper();
        }
    }

    private boolean canScrollVertical(View view, int type) {
        if (!view.canScrollVertically(1) && !view.canScrollVertically(-1)) {
            debugLog("can not scroll down or up", view);
            return false;
        }
        IViewWrapper viewWrapper = view.getViewWrapper();
        if (viewWrapper.computeVerticalScrollOffset() != 0) {
            debugLog("scrollOffet != 0", view);
            return true;
        }
        debugLog("try scroll vertical:", view);
        view.scrollBy(0, 1);
        if (viewWrapper.computeVerticalScrollOffset() != 0) {
            return true;
        }
        if (type >= 2000) {
            OplusLog.i(TAG, "try async scroll! just return true");
            return true;
        }
        view.scrollBy(0, -1);
        debugLog("can not scroll:", view);
        return false;
    }

    private ScrollCaptureViewHelper<? extends View> createViewHelper() {
        return new OplusScrollCaptureHelper(new OplusViewScrollable(), new OplusHandlerExecutor());
    }

    private ScrollCaptureViewHelper<? extends View> createAsyncViewHelper(long captureDelay) {
        return new OplusAsyncScrollCaptureHelper(new OplusViewScrollable(), new OplusHandlerExecutor(captureDelay));
    }

    private ScrollCaptureViewHelper<? extends View> createWebViewHelper() {
        return new OplusScrollCaptureHelper(new OplusWebViewScrollable(), new OplusHandlerExecutor());
    }

    private ScrollCaptureViewHelper<? extends View> createAsyncWebViewHelper(long captureDelay) {
        return new OplusAsyncScrollCaptureHelper(new OplusWebViewScrollable(), new OplusHandlerExecutor(captureDelay));
    }

    private static void debugLog(String msg, View view) {
        IViewWrapper viewWrapper = view.getViewWrapper();
        OplusLog.v(false, TAG, "" + view + ": " + msg + " " + viewWrapper.computeVerticalScrollOffset() + ", " + viewWrapper.computeVerticalScrollExtent() + ", " + viewWrapper.computeVerticalScrollRange());
    }
}
