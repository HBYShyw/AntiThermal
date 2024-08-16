package com.oplus.wrapper.app;

import android.app.ActivityManager;
import android.graphics.Point;
import android.graphics.Rect;
import com.oplus.wrapper.window.WindowContainerToken;

/* loaded from: classes.dex */
public class ActivityManager {
    private final android.app.ActivityManager mActivityManager;

    public ActivityManager(android.app.ActivityManager activityManager) {
        this.mActivityManager = activityManager;
    }

    public static int getCurrentUser() {
        return android.app.ActivityManager.getCurrentUser();
    }

    public void forceStopPackage(String packageName) {
        this.mActivityManager.forceStopPackage(packageName);
    }

    /* loaded from: classes.dex */
    public static class RecentTaskInfo {
        private final ActivityManager.RecentTaskInfo mRecentTaskInfo;

        /* loaded from: classes.dex */
        public static class PersistedTaskSnapshotData {
            private final ActivityManager.RecentTaskInfo.PersistedTaskSnapshotData mPersistedTaskSnapshotData;

            public PersistedTaskSnapshotData() {
                this.mPersistedTaskSnapshotData = new ActivityManager.RecentTaskInfo.PersistedTaskSnapshotData();
            }

            public PersistedTaskSnapshotData(ActivityManager.RecentTaskInfo.PersistedTaskSnapshotData persistedTaskSnapshotData) {
                this.mPersistedTaskSnapshotData = persistedTaskSnapshotData;
            }

            ActivityManager.RecentTaskInfo.PersistedTaskSnapshotData getPersistedTaskSnapshotData() {
                return this.mPersistedTaskSnapshotData;
            }

            public void set(PersistedTaskSnapshotData other) {
                this.mPersistedTaskSnapshotData.set(other.getPersistedTaskSnapshotData());
            }

            public Rect getContentInsets() {
                return this.mPersistedTaskSnapshotData.contentInsets;
            }

            public void setContentInsets(Rect rect) {
                this.mPersistedTaskSnapshotData.contentInsets = rect;
            }

            public Point getTaskSize() {
                return this.mPersistedTaskSnapshotData.taskSize;
            }

            public void setTaskSize(Point point) {
                this.mPersistedTaskSnapshotData.taskSize = point;
            }
        }

        public RecentTaskInfo() {
            this.mRecentTaskInfo = new ActivityManager.RecentTaskInfo();
        }

        public RecentTaskInfo(ActivityManager.RecentTaskInfo recentTaskInfo) {
            this.mRecentTaskInfo = recentTaskInfo;
        }

        public PersistedTaskSnapshotData getLastSnapshotData() {
            return new PersistedTaskSnapshotData(this.mRecentTaskInfo.lastSnapshotData);
        }

        public void setLastSnapshotData(PersistedTaskSnapshotData persistedTaskSnapshotData) {
            this.mRecentTaskInfo.lastSnapshotData = persistedTaskSnapshotData.mPersistedTaskSnapshotData;
        }

        /* loaded from: classes.dex */
        public static class RunningTaskInfo {
            private final ActivityManager.RunningTaskInfo mRunningTaskInfo;

            public RunningTaskInfo(ActivityManager.RunningTaskInfo runningTaskInfo) {
                this.mRunningTaskInfo = runningTaskInfo;
            }

            public WindowContainerToken getToken() {
                android.window.WindowContainerToken token = this.mRunningTaskInfo.token;
                if (token == null) {
                    return null;
                }
                return new WindowContainerToken(token);
            }
        }
    }
}
