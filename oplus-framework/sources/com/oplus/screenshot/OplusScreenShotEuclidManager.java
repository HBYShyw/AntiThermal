package com.oplus.screenshot;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.IOplusLongshotWindowManager;
import android.view.OplusBaseLayoutParams;
import android.view.OplusLongshotWindowManager;
import android.view.WindowManager;
import com.oplus.util.OplusLog;
import com.oplus.util.OplusTypeCastingHelper;
import com.oplus.view.OplusWindowManager;

/* loaded from: classes.dex */
public class OplusScreenShotEuclidManager implements IOplusScreenShotEuclidManager {
    private static final boolean DBG = true;
    private static final String TAG = "LongshotDump/OplusScreenShotEuclidManager";
    private static volatile OplusScreenShotEuclidManager sInstance = null;

    public static OplusScreenShotEuclidManager getInstance() {
        if (sInstance == null) {
            synchronized (OplusScreenShotEuclidManager.class) {
                if (sInstance == null) {
                    sInstance = new OplusScreenShotEuclidManager();
                }
            }
        }
        return sInstance;
    }

    OplusScreenShotEuclidManager() {
    }

    @Override // com.oplus.screenshot.IOplusScreenShotEuclidManager
    public boolean updateSpecialSystemBar(WindowManager.LayoutParams lp) {
        return OplusWindowManager.isUseLastStatusBarTint(lp) || OplusWindowManager.updateDarkNavigationBar(lp);
    }

    @Override // com.oplus.screenshot.IOplusScreenShotEuclidManager
    public boolean skipSystemUiVisibility(WindowManager.LayoutParams lp) {
        OplusBaseLayoutParams cbp = (OplusBaseLayoutParams) OplusTypeCastingHelper.typeCasting(OplusBaseLayoutParams.class, lp);
        if (cbp == null) {
            return false;
        }
        boolean result = cbp.mOplusLayoutParams.getSkipSystemUiVisibility();
        return result;
    }

    @Override // com.oplus.screenshot.IOplusScreenShotEuclidManager
    public boolean isSpecialAppWindow(boolean appWindow, WindowManager.LayoutParams attrs) {
        if (OplusWindowManager.LayoutParams.isFullscreen(attrs) && OplusWindowManager.LayoutParams.isLongshotWindow(attrs.type)) {
            return true;
        }
        return appWindow;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.oplus.screenshot.IOplusScreenShotEuclidManager
    public boolean takeScreenshot(Context context, int screenshotType, boolean hasStatus, boolean hasNav, Handler handler) {
        OplusScreenshotManager sm = OplusLongshotUtils.getScreenshotManager(context);
        if (sm == null || !sm.isScreenshotSupported()) {
            OplusLog.e(true, TAG, "takeScreenshot : FAIL sm = " + sm + " , " + (sm == null ? false : sm.isScreenshotSupported()));
            return false;
        }
        Bundle extras = new Bundle();
        if (handler instanceof IOplusScreenshotHelper) {
            IOplusScreenshotHelper helper = (IOplusScreenshotHelper) handler;
            extras.putString(OplusScreenshotManager.SCREENSHOT_SOURCE, helper.getSource());
            extras.putBoolean(OplusScreenshotManager.GLOBAL_ACTION_VISIBLE, helper.isGlobalAction());
        }
        extras.putBoolean(OplusScreenshotManager.SCREENSHOT_ORIENTATION, isLandscape(context));
        sm.takeScreenshot(extras);
        OplusLog.d(true, TAG, "takeScreenshot : PASS");
        return true;
    }

    @Override // com.oplus.screenshot.IOplusScreenShotEuclidManager
    public Handler getScreenShotHandler(Looper looper) {
        return new OplusGlobalActionHandler(Looper.getMainLooper());
    }

    @Override // com.oplus.screenshot.IOplusScreenShotEuclidManager
    public IOplusLongshotWindowManager getIOplusLongshotWindowManager() {
        return new OplusLongshotWindowManager();
    }

    private boolean isLandscape(Context context) {
        OplusScreenshotManager sm = OplusLongshotUtils.getScreenshotManager(context);
        if (sm.isLaunching()) {
            return sm.getRatation() == 1 || sm.getRatation() == 3;
        }
        Configuration configuration = context.getResources().getConfiguration();
        return configuration.orientation == 2;
    }
}
