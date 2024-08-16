package com.oplus.screenshot;

import android.common.IOplusCommonFeature;
import android.common.OplusFeatureList;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.IOplusLongshotWindowManager;
import android.view.WindowManager;

/* loaded from: classes.dex */
public interface IOplusScreenShotEuclidManager extends IOplusCommonFeature {
    public static final IOplusScreenShotEuclidManager DEFAULT = new IOplusScreenShotEuclidManager() { // from class: com.oplus.screenshot.IOplusScreenShotEuclidManager.1
    };

    default IOplusScreenShotEuclidManager getDefault() {
        return DEFAULT;
    }

    default OplusFeatureList.OplusIndex index() {
        return OplusFeatureList.OplusIndex.IOplusScreenShotEuclidManager;
    }

    default boolean updateSpecialSystemBar(WindowManager.LayoutParams lp) {
        return false;
    }

    default boolean skipSystemUiVisibility(WindowManager.LayoutParams lp) {
        return false;
    }

    default boolean isSpecialAppWindow(boolean appWindow, WindowManager.LayoutParams attrs) {
        return appWindow;
    }

    default boolean takeScreenshot(Context context, int screenshotType, boolean hasStatus, boolean hasNav, Handler handler) {
        return false;
    }

    default Handler getScreenShotHandler(Looper looper) {
        return new Handler(Looper.getMainLooper());
    }

    default IOplusLongshotWindowManager getIOplusLongshotWindowManager() {
        return null;
    }
}
