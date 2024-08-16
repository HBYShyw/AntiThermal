package com.oplus.splitscreen;

import android.app.ActivityManager;
import android.app.TaskInfo;

/* loaded from: classes.dex */
public class OplusRunningTaskInfo {
    private boolean mSupportsSplitScreenMultiWindow;

    public OplusRunningTaskInfo() {
    }

    public OplusRunningTaskInfo(ActivityManager.RunningTaskInfo info) {
        this.mSupportsSplitScreenMultiWindow = info.supportsSplitScreenMultiWindow && info.supportsMultiWindow;
    }

    public void updateFrom(TaskInfo info) {
        this.mSupportsSplitScreenMultiWindow = info.supportsSplitScreenMultiWindow && info.supportsMultiWindow;
    }

    public boolean getIsSupportSplitScreenMultiWindow() {
        return this.mSupportsSplitScreenMultiWindow;
    }

    public void setIsSupportSplitScreenMultiWindow(boolean supportsSplitScreenMultiWindow) {
        this.mSupportsSplitScreenMultiWindow = supportsSplitScreenMultiWindow;
    }
}
