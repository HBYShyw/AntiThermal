package com.oplus.autolayout;

import android.common.OplusFeatureCache;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.autolayout.IOplusAutoLayoutManager;

/* loaded from: classes.dex */
public class OplusAutoLayoutAdapterExtImpl implements IAutoLayoutAdapterExt {
    private static volatile OplusAutoLayoutAdapterExtImpl sInstance = null;

    private OplusAutoLayoutAdapterExtImpl() {
    }

    public static OplusAutoLayoutAdapterExtImpl getInstance(Object base) {
        if (sInstance == null) {
            synchronized (OplusAutoLayoutAdapterExtImpl.class) {
                if (sInstance == null) {
                    sInstance = new OplusAutoLayoutAdapterExtImpl();
                }
            }
        }
        return sInstance;
    }

    private IOplusAutoLayoutManager getAutoLayoutManager() {
        return (IOplusAutoLayoutManager) OplusFeatureCache.getOrCreate(IOplusAutoLayoutManager.mDefault, new Object[0]);
    }

    public void onAttachedToWindow(View recyclerView, Object layoutManager) {
        getAutoLayoutManager().onAttachedToWindow(recyclerView, layoutManager);
    }

    public int getSpanSize(Object obj, int in) {
        return getAutoLayoutManager().getSpanSize(obj, in);
    }

    public boolean checkFindItemPositions(Object obj, int[] iArr) {
        return getAutoLayoutManager().checkFindItemPositions(obj, iArr);
    }

    public int[] findFirstVisibleItemPositions(Object obj, int[] iArr) {
        return getAutoLayoutManager().findFirstVisibleItemPositions(obj, iArr);
    }

    public int[] findFirstCompletelyVisibleItemPositions(Object obj, int[] iArr) {
        return getAutoLayoutManager().findFirstCompletelyVisibleItemPositions(obj, iArr);
    }

    public int[] findLastVisibleItemPositions(Object obj, int[] iArr) {
        return getAutoLayoutManager().findLastVisibleItemPositions(obj, iArr);
    }

    public int[] findLastCompletelyVisibleItemPositions(Object obj, int[] iArr) {
        return getAutoLayoutManager().findLastCompletelyVisibleItemPositions(obj, iArr);
    }

    public void getItemOffsets(Rect outRect, View view, View parent, Object state) {
        getAutoLayoutManager().getItemOffsets(outRect, view, parent, state);
    }

    public void attachViewPagerToWindow(View viewPager) {
        getAutoLayoutManager().attachViewPagerToWindow(viewPager);
    }

    public DisplayMetrics setDisplayMetrics(DisplayMetrics dm) {
        return getAutoLayoutManager().setDisplayMetrics(dm);
    }

    public void onTinkerSuccess(Object obj) {
        getAutoLayoutManager().onTinkerSuccess(obj);
    }
}
