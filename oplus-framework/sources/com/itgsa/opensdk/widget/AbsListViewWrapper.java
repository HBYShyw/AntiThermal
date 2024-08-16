package com.itgsa.opensdk.widget;

import android.util.Log;
import android.widget.AbsListView;
import android.widget.OverScroller;

/* loaded from: classes.dex */
public class AbsListViewWrapper {
    private static final String TAG = "AbsListViewWrapper";

    public static OverScroller getOverScrollerInListView(AbsListView absListView) {
        try {
            absListView.getWrapper().setFlingRunnable();
            return absListView.getWrapper().getOverScroller();
        } catch (Exception e) {
            Log.d(TAG, "error on getOverScrollerInListView " + e.getMessage());
            return null;
        }
    }
}
