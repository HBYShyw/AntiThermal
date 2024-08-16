package com.oplus.oms.split.splitload;

import com.oplus.oms.split.common.SplitLog;

/* loaded from: classes.dex */
public class SplitLoadStrategy {
    private static final String TAG = "SplitLoadStrategy";
    private static int mClassLoader;

    public static void setMultiSplitLoadStrategy(int splitLoadMode) {
        if (splitLoadMode != 1 && splitLoadMode != 2) {
            mClassLoader = 1;
            SplitLog.w(TAG, "splitLoadMode is " + splitLoadMode + ", the setting parameter is wrong. take the default 1", new Object[0]);
        } else {
            mClassLoader = splitLoadMode;
        }
    }

    public static int getSplitLoadStrategy() {
        return mClassLoader;
    }
}
