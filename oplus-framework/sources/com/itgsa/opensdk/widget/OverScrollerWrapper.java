package com.itgsa.opensdk.widget;

import android.util.Log;
import android.widget.OverScroller;

/* loaded from: classes.dex */
public class OverScrollerWrapper {
    private static final int FLING_MODE = 1;
    private static final int SCROLL_MODE = 0;
    private static final String TAG = "OverScrollerWrapper";

    public static int getModeInOverScroller(OverScroller overScroller) {
        try {
            Log.d(TAG, "getModeInOverScroller calling");
            return overScroller.getWrapper().getMode();
        } catch (Exception e) {
            Log.d(TAG, "error on getModeInOverScroller " + e.getMessage());
            return 0;
        }
    }
}
