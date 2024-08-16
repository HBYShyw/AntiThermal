package com.itgsa.opensdk.widget;

import android.util.Log;
import android.widget.OverScroller;
import android.widget.ScrollView;

/* loaded from: classes.dex */
public class ScrollViewWrapper {
    private static final String TAG = "ScrollViewWrapper";

    public static OverScroller getOverScrollerInScrollView(ScrollView scrollView) {
        try {
            Log.d(TAG, "getOverScrollerInScrollView calling");
            return scrollView.getWrapper().getOverScroller();
        } catch (Exception e) {
            Log.d(TAG, "error on getOverScrollerInScrollView " + e.getMessage());
            return null;
        }
    }
}
