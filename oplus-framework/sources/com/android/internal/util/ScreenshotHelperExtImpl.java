package com.android.internal.util;

import android.common.OplusFeatureCache;
import android.content.Context;
import android.os.Handler;
import com.oplus.screenshot.IOplusScreenShotEuclidManager;

/* loaded from: classes.dex */
public class ScreenshotHelperExtImpl implements IScreenshotHelperExt {
    private final ScreenshotHelper mScreenshotHelper;

    public ScreenshotHelperExtImpl(Object base) {
        this.mScreenshotHelper = (ScreenshotHelper) base;
    }

    public boolean takeScreenshotNeedReturn(Context context, int screenshotType, ScreenshotRequest screenshotRequest, Handler handler) {
        return ((IOplusScreenShotEuclidManager) OplusFeatureCache.getOrCreate(IOplusScreenShotEuclidManager.DEFAULT, new Object[0])).takeScreenshot(context, screenshotType, true, true, handler);
    }
}
