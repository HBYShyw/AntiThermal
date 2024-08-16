package com.android.server.wm;

import android.app.ActivityOptions;
import android.content.pm.ActivityInfo;
import android.graphics.Rect;
import android.os.SystemProperties;
import com.android.server.wm.ActivityStarter;
import com.android.server.wm.LaunchParamsController;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public class DesktopModeLaunchParamsModifier implements LaunchParamsController.LaunchParamsModifier {
    private static final boolean DEBUG = false;
    private static final int DESKTOP_MODE_DEFAULT_HEIGHT_DP;
    private static final int DESKTOP_MODE_DEFAULT_WIDTH_DP;
    static final boolean DESKTOP_MODE_SUPPORTED;
    private static final String TAG = "ActivityTaskManager";
    private StringBuilder mLogBuilder;

    private void appendLog(String str, Object... objArr) {
    }

    private void initLogBuilder(Task task, ActivityRecord activityRecord) {
    }

    private void outputLog() {
    }

    static {
        DESKTOP_MODE_SUPPORTED = SystemProperties.getBoolean("persist.wm.debug.desktop_mode", false) || SystemProperties.getBoolean("persist.wm.debug.desktop_mode_2", false);
        DESKTOP_MODE_DEFAULT_WIDTH_DP = SystemProperties.getInt("persist.wm.debug.desktop_mode.default_width", 840);
        DESKTOP_MODE_DEFAULT_HEIGHT_DP = SystemProperties.getInt("persist.wm.debug.desktop_mode.default_height", 630);
    }

    @Override // com.android.server.wm.LaunchParamsController.LaunchParamsModifier
    public int onCalculate(Task task, ActivityInfo.WindowLayout windowLayout, ActivityRecord activityRecord, ActivityRecord activityRecord2, ActivityOptions activityOptions, ActivityStarter.Request request, int i, LaunchParamsController.LaunchParams launchParams, LaunchParamsController.LaunchParams launchParams2) {
        initLogBuilder(task, activityRecord);
        int calculate = calculate(task, windowLayout, activityRecord, activityRecord2, activityOptions, request, i, launchParams, launchParams2);
        outputLog();
        return calculate;
    }

    private int calculate(Task task, ActivityInfo.WindowLayout windowLayout, ActivityRecord activityRecord, ActivityRecord activityRecord2, ActivityOptions activityOptions, ActivityStarter.Request request, int i, LaunchParamsController.LaunchParams launchParams, LaunchParamsController.LaunchParams launchParams2) {
        if (task == null) {
            appendLog("task null, skipping", new Object[0]);
            return 0;
        }
        if (i != 3) {
            appendLog("not in bounds phase, skipping", new Object[0]);
            return 0;
        }
        if (!task.isActivityTypeStandard()) {
            appendLog("not standard activity type, skipping", new Object[0]);
            return 0;
        }
        if (!launchParams.mBounds.isEmpty()) {
            appendLog("currentParams has bounds set, not overriding", new Object[0]);
            return 0;
        }
        launchParams2.set(launchParams);
        float f = task.getConfiguration().densityDpi / 160.0f;
        int i2 = (int) ((DESKTOP_MODE_DEFAULT_WIDTH_DP * f) + 0.5f);
        Rect rect = launchParams2.mBounds;
        rect.right = i2;
        rect.bottom = (int) ((DESKTOP_MODE_DEFAULT_HEIGHT_DP * f) + 0.5f);
        Rect bounds = task.getWindowConfiguration().getBounds();
        launchParams2.mBounds.offset(bounds.centerX() - launchParams2.mBounds.centerX(), bounds.centerY() - launchParams2.mBounds.centerY());
        appendLog("setting desktop mode task bounds to %s", launchParams2.mBounds);
        return 1;
    }
}
