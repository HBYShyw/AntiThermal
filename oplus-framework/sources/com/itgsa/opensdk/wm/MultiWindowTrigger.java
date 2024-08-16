package com.itgsa.opensdk.wm;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import com.oplus.splitscreen.OplusSplitScreenManager;

/* loaded from: classes.dex */
public class MultiWindowTrigger {
    private static final int SDK_VERSION = 10000;

    public boolean isDeviceSupport(Context context) {
        return OplusSplitScreenManager.getInstance().hasLargeScreenFeature();
    }

    public int getVersion() {
        return 10000;
    }

    public boolean requestSwitchToSplitScreen(Activity requestActivity, SplitScreenParams params) {
        if (params == null) {
            return false;
        }
        Intent launchIntent = params.getLaunchIntent();
        PendingIntent pendingIntent = null;
        if (launchIntent != null) {
            pendingIntent = PendingIntent.getActivity(requestActivity, 0, launchIntent, 1140850688, null);
        }
        PendingIntent pendingIntent2 = pendingIntent;
        com.oplus.app.SplitScreenParams innerParams = new com.oplus.app.SplitScreenParams(params.isSelfSplit(), pendingIntent2, launchIntent, params.getLaunchPosition());
        return OplusSplitScreenManager.getInstance().requestSwitchToSplitScreen(requestActivity, innerParams);
    }

    public boolean requestSwitchToFullScreen(Activity requestActivity) {
        return OplusSplitScreenManager.getInstance().requestSwitchToFullScreen(requestActivity);
    }

    public void registerActivityMultiWindowAllowanceObserver(Activity activity, ActivityMultiWindowAllowanceObserver observer) {
        OplusSplitScreenManager.getInstance().registerActivityMultiWindowAllowanceObserver(activity, observer.asObserver());
    }

    public void unregisterActivityMultiWindowAllowanceObserver(Activity activity, ActivityMultiWindowAllowanceObserver observer) {
        OplusSplitScreenManager.getInstance().unregisterActivityMultiWindowAllowanceObserver(activity, observer.asObserver());
    }
}
