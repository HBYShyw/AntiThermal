package com.oplus.wrapper.app;

import android.app.ActivityManager;
import android.app.ActivityTaskManager;
import android.content.Context;
import android.util.Singleton;
import java.util.List;

/* loaded from: classes.dex */
public class ActivityTaskManager {
    private static final Singleton<ActivityTaskManager> SINSTANCE = new Singleton<ActivityTaskManager>() { // from class: com.oplus.wrapper.app.ActivityTaskManager.1
        /* JADX INFO: Access modifiers changed from: protected */
        /* renamed from: create, reason: merged with bridge method [inline-methods] */
        public ActivityTaskManager m1144create() {
            ActivityTaskManager activityTaskManager = new ActivityTaskManager();
            activityTaskManager.mActivityTaskManager = android.app.ActivityTaskManager.getInstance();
            return activityTaskManager;
        }
    };
    private android.app.ActivityTaskManager mActivityTaskManager;

    private ActivityTaskManager() {
    }

    public static ActivityTaskManager getInstance() {
        return (ActivityTaskManager) SINSTANCE.get();
    }

    public List<ActivityManager.RecentTaskInfo> getRecentTasks(int maxNum, int flags, int userId) {
        return this.mActivityTaskManager.getRecentTasks(maxNum, flags, userId);
    }

    public List<ActivityManager.RunningTaskInfo> getTasks(int maxNum) {
        return this.mActivityTaskManager.getTasks(maxNum);
    }

    public List<ActivityManager.RunningTaskInfo> getTasks(int maxNum, boolean filterOnlyVisibleRecents) {
        return this.mActivityTaskManager.getTasks(maxNum, filterOnlyVisibleRecents);
    }

    public static boolean supportsMultiWindow(Context context) {
        return android.app.ActivityTaskManager.supportsMultiWindow(context);
    }

    /* loaded from: classes.dex */
    public static class RootTaskInfo {
        private final ActivityTaskManager.RootTaskInfo mRootTaskInfo;

        public RootTaskInfo(ActivityTaskManager.RootTaskInfo rootTaskInfo) {
            this.mRootTaskInfo = rootTaskInfo;
        }

        public ActivityTaskManager.RootTaskInfo getRootTaskInfo() {
            return this.mRootTaskInfo;
        }
    }
}
