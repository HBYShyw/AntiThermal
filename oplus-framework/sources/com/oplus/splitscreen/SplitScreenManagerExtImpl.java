package com.oplus.splitscreen;

import android.content.res.Resources;

/* loaded from: classes.dex */
public class SplitScreenManagerExtImpl implements ISplitScreenManagerExt {
    private static volatile SplitScreenManagerExtImpl sInstance = null;

    public static SplitScreenManagerExtImpl getInstance(Object base) {
        if (sInstance == null) {
            synchronized (SplitScreenManagerExtImpl.class) {
                if (sInstance == null) {
                    sInstance = new SplitScreenManagerExtImpl();
                }
            }
        }
        return sInstance;
    }

    private SplitScreenManagerExtImpl() {
    }

    public boolean hasLargeScreenFeature() {
        return OplusSplitScreenManager.getInstance().hasLargeScreenFeature();
    }

    public int getDividerInsets(Resources res) {
        return OplusSplitScreenManager.getInstance().getDividerInsets(res);
    }
}
